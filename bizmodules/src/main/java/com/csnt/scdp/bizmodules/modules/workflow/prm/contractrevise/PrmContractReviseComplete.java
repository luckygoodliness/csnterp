package com.csnt.scdp.bizmodules.modules.workflow.prm.contractrevise;

import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_contract_revised-complete")
@Transactional

public class PrmContractReviseComplete extends PrmContractReviseStart {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapVar = super.perform(inMap);
        String role1 = "运管部运营主管";
        List assigneeList = new ArrayList();
        if (StringUtil.isNotEmpty(mapVar.get(role1))) {
            assigneeList = StringUtil.splitAsList((String) mapVar.get(role1), ",");
        }
        String assignees = (String) inMap.get(WorkFlowAttribute.ASSIGNEE_LOWER);
        if(StringUtil.isNotEmpty(assignees)){
            assigneeList= StringUtil.splitAsList(assignees, ";");
        }
        mapVar.put("assigneeList", assigneeList);
        return mapVar;
    }
}
