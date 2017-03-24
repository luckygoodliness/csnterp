package com.csnt.scdp.bizmodules.entity.workflow;

import com.csnt.scdp.framework.dto.BasePojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.math.BigDecimal;

/**
 * Description:  WorkFlowDelete
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-09-27 16:46:36
 */

@javax.persistence.Entity
@Table(name = "WORK_FLOW_DELETE")
public class WorkFlowDelete extends BasePojo {
    private String uuid;
    private String businessKey;
    private String ruAssignee;
    private String hiAssignee;
    private String modules;
    private String stateField;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String locTimezone;
    private String tblVersion;
    private Integer isVoid;
    private String newBusinessKey;
    private String assigneeRecord;
    private String deleteReason;

    @Column(name = "UUID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "BUSINESS_KEY", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    @Column(name = "RU_ASSIGNEE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getRuAssignee() {
        return ruAssignee;
    }

    public void setRuAssignee(String ruAssignee) {
        this.ruAssignee = ruAssignee;
    }

    @Column(name = "HI_ASSIGNEE", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getHiAssignee() {
        return hiAssignee;
    }

    public void setHiAssignee(String hiAssignee) {
        this.hiAssignee = hiAssignee;
    }

    @Column(name = "MODULES", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getModules() {
        return modules;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }

    @Column(name = "STATE_FIELD", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getStateField() {
        return stateField;
    }

    public void setStateField(String stateField) {
        this.stateField = stateField;
    }

    @Column(name = "CREATE_BY", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_BY", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_TIME", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "LOC_TIMEZONE", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    public String getLocTimezone() {
        return locTimezone;
    }

    public void setLocTimezone(String locTimezone) {
        this.locTimezone = locTimezone;
    }

    @Column(name = "TBL_VERSION", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    public String getTblVersion() {
        return tblVersion;
    }

    public void setTblVersion(String tblVersion) {
        this.tblVersion = tblVersion;
    }

    @Column(name = "IS_VOID", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    public Integer getIsVoid() {
        return isVoid;
    }

    public void setIsVoid(Integer isVoid) {
        this.isVoid = isVoid;
    }

    @Column(name = "NEW_BUSINESS_KEY", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    public String getNewBusinessKey() {
        return newBusinessKey;
    }

    public void setNewBusinessKey(String newBusinessKey) {
        this.newBusinessKey = newBusinessKey;
    }

    @Column(name = "ASSIGNEE_RECORD", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    public String getAssigneeRecord() {
        return assigneeRecord;
    }

    public void setAssigneeRecord(String assigneeRecord) {
        this.assigneeRecord = assigneeRecord;
    }

    @Column(name = "DELETE_REASON", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

}