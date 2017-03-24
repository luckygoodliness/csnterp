package com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto;

import com.csnt.scdp.bizmodules.entity.cdm.CdmFileRelation;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpFileHelper;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.framework.util.StringUtil;


/**
 * Description:  CdmFileRelationDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-10-23 10:09:49
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.cdm.CdmFileRelation")
public class CdmFileRelationDto extends CdmFileRelation {

    private String convertedFileSize;

    public String getConvertedFileSize() {
        if (StringUtil.isEmpty(convertedFileSize) && getFileSize() > 0) {
            convertedFileSize = ErpFileHelper.convertedFileSize(getFileSize());
            //if1024
            //else 1K
        } else {
            convertedFileSize = "0Byte";
        }
        return convertedFileSize;
    }

    public void setConvertedFileSize(String convertedFileSize) {
        this.convertedFileSize = convertedFileSize;
    }
}