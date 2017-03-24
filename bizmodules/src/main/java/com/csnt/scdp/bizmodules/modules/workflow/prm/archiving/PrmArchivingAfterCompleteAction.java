package com.csnt.scdp.bizmodules.modules.workflow.prm.archiving;

import com.csnt.scdp.bizmodules.entity.prm.PrmArchiving;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_archiving_detail-after-fixed")
@Transactional

public class PrmArchivingAfterCompleteAction extends ERPWorkFlowBaseVariableCollectionAction {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        updateArchivingDate(inMap);
        return mapResult;
    }

    /**
     * 更新归档时间
     *
     * @param inMap
     */
    private void updateArchivingDate(Map inMap) {
        String uuid = (String) inMap.get("businessKey");
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmArchiving archiving = pcm.findByPK(PrmArchiving.class, uuid);
        Date currDate = new Date();
        archiving.setArchivingDate(currDate);
        pcm.update(archiving);
    }
}
