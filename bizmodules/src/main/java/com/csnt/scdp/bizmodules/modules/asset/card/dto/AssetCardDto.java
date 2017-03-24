package com.csnt.scdp.bizmodules.modules.asset.card.dto;

import com.csnt.scdp.bizmodules.modules.asset.check.dto.AssetAnnualCheckDto;


import com.csnt.scdp.bizmodules.modules.asset.maintain.dto.AssetMaintainDto;
import com.csnt.scdp.bizmodules.modules.asset.transfer.dto.AssetTransferDto;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.asset.AssetCard;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import javax.annotation.Resource;
import java.util.List;


/**
 * Description:  AssetCardDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-22 10:18:39
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.asset.AssetCard")
public class AssetCardDto extends AssetCard {

    @CascadeChilds(FK = "dataId|uuid")
    private List<CdmFileRelationDto> cdmFileRelationDto;

    @CascadeChilds(FK = "cardUuid|uuid")//子表的外键/子表主键
    private List<AssetAnnualCheckDto> assetAnnualCheckDto;

    @CascadeChilds(FK = "cardUuid|uuid")
    private List<AssetMaintainDto> assetMaintainDto;

    @CascadeChilds(FK = "cardUuid|uuid")
    private List<AssetTransferDto> assetTransferDto;

    public List<CdmFileRelationDto> getCdmFileRelationDto() {
        return cdmFileRelationDto;
    }

    public void setCdmFileRelationDto(List<CdmFileRelationDto> childDto) {
        this.cdmFileRelationDto = childDto;
    }

    public List<AssetAnnualCheckDto> getAssetAnnualCheckDto() {
        return assetAnnualCheckDto;
    }

    public void setAssetAnnualCheckDto(List<AssetAnnualCheckDto> assetAnnualCheckDto) {
        this.assetAnnualCheckDto = assetAnnualCheckDto;
    }

    public List<AssetMaintainDto> getAssetMaintainDto() {
        /*if (assetMaintainDto != null) {
            for (int i = 0; i < assetMaintainDto.size(); i++) {
                AssetMaintainDto assetMaintainDtoObj = assetMaintainDto.get(i);
                if (!isNullReturnEmpty(assetMaintainDtoObj.getState()).equals("2")) {
                    assetMaintainDto.remove(assetMaintainDtoObj);
                }
            }
        }*/
        return assetMaintainDto;
    }

    public void setAssetMaintainDto(List<AssetMaintainDto> assetMaintainDto) {
        this.assetMaintainDto = assetMaintainDto;
    }

    public List<AssetTransferDto> getAssetTransferDto() {
        if (assetTransferDto != null) {
            for (int i = 0; i < assetTransferDto.size(); i++) {
                AssetTransferDto assetTransferDtoObj = assetTransferDto.get(i);
                if (!isNullReturnEmpty(assetTransferDtoObj.getState()).equals("2")) {
                    assetTransferDto.remove(assetTransferDtoObj);
                }
            }
        }
        return assetTransferDto;
    }

    public void setAssetTransferDto(List<AssetTransferDto> assetTransferDto) {
        this.assetTransferDto = assetTransferDto;
    }

    private String isNullReturnEmpty(Object value) {
        if (value == null || value.equals("null")) {
            return "";
        } else {
            return value.toString().trim();
        }
    }
}