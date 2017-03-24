package com.csnt.scdp.bizmodules.modules.scm.purchasereq.action;

import com.csnt.scdp.bizmodules.modules.outerservice.ciimp.intf.CIIMPServiceManager;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.framework.commonaction.crud.CommonModifyAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lijl on 2016/10/26.
 */
@Scope("singleton")
@Controller("purchasereq-pushquotation")
@Transactional
public class pushQuotationAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(pushQuotationAction.class);

    @Resource(name = "ciimpservice-manager")
    private CIIMPServiceManager ciimpServiceManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        Map out = new HashMap();

        String scmContractId = "" + inMap.get("scmContractId");
        Map mainMap = new HashMap<>();
        Map detailMap = new HashMap<>();
        List paramList = new ArrayList<>();
        paramList.add(scmContractId);
        paramList.add(scmContractId);
        DAOMeta mainDao = DAOHelper.getDAO("purchasereq-dao", "quotation_main_query", paramList);
        List mainList = PersistenceFactory.getInstance().findByNativeSQL(mainDao);
        if (ListUtil.isNotEmpty(mainList)) {
            mainMap = (Map) mainList.get(0);
        } else {
            throw new BizException("未找到该询价单！");
        }
        List paramList2 = new ArrayList<>();
        paramList2.add(scmContractId);
        DAOMeta detailDao = DAOHelper.getDAO("purchasereq-dao", "quotation_detail_query", paramList2);
        List detailList = PersistenceFactory.getInstance().findByNativeSQL(detailDao);
        if (ListUtil.isNotEmpty(detailList)) {
            mainMap.put("ciimpInquiryProductDto", detailList);
        }
        String result = ciimpServiceManager.sendFinInfo("inquiry/submitInquiry", mainMap);
        if("1".equals(result)){
            out.put("msg", "发送成功！");
        }else {
            out.put("msg", "发送失败，请重新发送！");
        }
        return out;
    }
}
