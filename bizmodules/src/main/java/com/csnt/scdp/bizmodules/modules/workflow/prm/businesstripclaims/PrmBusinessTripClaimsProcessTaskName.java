package com.csnt.scdp.bizmodules.modules.workflow.prm.businesstripclaims;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_businesstrip_claims-process-taskname")
@Transactional

public class PrmBusinessTripClaimsProcessTaskName extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        String sql = "select a.state,a.invoice_total_value,a.render_person,a.office_id,a.fad_subject_code,a.invoice_req_no From fad_invoice a where a.uuid=?";
        List lstParam = new ArrayList();
        lstParam.add(businessKey);
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setLstParams(lstParam);
        daoMeta.setNeedFilter(false);
        List lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstResult)) {
            Map temp = (Map) lstResult.get(0);
            String userName = null;
            String orgName = null;
            try {
                String userId = (String) temp.get("renderPerson");
                String orgCode = (String) temp.get("officeId");
                ScdpUser user = ErpUserHelper.getScdpUserByUserId(userId);
                userName = user.getUserName();
                orgName = OrgHelper.getOrgNameByCode(orgCode);
            } catch (Exception e) {
                throw new BizException("项目差旅报销：流水号" + temp.get("invoiceReqNo") + "报销人或者报销部门未填");
            }
            String state = null;
            if (temp.get("state") != null) {
                state = temp.get("state").toString();
            }
            String stateDesc = ErpWorkFlowHelper.stateFlag(state);
            mapResult.put("name", stateDesc + "部门：" + orgName + ";报销人：" + userName + ";课题代号：" + temp.get("fadSubjectCode") + ";报销金额：" + temp.get("invoiceTotalValue"));
        }
        return mapResult;
    }
}
