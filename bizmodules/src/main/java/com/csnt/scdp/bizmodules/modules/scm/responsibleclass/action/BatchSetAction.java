package com.csnt.scdp.bizmodules.modules.scm.responsibleclass.action;

import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.bizmodules.modules.scm.responsibleclass.services.intf.ResponsibleclassManager;
import com.csnt.scdp.framework.commonaction.crud.CommonModifyAction;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.UserHelper;
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
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 15:57:12
 */

@Scope("singleton")
@Controller("responsibleclass-batchset")
@Transactional
public class BatchSetAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(BatchSetAction.class);

    @Resource(name = "responsibleclass-manager")
    private ResponsibleclassManager responsibleclassManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        List uuidLst = (List) inMap.get("uuidLst");
        String paramId = (String) inMap.get("paramId");
        String updater = UserHelper.getUserId();
        if (ListUtil.isNotEmpty(uuidLst)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            String uuids = uuidLst.toString().replace(" ", "").replace("[", "").replace("]", "").replace(",", "','");
            List paramList = new ArrayList<>();
            paramList.add(paramId);
            paramList.add(updater);
            DAOMeta daoMeta = DAOHelper.getDAO("responsibleclass-dao", "update_principalname", paramList);
            String sql = daoMeta.getStrSql();
            sql = sql.replace("${selfConditions}", " UUID IN ('" + uuids + "')");
            daoMeta.setStrSql(sql);
            pcm.updateByNativeSQL(daoMeta);
        } else {
            throw new BizException("");
        }
        Map out = new HashMap<>();
        return out;
    }
}
