package com.csnt.scdp.bizmodules.modules.fad.fadinterestrate.action;

import com.csnt.scdp.bizmodules.modules.fad.fadinterestrate.services.intf.FadinterestrateManager;
import com.csnt.scdp.bizmodules.modules.scm.scmsae.services.intf.ScmsaeManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
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
@Controller("fadinterestrate-validate")
@Transactional
public class ValidateAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ValidateAction.class);

    @Resource(name = "fadinterestrate-manager")
    private FadinterestrateManager fadinterestrateManager;

    @Override
    public Map perform(Map map) throws BizException, SysException {
        Map rsMap = new HashMap();
        String uuid = map.get("uuid").toString();
        String dateFrom = map.get("dateFrom").toString();
        dateFrom = dateFrom.substring(0, dateFrom.indexOf('T'));
        String dateTo = map.get("dateTo").toString();
        dateTo = dateTo.substring(0, dateTo.indexOf('T'));

        StringBuffer sql = new StringBuffer();
        if(StringUtil.isNotEmpty(uuid)) {
            sql.append(" and fad.uuid not in ('" + uuid + "')");
        }
        Map contextMap = new HashMap();
        contextMap.put("dateFrom", dateFrom);
        contextMap.put("dateTo", dateTo);
        contextMap.put("selfSql", sql);
        DAOMeta daoMeta = DAOHelper.getDAO("fadinterestrate-dao", "validate_query", null, contextMap);
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
