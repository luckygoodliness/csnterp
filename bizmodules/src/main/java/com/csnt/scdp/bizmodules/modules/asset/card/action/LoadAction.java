package com.csnt.scdp.bizmodules.modules.asset.card.action;

import com.csnt.scdp.bizmodules.entity.asset.AssetCard;
import com.csnt.scdp.bizmodules.modules.asset.card.dto.AssetCardDto;
import com.csnt.scdp.bizmodules.modules.asset.card.services.intf.CardManager;
import com.csnt.scdp.bizmodules.modules.asset.check.dto.AssetAnnualCheckDto;
import com.csnt.scdp.bizmodules.modules.asset.maintain.dto.AssetMaintainDto;
import com.csnt.scdp.bizmodules.modules.asset.transfer.dto.AssetTransferDto;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 10:18:39
 */

@Scope("singleton")
@Controller("card-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "card-manager")
    private CardManager cardManager;

    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    String menuCode = "";

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        //Do before
        this.menuCode = certificateManager.isNullReturnEmpty(inMap.get("menuCode"));
        Map out = super.perform(inMap);
        if (this.menuCode.equals("ASSETCARD")) {
            Map dtoMap = (Map) out.get("assetCardDto");
            if (dtoMap != null) {
                String orgCode = certificateManager.isNullReturnEmpty(dtoMap.get("officeId"));
                if (certificateManager.isNullReturnEmpty(orgCode).length() > 0) {
                    String orgName = certificateManager.isNullReturnEmpty(OrgHelper.getOrgNameByCode(orgCode));
                    dtoMap.put("officeIdDesc", orgName);
                }

                String userId = certificateManager.isNullReturnEmpty(dtoMap.get("liablePerson"));
                Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, userId);
                List<ScdpUser> scdpUserList = certificateManager.isNullReturnEmpty(userId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                if (scdpUserList.size() > 0) {
                    ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                    String userName = certificateManager.isNullReturnEmpty(scdpUser.getUserName());
                    dtoMap.put("liablePersonDesc", userName);
                }
            }
        } else if (this.menuCode.equals("ASSETADDCARD")) {
            Map dtoMap = (Map) out.get("assetHandoverDto");
            if (dtoMap != null) {
                String orgCode = certificateManager.isNullReturnEmpty(dtoMap.get("officeId"));
                if (certificateManager.isNullReturnEmpty(orgCode).length() > 0) {
                    String orgName = certificateManager.isNullReturnEmpty(OrgHelper.getOrgNameByCode(orgCode));
                    dtoMap.put("officeIdDesc", orgName);
                }

                String userId = certificateManager.isNullReturnEmpty(dtoMap.get("liablePerson"));
                Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, userId);
                List<ScdpUser> scdpUserList = certificateManager.isNullReturnEmpty(userId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                if (scdpUserList.size() > 0) {
                    ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                    String userName = certificateManager.isNullReturnEmpty(scdpUser.getUserName());
                    dtoMap.put("liablePersonDesc", userName);
                }
            }
        } else if (this.menuCode.equals("ASSETTRANSFERCARD")) {
            Map dtoMap = (Map) out.get("assetCardTransferDto");
            if (dtoMap != null) {
                AssetCard assetCard = certificateManager.isNullReturnEmpty(dtoMap.get("cardUuid")).length() > 0 ? PersistenceFactory.getInstance().findByPK(AssetCard.class, certificateManager.isNullReturnEmpty(dtoMap.get("cardUuid"))) : null;
                if (assetCard != null) {
                    dtoMap.put("cardCode", assetCard.getCardCode());
                    dtoMap.put("assetCode", assetCard.getAssetCode());
                    dtoMap.put("assetName", assetCard.getAssetName());
                    dtoMap.put("assetTypeCode", assetCard.getAssetTypeCode());
                    dtoMap.put("deviceType", assetCard.getDeviceType());
                    dtoMap.put("specification", assetCard.getSpecification());
                    dtoMap.put("model", assetCard.getModel());
                    dtoMap.put("storeplace", assetCard.getStoreplace());
                    dtoMap.put("status", assetCard.getStatus());
                    dtoMap.put("fromNc", assetCard.getFromNc());
                    dtoMap.put("purchaseTime", assetCard.getPurchaseTime());
                    dtoMap.put("discardTime", assetCard.getDiscardTime());
                    dtoMap.put("limitMonth", assetCard.getLimitMonth());
                    dtoMap.put("localValue", assetCard.getLocalValue());
                    dtoMap.put("monthDepreciation", assetCard.getMonthDepreciation());
                    dtoMap.put("netValue", assetCard.getNetValue());
                    dtoMap.put("factoryName", assetCard.getFactoryName());
                    dtoMap.put("releaseDate", assetCard.getReleaseDate());
                    dtoMap.put("unit", assetCard.getUnit());
                    dtoMap.put("identificationNumber", assetCard.getIdentificationNumber());
                    dtoMap.put("buildingProperty", assetCard.getBuildingProperty());
                    dtoMap.put("area", assetCard.getArea());
                    dtoMap.put("chassisNumber", assetCard.getChassisNumber());
                    dtoMap.put("vehicleNumber", assetCard.getVehicleNumber());
                    dtoMap.put("vehicleType", assetCard.getVehicleType());
                    dtoMap.put("authorizationCode", assetCard.getAuthorizationCode());
                    dtoMap.put("checkedDate", assetCard.getCheckedDate());
                    dtoMap.put("validDate", assetCard.getValidDate());
                    dtoMap.put("annualCheckExpiredDate", assetCard.getAnnualCheckExpiredDate());
                    dtoMap.put("insuranceExpiredDate", assetCard.getInsuranceExpiredDate());
                    dtoMap.put("accessory", assetCard.getAccessory());
                    dtoMap.put("source", assetCard.getSource());
                    dtoMap.put("operationUnit", assetCard.getOperationUnit());
                    dtoMap.put("operator", assetCard.getOperator());
                    dtoMap.put("operatorTel", assetCard.getOperatorTel());
                    dtoMap.put("assetHandoverUuid", assetCard.getAssetHandoverUuid());

                    String orgCode = certificateManager.isNullReturnEmpty(dtoMap.get("officeId"));
                    if (certificateManager.isNullReturnEmpty(orgCode).length() > 0) {
                        String orgName = certificateManager.isNullReturnEmpty(OrgHelper.getOrgNameByCode(orgCode));
                        dtoMap.put("officeIdDesc", orgName);
                    }

                    String userId = certificateManager.isNullReturnEmpty(dtoMap.get("liablePerson"));
                    Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                    scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, userId);
                    List<ScdpUser> scdpUserList = certificateManager.isNullReturnEmpty(userId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                    if (scdpUserList.size() > 0) {
                        ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                        String userName = certificateManager.isNullReturnEmpty(scdpUser.getUserName());
                        dtoMap.put("liablePersonDesc", userName);
                    }
                } else {
                    throw new BizException("未找到【固定资产变动申请单】对应的【固定资产卡片】");
                }
            }
        }

        //Do After
        return out;
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        if (this.menuCode.equals("ASSETCARD")) {
            AssetCardDto assetCardDto = (AssetCardDto) dtoObj;
            List assetMaintainLst = assetCardDto.getAssetMaintainDto();
            //维修记录只显示已通过的记录
            List assetTransferDtoLst = assetCardDto.getAssetTransferDto();
            if (ListUtil.isNotEmpty(assetTransferDtoLst)) {
                for (Object obj : assetTransferDtoLst) {
                    AssetTransferDto assetTransferDto = (AssetTransferDto) obj;

                    //转入
                    {
                        String orgCode = certificateManager.isNullReturnEmpty(assetTransferDto.getInOfficeId());
                        Map scdpOrgConditionsMap = new HashMap();
                        scdpOrgConditionsMap.put(ScdpOrgAttribute.ORG_CODE, orgCode);
                        List list = certificateManager.isNullReturnEmpty(orgCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpOrg.class, scdpOrgConditionsMap, null) : new ArrayList<>();
                        if (list.size() > 0) {
                            ScdpOrg scdpOrgIn = (ScdpOrg) list.get(0);
                            assetTransferDto.setInOfficeIdDesc(certificateManager.isNullReturnEmpty(scdpOrgIn.getOrgName()));
                        }

                        String userId = certificateManager.isNullReturnEmpty(assetTransferDto.getInLiablePerson());
                        Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                        scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, userId);
                        List<ScdpUser> scdpUserList = certificateManager.isNullReturnEmpty(userId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                        if (scdpUserList.size() > 0) {
                            ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                            String userName = certificateManager.isNullReturnEmpty(scdpUser.getUserName());
                            assetTransferDto.setInLiablePersonDesc(userName);
                        }
                    }

                    //转出
                    {
                        String orgCode = certificateManager.isNullReturnEmpty(assetTransferDto.getOutOfficeId());
                        Map scdpOrgConditionsMap = new HashMap();
                        scdpOrgConditionsMap.put(ScdpOrgAttribute.ORG_CODE, orgCode);
                        List list = certificateManager.isNullReturnEmpty(orgCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpOrg.class, scdpOrgConditionsMap, null) : new ArrayList<>();
                        if (list.size() > 0) {
                            ScdpOrg scdpOrgOut = (ScdpOrg) list.get(0);
                            assetTransferDto.setOutOfficeIdDesc(certificateManager.isNullReturnEmpty(scdpOrgOut.getOrgName()));
                        }

                        String userId = certificateManager.isNullReturnEmpty(assetTransferDto.getOutLiablePerson());
                        Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
                        scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, userId);
                        List<ScdpUser> scdpUserList = certificateManager.isNullReturnEmpty(userId).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
                        if (scdpUserList.size() > 0) {
                            ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
                            String userName = certificateManager.isNullReturnEmpty(scdpUser.getUserName());
                            assetTransferDto.setOutLiablePersonDesc(userName);
                        }
                    }
                }
            }

            Date purchaseTime = assetCardDto.getPurchaseTime();
            Integer start = 0;
            if (purchaseTime != null) {
                start = DateUtil.getDateField(purchaseTime, Calendar.YEAR);
            }
            List assetAnnualCheckLst = assetCardDto.getAssetAnnualCheckDto();
            if (ListUtil.isNotEmpty(assetAnnualCheckLst)) {
                for (Object obj : assetAnnualCheckLst) {
                    AssetAnnualCheckDto assetAnnualCheckDto = (AssetAnnualCheckDto) obj;
                    Date checkDate = assetAnnualCheckDto.getAnnualCheckTime();
                    Integer end = DateUtil.getDateField(checkDate, Calendar.YEAR);
                    assetAnnualCheckDto.setAlreadyUse(end - start);
                }
            }
        }
    }
}
