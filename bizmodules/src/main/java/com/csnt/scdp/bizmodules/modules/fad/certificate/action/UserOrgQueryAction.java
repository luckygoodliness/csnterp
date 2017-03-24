package com.csnt.scdp.bizmodules.modules.fad.certificate.action;

import com.csnt.scdp.bizmodules.entity.fad.*;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entityattributes.fad.*;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmCustomerAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierAttribute;
import com.csnt.scdp.bizmodules.modules.fad.certificate.dto.FadCertificateDetailDto;
import com.csnt.scdp.bizmodules.modules.fad.certificate.dto.FadCertificateDto;
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
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;

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
@Controller("certificate-userOrgQuery")
@Transactional
public class UserOrgQueryAction extends CommonLoadAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(UserOrgQueryAction.class);
    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap<>();

        String OrgUuid = "";
        String OrgCode = "";
        String OrgName = "";
        String receiverOrPayerId = certificateManager.isNullReturnEmpty(inMap.get(FadCertificateAttribute.RECEIVER_OR_PAYER_ID));
        String receiverOrPayerCode = certificateManager.isNullReturnEmpty(inMap.get(FadCertificateAttribute.RECEIVER_OR_PAYER_CODE));

        if (certificateManager.isUuidBelongNcCode(receiverOrPayerId)) {
            Map<String, Object> fadNcPersonConditionsMap = new HashMap<String, Object>();
            fadNcPersonConditionsMap.put(FadNcPersonAttribute.NC_PERSON_CODE, receiverOrPayerCode);
            List<FadNcPerson> fadNcPersonList = certificateManager.isNullReturnEmpty(receiverOrPayerCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadNcPerson.class, fadNcPersonConditionsMap, null) : new ArrayList<>();
            if (fadNcPersonList.size() > 0) {
                FadNcPerson fadNcPerson = (FadNcPerson) fadNcPersonList.get(0);
                Map<String, Object> fadNcOrgConditionsMap = new HashMap<String, Object>();
                fadNcOrgConditionsMap.put(FadNcOrgAttribute.NC_ORG_CODE, certificateManager.isNullReturnEmpty(fadNcPerson.getNcOrgCode()));
                List<FadNcOrg> fadNcOrgList = certificateManager.isNullReturnEmpty(fadNcPerson.getNcOrgCode()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadNcOrg.class, fadNcOrgConditionsMap, null) : new ArrayList<>();
                if (fadNcOrgList.size() > 0) {
                    FadNcOrg fadNcOrg = (FadNcOrg) fadNcOrgList.get(0);
                    OrgUuid = receiverOrPayerId;
                    OrgCode = certificateManager.isNullReturnEmpty(fadNcOrg.getNcOrgCode());
                    OrgName = certificateManager.isNullReturnEmpty(fadNcOrg.getNcOrgName());
                }
            }
        } else {
            Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
            scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, receiverOrPayerCode);
            List<ScdpUser> scdpUserList = certificateManager.isNullReturnEmpty(receiverOrPayerCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
            if (scdpUserList.size() > 0) {
                ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                ScdpOrg scdpOrg = certificateManager.isNullReturnEmpty(scdpUser.getOrgUuid()).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScdpOrg.class, certificateManager.isNullReturnEmpty(scdpUser.getOrgUuid())) : null;
                if (scdpOrg != null) {
                    OrgUuid = certificateManager.isNullReturnEmpty(scdpOrg.getUuid());
                    OrgCode = certificateManager.isNullReturnEmpty(scdpOrg.getOrgCode());
                    OrgName = certificateManager.isNullReturnEmpty(scdpOrg.getOrgName());
                }
            }
        }

        result.put("OrgUuid", OrgUuid);
        result.put("OrgCode", OrgCode);
        result.put("OrgName", OrgName);

        return result;
    }
}
