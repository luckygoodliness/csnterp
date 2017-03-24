package com.csnt.scdp.bizmodules.modules.prm.receipts.action;

import com.csnt.scdp.bizmodules.modules.prm.receipts.services.intf.ReceiptsManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Created by Tao on 2015/12/16.
 */
@Scope("singleton")
@Controller("receipts-internal")
@Transactional
public class InternalAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(InternalAction.class);

    @Resource(name = "receipts-manager")
    private ReceiptsManager receiptsManager;

    @Override
    public Map perform(Map map) throws BizException, SysException {
        String prmContractId = StringUtil.replaceNull(map.get("prmContractId"));
        Map outMap = receiptsManager.loadInternalInfo(prmContractId);
        return outMap;
    }
}
