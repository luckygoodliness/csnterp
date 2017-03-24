package com.csnt.scdp.bizmodules.modules.nonprm.monitory.action;

import com.csnt.scdp.bizmodules.entity.nonprm.NonBudgetMonitorY;
import com.csnt.scdp.bizmodules.modules.nonprm.monitory.dto.NonBudgetMonitorYDto;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.nonprm.monitory.services.intf.MonitoryManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-28 09:06:36
 */

@Scope("singleton")
@Controller("monitory-load")
@Transactional
public class LoadAction implements IAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

	@Resource(name = "monitory-manager")
	private MonitoryManager monitoryManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		String year = StringUtil.replaceNull(inMap.get("year"));
        List paramLst = new ArrayList();
        paramLst.add(year);
        DAOMeta daoMetaIn = DAOHelper.getDAO("monitory-dao","get_monitor_in",paramLst);
        DAOMeta daoMetaOut = DAOHelper.getDAO("monitory-dao","get_monitor_out",paramLst);

        List incomeLst = PersistenceFactory.getInstance().findByNativeSQL(daoMetaIn);
        List paymentLst = PersistenceFactory.getInstance().findByNativeSQL(daoMetaOut);

        Map out = new HashMap();
        out.put("incomeLst", incomeLst);
        out.put("paymentLst", paymentLst);

		//Do After
		return out;
	}
}
