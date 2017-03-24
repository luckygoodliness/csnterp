package com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.action;

import com.csnt.scdp.bizmodules.modules.common.action.ErpDeleteAction;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf.SubjectsubjectManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  DeleteAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 18:59:14
 */

@Scope("singleton")
@Controller("subjectsubject-delete")
@Transactional
public class DeleteAction extends ErpDeleteAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(DeleteAction.class);

	@Resource(name = "subjectsubject-manager")
	private SubjectsubjectManager subjectsubjectManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
        List lstUuids = (List) super.getDeleteUuids(inMap);
        if (ListUtil.isNotEmpty(lstUuids)) {
            if (subjectsubjectManager.checkDelete(lstUuids.get(0).toString())) {
                Map out = super.perform(inMap);
                return out;
            } else {
                throw new BizException("删除失败,预算计划申报中还引用此数据！", new HashMap());
            }
        }
        throw new BizException("删除错误！", new HashMap());
	}
}
