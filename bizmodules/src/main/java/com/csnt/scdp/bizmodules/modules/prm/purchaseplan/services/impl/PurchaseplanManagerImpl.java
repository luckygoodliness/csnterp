package com.csnt.scdp.bizmodules.modules.prm.purchaseplan.services.impl;

import com.csnt.scdp.bizmodules.attributes.ErpCommonAttribute;
import com.csnt.scdp.bizmodules.attributes.PrmBudgetCodes;
import com.csnt.scdp.bizmodules.entity.fad.FadInvoice;
import com.csnt.scdp.bizmodules.entity.prm.*;
import com.csnt.scdp.bizmodules.entity.scm.ScmMaterialClass;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadInvoiceAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.*;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePackageDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePackageRecordDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePlanDetailDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePlanDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.services.intf.PurchaseplanManager;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.*;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:  PurchaseplanManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 19:04:01
 */

@Scope("singleton")
@Service("purchaseplan-manager")
public class PurchaseplanManagerImpl implements PurchaseplanManager {

    @Override
    public String getDepartmentNameByCode(String code) {
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(ScdpOrgAttribute.ORG_CODE, code);
        List<ScdpOrg> lstScdpOrg = PersistenceFactory.getInstance().
                findByAnyFields(ScdpOrg.class, mapConditions, null);
        String name = null;
        if (ListUtil.isNotEmpty(lstScdpOrg)) {
            name = lstScdpOrg.get(0).getOrgName();
        } else {
            name = code;
        }
        return name;
    }

    @Override
    public String getCustmerNameByUuid(String uuid) {
        PrmCustomer cust = PersistenceFactory.getInstance().findByPK(PrmCustomer.class, uuid);
        String name = null;
        if (cust != null) {
            name = cust.getCustomerName();
        }
        return name;
    }

    @Override
    public Class getBudgetClassByType(String type) {
        Class c = null;
        if (type.equals(PrmBudgetCodes.PRINCIPAL)) {
            c = PrmBudgetPrincipal.class;
        } else if (type.equals(PrmBudgetCodes.ACCESSORY)) {
            c = PrmBudgetAccessory.class;
        } else {
            c = PrmBudgetOutsource.class;
        }
        return c;
    }

