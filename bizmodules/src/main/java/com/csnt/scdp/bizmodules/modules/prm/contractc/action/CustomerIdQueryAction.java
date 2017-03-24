package com.csnt.scdp.bizmodules.modules.prm.contractc.action;

import com.csnt.scdp.bizmodules.entity.fad.FadCertificateDetail;
import com.csnt.scdp.bizmodules.entity.fad.FadRtfreevalueView;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadRtfreevalueViewAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmCustomerAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierAttribute;
import com.csnt.scdp.bizmodules.modules.fad.certificate.dto.FadCertificateDetailDto;
import com.csnt.scdp.bizmodules.modules.fad.certificate.dto.FadCertificateDto;
import com.csnt.scdp.bizmodules.modules.prm.contractc.services.intf.ContractCManager;
import com.csnt.scdp.framework.helper.DtoHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.bizmodules.entity.fad.FadAccsubjRtfreevalue;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCertificateAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCertificateDetailAttribute;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadAccsubjRtfreevalueAttribute;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Reese on 2015/10/14.
 */
@Scope("singleton")
@Controller("contract-c-customerIdQuery")
@Transactional
public class CustomerIdQueryAction extends CommonLoadAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(CustomerIdQueryAction.class);
    @Resource(name = "contract-c-manager")
    private ContractCManager contractManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap<>();
        result.put("isInScdpOrg", false);

        String customerId = contractManager.isNullReturnEmpty(inMap.get("customerId"));
        if (contractManager.isNullReturnEmpty(customerId).length() > 0) {
            Map<String, Object> scdpOrgConditionsMap = new HashMap<String, Object>();
            scdpOrgConditionsMap.put(ScdpOrgAttribute.ORG_NAME, contractManager.isNullReturnEmpty(customerId));
            List<ScdpOrg> scdpOrgList = PersistenceFactory.getInstance().findByAnyFields(ScdpOrg.class, scdpOrgConditionsMap, null);
            if (scdpOrgList.size() > 0) {
                result.put("isInScdpOrg", true);
            }
        }
        return result;
    }
}
