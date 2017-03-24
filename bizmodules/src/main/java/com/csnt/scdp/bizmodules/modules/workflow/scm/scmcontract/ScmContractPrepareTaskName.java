package com.csnt.scdp.bizmodules.modules.workflow.scm.scmcontract;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractAttribute;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_contract_prepare-process-taskname")
@Transactional

public class ScmContractPrepareTaskName extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        inMap.put("dto", "com.csnt.scdp.bizmodules.modules.scm.scmcontract.dto.ScmContractDto");
        Map dataInfo = super.perform(inMap);
        String scmContractCode = (String) dataInfo.get("scmContractCode");
        Map mapResult = new HashMap();
        String state = (String) dataInfo.get("state");
        String supplierName = (String) dataInfo.get("supplierName");
        BigDecimal amount = (BigDecimal) dataInfo.get("amount");
        DecimalFormat df = new DecimalFormat("#,###.00");
        String amountFormate = null;
        if (null != amount) {
            amountFormate = df.format(amount);
        }
        String officeId = (String) dataInfo.get("officeId");
        String orgName = null;
        if (StringUtil.isNotEmpty(officeId)) {
            orgName = OrgHelper.getOrgNameByCode(officeId);
        }

        String fadSubjectCode = null;
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        String projectId = (String) dataInfo.get("projectId");
        String subjectCode = (String) dataInfo.get("subjectCode");
        if (StringUtil.isNotEmpty(projectId)) {
            PrmProjectMain prmProjectMain = pcm.findByPK(PrmProjectMain.class, projectId);
            if (prmProjectMain != null) {
                fadSubjectCode = prmProjectMain.getProjectCode();
            }
        } else if (StringUtil.isNotEmpty(subjectCode)) {
            fadSubjectCode = subjectCode;
        }

        String stateDesc = ErpWorkFlowHelper.stateFlag(state);
        mapResult.put("name", stateDesc + " " + scmContractCode + " ￥" + amountFormate + " " + supplierName + " " + fadSubjectCode + " " + orgName);
        return mapResult;
    }
}
