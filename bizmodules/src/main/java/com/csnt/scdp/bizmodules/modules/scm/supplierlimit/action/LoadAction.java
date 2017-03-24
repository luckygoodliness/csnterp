package com.csnt.scdp.bizmodules.modules.scm.supplierlimit.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierLimitAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierLimitDetailAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.supplierlimit.services.intf.SupplierlimitManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-07-19 15:14:12
 */

@Scope("singleton")
@Controller("supplierlimit-load")
@Transactional
public class LoadAction extends CommonLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "supplierlimit-manager")
    private SupplierlimitManager supplierlimitManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map scmSupplierLimitMap = (Map) out.get(ScmSupplierLimitAttribute.SCM_SUPPLIER_LIMIT_DTO);
        List<Map> scmSupplierLimitDetailList = (List) scmSupplierLimitMap.get(ScmSupplierLimitDetailAttribute.SCM_SUPPLIER_LIMIT_DETAIL_DTO);

        if (null != scmSupplierLimitMap.get("year")) {
            String taxRate = scmSupplierLimitMap.get("year").toString();
            if (taxRate != null) {
                String taxRateDesc = FMCodeHelper.getDescByCode(taxRate, "CDM_YEAR");
                scmSupplierLimitMap.put("year", taxRateDesc);
            }
        }

        if (ListUtil.isNotEmpty(scmSupplierLimitDetailList)) {
            List<Map<String, Object>> reqList = supplierlimitManager.selectCurAmount(scmSupplierLimitMap.get("uuid").toString());
            Map<String, Map<String, Object>> mapCurAmount = new HashMap<String, Map<String, Object>>();
            if (ListUtil.isNotEmpty(reqList)) {
                for (int i = 0; i < reqList.size(); i++) {
                    mapCurAmount.put(reqList.get(i).get("supplierCode").toString(), reqList.get(i));
                }
            }
            if (null != scmSupplierLimitMap.get("state")) {
                String taxRate = scmSupplierLimitMap.get("state").toString();
                if (taxRate != null) {
                    String taxRateDesc = FMCodeHelper.getDescByCode(taxRate, "FAD_BILL_STATE");
                    scmSupplierLimitMap.put("state", taxRateDesc);
                }
            }
            List supplierId = new ArrayList();
            if (ListUtil.isNotEmpty(scmSupplierLimitDetailList)) {
                for (Map m : scmSupplierLimitDetailList) {
                    supplierId.add(m.get(ScmSupplierLimitDetailAttribute.SCM_SUPPLIER_ID));
                }
            }
            List<ScmSupplier> scmSupplierList = new ArrayList<>();
            if (ListUtil.isNotEmpty(supplierId)) {
                Map mapContractId = new HashMap();
                mapContractId.put("uuid", supplierId);
                scmSupplierList = pcm.findByAnyFields(ScmSupplier.class, mapContractId, null);
            }
            Map<String, ScmSupplier> scdpOrgMap = new HashMap();
            if (ListUtil.isNotEmpty(scmSupplierList)) {
                scmSupplierList.forEach(t -> scdpOrgMap.put(t.getUuid(), t));
                scmSupplierLimitDetailList.forEach(s -> {
                    if (scdpOrgMap.containsKey(s.get(ScmSupplierLimitDetailAttribute.SCM_SUPPLIER_ID))) {
                        ScmSupplier so = scdpOrgMap.get(s.get(ScmSupplierLimitDetailAttribute.SCM_SUPPLIER_ID));
                        if (mapCurAmount.containsKey(so.getUuid())) {
                            s.put("curVolume", mapCurAmount.get(so.getUuid()).get("curVolume"));
                            s.put("curAmount", mapCurAmount.get(so.getUuid()).get("curAmount"));
                        }
                        s.put("scmSupplierName", so.getCompleteName());
                        s.put("supplierType", FMCodeHelper.getDescByCode(so.getSupplierGenre(), "SCM_SUPPLIER_GENRE"));
                    }
                });
            }
        }
        //Do After
        return out;
    }
}

