package com.csnt.scdp.bizmodules.modules.scm.scmcontract.action;

import com.csnt.scdp.bizmodules.entity.cdm.CdmFileRelation;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entityattributes.cdm.CdmFileRelationAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpFileHelper;
import com.csnt.scdp.bizmodules.modules.common.intf.CommonServiceManager;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.impl.CashreqManagerImpl;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpFileManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("scmcontract-upload-and-save")
@Transactional
public class FileUploadAndSaveAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(FileUploadAndSaveAction.class);

    @Resource(name = "erp-common-service-manager")
    private CommonServiceManager commonServiceManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        String uuid = (String) inMap.get("dataId");
        if (StringUtil.isEmpty(uuid)) {
            throw new BizException("没有UUID！");
        }
        HttpServletRequest request = (HttpServletRequest) inMap.get(CommonAttribute.HTTP_REQUEST);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartRequest.getFiles("uploadFile");
        MultipartFile file = files.get(0);
        String fileName = file.getOriginalFilename();
        String fileType = (String) inMap.get("fileType");
        String fileRemark = (String) inMap.get("fileRemark");
        String contractUuid = (String) inMap.get("uuid");
        String fileFormate = (String) inMap.get("fileFormate");
        String cdmFileType = (String) inMap.get("cdmFileType");
        String fileClassify = (String) inMap.get("fileClassify");
        String needPersistence = (String) inMap.get(CdmFileRelationAttribute.NEED_PERSISTENCE);

        ScdpFileManager scdpFileManager = commonServiceManager.fileUpload(file, fileType, fileRemark, contractUuid, fileFormate);
        Map outMap = new HashMap();
        Map fileMap = BeanUtil.bean2Map(scdpFileManager);
        CdmFileRelation cdmFileRelation = new CdmFileRelation();
        if (StringUtil.isNotEmpty(cdmFileType)) {
            fileMap.put(CdmFileRelationAttribute.CDM_FILE_TYPE, cdmFileType);
            cdmFileRelation.setCdmFileType(cdmFileType);
        }
        if (StringUtil.isNotEmpty(fileClassify)) {
            fileMap.put(CdmFileRelationAttribute.FILE_CLASSIFY, fileClassify);
            cdmFileRelation.setFileClassify(fileClassify);
        }
        String fileSize = ErpFileHelper.convertedFileSize(scdpFileManager.getFileSize());
        fileMap.put(CdmFileRelationAttribute.CONVERTED_FILE_SIZE, fileSize);
        cdmFileRelation.setFileSize(scdpFileManager.getFileSize());
        cdmFileRelation.setFileType(scdpFileManager.getFileType());
        cdmFileRelation.setFileName(scdpFileManager.getFileName());
        cdmFileRelation.setRemark(scdpFileManager.getRemark());
        cdmFileRelation.setFileId(scdpFileManager.getUuid());
        cdmFileRelation.setDataId(uuid);
        //1.插入数据库
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        pcm.insert(cdmFileRelation);
        outMap.put("fileData", fileMap);

        if ("SCM_CONTRACT_SCAN".equals(fileClassify)) {
            List paramList = new ArrayList<>();
            paramList.add(ScmContractAttribute.CONTRACT_STATE_VALUE_5);
            paramList.add(uuid);
            DAOMeta daoMeta = DAOHelper.getDAO("scmcontract-dao", "update_contractstate_by_pk", paramList);
            pcm.updateByNativeSQL(daoMeta);
        }
//        if ("1".equals(needPersistence)) {
//            Map cdmFileMap = new HashMap();
//            cdmFileMap.putAll(fileMap);
//            cdmFileMap.putAll(inMap);
//            cdmFileMap.put(CdmFileRelationAttribute.DATA_ID, uuid);
//            cdmFileMap.remove("uuid");
//            CdmFileRelation cdmFileRelation = (CdmFileRelation) BeanUtil.map2Dto(cdmFileMap, CdmFileRelation.class);
//            PersistenceFactory.getInstance().insert(cdmFileRelation);
//            fileMap.put("uuid", cdmFileRelation.getUuid());
//        }
        return outMap;
    }
}
