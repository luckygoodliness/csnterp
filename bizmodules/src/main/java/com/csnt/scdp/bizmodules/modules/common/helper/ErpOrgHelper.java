package com.csnt.scdp.bizmodules.modules.common.helper;

import com.csnt.scdp.bizmodules.attributes.ExpandFieldName;
import com.csnt.scdp.bizmodules.attributes.ExpandFieldType;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpExpandRowAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import com.csnt.scdp.sysmodules.helper.OrgHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fisher on 2015/11/12.
 */
public class ErpOrgHelper {

    public static Boolean isBizDivision(String officeUUid) {
        String deptProp = ErpExpandFieldHelper.getExpandFieldValue(ExpandFieldType.ORG, officeUUid, ExpandFieldName.DEPT_PROP);
        if (StringUtil.isNotEmpty(deptProp) && "事业部".equals(deptProp)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean isBizDivision2(String orgCode) {
        String officeUUid = OrgHelper.getOrgIdByCode(orgCode);
        return isBizDivision(officeUUid);
    }

    public static String getBudgetMark(String officeUUid) {
        String bugdetCode = ErpExpandFieldHelper.getExpandFieldValue(ExpandFieldType.ORG, officeUUid, ExpandFieldName.BUGDET_CODE);
        return bugdetCode == null ? bugdetCode : bugdetCode.trim();
    }

    /**
     * 项目和合同界面是否需要判断重大项目的部门
     *
     * @return
     */
    public static Map<String, BigDecimal> getDeptMajorProjectMoney() {
        String sql = "select o.org_code, r.expand_value\n" +
                "  from SCDP_EXPAND_COLUMN t\n" +
                "  inner join SCDP_EXPAND_ROW r\n" +
                "    on t.uuid = r.expand_id\n" +
                "    inner join scdp_org o on r.data_uuid=o.uuid and o.org_type='D'\n" +
                " where t.expand_type = ?\n" +
                "   and t.is_secret = 0\n" +
                "  and t.expand_code=?\n" +
                "   and (t.company_uuid = ? or t.company_uuid = '*')";
        List lstParam = new ArrayList();
        lstParam.add(ExpandFieldType.ORG);
        lstParam.add(ExpandFieldName.MAJOR_PROJECT_MONEY);
        lstParam.add(UserHelper.getCompanyUuid());
        DAOMeta daoMeta = new DAOMeta(sql, lstParam);
        List<Map<String, Object>> lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        Map<String, BigDecimal> mapResult = new HashMap();
        lstResult.forEach(x -> {
            if (StringUtil.isNotEmpty(x.get(ScdpExpandRowAttribute.EXPAND_VALUE))) {
                mapResult.put((String) x.get(ScdpOrgAttribute.ORG_CODE), new BigDecimal(x.get(ScdpExpandRowAttribute
                        .EXPAND_VALUE).toString()));
            }
        });
        return mapResult;
    }

}
