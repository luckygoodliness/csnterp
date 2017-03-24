package com.csnt.scdp.bizmodules.modules.workflow.scm.supplierlimitchange;

import com.csnt.scdp.bizmodules.entity.fad.FadRtfreevalue;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplierLimitChangeD;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractAttribute;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.impl.CashreqManagerImpl;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.bizmodules.modules.scm.scmsupplierlimitchange.services.intf.ScmsupplierlimitchangeManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
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
@Controller("scm_supplier_limit_change-after-fixed")
@Transactional

public class ScmSupplierLimitChangeAfterFixedAction extends ERPWorkFlowBaseVariableCollectionAction {
    @Resource(name = "scmsupplierlimitchange-manager")
    private ScmsupplierlimitchangeManager scmsupplierlimitchangeManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map dataInfo = super.perform(inMap);
        Map mapVar = new HashMap<>();
        String SupplierLimitChangeDID = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        if (StringUtil.isNotEmpty(SupplierLimitChangeDID)) {
            scmsupplierlimitchangeManager.replaceMxxAmount(SupplierLimitChangeDID);
        }
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map conditionMap = new HashMap<>();
        conditionMap.put("scmSupplierLimitChangeId", SupplierLimitChangeDID);
        List<ScmSupplierLimitChangeD> scmSupplierLimitChangeDList = pcm.findByAnyFields(ScmSupplierLimitChangeD.class, conditionMap, null);
        conditionMap.remove("scmSupplierLimitChangeId");
        if (ListUtil.isNotEmpty(scmSupplierLimitChangeDList)) {
            List<String> supplierId = new ArrayList<>();
            for (int i = 0; i < scmSupplierLimitChangeDList.size(); i++) {
                supplierId.add(scmSupplierLimitChangeDList.get(i).getScmSupplierId());
            }
            conditionMap.put("uuid",supplierId);
            List<ScmSupplier> ScmSupplierList = pcm.findByAnyFields(ScmSupplier.class, conditionMap, null);
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < ScmSupplierList.size(); i++) {
                map.put(ScmSupplierList.get(i).getUuid(), ScmSupplierList.get(i).getCompleteName());
            }
            for (int i = 0; i < scmSupplierLimitChangeDList.size(); i++) {
                sendMsg(map.get(scmSupplierLimitChangeDList.get(i).getScmSupplierId()), scmSupplierLimitChangeDList.get(i).getMaxVolume(), scmSupplierLimitChangeDList.get(i).getMaxAmount());
            }
        }
        return mapVar;
    }

    private void sendMsg( String completeName, Long maxVolume, BigDecimal maxAmount) {
        String sql = "SELECT * FROM SCM_CONTRACT S WHERE S.SUPPLIER_NAME='"+completeName+"' AND S.STATE='0'";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List<Map<String, Object>> lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        List<String> userIdLst = new ArrayList<String>();
        ReceiptsMeta receiptsMeta = new ReceiptsMeta();
        if (ListUtil.isNotEmpty(lstUserInfo)) {
            for (int i = 0; i < lstUserInfo.size(); i++) {
                String userId = (String) lstUserInfo.get(i).get("createBy");
                userIdLst.add(userId);
            }
            receiptsMeta.setLstSendToUserId(userIdLst);
        }
        String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
        String templateNo = "SCM_SUPPLIER_LIMIT_CHANGE";
        Map map = new HashMap<>();
        map.put("completeName", completeName);
        map.put("maxVolume", maxVolume);
        map.put("maxAmount", maxAmount);
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);
    }
}
