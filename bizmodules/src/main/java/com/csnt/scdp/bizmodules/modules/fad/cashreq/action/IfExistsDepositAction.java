package com.csnt.scdp.bizmodules.modules.fad.cashreq.action;

import com.csnt.scdp.bizmodules.entity.cdm.CdmFileRelation;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.services.intf.CdmfileManager;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf.CashreqManager;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmProjectMainDto;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.entity.ScdpFileManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fisher on 2015/11/10.
 */
@Scope("singleton")
@Controller("cashreq-if-exists-deposit")
@Transactional
public class IfExistsDepositAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String projectId = (String)inMap.get("projectId");
        PrmProjectMainDto projectMainDto = (PrmProjectMainDto) DtoHelper.findDtoByPK(PrmProjectMainDto.class, projectId);
        String officeId  = projectMainDto.getContractorOffice();
        Map contextMap = new HashMap();
        contextMap.put("projectId", projectId);
        contextMap.put("officeId", officeId);
        DAOMeta daoMeta = DAOHelper.getDAO("cashreq-dao", "if-exists-deposit", null, contextMap);
        List<Map<String,Object>> lst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if(ListUtil.isNotEmpty(lst)) {
            if(lst.size() > 0) {
                rsMap.put("result", lst.size());
                return rsMap;
            }
        }

        rsMap.put("result", 0);
        return rsMap;
    }
}
