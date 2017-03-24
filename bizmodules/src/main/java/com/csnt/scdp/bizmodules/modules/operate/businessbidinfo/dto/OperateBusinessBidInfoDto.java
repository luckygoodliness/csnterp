package com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmContractC;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.operate.OperateBusinessBidInfo;

import java.util.Date;
import java.util.List;


/**
 * Description:  OperateBusinessBidInfoDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-28 18:18:49
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.operate.OperateBusinessBidInfo")
public class OperateBusinessBidInfoDto extends OperateBusinessBidInfo {
    @CascadeChilds(FK = "dataId|uuid")
    private List<CdmFileRelationDto> cdmFileRelationDto;

    private String customerIdDesc;
    private String operateByName;//经办人
    private String contractorOfficeDesc;

    public String getProjectManagerDesc() {
        return projectManagerDesc;
    }

    public void setProjectManagerDesc(String projectManagerDesc) {
        this.projectManagerDesc = projectManagerDesc;
    }

    public String getGeneralEngineerDesc() {
        return generalEngineerDesc;
    }

    public void setGeneralEngineerDesc(String generalEngineerDesc) {
        this.generalEngineerDesc = generalEngineerDesc;
    }

    public String getDesignerIdDesc() {
        return designerIdDesc;
    }

    public void setDesignerIdDesc(String designerIdDesc) {
        this.designerIdDesc = designerIdDesc;
    }

    public String getManagementIdDesc() {
        return managementIdDesc;
    }

    public void setManagementIdDesc(String managementIdDesc) {
        this.managementIdDesc = managementIdDesc;
    }

    public Date getSuccessBidDate() {
        return successBidDate;
    }

    public void setSuccessBidDate(Date successBidDate) {
        this.successBidDate = successBidDate;
    }

    private String projectManagerDesc;
    private String generalEngineerDesc;
    private String designerIdDesc;
    private String managementIdDesc;
    private Date successBidDate;

    private int canBeModified;//  是否可以修改

    private int canBeDeleted;//  是否可以删除

    private int canMoneyBeModified;//  是否投标保证金可以修改

    public int getCanBeModified() {
        return canBeModified;
    }

    public void setCanBeModified(int canBeModified) {
        this.canBeModified = canBeModified;
    }

    public int getCanBeDeleted() {
        return canBeDeleted;
    }

    public void setCanBeDeleted(int canBeDeleted) {
        this.canBeDeleted = canBeDeleted;
    }

    public List<CdmFileRelationDto> getCdmFileRelationDto() {
        return cdmFileRelationDto;
    }

    public void setCdmFileRelationDto(List<CdmFileRelationDto> cdmFileRelationDto) {
        this.cdmFileRelationDto = cdmFileRelationDto;
    }

    public String getCustomerIdDesc() {
        return customerIdDesc;
    }

    public void setCustomerIdDesc(String customerIdDesc) {
        this.customerIdDesc = customerIdDesc;
    }

    public String getOperateByName() {
        return operateByName;
    }

    public void setOperateByName(String operateByName) {
        this.operateByName = operateByName;
    }

    public String getContractorOfficeDesc() {
        return contractorOfficeDesc;
    }

    public void setContractorOfficeDesc(String contractorOfficeDesc) {
        this.contractorOfficeDesc = contractorOfficeDesc;
    }

    public int getCanMoneyBeModified() {
        return canMoneyBeModified;
    }

    public void setCanMoneyBeModified(int canMoneyBeModified) {
        this.canMoneyBeModified = canMoneyBeModified;
    }
}