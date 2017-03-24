package com.csnt.scdp.bizmodules.modules.common.helper;

import com.csnt.scdp.bizmodules.entity.cdm.CdmFileRelation;
import com.csnt.scdp.bizmodules.entityattributes.cdm.CdmFileRelationAttribute;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.StringUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  ErpFileHelper
 * Copyright: © 2015 Aardwolf Studio. All rights reserved.
 * Company:CSNT中海网络科技股份有限公司
 *
 * @author liujingyuan
 * @version 1.0
 */
public class ErpFileHelper {

    public static String convertedFileSize(Long fileSize) {
        String convertedSize = null;
        //if1024*1024  1M
        if (fileSize >= 1024 * 1024) {
            convertedSize = BigDecimal.valueOf(fileSize).divide(BigDecimal.valueOf(1024 * 1024), 1, RoundingMode
                    .HALF_UP) + "M";
        } else if (fileSize >= 1024) {
            convertedSize = BigDecimal.valueOf(fileSize).divide(BigDecimal.valueOf(1024), 1, RoundingMode.HALF_UP) +
                    "K";
        } else {
            convertedSize = "1B";
        }
        return convertedSize;
    }

    public static List<CdmFileRelation> getCdmFileRelationByDataId(String dataId) {
        if (StringUtil.isEmpty(dataId)) {
            return new ArrayList<CdmFileRelation>();
        } else {
            Map<String, Object> mapConditions = new HashMap<String, Object>();
            mapConditions.put(CdmFileRelationAttribute.DATA_ID, dataId);
            return PersistenceFactory.getInstance().
                    findByAnyFields(CdmFileRelation.class, mapConditions, null);
        }
    }
}

