package com.csnt.scdp.bizmodules.modules.workflow.prm.projectmain;

import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_main-reject")
@Transactional

public class PrmMainReject extends PrmMainComplete {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        return super.perform(inMap);
    }
}
