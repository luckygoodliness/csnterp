package com.csnt.scdp.bizmodules.modules.prm.receipts.services.intf;

import com.csnt.scdp.bizmodules.entity.prm.PrmReceipts;
import com.csnt.scdp.bizmodules.modules.prm.receipts.dto.PrmReceiptsScmInvoiceDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Description:  ReceiptsManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 09:20:00
 */
public interface ReceiptsManager {
    public boolean checkMoneyBasedonUnknownReceipts(Map inMap);

    /**
     * 返回一组项目收款数据
     *
     * @return 项目收款数据<项目UUID,项目收款数据>
     */
    Map<String, List<PrmReceipts>> loadPrmReceiptsByUUids(List prmUUids);

    Map<String, Map> loadPrmAmountAndReceiptsByUUids(List prmUUids);

    /**
     * 根据合同主键获取内委信息
     *
     * @param prmContractId
     * @return
     */
    public Map loadInternalInfo(String prmContractId);
	
	public Map getReceiptsScmInvoiceFields(String prmContractId);

    //项目是内委项目，结算审批发送报表管理员消息
    public void sendMsg(String uuid, String projectCode, BigDecimal squareProjectMoney);

    public Map validatePrmReceiptBeforeWorkFlowCommit(String uuid);

    //填充Desc
    public void loadExtraDescField(List<PrmReceiptsScmInvoiceDto> receiptsScmInvoiceDtoList);
}