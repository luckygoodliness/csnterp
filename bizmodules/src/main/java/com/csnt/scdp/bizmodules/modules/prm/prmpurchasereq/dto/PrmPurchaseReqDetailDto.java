package com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.math.BigDecimal;

/**
 * Description:  PrmPurchaseReqDetailDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-26 15:30:52
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail")
public class PrmPurchaseReqDetailDto extends PrmPurchaseReqDetail {

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String packageName = "";

    public BigDecimal getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(BigDecimal planAmount) {
        this.planAmount = planAmount;
    }

    public BigDecimal planAmount;

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }

    public String isUploaded = "";
    public String fadSubjectCode;

    public String getFadSubjectCode() {
        return fadSubjectCode;
    }

    public void setFadSubjectCode(String fadSubjectCode) {
        this.fadSubjectCode = fadSubjectCode;
    }
}