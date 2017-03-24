package com.csnt.scdp.bizmodules.modules.mobileservice;

import com.csnt.scdp.bizmodules.attributes.ErpMobileDtoAttribute;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuhd on 2015/11/9.
 */
public class ERPMobileTerminalBaseVariableCollectionAction implements IAction {

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        try {
            String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
            String workFlowDefinitionKey = (String) inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
            String dto = (String) inMap.get(WorkFlowAttribute.DTO);
            Class pojoClass = BeanUtil.getPojoClassByDto(BeanFactory.getClass(dto));
            BasePojo basePojo = DtoHelper.findDtoByPK(BeanFactory.getClass(dto), businessKey);
            Map mapResult = new HashMap<>();
            mapResult.put("basePojo",basePojo);
            return mapResult;
        } catch (Exception e) {
            throw new BizException(e.getMessage());
        }
    }
}
