package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMainC;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.util.List;
import java.util.Map;


/**
 * Description:  PrmProjectMainCDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-27 15:28:19
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmProjectMainC")
public class PrmProjectMainCDto extends PrmProjectMainC {

    //项目人员计划变更
    @CascadeChilds(FK = "prmProjectMainCId|uuid")
    private List<PrmMemberDetailPCDto> prmMemberDetailPCDto;
    //质量安全计划变更
    @CascadeChilds(FK = "prmProjectMainCId|uuid")
    private List<PrmQsPCDto> prmQsPCDto;
    //开支计划变更
    @CascadeChilds(FK = "prmProjectMainCId|uuid")
    private List<PrmPayDetailPCDto> prmPayDetailPCDto;
    //进度计划变更
    @CascadeChilds(FK = "prmProjectMainCId|uuid")
    private List<PrmProgressDetailPCDto> prmProgressDetailPCDto;
    //结算计划变更
    @CascadeChilds(FK = "prmProjectMainCId|uuid")
    private List<PrmSquareDetailPCDto> prmSquareDetailPCDto;
    //收款计划变更
    @CascadeChilds(FK = "prmProjectMainCId|uuid")
    private List<PrmReceiptsDetailPCDto> prmReceiptsDetailPCDto;
    //关联单位变更
    @CascadeChilds(FK = "prmProjectMainCId|uuid")
    private List<PrmAssociatedUnitsCDto> prmAssociatedUnitsCDto;
    //立项预算变更明细
    @CascadeChilds(FK = "prmProjectMainCId|uuid")
    private List<PrmBudgetDetailCDto> prmBudgetDetailCDto;
    //设备材料预算变更
    @CascadeChilds(FK = "prmProjectMainCId|uuid")
    private List<PrmBudgetPrincipalCDto> prmBudgetPrincipalCDto;
    //辅助材料预算变更
    @CascadeChilds(FK = "prmProjectMainCId|uuid")
    private List<PrmBudgetAccessoryCDto> prmBudgetAccessoryCDto;
    //外协预算变更
    @CascadeChilds(FK = "prmProjectMainCId|uuid")
    private List<PrmBudgetOutsourceCDto> prmBudgetOutsourceCDto;
    //运行成本预算变更
    @CascadeChilds(FK = "prmProjectMainCId|uuid")
    private List<PrmBudgetRunCDto> prmBudgetRunCDto;
    //附件
    @CascadeChilds(FK = "dataId|uuid")
    private List<CdmFileRelationDto> cdmFileRelationDto;
    //合同
    @CascadeChilds(FK = "prmProjectMainCId|uuid")
    private List<PrmContractDetailCDto> prmContractDetailCDto;

    private String prmContractIdDesc;
    private String projectManagerDesc;
    private String contractorOfficeDesc;

    private String changedFields;
    private Map headerChangedValues;
    private String projectManagerRoleUuid;


    public List<PrmMemberDetailPCDto> getPrmMemberDetailPCDto() {
        return prmMemberDetailPCDto;
    }

    public void setPrmMemberDetailPCDto(List<PrmMemberDetailPCDto> childDto) {
        this.prmMemberDetailPCDto = childDto;
    }

    public List<PrmQsPCDto> getPrmQsPCDto() {
        return prmQsPCDto;
    }

    public void setPrmQsPCDto(List<PrmQsPCDto> childDto) {
        this.prmQsPCDto = childDto;
    }

    public List<PrmPayDetailPCDto> getPrmPayDetailPCDto() {
        return prmPayDetailPCDto;
    }

    public void setPrmPayDetailPCDto(List<PrmPayDetailPCDto> childDto) {
        this.prmPayDetailPCDto = childDto;
    }

    public List<PrmProgressDetailPCDto> getPrmProgressDetailPCDto() {
        return prmProgressDetailPCDto;
    }

    public void setPrmProgressDetailPCDto(List<PrmProgressDetailPCDto> childDto) {
        this.prmProgressDetailPCDto = childDto;
    }

    public List<PrmSquareDetailPCDto> getPrmSquareDetailPCDto() {
        return prmSquareDetailPCDto;
    }

    public void setPrmSquareDetailPCDto(List<PrmSquareDetailPCDto> childDto) {
        this.prmSquareDetailPCDto = childDto;
    }

    public List<PrmReceiptsDetailPCDto> getPrmReceiptsDetailPCDto() {
        return prmReceiptsDetailPCDto;
    }

    public void setPrmReceiptsDetailPCDto(List<PrmReceiptsDetailPCDto> childDto) {
        this.prmReceiptsDetailPCDto = childDto;
    }

    public List<PrmAssociatedUnitsCDto> getPrmAssociatedUnitsCDto() {
        return prmAssociatedUnitsCDto;
    }

    public void setPrmAssociatedUnitsCDto(List<PrmAssociatedUnitsCDto> prmAssociatedUnitsCDto) {
        this.prmAssociatedUnitsCDto = prmAssociatedUnitsCDto;
    }

    public List<PrmBudgetDetailCDto> getPrmBudgetDetailCDto() {
        return prmBudgetDetailCDto;
    }

    public void setPrmBudgetDetailCDto(List<PrmBudgetDetailCDto> childDto) {
        this.prmBudgetDetailCDto = childDto;
    }

    public List<PrmBudgetPrincipalCDto> getPrmBudgetPrincipalCDto() {
        return prmBudgetPrincipalCDto;
    }

    public void setPrmBudgetPrincipalCDto(List<PrmBudgetPrincipalCDto> childDto) {
        this.prmBudgetPrincipalCDto = childDto;
    }

    public List<PrmBudgetAccessoryCDto> getPrmBudgetAccessoryCDto() {
        return prmBudgetAccessoryCDto;
    }

    public void setPrmBudgetAccessoryCDto(List<PrmBudgetAccessoryCDto> childDto) {
        this.prmBudgetAccessoryCDto = childDto;
    }

    public List<PrmBudgetOutsourceCDto> getPrmBudgetOutsourceCDto() {
        return prmBudgetOutsourceCDto;
    }

    public void setPrmBudgetOutsourceCDto(List<PrmBudgetOutsourceCDto> childDto) {
        this.prmBudgetOutsourceCDto = childDto;
    }

    public List<PrmBudgetRunCDto> getPrmBudgetRunCDto() {
        return prmBudgetRunCDto;
    }

    public void setPrmBudgetRunCDto(List<PrmBudgetRunCDto> childDto) {
        this.prmBudgetRunCDto = childDto;
    }

    public String getPrmContractIdDesc() {
        return prmContractIdDesc;
    }

    public void setPrmContractIdDesc(String prmContractIdDesc) {
        this.prmContractIdDesc = prmContractIdDesc;
    }

    public String getProjectManagerDesc() {
        return projectManagerDesc;
    }

    public void setProjectManagerDesc(String projectManagerDesc) {
        this.projectManagerDesc = projectManagerDesc;
    }
    public String getContractorOfficeDesc() {
        return contractorOfficeDesc;
    }

    public void setContractorOfficeDesc(String contractorOfficeDesc) {
        this.contractorOfficeDesc = contractorOfficeDesc;
    }

    public String getChangedFields() {
        return changedFields;
    }

    public void setChangedFields(String changedFields) {
        this.changedFields = changedFields;
    }

    public Map getHeaderChangedValues() {
        return headerChangedValues;
    }

    public void setHeaderChangedValues(Map headerChangedValues) {
        this.headerChangedValues = headerChangedValues;
    }

    public String getProjectManagerRoleUuid() {
        return projectManagerRoleUuid;
    }

    public void setProjectManagerRoleUuid(String projectManagerRoleUuid) {
        this.projectManagerRoleUuid = projectManagerRoleUuid;
    }

    public List<CdmFileRelationDto> getCdmFileRelationDto() {
        return cdmFileRelationDto;
    }

    public void setCdmFileRelationDto(List<CdmFileRelationDto> childDto) {
        this.cdmFileRelationDto = childDto;
    }

    public List<PrmContractDetailCDto> getPrmContractDetailCDto() {
        return prmContractDetailCDto;
    }

    public void setPrmContractDetailCDto(List<PrmContractDetailCDto> prmContractDetailCDto) {
        this.prmContractDetailCDto = prmContractDetailCDto;
    }
}