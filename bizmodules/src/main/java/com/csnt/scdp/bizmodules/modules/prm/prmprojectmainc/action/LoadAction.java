package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmProjectMainCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf.PrmprojectmaincManager;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.services.intf.ProjectmainManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-27 15:28:19
 */

@Scope("singleton")
@Controller("prmprojectmainc-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "prmprojectmainc-manager")
    private PrmprojectmaincManager prmprojectmaincManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        Map mainCDto = (HashMap) (out.get(PrmProjectMainCAttribute.PRM_PROJECT_MAIN_C_DTO));
        if (MapUtil.isNotEmpty(mainCDto)) {
            String detailType = (String) mainCDto.get(PrmProjectMainCAttribute.DETAIL_TYPE);
            String state = (String) mainCDto.get(PrmProjectMainCAttribute.STATE);
            if (!PrmProjectMainAttribute.PRM_DETAIL_NEW.equals(detailType) &&
                    !BillStateAttribute.FAD_BILL_STATE_APPROVED.equals(state)) {
                String mainUuid = (String) mainCDto.get(PrmProjectMainCAttribute.PRM_PROJECT_MAIN_ID);
                if (StringUtil.isNotEmpty(mainUuid)) {
                    ProjectmainManager projectmainManager = BeanFactory.getBean("projectmain-manager");
                    List<String> uuids = projectmainManager.getRelatedContractUuidsFromMain(mainUuid);
                    out.put("mainContractUuids", uuids);
                }
            }

        }
        return out;
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        PrmProjectMainCDto projectMainCDto = (PrmProjectMainCDto) dtoObj;
        if (projectMainCDto == null) {
            return;
        }
        prmprojectmaincManager.loadExtraDescField(projectMainCDto);
    }
}
