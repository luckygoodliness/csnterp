package com.csnt.scdp.bizmodules.modules.workflow.prm.failyclaim;

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
@Controller("prm_faily_claims-reject")
@Transactional

public class PrmFailyClaimsReject extends PrmFailyClaimsStart {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        return super.perform(inMap);
    }
}
