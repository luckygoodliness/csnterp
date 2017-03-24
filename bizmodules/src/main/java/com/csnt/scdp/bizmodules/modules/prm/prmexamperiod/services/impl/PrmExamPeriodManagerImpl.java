package com.csnt.scdp.bizmodules.modules.prm.prmexamperiod.services.impl;

import com.csnt.scdp.bizmodules.entity.prm.PrmExamPeriod;
import com.csnt.scdp.bizmodules.modules.prm.prmexamperiod.services.intf.PrmExamPeriodManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/14.
 */
@Scope("singleton")
@Service("prmexamperiod-manager")
public class PrmExamPeriodManagerImpl implements PrmExamPeriodManager {

    public Date getExamDateFromAppointedDate(Date appointedDate) {
        Date examDate = appointedDate;
        if (examDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String d = sdf.format(appointedDate);
            String sql = "SELECT *\n" +
                    "  FROM PRM_EXAM_PERIOD T\n" +
                    " WHERE TO_DATE('" + d + "', 'yyyy-mm-dd hh24:mi:ss') BETWEEN T.BEGIN_DATE AND\n" +
                    "       T.END_DATE\n" +
                    "   AND T.IS_ACTIVE = 1" +
                    "   AND (T.IS_VOID = 0 OR T.IS_VOID IS NULL)";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            List<Map<String, Object>> examDatelst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(examDatelst)) {
                examDate = examDatelst.get(0).get("examDate") == null ? appointedDate : (Date) examDatelst.get(0).get("examDate");
            }
        }
        return examDate;
    }

    public boolean checkValidDate(PrmExamPeriod period) {
        boolean checkResult = true;
        if (period != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sd = sdf.format(period.getBeginDate());
            String ed = sdf.format(period.getEndDate());
            String sql = "SELECT *\n" +
                    "  FROM PRM_EXAM_PERIOD T\n" +
                    " WHERE (TO_DATE('" + sd + "', 'yyyy-mm-dd hh24:mi:ss') BETWEEN" +
                    "       T.BEGIN_DATE AND T.END_DATE OR" +
                    "       TO_DATE('" + ed + "', 'yyyy-mm-dd hh24:mi:ss') BETWEEN" +
                    "       T.BEGIN_DATE AND T.END_DATE)" +
                    "   AND (T.IS_VOID IS NULL OR T.IS_VOID = 0)";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            List<Map<String, Object>> examDatelst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(examDatelst)) {
                if (examDatelst.size() > 1 || StringUtil.isEmpty(period.getUuid())) {
                    checkResult = false;
                } else {
                    String existedUuid = (String) examDatelst.get(0).get("uuid");
                    if (!period.getUuid().equals(existedUuid)) {
                        checkResult = false;
                    }
                }
            }
        }

        return checkResult;
    }
}
