package com.csnt.scdp.bizmodules.modules.prm.projectmain.services.intf;

import com.csnt.scdp.bizmodules.entityattributes.prm.PrmAssociatedUnitsAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetAccessoryAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetDetailCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetOutsourceAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetPrincipalAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetRunAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmContractAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmMemberDetailPAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPayDetailPCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProgressDetailPAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmQsPAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmReceiptsDetailPAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmSquareDetailPAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmProjectMainCDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmProjectMainDto;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.dto.BasePojo;

import java.util.List;
import java.util.Map;

/**
 * Description:  ProjectmainManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 13:30:43
 */
public interface ProjectmainManager {
    String[] ASSOCIATED_UNITS_FIELDS = {
            PrmAssociatedUnitsAttribute.ASSOCIATED_TYPE,
            PrmAssociatedUnitsAttribute.ASSOCIATED_UNITS_NAME,
            PrmAssociatedUnitsAttribute.REMARK
    };
    String[] MEMBER_DETAIL_FIELDS = {
            PrmMemberDetailPAttribute.STAFF_ID,
            PrmMemberDetailPAttribute.POST,
            PrmMemberDetailPAttribute.JOB_SHARE,
            PrmMemberDetailPAttribute.ENTER_DATE,
            PrmMemberDetailPAttribute.OUT_DATE,
            PrmMemberDetailPAttribute.PERCENT
    };
    String[] PAY_DETAIL_FIELDS = {
            PrmPayDetailPCAttribute.PROJECT_STAGE,
            PrmPayDetailPCAttribute.PAY_CONTENT,
            PrmPayDetailPCAttribute.PAY_MONEY,
            PrmPayDetailPCAttribute.BEGIN_DATE,
            PrmPayDetailPCAttribute.END_DATE
    };
    String[] PROGRESS_DETAIL_FIELDS = {
            PrmProgressDetailPAttribute.PROJECT_STAGE,
            PrmProgressDetailPAttribute.SCHEMING_BEGIN_DATE,
            PrmProgressDetailPAttribute.SCHEMING_END_DATE
    };
    String[] SQUARE_DETAIL_FIELDS = {
            PrmSquareDetailPAttribute.SCHEMING_SQUARE_DATE,
            PrmSquareDetailPAttribute.SCHEMING_SQUARE_MONEY,
            PrmSquareDetailPAttribute.SCHEMING_SQUARE_COST,
            PrmSquareDetailPAttribute.SCHEMING_SQUARE_PROFITS,
            PrmSquareDetailPAttribute.EXPLANATION
    };
    String[] RECEIPTS_DETAIL_FIELDS = {
            PrmReceiptsDetailPAttribute.PROJECT_STAGE,
            PrmReceiptsDetailPAttribute.SCHEMING_RECEIPTS_DATE,
            PrmReceiptsDetailPAttribute.SCHEMING_RECEIPTS_MONEY,
            PrmReceiptsDetailPAttribute.EXPLANATION
    };
    String[] QS_DETAIL_FIELDS = {
            PrmQsPAttribute.SAFE_PRINCIPAL,
            PrmQsPAttribute.SAFE_CONTACT,
            PrmQsPAttribute.SAFE_CONTACT,
            PrmQsPAttribute.SAFE_DOCUMENT,
            PrmQsPAttribute.SAFE_MEASURE,
            PrmQsPAttribute.QUALITY_PRINCIPAL,
            PrmQsPAttribute.QUALITY_CONTACT,
            PrmQsPAttribute.QUALITY_DOCUMENT,
            PrmQsPAttribute.QUALITY_MEASURE,
            PrmQsPAttribute.OUTER_NO,
            PrmQsPAttribute.REMARK
    };
    String[] BUDGET_DETAIL_FIELDS = {
            PrmBudgetDetailCAttribute.BUDGET_CODE,
            PrmBudgetDetailCAttribute.CONTRACT_MONEY,
            PrmBudgetDetailCAttribute.JOINT_DESIGN_MONEY,
            PrmBudgetDetailCAttribute.COST_CONTROL_MONEY,
            PrmBudgetDetailCAttribute.VAT_AMOUNT,
            PrmBudgetDetailCAttribute.EXPLANATION,
            PrmBudgetDetailCAttribute.REMARK
    };
    String[] BUDGET_OUTSOURCE_FIELDS = {
            PrmBudgetOutsourceAttribute.SERIAL_NUMBER,
            PrmBudgetOutsourceAttribute.SUPPLIER_CODE,
            PrmBudgetOutsourceAttribute.UNIT,
            PrmBudgetOutsourceAttribute.AMOUNT,
            PrmBudgetOutsourceAttribute.PRICE,
            PrmBudgetOutsourceAttribute.TOTAL_VALUE,
            PrmBudgetOutsourceAttribute.CONTENT,
            PrmBudgetOutsourceAttribute.REMARK
    };
    String[] BUDGET_PRINCIPAL_FIELDS = {
            PrmBudgetPrincipalAttribute.SERIAL_NUMBER,
            PrmBudgetPrincipalAttribute.EQUIPMENT_NAME,
            PrmBudgetPrincipalAttribute.EQUIPMENT_MODEL,
            PrmBudgetPrincipalAttribute.FACTORY,
            PrmBudgetPrincipalAttribute.UNIT,
            PrmBudgetPrincipalAttribute.CONTRACT_AMOUNT,
            PrmBudgetPrincipalAttribute.CONTRACT_PRICE,
            PrmBudgetPrincipalAttribute.CONTRACT_TOTAL_VALUE,
            PrmBudgetPrincipalAttribute.AMOUNT,
            PrmBudgetPrincipalAttribute.SCHEMING_PRICE,
            PrmBudgetPrincipalAttribute.SCHEMING_TOTAL_VALUE,
            PrmBudgetPrincipalAttribute.REMARK
    };
    String[] BUDGET_ACCESSORY_FIELDS = {
            PrmBudgetAccessoryAttribute.SERIAL_NUMBER,
            PrmBudgetAccessoryAttribute.ACCESSORY_NAME,
            PrmBudgetAccessoryAttribute.ACCESSORY_MODEL,
            PrmBudgetAccessoryAttribute.AMOUNT,
            PrmBudgetAccessoryAttribute.PRICE,
            PrmBudgetAccessoryAttribute.TOTAL_VALUE,
            PrmBudgetAccessoryAttribute.REMARK
    };
    String[] BUDGET_RUN_FIELDS = {
            PrmBudgetRunAttribute.FINANCIAL_SUBJECT_CODE,
            PrmBudgetRunAttribute.UNIT,
            PrmBudgetRunAttribute.AMOUNT,
            PrmBudgetRunAttribute.PRICE,
            PrmBudgetRunAttribute.TOTAL_VALUE,
            PrmBudgetRunAttribute.REMARK
    };
    String[] HEADER_IGNORE_FIELDS = new String[]{
            PrmProjectMainCAttribute.STATE,
            PrmProjectMainCAttribute.DETAIL_TYPE,
            CommonAttribute.UUID,
            CommonAttribute.TBL_VERSION,
            CommonAttribute.CREATE_BY,
            CommonAttribute.CREATE_TIME,
            CommonAttribute.UPDATE_BY,
            CommonAttribute.UPDATE_TIME
    };
    String[] CONTRACT_DETAIL_FIELD = new String[]{
            PrmProjectMainCAttribute.CUSTOMER_ID,
            PrmContractAttribute.CONTRACT_NOW_MONEY
    };
    String LAST_UUID = PrmBudgetDetailCAttribute.LAST_UUID;
    String UUID = CommonAttribute.UUID;

    String[] HISTORY_STATUS = new String[]{
            PrmProjectMainAttribute.CHANGE_STATUS_CURRENT,
            PrmProjectMainAttribute.CHANGE_STATUS_MODIFIED,
            PrmProjectMainAttribute.CHANGE_STATUS_DELETE
    };

    Map<String, List<BasePojo>> loadProjectPlanHistory(String mainUuid);

    Map<String, List<BasePojo>> loadProjectBudgetHistory(String mainUuid);

    List<Map<String, Object>> getInnerProjectInfos(String prmProjectMainId);

    PrmProjectMainDto loadExtraDescField(PrmProjectMainDto projectMainDto);

    PrmProjectMainCDto loadAllDifferenceData(String prmProjectReviseUuid, String prmProjectMainUuid);

    String getPrmTaxType(String prmProjectMainId);

    List<String> getRelatedContractUuidsFromMain(String MainUuid);
}