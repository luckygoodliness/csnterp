package com.csnt.scdp.bizmodules.modules.workflow.prm.contractrevise;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmContractC;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmContractAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_contract_revised-start")
@Transactional

public class PrmContractReviseStart extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map dataInfo = super.perform(inMap);
        Map mapVar = new HashMap();
        mapVar.put("isMajorProject", dataInfo.get("isMajorProject"));
        mapVar.put("contractSignMoney", dataInfo.get("contractSignMoney"));

        String contractId = (String) dataInfo.get("prmContractId");
        BigDecimal contractNowMoney = dataInfo.get("contractNowMoney") == null ? BigDecimal.ZERO : (BigDecimal) dataInfo.get("contractNowMoney");
        BigDecimal contractLastMoney = BigDecimal.ZERO;
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map mapCondition = new HashMap();
        mapCondition.put(PrmContractAttribute.PRM_CONTRACT_ID, contractId);
        List<PrmContractC> lstC = pcm.findByAnyFields(PrmContractC.class, mapCondition, null);
        if (ListUtil.isNotEmpty(lstC)) {
            lstC = lstC.stream().filter(t -> t.getState().equals(BillStateAttribute.FAD_BILL_STATE_APPROVED) && t.getAuditTime() != null).collect(Collectors.toList());
            if (ListUtil.isNotEmpty(lstC)) {
                PrmContractC c = lstC.stream().max((x, y) -> x.getAuditTime().compareTo(y.getAuditTime())).get();
                if (c.getContractNowMoney() == null) {
                    contractLastMoney = c.getContractSignMoney();
                } else {
                    contractLastMoney = c.getContractNowMoney();
                }
            }
        }
        if (contractNowMoney.compareTo(contractLastMoney) == -1) {
            mapVar.put("contractMoneyReduce", 1);
        } else {
            mapVar.put("contractMoneyReduce", 0);
        }
        mapVar.putAll(ErpWorkFlowHelper.filterRoles(inMap, ErpWorkFlowHelper.loadUserInfoByUserID(inMap)));
        return mapVar;
    }
}
