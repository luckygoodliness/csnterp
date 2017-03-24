package com.csnt.scdp.bizmodules.modules.nonprm.monitor.services.intf;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Description:  MonitorManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-27 15:12:59
 */
public interface MonitorManager {
    /**
     * 字符串值处理
     *
     * @param value 字符串值
     * @return 字符串值为null时返回"",否则返回字符串值
     */
    String isNullReturnEmpty(Object value);

    /**
     * 字符串值处理
     *
     * @param value 数字整型值
     * @return 数字整型值为null时返回"",否则返回字符串值
     */
    String isNullReturnEmpty(Integer value);

    /**
     * 监控基础表时间戳验证
     *
     * @param officeId   须时间戳验证的监控基础表部门编号
     * @param tblVersion 时间戳
     */
    void monitorBaseCheckTimeStamp(String officeId, String tblVersion);

    /**
     * 人工费用表时间戳验证
     *
     * @param monitorLaborCostUuid 须时间戳验证的人工费用表uuid
     * @param tblVersion           时间戳
     */
    void monitorLaborCostCheckTimeStamp(String monitorLaborCostUuid, String tblVersion);

    /**
     * 其他分摊费用表时间戳验证
     *
     * @param monitorOtherShareUuid 须时间戳验证的其他分摊费用表uuid
     * @param tblVersion            时间戳
     */
    void monitorOtherShareCheckTimeStamp(String monitorOtherShareUuid, String tblVersion);

    /**
     * 人工费用
     *
     * @param map
     * @return
     */
    List getMonitorLaborCost(Map map);

    /**
     * 人工费用模版
     *
     * @param map
     * @return
     */
    List getMonitorLaborCostM(Map map);

    /**
     * 其他分摊费用
     *
     * @param map
     * @return
     */
    List getMonitorOtherShare(Map map);

    /**
     * 其他分摊费用模版
     *
     * @param map
     * @return
     */
    List getMonitorOtherShareM(Map map);

    /**
     * 其他非项目支出终审金额
     *
     * @param map
     * @return
     */
    List getNonPrmIncomeByYear(Map map);

    /**
     * 获取经营合同计划
     *
     * @param map
     * @return
     */
    List getOperateAgreementByYear(Map map);

    void initialMonitorData(String year);
}