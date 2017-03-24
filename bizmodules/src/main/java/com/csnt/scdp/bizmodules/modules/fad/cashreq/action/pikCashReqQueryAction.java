package com.csnt.scdp.bizmodules.modules.fad.cashreq.action;

import com.csnt.scdp.bizmodules.modules.fad.cashreq.services.intf.CashreqManager;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmProjectMainDto;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.ObjectUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 09:40:50
 */

@Scope("singleton")
@Controller("pikcashreq-query")
@Transactional
public class pikCashReqQueryAction extends CommonQueryAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(pikCashReqQueryAction.class);

	@Resource(name = "cashreq-manager")
	private CashreqManager cashreqManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
        StringBuffer selfSql = new StringBuffer();
        selfSql.append(" 1=1 ");
        //Do before
        Map condMap = (Map) inMap.get("queryConditions");
//        if ("1".equals(StringUtil.replaceNull(printFalg).toString())) {
//            selfSql.append(" and ivhdr.print_num>0 ");
//        } else if ("0".equals(StringUtil.replaceNull(printFalg).toString())) {
//            selfSql.append(" and (ivhdr.print_num=0 or ivhdr.print_num is null)");
//        }
		//日常报销（项目）-保证金核销
		Object bzj = condMap.remove("projectIdBzj");
        List lst = null;
        if(bzj != null) {
            String subjectCodeBzj = condMap.get("subjectCodeBzj").toString();
            if("TECHNICAL_SERVICE_FEE".equals(subjectCodeBzj)) {
                String projectId = bzj.toString();
                PrmProjectMainDto projectMainDto = (PrmProjectMainDto)DtoHelper.findDtoByPK(PrmProjectMainDto.class, projectId);
                String officeId  = projectMainDto.getContractorOffice();
                StringBuffer sql = new StringBuffer();
                if(condMap.containsKey("notInRow")) {
                    Object notInRow = condMap.get("notInRow");
                    sql.append(" t.uuid not in (" + notInRow + ")");
                }
                Map contextMap = new HashMap();
                contextMap.put("projectId", projectId);
                contextMap.put("projectId", projectId);
                contextMap.put("officeId", officeId);
                contextMap.put("selfSql", sql);
                DAOMeta daoMeta = DAOHelper.getDAO("cashreq-dao", "if-exists-deposit", null, contextMap);
                daoMeta.setNeedFilter(false);
                lst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            }
        }
        condMap.remove("subjectCodeBzj");

        if(condMap.containsKey("notInRow")) {
            Object notInRow = condMap.get("notInRow");
            selfSql.append(" and t.uuid not in (" + notInRow + ")");
        }
        if(condMap.containsKey("isProject")){
            Object isProject = condMap.get("isProject");
            if(!((String) isProject).isEmpty()){
                selfSql.append(" and nvl2(t.project_Id,'1','0') = '"+isProject+"'");
            }
        }
        condMap.remove("notInRow");
        condMap.remove("isProject");
        Map map = new HashMap();
        map.put("selfconditions", selfSql.toString());
        inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, map);
		Map out = super.perform(inMap);

        if(ListUtil.isNotEmpty(lst)) {
            List resLst = (ArrayList)out.get("root");
            Integer num = (Integer)out.get("totalProperty");
            resLst.addAll(lst);
            out.put("root", resLst);
            out.put("totalProperty", num+lst.size());
        }

		//Do After
		return out;
	}
}
