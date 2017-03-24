package com.csnt.scdp.bizmodules.modules.common.action;

import com.csnt.scdp.bizmodules.entity.cdm.CdmFileRelation;
import com.csnt.scdp.bizmodules.entityattributes.cdm.CdmFileRelationAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpFileHelper;
import com.csnt.scdp.bizmodules.modules.common.intf.CommonServiceManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonAddAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpFileManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2015/10/24.
 */
@Scope("singleton")
@Controller("erp-common-file-upload")
@Transactional
public class FileUploadAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(FileUploadAction.class);

    @Resource(name = "erp-common-service-manager")
    private CommonServiceManager commonServiceManager;

    @Override
    public Map perform(Map inMap) {
        HttpServletRequest request = (HttpServletRequest) inMap.get(CommonAttribute.HTTP_REQUEST);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartRequest.getFiles("uploadFile");
        MultipartFile file = files.get(0);
        String fileName = file.getOriginalFilename();
        String fileType = (String) inMap.get("fileType");
        String fileRemark = (String) inMap.get("fileRemark");
        String dataId = (String) inMap.get("dataId");
        String fileFormate = (String) inMap.get("fileFormate");
        String cdmFileType = (String) inMap.get("cdmFileType");
        String fileClassify = (String) inMap.get("fileClassify");
        String begindate = (String) inMap.get("begindate");
        String enddate = (String) inMap.get("enddate");
        String needPersistence = (String) inMap.get(CdmFileRelationAttribute.NEED_PERSISTENCE);

        if (StringUtil.isNotEmpty(fileName)) {
            long fileNameLength = fileName.getBytes().length;
            if (fileNameLength >= 60) {
                throw new BizException("文件名长度请小于30个中文或者60个英文字符");
            }
        }
        ScdpFileManager scdpFileManager = commonServiceManager.fileUpload(file, fileType, fileRemark, dataId, fileFormate);
        Map outMap = new HashMap();
        Map fileMap = BeanUtil.bean2Map(scdpFileManager);
        if (StringUtil.isNotEmpty(cdmFileType)) {
            fileMap.put(CdmFileRelationAttribute.CDM_FILE_TYPE, cdmFileType);
        }
        if (StringUtil.isNotEmpty(fileClassify)) {
            fileMap.put(CdmFileRelationAttribute.FILE_CLASSIFY, fileClassify);
        }
        if (StringUtil.isNotEmpty(begindate)) {
            fileMap.put(CdmFileRelationAttribute.BEGINDATE, begindate);
        }
        if (StringUtil.isNotEmpty(enddate)) {
            fileMap.put(CdmFileRelationAttribute.ENDDATE, enddate);
        }
        fileMap.put(CdmFileRelationAttribute.CONVERTED_FILE_SIZE, ErpFileHelper.convertedFileSize(scdpFileManager.getFileSize()));
        outMap.put("fileData", fileMap);
        fileMap.put(CdmFileRelationAttribute.NEED_PERSISTENCE, needPersistence);

        if ("1".equals(needPersistence)) {
            Map cdmFileMap = new HashMap();
            cdmFileMap.putAll(fileMap);
            cdmFileMap.putAll(inMap);
            cdmFileMap.put(CdmFileRelationAttribute.FILE_ID, fileMap.get("uuid"));
            cdmFileMap.put(CdmFileRelationAttribute.DATA_ID, dataId);
            cdmFileMap.remove("uuid");
            CdmFileRelation cdmFileRelation = (CdmFileRelation) BeanUtil.map2Dto(cdmFileMap, CdmFileRelation.class);
            PersistenceFactory.getInstance().insert(cdmFileRelation);
            fileMap.put("cdmFileRelationId", cdmFileRelation.getUuid());
        }
        return outMap;
    }
}

