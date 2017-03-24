package com.csnt.scdp.bizmodules.modules.scm.scmsaeapproval.action;

import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.StringUtil;
import com.fr.stable.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lijx on 2016/9/7.
 */
@Scope("singleton")
@Controller("scmsaeapproval-queryresult")
@Transactional
public class QueryResultAction implements IAction {

    @Override
    public Map perform(Map map) throws BizException, SysException {
        String curYear = (String) map.get("curYear");
        curYear = curYear.substring(0, 4);
        String materialCode = (String)map.get("materialCode");
        Object printFalg = map.remove("supplierId");
        String supplierId2 = (String)map.remove("supplierId2");
        String extend = " 1=1 ";
        if(StringUtils.isNotEmpty(curYear))
            extend += "and ss.cur_year='"+curYear+"' ";
        if(StringUtils.isNotEmpty(supplierId2))
            extend += "and  ssr.supplier_id ='"+supplierId2+"' ";
        if(StringUtils.isNotEmpty(materialCode))
            extend += "and exists (select 1 from scm_sae_object sso\n" +
                    "    where sso.supplier_id = ssr.supplier_id and sso.material_code='"+materialCode+"' )";
        if (StringUtil.isNotEmpty(printFalg)){
            extend+=" and ssr.supplier_id not in (" + printFalg + ")";
        }
        Map mapCond = new HashMap();
        mapCond.put("conditions", extend);
        DAOMeta daoMeta = DAOHelper.getDAO("scmsaeapproval-dao", "result_query", null, mapCond);
        List lst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        Map outMap = new HashMap<>();
        outMap.put("scmSaeResultDto",lst);
        return outMap;
    }
}
