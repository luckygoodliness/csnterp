package com.csnt.scdp.bizmodules.modules.scm.purchasereq.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.dto.ScmContractDto;
import com.csnt.scdp.framework.commonaction.crud.CommonModifyAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
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
 * Created by lijx on 2016/8/18.
 */
@Scope("singleton")
@Controller("prmpurchasereqdtl-update-isread")
@Transactional
public class UpdateIsReadAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(UpdateIsReadAction.class);

    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap();
        String sql = " update SCM_PURCHASE_REQ set is_read=1 where uuid=? ";
        List lstParams = new ArrayList<>();
        lstParams.add((String) inMap.get("uuid"));
        DAOMeta daoMeta1 = new DAOMeta(sql, lstParams );
        PersistenceFactory.getInstance().updateByNativeSQL(daoMeta1);
        //Do After
        return out;
    }
}
