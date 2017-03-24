package com.csnt.scdp.bizmodules.modules.scm.scmsaeapproval.action;

import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.fr.stable.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lijx on 2016/9/8.
 */
@Scope("singleton")
@Controller("scmsaeapproval-queryobject")
@Transactional
public class QueryObjectAction implements IAction {
    @Override
    public Map perform(Map map) throws BizException, SysException {
        String curYear = (String) map.get("curYear");
        curYear = curYear.substring(0, 4);
        String supplierId = (String)map.get("supplierId");
        String materialCode = (String)map.get("materialCode");
        long showAllDetail = (long)map.get("showAllDetail");
        String extend = " 1=1 ";
        if(StringUtils.isNotEmpty(curYear))
            extend += "and ss.cur_year='"+curYear+"' ";
        if( showAllDetail==0 ){
            if(StringUtils.isNotEmpty(supplierId))
                extend += "and sso.supplier_id='"+supplierId+"' ";
        }
        if(StringUtils.isNotEmpty(materialCode))
            extend += "and sso.material_code='"+materialCode+"'";

        Map mapCond = new HashMap();
        mapCond.put("conditions", extend);
        DAOMeta daoMeta = DAOHelper.getDAO("scmsaeapproval-dao", "objcet_query", null, mapCond);
        List lst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        Map outMap = new HashMap<>();
        outMap.put("scmSaeObjectDto",lst);
        return outMap;
    }
}
