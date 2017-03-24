package com.csnt.scdp.bizmodules.modules.operate.operatekeyprojectsinfo.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmCustomerAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.operate.operatekeyprojectsinfo.dto.OperateKeyProjectsInfoDto;
import com.csnt.scdp.bizmodules.modules.operate.operatekeyprojectsinfo.services.intf.OperatekeyprojectsinfoManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
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
 * @timestamp 2015-10-27 17:35:53
 */

@Scope("singleton")
@Controller("operatekeyprojectsinfo-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "operatekeyprojectsinfo-manager")
    private OperatekeyprojectsinfoManager operatekeyprojectsinfoManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        Map dtoMap = (Map) out.get("operateKeyProjectsInfoDto");
        if (dtoMap != null) {
            String officeId = (String) dtoMap.get("officeId");
            if (StringUtil.isNotEmpty(officeId)) {
                dtoMap.put("officeIdDesc", OrgHelper.getOrgNameByCode(officeId));
            }
        }
        return out;
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        OperateKeyProjectsInfoDto operateKeyProjectsInfo = (OperateKeyProjectsInfoDto) dtoObj;
        if (operateKeyProjectsInfo == null) {
            return;
        }

        String proprietorUnit = operateKeyProjectsInfo.getProprietorUnit();
        String projectName = operateKeyProjectsInfo.getProjectName();


        if (StringUtil.isNotEmpty(projectName)) {
            PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, projectName);
            if (prmProjectMain != null) {
                operateKeyProjectsInfo.setProjectShowName(prmProjectMain.getProjectName());
            }
        }

        List<String> lstPartyId = new ArrayList<>();
        if (StringUtil.isNotEmpty(proprietorUnit)) {
            lstPartyId.add(proprietorUnit);
        }
        if (ListUtil.isNotEmpty(lstPartyId)) {
            Map mapPartyId = new HashMap();
            mapPartyId.put(PrmCustomerAttribute.UUID, lstPartyId);
            List<PrmCustomer> lstParty = pcm.findByAnyFields(PrmCustomer.class, mapPartyId, null);
            if (ListUtil.isNotEmpty(lstParty)) {
                for (PrmCustomer party : lstParty) {
                    if (party.getUuid().equals(proprietorUnit)) {
                        operateKeyProjectsInfo.setProprietorUnitDesc(party.getCustomerName());
                    }
                }
            }
        }


    }
}
