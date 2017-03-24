package com.csnt.scdp.bizmodules.modules.prm.prmexamdatehistory.action;

import com.csnt.scdp.bizmodules.entity.fad.FadRtfreevalue;
import com.csnt.scdp.bizmodules.entity.prm.PrmExamPeriod;
import com.csnt.scdp.bizmodules.modules.prm.prmexamdatehistory.dto.PrmExamDateHistoryDto;
import com.csnt.scdp.bizmodules.modules.prm.prmexamperiod.services.intf.PrmExamPeriodManager;
import com.csnt.scdp.framework.commonaction.crud.CommonAddAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/15.
 */
@Scope("singleton")
@Controller("prmexampdatahistory-add")
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

    @Override
    protected void afterAction(BasePojo dtoObj) {
        PrmExamDateHistoryDto Obj = (PrmExamDateHistoryDto) dtoObj;
        Object businessData = PersistenceFactory.getInstance().findByPK(BeanFactory.getClass(Obj.getTableName()), Obj.getDataUuid());
        BeanUtil.setProperty(businessData, Obj.getColumnName(), Obj.getNewDate());
        PersistenceFactory.getInstance().update(businessData);
    }
}
