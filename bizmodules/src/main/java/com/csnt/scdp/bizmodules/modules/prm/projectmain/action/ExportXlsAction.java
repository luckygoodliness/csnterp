package com.csnt.scdp.bizmodules.modules.prm.projectmain.action;

import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.services.intf.ProjectmainManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonExportXlsAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  ExportXlsAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 13:30:43
 */

@Scope("singleton")
@Controller("projectmain-exportxls")
@Transactional
public class ExportXlsAction extends CommonExportXlsAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ExportXlsAction.class);

	@Resource(name = "projectmain-manager")
	private ProjectmainManager projectmainManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map queryExtraMap = (Map) inMap.get(CommonAttribute.QUERY_EXTRA_PARAMS);
		if (queryExtraMap == null) {
			queryExtraMap = new HashMap();
			inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, queryExtraMap);
		}
		Object filterFinishedProject = queryExtraMap.get(PrmProjectMainAttribute.FILTER_FINISHED_PROJECT);
		StringBuffer sb = new StringBuffer(" 1=1 ");
		if (Boolean.TRUE.equals(filterFinishedProject)) {
			//结项的项目要过滤掉
			sb.append(" and not exists(select 1 from prm_final_estimate t21 " +
					"where (t21.is_void is null or t21.is_void=0) " +
					"and t21.square_type='1' " +
					"and t21.prm_project_main_id=t1.uuid) ");
		}
		queryExtraMap.put("selfconditions", sb.toString());
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
