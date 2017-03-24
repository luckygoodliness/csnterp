package com.csnt.scdp.bizmodules.modules.workflow.nonprm.businesstripclaims;

import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("non_prm_businesstrip_claims-after-start")
@Transactional

public class NonPrmBusinessTripClaimsProcessAfterStartAction implements IAction {
    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map map = new HashedMap();
        String uuid = (String) inMap.get("businessKey");
        String officeCode = "";
        String fadYear = "";
        BigDecimal payCash = new BigDecimal("0");
        DAOMeta daoMeta = DAOHelper.getDAO("invoice-dao", "vw_fad_invoice", null);
        String sql = daoMeta.getStrSql();
        sql = sql + " where uuid ='" + uuid + "' ";
        daoMeta.setStrSql(sql);
        List lstContractCashReqInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstContractCashReqInfo)) {
            Map dtoMap = (Map) lstContractCashReqInfo.get(0);
            officeCode = dtoMap.get("officeId") + "";
            payCash = new BigDecimal(dtoMap.get("payCash").toString());
            fadYear= dtoMap.get("fadYear") + "";
        }
        invoiceManager.checkNonTripBudget(officeCode, payCash,fadYear);
        return map;
    }
}
