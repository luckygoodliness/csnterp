package com.csnt.scdp.bizmodules.modules.workflow.prm.purchasereq;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchaseReqDetailAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_purchase_request-start")
@Transactional

public class PrmPurchaseRequestStart extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map dataInfo = super.perform(inMap);
        Integer isInner = (Integer) dataInfo.get("isInner");
        Map mapVar = new HashMap();
        mapVar.putAll(ErpWorkFlowHelper.filterRoles(inMap, ErpWorkFlowHelper.loadUserInfoByUserID(inMap)));
        mapVar.put("isProjectManager", 0);
        mapVar.put("isInner", isInner);

        Map condition1 = new HashMap<>();
        condition1.put(PrmPurchaseReqDetailAttribute.PRM_PURCHASE_REQ_ID,inMap.get(WorkFlowAttribute.BUSINESS_KEY));
        List<PrmPurchaseReqDetail> prmPurchaseReqDetails=new ArrayList<>();
        prmPurchaseReqDetails=PersistenceFactory.getInstance().findByAnyFields(PrmPurchaseReqDetail.class,condition1,null);
        PrmPurchaseReqDetail prmPurchaseReqDetail = prmPurchaseReqDetails.stream().filter(t->"1".equals(t.getIsStamp())).findAny().orElse(null);
        if(prmPurchaseReqDetail==null){
            mapVar.put("markdeDetail",0);
        }else {
            mapVar.put("markdeDetail",1);
        }


        //查看登录人是否是项目经理
        String uuid = (String) inMap.get("businessKey");
        String userId = UserHelper.getUserId();
        String sql = "select sr.role_name　from scdp_role sr where uuid in (select pmdp.post\n" +
                "                                                       from prm_member_detail_p pmdp\n" +
                "                                                      where pmdp.prm_project_main_id in\n" +
                "                                                            (select ppr.prm_project_main_id\n" +
                "                                                               from prm_purchase_req ppr\n" +
                "                                                              where ppr.uuid =\n" +
                "                                                                    ?)\n" +
                "                                                        and pmdp.staff_id =\n" +
                "                                                            ?) and sr.role_name = ?";

        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List condition = new ArrayList<>();
        condition.add(uuid);
        condition.add(userId);
        condition.add("项目经理");
        daoMeta.setLstParams(condition);
        List lst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lst)) {
            mapVar.put("isProjectManager", 1);
        }


        String officeId = dataInfo.get("innerSupplier") == null ? "" : (String) dataInfo.get("innerSupplier");
        if (StringUtil.isNotEmpty(officeId)) {
            String role1 = "被委托事业部总经理";
            String orgUuid = OrgHelper.getOrgIdByCode(officeId);
            Map userMap = ErpWorkFlowHelper.loadBusinessDivisionInfo(orgUuid, officeId);
            mapVar.put(role1, userMap.get("事业部总经理"));
        }


        return mapVar;
    }
}
