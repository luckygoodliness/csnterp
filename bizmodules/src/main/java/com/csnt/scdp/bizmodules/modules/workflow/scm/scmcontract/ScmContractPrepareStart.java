package com.csnt.scdp.bizmodules.modules.workflow.scm.scmcontract;

import com.csnt.scdp.bizmodules.attributes.ErpWorkFlowAttribute;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.modules.sys.workflow.action.WorkFlowVariableProcessAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_contract_prepare-start")
@Transactional

public class ScmContractPrepareStart extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map dataInfo = super.perform(inMap);
        String uuid=(String)inMap.get("businessKey");
        BigDecimal amount = (BigDecimal)dataInfo.get("amount");
        BigDecimal totalValue = ((BigDecimal)dataInfo.get("totalValue")==null)?new BigDecimal(0):(BigDecimal)dataInfo.get("totalValue");
        Map roleMap = ErpWorkFlowHelper.filterRoles(inMap, ErpWorkFlowHelper.loadUserInfoByUserID(inMap));
        roleMap.put("amount", amount);
        roleMap.put("totalValue", totalValue);
        roleMap.put("isManager", isManager());
        roleMap.put("isDetailEmpty", isDetailEmpty(uuid));
        roleMap.put("directlyEffectiveContidion", isDirectlyEffective(uuid));
        return roleMap;
    }

    //查询明细是否为空
    public static boolean isDetailEmpty(String uuid){
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
    //查询当前用户是否是采购经理
    public static boolean isManager(){
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
            if("供应链部采购经理".equals(roleName)){
                isManager=true;
                break;
            }
        }
        return  isManager;
    }
    //查询当前用户是否直接生效
    public static boolean isDirectlyEffective(String uuid){

        //判断用户角色是否是供应链部采购经理或者供应链部采购专员
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
            if("供应链部采购经理".equals(roleName)||"供应链部采购专员".equals(roleName)){
                isManager=true;
                break;
            }
        }

        //判断金额是否满足条件
        boolean isProjectAmount=false;
        ScmContract scmContract=PersistenceFactory.getInstance().findByPK(ScmContract.class,uuid);
        Integer isProject=scmContract.getIsProject()==null?0:scmContract.getIsProject();
        BigDecimal amount=scmContract.getAmount()==null?new BigDecimal(0.00):scmContract.getAmount();
        if(isProject.compareTo(1)==0){
            if(amount.compareTo(new BigDecimal(20000))!=1){
                isProjectAmount=true;
            }
        }else{
            if(amount.compareTo(new BigDecimal(5000))!=1){
                isProjectAmount=true;
            }
        }

        //判断是否是零星采购
        boolean isSporadicProcurement=false;
        String purchaseType=scmContract.getPurchaseTypes();
        if("00".equals(purchaseType)){
            isSporadicProcurement=true;
        }

        //判断明细是否为空
        boolean isContractDetailNull=false;
        if(!isDetailEmpty(uuid)){
            isContractDetailNull=true;
        }


        boolean isDirectlyEffective=isManager&&isContractDetailNull&&isSporadicProcurement&&isProjectAmount;
        return  isDirectlyEffective;
    }
}