    @Override
    public PrmProjectMain findBudgetByProjectMainId(String id) {
        return PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, id);
    }

    @Override
    public void setPackageDetailRefId(Map inMap, String prmProjectMainId) {
        PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, prmProjectMainId);
        List lstExcel = (ArrayList) inMap.get("listDatas");
        if (ListUtil.isEmpty(lstExcel)) return;//为空直接返回
        //校验Excel数据开始
        List<String> lstValOut = new ArrayList<String>();//外协
        List<String> lstValPri = new ArrayList<String>();//主材
        List<String> lstValAcc = new ArrayList<String>();//辅材
        for (int i = 0; i < lstExcel.size(); i++) {
            Map dataMap = (Map) lstExcel.get(i);
            Map rowMap = (Map) dataMap.get("data");
            rowMap.put(com.csnt.scdp.framework.attributes.CommonAttribute.COMPANY_CODE, prmProjectMain.getCompanyCode());
            rowMap.put(com.csnt.scdp.framework.attributes.CommonAttribute.DEPARTMENT_CODE, prmProjectMain
                    .getContractorOffice());
            String budgetType = StringUtil.replaceNull(rowMap.get("prmBudgetType"));
            String serialNumber = StringUtil.replaceNull(rowMap.get("serialNumber"));
            if (StringUtil.isEmpty(serialNumber)) {
                throw new BizException("第 " + (i + 2) + " 行" + "序号为空，请检查Excel文件！");
            }
            if (StringUtil.isEmpty(budgetType)) {
                throw new BizException("第 " + (i + 2) + " 行" + "立项预算类别为空，请检查Excel文件！");
            }
            if ("主材".equals(budgetType)) {
                if (!lstValPri.contains(serialNumber)) {
                    lstValPri.add(serialNumber);
                } else {
                    throw new BizException("主材存在相同序号，请检查Excel文件！");
                }
            }
            if ("辅材".equals(budgetType)) {
                if (!lstValAcc.contains(serialNumber)) {
                    lstValAcc.add(serialNumber);
                } else {
                    throw new BizException("辅材存在相同序号，请检查Excel文件！");
                }
            }
            if ("外协".equals(budgetType)) {
                if (!lstValOut.contains(serialNumber)) {
                    lstValOut.add(serialNumber);
                } else {
                    throw new BizException("外协存在相同序号，请检查Excel文件！");
                }
            }
        }
        //校验Eexcel数据结束

        /*PrmPurchasePlan prmPurchasePlan = PersistenceFactory.getInstance().findByPK(PrmPurchasePlan.class, uuid);
        String prmProjectMainId = null;
        if (prmPurchasePlan != null) {
            prmProjectMainId = prmPurchasePlan.getPrmProjectMainId();
        }*/

        //外协
        DAOMeta daoOutsource = new DAOMeta();
        String sqlOutsource = "select uuid, SERIAL_NUMBER,amount from PRM_BUDGET_OUTSOURCE " +
                "where PRM_PROJECT_MAIN_ID='" + prmProjectMainId + "' order by length(SERIAL_NUMBER) desc";
        daoOutsource.setStrSql(sqlOutsource);
        daoOutsource.setNeedFilter(false);
        List lstOutsource = PersistenceFactory.getInstance().findByNativeSQL(daoOutsource);
        int lenOut = lstOutsource.size();

        //设备材料(主材)
        DAOMeta daoPrincipal = new DAOMeta();
        String sqlPrincipal = "select uuid, SERIAL_NUMBER,amount from PRM_BUDGET_PRINCIPAL " +
                "where PRM_PROJECT_MAIN_ID='" + prmProjectMainId + "' order by length(SERIAL_NUMBER) desc, SERIAL_NUMBER desc";
        daoPrincipal.setStrSql(sqlPrincipal);
        daoPrincipal.setNeedFilter(false);
        List lstPrincipal = PersistenceFactory.getInstance().findByNativeSQL(daoPrincipal);
        int lenPri = lstPrincipal.size();

        //辅助材料(辅材)
        DAOMeta daoAccessory = new DAOMeta();
        String sqlAccessory = "select uuid, SERIAL_NUMBER,amount from PRM_BUDGET_ACCESSORY " +
                "where PRM_PROJECT_MAIN_ID='" + prmProjectMainId + "' order by length(SERIAL_NUMBER) desc, SERIAL_NUMBER desc";
        daoAccessory.setStrSql(sqlAccessory);
        daoAccessory.setNeedFilter(false);
        List lstAccessory = PersistenceFactory.getInstance().findByNativeSQL(daoAccessory);
        int lenAcc = lstAccessory.size();

        for (int j = 0; j < lstExcel.size(); j++) {
            Map dataMap = (Map) lstExcel.get(j);
            Map rowMap = (Map) dataMap.get("data");
            String budgetType = StringUtil.replaceNull(rowMap.get("prmBudgetType"));
            String serialNumber = StringUtil.replaceNull(rowMap.get("serialNumber"));

            if ("主材".equals(budgetType)) {
                boolean isExitsPri = false;
                for (int i = 0; i < lenPri; i++) {
                    Map mapPri = (Map) lstPrincipal.get(i);
                    String serialNumOrig = StringUtil.replaceNull(mapPri.get("serialNumber"));
                    if (StringUtil.isNotEmpty(serialNumber)
                            //&& !serialNumber.equals(serialNumOrig)
                            && serialNumber.startsWith(serialNumOrig)
                            && handleSerial(serialNumber, serialNumOrig)) {
                        isExitsPri = true;
                        rowMap.put("prmBudgetRefId", StringUtil.replaceNull(mapPri.get("uuid")));
                        rowMap.put("originalAmount", mapPri.get(PrmBudgetPrincipalAttribute.AMOUNT));
                        break;
                    }
                }
                if (lenPri > 0 && !isExitsPri) {
                    throw new BizException("第 " + (j + 2) + " 行" + "主材原始序列号不存在，请检查！");
                }
            } else if ("辅材".equals(budgetType)) {
                boolean isExistsAcc = false;
                for (int i = 0; i < lenAcc; i++) {
                    Map mapAcc = (Map) lstAccessory.get(i);
                    String serialNumOrig = StringUtil.replaceNull(mapAcc.get("serialNumber"));
                    if (StringUtil.isNotEmpty(serialNumber)
                            //&& !serialNumber.equals(serialNumOrig)
                            && serialNumber.startsWith(serialNumOrig)
                            && handleSerial(serialNumber, serialNumOrig)) {
                        isExistsAcc = true;
                        rowMap.put("prmBudgetRefId", StringUtil.replaceNull(mapAcc.get("uuid")));
                        rowMap.put("originalAmount", mapAcc.get(PrmBudgetPrincipalAttribute.AMOUNT));
                        break;
                    }
                }
                if (lenAcc > 0 && !isExistsAcc) {
                    throw new BizException("第 " + (j + 2) + " 行" + "辅材原始序列号不存在，请检查！");
                }
            } else if ("外协".equals(budgetType)) {
                boolean isExistsOut = false;
                for (int i = 0; i < lenOut; i++) {
                    Map mapOut = (Map) lstOutsource.get(i);
                    String serialNumOrig = StringUtil.replaceNull(mapOut.get("serialNumber"));
                    if (StringUtil.isNotEmpty(serialNumber)
                            //&& !serialNumber.equals(serialNumOrig)
                            && serialNumber.startsWith(serialNumOrig)
                            && handleSerial(serialNumber, serialNumOrig)) {
                        isExistsOut = true;
                        rowMap.put("prmBudgetRefId", StringUtil.replaceNull(mapOut.get("uuid")));
                        rowMap.put("originalAmount", mapOut.get(PrmBudgetPrincipalAttribute.AMOUNT));
                        break;
                    }
                }
                if (lenOut > 0 && !isExistsOut) {
                    throw new BizException("第 " + (j + 2) + " 行" + "外协原始序列号不存在，请检查！");
                }
            }
        }
    }

    /**
     * 处理  11 ,  1的类似情况
     *
     * @param serial
     * @param serialNoOgi
     * @return
     */
    private boolean handleSerial(String serial, String serialNoOgi) {
        if (serial.equals(serialNoOgi)) {
            return true;
        }
        int len = serialNoOgi.length();
        char ch = serial.charAt(len);
        if (Character.isDigit(ch)) {
            return false;
        } else {
            return true;
        }
    }

    public List<String> queryExistedSerialNo(String projectMainId, List<String> lstSerialNo, String budgetType) {
        Map condition = new HashMap();
        condition.put(PrmPurchasePlanDetailAttribute.PRM_PROJECT_MAIN_ID, projectMainId);
        condition.put(PrmPurchasePlanDetailAttribute.PRM_BUDGET_TYPE, budgetType);
        condition.put(PrmPurchasePlanDetailAttribute.SERIAL_NUMBER, lstSerialNo);
        List<PrmPurchasePlanDetail> lstDetail = PersistenceFactory.getInstance().findByAnyFields(PrmPurchasePlanDetail
                .class, condition, "serial_number asc");
        return lstDetail.stream().map(x -> x.getSerialNumber()).collect(Collectors.toList());

    }

    public Map<String, Map<String, Map<String, Object>>> getPurchasePlanDetailInfo(String mainUuid) {
        if (StringUtil.isEmpty(mainUuid)) {
            return new HashMap();
        }
        //占用数量和金额取自采购申请和采购变更明细
        String sql = "select t1.prm_budget_ref_id,\n" +
                "       t1.prm_budget_type,\n" +
                "       t1.subject_property,\n" +
                "       pc.amount,\n" +
                "       pc.handle_amount,\n" +
                "       pc.expected_price,\n" +
                "       pc.actual_purchase_money,\n" +
                "       t3.package_no,\n" +
                "       t3.package_name\n" +
                "  from prm_purchase_plan_detail t1\n" +
                "  left join (select t2.prm_purchase_plan_detail_id,\n" +
                "                    t2.purchase_package_id,\n" +
                "                    t2.amount,\n" +
                "                    t2.handle_amount,\n" +
                "                    t2.expected_price,\n" +
                "                    t2.actual_purchase_money\n" +
                "               from prm_purchase_req_detail t2\n" +
                "              where t2.isfallback is null\n" +
                "                 or t2.isfallback = 0\n" +
                "             union all\n" +
                "             select t.prm_purchase_plan_detail_id,\n" +
                "                    t.purchase_package_id,\n" +
                "                    t.handle_amount,\n" +
                "                    t.handle_amount,\n" +
                "                    t.expected_price,\n" +
                "                    t.handle_amount * t.expected_price as actual_purchase_money\n" +
                "               from scm_contract_change c, scm_contract_change_d t\n" +
                "              where c.uuid = t.scm_contract_change_id\n" +
                "                and c.state in ('1', '9')\n" +
                "                and t.change_state = '1') pc\n" +
                "    on pc.prm_purchase_plan_detail_id = t1.uuid\n" +
                "  left join prm_purchase_package t3\n" +
                "    on pc.purchase_package_id = t3.uuid\n" +
                "   and (t3.package_state = '2')\n" +
                " where t1.prm_project_main_id=? ";
        List lstParam = new ArrayList<>();
        lstParam.add(mainUuid);
        DAOMeta daoMeta = new DAOMeta(sql, lstParam);
        daoMeta.setNeedFilter(false);
        List<Map<String, Object>> lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        Map<String, Map<String, Map<String, Object>>> returnMap = new HashMap<String, Map<String, Map<String, Object>>>();
        if (ListUtil.isNotEmpty(lstResult)) {
            Map<String, List<Map<String, Object>>> mapTypes = lstResult.stream().collect(Collectors.groupingBy(x -> (String) x.get
                    (PrmPurchasePlanDetailAttribute
                            .PRM_BUDGET_TYPE)));
            mapTypes.keySet().forEach(x -> {
                Map<String, Map<String, Object>> mapType = new HashMap<>();
                Map<String, List<Map<String, Object>>> mapItems = mapTypes.get(x).stream().collect(Collectors
                        .groupingBy(y -> (String) y.get(PrmPurchasePlanDetailAttribute.PRM_BUDGET_REF_ID)));
                mapItems.keySet().forEach(z -> {
                    Map<String, Object> mapItem = new HashMap<>();
                    BigDecimal lockedAmount = BigDecimal.ZERO;
                    BigDecimal lockedMoney = BigDecimal.ZERO;
                    for (Map<String, Object> mapReqDetail : mapItems.get(z)) {
                        BigDecimal actualAmount = (BigDecimal) (mapReqDetail.get(PrmPurchaseReqDetailAttribute.HANDLE_AMOUNT)
                                != null ? mapReqDetail.get(PrmPurchaseReqDetailAttribute.HANDLE_AMOUNT) :
                                mapReqDetail.get(PrmPurchaseReqDetailAttribute.AMOUNT));
                        //比较的实际金额取合同金额与意向金额的大值，现系统数据的合同金额不会大于意向金额，但是老数据存在合同金额大于意向金额的情况
                        //为向前兼容，取大值
                        BigDecimal contractMoney = mapReqDetail.get(PrmPurchaseReqDetailAttribute.ACTUAL_PURCHASE_MONEY)
                                != null ? (BigDecimal) mapReqDetail.get(PrmPurchaseReqDetailAttribute.ACTUAL_PURCHASE_MONEY) : BigDecimal.ZERO;

                        BigDecimal expectedMoney = MathUtil.mul(actualAmount, (BigDecimal) mapReqDetail.get(PrmPurchaseReqDetailAttribute
                                .EXPECTED_PRICE));
                        BigDecimal actualMoney = contractMoney.compareTo(expectedMoney) >= 0 ? contractMoney : expectedMoney;

                        lockedAmount = MathUtil.add(lockedAmount, actualAmount);
                        lockedMoney = MathUtil.add(lockedMoney, actualMoney);
                    }
                    mapItem.put("lockedAmount", lockedAmount);
                    mapItem.put("lockedMoney", lockedMoney);
                    mapItem.put("subjectProperty", mapItems.get(z).get(0).get("subjectProperty"));
                    mapItem.put("packageNo", mapItems.get(z).get(0).get("packageNo"));
                    mapItem.put("packageName", mapItems.get(z).get(0).get("packageName"));
                    mapType.put(z, mapItem);
                });
                returnMap.put(x, mapType);
            });
        }
        return returnMap;
    }

    @Override
    public Map getInvoiceInfoByMainId(String prmProjectMainId) {
        Map result = new HashMap<>();
        Map condition = new HashMap<>();
        condition.put(PrmProjectMainCAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainId);
        List<FadInvoice> fadInvoices = PersistenceFactory.getInstance().findByAnyFields(FadInvoice.class, condition, null);
        if (ListUtil.isNotEmpty(fadInvoices)) {
            for (FadInvoice fadInvoice : fadInvoices) {
                String subjectCode = fadInvoice.getSubjectCode();
                result.put(subjectCode, subjectCode);
            }
        }
        return result;
    }

    @Override
    public void addReviseRecord(BasePojo dtoObj) {
        PrmPurchasePlanDto prmPurchasePlanDto = (PrmPurchasePlanDto) dtoObj;
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map record = new HashMap<>();
        List<PrmPurchasePackageDto> prmPurchasePackages = prmPurchasePlanDto.getPrmPurchasePackageDto();
        if (ListUtil.isNotEmpty(prmPurchasePackages)) {
            for (PrmPurchasePackage prmPurchasePackage : prmPurchasePackages) {
                Integer packageNo = prmPurchasePackage.getPackageNo();
                record.put(packageNo, prmPurchasePackage);
            }
        }
        String uuid = prmPurchasePlanDto.getUuid();
        Map condition = new HashMap<>();
        condition.put(PrmProjectMainCAttribute.PRM_PROJECT_MAIN_ID, uuid);
        List<PrmPurchasePackage> prePrmPurchasePackages = pcm.findByAnyFields(PrmPurchasePackage.class, condition, null);
        if (ListUtil.isNotEmpty(prePrmPurchasePackages)) {
            List<PrmPurchasePackageRecord> prmPurchasePackageRecords = new ArrayList<>();
            for (PrmPurchasePackage prePrmPurchasePackage : prePrmPurchasePackages) {
                Integer packageNo = prePrmPurchasePackage.getPackageNo();
                String preMaterialClassCode = prePrmPurchasePackage.getMaterialClassCode();
                PrmPurchasePackage prmPurchasePackage = (PrmPurchasePackage) record.get(packageNo);
                String materialClassCode = null;
                if (prmPurchasePackage != null) {
                    materialClassCode = prmPurchasePackage.getMaterialClassCode();
                }
                if (StringUtil.isNotEmpty(materialClassCode)) {
                    if (!materialClassCode.equals(preMaterialClassCode)) {
                        PrmPurchasePackageRecord prmPurchasePackageRecord = new PrmPurchasePackageRecord();
                        BeanUtil.bean2Bean(prmPurchasePackage, prmPurchasePackageRecord);
                        prmPurchasePackageRecord.setRevisedBy(UserHelper.getUserId());
                        prmPurchasePackageRecord.setRevisedTime(new Date());
                        prmPurchasePackageRecord.setPrmPurchasePackageId(prePrmPurchasePackage.getUuid());
                        prmPurchasePackageRecord.setCreateBy(UserHelper.getUserId());
                        prmPurchasePackageRecord.setCreateTime(new Date());
                        prmPurchasePackageRecord.setUpdateBy(null);
                        prmPurchasePackageRecord.setUpdateTime(null);
                        prmPurchasePackageRecords.add(prmPurchasePackageRecord);
                    }
                }
            }
            pcm.batchInsert(prmPurchasePackageRecords);
        }
    }

    public Map getRunMaterialInfo(List classCode) {
        Map result = new HashMap<>();
        QueryCondition condition = new QueryCondition();
        condition.setField("code");
        condition.setOperator("in");
        condition.setValueList(classCode);
        List<QueryCondition> queryConditions = new ArrayList<>();
        queryConditions.add(condition);
        List<ScmMaterialClass> scmMaterialClasss = PersistenceFactory.getInstance().findByAnyFields(ScmMaterialClass.class, queryConditions, null);
        if (ListUtil.isNotEmpty(scmMaterialClasss)) {
            for (ScmMaterialClass scmMaterialClass : scmMaterialClasss) {
                if (scmMaterialClass.getCode().equals("0-011")) {
                    result.put("onePurchaseLevel", scmMaterialClass.getClassLevel());
                    String onePurchaseLevelDesc = FMCodeHelper.getDescByCode(scmMaterialClass.getClassLevel(), "SCM_MATERIAL_CLASS_LEVEL");
                    result.put("onePurchaseLevelDesc", onePurchaseLevelDesc);
                    result.put("oneSubjectProperty", scmMaterialClass.getIsRigid());
                    String oneSubjectPropertyDesc = FMCodeHelper.getDescByCode(scmMaterialClass.getIsRigid(), "SCM_DEFAULTPROPERTY");
                    result.put("oneSubjectPropertyDesc", oneSubjectPropertyDesc);
                    result.put("oneName", scmMaterialClass.getName());
                } else if (scmMaterialClass.getCode().equals("0-012")) {
                    result.put("twoPurchaseLevel", scmMaterialClass.getClassLevel());
                    String twoPurchaseLevelDesc = FMCodeHelper.getDescByCode(scmMaterialClass.getClassLevel(), "SCM_MATERIAL_CLASS_LEVEL");
                    result.put("twoPurchaseLevelDesc", twoPurchaseLevelDesc);
                    result.put("twoSubjectProperty", scmMaterialClass.getIsRigid());
                    String twoSubjectPropertyDesc = FMCodeHelper.getDescByCode(scmMaterialClass.getIsRigid(), "SCM_DEFAULTPROPERTY");
                    result.put("twoSubjectPropertyDesc", twoSubjectPropertyDesc);
                    result.put("twoName", scmMaterialClass.getName());
                }
            }
        }
        return result;
    }

    @Override
    public String addPurchaseDetailToPackage(PrmPurchasePlanDetail prmPurchasePlanDetail, PrmPurchasePackage prmPurchasePackage) {

        Map result = new HashMap<>();
        BigDecimal purchaseBudgetMoney = BigDecimal.ZERO;
        if (prmPurchasePlanDetail != null) {
            purchaseBudgetMoney = prmPurchasePlanDetail.getPurchaseBudgetMoney();
        }

        Integer newPackageNo = null;
        BigDecimal newpackageBudgetMoney = BigDecimal.ZERO;
        BigDecimal newPendingMoney = BigDecimal.ZERO;
        String newpurchaseLevel = null;
        String newsubjectProperty = null;
        if (prmPurchasePackage != null) {
            newPackageNo = prmPurchasePackage.getPackageNo();
            newpackageBudgetMoney = prmPurchasePackage.getPackageBudgetMoney() == null ? BigDecimal.ZERO : prmPurchasePackage.getPackageBudgetMoney();
            newpurchaseLevel = prmPurchasePackage.getPurchaseLevel();
            newsubjectProperty = prmPurchasePackage.getSubjectProperty();
            newPendingMoney = prmPurchasePackage.getPendingMoney() == null ? BigDecimal.ZERO : prmPurchasePackage.getPendingMoney();
        }

        StringBuffer errorMsg = new StringBuffer();
        if (newPackageNo == null) {
            throw new BizException("调入包号不能为空");
        }

        if (purchaseBudgetMoney == null) {
            errorMsg.append("采购预算不能为空" + ErpCommonAttribute.BR);
        }
        if (StringUtil.isEmpty(newpurchaseLevel)) {
            errorMsg.append("包号" + newPackageNo + ":" + "包采购级别不能为空" + ErpCommonAttribute.BR);
        }
        if (StringUtil.isEmpty(newsubjectProperty)) {
            errorMsg.append("包号" + newPackageNo + ":" + "包科目属性不能为空" + ErpCommonAttribute.BR);
        }

        if (errorMsg != null && StringUtil.isNotEmpty(errorMsg.toString())) {
            return errorMsg.toString();
        } else {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            //更新调入包
            BigDecimal newpackageBudgetMoney1 = MathUtil.add(newpackageBudgetMoney, purchaseBudgetMoney);
            prmPurchasePackage.setPackageBudgetMoney(newpackageBudgetMoney1);
            BigDecimal newPendingMoney1 = MathUtil.add(newPendingMoney, purchaseBudgetMoney);
            prmPurchasePackage.setPendingMoney(newPendingMoney1);

            //更新明细
            prmPurchasePlanDetail.setPurchaseLevel(newpurchaseLevel);
            prmPurchasePlanDetail.setSubjectProperty(newsubjectProperty);
            prmPurchasePlanDetail.setSubPackageNo(new BigDecimal(String.valueOf(newPackageNo)));
            prmPurchasePlanDetail.setPurchasePackageId(prmPurchasePackage.getUuid());
            pcm.update(prmPurchasePackage);
            pcm.update(prmPurchasePlanDetail);
            return "";
        }
    }

    @Override
    public String subPurchaseDetailFromPackage(PrmPurchasePlanDetail prmPurchasePlanDetail, PrmPurchasePackage prmPurchasePackage) {

        BigDecimal oldPackageBudgetMoney = BigDecimal.ZERO;
        Integer oldPackageNo = null;
        BigDecimal oldPendingMoney = BigDecimal.ZERO;
        if (prmPurchasePackage != null) {
            oldPackageBudgetMoney = prmPurchasePackage.getPackageBudgetMoney() == null ? BigDecimal.ZERO : prmPurchasePackage.getPackageBudgetMoney();
            oldPackageNo = prmPurchasePackage.getPackageNo();
            oldPendingMoney = prmPurchasePackage.getPendingMoney() == null ? BigDecimal.ZERO : prmPurchasePackage.getPendingMoney();
        }

        BigDecimal purchaseBudgetMoney = BigDecimal.ZERO;
        if (prmPurchasePlanDetail != null) {
            purchaseBudgetMoney = prmPurchasePlanDetail.getPurchaseBudgetMoney();
        }

        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        if (oldPackageNo != null) {
            //更新调出包
            BigDecimal oldPackageBudgetMoney1 = MathUtil.sub(oldPackageBudgetMoney, purchaseBudgetMoney);
            prmPurchasePackage.setPackageBudgetMoney(oldPackageBudgetMoney1);
            BigDecimal oldPendingMoney1 = MathUtil.sub(oldPendingMoney, purchaseBudgetMoney);
            prmPurchasePackage.setPendingMoney(oldPendingMoney1);
        }
        //更新明细
        prmPurchasePlanDetail.setPurchaseLevel(null);
        prmPurchasePlanDetail.setSubjectProperty(null);
        prmPurchasePlanDetail.setSubPackageNo(null);
        prmPurchasePlanDetail.setPurchasePackageId(null);

        pcm.update(prmPurchasePackage);
        pcm.update(prmPurchasePlanDetail);

        return null;

    }

    @Override
    public void isApplyForReimb(PrmPurchasePlanDto prmPurchasePlanDto) {

        Map fadInvoiceInfo = new HashMap<>();
        fadInvoiceInfo.putAll(getInvoiceInfoByMainId(prmPurchasePlanDto.getUuid()));
        List<PrmPurchasePlanDetailDto> prmPurchasePlanRunDetails = prmPurchasePlanDto.getPrmPurchasePlanDetailDto().stream().filter(t -> "RUN".equals(t.getPrmBudgetType())).collect(Collectors.toList());

        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map mapCondition = new HashMap();
        mapCondition.put(PrmBudgetPrincipalAttribute.PRM_PROJECT_MAIN_ID, prmPurchasePlanDto.getUuid());
        List<PrmBudgetRun> lstBudgetRun = pcm.findByAnyFields(PrmBudgetRun.class, mapCondition, null);


        StringBuffer errorMsg = new StringBuffer();
        prmPurchasePlanRunDetails.forEach(x -> {
            PrmBudgetRun budget = lstBudgetRun.parallelStream().filter(y -> y.getUuid
                    ().equals(x.getPrmBudgetRefId())).findAny().orElse(null);
            if (budget != null) {
                if (fadInvoiceInfo.get(budget.getFinancialSubjectCode()) != null) {
                    errorMsg.append("运行费" + x.getName() + "已进行日常报销，不能加入包内;");
                }
            }
        });
        if (errorMsg != null && errorMsg.length() > 0) {
            throw new BizException(errorMsg.toString());
        }
    }


    @Override
    public boolean hasPartPackage(String purchasePlanUuid) {
        Map conditions = new HashMap<>();
        conditions.put(PrmPurchasePlanAttribute.PRM_PROJECT_MAIN_ID, purchasePlanUuid);
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        List<PrmPurchasePlanDetail> details = pcm.findByAnyFields
                (PrmPurchasePlanDetail.class, conditions, null);
        boolean hasPartPackage = false;//运行费是否已经存在包
        List<String> lstPurchaseLevelIds = new ArrayList<>();
        if (ListUtil.isNotEmpty(details)) {
            for (PrmPurchasePlanDetail prmPurchasePlanDetail : details) {
                if (!"RUN".equals(prmPurchasePlanDetail.getPrmBudgetType()) && prmPurchasePlanDetail.getSubPackageNo() == null) {
                    throw new BizException("存在没有分包的非运行费，请进行分包");
                } else if ("RUN".equals(prmPurchasePlanDetail.getPrmBudgetType()) && prmPurchasePlanDetail.getSubPackageNo() != null) {
                    hasPartPackage = true;
                }
            }
        }
        return hasPartPackage;
    }

    /*
     * @param type
     *
     * 类型代表是工作流提交时或者是项目变更同步老项目运行费时
     * 增加两个运行费的包
     **/
    @Override
    public void partPackageRun(String purchasePlanUuid, String type) {
        List classCode = new ArrayList<>();
        classCode.add("0-011");
        classCode.add("0-012");
        Map classInfo = new HashMap<>();
        classInfo = getRunMaterialInfo(classCode);

        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        //查找最大序号和包号
        Map condition = new HashMap<>();
        condition.put("prmProjectMainId", purchasePlanUuid);
        List<PrmPurchasePackage> prmPurchasePackages = pcm.findByAnyFields(PrmPurchasePackage.class, condition, null);
        Integer maxPackageNo = 0;
        Integer maxSeqNo = 0;
        if (ListUtil.isNotEmpty(prmPurchasePackages)) {
            for (PrmPurchasePackage prmPurchasePackage : prmPurchasePackages) {
                Integer packageNo = prmPurchasePackage.getPackageNo();
                Integer seqNo = prmPurchasePackage.getSeqNo();
                if (maxPackageNo.compareTo(packageNo) < 0) {
                    maxPackageNo = packageNo;
                }
                if (seqNo != null && maxSeqNo.compareTo(seqNo) < 0) {
                    maxSeqNo = seqNo;
                }
            }
        }


        List<PrmPurchasePackage> runPackage = new ArrayList<>();
        PrmPurchasePackage prmPurchasePackage1 = new PrmPurchasePackage();
        String uuid1 = UUIDUtil.getUUID();
        prmPurchasePackage1.setUuid(uuid1);
        prmPurchasePackage1.setMaterialClassCode("0-011");
        prmPurchasePackage1.setPurchaseLevel((String) classInfo.get("onePurchaseLevel"));
        prmPurchasePackage1.setSubjectProperty((String) classInfo.get("oneSubjectProperty"));
        prmPurchasePackage1.setPrmProjectMainId(purchasePlanUuid);
        prmPurchasePackage1.setPackageNo(++maxPackageNo);
        prmPurchasePackage1.setSeqNo(++maxSeqNo);
        prmPurchasePackage1.setPackageName("运行一级");
        runPackage.add(prmPurchasePackage1);

        PrmPurchasePackage prmPurchasePackage2 = new PrmPurchasePackage();
        String uuid2 = UUIDUtil.getUUID();
        prmPurchasePackage2.setUuid(uuid2);
        prmPurchasePackage2.setMaterialClassCode("0-012");
        prmPurchasePackage2.setPurchaseLevel((String) classInfo.get("twoPurchaseLevel"));
        prmPurchasePackage2.setSubjectProperty((String) classInfo.get("twoSubjectProperty"));
        prmPurchasePackage2.setPrmProjectMainId(purchasePlanUuid);
//        prmPurchasePackage2.setPackageState("0");
//        prmPurchasePackage2.setPackageBudgetMoney(BigDecimal.ZERO);
        prmPurchasePackage2.setPackageNo(++maxPackageNo);
        prmPurchasePackage2.setSeqNo(++maxSeqNo);
        prmPurchasePackage2.setPackageName("运行二级");
        runPackage.add(prmPurchasePackage2);

        //查找pendingMoney
        BigDecimal runPendingMoney1 = BigDecimal.ZERO;
        BigDecimal runPendingMoney2 = BigDecimal.ZERO;
        List<PrmPurchasePlanDetail> prmPurchasePlanDetails = pcm.findByAnyFields(PrmPurchasePlanDetail.class, condition, null);
        if (ListUtil.isNotEmpty(prmPurchasePlanDetails)) {
            for (PrmPurchasePlanDetail prmPurchasePlanDetail : prmPurchasePlanDetails) {
                String name = prmPurchasePlanDetail.getName();
                BigDecimal purchaseBudgetMoney = prmPurchasePlanDetail.getPurchaseBudgetMoney();
                if (StringUtil.isNotEmpty(name)) {
                    if (name.equals("工程保险费")) {
                        if (!MathUtil.isNullOrZero(purchaseBudgetMoney)) {
                            runPendingMoney1 = runPendingMoney1.add(purchaseBudgetMoney);
                        }
                        prmPurchasePlanDetail.setPurchasePackageId(uuid1);
                        prmPurchasePlanDetail.setPurchaseLevel(prmPurchasePackage1.getPurchaseLevel());
                        prmPurchasePlanDetail.setSubjectProperty(prmPurchasePackage1.getSubjectProperty());
                        prmPurchasePlanDetail.setSubPackageNo(new BigDecimal(prmPurchasePackage1.getPackageNo()));
                    } else if (name.equals("项目房租") || name.equals("租车费")) {
                        if (!MathUtil.isNullOrZero(purchaseBudgetMoney)) {
                            runPendingMoney2 = runPendingMoney2.add(purchaseBudgetMoney);
                        }
                        prmPurchasePlanDetail.setPurchasePackageId(uuid2);
                        prmPurchasePlanDetail.setPurchaseLevel(prmPurchasePackage2.getPurchaseLevel());
                        prmPurchasePlanDetail.setSubjectProperty(prmPurchasePackage2.getSubjectProperty());
                        prmPurchasePlanDetail.setSubPackageNo(new BigDecimal(prmPurchasePackage2.getPackageNo()));
                    }

                }
            }
        }
        prmPurchasePackage1.setPendingMoney(runPendingMoney1);
        prmPurchasePackage2.setPendingMoney(runPendingMoney2);
        if ("commit".equals(type)) {
            prmPurchasePackage1.setPackageState("0");
            prmPurchasePackage1.setPackageBudgetMoney(BigDecimal.ZERO);
            prmPurchasePackage2.setPackageState("0");
            prmPurchasePackage2.setPackageBudgetMoney(BigDecimal.ZERO);
        } else if ("reverse".equals(type)) {
            prmPurchasePackage1.setPackageState("2");
            prmPurchasePackage1.setPackageBudgetMoney(runPendingMoney1);
            prmPurchasePackage2.setPackageState("2");
            prmPurchasePackage2.setPackageBudgetMoney(runPendingMoney2);
        }
        pcm.batchInsert(runPackage);
        pcm.batchUpdate(prmPurchasePlanDetails);
    }

    /*
     * @param type
     *
     * 类型代表是工作流提交时或者是项目变更同步老项目运行费时
     * 增加两个运行费的包
     **/
    @Override
    public void partPackageRun(String purchasePlanUuid, List hadReimb) {
        List classCode = new ArrayList<>();
        classCode.add("0-011");
        classCode.add("0-012");
        Map classInfo = new HashMap<>();
        classInfo = getRunMaterialInfo(classCode);

        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        //查找最大序号和包号
        Map condition = new HashMap<>();
        condition.put("prmProjectMainId", purchasePlanUuid);
        List<PrmPurchasePackage> prmPurchasePackages = pcm.findByAnyFields(PrmPurchasePackage.class, condition, null);
        Integer maxPackageNo = 0;
        Integer maxSeqNo = 0;
        if (ListUtil.isNotEmpty(prmPurchasePackages)) {
            for (PrmPurchasePackage prmPurchasePackage : prmPurchasePackages) {
                Integer packageNo = prmPurchasePackage.getPackageNo();
                Integer seqNo = prmPurchasePackage.getSeqNo();
                if (maxPackageNo.compareTo(packageNo) < 0) {
                    maxPackageNo = packageNo;
                }
                if (seqNo != null && maxSeqNo.compareTo(seqNo) < 0) {
                    maxSeqNo = seqNo;
                }
            }
        }


        List<PrmPurchasePackage> runPackage = new ArrayList<>();
        PrmPurchasePackage prmPurchasePackage1 = new PrmPurchasePackage();
        String uuid1 = UUIDUtil.getUUID();
        prmPurchasePackage1.setUuid(uuid1);
        prmPurchasePackage1.setMaterialClassCode("0-011");
        prmPurchasePackage1.setPurchaseLevel((String) classInfo.get("onePurchaseLevel"));
        prmPurchasePackage1.setSubjectProperty((String) classInfo.get("oneSubjectProperty"));
        prmPurchasePackage1.setPrmProjectMainId(purchasePlanUuid);
        prmPurchasePackage1.setPackageNo(++maxPackageNo);
        prmPurchasePackage1.setSeqNo(++maxSeqNo);
        prmPurchasePackage1.setPackageName("运行一级");
        runPackage.add(prmPurchasePackage1);

        PrmPurchasePackage prmPurchasePackage2 = new PrmPurchasePackage();
        String uuid2 = UUIDUtil.getUUID();
        prmPurchasePackage2.setUuid(uuid2);
        prmPurchasePackage2.setMaterialClassCode("0-012");
        prmPurchasePackage2.setPurchaseLevel((String) classInfo.get("twoPurchaseLevel"));
        prmPurchasePackage2.setSubjectProperty((String) classInfo.get("twoSubjectProperty"));
        prmPurchasePackage2.setPrmProjectMainId(purchasePlanUuid);
//        prmPurchasePackage2.setPackageState("0");
//        prmPurchasePackage2.setPackageBudgetMoney(BigDecimal.ZERO);
        prmPurchasePackage2.setPackageNo(++maxPackageNo);
        prmPurchasePackage2.setSeqNo(++maxSeqNo);
        prmPurchasePackage2.setPackageName("运行二级");
        runPackage.add(prmPurchasePackage2);

        //查找pendingMoney
        BigDecimal runPendingMoney1 = BigDecimal.ZERO;
        BigDecimal runPendingMoney2 = BigDecimal.ZERO;
        List<PrmPurchasePlanDetail> prmPurchasePlanDetails = pcm.findByAnyFields(PrmPurchasePlanDetail.class, condition, null);
        if (ListUtil.isNotEmpty(prmPurchasePlanDetails)) {
            for (PrmPurchasePlanDetail prmPurchasePlanDetail : prmPurchasePlanDetails) {
                String name = prmPurchasePlanDetail.getName();
                BigDecimal purchaseBudgetMoney = prmPurchasePlanDetail.getPurchaseBudgetMoney();
                if (StringUtil.isNotEmpty(name)) {
                    if (name.equals("工程保险费") && !hadReimb.contains("ENGINEERING_INSURANCE")) {
                        if (!MathUtil.isNullOrZero(purchaseBudgetMoney)) {
                            runPendingMoney1 = runPendingMoney1.add(purchaseBudgetMoney);
                        }
                        prmPurchasePlanDetail.setPurchasePackageId(uuid1);
                        prmPurchasePlanDetail.setPurchaseLevel(prmPurchasePackage1.getPurchaseLevel());
                        prmPurchasePlanDetail.setSubjectProperty(prmPurchasePackage1.getSubjectProperty());
                        prmPurchasePlanDetail.setSubPackageNo(new BigDecimal(prmPurchasePackage1.getPackageNo()));
                    } else if ((name.equals("项目房租") && !hadReimb.contains("HOUSE_RENT")) || (name.equals("租车费") && !hadReimb.contains("HIRE_CAR"))) {
                        if (!MathUtil.isNullOrZero(purchaseBudgetMoney)) {
                            runPendingMoney2 = runPendingMoney2.add(purchaseBudgetMoney);
                        }
                        prmPurchasePlanDetail.setPurchasePackageId(uuid2);
                        prmPurchasePlanDetail.setPurchaseLevel(prmPurchasePackage2.getPurchaseLevel());
                        prmPurchasePlanDetail.setSubjectProperty(prmPurchasePackage2.getSubjectProperty());
                        prmPurchasePlanDetail.setSubPackageNo(new BigDecimal(prmPurchasePackage2.getPackageNo()));
                    }

                }
            }
        }
        prmPurchasePackage1.setPendingMoney(runPendingMoney1);
        prmPurchasePackage2.setPendingMoney(runPendingMoney2);
        prmPurchasePackage1.setPackageState("2");
        prmPurchasePackage1.setPackageBudgetMoney(runPendingMoney1);
        prmPurchasePackage2.setPackageState("2");
        prmPurchasePackage2.setPackageBudgetMoney(runPendingMoney2);

        pcm.batchInsert(runPackage);
        pcm.batchUpdate(prmPurchasePlanDetails);
    }


}


