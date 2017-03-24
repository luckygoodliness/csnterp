package com.csnt.scdp.bizmodules.modules.workflow.nonprm.faily;

import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("non_prm_faily_claims-after-start")
@Transactional

public class NonPrmFailyAfterStartAction implements IAction {
    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map map = new HashedMap();
        String uuid = (String) inMap.get("businessKey");
        String budgetId = "";
        BigDecimal payCash = new BigDecimal("0");
        DAOMeta daoMeta = DAOHelper.getDAO("invoice-dao", "vw_fad_invoice", null);
        String sql = daoMeta.getStrSql();
        sql = sql + " where uuid ='" + uuid + "' ";
        daoMeta.setStrSql(sql);
        List lstContractCashReqInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstContractCashReqInfo)) {
            Map dtoMap = (Map) lstContractCashReqInfo.get(0);
            budgetId = dtoMap.get("subjectId") + "";
            payCash = new BigDecimal(dtoMap.get("payCash").toString());
        }
        invoiceManager.checkBudgetIsEnough(false,budgetId, payCash);
        return map;
    }
}
