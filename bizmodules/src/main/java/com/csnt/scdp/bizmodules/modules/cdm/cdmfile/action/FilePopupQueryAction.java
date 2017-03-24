package com.csnt.scdp.bizmodules.modules.cdm.cdmfile.action;

import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.services.intf.CdmfileManager;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-23 10:09:49
 */

@Scope("singleton")
@Controller("file-popup-query")
@Transactional
public class FilePopupQueryAction extends CommonQueryAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(FilePopupQueryAction.class);

	@Resource(name = "cdmfile-manager")
	private CdmfileManager cdmfileManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = new HashMap();
		String dataId = (String)inMap.get("dataId");
		if(StringUtil.isNotEmpty(dataId)){
			List lstParam = new ArrayList();
			lstParam.add(dataId);
			DAOMeta daoMeta = DAOHelper.getDAO("cdmfile-dao", "file_popup_query", lstParam);
			List lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
			out.put("cdmFileRelationDto", lstResult);
		}
		//Do After
		return out;
	}
}
