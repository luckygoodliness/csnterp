package com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmContractChange;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.services.intf.ScmcontractchangeManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 18:01:19
 */

@Scope("singleton")
@Controller("scmcontractchange-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "scmcontractchange-manager")
    private ScmcontractchangeManager scmcontractchangeManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        boolean result = false;
        //Do before
        //1.新增的数据，状态设置为新增
        scmcontractchangeManager.initMapForAdd(inMap);
        //2.检查金额（如果是采购，则变更前金额应大于变更后金额）
//        scmcontractchangeManager.checkMoney(inMap);
        //3.一切条件通过，执行新增语句
        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);

        String mainTabName = StringUtil.capFirst(BeanFactory.getClass(dtoClass).getSimpleName(), false);

        Map mapInput = (Map) viewMap.get(mainTabName);
        String runningNo = NumberingHelper.getNumbering("SCM_CONTRACT_CHANGE_NO", null);
        mapInput.put("runningNo", runningNo);
        Map out = super.perform(inMap);
        out.put("runningNo", runningNo);
        return out;
    }
    protected void beforeAction(BasePojo dtoObj) {
        ScmContractChange scmContractChange = (ScmContractChange) dtoObj;
		/*
        BigDecimal remainBudget = scmcontractchangeManager.getRemainBudget(scmContractChange.getScmContractId());
        if((scmContractChange.getNewValue().subtract(scmContractChange.getOriginalValue())).compareTo(remainBudget)>0){
            throw new BizException("变更金额过大,可用预算不足！"+"当前可用预算为"+remainBudget);
        }*/

        //校验是否已存在该合同
        String sql = "select t.running_no from SCM_CONTRACT_CHANGE t where t.state in('0','1','9') and t.scm_contract_id = ?";
        List lstParams = new ArrayList<>();
        lstParams.add(scmContractChange.getScmContractId());
        DAOMeta daoMeta1 = new DAOMeta(sql, lstParams);
        List list = PersistenceFactory.getInstance().findByNativeSQL(daoMeta1);
        if(ListUtil.isNotEmpty(list)){
            String runningNo = ""+((Map) list.get(0)).get("runningNo");
                throw new BizException("该合同已存在未审核变更流程"+runningNo+"，不能重复添加。");
        }

        DAOMeta daoMeta= new DAOMeta();
        String vsql = "SELECT SC.UUID,"+
                " (SELECT V.REMAIN_BUDGET"+
                " FROM VW_NON_BUDGET_EXECUTE V"+
                " WHERE V.UUID = SC.BUGDET_ID) AS REMAIN_BUDGET"+
                " FROM SCM_CONTRACT SC"+
                " WHERE SC.IS_PROJECT <> 1" +
                " AND SC.UUID = '"+scmContractChange.getScmContractId()+"'";
        daoMeta.setStrSql(vsql);
        List<Map<String,Object>> lstContractInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstContractInfo)) {
            BigDecimal remainBudget = new BigDecimal(((Map) (lstContractInfo.get(0))).get("remainBudget").toString());
            if((scmContractChange.getNewValue().subtract(scmContractChange.getOriginalValue())).compareTo(remainBudget)>0){
                throw new BizException("变更金额过大,可用预算不足！"+"当前可用预算为"+remainBudget);
            }
        }
    }
}
