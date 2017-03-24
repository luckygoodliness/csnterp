package com.csnt.scdp.bizmodules.modules.prm.archiving.services.impl;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmArchivingAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.modules.prm.archiving.services.intf.ArchivingManager;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Description:  ArchivingManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 15:45:10
 */

@Scope("singleton")
@Service("archiving-manager")
public class ArchivingManagerImpl implements ArchivingManager {
    @Override
    public void setExtraData(Map outMap) {
        Map dtoMap = (Map) outMap.get("prmArchivingDto");
        if (dtoMap != null) {
            String projectIdMain = dtoMap.get(PrmArchivingAttribute.PRM_PROJECT_MAIN_ID) + "";
            PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, projectIdMain);
            if(prmProjectMain!=null){
                dtoMap.put(PrmArchivingAttribute.PROJECT_NAME_MAIN, prmProjectMain.getProjectName());
                dtoMap.put(PrmProjectMainAttribute.PRM_CONTRACTOR_OFFICE, prmProjectMain.getContractorOffice());
                dtoMap.put(PrmProjectMainAttribute.PROJECT_CODE, prmProjectMain.getProjectCode());
            }

        }
    }


}