package com.csnt.scdp.bizmodules.modules.fad.fadinterestrate.services.intf;

import com.csnt.scdp.bizmodules.entity.fad.FadInterestRate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Description:  FadinterestrateManager
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-30 10:37:36
 */
public interface FadinterestrateManager {
    public List<FadInterestRate> getRateListOrderByValidityDateFrom();
    public List<Map<String, Object>> getReqClearList(String fadCashReqId);
}