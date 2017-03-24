package com.csnt.scdp.bizmodules.modules.scm.supplierlimit.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmSupplierLimit;
import com.csnt.scdp.bizmodules.modules.scm.supplierlimit.dto.ScmSupplierLimitDto;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.supplierlimit.services.intf.SupplierlimitManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-07-19 15:14:12
 */

@Scope("singleton")
@Controller("supplierlimit-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "supplierlimit-manager")
    private SupplierlimitManager supplierlimitManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);

        //Do After
        return out;
    }

    @Override
    protected void beforeAction(BasePojo dtoObj) {
        ScmSupplierLimitDto scmSupplierLimitMain = (ScmSupplierLimitDto) dtoObj;
        if ((scmSupplierLimitMain.getBeginTime().after(scmSupplierLimitMain.getEndTime()))){
            throw new BizException("开始时间不能大于结束时间");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(8);
        String beginTimeString = formatter.format(scmSupplierLimitMain.getBeginTime());
        String endTimeString = formatter.format(scmSupplierLimitMain.getEndTime());
        List<Map<String, Object>> reqListTime= supplierlimitManager.selectTime(beginTimeString,endTimeString);
        if (reqListTime.size() > 0) {
            throw new BizException("已经存在该时间段，请重新选择时间段");
        }
        if(StringUtil.isEmpty(scmSupplierLimitMain.getYear())){
            throw new BizException("年度不能重复");
        }
        List<ScmSupplierLimit> reqList = supplierlimitManager.selectYear(scmSupplierLimitMain.getYear());
       if (ListUtil.isNotEmpty(reqList)){
           throw new BizException("年度不能重复");
       }

    }
}
