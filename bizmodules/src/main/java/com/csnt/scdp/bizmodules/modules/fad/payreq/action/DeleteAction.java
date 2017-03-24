package com.csnt.scdp.bizmodules.modules.fad.payreq.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpDeleteAction;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqCDto;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqHDto;
import com.csnt.scdp.bizmodules.modules.fad.payreq.services.intf.PayreqManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Description:  DeleteAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 17:42:24
 */

@Scope("singleton")
@Controller("payreq-delete")
@Transactional
public class DeleteAction extends ErpDeleteAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(DeleteAction.class);

    @Resource(name = "payreq-manager")
    private PayreqManager payreqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        List lst = (List) getDeleteUuids(inMap);
        if (ListUtil.isNotEmpty(lst)) {
            String uuid = (String) lst.get(0);
            BasePojo fadPayReqHDto = DtoHelper.findDtoByPK(FadPayReqHDto.class, uuid);
            beforeAction(fadPayReqHDto);
        }
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    protected void beforeAction(BasePojo dtoObj) {
        FadPayReqHDto fadPayReqHDto = (FadPayReqHDto) dtoObj;
        if (fadPayReqHDto != null && Arrays.binarySearch(BillStateAttribute.getFadBillAuditUnstartState(), fadPayReqHDto.getState()) < 0) {
            throw new BizException("该支付申请未处于初始状态，无法删除！");
        }
        if (ListUtil.isNotEmpty(fadPayReqHDto.getFadPayReqCDto())) {
            Optional<FadPayReqCDto> OptionalC = fadPayReqHDto.getFadPayReqCDto().stream().filter(t ->
                            Arrays.binarySearch(BillStateAttribute.getFadBillStateWorkflow(), t.getState()) >= 0
            ).findAny();
            if (OptionalC.isPresent()) {
                throw new BizException("该支付申请存在未处于初始状态的支付明细，无法删除！");
            }
        }
    }
}
