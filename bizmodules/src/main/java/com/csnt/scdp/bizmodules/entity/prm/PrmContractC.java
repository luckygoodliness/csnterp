package com.csnt.scdp.bizmodules.entity.prm;

import com.csnt.scdp.bizmodules.entity.prm.common.PrmAbstractContract;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * Description:  PrmContract
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 09:57:20
 */

@javax.persistence.Entity
@Table(name = "PRM_CONTRACT_C")
public class PrmContractC extends PrmAbstractContract {
    private String prmContractId;
    private String contractStatus;
    private String remark;
    private Date auditTime;
    private Date examDate;

    @Column(name = "PRM_CONTRACT_ID", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getPrmContractId() {
        return prmContractId;
    }

    public void setPrmContractId(String prmContractId) {
        this.prmContractId = prmContractId;
    }

    @Column(name = "CONTRACT_STATUS", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    @Column(name = "REMARK", nullable = true, insertable = true, updatable = true, length = 1000, precision = 0)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "AUDIT_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    @Column(name = "EXAM_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }
}