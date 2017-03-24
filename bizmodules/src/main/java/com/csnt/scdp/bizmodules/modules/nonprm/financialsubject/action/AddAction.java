package com.csnt.scdp.bizmodules.modules.nonprm.financialsubject.action;


import com.csnt.scdp.bizmodules.entity.nonprm.FinancialSubject;
import com.csnt.scdp.bizmodules.modules.nonprm.financialsubject.services.intf.FinancialsubjectManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonAddAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 10:37:43
 */

@Scope("singleton")
@Controller("financialsubject-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    private FinancialSubject financialSubject;
    @Resource(name = "financialsubject-manager")
    private FinancialsubjectManager financialsubjectManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);
        BasePojo dtoObj = DtoHelper.getDtoFromMap(viewMap, dtoClass);

        List lstValidateFields = new ArrayList();
        lstValidateFields.add("subjectName");

        String modulePath = (String) inMap.get("modulePath");

        DtoHelper.commonAddValidation(dtoObj, lstValidateFields, modulePath);

        Map out = super.perform(inMap);
        return out;

    }
}
