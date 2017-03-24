package com.csnt.scdp.bizmodules.modules.prm.receipts.action;

import com.csnt.scdp.bizmodules.modules.prm.receipts.services.intf.ReceiptsManager;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.fr.stable.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lijx on 2016/8/12.
 */
@Scope("singleton")
@Controller("receipts-query1")
@Transactional
public class ContractQueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ContractQueryAction.class);

    @Resource(name = "receipts-manager")
    private ReceiptsManager receiptsManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
