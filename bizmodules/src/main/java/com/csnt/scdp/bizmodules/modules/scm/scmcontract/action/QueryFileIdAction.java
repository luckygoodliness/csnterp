package com.csnt.scdp.bizmodules.modules.scm.scmcontract.action;

import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("scmcontract_getfileid")
@Transactional
public class QueryFileIdAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(QueryFileIdAction.class);

    @Resource(name = "scmcontract-manager")
    private ScmcontractManager scmcontractManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap<>();
        String fileId = "";
        String cdmFileType = (String) inMap.get("cdmFileType");
        String fileClassifyType = (String) inMap.get("fileClassifyType");
        List paramList = new ArrayList<>();
        paramList.add(cdmFileType);
        paramList.add(fileClassifyType);
        DAOMeta daoMeta = DAOHelper.getDAO("scmcontract-dao", "query_file", paramList);
        List cdmFileRelationList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(cdmFileRelationList)) {
            fileId = (String) ((Map) cdmFileRelationList.get(0)).get("fileId");
        } else {
            throw new BizException("未找到该模板！");
        }
        out.put("fileId", fileId);
        return out;
    }
}

