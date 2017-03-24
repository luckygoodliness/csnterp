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
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
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
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-16 10:07:48
 */

@Scope("singleton")
@Controller("certificate-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = null;
        Map result = new HashMap<>();
        Map viewdata = (Map) inMap.get(CommonAttribute.VIEW_DATA);
        Map fadCertificateMap = (Map) viewdata.get("fadCertificateDto");

        String fadCertificateUuid = certificateManager.isNullReturnEmpty(fadCertificateMap.get(FadCertificateAttribute.UUID));
        String tblVersion = certificateManager.isNullReturnEmpty(fadCertificateMap.get("tblVersion"));
        //certificateManager.certificateCheckTimeStamp(fadCertificateUuid, tblVersion);

        try {

            //Do before
            out = super.perform(inMap);
            //Do After

            return out;
        } catch (BizException e) {
            throw e;
        } finally {
            try {

                //主表凭证取得
                FadCertificateDto fadCertificate = certificateManager.isNullReturnEmpty(fadCertificateUuid).length() > 0 ? (FadCertificateDto) DtoHelper.findDtoByPK(FadCertificateDto.class, fadCertificateUuid) : null;
                if (fadCertificate != null) {
                    if (certificateManager.isNullReturnEmpty(fadCertificate.getState()).equals("1")) {

                        //凭证冲红关系取得
                        Map<String, Object> fadCertifiCertifiRelConditionsMap = new HashMap<String, Object>();
                        fadCertifiCertifiRelConditionsMap.put(FadCertifiDeficitRelAttribute.CERTIFI_ID, fadCertificateUuid);
                        List<FadCertifiDeficitRel> fadCertifiCertifiRelList = certificateManager.isNullReturnEmpty(fadCertificateUuid).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiCertifiRelConditionsMap, "seq_no asc") : new ArrayList<>();
                        if (fadCertifiCertifiRelList.size() > 0) {
                            throw new BizException("【已红冲凭证】不能发送保存!");
                        }

                        //凭证红冲关系取得
                        Map<String, Object> fadCertifiDeficitRelConditionsMap = new HashMap<String, Object>();
                        fadCertifiDeficitRelConditionsMap.put(FadCertifiDeficitRelAttribute.DEFICIT_ID, fadCertificateUuid);
                        List<FadCertifiDeficitRel> fadCertifiDeficitRelList = certificateManager.isNullReturnEmpty(fadCertificateUuid).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(FadCertifiDeficitRel.class, fadCertifiDeficitRelConditionsMap, "seq_no asc") : new ArrayList<>();
                        if (fadCertifiDeficitRelList.size() > 0) {
                            throw new BizException("【红冲凭证】不能发送保存!");
                        }

                        String sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE UUID = '" + fadCertificateUuid + "' AND DEBTOR < 0";
                        DAOMeta daoMeta = new DAOMeta();
                        daoMeta.setStrSql(sql);
                        daoMeta.setNeedFilter(false);
                        if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                            throw new BizException("【赤字凭证】不能发送保存!");
                        }

                        sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE UUID = '" + fadCertificateUuid + "' AND NVL(IS_VOID, 0) = 1";
                        daoMeta = new DAOMeta();
                        daoMeta.setStrSql(sql);
                        daoMeta.setNeedFilter(false);
                        if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                            throw new BizException("【已逻辑删除凭证】不能发送保存!");
                        }

                        sql = "SELECT 1 FROM FAD_CERTIFICATE WHERE UUID = '" + fadCertificateUuid + "' AND STATE = '2'";
                        daoMeta = new DAOMeta();
                        daoMeta.setStrSql(sql);
                        daoMeta.setNeedFilter(false);
                        if (PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta) > 0) {
                            throw new BizException("【作废凭证】不能发送保存!");
                        }

                        //Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                        //scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, certificateManager.isNullReturnEmpty(fadCertificate.getMaker()));
                        //List<ScdpUser> scdpUserList = certificateManager.isNullReturnEmpty(fadCertificate.getMaker()).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                        sql = "SELECT * FROM SCDP_USER WHERE USER_ID = '" + certificateManager.isNullReturnEmpty(fadCertificate.getMaker()) + "'";
                        daoMeta = new DAOMeta();
                        daoMeta.setStrSql(sql);
                        daoMeta.setNeedFilter(false);
                        List scdpUserList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                        if (scdpUserList.size() > 0) {
                            Map scdpUser = (Map) scdpUserList.get(0);
                            String makerUuid = certificateManager.isNullReturnEmpty(scdpUser.get("uuid"));
                            result = certificateManager.certificateSendNC(fadCertificateUuid, makerUuid);
                            if (certificateManager.isNullReturnEmpty(result.get("state")).length() > 0) {
                                if (!(certificateManager.isNullReturnEmpty(result.get("state")).equals("1"))) {
                                    throw new BizException("发送NC失败,凭证保存失败:【NC反馈:" + certificateManager.isNullReturnEmpty(result.get("feedback")) + "】");
                                }
                            } else if (certificateManager.isNullReturnEmpty(result.get("errMessages")).length() > 0) {
                                throw new BizException(certificateManager.isNullReturnEmpty(result.get("errMessages")));
                            } else {
                                throw new BizException("连接【NCService】失败,凭证保存失败,请联系网络管理员!");
                            }
                        } else {
                            throw new BizException("【凭证抬头】【制单人】不能为空!");
                        }
                    } else {
                        String years = certificateManager.isNullReturnEmpty(fadCertificate.getYears());
                        String months = certificateManager.isNullReturnEmpty(fadCertificate.getMonths());
                        String state = certificateManager.isNullReturnEmpty(fadCertificate.getState());
                        certificateManager.certificateCheckToDayValidDate(years + months, "yyyyMM", state);
                    }
                } else {
                    throw new BizException("记录不存在!");
                }

                certificateManager.certificateMergeOriginalFormCodeAbstractsFromCertificateDetailByFadCertificateUuid(fadCertificateUuid);
                //certificateManager.certificateDetailMerge(fadCertificateUuid);
            } catch (BizException e) {
                throw new BizException(e.getExceptionKey().replaceAll("[\\t\\n\\r]", ""));
            }
        }
    }
}