package com.csnt.scdp.bizmodules.entity.prm;

import com.csnt.scdp.bizmodules.entity.prm.common.PrmAbstractProjectMain;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * Description:  PrmProjectMain
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 13:30:43
 */

@javax.persistence.Entity
@Table(name = "PRM_PROJECT_MAIN")
public class PrmProjectMain extends PrmAbstractProjectMain {
    private String purchasePlanState;
    private String sijiNo;
    private Date conferenceDate;

    @Column(name = "PURCHASE_PLAN_STATE", nullable = true, insertable = true, updatable = true, length = 10, precision
            = 0)
    public String getPurchasePlanState() {
        return purchasePlanState;
    }

    public void setPurchasePlanState(String purchasePlanState) {
        this.purchasePlanState = purchasePlanState;
    }

    @Column(name = "SIJI_NO", nullable = true, insertable = true, updatable = true, length = 64, precision = 0)
    public String getSijiNo() {
        return sijiNo;
    }

    public void setSijiNo(String sijiNo) {
        this.sijiNo = sijiNo;
    }

    @Column(name = "CONFERENCE_DATE", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getConferenceDate() {
        return conferenceDate;
    }

    public void setConferenceDate(Date conferenceDate) {
        this.conferenceDate = conferenceDate;
    }
}