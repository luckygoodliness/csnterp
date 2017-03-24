package com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.action;

import com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.services.intf.ScmcontractchangeManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.math.*;
import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Created by lijx on 2016/8/30.
 */
@Scope("singleton")
@Controller("scmcontractchange-loadpuchasereqdetail")
@Transactional
public class LoadPuchaseReqDetailAction implements IAction{
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadPuchaseReqDetailAction.class);

    @Resource(name = "scmcontractchange-manager")
    private ScmcontractchangeManager scmcontractchangeManager;
    @Override
    public Map perform(Map map) throws BizException, SysException {
        String contractId = (String)map.get("uuid");
        List lstResult = new ArrayList();
        Map mapCond = new HashMap();
        mapCond.put("qryCondition", contractId);
        DAOMeta daoMeta = DAOHelper.getDAO("prmpurchasereq-dao","query_load_conchanged",null,mapCond);
        List lst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        for(Object obj : lst){
            HashMap m = (HashMap) obj;
//            m.remove("prmPurchaseReqDetailId");
            m.put("prmPurchaseReqDetailId",m.get("uuid"));
            m.put("changeState","0");
            double amount = m.get("handleAmount")==null?0:  ((BigDecimal)m.get("handleAmount")).doubleValue();
            double expectedPrice = m.get("expectedPrice")==null?0:((BigDecimal)m.get("expectedPrice")).doubleValue();
            m.put("subTotal",amount*expectedPrice);
            m.remove("uuid");
            m.remove("puuid");
            lstResult.add(m);
        }
        Map outMap = new HashMap();
        outMap.put("dto",lstResult);
        return outMap;
    }
}
