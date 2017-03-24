package com.csnt.scdp.bizmodules.modules.workflow.prm.receipt;

import com.csnt.scdp.bizmodules.entity.prm.PrmContract;
import com.csnt.scdp.bizmodules.entity.prm.PrmReceipts;
import com.csnt.scdp.bizmodules.modules.prm.prmexamperiod.services.intf.PrmExamPeriodManager;
import com.csnt.scdp.bizmodules.modules.prm.receipts.dto.PrmReceiptsDto;
import com.csnt.scdp.bizmodules.modules.prm.receipts.dto.PrmReceiptsScmInvoiceDto;
import com.csnt.scdp.bizmodules.modules.prm.receipts.services.intf.ReceiptsManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.OnlineUserHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_receipt-after-fixed")
@Transactional

public class PrmReceiptProcessAfterCompleteAction extends ERPWorkFlowBaseVariableCollectionAction {

    @Resource(name = "receipts-manager")
    private ReceiptsManager receiptsManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        String uuid = (String) inMap.get("businessKey");
        PrmReceiptsDto receiptsDto = (PrmReceiptsDto) DtoHelper.findDtoByPK(PrmReceiptsDto.class, uuid);
        if ("HEDGE".equals(receiptsDto.getMoneyType())) {
            //付款方式为对冲时
            //对冲金额不大于可对冲应付账款（即 可对冲应付账款 < 0）
            List<PrmReceiptsScmInvoiceDto> scmInvoiceLst = receiptsDto.getPrmReceiptsScmInvoiceDto();
            receiptsManager.loadExtraDescField(scmInvoiceLst);
            BigDecimal hedgeMoneySum = new BigDecimal(0.0);
            for (PrmReceiptsScmInvoiceDto scmInvoiceDto : scmInvoiceLst) {
                hedgeMoneySum = hedgeMoneySum.add(scmInvoiceDto.getHedgeMoney());
                if (scmInvoiceDto.getNeedPayMoneyLock().compareTo(new BigDecimal(0.0)) < 0) {
                    throw new BizException("对冲金额不能大于可对冲应付账款");
                }
            }
            if (hedgeMoneySum.compareTo(receiptsDto.getActualMoney()) != 0) {
                throw new BizException("对冲金额合计与实际收款金额不一致");
            }
        }

        Map dataInfo = super.perform(inMap);
        String prmProjectMainId = (String) dataInfo.get("prmProjectMainId");
        String projectCode = (String) dataInfo.get("projectCode");
        BigDecimal actualMoney = (BigDecimal) dataInfo.get("actualMoney");
        boolean isInternal = false;
        if (StringUtil.isNotEmpty(prmProjectMainId)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            Map condition = new HashMap<>();
            condition.put("prmProjectMainId", prmProjectMainId);
            List<PrmContract> prmContractList = pcm.findByAnyFields(PrmContract.class, condition, null);
            if (ListUtil.isNotEmpty(prmContractList)) {
                for (PrmContract prmContract : prmContractList) {
                    String innerPurchaseReqId = prmContract.getInnerPurchaseReqId();
                    if (StringUtil.isNotEmpty(innerPurchaseReqId)) {
                        isInternal = true;
                    }
                }
            }
        }
        //增加考核日期
        PrmReceipts prmReceipts = PersistenceFactory.getInstance().findByPK(PrmReceipts.class, uuid);
        PrmExamPeriodManager prmExamPeriodManager = BeanFactory.getBean("prmexamperiod-manager".toLowerCase());

        Date examDate = prmExamPeriodManager.getExamDateFromAppointedDate(prmReceipts.getActualDate());
        prmReceipts.setExamDate(examDate);
        PersistenceFactory.getInstance().update(prmReceipts);

        if (isInternal) {
            OnlineUserHelper.getCurrentOnlineUser();
            receiptsManager.sendMsg(uuid, projectCode, actualMoney);
        }
        return mapResult;
    }
}
