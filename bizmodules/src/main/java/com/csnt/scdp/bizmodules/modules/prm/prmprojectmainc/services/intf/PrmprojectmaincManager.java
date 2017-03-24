package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf;

import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetAccessoryC;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetOutsourceC;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetPrincipalC;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMainC;
import com.csnt.scdp.bizmodules.entityattributes.prm.*;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmProjectMainCDto;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.exception.BizException;

import java.util.List;
import java.util.Map;

/**
 * Description:  PrmprojectmaincManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-27 15:28:19
 */
public interface PrmprojectmaincManager {

    String LAST_UUID = PrmBudgetDetailCAttribute.LAST_UUID;
    String UUID = CommonAttribute.UUID;

    String[] BUDGET_PRINCIPAL_QUANTITY_FIELDS = {
            PrmBudgetPrincipalAttribute.AMOUNT,
            PrmBudgetPrincipalAttribute.SCHEMING_PRICE,
    };
    String[] BUDGET_ACCESSORY_QUANTITY_FIELDS = {
            PrmBudgetAccessoryAttribute.AMOUNT,
            PrmBudgetAccessoryAttribute.PRICE,
    };
    String[] BUDGET_OUTSOURCE_QUANTITY_FIELDS = {
            PrmBudgetOutsourceAttribute.AMOUNT,
            PrmBudgetOutsourceAttribute.PRICE,
    };

    public PrmProjectMainCDto wrapperProjectMainToChange(String uuid, String detailType);

    public void loadExtraDescField(PrmProjectMainCDto dtoObj);

    String syncMainChangeToMain(String mainChangeUuid);

    void syncProjectDetail(String mainChangeUuid, String mainUuid, Class revisedClazz, Class oldClazz, boolean isInsert);

    String generateProjectCode(String uuid, String stampType, String sijiType, boolean... preview) throws BizException;

    void updateProjectCode(String mainChangeUuid, String mainUuid, String projectCode);

    void invokeApproveAction(String uuid);

    void updateApproveFields(String uuid) throws BizException;

    void updateHeaderAndDetailAmount(String uuid) throws BizException;

    List<PrmBudgetOutsourceC> reloadOutsourceFromPurchasePlan(String projectUuid, List<Map<String, Object>>
            lstPrmBudget);

    List<PrmBudgetPrincipalC> reloadPrincipalFromPurchasePlan(String projectUuid, List<Map<String, Object>>
            lstPrmBudget);

    List<PrmBudgetAccessoryC> reloadAccessoryFromPurchasePlan(String projectUuid, List<Map<String, Object>>
            lstPrmBudget);

    boolean validateProjectName(PrmProjectMainC prmProjectMainC);

    String getProjectManagerRoleUuid();

    List<String> validateSerialNumber(PrmProjectMainCDto projectMainCDto);

    List<String> validateSplitItemTotalValue(PrmProjectMainCDto prmProjectMainC);

    public void sendMsg(String projectCode, String projectName, String contractorOffice, String step);

    public void sendMsg(String uuid, String projectName, String type);

    String getPrmChangeTaxType(String prmProjectMainCId);

    void handlePrmCodeType(PrmProjectMainCDto prmProjectMainC);

    boolean isProjectMainChangeDepartInner(String prmProjectMainCId);

    public void sendMsgToManager(String uuid, String projectCode);

}