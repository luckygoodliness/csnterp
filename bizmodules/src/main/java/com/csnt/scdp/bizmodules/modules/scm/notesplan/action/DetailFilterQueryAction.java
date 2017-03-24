package com.csnt.scdp.bizmodules.modules.scm.notesplan.action;

import com.csnt.scdp.bizmodules.modules.scm.notesplan.services.intf.NotesplanManager;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-04 15:02:50
 */

@Scope("singleton")
@Controller("notesplan-detail-filter-query")
@Transactional
public class DetailFilterQueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(DetailFilterQueryAction.class);

    @Resource(name = "notesplan-manager")
    private NotesplanManager notesplanManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //1.在编辑页面里点击自定义的查询按钮时的加载数据方法
        return notesplanManager.loadDataDefault(inMap);
    }
}
