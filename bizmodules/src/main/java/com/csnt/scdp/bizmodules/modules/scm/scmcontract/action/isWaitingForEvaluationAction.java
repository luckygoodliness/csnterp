package com.csnt.scdp.bizmodules.modules.scm.scmcontract.action;

import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("scmcontract-iswaitingforevaluation")
@Transactional
public class isWaitingForEvaluationAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(isWaitingForEvaluationAction.class);


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String supplierId = (String) inMap.get("supplierId");
        //结算合同
        List paramList = new ArrayList<>();
        paramList.add(supplierId);
        DAOMeta daoMeta = DAOHelper.getDAO("scmcontract-dao", "get_supplier_last_contract_time", paramList);
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        List<Map> detailList = (List) pcm.findByNativeSQL(daoMeta);
        rsMap.put("result", "false");
        if (ListUtil.isNotEmpty(detailList)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date lateDate = sdf.parse("" + detailList.get(0).get("lastTime"));
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                int tYear = c.get(Calendar.YEAR);
                long tTime = c.getTimeInMillis();
                c.setTime(lateDate);
                int lYear = c.get(Calendar.YEAR);
                long lTime = c.getTimeInMillis();
                long between_days = (tTime - lTime) / (1000 * 3600 * 24);
                if (lYear != tYear) {
                    rsMap.put("result", "true");
                }
                if (between_days > 90) {
                    rsMap.put("result", "true");
                }
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        } else {
            rsMap.put("result", "true");
        }
        return rsMap;
    }
}
