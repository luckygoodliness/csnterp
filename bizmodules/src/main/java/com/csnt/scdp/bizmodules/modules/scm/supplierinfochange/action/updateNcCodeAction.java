package com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.action;

import com.csnt.scdp.bizmodules.attributes.CommonAttribute;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplierC;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.services.intf.SupplierinfochangeManager;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  ExportXlsAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 16:02:00
 */

@Scope("singleton")
@Controller("supplierinfochange-updatenccode")
@Transactional
public class updateNcCodeAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(updateNcCodeAction.class);

    @Resource(name = "supplierinfochange-manager")
    private SupplierinfochangeManager supplierinfochangeManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        String uuid = (String) inMap.get(CommonAttribute.UUID);
        String ncCode = (String) inMap.get("ncCode");
        if (StringUtil.isEmpty(ncCode)) {
            throw new BizException("NC编号不能为空！");
        }
        Map out = new HashMap<>();
        if (StringUtil.isNotEmpty(uuid)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            ScmSupplierC scmSupplierC = pcm.findByPK(ScmSupplierC.class, uuid);
            if (scmSupplierC != null) {
                scmSupplierC.setNcCode(ncCode);
                pcm.update(scmSupplierC);
            }
            pcm.update(scmSupplierC);
        } else {
            throw new BizException("没有UUID！");
        }
        return out;
    }
}
