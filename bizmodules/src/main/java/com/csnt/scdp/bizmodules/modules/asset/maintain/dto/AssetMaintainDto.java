package com.csnt.scdp.bizmodules.modules.asset.maintain.dto;

import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.asset.AssetMaintain;

import java.util.List;


/**
 * Description:  AssetMaintainDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-22 20:02:10
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.asset.AssetMaintain")
public class AssetMaintainDto extends AssetMaintain {

    private String cardCode;
    private String assetCode;
    private String assetName;
    private String officeId;

    @CascadeChilds(FK = "dataId|uuid")
    private List<CdmFileRelationDto> cdmFileRelationDto;

    public List<CdmFileRelationDto> getCdmFileRelationDto() {
        return cdmFileRelationDto;
    }

    public void setCdmFileRelationDto(List<CdmFileRelationDto> childDto) {
        this.cdmFileRelationDto = childDto;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }
}