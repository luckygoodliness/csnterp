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
@Controller("certificate-certificateSendNCUpdata")
@Transactional
public class CertificateSendNCUpdataAction extends CommonLoadAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(CertificateSendNCUpdataAction.class);
    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        String fadCertificateUuid = certificateManager.isNullReturnEmpty(inMap.get("fadCertificateUuid"));
        String tblVersion = certificateManager.isNullReturnEmpty(inMap.get("tblVersion"));
        certificateManager.certificateCheckTimeStamp(fadCertificateUuid, tblVersion);

        //主表凭证取得
        FadCertificateDto fadCertificate = certificateManager.isNullReturnEmpty(fadCertificateUuid).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, fadCertificateUuid) : null;
        if (fadCertificate != null) {
            String years = certificateManager.isNullReturnEmpty(fadCertificate.getYears());
            String months = certificateManager.isNullReturnEmpty(fadCertificate.getMonths());
            String state = certificateManager.isNullReturnEmpty(fadCertificate.getState());
            certificateManager.certificateCheckToDayValidDate(years + months, "yyyyMM", state);
        }

        String makerUuid = certificateManager.isNullReturnEmpty(inMap.get("makerUuid"));
        return certificateManager.certificateSendNC(fadCertificateUuid, makerUuid);
    }
}
