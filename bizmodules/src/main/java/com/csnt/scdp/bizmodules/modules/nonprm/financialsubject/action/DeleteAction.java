package com.csnt.scdp.bizmodules.modules.nonprm.financialsubject.action;

import com.csnt.scdp.bizmodules.entity.nonprm.FinancialSubject;
import com.csnt.scdp.bizmodules.modules.common.action.ErpDeleteAction;
import com.csnt.scdp.bizmodules.modules.nonprm.financialsubject.services.intf.FinancialsubjectManager;
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
 * @timestamp 2015-09-23 10:37:43
 */

@Scope("singleton")
@Controller("financialsubject-delete")
@Transactional
public class DeleteAction extends ErpDeleteAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(DeleteAction.class);
    private FinancialSubject financialSubject;
    @Resource(name = "financialsubject-manager")
    private FinancialsubjectManager financialsubjectManager;

    public void setFinancialSubject(FinancialSubject financialSubject) {
        this.financialSubject = financialSubject;
    }

    public void setFinancialsubjectManager(FinancialsubjectManager financialsubjectManager) {
        this.financialsubjectManager = financialsubjectManager;
    }

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        List lstUuids = (List) super.getDeleteUuids(inMap);
        if (ListUtil.isNotEmpty(lstUuids)) {
            if (financialsubjectManager.checkDelete(lstUuids.get(0).toString())) {
                Map out = super.perform(inMap);
                return out;
            } else {
                throw new BizException("删除失败,费用内容分配中还引用此数据", new HashMap());
            }
        }
        throw new BizException("删除错误！", new HashMap());
    }
}
