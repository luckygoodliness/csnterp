package com.csnt.scdp.bizmodules.modules.fad.cashreq.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.cdm.CdmFileRelation;
import com.csnt.scdp.bizmodules.entity.fad.FadCashReq;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.services.intf.CdmfileManager;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf.CashreqManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.sysmodules.entity.ScdpFileManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fisher on 2015/11/10.
 */
@Scope("singleton")
@Controller("cashreq-attachment")
@Transactional
public class AttachmentDealAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "cashreq-manager")
    private CashreqManager cashreqManager;

    @Resource(name = "cdmfile-manager")
    private CdmfileManager cdmfileManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String dataId = (String) inMap.get("dataId");
        String fileId = (String) inMap.get("fileId");
        String actionType = (String) inMap.get("actionType");
        switch (actionType) {
            case "upload":
                cdmfileManager.deleteCdmFileRelationByDataId(dataId,true);
                ScdpFileManager fileManager = cdmfileManager.getScdpFileManagerByUUid(fileId);
                CdmFileRelation fileRelation = new CdmFileRelation();
                fileRelation.setDataId(dataId);
                fileRelation.setFileId(fileId);
                fileRelation.setFileName(fileManager.getFileName());
                fileRelation.setFileType(fileManager.getFileType());
                fileRelation.setFileSize(fileManager.getFileSize());
                cdmfileManager.saveCdmFileRelation(fileRelation);
                break;
            case "delete":
                cdmfileManager.deleteCdmFileRelationByDataId(dataId,true);
                break;
        }
        rsMap.put("result", true);
        return rsMap;
    }
}
