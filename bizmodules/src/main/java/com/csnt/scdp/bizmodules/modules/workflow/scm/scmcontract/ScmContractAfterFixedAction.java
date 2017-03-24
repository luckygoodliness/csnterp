package com.csnt.scdp.bizmodules.modules.workflow.scm.scmcontract;

import com.csnt.scdp.bizmodules.entity.fad.FadRtfreevalue;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractAttribute;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.impl.CashreqManagerImpl;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_contract_prepare-after-fixed")
@Transactional

public class ScmContractAfterFixedAction extends ERPWorkFlowBaseVariableCollectionAction {
    @Resource(name = "scmcontract-manager")
    private ScmcontractManager scmcontractManager;

    @Resource(name = "cashreq-manager")
    private CashreqManagerImpl cashreqManagerImpl;

    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map dataInfo = super.perform(inMap);
        Map mapVar = new HashMap<>();
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        ScmContract scmContract = scmcontractManager.approved(businessKey);
        //如果包号不为空，则更新包号
        String purchasePackageId = scmContract.getPurchasePackageId();
        if (StringUtil.isNotEmpty(purchasePackageId)) {
            prmpurchasereqManager.updatePackageState(purchasePackageId);
        }
        //1.生成采购请款单，（如果是个人报销和其他，则不生成采购请款单）
        String scmContractPayType = scmContract.getContractPayType();
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        if (ScmContractAttribute.CONTRACT_PAY_TYPE_VALUE_1.equals(scmContractPayType) || ScmContractAttribute.CONTRACT_PAY_TYPE_VALUE_2.equals(scmContractPayType) || ScmContractAttribute.CONTRACT_PAY_TYPE_VALUE_5.equals(scmContractPayType)) {
            BigDecimal totalValue = scmContract.getTotalValue();
            if (!MathUtil.isNullOrZero(totalValue)) {
                List<ScmContract> list = new ArrayList<ScmContract>();
                list.add(scmContract);
                if (scmContract.getTotalValue().compareTo(new BigDecimal("0.00")) == 1) {
                    cashreqManagerImpl.generateScmCashreqByScmContract(list);
                }
            }
        }
        scmContract.setState("2");
        pcm.update(scmContract);

        String supplierCode = (String) dataInfo.get("supplierCode");
        String supplierName = (String) dataInfo.get("supplierName");

        if(StringUtil.isNotEmpty(supplierCode)){
            ScmSupplier scmSupplier = PersistenceFactory.getInstance().findByPK(ScmSupplier.class, supplierCode);
            String uuid = null;
            String completeName = null;
            String simpleName = null;
            String ncCode = null;
            if (null!=scmSupplier) {
                uuid = scmSupplier.getUuid();
                completeName = scmSupplier.getCompleteName();
                simpleName = scmSupplier.getSimpleName();
                ncCode = scmSupplier.getNcCode();
            }

            Map accountCondition = new HashMap<>();
            accountCondition.put("accountInfoName", supplierName);
            List<FadRtfreevalue> fadRtfreevalues = PersistenceFactory.getInstance().findByAnyFields(FadRtfreevalue.class, accountCondition, null);
            if (ListUtil.isEmpty(fadRtfreevalues)) {
                //nc表没有供应商
                sendMsg(uuid, completeName, simpleName, supplierCode, "noSupplier");
            } else {
                //如果供应商表ncCode为空，发送消息，
                // 此功能暂时不需要
//                if (StringUtil.isEmpty(ncCode)) {
//                    sendMsg(uuid, completeName, simpleName, supplierCode, "noNcCode");
//                }
            }
        }

        return mapVar;
    }

    private void sendMsg(String uuid, String completeName, String simpleName, String supplierCode, String type) {
        String sql = "SELECT * FROM V_USER_ROLES T WHERE ROLE_NAME ='出纳'";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List<Map<String, Object>> lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        List<String> userIdLst = new ArrayList<String>();
        ReceiptsMeta receiptsMeta = new ReceiptsMeta();
        if (ListUtil.isNotEmpty(lstUserInfo)) {
            for (int i = 0; i < lstUserInfo.size(); i++) {
                String userId = (String) lstUserInfo.get(i).get("userId");
                userIdLst.add(userId);
            }
            receiptsMeta.setLstSendToUserId(userIdLst);
        }
        String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
        String templateNo = null;
        if (type.equals("noSupplier")) {
            templateNo = "NC_SUPPLIER_INFO_CHECKED";
        } else {
            templateNo = "SUPPLIER_INFO_CHECKED";
        }
        Map map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("completeName", completeName);
        map.put("simpleName", simpleName);
        map.put("supplierCode", supplierCode);
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);

    }
}
