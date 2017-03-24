package com.csnt.scdp.bizmodules.modules.workflow.prm.invoice;

import com.csnt.scdp.bizmodules.entity.prm.PrmBilling;
import com.csnt.scdp.bizmodules.entity.prm.PrmContract;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by as on 2017/3/9.
 */
@Scope("singleton")
@Controller("prm_invoice-after-start")
@Transactional
public class PrmInvoiceAfterTask implements IAction {

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        String uuid = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        if(StringUtil.isNotEmpty(uuid)) {
            PrmBilling bill = PersistenceFactory.getInstance().findByPK(PrmBilling.class, uuid);
            String contractId = bill.getPrmContractId();
            Double invoiceMoney = Double.parseDouble(bill.getInvoiceMoney().toString());
            if (StringUtil.isNotEmpty(contractId)) {
                String billSql = "select sum(invoice_Money) as invoice_Money_Count from PRM_BILLING \n" +
                        "where prm_Contract_Id=? \n" +
                        "and BILL_TYPE=0 \n" +
                        "and (is_void=0 or is_void is null) \n" +
                        "and state in (1,2,4,8,9)";
                List<String> lstBill = new ArrayList<String>();
                lstBill.add(contractId);
                List<Map<String, Object>> list = PersistenceFactory.getInstance().findByNativeSQL(new DAOMeta(billSql, lstBill));
                Double invoiceMoneyCount = 0.0d;
                for (Map m : list) {
                    if (StringUtil.isNotEmpty(m.get("invoiceMoneyCount").toString())) {
                        invoiceMoneyCount = Double.parseDouble(m.get("invoiceMoneyCount").toString());
                    }
                }
                PrmContract contract = PersistenceFactory.getInstance().findByPK(PrmContract.class, contractId);
                if (StringUtil.isNotEmpty(contract.getContractNowMoney())) {
                    if (invoiceMoney + invoiceMoneyCount > Double.parseDouble(contract.getContractNowMoney().toString())) {
                        throw new BizException("累计开票金额不能大于合同金额！");
                    }
                }
            }
        }
        return mapResult;
    }
}
