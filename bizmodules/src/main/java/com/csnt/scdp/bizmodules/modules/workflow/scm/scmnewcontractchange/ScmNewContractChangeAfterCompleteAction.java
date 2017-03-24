package com.csnt.scdp.bizmodules.modules.workflow.scm.scmnewcontractchange;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.bizmodules.entity.scm.ScmContractChange;
import com.csnt.scdp.bizmodules.entity.scm.ScmContractChangeD;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.services.intf.ScmcontractchangeManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.StringUtil;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_purchase_change-after-fixed")
@Transactional

public class ScmNewContractChangeAfterCompleteAction extends ERPWorkFlowBaseVariableCollectionAction {

    @Resource(name = "scmcontractchange-manager")
    private ScmcontractchangeManager scmcontractchangeManager;

    @Resource(name = "scmcontract-manager")
    private ScmcontractManager scmcontractManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        String uuid = (String) inMap.get("businessKey");
        List lastParam = new ArrayList();
        lastParam.add(uuid);
        String message = (PersistenceFactory.getInstance().getDataFromProcOrFunc("call PKG_SCM.SP_SCM_CONTRACT_CHANGE(?,?)", lastParam, PersistenceCrudManager.RETURN_DATA_TYPE.VARCHAR)).toString();
//        Util.println(message);
        if ("TRUE".equals(message)) {
//            todo
        } else {
            throw new BizException("PKG_SCM.SP_SCM_CONTRACT_CHANGE出现异常，请联系管理员！");
        }
//        ScmContractChange scmContractChange = PersistenceFactory.getInstance().findByPK(ScmContractChange.class, uuid);
//        String scmContractId = scmContractChange.getScmContractId();
//        if (StringUtil.isNotEmpty(scmContractId)) {
//            scmcontractchangeManager.updateStateToApproved(uuid,scmContractId);
//
//
//            Map<String, Object> queryMap = new HashMap();
//            queryMap.put("scmContractChangeId", uuid);
//            List<ScmContractChangeD> list = PersistenceFactory.getInstance().findByAnyFields(ScmContractChangeD.class,queryMap,null);
//            for(ScmContractChangeD sscd : list){
//                if("0".equals(sscd.getChangeState())){
//                    String sql = "UPDATE PRM_PURCHASE_REQ_DETAIL R" +
//                            "   SET R.HANDLE_AMOUNT =" +
//                            "       (SELECT C.HANDLE_AMOUNT" +
//                            "          FROM SCM_CONTRACT_CHANGE_D C" +
//                            "         WHERE R.UUID = C.PRM_PURCHASE_REQ_DETAIL_ID AND ROWNUM=1)" +
//                            " WHERE R.UUID = ? ";
//                    List lstParams = new ArrayList<>();
//                    lstParams.add(sscd.getPrmPurchaseReqDetailId());
//                    PersistenceFactory.getInstance().updateByNativeSQL(new DAOMeta(sql, lstParams));
//                }else if("1".equals(sscd.getChangeState())){
//                    PrmPurchaseReqDetail req = new PrmPurchaseReqDetail();
////                BeanUtil.bean2Bean(sscd,new PrmPurchaseReqDetail());
//                    try {
//                        BeanUtils.copyProperties(req, sscd);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    } catch (InvocationTargetException e) {
//                        e.printStackTrace();
//                    }
//                    req.setAddFrom(1);
//                    req.setPrmPurchaseReqId(uuid);
//                    req.setScmContractId(scmContractChange.getScmContractId());
//                    PersistenceFactory.getInstance().insert(req);
//                }else if("2".equals(sscd.getChangeState())){
//                    //如果是原有变为退回，把原合同该明细删除
//                    if(StringUtil.isEmpty(sscd.getPuuid())){
//                        PrmPurchaseReqDetail pprd = PersistenceFactory.getInstance().findByPK(PrmPurchaseReqDetail.class, sscd.getPrmPurchaseReqDetailId());
//                        PersistenceFactory.getInstance().delete(pprd);
//                    }
//
//                    PrmPurchaseReqDetail req = new PrmPurchaseReqDetail();
//                    try {
//                        BeanUtils.copyProperties(req, sscd);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    } catch (InvocationTargetException e) {
//                        e.printStackTrace();
//                    }
//                    req.setIsfallback(1);
//                    //李坚立要求加上  modify 2016-09-06
//                    req.setScmContractCode("");
//                    req.setScmContractId("");
//                    req.setFallbackReason("采购合同变更申请退回。");
//                    PersistenceFactory.getInstance().insert(req);
//                }
//            }
//
//            scmcontractManager.allotContractMoney(scmContractId);
//        }

        return mapResult;
    }
}
