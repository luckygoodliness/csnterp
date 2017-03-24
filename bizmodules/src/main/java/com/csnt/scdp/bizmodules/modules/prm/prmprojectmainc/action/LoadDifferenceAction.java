package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.action;

import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmProjectMainCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf.PrmprojectmaincManager;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.services.intf.ProjectmainManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangming on 2015-10-21.
 */

@Scope("singleton")
@Controller("prmprojectmainc-load-difference")
@Transactional
public class LoadDifferenceAction implements IAction {

    @Resource(name = "prmprojectmainc-manager")
    private PrmprojectmaincManager prmprojectmaincManager;

    @Resource(name = "projectmain-manager")
    private ProjectmainManager projectmainManager;

    @Override
    public Map perform(Map map) throws BizException, SysException {
        String uuid = (String) map.get(CommonAttribute.UUID);
        String prmProjectMainId = (String) map.get(PrmProjectMainCAttribute.PRM_PROJECT_MAIN_ID);
        PrmProjectMainCDto dto = projectmainManager.loadAllDifferenceData(uuid, prmProjectMainId);
        prmprojectmaincManager.loadExtraDescField(dto);
        Map out = new HashMap<>();
        out.put(PrmProjectMainCAttribute.PRM_PROJECT_MAIN_C_DTO, dto);
        return out;
    }
}
