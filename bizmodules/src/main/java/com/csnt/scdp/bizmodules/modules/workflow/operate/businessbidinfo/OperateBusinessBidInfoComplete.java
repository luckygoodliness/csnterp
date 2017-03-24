package com.csnt.scdp.bizmodules.modules.workflow.operate.businessbidinfo;

import com.csnt.scdp.bizmodules.entity.nonprm.FinancialSubject;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchaseReqAttribute;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("operate_businessbidinfo-complete")
@Transactional

public class OperateBusinessBidInfoComplete extends OperateBusinessBidInfoStart {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapVar = super.perform(inMap);
        return mapVar;
    }
}
