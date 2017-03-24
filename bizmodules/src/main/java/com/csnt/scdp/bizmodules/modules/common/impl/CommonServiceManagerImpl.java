package com.csnt.scdp.bizmodules.modules.common.impl;

import com.csnt.scdp.bizmodules.entity.cdm.CdmFileRelation;
import com.csnt.scdp.bizmodules.entityattributes.cdm.CdmFileRelationAttribute;
import com.csnt.scdp.bizmodules.modules.common.intf.CommonServiceManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DFSFileHelper;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpFileManager;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.FileManagerHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shenyouxing on 2015/10/20.
 */
@Scope("singleton")
@Service("erp-common-service-manager")
public class CommonServiceManagerImpl implements CommonServiceManager {
    private static ILogTracer tracer = LogTracerFactory.getInstance(CommonServiceManagerImpl.class);

    /**
     * 根据用户userId，查找用户对象
     */
    @Override
    public List<ScdpUser> findUserByUserId(String userId) {
        Map mapCon = new HashMap();
        mapCon.put(ScdpUserAttribute.USER_ID, userId);
        return PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, mapCon, null);
    }

    @Override
    public List<ScdpUser> findUserByUserIds(List userIds) {
        QueryCondition condition1 = new QueryCondition();
        condition1.setField(ScdpUserAttribute.USER_ID);
        condition1.setOperator("in");
        condition1.setValueList(userIds);
        List<QueryCondition> lstCondition = new ArrayList<QueryCondition>();
        lstCondition.add(condition1);
        return PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, lstCondition, null);
    }

    @Override
    public ScdpFileManager fileUpload(MultipartFile file, String fileType, String fileRemark, String uuid, String fileFormate) throws BizException, SysException {
        long fileSize = file.getSize();
        String fileName = file.getOriginalFilename();
        try {
            Map<String, String> metaMap = new HashMap();
            metaMap.put(CommonAttribute.FILE_NAME, fileName);
            metaMap.put(CommonAttribute.FILE_TYPE, fileType);
            metaMap.put(CommonAttribute.COMPANY_CODE, UserHelper.getCompanyCode());
            metaMap.put(CommonAttribute.DEPARTMENT_CODE, UserHelper.getOrgCode());
            metaMap.put(CommonAttribute.CREATE_BY, UserHelper.getUserId());
            Map map = DFSFileHelper.upload(file.getBytes(), fileType, metaMap);

            String groupName = (String) map.get(CommonAttribute.GROUP_NAME);
            String filePath = (String) map.get(CommonAttribute.FILE_PATH);

            ScdpFileManager scdpFileManager = new ScdpFileManager();
            scdpFileManager.setFileName(fileName);
            scdpFileManager.setFileType(fileType);
            scdpFileManager.setFileSize(fileSize);
            scdpFileManager.setRemark(fileRemark);
            scdpFileManager.setDfsGroupName(groupName);
            scdpFileManager.setDfsFilePath(filePath);
            String newUuid = (String) PersistenceFactory.getInstance().insert(scdpFileManager);
            scdpFileManager.setUuid(newUuid);

            return scdpFileManager;
        } catch (Exception e) {
            throw new SysException(e);
        }
    }

    @Override
    public String fileDownload(String fileId) {
        return FileManagerHelper.getFileDownloadUrl(fileId);
    }


    @Override
    public Map downLoadSingleFile(Map inMap) throws BizException, SysException {
        Map outMap = new HashMap();
        Map<String, Object> queryMap = new HashMap();
        String cdmFileType = (String) inMap.get(CdmFileRelationAttribute.CDM_FILE_TYPE);
        String fileClassify = (String) inMap.get(CdmFileRelationAttribute.FILE_CLASSIFY);
        String fileId = (String) inMap.get(CdmFileRelationAttribute.FILE_ID);
        String uuid = (String) inMap.get(CommonAttribute.UUID);
        if (StringUtil.isNotEmpty(fileId)) {
            queryMap.put(CdmFileRelationAttribute.FILE_ID, fileId);
        } else {
            if (StringUtil.isNotEmpty(uuid)) {
                queryMap.put(CommonAttribute.UUID, uuid);
            }
            if (StringUtil.isNotEmpty(cdmFileType)) {
                queryMap.put(CdmFileRelationAttribute.CDM_FILE_TYPE, cdmFileType);
            }
            if (StringUtil.isNotEmpty(fileClassify)) {
                queryMap.put(CdmFileRelationAttribute.FILE_CLASSIFY, fileClassify);
            }
            if (StringUtil.isNotEmpty(fileClassify) && StringUtil.isNotEmpty(cdmFileType)) {
                List lstResult = PersistenceFactory.getInstance().findByAnyFields(CdmFileRelation.class, queryMap, "createTime");
                if (ListUtil.isNotEmpty(lstResult)) {
                    fileId = ((CdmFileRelation) lstResult.get(lstResult.size() - 1)).getFileId();
                }
            }
        }
        if (StringUtil.isNotEmpty(fileId)) {
            String fileUrl = fileDownload(fileId);
            List fileUrlList = new ArrayList<>();
            if (!StringUtil.isEmpty(fileUrl)) {
                fileUrlList.add(fileUrl);
                outMap.put("URL_LIST", fileUrlList);
            } else {
                throw new BizException("未找到文件", new HashMap());
            }
        }
        return outMap;
    }
}
