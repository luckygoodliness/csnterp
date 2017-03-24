package com.csnt.scdp.bizmodules.modules.workflow.nonprm.purchasereq;

import com.csnt.scdp.bizmodules.entity.nonprm.FinancialSubject;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchaseReqAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("non_prm_purchase_request-complete")
@Transactional

public class NonPrmPruchaseRequestComplete extends NonPrmPruchaseRequestStart {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapVar = super.perform(inMap);
        String subjectCode = (String) mapVar.get(PrmPurchaseReqAttribute.FINANCIAL_SUBJECT_CODE);
        String subjectNo = subjectCode.substring(subjectCode.indexOf("-") + 1, subjectCode.length());
        Map condition = new HashMap();
        condition.put("subjectNo", subjectNo);
        List<FinancialSubject> financialSubjectList = PersistenceFactory.getInstance().findByAnyFields(FinancialSubject.class, condition, null);
        String feeType = null;
        if (null != financialSubjectList) {
            feeType = financialSubjectList.get(0).getSubjectCategory();
        }
        mapVar.put("feeType", feeType);

        String prmPurchaseReqUuid = (String) mapVar.get("uuid");
        Map condition1 = new HashMap();
        condition1.put("prmPurchaseReqId", prmPurchaseReqUuid);
        List<PrmPurchaseReqDetail> prmPurchaseReqDetailList = PersistenceFactory.getInstance().findByAnyFields(PrmPurchaseReqDetail.class, condition1, null);
        BigDecimal totalPrice = new BigDecimal(0);
        if (ListUtil.isNotEmpty(prmPurchaseReqDetailList)) {
            for (int i = 0; i < prmPurchaseReqDetailList.size(); i++) {
                BigDecimal amount = prmPurchaseReqDetailList.get(i).getAmount();
                BigDecimal exceptedPrice = prmPurchaseReqDetailList.get(i).getExpectedPrice();
                BigDecimal total = (null == amount || null == exceptedPrice) ? new BigDecimal(0) : amount.multiply(exceptedPrice);
                totalPrice = totalPrice.add(total);
            }
        }
        mapVar.put("money",totalPrice);
        return mapVar;
    }
}
