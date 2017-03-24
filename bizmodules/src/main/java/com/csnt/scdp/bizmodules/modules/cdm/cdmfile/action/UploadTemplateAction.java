package com.csnt.scdp.bizmodules.modules.cdm.cdmfile.action;

import com.csnt.scdp.bizmodules.entityattributes.cdm.CdmFileRelationAttribute;
import com.csnt.scdp.bizmodules.modules.common.intf.CommonServiceManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.CommonFileUploadAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.sysmodules.entity.ScdpFileManager;
import com.csnt.scdp.sysmodules.entityattributes.ScdpFileManagerAttribute;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Scope("singleton")
@Controller("template-fileupload")
@Transactional
public class UploadTemplateAction extends CommonFileUploadAction {
    @Resource(name = "erp-common-service-manager")
    private CommonServiceManager commonServiceManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        HttpServletRequest request = (HttpServletRequest) inMap.get(CommonAttribute.HTTP_REQUEST);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartRequest.getFiles("uploadFile");
        MultipartFile file = files.get(0);
        String fileType = (String) inMap.get(CdmFileRelationAttribute.FILE_TYPE);
        String fileRemark = (String) inMap.get(CdmFileRelationAttribute.REMARK);
        String uuid = (String) inMap.get(CommonAttribute.UUID);
        String fileFormate = (String) inMap.get("fileFormate");
        ScdpFileManager scdpFileManager = commonServiceManager.fileUpload(file, fileType, fileRemark, uuid, fileFormate);
        insertFileRelation(scdpFileManager, inMap);
        Map returnMap = new HashMap();
        return returnMap;
    }

    public void insertFileRelation(ScdpFileManager scdpFileManager, Map inMap){
        Map dataMap = new HashMap<>();
        String cdmFileType = (String) inMap.get(CdmFileRelationAttribute.CDM_FILE_TYPE);
        String fileClassify = (String) inMap.get(CdmFileRelationAttribute.FILE_CLASSIFY);
        dataMap.put(CdmFileRelationAttribute.FILE_ID, scdpFileManager.getUuid());
        dataMap.put(CdmFileRelationAttribute.FILE_TYPE, scdpFileManager.getFileType());
        dataMap.put(CdmFileRelationAttribute.REMARK, scdpFileManager.getRemark());
        dataMap.put(ScdpFileManagerAttribute.COMPANY_CODE, scdpFileManager.getCompanyCode());
        dataMap.put(ScdpFileManagerAttribute.DEPARTMENT_CODE, scdpFileManager.getDepartmentCode());
        dataMap.put(CommonAttribute.SEQ_NO, "1");
        dataMap.put(ScdpFileManagerAttribute.FILE_NAME, scdpFileManager.getFileName());
        dataMap.put(ScdpFileManagerAttribute.FILE_SIZE, scdpFileManager.getFileSize());
        dataMap.put(CdmFileRelationAttribute.CDM_FILE_TYPE, cdmFileType);
        dataMap.put(CdmFileRelationAttribute.FILE_CLASSIFY, fileClassify);
        Map viewMap = new HashMap();
        viewMap.put("cdmFileRelationDto", dataMap);
        BasePojo dtoObj = DtoHelper.getDtoFromMap(viewMap, "com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto");
        dtoObj.setEditflag("+");
        DtoHelper.CascadeSave(dtoObj);
    }

}