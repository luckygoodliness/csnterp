package com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.impl;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePackage;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReq;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchaseReqAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.dto.PrmPurchaseReqDetailDto;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.dto.PrmPurchaseReqDto;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.dto.ScmContractDto;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Description:  PrmpurchasereqManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:30:53
 */

@Scope("singleton")
@Service("prmpurchasereq-manager")
public class PrmpurchasereqManagerImpl implements PrmpurchasereqManager {
    public void cleanContract(Map inMap) {
        Boolean cleanContract = (Boolean) inMap.get("cleanContract");
        if (StringUtil.isNotEmpty(cleanContract) && cleanContract == true) {
            String contractUuid = (String) inMap.get("contractUuid");
            if (StringUtil.isNotEmpty(contractUuid)) {
                ScmContractDto contract = (ScmContractDto) DtoHelper.findDtoByPK(ScmContractDto.class, contractUuid);
                if (contract != null) {
                    List lstDelete = new ArrayList();
                    lstDelete.add(contract);
                    DtoHelper.batchDel(lstDelete, ScmContractDto.class);
                }
            }
        }
    }

//    public void setExtraData(Map out) {
//        Map dtoMap = (Map) out.get("prmPurchaseReqDto");
//        if (dtoMap != null) {
//            PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, dtoMap.get("projectId") + "");
//            if (prmProjectMain != null) {
//                dtoMap.put(PrmPurchaseReqAttribute.PROJECT_NAME, prmProjectMain.getProjectName());
//            }
//            ScdpOrg scdpOrg = PersistenceFactory.getInstance().findByPK(ScdpOrg.class, dtoMap.get("DEPARTMENT_CODE") + "");
//            if (scdpOrg != null) {
//                dtoMap.put(PrmPurchaseReqAttribute.ORG_NAME, scdpOrg.getOrgName());
//            }
//        }
//    }

    public void submit(String uuid) {
        PrmPurchaseReq rs = PersistenceFactory.getInstance().findByPK(PrmPurchaseReq.class, uuid);
        if (rs != null) {
            rs.setState(PrmPurchaseReqAttribute.STATE_VALUE_1);
            PersistenceFactory.getInstance().update(rs);
        } else {
            throw new BizException("记录不存在");
        }
    }

