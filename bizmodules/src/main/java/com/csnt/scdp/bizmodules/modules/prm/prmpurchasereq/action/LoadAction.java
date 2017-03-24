package com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchaseReqDetailAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.dto.PrmPurchaseReqDetailDto;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.dto.PrmPurchaseReqDto;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:30:52
 */

@Scope("singleton")
@Controller("prmpurchasereq-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        String uuid = (String) inMap.get("uuid");
        //设置部门显示
        Map dtoMap = (Map) out.get("prmPurchaseReqDto");
        if (dtoMap != null) {
            String innerSupplier = (String) dtoMap.get("innerSupplier");
            if (StringUtil.isNotEmpty(innerSupplier)) {
                dtoMap.put("innerSupplierDesc", OrgHelper.getOrgNameByCode(innerSupplier));
            }
            String departCode = (String) dtoMap.get("departmentCode");
            if (StringUtil.isNotEmpty(departCode)) {
                dtoMap.put("departmentCodeDesc", OrgHelper.getOrgNameByCode(departCode));
            }
        }
        Map map = new HashMap();
        map.put("selfconditions", " REQ.UUID = '" + uuid + "'");
        DAOMeta daoMeta = DAOHelper.getDAO("prmpurchasereq-dao", "common_query", new ArrayList(), map);
        List<Map<String, Object>> lstChange = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstChange)) {
            ((Map) out.get("prmPurchaseReqDto")).put("stateNm", lstChange.get(0).get("stateNm"));
            ((Map) out.get("prmPurchaseReqDto")).put("projectName", lstChange.get(0).get("projectName"));
        }

        ArrayList<Map> detailList = (ArrayList<Map>) ((Map) out.get("prmPurchaseReqDto")).get("prmPurchaseReqDetailDto");
        for (Map mapTemp : detailList) {
            if (mapTemp.get("technicalDrawing") != null && mapTemp.get("technicalDrawing") != "") {
                mapTemp.put("isUploaded", "有");
            }
        }

        Map mapCond = new HashMap();
        mapCond.put("qryCondition", uuid);
        daoMeta = DAOHelper.getDAO("prmpurchasereq-dao", "query_load_reqdetail", null, mapCond);
        lstChange = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstChange)) {
            for (Map mapTemp : detailList) {
                for (Map tmp : lstChange) {
                    if (((Map) tmp).get("uuid").toString().equals(mapTemp.get("uuid").toString())) {
                        mapTemp.put("packageName", tmp.get("packageName"));
                        mapTemp.put("planAmount", tmp.get("planAmount"));
                        mapTemp.put("contractState", tmp.get("contractState"));
                        mapTemp.put("undertaker", tmp.get("undertaker"));
                        break;
                    }
                }
            }
        }

        //Do After
        return out;
    }


    @Override
    protected void afterAction(BasePojo dtoObj) {
        //通过明细去找项目
        PrmPurchaseReqDto prmPurchaseReqDto = (PrmPurchaseReqDto) dtoObj;
        List<PrmPurchaseReqDetailDto> prmPurchaseReqDetailDtoList = prmPurchaseReqDto.getPrmPurchaseReqDetailDto();
        //获取采购申请明细StampProjectUuid
        List stampProjectUuidList = new ArrayList();
        for (int i = 0; i < prmPurchaseReqDetailDtoList.size(); i++) {
            String stampProjectUuid = prmPurchaseReqDetailDtoList.get(i).getStampProjectUuid();
            if (StringUtil.isNotEmpty(stampProjectUuid)) {
                stampProjectUuidList.add(stampProjectUuid);
            }
        }
        if (ListUtil.isNotEmpty(stampProjectUuidList)) {
            QueryCondition condition = new QueryCondition();
            condition.setField("UUID");
            condition.setOperator("in");
            condition.setValueList(stampProjectUuidList);
            List<QueryCondition> listCondition = new ArrayList<QueryCondition>();
            listCondition.add(condition);
            List<PrmProjectMain> prmProjectMainList = PersistenceFactory.getInstance().findByAnyFields(PrmProjectMain.class, listCondition, null);
            for (int i = 0; i < prmPurchaseReqDetailDtoList.size(); i++) {
                PrmPurchaseReqDetailDto prmPurchaseReqDetailDto = prmPurchaseReqDetailDtoList.get(i);
                for (PrmProjectMain prmProjectMain : prmProjectMainList) {
                    if (prmProjectMain.getUuid().equals(prmPurchaseReqDetailDto.getStampProjectUuid())) {
                        prmPurchaseReqDetailDto.setFadSubjectCode(prmProjectMain.getProjectCode());
                    }
                }
            }
        }
    }
}
