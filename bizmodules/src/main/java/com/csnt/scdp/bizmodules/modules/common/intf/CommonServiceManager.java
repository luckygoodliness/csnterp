package com.csnt.scdp.bizmodules.modules.common.intf;

import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.sysmodules.entity.ScdpFileManager;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by shenyouxing on 2015/10/20.
 */
public interface CommonServiceManager {
    /**
     * 根据用户userId，查找用户对象
     */
    public List<ScdpUser> findUserByUserId(String userId);

    /**
     * userIds，查找用户对象
     */
    public List<ScdpUser> findUserByUserIds(List userIds);
    /**
     * 上传文件
     * @param file
     * @param fileType
     * @param fileRemark
     * @param uuid
     * @param fileFormate
     * @return
     */
    public ScdpFileManager fileUpload(MultipartFile file, String fileType, String fileRemark, String uuid, String fileFormate);

    /**
     * 根据uuid的list下载文件
     * @param fileId
     * @return
     */
    public String fileDownload(String fileId);

    Map downLoadSingleFile(Map inMap) throws BizException, SysException;
}
