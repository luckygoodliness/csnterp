package com.csnt.scdp.bizmodules.modules.scm.scmcontract.action;

import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonModifyAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("scmcontract-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

    @Resource(name = "scmcontract-manager")
    private ScmcontractManager scmcontractManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);
        String mainTabName = StringUtil.capFirst(BeanFactory.getClass(dtoClass).getSimpleName(), false);
        Map mapInput = (Map) viewMap.get(mainTabName);
        String uuid = (String) mapInput.get(CommonAttribute.UUID);
        //1.合同总额不能超过申请明细意向总金额(申请明细数据*意向单价)
//        scmcontractManager.checkAmount(inMap);
        Map out = super.perform(inMap);
        scmcontractManager.allotContractMoney(uuid);
        return out;
    }
}
