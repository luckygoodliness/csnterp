package com.csnt.scdp.bizmodules.modules.workflow.prm.projectmain;

import com.csnt.scdp.bizmodules.attributes.ExpandFieldName;
import com.csnt.scdp.bizmodules.attributes.ExpandFieldType;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpExpandFieldHelper;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_main-complete")
@Transactional

public class PrmMainComplete extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map dataMap = super.perform(inMap);
        Map mapVar = new HashMap();
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

        BigDecimal projectMoney = (BigDecimal) dataMap.get(PrmProjectMainCAttribute.PROJECT_MONEY);
        BigDecimal costControllMoney = (BigDecimal) dataMap.get(PrmProjectMainCAttribute.COST_CONTROL_MONEY);
        BigDecimal actualProfitRate = projectMoney.equals(new BigDecimal(0)) ? new BigDecimal(0) : projectMoney.subtract(costControllMoney).divide(projectMoney, 3, BigDecimal.ROUND_HALF_DOWN);
        if (actualProfitRate.compareTo(perProfitRate) == -1) {
            mapVar.put("isMeagerProfit",1);
        }else {
            mapVar.put("isMeagerProfit",0);
        }

        mapVar.put("isPreProject", (Integer) dataMap.get("isPreProject"));
        mapVar.put("isMajorProject", (Integer) dataMap.get("isMajorProject"));
        Map userRoles = ErpWorkFlowHelper.loadUserInfoByUserID(inMap);
        String tempUsers = (String) userRoles.get("运管部运营主管");
        String assignees = (String) inMap.get(WorkFlowAttribute.ASSIGNEE_LOWER);
        if (StringUtil.isNotEmpty(tempUsers)) {
            mapVar.put("assigneeList", StringUtil.splitAsList(tempUsers, ","));
        }
        if (StringUtil.isNotEmpty(assignees)) {
            mapVar.put("assigneeList", StringUtil.splitAsList(assignees, ";"));
        }
        mapVar.putAll(ErpWorkFlowHelper.filterRoles(inMap, userRoles));
        return mapVar;
    }
}
