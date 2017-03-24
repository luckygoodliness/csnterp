package com.csnt.scdp.bizmodules.modules.fad.payreq.action;

import com.csnt.scdp.bizmodules.attributes.CommonAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqC;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqH;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqHDto;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.fad.payreq.services.intf.PayreqManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 17:42:24
 */

@Scope("singleton")
@Controller("payreq-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

    @Resource(name = "payreq-manager")
    private PayreqManager payreqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        String dtoClass = (String) inMap.get(com.csnt.scdp.framework.attributes.CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(com.csnt.scdp.framework.attributes.CommonAttribute.VIEW_DATA);

        BasePojo dtoObj = DtoHelper.getDtoFromMap(viewMap, dtoClass);
        String UUid = ((FadPayReqHDto) dtoObj).getUuid();

        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    @Override
    protected void beforeAction(BasePojo dtoObj) {

    }
}
