package com.csnt.scdp.bizmodules.modules.fad.certificate.action;

import com.csnt.scdp.bizmodules.entity.fad.*;
import com.csnt.scdp.bizmodules.entityattributes.fad.*;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.bizmodules.modules.fad.certificate.dto.FadCertificateAccountDto;
import com.csnt.scdp.bizmodules.modules.fad.certificate.dto.FadCertificateDetailDto;
import com.csnt.scdp.bizmodules.modules.fad.certificate.dto.FadCertificateDto;
import com.csnt.scdp.framework.util.XmlUtil;
import org.dom4j.Document;
import org.dom4j.Element;
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
import com.csnt.scdp.bizmodules.modules.common.helper.ErpExpandFieldHelper;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmCustomerAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * Created by Reese on 2015/10/14.
 */
@Scope("singleton")
@Controller("certificate-certificateBatchSendNCUpdata")
@Transactional
public class CertificateBatchSendNCUpdataAction extends CommonLoadAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(CertificateBatchSendNCUpdataAction.class);
    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap<>();
        List fadCertificateGridSelectionUuidList = (List) inMap.get("fadCertificateGridSelectionUuidList");
        List fadCertificateGridSelectionTblVersionList = (List) inMap.get("fadCertificateGridSelectionTblVersionList");
        String makerUuid = certificateManager.isNullReturnEmpty(inMap.get("makerUuid"));
        String errMessages = "";

        for (int i = 0; i < fadCertificateGridSelectionUuidList.size(); i++) {
            String fadCertificateUuid = certificateManager.isNullReturnEmpty(fadCertificateGridSelectionUuidList.get(i));
            String tblVersion = certificateManager.isNullReturnEmpty(fadCertificateGridSelectionTblVersionList.get(i));
            certificateManager.certificateCheckTimeStamp(fadCertificateUuid, tblVersion);

            //主表凭证取得
            FadCertificateDto fadCertificate = certificateManager.isNullReturnEmpty(fadCertificateUuid).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, fadCertificateUuid) : null;
            if (fadCertificate != null) {
                String years = certificateManager.isNullReturnEmpty(fadCertificate.getYears());
                String months = certificateManager.isNullReturnEmpty(fadCertificate.getMonths());
                String state = certificateManager.isNullReturnEmpty(fadCertificate.getState());
                certificateManager.certificateCheckToDayValidDate(years + months, "yyyyMM", state);
            }

            String errMessage = certificateManager.isNullReturnEmpty(((Map) (certificateManager.certificateSendNC(fadCertificateUuid, makerUuid))).get("errMessages"));
            if (errMessage.length() > 0) {
                if (errMessage.split("\n").length > 1) {
                    errMessage = certificateManager.isNullReturnEmpty(errMessage.split("\n")[0]) + certificateManager.isNullReturnEmpty(errMessage.split("\n")[1]);
                }
                errMessages = (errMessages.length() > 0) ? errMessages + "\n\n" + errMessage : "\n\n" + errMessage;
            }
        }
        result.put("errorMsg", errMessages);
        return result;
    }
}
