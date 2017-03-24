package com.csnt.scdp.bizmodules.modules.prm.prmunknownreceipts.services.intf;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Description:  PrmunknownreceiptsManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 16:52:16
 */
public interface PrmunknownreceiptsManager {
    public void sendMsg(String uuid,String receiptNo, String money, String payer);
}