package com.csnt.scdp.bizmodules.entity.prm;

import com.csnt.scdp.bizmodules.entity.prm.common.PrmAbstractProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * Description:  PrmProjectMainC
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-27 15:28:19
 */

@javax.persistence.Entity
@Table(name = "PRM_PROJECT_MAIN_C")
public class PrmProjectMainC extends PrmAbstractProjectMain {
    private String prmProjectMainId;
    private String detailType = PrmProjectMainAttribute.PRM_DETAIL_ALL;
    private String reviseReason;
    private Date auditTime;
    private Date examDate;

    @Column(name = "PRM_PROJECT_MAIN_ID", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getPrmProjectMainId() {
        return prmProjectMainId;
    }

    public void setPrmProjectMainId(String prmProjectMainId) {
        this.prmProjectMainId = prmProjectMainId;
    }

    @Column(name = "DETAIL_TYPE", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    @Column(name = "REVISE_REASON", nullable = true, insertable = true, updatable = true, length = 4000, precision = 0)
    public String getReviseReason() {
        return reviseReason;
    }

    public void setReviseReason(String reviseReason) {
        this.reviseReason = reviseReason;
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