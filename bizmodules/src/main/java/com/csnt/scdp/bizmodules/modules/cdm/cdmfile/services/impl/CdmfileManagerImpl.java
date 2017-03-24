package com.csnt.scdp.bizmodules.modules.cdm.cdmfile.services.impl;

import com.csnt.scdp.bizmodules.entity.cdm.CdmFileRelation;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetAppro;
import com.csnt.scdp.bizmodules.entityattributes.cdm.CdmFileRelationAttribute;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.services.intf.CdmfileManager;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpFileManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  CdmfileManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-23 10:09:49
 */

@Scope("singleton")
@Service("cdmfile-manager")
public class CdmfileManagerImpl implements CdmfileManager {

    @Override
    public void setExtraData(Map outMap) {

    }

    @Override
    public Object saveCdmFileRelation(CdmFileRelation fileRelation) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        return pcm.insert(fileRelation);
    }

    @Override
    public void updateCdmFileRelation(CdmFileRelation fileRelation) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        pcm.update(fileRelation);
    }

    @Override
    public void deleteCdmFileRelation(CdmFileRelation fileRelation, boolean scdpFileManagerDelete) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        if (scdpFileManagerDelete) {
            ScdpFileManager scdpFileManager = getScdpFileManagerByUUid(fileRelation.getFileId());
            pcm.delete(scdpFileManager);
        }
        pcm.delete(fileRelation);
    }

    @Override
    public void deleteCdmFileRelationByUUid(String uuid, boolean scdpFileManagerDelete) {
        CdmFileRelation fileRelation = getCdmFileRelationByUUid(uuid);
        deleteCdmFileRelation(fileRelation,scdpFileManagerDelete);
    }

    @Override
    public void deleteCdmFileRelationByDataId(String dataId, boolean scdpFileManagerDelete) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(CdmFileRelationAttribute.DATA_ID, dataId);
        List<CdmFileRelation> cdmFileRelationList = PersistenceFactory.getInstance().findByAnyFields(CdmFileRelation.class, mapConditions, null);
        cdmFileRelationList.forEach(t -> {
            deleteCdmFileRelation(t,scdpFileManagerDelete);
        });
    }

    @Override
    public void deleteCdmFileRelationByCondition(Map<String, Object> mapConditions,boolean scdpFileManagerDelete) {
        List<CdmFileRelation> cdmFileRelationList = PersistenceFactory.getInstance().findByAnyFields(CdmFileRelation.class, mapConditions, null);
        cdmFileRelationList.forEach(t -> {
            deleteCdmFileRelation(t,scdpFileManagerDelete);
        });
    }

    @Override
    public CdmFileRelation getCdmFileRelationByUUid(String uuid) {
        CdmFileRelation cdmFileRelation = null;
        if (StringUtil.isNotEmpty(uuid)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            cdmFileRelation = pcm.findByPK(CdmFileRelation.class, uuid);
        }
        return cdmFileRelation;
    }

    @Override
    public List<CdmFileRelation> getCdmFileRelationByDataId(String dataId) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(CdmFileRelationAttribute.DATA_ID, dataId);
        return PersistenceFactory.getInstance().findByAnyFields(CdmFileRelation.class, mapConditions, null);
    }

    @Override
    public List<CdmFileRelation> getCdmFileRelationByCondition(Map<String, Object> mapConditions) {
        return PersistenceFactory.getInstance().findByAnyFields(CdmFileRelation.class, mapConditions, null);
    }

    @Override
    public ScdpFileManager getScdpFileManagerByUUid(String uuid) {
        ScdpFileManager cdmFileRelation = null;
        if (StringUtil.isNotEmpty(uuid)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            cdmFileRelation = pcm.findByPK(ScdpFileManager.class, uuid);
        }
        return cdmFileRelation;
    }
}