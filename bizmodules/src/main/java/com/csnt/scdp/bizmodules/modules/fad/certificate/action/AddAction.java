package com.csnt.scdp.bizmodules.modules.fad.certificate.action;

import com.csnt.scdp.bizmodules.entity.fad.FadCertifiDeficitRel;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCertifiDeficitRelAttribute;
import com.csnt.scdp.bizmodules.modules.fad.certificate.dto.FadCertificateDto;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCertificateAttribute;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-16 10:07:48
 */

@Scope("singleton")
@Controller("certificate-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = null;
        try {

            //Do before
            out = super.perform(inMap);
            //Do After

            return out;
        } catch (BizException e) {
            throw e;
        } finally {
            try {
                String fadCertificateUuid = certificateManager.isNullReturnEmpty(out.get(FadCertificateAttribute.UUID));
                Map viewdata = (Map) inMap.get(CommonAttribute.VIEW_DATA);
                Map fadCertificateMap = (Map) viewdata.get("fadCertificateDto");

                String years = certificateManager.isNullReturnEmpty(fadCertificateMap.get(FadCertificateAttribute.YEARS));
                String months = certificateManager.isNullReturnEmpty(fadCertificateMap.get(FadCertificateAttribute.MONTHS));
                String state = certificateManager.isNullReturnEmpty(fadCertificateMap.get(FadCertificateAttribute.STATE));
                certificateManager.certificateCheckToDayValidDate(years + months, "yyyyMM", state);

                String free2 = certificateManager.isNullReturnEmpty(fadCertificateMap.get("free2"));
                String free3 = certificateManager.isNullReturnEmpty(fadCertificateMap.get("free3"));

                if (certificateManager.isNullReturnEmpty(free2).length() > 0) {

                    certificateManager.certificateCheckTimeStamp(free2, free3);

                    String sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE UUID = '" + free2 + "' AND FREE2 IS NOT NULL";
                    DAOMeta daoMeta = new DAOMeta();
                    daoMeta.setStrSql(sql);
                    daoMeta.setNeedFilter(false);
                    if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                        throw new BizException("【复制凭证】不能复制新增!");
                    }

                    //凭证红冲关系取得
                    Map<String, Object> fadCertifiDeficitRelConditionsMap = new HashMap<String, Object>();
                    fadCertifiDeficitRelConditionsMap.put(FadCertifiDeficitRelAttribute.DEFICIT_ID, free2);
                    List<FadCertifiDeficitRel> fadCertifiDeficitRelList = certificateManager.isNullReturnEmpty(free2).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiDeficitRelConditionsMap, "seq_no asc") : new ArrayList<>();
                    if (fadCertifiDeficitRelList.size() > 0) {
                        throw new BizException("【红冲凭证】不能复制新增!");
                    }

                    sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE UUID = '" + free2 + "' AND DEBTOR < 0";
                    daoMeta = new DAOMeta();
                    daoMeta.setStrSql(sql);
                    daoMeta.setNeedFilter(false);
                    if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                        throw new BizException("【赤字凭证】不能复制新增!");
                    }

                    sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE UUID = '" + free2 + "' AND STATE = '1'";
                    daoMeta = new DAOMeta();
                    daoMeta.setStrSql(sql);
                    daoMeta.setNeedFilter(false);
                    if (!(PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0)) {
                        throw new BizException("【未发送凭证】不能复制新增!");
                    }

                    //凭证冲红关系取得
                    Map<String, Object> fadCertifiCertifiRelConditionsMap = new HashMap<String, Object>();
                    fadCertifiCertifiRelConditionsMap.put(FadCertifiDeficitRelAttribute.CERTIFI_ID, free2);
                    List<FadCertifiDeficitRel> fadCertifiCertifiRelList = certificateManager.isNullReturnEmpty(free2).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiCertifiRelConditionsMap, "seq_no asc") : new ArrayList<>();
                    if (!(fadCertifiCertifiRelList.size() > 0)) {
                        throw new BizException("【未红冲凭证】不能复制新增!");
                    }
                    FadCertifiDeficitRel fadCertifiDeficitRel = fadCertifiCertifiRelList.get(0);

                    //主表凭证取得
                    FadCertificateDto fadCertificate = certificateManager.isNullReturnEmpty(fadCertifiDeficitRel.getDeficitId()).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, certificateManager.isNullReturnEmpty(fadCertifiDeficitRel.getDeficitId())) : null;
                    if (fadCertificate != null) {
                        if (!(certificateManager.isNullReturnEmpty(fadCertificate.getState()).equals("1"))) {
                            throw new BizException("本张【已红冲凭证】对应的【红冲凭证】尚未发送NC不能复制新增!");
                        } else {
                            sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE FREE2 = '" + free2 + "' AND UUID <> '" + fadCertificateUuid + "' AND STATE IN ('0', '1')";
                            daoMeta = new DAOMeta();
                            daoMeta.setStrSql(sql);
                            daoMeta.setNeedFilter(false);
                            if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                                throw new BizException("本张【已红冲凭证】已复制新增,不能再次复制新增!");
                            }
                        }
                    } else {
                        throw new BizException("本张【已红冲凭证】对应的【红冲凭证】尚未发送NC不能复制新增!");
                    }
                }
                certificateManager.certificateMergeOriginalFormCodeAbstractsFromCertificateDetailByFadCertificateUuid(fadCertificateUuid);
                //certificateManager.certificateDetailMerge(fadCertificateUuid);

                String sql = "UPDATE FAD_CERTIFICATE SET SEQ_NO = FAD_CERTIFICATE_SEQ_NO.NEXTVAL WHERE UUID = '" + fadCertificateUuid + "' AND SEQ_NO IS NULL";
                DAOMeta daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
            } catch (BizException e) {
                throw e;
            }
        }
    }
}
