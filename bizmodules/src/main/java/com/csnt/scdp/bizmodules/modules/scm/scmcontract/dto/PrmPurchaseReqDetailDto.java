package com.csnt.scdp.bizmodules.modules.scm.scmcontract.dto;

import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;

import java.math.BigDecimal;
import java.util.List;


/**
 * Description:  PrmPurchaseReqDetailDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail")
public class PrmPurchaseReqDetailDto extends PrmPurchaseReqDetail {

    private BigDecimal totalExpectedMoney;
    private BigDecimal totalBudgetMoney;


    public BigDecimal getTotalBudgetMoney() {
        return totalBudgetMoney;
    }

    public void setTotalExpectedMoney(BigDecimal totalExpectedMoney) {
        this.totalExpectedMoney = totalExpectedMoney;
    }

    public void setTotalBudgetMoney(BigDecimal totalBudgetMoney) {
        this.totalBudgetMoney = totalBudgetMoney;
    }


    public BigDecimal getTotalExpectedMoney() {
        return totalExpectedMoney;
    }


}