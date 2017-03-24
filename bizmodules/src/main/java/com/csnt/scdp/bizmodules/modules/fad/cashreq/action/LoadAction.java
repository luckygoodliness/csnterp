package com.csnt.scdp.bizmodules.modules.fad.cashreq.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.cdm.CdmFileRelation;
import com.csnt.scdp.bizmodules.entity.fad.*;
import com.csnt.scdp.bizmodules.entity.operate.OperateBusinessBidInfo;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entity.scm.ScmEbusinessUser;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.services.intf.CdmfileManager;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.common.intf.CommonServiceManager;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.dto.FadCashReqDto;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf.CashreqManager;
import com.csnt.scdp.bizmodules.modules.fad.fadinterestrate.services.intf.FadinterestrateManager;
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
import com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.services.intf.BusinessbidinfoManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.bizmodules.modules.scm.scmebusinessuser.services.intf.ScmebusinessuserManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpFileManager;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import com.fr.function.DECIMAL;
import com.fr.general.FUNC;
import org.apache.commons.lang3.StringUtils;
import org.freehep.graphicsio.swf.SWFAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.spi.CalendarNameProvider;
import java.util.stream.Collectors;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 09:40:50
 */

@Scope("singleton")
@Controller("cashreq-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "cashreq-manager")
    private CashreqManager cashreqManager;

    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;

    @Resource(name = "erp-common-service-manager")
    private CommonServiceManager commonServiceManager;

    @Resource(name = "cdmfile-manager")
    private CdmfileManager cdmfileManager;

    @Resource(name = "fadinterestrate-manager")
    private FadinterestrateManager fadinterestrateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        Map dtoMap = (Map) out.get("fadCashReqDto");
        if (dtoMap != null) {
            String orgCode = (String) dtoMap.get("officeId");
            if (orgCode != null) {
                String orgName = OrgHelper.getOrgNameByCode(orgCode);
                dtoMap.put("officeIdDesc", orgName);
            }

            String userId = (String) dtoMap.get("staffId");
            ScdpUser user = ErpUserHelper.findUserByUserId(userId);
            if (user != null) {
                String userName = user.getUserName();
                dtoMap.put("staffIdDesc", userName);
            }
        }
        //Do After
        return out;
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        FadCashReqDto fadCashReqDto = (FadCashReqDto) dtoObj;
        if (fadCashReqDto != null) {
            //填充项目名称
            String projectId = fadCashReqDto.getProjectId();
            if (StringUtils.isNotBlank(projectId)) {
                PrmProjectMain obj = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, projectId);
                if (obj != null) {
                    fadCashReqDto.setProjectName(obj.getProjectName());
                    fadCashReqDto.setPrmContractorOffice(obj.getContractorOffice());
                }
            }
            //填充合同
            String purchaseContractId = fadCashReqDto.getPurchaseContractId();
            if (StringUtils.isNotBlank(purchaseContractId)) {
                ScmContract obj = PersistenceFactory.getInstance().findByPK(ScmContract.class, purchaseContractId);
                if (obj != null) {
                    fadCashReqDto.setPurchaseContractIdPlus(obj.getScmContractCode());
                }
            }

            //对未提交的数据在前端显示剩余预算
            if (BillStateAttribute.FAD_BILL_STATE_NEW.equals(fadCashReqDto.getState()) || BillStateAttribute.FAD_BILL_STATE_SUBMITTED.equals(fadCashReqDto.getState())
                    || BillStateAttribute.CMD_BILL_STATE_REJECTED.equals(fadCashReqDto.getState())) {
                BigDecimal remainBudget = cashreqManager.getRemainBudget(fadCashReqDto);
                fadCashReqDto.setBudgetDesc("当前该科目可用预算为：" + remainBudget + "元");
            }

            //填充子表的发票、流水号编号
            if (fadCashReqDto.getFadCashReqInvoiceDto() != null && fadCashReqDto.getFadCashReqInvoiceDto().size() > 0) {
                //有效的发票核销
                List<String> fadUUids = fadCashReqDto.getFadCashReqInvoiceDto().stream().
                        filter(p -> (StringUtils.isNotBlank(p.getFadInvoiceId()))).
                        filter(p -> "0".equals(p.getClearanceType())).
                        map(t -> t.getFadInvoiceId()).distinct().collect(Collectors.toList());

                Map<String, FadInvoice> fadInvoiceMap = invoiceManager.getObjectsByIds(fadUUids);
                //现金核销流水号
                List<String> fadCashClearanceUUids = fadCashReqDto.getFadCashReqInvoiceDto().stream().
                        filter(p -> (StringUtils.isNotBlank(p.getFadInvoiceId()))).
                        filter(p -> "1".equals(p.getClearanceType())).
                        map(t -> t.getFadInvoiceId()).distinct().collect(Collectors.toList());
                Map<String, FadCashClearance> clearanceMap = invoiceManager.getFadCashClearanceByIds(fadCashClearanceUUids);

                fadCashReqDto.getFadCashReqInvoiceDto().stream().forEach(t -> {
                            if (fadInvoiceMap.containsKey((t.getFadInvoiceId()))) {
                                t.setState(fadInvoiceMap.get(t.getFadInvoiceId()).getState());
                                t.setInvoiceNo(fadInvoiceMap.get(t.getFadInvoiceId()).getInvoiceReqNo());
                                t.setInvoiceRunningNo(fadInvoiceMap.get(t.getFadInvoiceId()).getInvoiceReqNo());
                                t.setNcDate(fadInvoiceMap.get(t.getFadInvoiceId()).getCertificateCreateTime());
                            }
                            if (clearanceMap.containsKey((t.getFadInvoiceId()))) {
                                t.setState(clearanceMap.get(t.getFadInvoiceId()).getState());
                                t.setInvoiceNo(clearanceMap.get(t.getFadInvoiceId()).getRunningNo());
                                t.setInvoiceRunningNo(clearanceMap.get(t.getFadInvoiceId()).getRunningNo());
                                t.setNcDate(clearanceMap.get(t.getFadInvoiceId()).getCertificateCreateTime());
                            }
                        }
                );
            }
            //填充用户名
            String staffId = fadCashReqDto.getStaffId();
            if (StringUtils.isNotBlank(staffId)) {
                List<ScdpUser> obj = commonServiceManager.findUserByUserId(staffId);
                if (ListUtil.isNotEmpty(obj)) {
                    String name = obj.get(0).getUserName();
                    fadCashReqDto.setStaffIdDesc(name);
                }
            }
            //填充投标信息收集
            String infoId = fadCashReqDto.getOperateBusinessBidInfoId();
            if (StringUtils.isNotBlank(infoId)) {
                OperateBusinessBidInfo bid = PersistenceFactory.getInstance().findByPK(OperateBusinessBidInfo.class, infoId);
                if (bid != null) {
                    fadCashReqDto.setOperateBusinessBidInfoIdDesc(bid.getProjectName());
                }
            }
            //填充投标信息收集
            String reqCUuid = fadCashReqDto.getFadPayReqCUuid();
            if (StringUtils.isNotBlank(reqCUuid)) {
                FadPayReqC req = PersistenceFactory.getInstance().findByPK(FadPayReqC.class, reqCUuid);
                if (req != null) {
                    fadCashReqDto.setFadPayReqHUuid(req.getPuuid());
                }
            }
            //M7_C5_F2_逾期利息
            if ("3".equals(fadCashReqDto.getReqType())) {//保证金申请
                Date preclearDate = fadCashReqDto.getPreclearDate();//预计核销日期
                if (preclearDate != null) {
                    Calendar computeInterestDate = Calendar.getInstance();//开始计算利息时间
                    computeInterestDate.setTime(preclearDate);
                    computeInterestDate.add(Calendar.MONTH, 1);
                    Calendar currentDate = Calendar.getInstance();//当前时间
                    if (currentDate.after(computeInterestDate)) {
                        //满一个月开始计算利息
                        BigDecimal totalOverdueInterest = null;//总利息
                        BigDecimal reqMoney = fadCashReqDto.getMoney();//请款金额
                        String uuid = fadCashReqDto.getUuid();
                        totalOverdueInterest = computeOverdueInterest(preclearDate, reqMoney, uuid);
                        if (totalOverdueInterest != null) {
                            fadCashReqDto.setTotalOverdueInterest(totalOverdueInterest);
                        }
                    }
                }
            }

            //加载附件名称
            List<CdmFileRelation> CdmFileRelationlst = cdmfileManager.getCdmFileRelationByDataId(fadCashReqDto.getUuid());
            if (ListUtil.isNotEmpty(CdmFileRelationlst)) {
                ScdpFileManager scdpFileManager = cdmfileManager.getScdpFileManagerByUUid(CdmFileRelationlst.get(0).getFileId());
                if (scdpFileManager != null) {
                    fadCashReqDto.setFileId(scdpFileManager.getUuid());
                    fadCashReqDto.setAttachmentFileName(scdpFileManager.getFileName());
                }
            }
            //填充电商密码
            if (StringUtil.isNotEmpty(fadCashReqDto.getJdName()) && StringUtil.isNotEmpty(fadCashReqDto.getElectricCommercialStore())) {
                ScmebusinessuserManager scmebusinessuserManager = BeanFactory.getBean("scmebusinessuser-manager".toLowerCase());
                List<ScmEbusinessUser> seu = scmebusinessuserManager.getScmebusinessuserByEbusinessNameAndUserCode(fadCashReqDto.getElectricCommercialStore(), fadCashReqDto.getJdName());
                if (ListUtil.isNotEmpty(seu)) {
                    fadCashReqDto.setJdPassword(seu.get(0).getPassword());
                }
            }
        }
    }

    //M7_C5_F2_逾期利息

    public BigDecimal computeOverdueInterest(Date preclearDate, BigDecimal reqMoney, String uuid) {
        //获取请款核销列表
        List<Map<String, Object>> reqClearList = fadinterestrateManager.getReqClearList(uuid);
        BigDecimal totalOverdueInterest = new BigDecimal(0.0);
        Date now = new Date();//当前日期
        now.setHours(0);
        now.setMinutes(0);
        now.setSeconds(0);

        if (ListUtil.isNotEmpty(reqClearList)) {
            Date lastClearanceDate = null;//上一次核销日期
            BigDecimal reqMoneyLeft = reqMoney;
            for (int i = 0; i < reqClearList.size(); i++) {
                Map<String, Object> record = reqClearList.get(i);
                Date clearanceDate = (Date) record.get("clearanceDate");
                BigDecimal clearanceMoney = (BigDecimal) record.get("clearanceMoney");
                if (i == 0) {
                    totalOverdueInterest = totalOverdueInterest.add(getInterestByRateTable(preclearDate, clearanceDate, reqMoney));
                } else {
                    totalOverdueInterest = totalOverdueInterest.add(getInterestByRateTable(lastClearanceDate, clearanceDate, reqMoneyLeft));
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(clearanceDate);
                cal.add(Calendar.DATE, 1);
                //上一次核销日期=当前核销日期+1
                lastClearanceDate = cal.getTime();
                reqMoneyLeft = reqMoneyLeft.subtract(clearanceMoney);
            }
            if (reqMoneyLeft.compareTo(BigDecimal.ZERO) > 0) {
                totalOverdueInterest = totalOverdueInterest.add(getInterestByRateTable(lastClearanceDate, now, reqMoneyLeft));
            }

        } else {
            //没有请款核销记录，利息用请款金额从预计核销日期算到当前日期
            totalOverdueInterest = getInterestByRateTable(preclearDate, now, reqMoney);
        }

        return totalOverdueInterest;
    }

    /**
     * 根据利率表获取利息
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param amount    计息金额
     * @return resultInterest 利息
     */
    public BigDecimal getInterestByRateTable(Date startDate, Date endDate, BigDecimal amount) {
        List<FadInterestRate> rateList = fadinterestrateManager.getRateListOrderByValidityDateFrom();
        BigDecimal resultInterest = new BigDecimal(0.0);
        int startIndex = -1, endIndex = -1;
        for (int i = 0; i < rateList.size(); i++) {
            FadInterestRate rate = rateList.get(i);
            if (startDate.compareTo(rate.getValidityDateFrom()) >= 0 && startDate.compareTo(rate.getValidityDateTo()) <= 0) {
                startIndex = i;
            }
            if (endDate.compareTo(rate.getValidityDateFrom()) >= 0 && endDate.compareTo(rate.getValidityDateTo()) <= 0) {
                endIndex = i;
            }
        }

        if (startIndex == endIndex) {
            //开始时间 和 结束时间 在同一利息段
            //利息=(结束时间-开始时间)*利率*计息金额/一整年的天数
            BigDecimal rate = rateList.get(startIndex).getRate();
            BigDecimal days = getDaysOfYear(startDate.getYear());
            resultInterest = getDaysBetweenTwoDates(endDate, startDate).multiply(rate).multiply(amount).divide(days, 2, BigDecimal.ROUND_HALF_DOWN);
        } else {
            //开始时间 和 结束时间 不在一个利息段
            for (int i = startIndex; i <= endIndex; i++) {
                FadInterestRate fadInterestRate = rateList.get(i);
                BigDecimal rate = fadInterestRate.getRate();
                BigDecimal days = getDaysOfYear(fadInterestRate.getValidityDateFrom().getYear());//一整年的天数
                if (i == startIndex) {
                    //利息=(利率有效性结束时间-开始时间)*利率*计息金额/一整年的天数
                    resultInterest = resultInterest.add(getDaysBetweenTwoDates(fadInterestRate.getValidityDateTo(), startDate).multiply(rate).multiply(amount).divide(days, 2, BigDecimal.ROUND_HALF_DOWN));
                } else if (i == endIndex) {
                    //利息=(结束时间-利率有效性起始时间)*利率*计息金额/一整年的天数
                    resultInterest = resultInterest.add(getDaysBetweenTwoDates(endDate, fadInterestRate.getValidityDateFrom()).multiply(rate).multiply(amount).divide(days, 2, BigDecimal.ROUND_HALF_DOWN));
                } else {
                    //利息=(利率有效性结束时间-利率有效性起始时间)*利率*计息金额/一整年的天数
                    resultInterest = resultInterest.add(getDaysBetweenTwoDates(fadInterestRate.getValidityDateTo(), fadInterestRate.getValidityDateFrom()).multiply(rate).multiply(amount).divide(days, 2, BigDecimal.ROUND_HALF_DOWN));
                }

            }
        }

        return resultInterest;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public BigDecimal getDaysBetweenTwoDates(Date date1, Date date2) {
        long days = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000) > 0 ? (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000) : (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000);
        return new BigDecimal(days);
    }

    /**
     * 获取某一年有多少天
     *
     * @param year
     * @return
     */
    public BigDecimal getDaysOfYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        int days = cal.getActualMaximum(Calendar.DAY_OF_YEAR);

        return new BigDecimal(days);
    }

}
