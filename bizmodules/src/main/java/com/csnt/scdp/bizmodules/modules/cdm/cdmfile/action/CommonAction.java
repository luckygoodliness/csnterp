package com.csnt.scdp.bizmodules.modules.cdm.cdmfile.action;

import com.csnt.scdp.bizmodules.entity.cdm.CdmFileRelation;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.services.intf.CdmfileManager;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpFileHelper;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.action.*;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.sysmodules.entity.ScdpFileManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/8.
 */
@Scope("singleton")
@Controller("cdmfile-common")
@Transactional
public class CommonAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(CommonAction.class);

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
                ScdpFileManager fileManager = cdmfileManager.getScdpFileManagerByUUid(fileId);
                CdmFileRelation fileRelation = new CdmFileRelation();
                fileRelation.setDataId(dataId);
                fileRelation.setFileId(fileId);
                fileRelation.setRemark(fileManager.getRemark());
                fileRelation.setFileName(fileManager.getFileName());
                fileRelation.setFileType(fileManager.getFileType());
                fileRelation.setFileSize(fileManager.getFileSize());
                cdmfileManager.saveCdmFileRelation(fileRelation);
                CdmFileRelationDto dto = new CdmFileRelationDto();
                BeanUtil.bean2Bean(fileRelation,dto);
                dto.setConvertedFileSize(ErpFileHelper.convertedFileSize(dto.getFileSize()));
                rsMap.put("CdmFileRelationDto", dto);
                break;
            case "delete":
                cdmfileManager.deleteCdmFileRelationByDataId(dataId, true);
                break;
        }
        rsMap.put("result", true);
        return rsMap;
    }
}
