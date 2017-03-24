package com.csnt.scdp.bizmodules.modules.workflow;

import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DBHelper;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.sysmodules.modules.sys.workflow.action.WorkFlowVariableProcessAction;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
public class ERPWorkFlowBaseVariableCollectionAction extends WorkFlowVariableProcessAction {

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        try {
            String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
            String dto = (String) inMap.get(WorkFlowAttribute.DTO);
            Class pojoClass = BeanUtil.getPojoClassByDto(BeanFactory.getClass(dto));
            Object beanMap = PersistenceFactory.getInstance().findByPK(pojoClass, businessKey);
            Map mapResult = BeanUtil.bean2Map(beanMap);
            return mapResult;
        } catch (Exception e) {
            throw new BizException(e.getMessage());
        }
    }
}
