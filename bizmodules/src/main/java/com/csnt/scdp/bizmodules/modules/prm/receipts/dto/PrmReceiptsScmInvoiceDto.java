package com.csnt.scdp.bizmodules.modules.prm.receipts.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmReceiptsScmInvoice;
import com.csnt.scdp.bizmodules.modules.prm.receipts.services.impl.ReceiptsManagerImpl;
import com.csnt.scdp.bizmodules.modules.prm.receipts.services.intf.ReceiptsManager;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * Description:  PrmReceiptsScmInvoiceDto
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2016-08-11 14:40:38
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmReceiptsScmInvoice")
public class PrmReceiptsScmInvoiceDto extends PrmReceiptsScmInvoice {

    private String officeId;
    private String officeIdDesc;
    private String projectCode;
    private String projectName;
    private String scmContractUuidDesc;
    private BigDecimal needPayMoney;
    private BigDecimal needPayMoneyLock;

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeIdDesc() {
        return officeIdDesc;
    }

    public void setOfficeIdDesc(String officeIdDesc) {
        this.officeIdDesc = officeIdDesc;
    }

    public String getProjectCode() {
        return this.projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getScmContractUuidDesc() {
        return this.scmContractUuidDesc;
    }

    public void setScmContractUuidDesc(String scmContractUuidDesc) {
        this.scmContractUuidDesc = scmContractUuidDesc;
    }

    public BigDecimal getNeedPayMoney() {
        return this.needPayMoney;
    }

    public void setNeedPayMoney(BigDecimal needPayMoney) {
        this.needPayMoney = needPayMoney;
    }

    public BigDecimal getNeedPayMoneyLock() {
        return this.needPayMoneyLock;
    }

    public void setNeedPayMoneyLock(BigDecimal needPayMoneyLock) {
        this.needPayMoneyLock = needPayMoneyLock;
    }

}