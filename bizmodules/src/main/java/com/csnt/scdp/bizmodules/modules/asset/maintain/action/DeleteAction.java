package com.csnt.scdp.bizmodules.modules.asset.maintain.action;

import com.csnt.scdp.bizmodules.entity.asset.AssetMaintain;
import com.csnt.scdp.bizmodules.modules.asset.maintain.services.intf.MaintainManager;
import com.csnt.scdp.bizmodules.modules.common.action.ErpDeleteAction;
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
import java.util.List;
import java.util.Map;

/**
 * Description:  DeleteAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-22 20:02:10
 */

@Scope("singleton")
@Controller("maintain-delete")
@Transactional
public class DeleteAction extends ErpDeleteAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(DeleteAction.class);

    @Resource(name = "maintain-manager")
    private MaintainManager maintainManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        List lst = (List) super.getDeleteUuids(inMap);
        if (ListUtil.isNotEmpty(lst)) {
            for (Object obj : lst) {
                String uuid = StringUtil.replaceNull(obj);
                AssetMaintain assetMaintain = PersistenceFactory.getInstance().findByPK(AssetMaintain.class, uuid);
                if (!"0".equals(assetMaintain.getState())) {
                    throw new BizException("您不能删除非新增状态的维修申请！");
                }
            }
        }
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
