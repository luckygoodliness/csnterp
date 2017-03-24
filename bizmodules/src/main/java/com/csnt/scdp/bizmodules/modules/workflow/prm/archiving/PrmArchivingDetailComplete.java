package com.csnt.scdp.bizmodules.modules.workflow.prm.archiving;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.core.workflow.intf.IWorkFlow;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.modules.sys.workflow.action.WorkFlowVariableProcessAction;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_archiving_detail-complete")
@Transactional

public class PrmArchivingDetailComplete extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map dataInfo = super.perform(inMap);
        Map mapVar = new HashMap();
        mapVar.put("isMajorProject", dataInfo.get("isMajorProject"));
        mapVar.put("contractSignMoney", dataInfo.get("contractSignMoney"));
        mapVar.putAll(ErpWorkFlowHelper.filterRoles(inMap, ErpWorkFlowHelper.loadUserInfoByUserID(inMap)));
        return mapVar;
    }
}
