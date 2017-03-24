package com.csnt.scdp.bizmodules.modules.scm.purchasereq.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.dto.PrmPurchaseReqDetailDto;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.bizmodules.modules.scm.purchasereq.services.intf.PurchasereqManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonModifyAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.core.workflow.impl.WorkFlowImpl;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:30:52
 */

@Scope("singleton")
@Controller("update-reject-reason")
@Transactional
public class UpdatePrmPurChaseDetailRejectAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(UpdatePrmPurChaseDetailRejectAction.class);

    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

    @Resource(name = "purchasereq-manager")
    private PurchasereqManager purchasereqManager;


    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap();
        String fallbackReason = (String) inMap.get("fallbackReason");
        List prmPurchaseReqDetailDto = (List) inMap.get("prmPurchaseReqDetailDto");
        if (ListUtil.isNotEmpty(prmPurchaseReqDetailDto)) {
            List<BasePojo> lstPojo = new ArrayList();
            for (Iterator<Map> it = prmPurchaseReqDetailDto.iterator(); it.hasNext(); ) {
                lstPojo.add((BasePojo) BeanUtil.map2Dto(it.next(), PrmPurchaseReqDetailDto.class));
            }
            DtoHelper.batchUpdate(lstPojo, PrmPurchaseReqDetail.class);
            //发送消息，询价单只可能属于一个采购申请，所以只发一条消息
            afterAction(lstPojo.get(0));
        }
        prmpurchasereqManager.cleanContract(inMap);
        //Do After
        return out;
    }

    protected void afterAction(BasePojo dtoObj) {
//        super.perform(inMap);
        super.afterAction(dtoObj);
        Map map = BeanUtil.bean2Map(dtoObj);
        String uuid = (String) map.get("prmPurchaseReqId");

        //查询审批人
        WorkFlowImpl workFlowImpl = BeanFactory.getBean(CommonAttribute.WORK_FLOW_IMPL);
        List lstEndUsers = workFlowImpl.loadProcessUserOfPreviousTask(uuid, null);

        //发送消息
        String sql = "SELECT T.START_USER_ID_, P.PURCHASE_REQ_NO\n" +
                "  FROM ACT_HI_PROCINST T\n" +
                "  LEFT JOIN PRM_PURCHASE_REQ P\n" +
                "    ON T.BUSINESS_KEY_ = P.UUID\n" +
                " WHERE T.BUSINESS_KEY_ =?";
        DAOMeta daoMeta = new DAOMeta();
        List conditionlst = new ArrayList<>();
        conditionlst.add(uuid);
        daoMeta.setLstParams(conditionlst);
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        List<Map<String, Object>> lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta, purchasereqManager.addScalarMapForWorkFlowCommentLoad());
        List<String> userIdLst = new ArrayList<>();
        userIdLst.addAll(lstEndUsers);
        ReceiptsMeta receiptsMeta = new ReceiptsMeta();
        String purchaseReqNo = null;
        if (ListUtil.isNotEmpty(lstUserInfo)) {
            for (int i = 0; i < lstUserInfo.size(); i++) {
                String userId = (String) lstUserInfo.get(i).get("startUserId");
                userIdLst.add(userId);
            }
            purchaseReqNo = (String) lstUserInfo.get(0).get("purchaseReqNo");
            receiptsMeta.setLstSendToUserId(userIdLst);
        }
        map.put("purchaseReqNo", purchaseReqNo);
        String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
        String templateNo = "PRM_PURCHASE_ROLLBACK_INFO";
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);
    }
}
