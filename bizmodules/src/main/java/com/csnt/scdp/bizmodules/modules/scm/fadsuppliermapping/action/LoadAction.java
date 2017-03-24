package com.csnt.scdp.bizmodules.modules.scm.fadsuppliermapping.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entityattributes.scm.FadSupplierMappingAttribute;
import com.csnt.scdp.bizmodules.modules.scm.fadsuppliermapping.dto.FadSupplierMappingDto;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.fadsuppliermapping.services.intf.FadsuppliermappingManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-04 15:22:38
 */

@Scope("singleton")
@Controller("fadsuppliermapping-load")
@Transactional
public class LoadAction extends CommonLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "fadsuppliermapping-manager")
    private FadsuppliermappingManager fadsuppliermappingManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);

        //Do After
        return out;
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        FadSupplierMappingDto fadSupplierMappingDto = (FadSupplierMappingDto) dtoObj;
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        ScmSupplier scmSupplier = pcm.findByPK(ScmSupplier.class, fadSupplierMappingDto.getSupplierToUuid());
        if (scmSupplier != null) {
            fadSupplierMappingDto.setSupplierToUuidDesc(scmSupplier.getCompleteName());
        }
    }
}
