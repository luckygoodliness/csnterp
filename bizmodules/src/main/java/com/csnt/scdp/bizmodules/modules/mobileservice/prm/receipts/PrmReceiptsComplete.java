package com.csnt.scdp.bizmodules.modules.mobileservice.prm.receipts;

import com.csnt.scdp.bizmodules.modules.mobileservice.ERPMobileTerminalBaseVariableCollectionAction;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalQuery;
import com.csnt.scdp.bizmodules.modules.prm.contractc.services.intf.ContractCManager;
import com.csnt.scdp.bizmodules.modules.prm.receipts.services.intf.ReceiptsManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/8/23.
 */
@Scope("singleton")
@Controller("mobile-terminal-complete-prm_receipt")
@Transactional
public class PrmReceiptsComplete extends ERPMobileTerminalBaseVariableCollectionAction {
    @Resource(name = "receipts-manager")
    private ReceiptsManager receiptsManager;
    @Resource(name = "mobile-terminal-query")
    private MobileTerminalQuery mobileTerminalQuery;

    @Override
    public Map perform(Map map) throws BizException, SysException {
        Map resultMap = new HashMap<>();
        String uuid = (String) map.get(WorkFlowAttribute.BUSINESS_KEY);
        List formDataList = mobileTerminalQuery.erpMobileLoadParamToClient(map);
        Map result = new HashMap<>();
        boolean sucessFlag = true;
        if (ListUtil.isNotEmpty(formDataList) && formDataList.contains("wf_enable_actualMoney_actualDate=1")) {
            result = receiptsManager.validatePrmReceiptBeforeWorkFlowCommit(uuid);
            sucessFlag = (boolean) result.get(CommonAttribute.SUCCESS);
        }

        if (sucessFlag) {
            resultMap.put("result", true);
            resultMap.put("message", "提交成功");
        } else {
            resultMap.put("result", false);
            resultMap.put("message", result.get("message"));
        }
        return resultMap;
    }
}
