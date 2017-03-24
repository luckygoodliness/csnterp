package com.csnt.scdp.bizmodules.modules.nonprm.monitor.services.impl;

import com.csnt.scdp.bizmodules.entity.nonprm.MonitorBase;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectExtraItem;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncome;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncomeIn;
import com.csnt.scdp.bizmodules.modules.nonprm.income.services.intf.IncomeManager;
import com.csnt.scdp.bizmodules.modules.nonprm.monitor.services.intf.MonitorManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.framework.util.UUIDUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:  MonitorManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-27 15:12:59
 */

@Scope("singleton")
@Service("monitor-manager")
public class MonitorManagerImpl implements MonitorManager {

    @Resource(name = "income-manager")
    private IncomeManager incomeManager;

    @Override
    public String isNullReturnEmpty(Object value) {
        if (value == null || value.equals("null")) {
            return "";
        } else {
            return value.toString().trim();
        }
    }

    @Override
    public String isNullReturnEmpty(Integer value) {
        if (value == null) {
            return "";
        } else {
            return value.toString().trim();
        }
    }

    @Override
    public void monitorBaseCheckTimeStamp(String officeId, String tblVersion) {
        try {
            if (isNullReturnEmpty(officeId).length() > 0 && isNullReturnEmpty(tblVersion).length() > 0) {
                String sql = "UPDATE MONITOR_BASE SET TBL_VERSION = '" + UUIDUtil.getUUID() + "' WHERE OFFICE_ID = '" + officeId + "' AND TBL_VERSION = '" + tblVersion + "'";
                DAOMeta daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (!(PersistenceFactory.getInstance().updateByNativeSQL(daoMeta) > 0)) {
                    throw new BizException("费用科目项时间戳验证:费用科目项过期(已被其他用户修改过),操作无效,请刷新后重新尝试!");
                }
            } else {
                throw new BizException("费用科目项时间戳验证:初始条件设定不足!");
            }
        } catch (Exception e) {
            throw new BizException("费用科目项时间戳异常:占用超时!");
        }
    }

    @Override
    public void monitorLaborCostCheckTimeStamp(String monitorLaborCostUuid, String tblVersion) {
        try {
            if (isNullReturnEmpty(monitorLaborCostUuid).length() > 0 && isNullReturnEmpty(tblVersion).length() > 0) {
                String sql = "UPDATE MONITOR_LABOR_COST SET TBL_VERSION = '" + UUIDUtil.getUUID() + "' WHERE UUID = '" + monitorLaborCostUuid + "' AND TBL_VERSION = '" + tblVersion + "'";
                DAOMeta daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (!(PersistenceFactory.getInstance().updateByNativeSQL(daoMeta) > 0)) {
                    throw new BizException("费用科目项时间戳验证:费用科目项过期(已被其他用户修改过),操作无效,请刷新后重新尝试!");
                }
            } else {
                throw new BizException("费用科目项时间戳验证:初始条件设定不足!");
            }
        } catch (Exception e) {
            throw new BizException("费用科目项时间戳异常:占用超时!");
        }
    }

    @Override
    public void monitorOtherShareCheckTimeStamp(String monitorOtherShareUuid, String tblVersion) {
        try {
            if (isNullReturnEmpty(monitorOtherShareUuid).length() > 0 && isNullReturnEmpty(tblVersion).length() > 0) {
                String sql = "UPDATE MONITOR_OTHER_SHARE SET TBL_VERSION = '" + UUIDUtil.getUUID() + "' WHERE UUID = '" + monitorOtherShareUuid + "' AND TBL_VERSION = '" + tblVersion + "'";
                DAOMeta daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                if (!(PersistenceFactory.getInstance().updateByNativeSQL(daoMeta) > 0)) {
                    throw new BizException("费用科目项时间戳验证:费用科目项过期(已被其他用户修改过),操作无效,请刷新后重新尝试!");
                }
            } else {
                throw new BizException("费用科目项时间戳验证:初始条件设定不足!");
            }
        } catch (Exception e) {
            throw new BizException("费用科目项时间戳异常:占用超时!");
        }
    }

