package com.csnt.scdp.bizmodules.modules.workflow.scm.scmcontractchange;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_contract_revise-process-taskname")
@Transactional

public class ScmContractChangeTaskName extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        Map mapResult = new HashMap();
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);

        inMap.put("dto", "com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.dto.ScmContractChangeDto");
        Map dataInfo = super.perform(inMap);
        BigDecimal originalValue = (BigDecimal) dataInfo.get("originalValue");
        BigDecimal newValue = (BigDecimal) dataInfo.get("newValue");
        DecimalFormat df = new DecimalFormat("#,###.00");
        String originalValueFormate = null;
        String newValueFormate = null;
        if (null != originalValue) {
            originalValueFormate = df.format(originalValue);
        }
        if (null != newValue) {
            newValueFormate = df.format(newValue);
        }
        String officeId = (String) dataInfo.get("officeId");
        String orgName = null;
        if (StringUtil.isNotEmpty(officeId)) {
            orgName = OrgHelper.getOrgNameByCode(officeId);
        }

        String sql = "select a.state,\n" +
                "       c.scm_contract_code,\n" +
                "       c.supplier_name,\n" +
                "       decode(nvl(c.is_project, 0), 0, c.subject_code, p.project_code) as fad_subject_code\n" +
                "  from scm_contract_change a\n" +
                " inner join scm_contract c\n" +
                "    on a.scm_contract_id = c.uuid\n" +
                "  left join prm_project_main p\n" +
                "    on p.uuid = c.project_id\n" +
                " where a.uuid =?";
        List lstParam = new ArrayList();
        lstParam.add(businessKey);
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setLstParams(lstParam);
        daoMeta.setNeedFilter(false);
        List lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstResult)) {
            Map temp = (Map) lstResult.get(0);
            String state = null;
            if (temp.get("state") != null) {
                state = temp.get("state").toString();
            }
            String stateDesc = ErpWorkFlowHelper.stateFlag(state);
            mapResult.put("name", stateDesc + " " + temp.get("scmContractCode") + " ￥" + originalValueFormate + "->" + "￥" + newValueFormate + " " + temp.get("supplierName") + " " + temp.get("fadSubjectCode") + " " + orgName);
        }

        return mapResult;
    }
}
