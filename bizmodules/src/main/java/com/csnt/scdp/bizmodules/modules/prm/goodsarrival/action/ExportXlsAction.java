package com.csnt.scdp.bizmodules.modules.prm.goodsarrival.action;

import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.prm.goodsarrival.services.intf.GoodsarrivalManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  ExportXlsAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-15 12:30:05
 */

@Scope("singleton")
@Controller("goodsarrival-exportxls")
@Transactional
public class ExportXlsAction extends CommonExportXlsAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ExportXlsAction.class);

	@Resource(name = "goodsarrival-manager")
	private GoodsarrivalManager goodsarrivalManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
        StringBuffer selfSql = new StringBuffer();
        selfSql.append(" 1=1 ");
        Map condMap = (Map) inMap.get("queryConditions");
        Object projectId = condMap.get("projectId");
        Object confirmState = condMap.get("confirmState");
//        Object scmContractCode = condMap.get("scmContractCode");
        condMap.remove("projectId");
        condMap.remove("confirmState");

        //查询项目拼的sql
        if (StringUtil.isNotEmpty(projectId)) {
            selfSql.append("and  c.PRM_PROJECT_MAIN_ID= '" + projectId + "' ");
        }
        //查询到货状态拼的sql
        if (StringUtil.isNotEmpty(confirmState)) {
            if(confirmState.equals("0")){
                //未确认
                selfSql.append(" and nvl(c.actual_amount,0) = 0");
            }else if(confirmState.equals("1")){
                //部分确认
                selfSql.append(" and c.amount > c.actual_amount  and  c.actual_amount <> 0");
            }else if(confirmState.equals("2")){
                //全部确认
                selfSql.append(" and c.amount = c.actual_amount");
            }else if(confirmState.equals("3")){
                //超额
                selfSql.append(" and c.amount < c.actual_amount");
            }

        }
        Map map ;
        if(inMap.containsKey(CommonAttribute.QUERY_EXTRA_PARAMS)){
            map = (HashMap) inMap.get(CommonAttribute.QUERY_EXTRA_PARAMS);
        }else{
            map = new HashMap();
        }
        map.put("selfconditions", selfSql.toString());
        inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, map);
		Map out = super.perform(inMap);
		//Do After
		return out;
	}
}
