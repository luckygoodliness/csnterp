package com.csnt.scdp.bizmodules.modules.asset.apply.action;

import com.csnt.scdp.bizmodules.entity.asset.AssetDiscardApply;
import com.csnt.scdp.bizmodules.modules.asset.apply.services.intf.ApplyManager;
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
import java.util.List;
import java.util.Map;

/**
 * Description:  DeleteAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 20:06:31
 */

@Scope("singleton")
@Controller("apply-delete")
@Transactional
public class DeleteAction extends ErpDeleteAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(DeleteAction.class);

    @Resource(name = "apply-manager")
    private ApplyManager applyManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        List lst = (List) super.getDeleteUuids(inMap);
        if (ListUtil.isNotEmpty(lst)) {
            for (Object obj : lst) {
                String uuid = StringUtil.replaceNull(obj);
                AssetDiscardApply assetDiscardApply = PersistenceFactory.getInstance().findByPK(AssetDiscardApply.class, uuid);
                if (!"0".equals(assetDiscardApply.getState())) {
                    throw new BizException("您不能删除非新增状态的报废申请");
                }
            }
        }
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
}
