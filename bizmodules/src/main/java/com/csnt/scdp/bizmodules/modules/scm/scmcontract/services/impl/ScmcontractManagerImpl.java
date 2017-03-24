package com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.impl;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entity.scm.ScmContractDetail;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplierBank;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchaseReqDetailAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractDetailAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierBankAttribute;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.dto.ScmContractDetailDto;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.dto.ScmContractDto;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.bizmodules.util.UseInMap;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.ObjectUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import com.fr.stable.StringUtils;
import oracle.jdbc.driver.SQLUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Description:  ScmcontractManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:43
 */

@Scope("singleton")
@Service("scmcontract-manager")
public class ScmcontractManagerImpl implements ScmcontractManager {
    @Override
    public void setExtraData(Map out) {
        Map dtoMap = (Map) out.get("scmContractDto");
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        if (dtoMap != null) {
            String projectId = (String) dtoMap.get("projectId");
            String subjectCode = (String) dtoMap.get("subjectCode");
            String supplierId = (String) dtoMap.get("supplierCode");
            if (StringUtil.isNotEmpty(projectId)) {
                PrmProjectMain prmProjectMain = pcm.findByPK(PrmProjectMain.class, projectId);
                if (prmProjectMain != null) {
                    dtoMap.put(ScmContractAttribute.PROJECT_NAME, prmProjectMain.getProjectName());
                    dtoMap.put(ScmContractAttribute.FAD_SUBJECT_CODE, prmProjectMain.getProjectCode());
                }
            } else if (StringUtil.isNotEmpty(subjectCode)) {
                DAOMeta daoMeta = new DAOMeta();
                String sql = "SELECT N.FINANCIAL_SUBJECT FROM NON_PROJECT_SUBJECT_SUBJECT N WHERE N.FINANCIAL_SUBJECT_CODE= '" + subjectCode + "'";
                daoMeta.setStrSql(sql);
                List<Map<String, Object>> lis = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                if (ListUtil.isNotEmpty(lis)) {
                    Map m = lis.get(0);
                    dtoMap.put(ScmContractAttribute.PROJECT_NAME, m.get("financialSubject"));

                }
                dtoMap.put(ScmContractAttribute.FAD_SUBJECT_CODE, subjectCode);
            }
            String officeId = (String) dtoMap.get("officeId");
            if (StringUtil.isNotEmpty(officeId)) {
                ScdpOrg scdpOrg = pcm.findByPK(ScdpOrg.class, officeId);
                if (scdpOrg != null) {
                    dtoMap.put(ScmContractAttribute.OFFICE_ID_NAME, scdpOrg.getOrgName());
                }
            }
            if (StringUtil.isNotEmpty(supplierId)) {
                ScmSupplier scmSupplier = pcm.findByPK(ScmSupplier.class, supplierId);
                if (scmSupplier != null) {
                    String taxTypes = scmSupplier.getTaxTypes();
                    String levelTypes = scmSupplier.getLevelCode();
                    String supplierGenre = scmSupplier.getSupplierGenre();
                    if (StringUtil.isNotEmpty(taxTypes)) {
                        if ("0".equals(taxTypes)) {
                            taxTypes = "一般纳税人";
                        } else {
                            taxTypes = "小规模纳税人";
                        }
                    } else {
                        taxTypes = "未知";
                    }
//                    if (StringUtil.isNotEmpty(levelTypes)) {
//                        if ("A".equals(levelTypes) || "B".equals(levelTypes) || "C".equals(levelTypes)) {
//                            levelTypes = levelTypes + "级年度合格供方";
//                        } else if ("D".equals(levelTypes)) {
//                            levelTypes = "不合格供方";
//                        }
//                    } else {
//                        levelTypes = "普通供方";
//                    }
                    if (StringUtil.isNotEmpty(supplierGenre)) {
                        switch (supplierGenre) {
                            case "0":
                                supplierGenre = "合格供方";
                                break;
                            case "1":
                                supplierGenre = "普通供方";
                                break;
                            case "2":
                                supplierGenre = "零星供方";
                                break;
                            case "3":
                                supplierGenre = "报销供方";
                                break;
                            default:
                                supplierGenre = "其它";
                        }
                    } else {
                        supplierGenre = "其它";
                    }
                    dtoMap.put(ScmContractAttribute.TAX_TYPES, taxTypes);
                    dtoMap.put(ScmContractAttribute.LEVEL_TYPES, levelTypes);
                    dtoMap.put(ScmContractAttribute.SUPPLIER_GENRE, supplierGenre);
                }
            }
        }
        //1.设置相关合同明细中的面价总计和实价总计，面价总计=单位面价*数量，实价总计=单位实价*数量
        if (dtoMap.get("scmContractDetailDto") != null) {
            List<Map> scmContractDetailList = (List) dtoMap.get("scmContractDetailDto");
            if (ListUtil.isNotEmpty(scmContractDetailList)) {
                Map arrivedMap = new HashMap<>();
                List<String> scmContractDetailIdList = new ArrayList<String>();
                for (Map s : scmContractDetailList) {
                    scmContractDetailIdList.add(s.get("uuid").toString());

                }
                if (ListUtil.isNotEmpty(scmContractDetailIdList)) {
                    String scmContractDetailId = StringUtil.joinForSqlIn(scmContractDetailIdList, ",");
                    List paramList = new ArrayList<>();
                    paramList.add(scmContractDetailId);
                    String sql = "        SELECT SCD.UUID, NVL(SUM(PGA.AMOUNT),0) AS ARRIVED" +
                            "        FROM SCM_CONTRACT_DETAIL SCD" +
                            "        LEFT JOIN PRM_GOODS_ARRIVAL PGA ON SCD.UUID = PGA.SCM_CONTRACT_DETAIL_ID" +
                            "        WHERE SCD.UUID IN (" + scmContractDetailId + ")" +
                            "        GROUP BY SCD.UUID";
                    DAOMeta daoMeta = new DAOMeta(sql);
                    List arrivedList = pcm.findByNativeSQL(daoMeta);
                    for (Object o : arrivedList) {
                        Map m = (Map) o;
                        String uuid = "" + m.get("uuid");
                        String arrived = "" + m.get("arrived");
                        arrivedMap.put(uuid, arrived);
                    }
                }
                for (Map s : scmContractDetailList) {
                    if (s.get("amount") != null) {
                        BigDecimal amount = new BigDecimal(s.get("amount").toString());
                        if (s.get("unitPriceTalk") != null) {
                            BigDecimal unitPriceTalkValue = new BigDecimal(s.get("unitPriceTalk").toString());
                            String totalPriceTalk = String.valueOf(unitPriceTalkValue.multiply(amount));
                            s.put(ScmContractDetailAttribute.TOTAL_PRICE_TALK, totalPriceTalk);
                        }
                        if (s.get("unitPriceTrue") != null) {
                            BigDecimal unitPriceTrueValue = new BigDecimal(s.get("unitPriceTrue").toString());
                            String totalPriceTrue = String.valueOf(unitPriceTrueValue.multiply(amount));
                            s.put(ScmContractDetailAttribute.TOTAL_PRICE_TRUE, totalPriceTrue);
                        }
                    }
                    s.put("arrived", arrivedMap.get(s.get("uuid")));
                }
            }
        }
        //2.设置询价单明细中的预算合计以及意向合计(合计=单价*数量)
        if (dtoMap.get("prmPurchaseReqDetailDto") != null) {
            List<Map> prmPurchaseReqDetailList = (List) dtoMap.get("prmPurchaseReqDetailDto");
            List purchasePackageIdList = new ArrayList<>();
            List purchaseReqIdList = new ArrayList<>();
            for (Map s : prmPurchaseReqDetailList) {
                BigDecimal amount = new BigDecimal("0");
                BigDecimal budgetPrice = new BigDecimal("0");
                BigDecimal expectedPrice = new BigDecimal("0");
                String purchasePackageId = (String) s.get("purchasePackageId");
                String purchaseReqId = (String) s.get("prmPurchaseReqId");
                if (StringUtil.isNotEmpty(purchasePackageId)) {
                    purchasePackageIdList.add(purchasePackageId);
                }
                if (StringUtil.isNotEmpty(purchaseReqId)) {
                    purchaseReqIdList.add(purchaseReqId);
                }
                if (s.get("handleAmount") != null) {
                    amount = new BigDecimal(s.get("handleAmount").toString());
                }
                if (s.get("budgetPrice") != null) {
                    budgetPrice = new BigDecimal(s.get("budgetPrice").toString());
                }
                if (s.get("expectedPrice") != null) {
                    expectedPrice = new BigDecimal(s.get("expectedPrice").toString());
                }
                String totalBudgetMoney = String.valueOf(budgetPrice.multiply(amount));
                String totalExpectedPrice = String.valueOf(expectedPrice.multiply(amount));
                s.put(PrmPurchaseReqDetailAttribute.TOTAL_BUDGET_MONEY, totalBudgetMoney);
                s.put(PrmPurchaseReqDetailAttribute.TOTAL_EXPECTED_MONEY, totalExpectedPrice);
            }
            //2.设置询价单明细中的预算合计以及意向合计(合计=单价*数量)
            if (dtoMap.get("scmContractChangeDto") != null) {
                List<Map> scmContractChangeDtoList = (List) dtoMap.get("scmContractChangeDto");
                for (Map m : scmContractChangeDtoList) {
                    String closeChange = new String();
                    String state = new String();
                    if ("1".equals("" + m.get("closeChange"))) {
                        closeChange = "是";
                    } else {
                        closeChange = "否";
                    }

                    switch ("" + m.get("state")) {
                        case "0":
                            state = "新增";
                            break;
                        case "1":
                            state = "已提交";
                            break;
                        case "2":
                            state = "已审核";
                            break;
                        case "5":
                            state = "退回";
                            break;
                        case "9":
                            state = "已提交(退回)";
                            break;
                        default:
                            state = "其它";
                    }
                    m.put("closeChange", closeChange);
                    m.put("state", state);
                }

            }

            //申请单号
            DAOMeta daoMeta = new DAOMeta();
            if (ListUtil.isNotEmpty(purchaseReqIdList)) {
                String sql = "SELECT P.UUID,P.PURCHASE_REQ_NO,P.IS_PROJECT FROM PRM_PURCHASE_REQ P WHERE P.UUID IN (" + StringUtil.joinForSqlIn(purchaseReqIdList, ",") + ")";
                daoMeta.setStrSql(sql);
                List purchaseReqNoList = pcm.findByNativeSQL(daoMeta);
                for (Map s : prmPurchaseReqDetailList) {
                    String prmPurchaseReqId = (String) s.get("prmPurchaseReqId");
                    for (Object l : purchaseReqNoList) {
                        Map m = (Map) l;
                        String mId = (String) m.get("uuid");
                        if (mId.equals(prmPurchaseReqId)) {
                            s.put("purchaseReqNo", "" + m.get("purchaseReqNo"));
                            s.put("isProject", "" + m.get("isProject"));
                            break;
                        }
                    }
                }
            }


            //查找所有的分包号的包名并设置
            if (ListUtil.isNotEmpty(purchasePackageIdList)) {
                String selfCondition = " T.PURCHASE_PACKAGE_ID IN (" + StringUtil.joinForSqlIn(purchasePackageIdList, ",") + ")";
                daoMeta = DAOHelper.getDAO("scmcontract-dao", "find_package_name_by_package_id", null);
                daoMeta.setStrSql(daoMeta.getStrSql().replace("${selfconditions}", selfCondition));
                List packageNameList = pcm.findByNativeSQL(daoMeta);
                if (ListUtil.isNotEmpty(packageNameList)) {
                    for (Map s : prmPurchaseReqDetailList) {
                        String purchasePackageId = (String) s.get("purchasePackageId");
                        for (Object l : packageNameList) {
                            Map m = (Map) l;
                            String mId = (String) m.get("purchasePackageId");
                            if (mId.equals(purchasePackageId)) {
                                String packageName = (String) m.get("packageName");
                                s.put("packageName", packageName);
                                break;
                            }
                        }
                    }
                }
            }


        }
        //3.设置请款人的显示
        String debterName = "";
        String debter = (String) dtoMap.get(ScmContractAttribute.DEBTER);
        if (StringUtil.isNotEmpty(debter)) {
            Map paramMap = new HashMap<>();
            paramMap.put(ScdpUserAttribute.USER_ID, debter);
            List<ScdpUser> list = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, paramMap, null);
            if (ListUtil.isNotEmpty(list)) {
                debterName = list.get(0).getUserName();
            }
        }
        dtoMap.put(ScmContractAttribute.DEBTER_NAME, debterName);
        //设置部门
        if (StringUtil.isNotEmpty((String) dtoMap.get("debterDepartment"))) {
            dtoMap.put("debterDepartmentDesc", OrgHelper.getOrgNameByCode((String) dtoMap.get("debterDepartment")));
        }
    }

    @Override
    public ScmContract directEffect(String uuid) {
        if (StringUtil.isEmpty(uuid)) {
            throw new BizException("UUID为空！");
        }
        PersistenceCrudManager prm = PersistenceFactory.getInstance();
        ScmContract rs = prm.findByPK(ScmContract.class, uuid);
        if (rs != null) {
            rs.setContractState(ScmContractAttribute.CONTRACT_STATE_VALUE_4);
            rs.setState("2");
            rs.setEffectiveDate(new Date());
            rs.setIsClosed("1");//直接生效的合同标记为已经结算
            prm.update(rs);
        } else {
            throw new BizException("合同不存在!");
        }
        return rs;
    }

    @Override
    public void cancelEffect(String uuid) {
        if (StringUtil.isEmpty(uuid)) {
            throw new BizException("UUID为空！");
        }
        PersistenceCrudManager prm = PersistenceFactory.getInstance();
        ScmContract rs = prm.findByPK(ScmContract.class, uuid);
        if (rs != null) {
            rs.setContractState(ScmContractAttribute.CONTRACT_STATE_VALUE_1);
            prm.update(rs);
        } else {
            throw new BizException("合同不存在!");
        }
    }

    @Override
    public void submitToPersion(String uuid) {
        if (StringUtil.isEmpty(uuid)) {
            throw new BizException("UUID为空！");
        }
        PersistenceCrudManager prm = PersistenceFactory.getInstance();
        ScmContract rs = prm.findByPK(ScmContract.class, uuid);
        if (rs != null) {
            rs.setContractState(ScmContractAttribute.CONTRACT_STATE_VALUE_2);
            prm.update(rs);
        } else {
            throw new BizException("合同不存在!");
        }
    }

    @Override
    public void submitToApprove(String uuid) {
        if (StringUtil.isEmpty(uuid)) {
            throw new BizException("UUID为空！");
        }
        PersistenceCrudManager prm = PersistenceFactory.getInstance();
        //1.根据主键更新合同状态为合同审批
        ScmContract rs = prm.findByPK(ScmContract.class, uuid);
        if (rs != null) {
            rs.setContractState(ScmContractAttribute.CONTRACT_STATE_VALUE_3);
            prm.update(rs);
        } else {
            throw new BizException("合同不存在!");
        }
    }

    //采购合同审核生效
    @Override
    public ScmContract approved(String uuid) {
        if (StringUtil.isEmpty(uuid)) {
            throw new BizException("UUID为空！");
        }
        Map resultMap = new HashMap();
        String purchaseContractId = "";
        //1.生成合同号字符串以供更新使用
        String contractNature = "";
        String purchaseTypes = "";
        String contractPayType = "";
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        ScmContract rs = pcm.findByPK(ScmContract.class, uuid);
        if (rs != null) {
            contractNature = rs.getContractNature();
            purchaseTypes = rs.getPurchaseTypes();
            contractPayType = rs.getContractPayType();
            if (StringUtil.isEmpty(contractNature)) {
                throw new BizException("该记录没有填写合同性质，无法生成合同号！");
            }
            if (ScmContractAttribute.SCM_PURCHASE_TYPE_00.equals(purchaseTypes)) {//零星采购
                purchaseContractId = NumberingHelper.getNumbering(ScmContractAttribute.NUMBER_SCM_CONTRACT_NO_L, null);
                rs.setIsClosed("1");
                rs.setContractState(ScmContractAttribute.CONTRACT_STATE_VALUE_5);
            } else if (ScmContractAttribute.CONTRACT_NATURE_VALUE_0.equals(contractNature)) {//采购
                purchaseContractId = NumberingHelper.getNumbering(ScmContractAttribute.NUMBER_SCM_CONTRACT_NO_S, null);
                rs.setIsVirtual(ScmContractAttribute.IS_VIRTUAL_VALUE_0);
                rs.setContractState(ScmContractAttribute.CONTRACT_STATE_VALUE_4);
            } else if (ScmContractAttribute.CONTRACT_NATURE_VALUE_1.equals(contractNature)) {//外协
                purchaseContractId = NumberingHelper.getNumbering(ScmContractAttribute.NUMBER_SCM_CONTRACT_NO_W, null);
                rs.setIsVirtual(ScmContractAttribute.IS_VIRTUAL_VALUE_0);
                rs.setContractState(ScmContractAttribute.CONTRACT_STATE_VALUE_4);
            }
            if (ScmContractAttribute.CONTRACT_PAY_TYPE_VALUE_5.equals(contractPayType)) {//如果是商城支付直接归档
                rs.setContractState(ScmContractAttribute.CONTRACT_STATE_VALUE_5);
            }
            rs.setScmContractCode(purchaseContractId);
            rs.setEffectiveDate(new Date());
//            rs.setState("2");
//            pcm.update(rs);
            //2.把合同编号更新到对应的采购申请单明细中
            List paramList = new ArrayList<>();
            paramList.add(purchaseContractId);
            paramList.add(uuid);
            DAOMeta daoMeta = DAOHelper.getDAO("scmcontract-dao", "update_contractcode_to_prmpurchasereqdetail", paramList);
            pcm.updateByNativeSQL(daoMeta);
            sendMsg(uuid);
        } else {
            throw new BizException("合同不存在!");
        }
        return rs;
    }

    public void sendMsg(String uuid) {
        List paramList = new ArrayList<>();
        paramList.add(uuid);
        DAOMeta daoMeta = DAOHelper.getDAO("scmcontract-dao", "scm_find_msg_info", paramList);
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        List<Map> detailList = (List) pcm.findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(detailList)) {
            Map m = detailList.get(0);
            String staffId = "" + m.get("staffId");
            if (StringUtil.isNotEmpty(staffId)) {
                String purchaseReqNo = "" + m.get("purchaseReqNo");
                String scmContractCode = "" + m.get("scmContractCode");
                String amount = "" + m.get("amount");
                String advance = "" + m.get("advance");
                List<String> userIdLst = new ArrayList<String>();
                ReceiptsMeta receiptsMeta = new ReceiptsMeta();
                userIdLst.add(staffId);
                receiptsMeta.setLstSendToUserId(userIdLst);
                String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
                String templateNo = "SCM_CONTRACT_APPROVED";
                Map map = new HashMap<>();
                map.put("scmContractId", uuid);
                map.put("purchaseReqNo", purchaseReqNo);
                map.put("scmContractCode", scmContractCode);
                map.put("amount", amount);
                map.put("advance", advance);
                MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);
            }
        }

    }

    @Override
    public void contractRevocation(String uuid, String fallbackReason) {
        if (StringUtil.isEmpty(uuid)) {
            throw new BizException("UUID为空！");
        }

        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        ScmContract rs = pcm.findByPK(ScmContract.class, uuid);
        if (rs != null) {
            if (rs.getState().equals("2")) {
                DAOMeta validateDaoMeta = new DAOMeta();
                String sql = "SELECT RIC.RUNNING_NO, " +
                        "       RIC.INVOICE_REQ_NO, " +
                        "       DECODE(RIC.HEDGE_MONEY, 0, NULL, RIC.HEDGE_MONEY) AS HEDGE_MONEY, " +
                        "       P.REQ_NO " +
                        "  FROM (SELECT NVL(RI.SCM_CONTRACT_ID, C.SCM_CONTRACT_ID) AS SCM_CONTRACT_ID, " +
                        "               RI.RUNNING_NO, " +
                        "               RI.INVOICE_REQ_NO, " +
                        "               C.HEDGE_MONEY " +
                        "          FROM (SELECT NVL(R.SCM_CONTRACT_ID, I.SCM_CONTRACT_ID) AS SCM_CONTRACT_ID, " +
                        "                       R.RUNNING_NO, " +
                        "                       I.INVOICE_REQ_NO " +
                        "                  FROM (SELECT LISTAGG(FCR.RUNNING_NO, ',') WITHIN " +
                        "                         GROUP( " +
                        "                         ORDER BY FCR.UUID) AS RUNNING_NO, FCR.PURCHASE_CONTRACT_ID AS SCM_CONTRACT_ID " +
                        "                          FROM FAD_CASH_REQ FCR " +
                        "                         WHERE FCR.PURCHASE_CONTRACT_ID = " +
                        "                               '" + uuid + "' " +
                        "                           AND FCR.STATE <> 3 " +
                        "                           AND NOT EXISTS " +
                        "                         (SELECT 1 " +
                        "                                  FROM FAD_CASH_REQ_INVOICE FCRI " +
                        "                                  LEFT JOIN FAD_CASH_CLEARANCE FCC ON FCRI.FAD_INVOICE_ID = " +
                        "                                                                      FCC.UUID " +
                        "                                 WHERE FCRI.FAD_CASH_REQ_ID = FCR.UUID " +
                        "                                   AND FCRI.CLEARANCE_TYPE = 1 " +
                        "                                   AND FCC.STATE = 4 HAVING " +
                        "                                 SUM(FCRI.CLEARANCE_MONEY) = FCR.MONEY) " +
                        "                         GROUP BY PURCHASE_CONTRACT_ID) R " +
                        "                  FULL JOIN (SELECT LISTAGG(FI.INVOICE_REQ_NO, ',') WITHIN " +
                        "                             GROUP( " +
                        "                             ORDER BY FI.UUID) AS INVOICE_REQ_NO, SCI.SCM_CONTRACT_ID " +
                        "                              FROM FAD_INVOICE FI, SCM_CONTRACT_INVOICE SCI " +
                        "                             WHERE FI.UUID = SCI.FAD_INVOICE_ID " +
                        "                               AND SCI.SCM_CONTRACT_ID = " +
                        "                                   ' " + uuid + "' " +
                        "                               AND FI.STATE <> 3 " +
                        "                             GROUP BY SCI.SCM_CONTRACT_ID) I ON R.SCM_CONTRACT_ID = " +
                        "                                                                I.SCM_CONTRACT_ID) RI " +
                        "          FULL JOIN (SELECT SCM_CONTRACT_UUID AS SCM_CONTRACT_ID, " +
                        "                           SUM(T.HEDGE_MONEY) AS HEDGE_MONEY " +
                        "                      FROM PRM_RECEIPTS_SCM_INVOICE T, PRM_RECEIPTS S " +
                        "                     WHERE T.PRM_RECEIPTS_UUID = S.UUID " +
                        "                       AND T.SCM_CONTRACT_UUID = " +
                        "                           '" + uuid + "' " +
                        "                     GROUP BY T.SCM_CONTRACT_UUID) C ON RI.SCM_CONTRACT_ID = " +
                        "                                                        C.SCM_CONTRACT_ID) RIC " +
                        "  FULL JOIN (SELECT LISTAGG(DECODE(H.REQ_TYPE,0,'临时支付编号：',1,'月度支付编号：',2,'支付调整编号：','支付编号：')|| H.REQNO, ',') WITHIN " +
                        "              GROUP( " +
                        "              ORDER BY H.UUID) AS REQ_NO, SCM_CONTRACT_ID AS SCM_CONTRACT_ID " +
                        "               FROM FAD_PAY_REQ_C C, FAD_PAY_REQ_H H " +
                        "              WHERE C.STATE IN ('8', '4') " +
                        "                AND C.PUUID = H.UUID " +
                        "                AND C.ACCOUNTS_PAYABLE > 0 " +
                        "                AND SCM_CONTRACT_ID = '" + uuid + "' " +
                        "              GROUP BY C.SCM_CONTRACT_ID) P ON RIC.SCM_CONTRACT_ID = " +
                        "                                               P.SCM_CONTRACT_ID";
                validateDaoMeta.setStrSql(sql);
                List<Map> detailList = (List) PersistenceFactory.getInstance().findByNativeSQL(validateDaoMeta);
                if (ListUtil.isNotEmpty(detailList)) {
                    StringBuffer sb = new StringBuffer();
                    String runningNo = (String) detailList.get(0).get("runningNo");
                    String invoiceReqNo = (String) detailList.get(0).get("invoiceReqNo");
                    String hedgeMoney = (String) detailList.get(0).get("hedgeMoney");
                    String reqNo = (String) detailList.get(0).get("reqNo");
                    sb.append("必须先作废以下单据才可以作废合同：");
                    if (StringUtil.isNotEmpty(runningNo)) {
                        sb.append("请款单号：" + runningNo + "。");
                    }
                    if (StringUtil.isNotEmpty(invoiceReqNo)) {
                        sb.append("采购发票流水号：" + invoiceReqNo + "。");
                    }
                    if (StringUtil.isNotEmpty(reqNo)) {
                        sb.append(reqNo+"。");
                    }
                    if (StringUtil.isNotEmpty(hedgeMoney)) {
                        sb.append("该合同存在对冲数据，请先作废。");
                    }
                    throw new BizException(sb.toString());
                }
                rs.setContractState(ScmContractAttribute.CONTRACT_STATE_VALUE_7);
                rs.setState("3");
                String oRemark = "";
                if (StringUtil.isNotEmpty(fallbackReason)) {
                    if (StringUtil.isNotEmpty(rs.getRemark())) {
                        oRemark = rs.getRemark();
                    }
                    rs.setRemark(oRemark + "  合同废止原因：" + fallbackReason);
                }
                pcm.update(rs);
            } else if (rs.getState().equals("3")) {

                throw new BizException("合同已经废止！");
            } else {
                throw new BizException("已审核合同才可以废止！");
            }

        } else {
            throw new BizException("合同不存在！");
        }
        List paramList = new ArrayList<>();
        paramList.add(uuid);
        DAOMeta daoMeta = DAOHelper.getDAO("scmcontract-dao", "split_in_prmpurchaserreqdetail_scmcontract", paramList);
        pcm.updateByNativeSQL(daoMeta);
    }

    @Override
    public Map getSupplierLatestBankInfo(Map inMap) {
        Map out = new HashMap<>();
        String supplierCode = (String) inMap.get("supplierCode");
        if (StringUtil.isNotEmpty(supplierCode)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            Map paramMap = new HashMap<>();
            paramMap.put(ScmSupplierBankAttribute.SCM_SUPPLIER_ID, supplierCode);
            paramMap.put(ScmSupplierBankAttribute.IS_EFFECT, ScmSupplierBankAttribute.IS_EFFECT_VALUE_1);
            List scmSupplierBankList = pcm.findByAnyFields(ScmSupplierBank.class, paramMap, com.csnt.scdp.framework.codegenerator.attributes.CommonAttribute.SYS_COLUMN_CREATE_TIME + " DESC");
            if (ListUtil.isNotEmpty(scmSupplierBankList)) {
                String bankId = ((ScmSupplierBank) scmSupplierBankList.get(0)).getAccountNo();
                String bankName = ((ScmSupplierBank) scmSupplierBankList.get(0)).getBankName();
                out.put("bankId", bankId);
                out.put("bankName", bankName);
            }
        }
        return out;
    }

    @Override
    public void validateIfCanDirect(String uuid) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        ScmContract scmContract = pcm.findByPK(ScmContract.class, uuid);
        if (scmContract != null) {
            //1.只有零星采购才可以使用直接生效功能
            String purchaseTypes = scmContract.getPurchaseTypes();
            if (!ScmContractAttribute.PURCHASE_TYPES_VALUE_00.equals(purchaseTypes)) {
                throw new BizException("只有零星采购才可以直接生效合同！");
            }
            BigDecimal amount = new BigDecimal(0);
            int isProject = 0;
            if (scmContract.getIsProject() != null) {
                isProject = scmContract.getIsProject();
            }
            if (scmContract.getAmount() != null) {
                amount = scmContract.getAmount();
            }
            if (ScmContractAttribute.IS_PROJECT_VALUE_0 == isProject) {
                if (amount.compareTo(new BigDecimal(5000)) > 0) {
                    throw new BizException("5000元以上非项目不能使用此功能！");
                }
            } else if (ScmContractAttribute.IS_PROJECT_VALUE_1 == isProject) {
                if (amount.compareTo(new BigDecimal(20000)) > 0) {
                    throw new BizException("2万元以上项目不能使用此功能！");
                }
            }
        } else {
            throw new BizException("未找到该合同！");
        }
    }

    @Override
    public void beforeDelete(List uuids) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        for (int i = 0; i < uuids.size(); i++) {
            ScmContract scmContract = pcm.findByPK(ScmContract.class, (String) uuids.get(i));
            if (scmContract != null) {
                String scmContractState = scmContract.getContractState();
                if (scmContractState != null && !ScmContractAttribute.CONTRACT_STATE_VALUE_0.equals(scmContractState) && !ScmContractAttribute.CONTRACT_STATE_VALUE_1.equals(scmContractState) && !ScmContractAttribute.CONTRACT_STATE_VALUE_2.equals(scmContractState)) {
                    throw new BizException("状态为" + scmContractState + "的记录不能被删除", new HashMap());
                }
            }
        }
    }

    @Override
    public void checkAmount(Map inMap) {
        Map mapInput = UseInMap.getMapForAdd(inMap);
        if (ObjectUtil.isLong(mapInput.get("amount"))) {
            long amount = (long) (mapInput.get("amount"));
            List paramList = new ArrayList<>();
            paramList.add(mapInput.get("uuid"));
            DAOMeta daometa = DAOHelper.getDAO("scmcontract-dao", "query_prmpurchasereqdetaillist_by_contract", paramList);
            List<Map> prmPurchaseReqDetailList = (List) PersistenceFactory.getInstance().findByNativeSQL(daometa);
            if (ListUtil.isNotEmpty(prmPurchaseReqDetailList)) {
                BigDecimal totalBudgetPrice = new BigDecimal(0);
                Object o = prmPurchaseReqDetailList.get(0).get("sum(budgetPrice)");
                if (o != null) {
                    totalBudgetPrice = (BigDecimal) o;
                }
                if (new BigDecimal(amount).compareTo(totalBudgetPrice) > 0) {
                    throw new BizException("合同总额不能超过申请明细意向总金额(申请明细数据*意向单价)！");
                }
            }
        }
    }

    @Override
    public void beforeModify(Map inMap) {
        Map mapInput = UseInMap.getMapForAdd(inMap);
        String uuid = (String) mapInput.get(CommonAttribute.UUID);
        ScmContract scmContract = PersistenceFactory.getInstance().findByPK(ScmContract.class, uuid);
        if (scmContract != null) {
            String scmContractState = scmContract.getContractState();
            if (scmContractState != null && !ScmContractAttribute.CONTRACT_STATE_VALUE_0.equals(scmContractState) && !ScmContractAttribute.CONTRACT_STATE_VALUE_1.equals(scmContractState) && !ScmContractAttribute.CONTRACT_STATE_VALUE_2.equals(scmContractState)) {
                throw new BizException("状态为" + scmContractState + "的记录不能被修改", new HashMap());
            }
        } else {
            throw new BizException("合同不存在");
        }
    }

    @Override
    public void balance(Map inMap) {
        String uuid = (String) inMap.get("uuid");
        if (StringUtil.isNotEmpty(uuid)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            ScmContract scmContract = pcm.findByPK(ScmContract.class, uuid);
            if (scmContract != null) {
                String contractState = scmContract.getContractState();
                String state = scmContract.getState();
                if (StringUtil.isEmpty(state) || !state.equals(ScmContractAttribute.CONTRACT_STATE_VALUE_2)) {
                    throw new BizException("已审核的合同才可以结算！");
                } else {
                    scmContract.setIsClosed(ScmContractAttribute.IS_CLOSED_1);
                    pcm.update(scmContract);
                }
            } else {
                throw new BizException("未找到该合同！");
            }
        } else {
            throw new BizException("未选择记录！");
        }
    }

    /**
     * 根据UUID组，获取合同集合，Key为对应的UUID
     *
     * @param lstUuid
     * @return 合同集
     */
    @Override
    public Map<String, ScmContract> getScmContractByIds(List lstUuid) {
        Map<String, ScmContract> outMap = new HashMap();
        QueryCondition condition1 = new QueryCondition();
        condition1.setField(CommonAttribute.UUID);
        condition1.setOperator("in");
        condition1.setValueList(lstUuid);
        List<QueryCondition> lstCondition = new ArrayList<QueryCondition>();
        lstCondition.add(condition1);
        List<ScmContract> objs = PersistenceFactory.getInstance().findByAnyFields(ScmContract.class, lstCondition, null);

        if (objs != null && objs.size() > 0) {
            objs.forEach(n -> {
                outMap.put(n.getUuid(), n);
            });
        }
        return outMap;
    }

    /**
     * 根据UUIDS字符串，获取所需集合，Key为对应的UUID
     *
     * @param uuidsOrSql 格式 "'111','eee'",若uuids为空，则不加过滤
     * @return 合同集
     */
    @Override
    public Map<String, Map> getObjectsById(String uuidsOrSql, String filter) {
        Map<String, Map> outMap = new HashMap();
        DAOMeta daoMeta = new DAOMeta();
        String appendWhere = "";
        if (StringUtils.isNotBlank(uuidsOrSql)) {
            appendWhere = "  and t.uuid in (" + uuidsOrSql + ") ";
        }
        String sql = "select  t.uuid,t.scm_contract_code,t.contract_nature,t.supplier_name,t.org_name,t.project_code,t.project_name," +
                "t.amount,t.scm_paid_money,t.scm_need_pay_money,t.checked_money,t.fad_invoice_money,invoice_scm_need_check,t.supplier_black from vw_scm_contract_execute t" +
                " where 1=1 " + appendWhere + filter;
        daoMeta.setStrSql(sql);

        List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstObjInfo)) {
            lstObjInfo.forEach(n -> outMap.put(((Map) n).get("uuid").toString(), (Map) n));
        }
        return outMap;
    }

    @Override
    public Map<String, Map> getObjectsById(String uuidsOrSql) {
        return this.getObjectsById(uuidsOrSql, "");
    }

    /**
     * 合同金额拆分
     * 把合同金额根据金额比例平均拆分到采购申请明细中
     *
     * @param uuid
     * @return
     */
    @Override
    public void allotContractMoney(String uuid) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        List paramList = new ArrayList<>();
        paramList.add(uuid);
        paramList.add(uuid);
        DAOMeta daoMeta = DAOHelper.getDAO("scmcontract-dao", "allotContractMoney", paramList);
        pcm.updateByNativeSQL(daoMeta);
    }

    @Override
    public void clearingBalance(ScmContractDto scmContractDto) {
        List<ScmContractDetailDto> scmContractDetailDtoList = scmContractDto.getScmContractDetailDto();
        BigDecimal amount = scmContractDto.getAmount();//合同总额
        BigDecimal detailAmount = BigDecimal.ZERO;//明细总额
        for (ScmContractDetailDto d : scmContractDetailDtoList) {
            detailAmount = detailAmount.add(d.getAmount().multiply(d.getUnitPriceTrue()));
        }
        BigDecimal balance = amount.subtract(detailAmount);
        if (balance.compareTo(BigDecimal.ZERO) != 0) {//存在差异

            if ((balance.multiply(new BigDecimal("1000")).remainder(BigDecimal.ONE)).abs().compareTo(BigDecimal.ZERO) > 0) {//小数位超过4位
                boolean falg = false;//跳出标志，找到合适的条目跳出循环
                for (int i = scmContractDetailDtoList.size(); i > 0; i--) {//循环子表查询合适数据
                    ScmContractDetailDto scmContractDetailDto = scmContractDetailDtoList.get(i - 1);
                    BigDecimal newUnitPriceTrue = scmContractDetailDto.getUnitPriceTrue().add(balance.multiply(new BigDecimal("100")));//调整后单价

                    if (newUnitPriceTrue.compareTo(BigDecimal.ZERO) > 0) {//需要补的差额比单价低
                        if (scmContractDetailDto.getAmount().compareTo(new BigDecimal("0.01")) == 0) {//如果数量为0.01
                            scmContractDetailDto.setUnitPriceTrue(newUnitPriceTrue);
                            scmContractDetailDto.setIsRepair(1);
                            scmContractDetailDto.setRemark("系统补差价");
                            scmContractDetailDto.setEditflag("*");
                            falg = true;
                        } else if ((scmContractDetailDto.getAmount().remainder(BigDecimal.ONE)).compareTo(BigDecimal.ZERO) > 0) {//非刚性
                            scmContractDetailDto.setAmount(scmContractDetailDto.getAmount().subtract(new BigDecimal("0.01")));
                            scmContractDetailDto.setEditflag("*");
                            ScmContractDetailDto newScmContractDetailDto = (ScmContractDetailDto) BeanUtil.DeepClone(scmContractDetailDto);
                            newScmContractDetailDto.setAmount(new BigDecimal("0.01"));
                            newScmContractDetailDto.setUnitPriceTrue(newUnitPriceTrue);
                            newScmContractDetailDto.setIsRepair(1);
                            newScmContractDetailDto.setRemark("系统补差价");
                            newScmContractDetailDto.setSeqNo(scmContractDetailDtoList.size() + 1);
                            newScmContractDetailDto.setEditflag("+");
                            scmContractDetailDtoList.add(newScmContractDetailDto);
                            falg = true;
                        }
                    }
                    if (falg) break;
                }
            } else {//小数位小于4位
                boolean falg = false;//跳出标志，找到合适的条目跳出循环
                for (int i = scmContractDetailDtoList.size(); i > 0; i--) {//循环子表查询合适数据
                    ScmContractDetailDto scmContractDetailDto = scmContractDetailDtoList.get(i - 1);
                    BigDecimal newUnitPriceTrue = scmContractDetailDto.getUnitPriceTrue().add(balance);//调整后单价
                    if (newUnitPriceTrue.compareTo(BigDecimal.ZERO) > 0) {//需要补的差额比单价低
                        if (scmContractDetailDto.getAmount().compareTo(BigDecimal.ONE) == 0) {//如果数量为1
                            scmContractDetailDto.setUnitPriceTrue(newUnitPriceTrue);
                            scmContractDetailDto.setIsRepair(1);
                            scmContractDetailDto.setRemark("系统补差价");
                            scmContractDetailDto.setEditflag("*");
                            falg = true;
                        } else if (scmContractDetailDto.getAmount().compareTo(BigDecimal.ONE) > 0) {//数量大于1
                            scmContractDetailDto.setAmount(scmContractDetailDto.getAmount().subtract(BigDecimal.ONE));
                            scmContractDetailDto.setEditflag("*");
                            ScmContractDetailDto newScmContractDetailDto = (ScmContractDetailDto) BeanUtil.DeepClone(scmContractDetailDto);
                            newScmContractDetailDto.setAmount(BigDecimal.ONE);
                            newScmContractDetailDto.setUnitPriceTrue(newUnitPriceTrue);
                            newScmContractDetailDto.setIsRepair(1);
                            newScmContractDetailDto.setRemark("系统补差价");
                            newScmContractDetailDto.setSeqNo(scmContractDetailDtoList.size() + 1);
                            newScmContractDetailDto.setEditflag("+");
                            scmContractDetailDtoList.add(newScmContractDetailDto);
                            falg = true;
                        }
                    }
                    if (falg) break;
                }
            }
        }


//        DtoHelper.CascadeSave(scmContractDto);
//        throw new BizException("文件解析异常！");

    }
}