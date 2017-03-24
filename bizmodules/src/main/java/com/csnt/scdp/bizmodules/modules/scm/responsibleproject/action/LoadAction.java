package com.csnt.scdp.bizmodules.modules.scm.responsibleproject.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmResponsibleProjectAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.responsibleproject.services.intf.ResponsibleprojectManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-05-16 16:03:55
 */

@Scope("singleton")
@Controller("responsibleproject-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "responsibleproject-manager")
    private ResponsibleprojectManager responsibleprojectManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
		Map dtoMap = (Map) out.get("scmResponsibleProjectDto");

		if (dtoMap != null) {
			PersistenceCrudManager pcm = PersistenceFactory.getInstance();
			//1.设置负责人的显示
			String principalName = "";
			String principal = (String) dtoMap.get(ScmResponsibleProjectAttribute.PRINCIPAL);
			if (StringUtil.isNotEmpty(principal)) {
				Map paramMap = new HashMap<>();
				paramMap.put(ScdpUserAttribute.USER_ID, principal);
				List<ScdpUser> list = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, paramMap, null);
				if (ListUtil.isNotEmpty(list)) {
					principalName = list.get(0).getUserName();
				}
			}
			dtoMap.put(ScmResponsibleProjectAttribute.PRINCIPAL_DESC, principalName);
			//2.设置科目的显示
			String projectDesc = "";
			String projectCode = "";
			String projectId = (String) dtoMap.get(ScmResponsibleProjectAttribute.RESPONSIBLE_PROJECT);
			if (StringUtil.isNotEmpty(projectId)) {
				PrmProjectMain prmProjectMain = pcm.findByPK(PrmProjectMain.class, projectId);
				if (prmProjectMain != null) {
					projectDesc = prmProjectMain.getProjectName();
					projectCode = prmProjectMain.getProjectCode();
				}
			}
			dtoMap.put(ScmResponsibleProjectAttribute.RESPONSIBLE_PROJECT_DESC, projectDesc);
			dtoMap.put(ScmResponsibleProjectAttribute.PROJECT_CODE, projectCode);
		}

        return out;
    }
}
