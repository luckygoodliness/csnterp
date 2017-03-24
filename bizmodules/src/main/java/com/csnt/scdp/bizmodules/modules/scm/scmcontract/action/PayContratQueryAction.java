package com.csnt.scdp.bizmodules.modules.scm.scmcontract.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqC;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadPayReqCAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.fad.payreq.services.intf.PayreqManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import com.csnt.scdp.sysmodules.modules.sys.org.dto.ScdpOrgTreeDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("paycontract-query")
@Transactional
public class PayContratQueryAction extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(PayContratQueryAction.class);

    @Resource(name = "scmcontract-manager")
    private ScmcontractManager scmcontractManager;

    @Resource(name = "payreq-manager")
    private PayreqManager payreqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        StringBuffer selfSql = new StringBuffer();
        selfSql.append(" 1=1 ");
        Map condMap = (Map) inMap.get("queryConditions");
        Object printFalg = condMap.get("supplierCodeS");
        Object needContractAmountBiggerThenPaidMoneyS = condMap.get("needContractAmountBiggerThenPaidMoneyS");

        condMap.remove("supplierCodeS");
        condMap.remove("needContractAmountBiggerThenPaidMoneyS");
        String[] filterUUid = null;
        if (StringUtil.isNotEmpty(printFalg)) {
            filterUUid = ((String) printFalg).split(",");
        }
        if (StringUtil.isEmpty(needContractAmountBiggerThenPaidMoneyS) ||"1".equals(needContractAmountBiggerThenPaidMoneyS)) {
            selfSql.append(" and t.amount > t.scm_paid_money ");
        }
        String officeId = (String) condMap.get("officeIdPlusQ");
        if (StringUtil.isNotEmpty(officeId)) {
            String[] officeIds = officeId.split("\\|");
            List list = Arrays.asList(officeIds);
            String tmpOffice = StringUtil.joinForSqlIn(list, ",");
            selfSql.append(" and t.office_id  in (" + tmpOffice + ")");
        }

        //传入条件若有上期支付信息，则对采购合同只选择该期支付过的采购合同
        String fadPayreqQ = (String) condMap.get("fadPayreqQ");
        condMap.remove("fadPayreqQ");

        Map<String, List<FadPayReqC>> fadPayReqCGroup = payreqManager.getFadPayReqCbyPuuid(fadPayreqQ)
                .stream().filter(t -> StringUtil.isNotEmpty(t.getScmContractId()) && t.getCreateBy().equals(UserHelper.getUserId()))
                .collect(Collectors.groupingBy(FadPayReqC::getScmContractId));
        if (StringUtil.isNotEmpty(fadPayreqQ)) {
            if (MapUtil.isNotEmpty(fadPayReqCGroup)) {
                List<String> scmId = new ArrayList(fadPayReqCGroup.keySet());
                if (StringUtil.isNotEmpty(scmId)) {
                    String tmp = StringUtil.joinForSqlIn(scmId, ",");
                    selfSql.append(" and t.uuid  in (" + tmp + ")");
                }
            } else {
                selfSql.append(" and 1=2");
            }
        }
        Map map = new HashMap();
        map.put("selfconditions", selfSql.toString());
        inMap.put(CommonAttribute.QUERY_EXTRA_PARAMS, map);

        //Do before 拼接Sql
        Map out = super.perform(inMap);
        //Do After 在选择上期支付信息时 填充上期申请金额 和上期未支付金额
        List<Map> oul = (ArrayList) out.get("root");
        if (filterUUid != null) {
            List<String> lstUuid = Arrays.asList(filterUUid);
            for (int i = 0; i < oul.size(); i++) {
                String uuid = "'" + (String) ((Map) oul.get(i)).get("uuid") + "'";
                if (lstUuid.contains(uuid)) {
                    oul.remove(oul.get(i));
                    i--;
                }
            }
        }
        if (StringUtil.isNotEmpty(fadPayreqQ)) {
            oul.forEach(t -> {
                        String uuid = (String) ((Map) t).get("uuid");
                        if (fadPayReqCGroup.containsKey(uuid)) {
                            FadPayReqC reqc = fadPayReqCGroup.get(uuid).get(0);
                            ((Map) t).put("reqcMoney", reqc.getReqMoney());
                            ((Map) t).put("reqcAuditMoney", reqc.getAuditMoney());
                            Object scmPaidMoney = ((Map) t).get("scmPaidMoney");
                            if (reqc.getScmContractExpectPaid() != null && scmPaidMoney != null && scmPaidMoney instanceof BigDecimal) {
                                BigDecimal reqcUnpaidMoney = reqc.getScmContractExpectPaid().subtract(new BigDecimal(scmPaidMoney.toString()));
                                reqcUnpaidMoney = reqcUnpaidMoney.compareTo(BigDecimal.ZERO) > 0 ? reqcUnpaidMoney : BigDecimal.ZERO;
                                ((Map) t).put("reqcUnpaidMoney", reqcUnpaidMoney);
                            }
                        }
                    }
            );
        }
        return out;
    }
}
