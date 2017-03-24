package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.bizmodules.modules.fad.cashreq.action.AddAction;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmProjectMainDto;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fisher on 2015/11/10.
 */
@Scope("singleton")
@Controller("invoice-exists-deposit")
@Transactional
public class IfExistsDepositAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String uuid = (String)inMap.get("uuid");
        Map contextMap = new HashMap();
        contextMap.put("fadInvoiceId", uuid);
        DAOMeta daoMeta = DAOHelper.getDAO("cashreq-dao", "invoice-exists-deposit", null, contextMap);
        List<Map<String,Object>> lst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if(ListUtil.isNotEmpty(lst)) {
            Map<String, Object> res = lst.get(0);
            BigDecimal num = (BigDecimal)res.get("num");
            if(num.compareTo(new BigDecimal(0.0)) > 0) {
                rsMap.put("result", num);
                return rsMap;
            }
        }

        rsMap.put("result", 0);
        return rsMap;
    }
}
