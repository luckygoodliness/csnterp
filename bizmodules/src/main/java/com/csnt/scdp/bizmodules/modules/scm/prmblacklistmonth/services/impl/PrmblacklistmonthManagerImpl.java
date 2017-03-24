package com.csnt.scdp.bizmodules.modules.scm.prmblacklistmonth.services.impl;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entityattributes.scm.PrmBlacklistMonthAttribute;
import com.csnt.scdp.bizmodules.modules.scm.prmblacklistmonth.services.intf.PrmblacklistmonthManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  PrmblacklistmonthManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-14 15:47:35
 */

@Scope("singleton")
@Service("prmblacklistmonth-manager")
public class PrmblacklistmonthManagerImpl implements PrmblacklistmonthManager {
    @Override
    public void setExtraData(Map outMap) {
        Map dtoMap = (Map) outMap.get("prmBlacklistMonthDto");
        if (dtoMap != null) {
            //1.设置项目名的显示
            String projectName = "";
            String prmProjectMainId = (String) dtoMap.get("prmProjectMainId");
            if (StringUtil.isNotEmpty(prmProjectMainId)) {
                PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, prmProjectMainId);
                if (prmProjectMain != null) {
                    projectName = prmProjectMain.getProjectName();
                }
            }
            dtoMap.put(PrmBlacklistMonthAttribute.PROJECT_NAME, projectName);
            //设置供应商名的显示
            String scmSupplierName = "";
            String scmSupplierId = (String) dtoMap.get("scmSupplierId");
            if (StringUtil.isNotEmpty(scmSupplierId)) {
                ScmSupplier scmSupplier = PersistenceFactory.getInstance().findByPK(ScmSupplier.class, scmSupplierId);
                if (scmSupplier != null) {
                    scmSupplierName = scmSupplier.getCompleteName();
                }
            }
            dtoMap.put(PrmBlacklistMonthAttribute.SCM_SUPPLIER_NAME, scmSupplierName);
            //设置投诉人名的显示
            String complainantName = "";
            String complainant = (String) dtoMap.get("complainant");
            if (complainant != null) {
                Map paramMap = new HashMap<>();
                paramMap.put(ScdpUserAttribute.USER_ID, complainant);
                List<ScdpUser> list = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, paramMap, null);
                if (ListUtil.isNotEmpty(list)) {
                    complainantName = list.get(0).getUserName();
                }
            }
            dtoMap.put(PrmBlacklistMonthAttribute.COMPLAINANT_NAME, complainantName);
        }
    }

    @Override
    public List getCurrentBlacklist() {
        String daoType = "prmblacklistmonth-dao";
        String daoKeyQuery = "common_query_current_blacklist";
        DAOMeta daoMeta = DAOHelper.getDAO(daoType, daoKeyQuery, null);
        return PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
    }
}