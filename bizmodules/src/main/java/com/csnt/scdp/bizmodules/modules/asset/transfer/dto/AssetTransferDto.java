package com.csnt.scdp.bizmodules.modules.asset.transfer.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.asset.AssetTransfer;

import java.util.List;


/**
 * Description:  AssetTransferDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-23 18:08:32
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.asset.AssetTransfer")
public class AssetTransferDto extends AssetTransfer {

    private String cardCode;
    private String outOfficeIdDesc;
    private String inOfficeIdDesc;
    private String outLiablePersonDesc;
    private String inLiablePersonDesc;
    private String state;

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getOutOfficeIdDesc() {
        return outOfficeIdDesc;
    }

    public void setOutOfficeIdDesc(String outOfficeIdDesc) {
        this.outOfficeIdDesc = outOfficeIdDesc;
    }

    public String getInOfficeIdDesc() {
        return inOfficeIdDesc;
    }

    public void setInOfficeIdDesc(String inOfficeIdDesc) {
        this.inOfficeIdDesc = inOfficeIdDesc;
    }

    public String getOutLiablePersonDesc() {
        return outLiablePersonDesc;
    }

    public void setOutLiablePersonDesc(String outLiablePersonDesc) {
        this.outLiablePersonDesc = outLiablePersonDesc;
    }

    public String getInLiablePersonDesc() {
        return inLiablePersonDesc;
    }

    public void setInLiablePersonDesc(String inLiablePersonDesc) {
        this.inLiablePersonDesc = inLiablePersonDesc;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}