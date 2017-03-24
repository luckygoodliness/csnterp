package com.csnt.scdp.bizmodules.modules.scm.scmsaetask.action;

import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.UserHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.scmsaetask.services.intf.ScmsaetaskManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  ExportXlsAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-26 09:56:50
 */

@Scope("singleton")
@Controller("scmsaetask-exportxls")
@Transactional
public class ExportXlsAction extends CommonExportXlsAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ExportXlsAction.class);

	@Resource(name = "scmsaetask-manager")
	private ScmsaetaskManager scmsaetaskManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		Map condMap = (Map) inMap.get("queryConditions");
		StringBuffer selfSql = new StringBuffer();
		String sql = "select * from scdp_role s where s.role_name='供应链部供应商管理专员' ";
		DAOMeta daoMeta1 = new DAOMeta(sql);
		selfSql.append("1=1");
		List<Map<String, Object>> reqList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta1);
		if (!(UserHelper.getRoles().contains(reqList.get(0).get("uuid")))) {
			selfSql.append(" and SST.CREATE_BY = '" + UserHelper.getUserId() + "'");
		}
		if(condMap.get("beginTime") != null&&!("".equals(condMap.get("beginTime")))) {
			String beginTime = condMap.remove("beginTime").toString();
			beginTime = beginTime.substring(0, beginTime.indexOf('T'));
			selfSql.append(" and SST.begin_Time >= to_date('"+beginTime+"', 'yyyy-mm-dd')");
		}
		if(condMap.get("endTime") != null&&!("".equals(condMap.get("endTime")))) {
			String endTime = condMap.remove("endTime").toString();
			endTime = endTime.substring(0, endTime.indexOf('T'));
			selfSql.append(" and SST.end_Time <= to_date('"+endTime+"', 'yyyy-mm-dd')");
		}
		Map map = new HashMap();
		map.put("selfconditions", selfSql.toString());
		inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, map);
		//Do before
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
