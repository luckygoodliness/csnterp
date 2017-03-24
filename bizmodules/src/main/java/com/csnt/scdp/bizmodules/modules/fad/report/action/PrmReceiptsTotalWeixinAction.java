package com.csnt.scdp.bizmodules.modules.fad.report.action;

import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.weixin.helper.WeixinHelper;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by shenyx on 2016/1/27.
 */
@Scope("singleton")
@Controller("report-prm-receipts-total-weixin-action")
@Transactional
public class PrmReceiptsTotalWeixinAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(PrmReceiptsTotalWeixinAction.class);

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new HashMap<>();
        String weixinReceivers = (String) inMap.get("weixinReceivers");
        if (StringUtil.isNotEmpty(weixinReceivers)) {
            List<String> lstUserId = StringUtil.splitAsList(weixinReceivers, ";");
            List<String> lstWeixinId = MsgSendHelper.getUserContactAddr(lstUserId, ScdpUserAttribute.USER_WEIXIN);
            String sendContent = getSendContent(inMap);
            WeixinHelper.sendTextMessage(lstWeixinId, sendContent);
        }

        return out;
    }

    private String getSendContent(Map inMap) {
        String yearMonthStr = (String) inMap.get("yearMonth");
        yearMonthStr = yearMonthStr.substring(0, 10);
        String sql = "SELECT REMARK\n" +
                "  FROM (SELECT O.SHORT_CODE || ':本周收款' ||\n" +
                "               ROUND(SUM(CASE\n" +
                "                           WHEN R.ACTUAL_DATE >= TO_DATE(?, 'YYYY-MM-DD') THEN\n" +
                "                            R.ACTUAL_MONEY\n" +
                "                           ELSE\n" +
                "                            0\n" +
                "                         END) / 10000,\n" +
                "                     2) || '万元,累计收款' ||\n" +
                "               ROUND(SUM(R.ACTUAL_MONEY) / 10000, 2) || '万元' AS REMARK,\n" +
                "               O.SEQ_NO\n" +
                "          FROM SCDP_ORG O\n" +
                "         INNER JOIN (SELECT *\n" +
                "                      FROM SCDP_EXPAND_ROW\n" +
                "                     WHERE EXPAND_CODE = 'DEPT_PROP'\n" +
                "                       AND EXPAND_VALUE = '事业部') OE ON O.UUID = OE.DATA_UUID\n" +
                "          LEFT JOIN (SELECT PM.CONTRACTOR_OFFICE,\n" +
                "                           PR.ACTUAL_MONEY,\n" +
                "                           PR.ACTUAL_DATE\n" +
                "                      FROM PRM_RECEIPTS PR\n" +
                "                      LEFT JOIN PRM_PROJECT_MAIN PM ON PR.PRM_PROJECT_MAIN_ID =\n" +
                "                                                       PM.UUID\n" +
                "                     WHERE PR.STATE = '2'\n" +
                "                       AND PR.ACTUAL_DATE >=\n" +
                "                           TRUNC(TO_DATE(?, 'YYYY-MM-DD'), 'YEAR')\n" +
                "                       AND PR.ACTUAL_DATE <\n" +
                "                           TO_DATE(?, 'YYYY-MM-DD') + 7) R ON O.ORG_CODE =\n" +
                "                                                                         R.CONTRACTOR_OFFICE\n" +
                "         GROUP BY O.SHORT_CODE, O.SEQ_NO\n" +
                "        UNION ALL\n" +
                "        SELECT '本周合计收款:' || ROUND(SUM(CASE\n" +
                "                                        WHEN R.ACTUAL_DATE >= TO_DATE(?, 'YYYY-MM-DD') THEN\n" +
                "                                         R.ACTUAL_MONEY\n" +
                "                                        ELSE\n" +
                "                                         0\n" +
                "                                      END) / 10000,\n" +
                "                                  2) || '万元,公司累计收款' ||\n" +
                "               ROUND(SUM(R.ACTUAL_MONEY) / 10000, 2) || '万元' AS REMARK,\n" +
                "               99\n" +
                "          FROM PRM_RECEIPTS R\n" +
                "        \n" +
                "         WHERE R.STATE = '2'\n" +
                "           AND R.ACTUAL_DATE >=\n" +
                "               TRUNC(TO_DATE(?, 'YYYY-MM-DD'), 'YEAR')\n" +
                "           AND R.ACTUAL_DATE < TO_DATE(?, 'YYYY-MM-DD') + 7\n" +
                "        \n" +
                "        ) T\n" +
                " ORDER BY SEQ_NO\n";
        List lstParam = new ArrayList<>();
        lstParam.add(yearMonthStr);
        lstParam.add(yearMonthStr);
        lstParam.add(yearMonthStr);
        lstParam.add(yearMonthStr);
        lstParam.add(yearMonthStr);
        lstParam.add(yearMonthStr);
        DAOMeta daoMeta = new DAOMeta(sql, lstParam);
        List<Map<String, Object>> lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        String content = lstResult.stream().map(x -> (String) x.get("remark")).collect(Collectors.joining("\r\n"));
        return content;
    }
}