    public void audit(String uuid) {
        List lastParam = new ArrayList();
        lastParam.add(uuid);
        String message = (PersistenceFactory.getInstance().getDataFromProcOrFunc("call PKG_SCM.SP_PURCHASE_PRM_TO_SCM(?,?)", lastParam, PersistenceCrudManager.RETURN_DATA_TYPE.VARCHAR)).toString();
//        Util.println(message);
        if ("TRUE".equals(message)) {
            String sql = "SELECT * FROM V_USER_ROLES T WHERE ROLE_NAME = '供应链部主任'";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            List<Map<String, Object>> lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            List<String> userIdLst = new ArrayList<>();
            ReceiptsMeta receiptsMeta = new ReceiptsMeta();
            if (ListUtil.isNotEmpty(lstUserInfo)) {
                for (int i = 0; i < lstUserInfo.size(); i++) {
                    String userId = (String) lstUserInfo.get(i).get("userId");
                    userIdLst.add(userId);
                }
                receiptsMeta.setLstSendToUserId(userIdLst);
            }
            String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
            String templateNo = "SCM_PURCHASER_REQ_MESSAGE";
            sql = "SELECT SPR.UUID, " +
                    "       SPR.PURCHASE_REQ_NO AS CODE, " +
                    "       SO.ORG_NAME, " +
                    "       PPM.PROJECT_NAME, " +
                    "       PPM.PROJECT_CODE, " +
                    "       SPR.TOTAL_MONEY AS MONEY " +
                    "  FROM SCM_PURCHASE_REQ SPR, SCDP_ORG SO, PRM_PROJECT_MAIN PPM " +
                    " WHERE SPR.OFFICE_ID = SO.ORG_CODE(+) " +
                    "   AND SPR.PRINCIPAL IS NULL " +
                    "   AND SPR.PRM_PROJECT_MAIN_ID = PPM.UUID(+) " +
                    "   AND SPR.PRM_PURCHASE_REQ_ID ='" + uuid + "'";
            daoMeta.setStrSql(sql);
            List<Map<String, Object>> lstScmPurchaseReq = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstScmPurchaseReq)) {
                for (int i = 0; i < lstScmPurchaseReq.size(); i++) {
                    Map map = new HashMap<>();
                    map.put("uuid", lstScmPurchaseReq.get(i).get("uuid"));
                    map.put("code", lstScmPurchaseReq.get(i).get("code"));
                    map.put("office", lstScmPurchaseReq.get(i).get("orgName"));
                    map.put("project", lstScmPurchaseReq.get(i).get("projectCode"));
                    map.put("money", lstScmPurchaseReq.get(i).get("money"));
                    MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);
                }
            }
        } else {
            throw new BizException("PKG_SCM出现异常，请联系管理员！");
        }
    }

    public void updateCdmFileRelation(Map inMap, String moduleName) {
        HashMap viewData = ((HashMap) inMap.get("viewdata"));
        HashMap reqDto = (HashMap) viewData.get("prmPurchaseReqDto");
        ArrayList dDtoList = (ArrayList) reqDto.get("prmPurchaseReqDetailDto");

        for (Object dob : dDtoList) {
            HashMap dm = (HashMap) dob;
            String dtuuid = StringUtil.replaceNull(dm.get("uuid"));

            if (dm.get("editflag").equals("-")) {
                // 删除附件信息
                deleteFileRelation(dtuuid);
            } else if (dm.containsKey("technicalDrawing")) {
                if (dm.get("technicalDrawing") != null && !dm.get("technicalDrawing").toString().equals("")) {
                    // 更新附件信息
                    String fileid = dm.get("technicalDrawing").toString();

                    // 取得SCDP_FILE_MANAGER数据
                    String sql = " SELECT FM.*, FR.UUID FR_UUID FROM SCDP_FILE_MANAGER FM " +
                            " LEFT JOIN CDM_FILE_RELATION FR ON FM.UUID = FR.FILE_ID " +
                            " WHERE FM.UUID = '" + fileid + "' ";

                    DAOMeta daoMeta = new DAOMeta();
                    daoMeta.setStrSql(sql);
                    List fileManagerList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);

                    if (fileManagerList.size() > 0) {
                        Map fmdata = (HashMap) fileManagerList.get(0);

                        String cfsql = " SELECT FR.* FROM CDM_FILE_RELATION FR " +
                                " WHERE FR.DATA_ID = '" + dm.get("uuid") + "' ";

                        daoMeta.setStrSql(cfsql);
                        List cfList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                        String fruuid = null;
//                        String createBy = null;
//                        String createTime = null;
                        if (cfList.size() > 0) {
                            fruuid = ((HashMap) cfList.get(0)).get("uuid").toString();
//                            createBy = ((HashMap) cfList.get(0)).get("createBy").toString();
//                            createTime = ((HashMap) cfList.get(0)).get("createTime").toString();
                        }

                        String dtoClass = "com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto";
                        Map viewMap = new HashMap<>();
                        Map dataMap = new HashMap<>();
                        dataMap.put("uuid", fruuid);
                        dataMap.put("dataId", dm.get("uuid"));
                        dataMap.put("fileId", fmdata.get("uuid"));
                        dataMap.put("fileType", fmdata.get("fileType"));
                        dataMap.put("module", moduleName);
                        dataMap.put("remark", fmdata.get("remark"));
                        dataMap.put("companyCode", fmdata.get("companyCode"));
                        dataMap.put("projectId", "");
                        dataMap.put("departmentCode", fmdata.get("departmentCode"));
                        dataMap.put("seqNo", "1");
                        dataMap.put("fileName", fmdata.get("fileName"));
                        dataMap.put("fileSize", fmdata.get("fileSize"));
                        viewMap.put("cdmFileRelationDto", dataMap);
//                        if (createBy != null) {
//                            viewMap.put("createBy", createBy);
//                        }
//                        if (createTime != null) {
//                            viewMap.put("createTime", createTime);
//                        }

                        BasePojo dtoObj = DtoHelper.getDtoFromMap(viewMap, dtoClass);
                        if (dataMap.get("uuid") == null) {
                            dtoObj.setEditflag("+");
                        } else {
                            dtoObj.setEditflag("*");
                            dtoObj.setIuuid(dataMap.get("uuid").toString());
                        }

                        DtoHelper.CascadeSave(dtoObj);
                    }
                } else {
                    // 删除附件信息
                    deleteFileRelation(dtuuid);
                }
            }
        }
    }

    private void deleteFileRelation(String dataId) {
        String delfSql = "DELETE FROM CDM_FILE_RELATION " +
                "WHERE DATA_ID = '" + dataId + "' ";

        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(delfSql);
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
    }


    //校验预算是否超过
    public void checkExcessBudget(Map inMap) {
        String uuid = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);

        PrmPurchaseReq prmPurchaseReq = PersistenceFactory.getInstance().findByPK(PrmPurchaseReq.class, uuid);
        Map queryMapById = new HashMap<>();
        queryMapById.put("PRM_PURCHASE_REQ_ID", uuid);
        List<PrmPurchaseReqDetail> detailList = PersistenceFactory.getInstance().findByAnyFields(PrmPurchaseReqDetail.class, queryMapById, null);

        String projectId = prmPurchaseReq.getPrmProjectMainId();
