package com.csnt.scdp.bizmodules.modules.prm.prmexamperiod.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmExamPeriod;
import com.csnt.scdp.bizmodules.modules.prm.prmexamperiod.services.intf.PrmExamPeriodManager;
import com.csnt.scdp.framework.commonaction.crud.CommonAddAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.dto.BasePojo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/15.
 */
@Scope("singleton")
@Controller("prmexamperiod-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "prmexamperiod-manager")
    private PrmExamPeriodManager prmExamPeriodManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    protected void beforeAction(BasePojo dtoObj) {
        PrmExamPeriod period = (PrmExamPeriod) dtoObj;
        if (!prmExamPeriodManager.checkValidDate(period)) {
            throw new BizException("该时间段与已有数据存在交集！");
        }
    }
}
