package com.csnt.scdp.bizmodules.modules.scm.scmcontract.action;

import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonAddAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("scmcontract-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "scmcontract-manager")
    private ScmcontractManager scmcontractManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);
        Map scmContractDto = (Map) viewMap.get("scmContractDto");
        //1.获取当前用户
        String operatorId = UserHelper.getUserId();
        String operatorName = UserHelper.getUserName();
        String reqNo = NumberingHelper.getNumbering("SCM_QUOTATION", null);
        scmContractDto.put("scmContractCode", reqNo);
        scmContractDto.put("operatorId", operatorId);
        scmContractDto.put("operatorName", operatorName);
        viewMap.put("scmContractCode", reqNo);
        viewMap.put("operatorId", operatorId);
        viewMap.put("operatorName", operatorName);
        viewMap.put("state", '0');
        viewMap.put("isClosed", "0");
        Map out = super.perform(inMap);
        out.put("scmContractCode", reqNo);
        return out;
    }
}