//        String reqId = uuid;

        HashMap<String, BigDecimal> sutBudgetMap = new HashMap<>();
        HashMap<String, String> pkgNameMap = new HashMap<>();

        for (PrmPurchaseReqDetail dto : detailList) {
            String pkgId = dto.getPurchasePackageId();
            if (!sutBudgetMap.containsKey(pkgId)) {
                sutBudgetMap.put(pkgId, getPackageBudget(projectId, pkgId));

                PrmPurchasePackage prmPurchasePackage = PersistenceFactory.getInstance().findByPK(PrmPurchasePackage.class, dto.getPurchasePackageId());
                String pkgName = prmPurchasePackage.getPackageName();
                pkgNameMap.put(pkgId, pkgName);
            }
            BigDecimal remainBudget = MathUtil.sub(sutBudgetMap.get(pkgId), MathUtil.mul(dto.getAmount(), dto.getExpectedPrice()));
            sutBudgetMap.put(pkgId, remainBudget);
        }

        BigDecimal curBudget = BigDecimal.ZERO;

        String msgPkgName = "";
        for (String key : sutBudgetMap.keySet()) {
            BigDecimal cb = sutBudgetMap.get(key);
            if (cb.compareTo(BigDecimal.ZERO) == -1) {
                curBudget = MathUtil.add(curBudget, cb);
                msgPkgName += "," + pkgNameMap.get(key);
            }
        }

        BigDecimal otherPackageBudget = BigDecimal.ZERO;
        String allowShare = FMCodeHelper.getDescByCode("ALLOW_SHARE_CLOSED_PACKAGE", "PRM_PURCHASE_CONFIG");
        if ("Y".equals(allowShare)) {
            otherPackageBudget = MathUtil.add(getTotalRemainBudget(projectId), getOtherPackageBudget(projectId, sutBudgetMap.keySet()));
        }

        if ((curBudget.compareTo(otherPackageBudget)) == -1) {
            String opbString = new DecimalFormat("#.00").format(MathUtil.add(curBudget, otherPackageBudget));

            throw new BizException("预算额度不足！(" + msgPkgName.substring(1) + " ,已超出预算" + opbString + "元)", new HashMap());
        }
    }

