package com.csnt.scdp.bizmodules.modules.operate.bidcontractplan.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmContractC;
import com.csnt.scdp.bizmodules.modules.common.action.ErpDeleteAction;
import com.csnt.scdp.bizmodules.modules.operate.bidcontractplan.services.intf.BidcontractplanManager;
import com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.services.intf.BusinessbidinfoManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Description:  DeleteAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-05 10:26:28
 */

@Scope("singleton")
@Controller("bidcontractplan-delete")
@Transactional
public class DeleteAction extends ErpDeleteAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(DeleteAction.class);

    @Resource(name = "businessbidinfo-manager")
    private BusinessbidinfoManager businessbidinfoManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        List lstUuids = (List) super.getDeleteUuids(inMap);
        for (int i = 0; i < lstUuids.size(); i++) {
            List<PrmContractC> prmContractcs = businessbidinfoManager.findContractbyBidInfoId(lstUuids.get(i).toString());
            if (ListUtil.isNotEmpty(prmContractcs) && prmContractcs.size() > 0) {
                throw new BizException("该合同协议草案已在合同新增中被引用，无法删除!");
            }
        }

        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
