package com.csnt.scdp.bizmodules.modules.workflow.scm.scmcontract;

import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.impl.CashreqManagerImpl;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.framework.attributes.BeanAttribute;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.core.transaction.TransCallback;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.TransHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

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
@Controller("scm_contract_prepare-after-start")
@Transactional
public class ScmContractAfterStartAction implements IAction {
    @Resource(name = "scmcontract-manager")
    private ScmcontractManager scmcontractManager;
    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;
    @Resource(name = "cashreq-manager")
    private CashreqManagerImpl cashreqManagerImpl;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //合同状态(0、合同谈判1、合同编辑2、合同明细导入3、合同审批4、审批通过5、盖章生效6、合同扫描件上传7、合同废止)
        Map map = new HashedMap();
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        String subMethod = (String) inMap.get("subMethod");
        Integer contractState = 0;
        Integer state = 0;
        Integer isClosed = 0;//零星采购直接结算
        if ("complete".equals(subMethod)) {
            String status = (String) inMap.get("status");
            if ("PROCESSING".equals(status)) {
                contractState = 3;//已提交
                state = 1;//已提交
            } else if ("FIXED".equals(status)) {
                contractState = 4;//已审核
                state = 2;//已审核
                ScmContract scmContract = scmcontractManager.approved(businessKey);
                //如果包号不为空，则更新包号
                String purchasePackageId = scmContract.getPurchasePackageId();
                if (StringUtil.isNotEmpty(purchasePackageId)) {
                    prmpurchasereqManager.updatePackageState(purchasePackageId);
                }
            }
        } else if ("start".equals(subMethod)) {
            Validation(businessKey);//提交前校验 todo
            if (ScmContractPrepareStart.isDirectlyEffective(businessKey)) {//第一步直接生效
                state = 2;//已审核
                contractState = 4;//审核通过
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
                        if (!scmContract.getState().equals("2")) {
                            if (scmContract.getTotalValue().compareTo(new BigDecimal("0.00")) == 1) {
                                cashreqManagerImpl.generateScmCashreqByScmContract(list);
                            }
                        }
                    }
                }
                scmContract.setState("2");
                pcm.update(scmContract);
            } else {
                contractState = 3;//已提交
                state = 1;//已提交
            }
        }
        inMap.put(WorkFlowAttribute.SKIP_COMMON_PROCESS_AFTER_ACTION, true);
        if (state == 1) {
            String sql = "";
            List lstParam = new ArrayList();
            sql = "update scm_contract set contract_state = ?,state=? where uuid = ?";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            lstParam.add(contractState);
            lstParam.add(state);
            lstParam.add(businessKey);
            daoMeta.setLstParams(lstParam);
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            pcm.updateByNativeSQL(daoMeta);
        }

        //移动端发送消息提醒
        ErpWorkFlowHelper.pushMsgToMobile(inMap);

        return map;
    }

    public void Validation(String uuid) {
        List paramList = new ArrayList<>();
        paramList.add(uuid);
        paramList.add(uuid);
        paramList.add(uuid);
        DAOMeta daoMeta = DAOHelper.getDAO("scmcontract-dao", "get_scm_supplier_limit", paramList);
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        List<Map> detailList = (List) pcm.findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(detailList)) {
            Map m = detailList.get(0);
            String supplierId = "" + m.get("scmSupplierId");
            String supplierName = "" + m.get("supplierName");
            int maxVolume = Integer.parseInt("" + m.get("maxVolume"));//最大数量
            BigDecimal maxAmount = new BigDecimal("" + m.get("maxAmount"));//最大金额
            int uVolume = Integer.parseInt("" + m.get("uVolume"));//目前数量（state=2）
            BigDecimal uAmount = new BigDecimal("" + m.get("uAmount"));//目前金额（state=2）
            BigDecimal amount = new BigDecimal("" + m.get("amount"));//本次金额
            StringBuffer sb = new StringBuffer();
            StringBuffer sb2 = new StringBuffer();
            boolean flag = false;//是不超额
            sb.append("因该供应商为一次供方，");
            if (maxVolume < (uVolume + 1)) {
                flag = true;
                sb.append("其合同总数量达到" + (uVolume + 1) + "，大于供方配置中的最大合同总数量" + maxVolume + "。");
                sb2.append("其合同总数量达到" + (uVolume + 1) + "，大于供方配置中的最大合同总数量" + maxVolume + "。");
            }
            if (maxAmount.compareTo(uAmount.add(amount)) < 0) {
                flag = true;
                sb.append("其合同总金额达到" + (uAmount.add(amount)) + "元，大于供方配置中的最大合同总金额" + maxAmount + "元。");
                sb2.append("其合同总金额达到" + (uAmount.add(amount)) + "元，大于供方配置中的最大合同总金额" + maxAmount + "元。");
            }
            sb.append("现已提交供应链部主任进行公司备案，暂无法编制合同。");
            if (flag) {
                String sql = "SELECT * FROM V_USER_ROLES T WHERE ROLE_NAME ='供应链部主任'";
                DAOMeta daoMeta2 = new DAOMeta();
                daoMeta2.setStrSql(sql);
                List<Map<String, Object>> lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta2);
                List<String> userIdLst = new ArrayList<String>();
                ReceiptsMeta receiptsMeta = new ReceiptsMeta();
                if (ListUtil.isNotEmpty(lstUserInfo)) {
                    for (int i = 0; i < lstUserInfo.size(); i++) {
                        String userId = (String) lstUserInfo.get(i).get("userId");
                        userIdLst.add(userId);
                    }
                    receiptsMeta.setLstSendToUserId(userIdLst);
                }
//                String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
//                String templateNo = "SCM_SUPPLIER_LIMIT_MSG";
                Map map = new HashMap<>();
                map.put("supplierId", supplierId);
                map.put("supplierName", supplierName);
                map.put("msg", sb2.toString());

                Map paramMap = new HashMap();
                paramMap.put("map", map);
                paramMap.put("meta", receiptsMeta);
                IAction action = null;
                try {
                    action = BeanFactory.getBean("scm_contract_send_msg");
                    Map mapResult = action.perform(paramMap);
                } catch (NoSuchBeanDefinitionException exception) {
                }
//                new Thread(){
//                    @Override
//                    public void run() {
//                        super.run();
//                        doInTrans(() -> SendMsg(map, msgType, templateNo, receiptsMeta));//异步发送消息
//                    }
//                };


                throw new BizException(sb.toString());
            }
        }
    }

    private Object doInTrans(final TransCallback callback) {
        JtaTransactionManager tm = BeanFactory.getBean(BeanAttribute.TRANSACTION_MANAGER);
        TransactionTemplate tt = new TransactionTemplate(tm);
        return tt.execute(transactionStatus -> callback.doIntrans());
    }
    //发送消息
    public String SendMsg(Map map, String msgType, String templateNo, ReceiptsMeta receiptsMeta) {
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);
//        String sql="update scm_contract s set s.remark= '666' where s.scm_contract_code ='T1600003719' ";
//        DAOMeta daoMeta2 = new DAOMeta();
//        daoMeta2.setStrSql(sql);
//        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta2);
        return "true";
    }

}
