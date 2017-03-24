package com.csnt.scdp.bizmodules.modules.prm.projectmain.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePackage;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePlanDetail;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmProjectMainDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.services.intf.ProjectmainManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 13:30:43
 */

@Scope("singleton")
@Controller("projectmain-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "projectmain-manager")
    private ProjectmainManager projectmainManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map out = super.perform(inMap);
        Map prmProjectMainDtoMap = (Map) out.get("prmProjectMainDto");
        List<Map> prmBudgetOutsourceList = (List) prmProjectMainDtoMap.get(PrmProjectMainAttribute.PRM_BUDGET_OUTSOURCE_DTO);
        List<Map> prmBudgetPrincipalList = (List) prmProjectMainDtoMap.get(PrmProjectMainAttribute.PRM_BUDGET_PRINCIPAL_DTO);
        List<Map> prmBudgetAccessoryList = (List) prmProjectMainDtoMap.get(PrmProjectMainAttribute.PRM_BUDGET_ACCESSORY_DTO);

        if (MapUtil.isNotEmpty(prmProjectMainDtoMap)) {
            String prmProjectMainId = (String) prmProjectMainDtoMap.get("uuid");
            prmProjectMainDtoMap.put("innerProjectInfos", projectmainManager.getInnerProjectInfos(prmProjectMainId));
            String state = (String) prmProjectMainDtoMap.get("state");
            if (state == null) {
                prmProjectMainDtoMap.put("state", "2");//未结算状态
            }
        }

        // M3_C3_F1_界面功能增加
        // 外协预算
        if (ListUtil.isNotEmpty(prmBudgetOutsourceList)) {
            prmBudgetOutsourceList.forEach(record -> {
                Map param = new HashMap();
                param.put("prm_project_main_id", prmProjectMainDtoMap.get("uuid"));
                param.put("prm_budget_ref_id", record.get("uuid"));
                List<PrmPurchasePlanDetail> planDetailList = pcm.findByAnyFields(PrmPurchasePlanDetail.class, param, null);
                if (ListUtil.isNotEmpty(planDetailList)) {
                    PrmPurchasePlanDetail detail = planDetailList.get(0);
                    if (detail.getPurchasePackageId() != null) {
                        PrmPurchasePackage pk = pcm.findByPK(PrmPurchasePackage.class, detail.getPurchasePackageId());
                        record.put("packageName", pk.getPackageName());
                        record.put("packageNo", pk.getPackageNo());
                    }
                }
            });
        }

        // 设备材料预算
        if (ListUtil.isNotEmpty(prmBudgetPrincipalList)) {
            prmBudgetPrincipalList.forEach(record -> {
                Map param = new HashMap();
                param.put("prm_project_main_id", prmProjectMainDtoMap.get("uuid"));
                param.put("prm_budget_ref_id", record.get("uuid"));
                List<PrmPurchasePlanDetail> planDetailList = pcm.findByAnyFields(PrmPurchasePlanDetail.class, param, null);
                if (ListUtil.isNotEmpty(planDetailList)) {
                    PrmPurchasePlanDetail detail = planDetailList.get(0);
                    if (detail.getPurchasePackageId() != null) {
                        PrmPurchasePackage pk = pcm.findByPK(PrmPurchasePackage.class, detail.getPurchasePackageId());
                        record.put("packageName", pk.getPackageName());
                        record.put("packageNo", pk.getPackageNo());
                    }
                }

            });
        }

        // 辅助材料预算
        if (ListUtil.isNotEmpty(prmBudgetAccessoryList)) {
            prmBudgetAccessoryList.forEach(record -> {
                Map param = new HashMap();
                param.put("prm_project_main_id", prmProjectMainDtoMap.get("uuid"));
                param.put("prm_budget_ref_id", record.get("uuid"));
                List<PrmPurchasePlanDetail> planDetailList = pcm.findByAnyFields(PrmPurchasePlanDetail.class, param, null);
                if (ListUtil.isNotEmpty(planDetailList)) {
                    PrmPurchasePlanDetail detail = planDetailList.get(0);
                    if (detail.getPurchasePackageId() != null) {
                        PrmPurchasePackage pk = pcm.findByPK(PrmPurchasePackage.class, detail.getPurchasePackageId());
                        record.put("packageName", pk.getPackageName());
                        record.put("packageNo", pk.getPackageNo());
                    }
                }

            });
        }
        //Do After
        return out;
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        PrmProjectMainDto projectMainDto = (PrmProjectMainDto) dtoObj;
        if (projectMainDto == null) {
            return;
        }
        projectmainManager = (ProjectmainManager) BeanFactory.getBean("projectmain-manager");
        projectmainManager.loadExtraDescField(projectMainDto);
    }
}
