package com.csnt.scdp.bizmodules.modules.fad.payreq.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqH;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadPayReqHAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqCDto;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqHDto;
import com.csnt.scdp.framework.attributes.BaseEntitiesAttribute;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.fad.payreq.services.intf.PayreqManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 17:42:24
 */

@Scope("singleton")
@Controller("payreq-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "payreq-manager")
    private PayreqManager payreqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);
        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);

        BasePojo dtoObj = DtoHelper.getDtoFromMap(viewMap, dtoClass);
        FadPayReqHDto dto = (FadPayReqHDto) dtoObj;
        List<FadPayReqH> FadPayReqHLst = null;
        if (FadPayReqHAttribute.REQ_TYPE_MONTH.equals(dto.getReqType())) {
            FadPayReqHLst = getExistSavedBill(dto.getYear().toString(), dto.getMonth());
        }

        if (ListUtil.isNotEmpty(FadPayReqHLst)) {
            if (dto.getFadPayReqCDto().size() == 0) {
                return null;
            }
        }
        String mainTabName = StringUtil.capFirst(BeanFactory.getClass(dtoClass).getSimpleName(), false);
        Map mapInput = (Map) viewMap.get(mainTabName);
        String reqNo = NumberingHelper.getNumbering("PAYREQ_NO", null);
        mapInput.put(FadPayReqHAttribute.REQNO, reqNo);
        Map out = super.perform(inMap);
        out.put(FadPayReqHAttribute.REQNO, reqNo);
        return out;
    }

    public List<FadPayReqH> getExistSavedBill(String year, String month) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        Integer y = new Integer(year);
        mapConditions.put(FadPayReqHAttribute.YEAR, y);
        mapConditions.put(FadPayReqHAttribute.MONTH, month);
        mapConditions.put(FadPayReqHAttribute.STATE, BillStateAttribute.CMD_BILL_STATE_NEW);
        List<FadPayReqH> fadPayReqHList = payreqManager.getFadPayReqHbyCondition(mapConditions);
        return fadPayReqHList;
    }
}
