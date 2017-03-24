package com.csnt.scdp.bizmodules.modules.common.action;

import com.csnt.scdp.bizmodules.modules.common.intf.CommonServiceManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonAddAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2015/10/24.
 */
@Scope("singleton")
@Controller("erp-common-file-delete")
@Transactional
public class FileDeleteAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(FileDeleteAction.class);

    @Resource(name = "erp-common-service-manager")
    private CommonServiceManager commonServiceManager;

    @Override
    public Map perform(Map inMap) {
        List<String> uuids = (List<String>) inMap.get(CommonAttribute.UUIDS);
        if (ListUtil.isNotEmpty(uuids)) {
            String sql = "delete from cdm_file_relation where uuid in (" + StringUtil.joinForSqlIn(uuids, ",") + ")";
            PersistenceFactory.getInstance().updateByNativeSQL(new DAOMeta(sql));
        }
        return new HashMap();
    }
}

