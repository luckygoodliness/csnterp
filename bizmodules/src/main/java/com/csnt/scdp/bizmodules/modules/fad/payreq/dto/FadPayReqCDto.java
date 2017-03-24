package com.csnt.scdp.bizmodules.modules.fad.payreq.dto;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.attributes.CommonAttribute;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqC;
import com.csnt.scdp.framework.util.MathUtil;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


/**
 * Description:  FadPayReqCDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-26 17:42:25
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.fad.FadPayReqC")
public class FadPayReqCDto extends FadPayReqC {

    //组织部门
    private String orgName;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    private  String departmentName;
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    //合同类型
    private String contractNature;
    public String getContractNature() {
        return contractNature;
    }

    public void setContractNature(String contractNature) {
        this.contractNature = contractNature;
    }

    //供应商名称
    private String supplierName;

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    //项目名称
    private String projectMainName;

    public String getProjectMainName() {
        return projectMainName;
    }

    public void setProjectMainName(String projectMainName) {
        this.projectMainName = projectMainName;
    }

    //项目代号
    private String projectCode;

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    //项目运行金额
    private BigDecimal prmMoney;

    public BigDecimal getPrmMoney() {
        return prmMoney;
    }

    public void setPrmMoney(BigDecimal prmMoney) {
        this.prmMoney = prmMoney;
    }

    //项目实际收款
    private BigDecimal prmActualMoney;

    public BigDecimal getPrmActualMoney() {
        return prmActualMoney;
    }

    public void setPrmActualMoney(BigDecimal prmActualMoney) {
        this.prmActualMoney = prmActualMoney;
    }

    //项目收款比例
    private BigDecimal prmReceiptRate;

    public BigDecimal getPrmReceiptRate() {
        if (getPrmActualMoney() == null || BigDecimal.ZERO.compareTo(getPrmActualMoney()) == 0 ||
                getPrmMoney() == null || BigDecimal.ZERO.compareTo(getPrmMoney()) == 0) {
            return BigDecimal.ZERO;
        } else {
            return (getPrmActualMoney().multiply(new BigDecimal("100")).divide(getPrmMoney(), 2, BigDecimal.ROUND_HALF_UP));
        }
    }

    //采购合同代码
    private String scmContractCode;

    public String getScmContractCode() {
        return scmContractCode;
    }

    public void setScmContractCode(String scmContractCode) {
        this.scmContractCode = scmContractCode;
    }

    //采购合同金额
    private BigDecimal scmContractAmount;

    public BigDecimal getScmContractAmount() {
        return scmContractAmount;
    }

    public void setScmContractAmount(BigDecimal scmContractAmount) {
        this.scmContractAmount = scmContractAmount;
    }

    //采购合同到货确认金额
    private BigDecimal scmContractCheckedAmount;

    public BigDecimal getScmContractCheckedAmount() {
        return scmContractCheckedAmount;
    }

    public void setScmContractCheckedAmount(BigDecimal scmContractCheckedAmount) {
        this.scmContractCheckedAmount = scmContractCheckedAmount;
    }

    //采购合同已支付金额
    private BigDecimal scmContractPaidMoney;

    public BigDecimal getScmContractPaidMoney() {
//        if (Arrays.binarySearch(BillStateAttribute.FAD_BILL_STATE_AUDIT, getState()) >= 0) {
//            return getScmContractThenPaid();
//        } else {
        return scmContractPaidMoney;
//        }
    }

    public void setScmContractPaidMoney(BigDecimal scmContractPaidMoney) {
        this.scmContractPaidMoney = scmContractPaidMoney;
    }

    //采购合同未支付金额
    private BigDecimal scmContractUnPaidMoney;

    public BigDecimal getScmContractUnPaidMoney() {
        if (scmContractAmount != null && scmContractPaidMoney != null && scmContractAmount.compareTo(scmContractPaidMoney) > 0) {
            return scmContractAmount.subtract(getScmContractPaidMoney());
        }
        return BigDecimal.ZERO;
    }

    //采购合同供应商应付账款账款
    private BigDecimal scmContractSupplierUnPaidMoney;

    public BigDecimal getScmContractSupplierUnPaidMoney() {
        return scmContractSupplierUnPaidMoney;
    }

    public void setScmContractSupplierUnPaidMoney(BigDecimal scmContractSupplierUnPaidMoney) {
        this.scmContractSupplierUnPaidMoney = scmContractSupplierUnPaidMoney;
    }

    //采购合同已付比例
    private BigDecimal scmPaidRate;

    public BigDecimal getScmPaidRate() {
        if (getScmContractAmount() == null || BigDecimal.ZERO.compareTo(getScmContractAmount()) == 0 ||
                getScmContractPaidMoney() == null || BigDecimal.ZERO.compareTo(getScmContractPaidMoney()) == 0) {
            return BigDecimal.ZERO;
        } else {
            return (getScmContractPaidMoney().multiply(new BigDecimal("100")).divide(getScmContractAmount(), 2, BigDecimal.ROUND_HALF_UP));
        }
    }

    //采购合同总应付金额(发票账款)
    private BigDecimal scmContractFadInvoiceMoney;

    public BigDecimal getScmContractFadInvoiceMoney() {
        return scmContractFadInvoiceMoney;
    }

    public void setScmContractFadInvoiceMoney(BigDecimal scmContractFadInvoiceMoney) {
        this.scmContractFadInvoiceMoney = scmContractFadInvoiceMoney;
    }

    //合同应付金额
    private BigDecimal scmContractNeedToPay;

    public BigDecimal getScmContractNeedToPay() {
        return scmContractNeedToPay;
    }

    public void setScmContractNeedToPay(BigDecimal scmContractNeedToPay) {
        this.scmContractNeedToPay = scmContractNeedToPay;
    }

    //创建人
    private String createByName;

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    //申请支付比例
    private BigDecimal reqMoneyRate;

    public BigDecimal getReqMoneyRate() {

        if (getScmContractAmount() == null || BigDecimal.ZERO.compareTo(getScmContractAmount()) == 0 ||
                getReqMoney() == null || BigDecimal.ZERO.compareTo(getReqMoney()) == 0) {
            return BigDecimal.ZERO;
        } else {
            if (Arrays.binarySearch(BillStateAttribute.getFadBillAuditState(), getState()) >= 0) {
                return MathUtil.add(getReqMoney(), getScmContractThenPaid(), false).multiply(new BigDecimal("100")).divide(getScmContractAmount(), 2, BigDecimal.ROUND_HALF_UP);
            } else {
                return MathUtil.add(getReqMoney(), getScmContractPaidMoney(), false).multiply(new BigDecimal("100")).divide(getScmContractAmount(), 2, BigDecimal.ROUND_HALF_UP);
            }
        }
    }

    //部门确认比例
    private BigDecimal approveMoneyRate;

    public BigDecimal getApproveMoneyRate() {
        if (getScmContractAmount() == null || BigDecimal.ZERO.compareTo(getScmContractAmount()) == 0 ||
                getApproveMoney() == null || BigDecimal.ZERO.compareTo(getApproveMoney()) == 0) {
            return BigDecimal.ZERO;
        } else {
            if (Arrays.binarySearch(BillStateAttribute.getFadBillAuditState(), getState()) >= 0) {
                return MathUtil.add(getApproveMoney(), getScmContractThenPaid(), false).multiply(new BigDecimal("100")).divide(getScmContractAmount(), 2, BigDecimal.ROUND_HALF_UP);
            } else {
                return MathUtil.add(getApproveMoney(), getScmContractPaidMoney(), false).multiply(new BigDecimal("100")).divide(getScmContractAmount(), 2, BigDecimal.ROUND_HALF_UP);
            }
        }
    }

    //采购经理申请比例
    private BigDecimal purchaseManagerMoneyRate;

    public BigDecimal getPurchaseManagerMoneyRate() {
        if (getScmContractAmount() == null || BigDecimal.ZERO.compareTo(getScmContractAmount()) == 0 ||
                getPurchaseManagerMoney() == null || BigDecimal.ZERO.compareTo(getPurchaseManagerMoney()) == 0) {
            return BigDecimal.ZERO;
        } else {
            if (Arrays.binarySearch(BillStateAttribute.getFadBillAuditState(), getState()) >= 0) {
                return MathUtil.add(getPurchaseManagerMoney(), getScmContractThenPaid(), false).multiply(new BigDecimal("100")).divide(getScmContractAmount(), 2, BigDecimal.ROUND_HALF_UP);
            } else {
                return MathUtil.add(getPurchaseManagerMoney(), getScmContractPaidMoney(), false).multiply(new BigDecimal("100")).divide(getScmContractAmount(), 2, BigDecimal.ROUND_HALF_UP);
            }
        }
    }

    //供应链部初审比例
    private BigDecimal purchaseChiefMoneyRate;

    public BigDecimal getPurchaseChiefMoneyRate() {
        if (getScmContractAmount() == null || BigDecimal.ZERO.compareTo(getScmContractAmount()) == 0 ||
                getPurchaseChiefMoney() == null || BigDecimal.ZERO.compareTo(getPurchaseChiefMoney()) == 0) {
            return BigDecimal.ZERO;
        } else {
            if (Arrays.binarySearch(BillStateAttribute.getFadBillAuditState(), getState()) >= 0) {
                return MathUtil.add(getPurchaseChiefMoney(), getScmContractThenPaid(), false).multiply(new BigDecimal("100")).divide(getScmContractAmount(), 2, BigDecimal.ROUND_HALF_UP);
            } else {
                return MathUtil.add(getPurchaseChiefMoney(), getScmContractPaidMoney(), false).multiply(new BigDecimal("100")).divide(getScmContractAmount(), 2, BigDecimal.ROUND_HALF_UP);
            }
        }
    }

    //会议终审比例
    private BigDecimal auditMoneyRate;

    public BigDecimal getAuditMoneyRate() {
        if (getScmContractAmount() == null || BigDecimal.ZERO.compareTo(getScmContractAmount()) == 0 ||
                getAuditMoney() == null || BigDecimal.ZERO.compareTo(getAuditMoney()) == 0) {
            return BigDecimal.ZERO;
        } else {
            if (Arrays.binarySearch(BillStateAttribute.getFadBillAuditState(), getState()) >= 0) {
                return MathUtil.add(getAuditMoney(), getScmContractThenPaid(), false).multiply(new BigDecimal("100")).divide(getScmContractAmount(), 2, BigDecimal.ROUND_HALF_UP);
            } else {
                return MathUtil.add(getAuditMoney(), getScmContractPaidMoney(), false).multiply(new BigDecimal("100")).divide(getScmContractAmount(), 2, BigDecimal.ROUND_HALF_UP);
            }
        }
    }

    //待核定发票
    private BigDecimal invoiceScmNeedCheck;

    public BigDecimal getInvoiceScmNeedCheck() {
        return invoiceScmNeedCheck;
    }

    public void setInvoiceScmNeedCheck(BigDecimal invoiceScmNeedCheck) {
        this.invoiceScmNeedCheck = invoiceScmNeedCheck;
    }

    //供应商是否黑名单
    private Integer isBlackList = new Integer("0");

    public Integer getIsBlackList() {
        return isBlackList;
    }

    public void setIsBlackList(Integer isBlackList) {
        this.isBlackList = isBlackList;
    }

    private String certificateNo;

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }
}