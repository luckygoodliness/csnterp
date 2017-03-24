package com.csnt.scdp.bizmodules.modules.scm.scmsae.action;

import com.csnt.scdp.bizmodules.modules.scm.scmsae.services.intf.ScmsaeManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by linzp on 2016/8/30.
 */
@Scope("singleton")
@Controller("scmsaeform-checkformempty")
@Transactional
public class CheckFormEmptyAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(CheckFormEmptyAction.class);

    @Override
    public Map perform(Map map) throws BizException, SysException {
        Map rsMap = new HashMap();
        String scmSaeId = map.get("scmSaeId").toString();
        String sql = "SELECT COUNT(1) AS NUM\n" +
                "      FROM SCM_SAE_FORM SSF\n" +
                "     WHERE EXISTS (SELECT 1\n" +
                "              FROM SCM_SAE_OBJECT SSO\n" +
                "             WHERE SSO.SCM_SAE_ID = '"+scmSaeId+"'\n" +
                "               AND SSO.UUID = SSF.SCM_SAE_OBJECT_ID)";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List<Map<String, Object>> resultList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        Map<String, Object> result = resultList.get(0);
        BigDecimal num = (BigDecimal)result.get("num");
        String message = null;
        if(num.intValue() == 0) {
            message = "FALSE";
        }

        rsMap.put("message", message);
        return rsMap;
    }
}
