package com.csnt.scdp.bizmodules.modules.workflow.scm.scmcontract;

import com.csnt.scdp.bizmodules.attributes.ErpWorkFlowAttribute;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractAttribute;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.impl.CashreqManagerImpl;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_contract_prepare-after-complete")
@Transactional

public class ScmContractAfterCompleteAction implements IAction {

    @Resource(name = "scmcontract-manager")
    private ScmcontractManager scmcontractManager;

    @Resource(name = "cashreq-manager")
    private CashreqManagerImpl cashreqManagerImpl;

    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

    private String scmContractId="0";

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
                state = 2;//已审核
                contractState = 4;//审核通过
//                if (ScmContractPrepareStart.isDirectlyEffective(businessKey)) {
//                    isClosed=1;
//                }
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
                        if(!scmContract.getState().equals("2")){
                            if(scmContract.getTotalValue().compareTo(new BigDecimal("0.00"))==1){
                                cashreqManagerImpl.generateScmCashreqByScmContract(list);
                            }
                        }
                    }
                }
                scmContract.setState("2");
                pcm.update(scmContract);
            }
        }
        if (state ==1) {
            String sql="";
            List lstParam = new ArrayList();
//            if(isClosed==1){
//                sql = "update scm_contract set contract_state = ?,state=?,effective_date= sysdate,is_closed='1' where uuid = ?";
//                lstParam.add(new Date());
//            }else {
                sql = "update scm_contract set contract_state = ?,state=? where uuid = ?";
//            }
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            lstParam.add(contractState);
            lstParam.add(state);
            lstParam.add(businessKey);
            daoMeta.setLstParams(lstParam);
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            pcm.updateByNativeSQL(daoMeta);
        }

        //供应链部采购专员提交时明细不能为空
        if(isManager()&&isDetailEmpty(businessKey)){
            if(state==1||state==5){
                throw new BizException("合同明细不能为空");
            }
        }
        return map;
    }


    //查询当前用户是否是采购专员
    private boolean isManager(){
        List roleLst= UserHelper.getRoles();
        String sql="select s.role_name from scdp_role s where s.uuid in(" + StringUtil.joinForSqlIn(roleLst, ErpWorkFlowAttribute.COMMA) + ")";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List lstParam = new ArrayList();
        daoMeta.setLstParams(lstParam);
        List lstRoleName = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        boolean isManager=false;
        for(Iterator it=lstRoleName.iterator();it.hasNext();){
            Map roleNameMap=(Map)it.next();
            String roleName =(String) roleNameMap.get("roleName");
            if("供应链部采购专员".equals(roleName)){
                isManager=true;
                break;
            }
        }
        return  isManager;
    }


    //查询明细是否为空
    private boolean isDetailEmpty(String uuid){
        String sql="select s.uuid from scm_contract_detail s where s.scm_contract_id=?";
        DAOMeta daoMeta=new DAOMeta();
        daoMeta.setStrSql(sql);
        List scmContaractList=new ArrayList<>();
        scmContaractList.add(uuid);
        daoMeta.setLstParams(scmContaractList);
        List uuidLst= PersistenceFactory.getInstance().findByNativeSQL(daoMeta,null);
        boolean isDetailEmpty=false;
        if(ListUtil.isEmpty(uuidLst)){
            isDetailEmpty=true;
        }
        return isDetailEmpty;
    }
}
