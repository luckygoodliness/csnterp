package com.csnt.scdp.bizmodules.modules.workflow.scm.scmcontract;

import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_contract_prepare-reject")
@Transactional

public class ScmContractPrepareReject extends ScmContractPrepareComplete {


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapVar = super.perform(inMap);
        //把合同编制的合同状态改为合同编辑
        String businessKey = (String) inMap.get("businessKey");
        if (StringUtil.isNotEmpty(businessKey)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            ScmContract scmContract = pcm.findByPK(ScmContract.class, businessKey);
            if (scmContract != null) {
                scmContract.setContractState("1");
                pcm.update(scmContract);
            } else {
                throw new BizException("没有找到该合同！");
            }
        } else {
            throw new BizException("没有businessKey！");
        }
        return mapVar;
    }
}
