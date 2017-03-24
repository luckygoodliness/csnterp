package com.csnt.scdp.bizmodules.modules.cdm.cdmfile.services.intf;

import com.csnt.scdp.bizmodules.entity.cdm.CdmFileRelation;
import com.csnt.scdp.sysmodules.entity.ScdpFileManager;

import java.util.List;
import java.util.Map;

/**
 * Description:  CdmfileManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-23 10:09:49
 */
public interface CdmfileManager {
    /**
     * 设置CODE需要显示NAME的字段
     *
     * @param outMap
     */
    void setExtraData(Map outMap);

    void updateCdmFileRelation(CdmFileRelation fileRelation);

    Object saveCdmFileRelation(CdmFileRelation fileRelation);

    void deleteCdmFileRelationByUUid(String uuid, boolean scdpFileManagerDelete);

    void deleteCdmFileRelation(CdmFileRelation fileRelation, boolean scdpFileManagerDelete);

    void deleteCdmFileRelationByDataId(String dataId, boolean scdpFileManagerDelete);

    void deleteCdmFileRelationByCondition(Map<String, Object> mapConditions,boolean scdpFileManagerDelete);

    CdmFileRelation getCdmFileRelationByUUid(String uuid);

    List<CdmFileRelation> getCdmFileRelationByDataId(String dataId);

    List<CdmFileRelation> getCdmFileRelationByCondition(Map<String, Object> mapConditions);

    ScdpFileManager getScdpFileManagerByUUid(String uuid);
}