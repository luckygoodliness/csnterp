package com.csnt.scdp.bizmodules.modules.asset.maintain.action;

import com.csnt.scdp.bizmodules.entity.asset.AssetMaintain;
import com.csnt.scdp.bizmodules.modules.asset.maintain.services.intf.MaintainManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/26.
 */
@Scope("singleton")
@Controller("maintain-del")
@Transactional
public class DelAction implements IAction{
    private static ILogTracer tracer = LogTracerFactory.getInstance(DelAction.class);

    @Resource(name = "maintain-manager")
    private MaintainManager maintainManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        PersistenceCrudManager pcm= PersistenceFactory.getInstance();
        List uuidlst=(List) inMap.get("uuidlst");
        List statelst = (List) inMap.get("statelst");
        if (uuidlst.size()>0 && statelst.size() > 0) {
            for (int i = 0; i < statelst.size(); i++) {
                for(int j=0;j<uuidlst.size();j++) {
                    AssetMaintain assetMaintain= maintainManager.getAssetMaintainForUUID(uuidlst.get(j).toString());
                    if (statelst.get(i).toString() == "未确认") {
                        pcm.delete(assetMaintain);
                    }
                }
            }
        }
        return null;
    }
}
