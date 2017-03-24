package com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf;

import com.csnt.scdp.bizmodules.entity.fad.FadCashReq;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqC;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqH;
import com.csnt.scdp.bizmodules.entity.operate.OperateBusinessBidInfo;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqCDto;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Description:  CashreqManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 09:40:50
 */
public interface CashreqManager {

    /**
     * 根据采购合同生成采购请款
     */
    void generateScmCashreqByScmContract(List<ScmContract> lst);

    /**
     * 根据采购合同生成采购请款
     *
     * @param contract 合同
     * @param amount   请款金额
     * @return 请款单编号
     */
    String generateScmCashreqByScmContractandAmount(ScmContract contract, BigDecimal amount, Integer advanced, String msg, FadPayReqC reqc);

    /**
     * @param cdto
     * @return
     */
    String generateScmCashreqByPayReqCDto(FadPayReqCDto cdto);

    FadPayReqC getPayReqCByUuid(String uuid);

    /**
     * 检查费用申请状态操作是否满足
     *
     * @param fadCashReq 操作的费用申请
     * @param actionType 操作类型  如：submit
     * @return 返回结果
     */
    boolean checkWorkAction(FadCashReq fadCashReq, String actionType);

    FadCashReq getFadCashReqbyUuid(String uuid);

    BigDecimal getRemainBudget(FadCashReq fadCashReq);

    void updateFadCashReq(FadCashReq fadCashReq);

    List<String> getUnFinishedFadInvoice(String contractCode);

    List<FadCashReq> getFadCashReqByBidInfoUuid(String InfoUuid);

    public void sendMsg(String uuid, String payeeName, String type);

    Map validateCashReqDepositBeforeWorkFlowCommitOfStart(String uuid);
}