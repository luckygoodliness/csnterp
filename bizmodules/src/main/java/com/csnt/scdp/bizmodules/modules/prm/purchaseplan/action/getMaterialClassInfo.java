package com.csnt.scdp.bizmodules.modules.prm.purchaseplan.action;

import com.csnt.scdp.bizmodules.entity.nonprm.FinancialSubject;
import com.csnt.scdp.bizmodules.entity.scm.ScmMaterialClass;
import com.csnt.scdp.bizmodules.modules.nonprm.financialsubject.dto.FinancialSubjectDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.services.intf.PurchaseplanManager;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/9/23.
 */
@Scope("singleton")
@Controller("get-materialClass-info")
@Transactional
public class getMaterialClassInfo implements IAction {

    @Resource(name = "purchaseplan-manager")
    private PurchaseplanManager purchaseplanManager;


    @Override
    public Map perform(Map map) throws BizException, SysException {
        Map result = new HashMap<>();
        List classCode = (List) map.get("classCode");
        result = purchaseplanManager.getRunMaterialInfo(classCode);
        return result;
    }
}