//    public void checkExcessBudget(Map inMap) {
//
//        HashMap viewData = ((HashMap) inMap.get("viewdata"));
//        HashMap reqDto = (HashMap) viewData.get("prmPurchaseReqDto");
//        ArrayList dDtoList = (ArrayList) reqDto.get("prmPurchaseReqDetailDto");
//
//        String projectId = reqDto.get("chkPrmProjectMainId").toString();
//
//        String reqId = reqDto.get("chkPrmPurchaseReqId").toString();
//
//        HashMap<String, Double> sutBudgetMap = new HashMap<>();
//        HashMap<String, String> pkgNameMap = new HashMap<>();
//        HashMap<String, Double> dtBudgetMap = new HashMap<>();
//
//        dtBudgetMap = getReqDetailBudget(reqId);
//        double curBudget = 0D;
//
//        for (Object dto : dDtoList) {
//            Map dtoMap = (Map) dto;
//
//            if (dtoMap.get("editflag").equals("-")) {
//                continue;
//            }
//
//            String pkgId = dtoMap.get("chkPackageId").toString();
//            String pkgName = dtoMap.get("chkPackageName").toString();
//            if (!sutBudgetMap.containsKey(pkgId)) {
//                curBudget = 0D;
//                if (dtBudgetMap.containsKey(pkgId)) {
//                    curBudget = dtBudgetMap.get(pkgId);
//                }
//                sutBudgetMap.put(pkgId, getPackageBudget(projectId, pkgId) + curBudget);
//                pkgNameMap.put(pkgId, pkgName);
//            }
//            double remainBudget = sutBudgetMap.get(pkgId) - (Double.parseDouble(dtoMap.get("chkAmount").toString()) * Double.parseDouble(dtoMap.get("chkBudgetPrice").toString()));
//            sutBudgetMap.put(pkgId, remainBudget);
//        }
//
//        curBudget = 0D;
//
//        String msgPkgName = "";
//        for (String key : sutBudgetMap.keySet()) {
//            double cb = sutBudgetMap.get(key);
//            if (cb < 0) {
//                curBudget += cb;
//                msgPkgName += "," + pkgNameMap.get(key);
//            }
//        }
//
//        if ((curBudget + getTotalRemainBudget(projectId) + getOtherPackageBudget(projectId, sutBudgetMap.keySet())) < 0) {
//            throw new BizException("预算额度不足！(" + msgPkgName.substring(1) + ")", new HashMap());
//        }
//    }

    // 获取本申请已保存的预算
    private HashMap<String, Double> getReqDetailBudget(String reqId) {

        DAOMeta daoMeta = new DAOMeta();
        String sql = "";
        StringBuffer selfSql = new StringBuffer();
        HashMap<String, Double> dtBudget = new HashMap<>();

        if (StringUtil.isEmpty(reqId)) {
            return dtBudget;
        }

        sql = " SELECT RD.PURCHASE_PACKAGE_ID" +
                "   ,NVL(SUM(RD.AMOUNT * RD.EXPECTED_PRICE),'0') BUDGET " +
                " FROM PRM_PURCHASE_REQ_DETAIL RD " +
                " WHERE RD.PRM_PURCHASE_REQ_ID = '" + reqId + "'" +
                " GROUP BY RD.PURCHASE_PACKAGE_ID";

        daoMeta.setStrSql(sql);
        List totalBudget = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);

        for (Object tmpBudget : totalBudget) {
            Map tmpMap = (Map) tmpBudget;
            dtBudget.put(StringUtil.replaceNull(tmpMap.get("purchasePackageId")), Double.parseDouble(tmpMap.get("budget").toString()));
        }

        return dtBudget;
    }


    //获得本包的剩余预算
    private BigDecimal getPackageBudget(String projectId, String packageId) {
        DAOMeta daoMeta = new DAOMeta();
        String sql = "";
        StringBuffer selfSql = new StringBuffer();
        BigDecimal pBudget = BigDecimal.ZERO;

        if (StringUtil.isNotEmpty(projectId)) {
            selfSql.append(" AND V.PROJECT_MAIN_ID = '" + projectId + "'");
        }
        if (StringUtil.isNotEmpty(packageId)) {
            selfSql.append(" AND V.PACKAGE_ID = '" + packageId + "'");
        }

        sql = "SELECT SUM(V.REMAIN_BUDGET) REMAIN FROM VW_PRM_BUDGET V,PRM_PURCHASE_PACKAGE PK " +
                "WHERE V.PACKAGE_ID = PK.UUID AND PK.PACKAGE_STATE = '2' " + selfSql;

        daoMeta.setStrSql(sql);
        List totalRemainBudget = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);

        if (!totalRemainBudget.isEmpty() && totalRemainBudget.size() != 0) {
            try {
                pBudget = new BigDecimal((((Map) totalRemainBudget.get(0)).get("remain")).toString());//可用额度
            } catch (Exception e) {
                pBudget = BigDecimal.ZERO;
            }
        } else {
            pBudget = BigDecimal.ZERO;
        }

        return pBudget;
    }

    //获得其他包的剩余预算
    private BigDecimal getOtherPackageBudget(String projectId, Set<String> packageIdSet) {
        DAOMeta daoMeta = new DAOMeta();
        String sql = "";
        StringBuffer selfSql = new StringBuffer();
        BigDecimal pBudget = BigDecimal.ZERO;

        if (StringUtil.isNotEmpty(projectId)) {
            selfSql.append(" AND V.PROJECT_MAIN_ID = '" + projectId + "'");
        }
        String packageId = "";
        for (String pkgId : packageIdSet) {
            packageId += ",'" + pkgId + "'";
        }
        selfSql.append(" AND V.PACKAGE_ID NOT IN (" + packageId.substring(1) + ")");

        sql = "SELECT SUM(V.REMAIN_BUDGET) REMAIN FROM VW_PRM_BUDGET V,PRM_PURCHASE_PACKAGE PK " +
                "WHERE V.PACKAGE_ID = PK.UUID AND PK.PACKAGE_STATE = '2' AND V.REMAIN_BUDGET < 0 " + selfSql;

        daoMeta.setStrSql(sql);
        List totalRemainBudget = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);

        if (!totalRemainBudget.isEmpty() && totalRemainBudget.size() != 0) {
            try {
                pBudget = new BigDecimal((((Map) totalRemainBudget.get(0)).get("remain")).toString());//可用额度
            } catch (Exception e) {
                pBudget = BigDecimal.ZERO;
            }
        } else {
            pBudget = BigDecimal.ZERO;
        }

        return pBudget;
    }

    //获得总的的剩余预算
    private BigDecimal getTotalRemainBudget(String projectId) {
        DAOMeta daoMeta = new DAOMeta();
        String sql = "";
        StringBuffer selfSql = new StringBuffer();
        BigDecimal tBudget = BigDecimal.ZERO;

        if (StringUtil.isNotEmpty(projectId)) {
            selfSql.append(" AND V.PROJECT_MAIN_ID = '" + projectId + "'");
        }

        sql = "SELECT SUM(V.REMAIN_BUDGET) REMAIN FROM VW_PRM_BUDGET V,PRM_PURCHASE_PACKAGE PK " +
                "WHERE V.PACKAGE_ID = PK.UUID AND PK.PACKAGE_STATE = '4' " + selfSql;

        daoMeta.setStrSql(sql);
        List totalRemainBudget = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (!totalRemainBudget.isEmpty() && totalRemainBudget.size() != 0) {
            try {
                tBudget = new BigDecimal((((Map) totalRemainBudget.get(0)).get("remain")).toString());//可用额度
            } catch (Exception e) {
                tBudget = BigDecimal.ZERO;
            }
        } else {
            tBudget = BigDecimal.ZERO;
        }

        return tBudget;
    }

    // 更新包状态(关闭)
    public boolean updatePackageState(String packageId) {

        DAOMeta daoMeta = new DAOMeta();
        String sql = "";
        StringBuffer selfSql = new StringBuffer();

        BigDecimal auditAmount = BigDecimal.ZERO;
        BigDecimal planAmount = BigDecimal.ZERO;

        if (StringUtil.isNotEmpty(packageId)) {
            selfSql.append(" AND RD.PURCHASE_PACKAGE_ID = '" + packageId + "'");
        }

        sql = "SELECT  " +
                " SUM(NVL(RD.HANDLE_AMOUNT,RD.AMOUNT)) AUDIT_AMOUNT " +
                " FROM  " +
                "   PRM_PURCHASE_REQ_DETAIL RD " +
                "   ,PRM_PURCHASE_REQ RH " +
                "   ,SCM_CONTRACT CH " +
                " WHERE 1=1 " +
                "   AND RH.STATE ='2' " +
                "   AND RD.PRM_PURCHASE_REQ_ID = RH.UUID " +
                "   AND RD.SCM_CONTRACT_ID = CH.UUID " +
                "   AND (RD.ISFALLBACK IS NULL OR RD.ISFALLBACK <> '1')" +
                "   AND CH.STATE = '2' " + selfSql;

        daoMeta.setStrSql(sql);

        List lstAuditAmount = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (!lstAuditAmount.isEmpty() && lstAuditAmount.size() != 0) {
            try {
                auditAmount = new BigDecimal((((Map) lstAuditAmount.get(0)).get("auditAmount")).toString());//可用额度
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }

        sql = "SELECT  " +
                "   SUM(RD.AMOUNT) PLAN_AMOUNT " +
                " FROM  " +
                "   PRM_PURCHASE_PLAN_DETAIL RD " +
                " WHERE 1=1" + selfSql;
        daoMeta.setStrSql(sql);
        List lstPlanAmount = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (!lstPlanAmount.isEmpty() && lstPlanAmount.size() != 0) {
            try {
                planAmount = new BigDecimal((((Map) lstPlanAmount.get(0)).get("planAmount")).toString());//可用额度
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }

        if (planAmount.compareTo(auditAmount) == 0) {
            PrmPurchasePackage prmPurchasePackage = PersistenceFactory.getInstance().findByPK(PrmPurchasePackage.class, packageId);
            prmPurchasePackage.setPackageState(PrmPurchaseReqAttribute.STATE_VALUE_4);
            PersistenceFactory.getInstance().update(prmPurchasePackage);
            return true;
        }

        return false;
    }

    // 删除所有附件
    public void deleteAllCdmFileRelation(Map inMap) {

        String uuid = StringUtil.replaceNull(((List) inMap.get(CommonAttribute.UUIDS)).get(0));
        DAOMeta daoMeta = new DAOMeta();
        String sql = "";

        sql = "SELECT D.UUID UUID FROM PRM_PURCHASE_REQ_DETAIL D WHERE 1=1 AND " +
                " D.TECHNICAL_DRAWING IS NOT NULL AND D.PRM_PURCHASE_REQ_ID = '" + uuid + "'";
        daoMeta.setStrSql(sql);
        List detailUuid = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);

        if (!detailUuid.isEmpty() && detailUuid.size() != 0) {
            for (Object tmp : detailUuid) {
                Map tmpUuid = (Map) tmp;
                deleteFileRelation(StringUtil.replaceNull(tmpUuid.get("uuid")));
            }
        }
    }

    public BigDecimal getTotalAmountByReqId(String purchaseReqId) {
        String sql = "select sum(nvl(t2.handle_amount,t2.amount) * t2.expected_price) as amount\n" +
                "  from prm_purchase_req_detail t2\n" +
                "   where (t2.isfallback is null or t2.isfallback != 1) " +
                " and t2.prm_purchase_req_id=? ";
        List lstParam = new ArrayList<>();
        lstParam.add(purchaseReqId);
        DAOMeta daoMeta = new DAOMeta(sql, lstParam, false);
        List<Map<String, Object>> lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstResult)) {
            BigDecimal amount = (BigDecimal) lstResult.get(0).get("amount");
            if (amount == null) {
                amount = BigDecimal.ZERO;
            }
            return amount;
        } else {
            return BigDecimal.ZERO;
        }
    }

    public void sendMsg(String uuid, String purchaseReqNo, String deptCode) {
        String sql = "select t.*\n" +
                "  from v_user_roles t\n" +
                "  left join scdp_user su\n" +
                "    on su.uuid = t.user_uuid\n" +
                "  left join scdp_org so\n" +
                "    on su.org_uuid = so.uuid\n" +
                " where t.role_name = '事业部行政主管'\n" +
                "   and so.org_code = ?";
        DAOMeta daoMeta = new DAOMeta();
        List condition = new ArrayList<>();
        condition.add(deptCode);
        daoMeta.setLstParams(condition);
        daoMeta.setStrSql(sql);
        List<Map<String, Object>> lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        List<String> userIdLst = new ArrayList<String>();
        ReceiptsMeta receiptsMeta = new ReceiptsMeta();
        if (ListUtil.isNotEmpty(lstUserInfo)) {
            for (int i = 0; i < lstUserInfo.size(); i++) {
                String userId = (String) lstUserInfo.get(i).get("userId");
                userIdLst.add(userId);
            }
            receiptsMeta.setLstSendToUserId(userIdLst);
        }
        String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
        String templateNo = "PRM_PURCHASE_REQ_INNER";
        Map map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("purchaseReqNo", purchaseReqNo);
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);

    }

    public void checkPurchase(String uuid, String checkFor) {
        String sql = "SELECT *" +
                " FROM (SELECT TD.NAME," +
                " NVL(PPPD.AMOUNT, 0) - NVL(V.LOCK_AMOUNT, 0) AS USEABLE," +
                " NVL(PPPD.AMOUNT, 0) - NVL(V.LOCK_AMOUNT, 0) -" +
                " NVL(TD.AMOUNT, 0) AS BALANCE";
        if ("0".equals(checkFor)) {
            sql = sql + " FROM PRM_PURCHASE_REQ T" +
                    " LEFT JOIN PRM_PURCHASE_REQ_DETAIL TD ON T.UUID =" +
                    " TD.PRM_PURCHASE_REQ_ID";
        } else if ("1".equals(checkFor)) {
            sql = sql + " FROM SCM_CONTRACT_CHANGE T" +
                    " LEFT JOIN (SELECT * FROM SCM_CONTRACT_CHANGE_D SCCD" +
                    " WHERE SCCD.CHANGE_STATE = '1') TD ON T.UUID =" +
                    " TD.SCM_CONTRACT_CHANGE_ID";
        }
        sql = sql + " LEFT JOIN PRM_PURCHASE_PLAN_DETAIL PPPD ON TD.PRM_PURCHASE_PLAN_DETAIL_ID = PPPD.UUID" +
                " LEFT JOIN (SELECT VL.PRM_PURCHASE_PLAN_DETAIL_ID," +
                " SUM(VL.LOCK_AMOUNT) AS LOCK_AMOUNT" +
                " FROM VW_PURCHASE_PLAN_DETAIL_LOCK VL" +
                " GROUP BY VL.PRM_PURCHASE_PLAN_DETAIL_ID) V ON V.PRM_PURCHASE_PLAN_DETAIL_ID = TD.PRM_PURCHASE_PLAN_DETAIL_ID" +
                " WHERE T.UUID = '" + uuid + "') R" +
                " WHERE R.BALANCE < 0";
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        DAOMeta daoMeta = new DAOMeta(sql);
        daoMeta.setNeedFilter(false);
        List<Map<String, Object>> lsit = pcm.findByNativeSQL(daoMeta);
        StringBuffer sb = new StringBuffer();
        if (ListUtil.isNotEmpty(lsit)) {
            for (Map m : lsit) {
                sb.append(m.get("name") + "可申请数量为" + m.get("useable") + ",");
            }
            sb.append("请调整后再提交!");
            throw new BizException(sb.toString());
        }
    }

    public String abolish(String uuid, String reason) {
        String result = "";
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmPurchaseReqDto prmPurchaseReqDto = DtoHelper.findDtoByPK(PrmPurchaseReqDto.class, uuid);
        if ("2".equals(prmPurchaseReqDto.getState()) && prmPurchaseReqDto.getIsInner() == 1) {
            String sql = "select * from prm_contract p where p.state <> 3 and p.inner_purchase_req_id = '" + uuid + "'";
            DAOMeta daoMeta = new DAOMeta(sql);
            daoMeta.setNeedFilter(false);
            List<Map<String, Object>> lsit = pcm.findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lsit)) {
                Map m = lsit.get(0);
                throw new BizException("作废失败：存在未作废合同“" + m.get("contractName") + "”！");
            } else {
                prmPurchaseReqDto.setState(PrmPurchaseReqAttribute.STATE_VALUE_3);
                List prmPurchaseReqDetailDtolist = prmPurchaseReqDto.getPrmPurchaseReqDetailDto();
                for (Object o : prmPurchaseReqDetailDtolist) {
                    PrmPurchaseReqDetailDto prmPurchaseReqDetailDto = (PrmPurchaseReqDetailDto) o;
                    prmPurchaseReqDetailDto.setIsfallback(1);
                    prmPurchaseReqDetailDto.setFallbackReason(reason);
                }
                List dl = new ArrayList<>();
                dl.add(prmPurchaseReqDto);
                DtoHelper.batchUpdate(prmPurchaseReqDetailDtolist, PrmPurchaseReqDetail.class);
                DtoHelper.batchUpdate(dl, PrmPurchaseReq.class);
                result = "作废成功！";
            }
        } else {
            throw new BizException("该功能只对已审核的内委申请开放！");
        }
        return result;
    }

    @Override
    public boolean updateBudget(Map inMap) {
        return false;
    }
}