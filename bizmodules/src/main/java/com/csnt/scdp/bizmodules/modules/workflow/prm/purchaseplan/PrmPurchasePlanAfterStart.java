package com.csnt.scdp.bizmodules.modules.workflow.prm.purchaseplan;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePackage;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePlan;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePlanDetail;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchasePackageAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchasePlanAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.services.intf.PurchaseplanManager;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.util.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_purchase_plan-after-start")
@Transactional

public class PrmPurchasePlanAfterStart implements IAction {

    @Resource(name = "purchaseplan-manager")
    private PurchaseplanManager purchaseplanManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        inMap.put(WorkFlowAttribute.SKIP_COMMON_PROCESS_AFTER_ACTION, true);
        if (BillStateAttribute.FAD_BILL_STATE_APPROVED.equals(inMap.get(PrmProjectMainAttribute.PURCHASE_PLAN_STATE))) {
            updateChangeMoney(inMap);
        } else {
            updateState(inMap);
            //移动端发送消息提醒
            ErpWorkFlowHelper.pushMsgToMobile(inMap);
        }
        return mapResult;
    }

    public void updateState(Map inMap) {
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        String dto = (String) inMap.get(WorkFlowAttribute.DTO);
        String tableName = BeanUtil.getTableNameByPojoClass(BeanUtil.getPojoClassByDto(BeanFactory.getClass(dto))).toLowerCase();
        String subMethod = (String) inMap.get("subMethod");
        Integer state = -1;
        if ("complete".equals(subMethod)) {
            String status = (String) inMap.get("status");
            if ("PROCESSING".equals(status)) {
                state = 1;//已提交
            } else if ("FIXED".equals(status)) {
                state = 2;//已审核
            }
        } else if ("reject".equals(subMethod)) {
            Boolean closed = (Boolean) inMap.get("wf_status_closed");
            if (closed != null && closed) {
                state = 5;//如果回到的是第一步，状态改成已退回
            } else {
                state = 9;//如果回到的不是第一步，状态改成已提交（退回）
            }
            List userList = (List) inMap.get(WorkFlowAttribute.WF_REJECTION_MSG_RECEIVERS_BETWEEN_CURR_AND_BACK2);
            if (ListUtil.isNotEmpty(userList)) {
                ErpWorkFlowHelper.sendMsgToRollback(inMap, userList);
            }
        } else if ("cancel".equals(subMethod)) {
            Boolean closed = (Boolean) inMap.get("wf_status_closed");
            if (closed != null && closed) {
                state = 0;//如果回到的是第一步，状态改成新增
            } else {
                state = 1;//如果回到的不是第一步，状态改成提交
            }
        } else if ("start".equals(subMethod)) {
            state = 1;//已提交
            if(!purchaseplanManager.hasPartPackage(businessKey)){
                purchaseplanManager.partPackageRun(businessKey,"commit");
            }
        }
        if (state != -1) {
            inMap.put(PrmProjectMainAttribute.PURCHASE_PLAN_STATE, state);
            String sql = "update prm_project_main set purchase_plan_state = ? where uuid = ?";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            List lstParam = new ArrayList();
            lstParam.add(state);
            lstParam.add(businessKey);
            daoMeta.setLstParams(lstParam);
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            pcm.updateByNativeSQL(daoMeta);
        }
    }

    /**
     * 待审核金额更新到采购包预算中
     *
     * @param inMap
     */
    protected void updateChangeMoney(Map inMap) {
        String purchasePlanUuid = (String) inMap.get("businessKey");
        Map conditions = new HashMap<>();
        conditions.put(PrmPurchasePlanAttribute.PRM_PROJECT_MAIN_ID, purchasePlanUuid);
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        List<PrmPurchasePackage> packageDtos = pcm.findByAnyFields
                (PrmPurchasePackage.class, conditions, null);
        List<String> lstPurchaseLevelIds = new ArrayList<>();
        for (int i = 0; i < packageDtos.size(); i++) {
            PrmPurchasePackage prmPurchasePackage = packageDtos.get(i);

            BigDecimal pendingMoney = prmPurchasePackage.getPendingMoney();
            if (pendingMoney != null) {
                //大于0
//                if (pendingMoney.compareTo(BigDecimal.ZERO) == 1) {
                //把待审核金额更新到包预算
                prmPurchasePackage.setPackageBudgetMoney(pendingMoney);
                //原先待审核金额置0
//                    prmPurchasePackage.setPendingMoney(BigDecimal.ZERO);
                if (!"1".equals(prmPurchasePackage.getPurchaseLevel())
                        && prmPurchasePackage.getPendingMoney() != null && prmPurchasePackage.getPendingMoney()
                        .compareTo(BigDecimal.valueOf(500000)) >= 0) {
                    prmPurchasePackage.setPurchaseLevel("1");
                    lstPurchaseLevelIds.add(prmPurchasePackage.getUuid());
                }
//                }
            }
            //包状态修改为已审核
            prmPurchasePackage.setPackageState("2");
            //手动保存到数据库
            pcm.update(prmPurchasePackage);
        }
        if (ListUtil.isNotEmpty(lstPurchaseLevelIds)) {
            String sql = "update prm_purchase_plan_detail set purchase_level='1' " +
                    "where purchase_package_id in (" + StringUtil.joinForSqlIn(lstPurchaseLevelIds, ",") + ")";
            DAOMeta daoMeta = new DAOMeta(sql);
            pcm.updateByNativeSQL(daoMeta);
        }
    }

    /**
     * 如果用户没有点击分包按钮，且只有运行费没有分包，则执行此方法进行运行费分包
     */
    public void updatePurchasePackage(Map inMap) {
        String purchasePlanUuid = (String) inMap.get("businessKey");
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

        if (!hasPartPackage) {
            List classCode = new ArrayList<>();
            classCode.add("0-011");
            classCode.add("0-012");
            Map classInfo = new HashMap<>();
            classInfo = purchaseplanManager.getRunMaterialInfo(classCode);

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
            prmPurchasePackage1.setPackageState("0");
            prmPurchasePackage1.setPackageBudgetMoney(BigDecimal.ZERO);
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
            prmPurchasePackage2.setPackageState("0");
            prmPurchasePackage2.setPackageBudgetMoney(BigDecimal.ZERO);
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
            pcm.batchInsert(runPackage);
            pcm.batchUpdate(prmPurchasePlanDetails);
        }


    }

    protected Map collectReturnResult(Map inMap) {
        Map mapResult = new HashMap();
        String purchasePlanUuid = (String) inMap.get("businessKey");
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmPurchasePlan plan = pcm.findByPK(PrmPurchasePlan.class, purchasePlanUuid);
        Map conditions = new HashMap();
        List<PrmPurchasePackage> packages = new ArrayList();
        if (plan != null) {
            conditions.put(PrmPurchasePackageAttribute.PRM_PROJECT_MAIN_ID, plan.getPrmProjectMainId());
        }
        if (conditions.containsKey(PrmPurchasePackageAttribute.PRM_PROJECT_MAIN_ID)) {
            packages = pcm.findByAnyFields(PrmPurchasePackage.class, conditions, null);
        }
        if (ListUtil.isNotEmpty(packages)) {
            mapResult.put("planPackages", packages);
        }
        return mapResult;
    }
}
