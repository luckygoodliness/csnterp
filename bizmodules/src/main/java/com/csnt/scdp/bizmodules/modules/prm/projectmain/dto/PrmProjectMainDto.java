package com.csnt.scdp.bizmodules.modules.prm.projectmain.dto;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;

import java.math.BigDecimal;
import java.util.List;


/**
 * Description:  PrmProjectMainDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-26 13:30:43
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain")
public class PrmProjectMainDto extends PrmProjectMain {
    //  人员计划
    @CascadeChilds(FK = "prmProjectMainId|uuid")
    private List<PrmMemberDetailPDto> prmMemberDetailPDto;
    //开支计划
    @CascadeChilds(FK = "prmProjectMainId|uuid")
    private List<PrmPayDetailPDto> prmPayDetailPDto;
    //进度计划
    @CascadeChilds(FK = "prmProjectMainId|uuid")
    private List<PrmProgressDetailPDto> prmProgressDetailPDto;
    //结算计划
    @CascadeChilds(FK = "prmProjectMainId|uuid")
    private List<PrmSquareDetailPDto> prmSquareDetailPDto;
    //收款计划
    @CascadeChilds(FK = "prmProjectMainId|uuid")
    private List<PrmReceiptsDetailPDto> prmReceiptsDetailPDto;
    //质量安全计划
    @CascadeChilds(FK = "prmProjectMainId|uuid")
    private List<PrmQsPDto> prmQsPDto;
    //关联单位
    @CascadeChilds(FK = "prmProjectMainId|uuid")
    private List<PrmAssociatedUnitsDto> prmAssociatedUnitsDto;
    //立项预算明细
    @CascadeChilds(FK = "prmProjectMainId|uuid")
    private List<PrmBudgetDetailDto> prmBudgetDetailDto;
    //设备材料预算
    @CascadeChilds(FK = "prmProjectMainId|uuid")
    private List<PrmBudgetPrincipalDto> prmBudgetPrincipalDto;
    //辅助材料预算
    @CascadeChilds(FK = "prmProjectMainId|uuid")
    private List<PrmBudgetAccessoryDto> prmBudgetAccessoryDto;
    //外协预算
    @CascadeChilds(FK = "prmProjectMainId|uuid")
    private List<PrmBudgetOutsourceDto> prmBudgetOutsourceDto;
    //运行成本预算
    @CascadeChilds(FK = "prmProjectMainId|uuid")
    private List<PrmBudgetRunDto> prmBudgetRunDto;
    //合同
    @CascadeChilds(FK = "prmProjectMainId|uuid")
    private List<PrmContractDetailDto> prmContractDetailDto;
    private String prmContractIdDesc;
    private String projectManagerDesc;
    private String customerIdDesc;
    private BigDecimal contractSignMoney;
    private String contractorOfficeDesc;

    public List<PrmMemberDetailPDto> getPrmMemberDetailPDto() {
        return prmMemberDetailPDto;
    }

    public void setPrmMemberDetailPDto(List<PrmMemberDetailPDto> childDto) {
        this.prmMemberDetailPDto = childDto;
    }

    public List<PrmPayDetailPDto> getPrmPayDetailPDto() {
        return prmPayDetailPDto;
    }

    public void setPrmPayDetailPDto(List<PrmPayDetailPDto> childDto) {
        this.prmPayDetailPDto = childDto;
    }

    public List<PrmProgressDetailPDto> getPrmProgressDetailPDto() {
        return prmProgressDetailPDto;
    }

    public void setPrmProgressDetailPDto(List<PrmProgressDetailPDto> childDto) {
        this.prmProgressDetailPDto = childDto;
    }

    public List<PrmSquareDetailPDto> getPrmSquareDetailPDto() {
        return prmSquareDetailPDto;
    }

    public void setPrmSquareDetailPDto(List<PrmSquareDetailPDto> childDto) {
        this.prmSquareDetailPDto = childDto;
    }

    public List<PrmReceiptsDetailPDto> getPrmReceiptsDetailPDto() {
        return prmReceiptsDetailPDto;
    }

    public void setPrmReceiptsDetailPDto(List<PrmReceiptsDetailPDto> childDto) {
        this.prmReceiptsDetailPDto = childDto;
    }

    public List<PrmQsPDto> getPrmQsPDto() {
        return prmQsPDto;
    }

    public void setPrmQsPDto(List<PrmQsPDto> childDto) {
        this.prmQsPDto = childDto;
    }

    public List<PrmAssociatedUnitsDto> getPrmAssociatedUnitsDto() {
        return prmAssociatedUnitsDto;
    }

    public void setPrmAssociatedUnitsDto(List<PrmAssociatedUnitsDto> prmAssociatedUnitsDto) {
        this.prmAssociatedUnitsDto = prmAssociatedUnitsDto;
    }

    public List<PrmBudgetDetailDto> getPrmBudgetDetailDto() {
        return prmBudgetDetailDto;
    }

    public void setPrmBudgetDetailDto(List<PrmBudgetDetailDto> childDto) {
        this.prmBudgetDetailDto = childDto;
    }

    public List<PrmBudgetPrincipalDto> getPrmBudgetPrincipalDto() {
        return prmBudgetPrincipalDto;
    }

    public void setPrmBudgetPrincipalDto(List<PrmBudgetPrincipalDto> childDto) {
        this.prmBudgetPrincipalDto = childDto;
    }

    public List<PrmBudgetAccessoryDto> getPrmBudgetAccessoryDto() {
        return prmBudgetAccessoryDto;
    }

    public void setPrmBudgetAccessoryDto(List<PrmBudgetAccessoryDto> childDto) {
        this.prmBudgetAccessoryDto = childDto;
    }

    public List<PrmBudgetOutsourceDto> getPrmBudgetOutsourceDto() {
        return prmBudgetOutsourceDto;
    }

    public void setPrmBudgetOutsourceDto(List<PrmBudgetOutsourceDto> childDto) {
        this.prmBudgetOutsourceDto = childDto;
    }

    public List<PrmBudgetRunDto> getPrmBudgetRunDto() {
        return prmBudgetRunDto;
    }

    public void setPrmBudgetRunDto(List<PrmBudgetRunDto> childDto) {
        this.prmBudgetRunDto = childDto;
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

    public String getCustomerIdDesc() {
        return customerIdDesc;
    }

    public void setCustomerIdDesc(String customerIdDesc) {
        this.customerIdDesc = customerIdDesc;
    }

    public BigDecimal getContractSignMoney() {
        return contractSignMoney;
    }

    public void setContractSignMoney(BigDecimal contractSignMoney) {
        this.contractSignMoney = contractSignMoney;
    }

    public String getContractorOfficeDesc() {
        return contractorOfficeDesc;
    }

    public void setContractorOfficeDesc(String contractorOfficeDesc) {
        this.contractorOfficeDesc = contractorOfficeDesc;
    }

    public List<PrmContractDetailDto> getPrmContractDetailDto() {
        return prmContractDetailDto;
    }

    public void setPrmContractDetailDto(List<PrmContractDetailDto> prmContractDetailDto) {
        this.prmContractDetailDto = prmContractDetailDto;
    }
}