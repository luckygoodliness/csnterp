package com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReq;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchaseReqAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchaseReqDetailAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/2/23.
 */

@Scope("singleton")
@Controller("purchasereq-validate-before-submit")
@Transactional
public class ValidateSubmitAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(SubmitAction.class);


    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new HashMap();
        String prmProjectMainId = (String) inMap.get("prmProjectMainId");
        String uuid = (String) inMap.get("uuid");
        Long isInner = (Long) inMap.get("isInner");
        String sql = "select sr.role_name\n" +
                "  from prm_member_detail_p pmdp\n" +
                "  left join scdp_role sr\n" +
                "    on sr.uuid = pmdp.post\n" +
                " where pmdp.prm_project_main_id =? and sr.role_name=?";

        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List condition = new ArrayList<>();
        condition.add(prmProjectMainId);
        condition.add("项目经理");
        daoMeta.setLstParams(condition);
        daoMeta.setNeedFilter(false);
        List lst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isEmpty(lst)) {
            out.put("result", false);
        } else {
            out.put("result", true);
        }
        if (isInner == 1) {
            Map conditionById = new HashMap<>();
            conditionById.put(PrmPurchaseReqDetailAttribute.PRM_PURCHASE_REQ_ID, uuid);
            List<PrmPurchaseReqDetail> list = PersistenceFactory.getInstance().findByAnyFields(PrmPurchaseReqDetail.class, conditionById, null);
            boolean flag = true;
            if (ListUtil.isNotEmpty(list)) {
                for (int i = 0; i < list.size(); i++) {
                    if (StringUtil.isNotEmpty(list.get(0).getIsStamp())) {
                        if (!list.get(0).getIsStamp().equals(list.get(i).getIsStamp())) {
                            flag = false;
                            break;
                        }
                    } else {
                        if (StringUtil.isNotEmpty(list.get(i).getIsStamp())) {
                            flag = false;
                            break;
                        }
                    }

                }
            }
            out.put("check", flag);
        }
        return out;
    }
}