    @Override
    public List getMonitorLaborCost(Map inMap) {
        String year = StringUtil.replaceNull(inMap.get("year"));
        String month = StringUtil.replaceNull(inMap.get("month"));
        String officeId = StringUtil.replaceNull(inMap.get("officeId"));
        List lstParam = new ArrayList();

        lstParam.add(year);
        lstParam.add(month);

        lstParam.add(officeId);

        lstParam.add(year);
        lstParam.add(month);

        lstParam.add(year);
        lstParam.add(month);

        DAOMeta daoMeta = DAOHelper.getDAO("monitor-dao", "query_monitor_labor_cost", lstParam);

        List lstValues = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstValues;
    }

    @Override
    public List getMonitorLaborCostM(Map inMap) {
        String year = StringUtil.replaceNull(inMap.get("year"));
        String month = StringUtil.replaceNull(inMap.get("month"));
        String officeId = StringUtil.replaceNull(inMap.get("officeId"));

        DAOMeta daoMeta = DAOHelper.getDAO("monitor-dao", "query_monitor_labor_cost_m", null);

        List lstValues = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstValues;
    }

    @Override
    public List getMonitorOtherShare(Map inMap) {
        String year = StringUtil.replaceNull(inMap.get("year"));
        String month = StringUtil.replaceNull(inMap.get("month"));
        String officeId = StringUtil.replaceNull(inMap.get("officeId"));
        List lstParam = new ArrayList();

        lstParam.add(year);
        lstParam.add(month);

        lstParam.add(officeId);

        lstParam.add(year);
        lstParam.add(month);

        lstParam.add(year);
        lstParam.add(month);

        DAOMeta daoMeta = DAOHelper.getDAO("monitor-dao", "query_monitor_other_share", lstParam);

        List lstValues = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstValues;
    }

    @Override
    public List getMonitorOtherShareM(Map inMap) {
        String year = StringUtil.replaceNull(inMap.get("year"));
        String month = StringUtil.replaceNull(inMap.get("month"));
        String officeId = StringUtil.replaceNull(inMap.get("officeId"));

        DAOMeta daoMeta = DAOHelper.getDAO("monitor-dao", "query_monitor_other_share_m", null);

        List lstValues = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstValues;
    }

    @Override
    public List getNonPrmIncomeByYear(Map inMap) {
        String year = StringUtil.replaceNull(inMap.get("year"));
        String month = StringUtil.replaceNull(inMap.get("month"));
        List lstParam = new ArrayList();
        lstParam.add(month);
        lstParam.add(month);
        lstParam.add(year);

        DAOMeta daoMeta = DAOHelper.getDAO("monitor-dao", "query_payment_year", lstParam);

        List lstValues = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstValues;
    }

    /**
     * 获取经营合同计划
     *
     * @param inMap
     * @return
     */
    @Override
    public List getOperateAgreementByYear(Map inMap) {
        String year = StringUtil.replaceNull(inMap.get("year"));
        String month = StringUtil.replaceNull(inMap.get("month"));
        List lstParam = new ArrayList();

        lstParam.add(month);
        lstParam.add(month);
        lstParam.add(year);

        DAOMeta daoMeta = DAOHelper.getDAO("monitor-dao", "query_income_year", lstParam);

        List lstValues = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstValues;
    }

    @Override
    public void initialMonitorData(String year) {
        List<NonProjectIncome> NonProjectIncome = incomeManager.getObjByYear(year);
        if (ListUtil.isEmpty(NonProjectIncome)) {
            String typeBudgetOut = "1";
            List<NonProjectExtraItem> lstBudgetOut = incomeManager.getNamesByType(typeBudgetOut);
            lstBudgetOut.forEach(t -> {
                NonProjectIncome in = new NonProjectIncome();
                in.setYear(year);
                in.setSubject(t.getName());
                in.setSeqNo(t.getSeqNo());
                incomeManager.save(in);
            });
        }

        List<NonProjectIncomeIn> NonProjectIncomeIn = incomeManager.getObjsByYear(year);
        if (ListUtil.isEmpty(NonProjectIncomeIn)) {

            NonProjectIncomeIn income = new NonProjectIncomeIn();
            income.setYear(year);
            income.setSubject("财务收入");
            incomeManager.save(income);

            NonProjectIncomeIn income2 = new NonProjectIncomeIn();
            income2.setYear(year);
            income2.setSubject("其他收入");
            incomeManager.save(income2);
        }
    }
}