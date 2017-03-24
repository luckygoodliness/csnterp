package com.csnt.scdp.bizmodules.modules.fad.payreq.action;

import com.csnt.scdp.bizmodules.entity.fad.FadPayReqH;
import com.csnt.scdp.bizmodules.modules.fad.payreq.services.intf.PayreqManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fisher on 2015/11/23.
 */
@Scope("singleton")
@Controller("scm_purchasepaymentrequestmonthpayment-after-complete")
@Transactional
public class PayReqAfterCompleteAction extends ERPWorkFlowBaseVariableCollectionAction implements IAction {

    @Resource(name = "payreq-manager")
    private PayreqManager payreqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map map = new HashedMap();
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        String dto = (String) inMap.get(WorkFlowAttribute.DTO);
        String tableName = BeanUtil.getTableNameByPojoClass(BeanUtil.getPojoClassByDto(BeanFactory.getClass(dto))).toLowerCase();
        Integer state = 0;
        String status = (String) inMap.get("status");
        if ("PROCESSING".equals(status)) {
            state = 1;//已提交
        } else if ("FIXED".equals(status)) {
            state = 2;//已审核
        }

        if (StringUtil.isNotEmpty(tableName) && state != 0) {
            String sql = "update " + tableName + " set state = ? where uuid = ?";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            List lstParam = new ArrayList();
            lstParam.add(state);
            lstParam.add(businessKey);
            daoMeta.setLstParams(lstParam);
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            pcm.updateByNativeSQL(daoMeta);
        }


        //关闭当月之前所有的月度支付申请
        if (state == 2) {
            FadPayReqH FadPayReqh = payreqManager.getFadPayReqHbyUuid(businessKey);
            payreqManager.closePreviousMonthReq(FadPayReqh);
            payreqManager.endorseMonthReqChildren(FadPayReqh);
        }
        //将该月之前的所有月度支付关闭，同时将状态不是发送的数据状态改为3作废

        Map dataInfo = super.perform(inMap);
        String month = (String) dataInfo.get("month");
        if ("usertask5".equals(inMap.get(WorkFlowAttribute.TASK_DEF_KEY))) {
            payreqManager.sendMsg(businessKey, month);
        }

        return map;
    }
}
