package com.csnt.scdp.bizmodules.modules.workflow.prm.projectmainrevise;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.attributes.ExpandFieldName;
import com.csnt.scdp.bizmodules.attributes.ExpandFieldType;
import com.csnt.scdp.bizmodules.entity.prm.PrmContractC;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMainC;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmContractAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpExpandFieldHelper;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf.PrmprojectmaincManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.core.workflow.intf.IWorkFlow;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_revise-complete")
@Transactional

public class PrmReviseComplete extends ERPWorkFlowBaseVariableCollectionAction {

    @Resource(name = "prmprojectmainc-manager")
    private PrmprojectmaincManager prmprojectmaincManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map dataMap = super.perform(inMap);
        Map roleData = ErpWorkFlowHelper.loadUserInfoByUserID(inMap);
        String role1 = "运管部运营主管";
        List assigneeList = new ArrayList();
        if (StringUtil.isNotEmpty(roleData.get(role1))) {
            assigneeList = StringUtil.splitAsList((String) roleData.get(role1), ",");
        }
        String assignees = (String) inMap.get(WorkFlowAttribute.ASSIGNEE_LOWER);
        if (StringUtil.isNotEmpty(assignees)) {
            assigneeList = StringUtil.splitAsList(assignees, ";");
        }
        Map mapVar = new HashMap();
        mapVar.put("assigneeList", assigneeList);
        mapVar.put("isPreProject", dataMap.get("isPreProject"));
        mapVar.put("isMajorProject", dataMap.get("isMajorProject"));

        String uuid = (String) inMap.get("businessKey");
        if ("usertask1".equals(inMap.get(WorkFlowAttribute.TASK_DEF_KEY))) {
            if (prmprojectmaincManager.isProjectMainChangeDepartInner(uuid)) {
                mapVar.put("isProjectMainChangeDepartInner", "1");
            } else {
                mapVar.put("isProjectMainChangeDepartInner", "0");
            }
        }

        String processDeptCode = (String) dataMap.get("contractorOffice");
        String officeUUid = OrgHelper.getOrgIdByCode(processDeptCode);
        String stipulateProfitRate = ErpExpandFieldHelper.getExpandFieldValue(ExpandFieldType.ORG, officeUUid,
                ExpandFieldName.PROFIT_RATE);
        BigDecimal perProfitRate = new BigDecimal(0);

        try {
            perProfitRate = new BigDecimal(stipulateProfitRate).divide(new BigDecimal(100));
        } catch (Exception e) {
            throw new BizException("请查看系统扩展字段最小利润率阈值是否正确");
        }

        BigDecimal projectMoney = dataMap.get(PrmProjectMainCAttribute.PROJECT_MONEY) == null ? BigDecimal.ZERO : (BigDecimal) dataMap.get(PrmProjectMainCAttribute.PROJECT_MONEY);
        BigDecimal costControllMoney = (BigDecimal) dataMap.get(PrmProjectMainCAttribute.COST_CONTROL_MONEY);
        BigDecimal actualProfitRate = projectMoney.equals(new BigDecimal(0)) ? new BigDecimal(0) : projectMoney.subtract(costControllMoney).divide(projectMoney, 3, BigDecimal.ROUND_HALF_DOWN);
        if (actualProfitRate.compareTo(perProfitRate) == -1) {
            mapVar.put("isMeagerProfit", 1);
        } else {
            mapVar.put("isMeagerProfit", 0);
        }

        String prmProjectMainId = (String) dataMap.get("prmProjectMainId");
        BigDecimal projectLastMoney = BigDecimal.ZERO;
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map mapCondition = new HashMap();
        mapCondition.put(PrmProjectMainCAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainId);
        List<PrmProjectMainC> lstC = pcm.findByAnyFields(PrmProjectMainC.class, mapCondition, null);
        if (ListUtil.isNotEmpty(lstC)) {
            lstC = lstC.stream().filter(t -> t.getState().equals(BillStateAttribute.FAD_BILL_STATE_APPROVED) && t.getAuditTime() != null).collect(Collectors.toList());
            if (ListUtil.isNotEmpty(lstC)) {
                PrmProjectMainC c = lstC.stream().max((x, y) -> x.getAuditTime().compareTo(y.getAuditTime())).get();
                if (c.getProjectMoney() != null) {
                    projectLastMoney = c.getProjectMoney();
                }
            }
        }
        if (projectMoney.compareTo(projectLastMoney) == -1) {
            mapVar.put("projectMoneyReduce", 1);
        } else {
            mapVar.put("projectMoneyReduce", 0);
        }

        mapVar.putAll(ErpWorkFlowHelper.filterRoles(inMap, roleData));
        return mapVar;
    }
}
