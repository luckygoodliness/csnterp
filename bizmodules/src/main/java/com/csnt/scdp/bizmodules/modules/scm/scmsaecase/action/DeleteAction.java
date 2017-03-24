package com.csnt.scdp.bizmodules.modules.scm.scmsaecase.action;

import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.scmsaecase.services.intf.ScmsaecaseManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  DeleteAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-16 15:17:56
 */

@Scope("singleton")
@Controller("scmsaecase-delete")
@Transactional
public class DeleteAction extends CommonDeleteAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(DeleteAction.class);

	@Resource(name = "scmsaecase-manager")
	private ScmsaecaseManager scmsaecaseManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		List lst  = (ArrayList)inMap.get("uuids");
		String scmSaeCaseId = lst.get(0).toString();
		String sql = "SELECT COUNT(1) AS NUM \n" +
				"FROM SCM_SAE SS\n" +
				"WHERE SS.SAI_CASE = '"+scmSaeCaseId+"' \n" +
				"OR SS.WAI_CASE = '"+scmSaeCaseId+"'";
		DAOMeta daoMeta = new DAOMeta();
		daoMeta.setStrSql(sql);
		List<Map<String, Object>> resultList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
		Map<String, Object> result = resultList.get(0);
		BigDecimal num = (BigDecimal)result.get("num");
		if(num.intValue() > 0) {
			throw new BizException("该考评方案已被考评管理引用,不允许删除！");
		}

		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
