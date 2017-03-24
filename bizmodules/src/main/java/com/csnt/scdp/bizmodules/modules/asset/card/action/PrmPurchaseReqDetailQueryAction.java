package com.csnt.scdp.bizmodules.modules.asset.card.action;

import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.StringUtil;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.asset.card.services.intf.CardManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 10:18:39
 */

@Scope("singleton")
@Controller("prmpurchasereqdetail-query")
@Transactional
public class PrmPurchaseReqDetailQueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(PrmPurchaseReqDetailQueryAction.class);

    @Resource(name = "card-manager")
    private CardManager cardManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        //Do before
        inMap.put("scalarMap", addScalarMapForProcinst());
        Map out = super.perform(inMap);

        //Do After
        return out;
    }

    public static Map addScalarMapForProcinst() {
        HashMap scalarMap = new HashMap();
        scalarMap.put("UUID", StandardBasicTypes.STRING);
        scalarMap.put("PURCHASE_REQ_NO", StandardBasicTypes.STRING);
        scalarMap.put("PERSON_ID", StandardBasicTypes.STRING);
        scalarMap.put("PERSON_NAME", StandardBasicTypes.STRING);
        scalarMap.put("DEPARTMENT_CODE", StandardBasicTypes.STRING);
        scalarMap.put("DEPARTMENT_NAME", StandardBasicTypes.STRING);
        scalarMap.put("PRM_PROJECT_MAIN_ID", StandardBasicTypes.STRING);
        scalarMap.put("SUBJECT_NO", StandardBasicTypes.STRING);
        scalarMap.put("SUBJECT_NAME", StandardBasicTypes.STRING);
        scalarMap.put("NAME", StandardBasicTypes.STRING);
        scalarMap.put("MODEL", StandardBasicTypes.STRING);
        scalarMap.put("FACTORY", StandardBasicTypes.STRING);
        scalarMap.put("AMOUNT", StandardBasicTypes.STRING);
        scalarMap.put("BUDGET_PRICE", StandardBasicTypes.STRING);
        scalarMap.put("SUPPLIER_ID", StandardBasicTypes.STRING);
        scalarMap.put("REMARK", StandardBasicTypes.STRING);
        scalarMap.put("SOURCE", StandardBasicTypes.STRING);
        scalarMap.put("CHECK_DATE", StandardBasicTypes.STRING);
        scalarMap.put("SEQ_NO", StandardBasicTypes.STRING);
        scalarMap.put("CREATE_BY", StandardBasicTypes.STRING);
        scalarMap.put("CREATE_TIME", StandardBasicTypes.STRING);
        scalarMap.put("UPDATE_BY", StandardBasicTypes.STRING);
        scalarMap.put("UPDATE_TIME", StandardBasicTypes.STRING);
        scalarMap.put("IS_VOID", StandardBasicTypes.STRING);
        scalarMap.put("LOC_TIMEZONE", StandardBasicTypes.STRING);
        scalarMap.put("TBL_VERSION", StandardBasicTypes.STRING);
        return scalarMap;
    }
}
