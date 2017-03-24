package com.csnt.scdp.bizmodules.modules.workflow.prm.contractremove;

import com.csnt.scdp.bizmodules.entity.prm.PrmContractC;
import com.csnt.scdp.bizmodules.modules.prm.contract.dto.PrmContractDto;
import com.csnt.scdp.bizmodules.modules.prm.contractc.dto.PrmContractCDto;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.sql.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/16.
 */
@Scope("singleton")
@Controller("prm_contract_remove-after-fixed")
@Transactional
public class PrmContractRemoveAfterComplete extends ERPWorkFlowBaseVariableCollectionAction {

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        String uuid = (String) inMap.get("businessKey");
        PrmContractC contractC = PersistenceFactory.getInstance().findByPK(PrmContractC.class, uuid);
        List uuids = Arrays.asList(contractC.getPrmContractId());
        DtoHelper.casecadeDelete(PrmContractDto.class, uuids, false);
        return mapResult;
    }
}
