package com.csnt.scdp.bizmodules.modules.workflow.prm.businesstripclaims;

import com.csnt.scdp.bizmodules.entity.fad.FadInvoice;
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
@Controller("prm_businesstrip_claims-after-start")
@Transactional

public class PrmBusinessTripClaimsProcessAfterStartAction implements IAction {
    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map map = new HashedMap();
        String uuid = (String) inMap.get("businessKey");
        String projectMainId = "";
        BigDecimal payCash = new BigDecimal("0");
        DAOMeta daoMeta = DAOHelper.getDAO("invoice-dao", "vw_fad_invoice", null);
        String sql = daoMeta.getStrSql();
        sql = sql + " where uuid ='" + uuid + "' ";
        daoMeta.setStrSql(sql);
        List lstContractCashReqInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstContractCashReqInfo)) {
            Map dtoMap = (Map) lstContractCashReqInfo.get(0);
            projectMainId = dtoMap.get("prmProjectMainId") + "";
            payCash = new BigDecimal(dtoMap.get("payCash").toString());
        }
        if(payCash.compareTo(BigDecimal.ZERO) == 1){//如果报现金额大于0 则校验项目差旅预算是否足够
            invoiceManager.checkPrmTripBudget(projectMainId, payCash);
        }

        return map;
    }
}
