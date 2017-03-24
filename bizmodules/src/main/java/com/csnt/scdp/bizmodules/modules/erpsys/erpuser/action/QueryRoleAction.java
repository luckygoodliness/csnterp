package com.csnt.scdp.bizmodules.modules.erpsys.erpuser.action;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fisher on 2015/11/17.
 */
@Scope("singleton")
@Controller("erpuser-queryrole")
@Transactional
public class QueryRoleAction extends CommonQueryAction {
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        String userId = (String) inMap.get("userId");
        List<String> roleInfo = ErpUserHelper.getUserRoleInfo(userId);
        Map info = new HashMap();
        info.put("USERID", userId);
        info.put("ROLE", roleInfo);
        Map out = new HashMap();

        out.put("USER_ROLE_NAME", info);
        //Do After
        return out;
    }
}

