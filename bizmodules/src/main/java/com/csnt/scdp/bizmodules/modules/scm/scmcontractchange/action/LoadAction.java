package com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmContractChangeD;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractChangeAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractChangeDAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.services.intf.ScmcontractchangeManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
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
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 18:01:19
 */

@Scope("singleton")
@Controller("scmcontractchange-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "scmcontractchange-manager")
    private ScmcontractchangeManager scmcontractchangeManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        Map dtoMap = (Map) out.get("scmContractChangeDto");
        if (dtoMap != null) {
            //1.获取合同信息
            Map contractInfo = scmcontractchangeManager.getContractInfo((String) dtoMap.get("scmContractId"));
            dtoMap.put(ScmContractChangeAttribute.CONTRACTNAME, contractInfo.get("scmContractCode"));
            dtoMap.put(ScmContractChangeAttribute.FAD_SUBJECT_CODE, contractInfo.get("fadSubjectCode"));
            dtoMap.put(ScmContractChangeAttribute.FAD_SUBJECT_NAME, contractInfo.get("fadSubjectName"));
            dtoMap.put(ScmContractChangeAttribute.SUPPLIER_NAME, contractInfo.get("supplierName"));
            dtoMap.put("isProject", contractInfo.get("isProject"));
            //M3_NC3_采购变更申请（项目）
            dtoMap.put(ScmContractChangeAttribute.PROJECT_ID, contractInfo.get("projectId"));
            dtoMap.put("contractNature", contractInfo.get("contractNature"));
            dtoMap.put("supplierCode", contractInfo.get("supplierCode"));
            dtoMap.put("purchasePackageId", contractInfo.get("purchasePackageId"));
            //2.设置负责人的显示
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            String createByName = "";
            String createByCode = (String) dtoMap.get(ScmContractChangeAttribute.CREATE_BY);
            if (StringUtil.isNotEmpty(createByCode)) {
                Map paramMap = new HashMap<>();
                paramMap.put(ScdpUserAttribute.USER_ID, createByCode);
                List<ScdpUser> list = pcm.findByAnyFields(ScdpUser.class, paramMap, null);
                if (ListUtil.isNotEmpty(list)) {
                    createByName = list.get(0).getUserName();
                }
            }
            dtoMap.put(ScmContractChangeAttribute.CREATE_BY_DESC, createByName);


            ArrayList<Map> detailList = (ArrayList<Map>) ((Map) out.get("scmContractChangeDto")).get("scmContractChangeDDto");
            List prmPurchasePlanDetailIdList = new ArrayList<>();
            if (ListUtil.isNotEmpty(detailList)) {
                for (Map m : detailList) {
                    prmPurchasePlanDetailIdList.add("" + m.get("prmPurchasePlanDetailId"));
                }
            }

            if (ListUtil.isNotEmpty(prmPurchasePlanDetailIdList)) {
                String sql = " SELECT V.PRM_PURCHASE_PLAN_DETAIL_ID," +
                        " PK.PACKAGE_NAME," +
                        " (PL.AMOUNT - NVL(V.APPLIEDAMOUNT, 0)) AS PLAN_AMOUNT" +
                        " FROM (SELECT V.PRM_PURCHASE_PLAN_DETAIL_ID," +
                        " SUM(V.LOCK_AMOUNT) AS APPLIEDAMOUNT" +
                        " FROM VW_PURCHASE_PLAN_DETAIL_LOCK V" +
                        " WHERE V.PRM_PURCHASE_PLAN_DETAIL_ID IN (" + StringUtil.joinForSqlIn(prmPurchasePlanDetailIdList, ",") + ")" +
                        " GROUP BY V.PRM_PURCHASE_PLAN_DETAIL_ID) V" +
                        " LEFT JOIN PRM_PURCHASE_PLAN_DETAIL PL ON V.PRM_PURCHASE_PLAN_DETAIL_ID = PL.UUID" +
                        " LEFT JOIN PRM_PURCHASE_PACKAGE PK ON PL.PURCHASE_PACKAGE_ID = PK.UUID";

                DAOMeta daoMeta1 = new DAOMeta(sql, null);
                List lst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta1);
                if (ListUtil.isNotEmpty(detailList)) {
                    for (Map mapTemp : detailList) {
                        for (Object obj : lst) {
                            HashMap m = (HashMap) obj;
                            if (m.get("prmPurchasePlanDetailId").toString().equals(mapTemp.get("prmPurchasePlanDetailId").toString())) {
                                mapTemp.put("packageName", m.get("packageName"));
                                mapTemp.put("planAmount", m.get("planAmount"));

                                BigDecimal amount = mapTemp.get("handleAmount") == null ? new BigDecimal("0") : (BigDecimal) mapTemp.get("handleAmount");
                                BigDecimal expectedPrice = mapTemp.get("expectedPrice") == null ? new BigDecimal("0") : (BigDecimal) mapTemp.get("expectedPrice");
                                mapTemp.put("subTotal", amount.multiply(expectedPrice));
                                break;
                            }
                        }
                    }
                }
            }
        }
        return out;
    }
}
