package com.csnt.scdp.bizmodules.modules.scm.requestallot.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmPurchaseReq;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmPurchaseReqAttribute;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.requestallot.services.intf.RequestallotManager;

import javax.annotation.Resource;
import javax.persistence.Persistence;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-22 22:18:10
 */

@Scope("singleton")
@Controller("requestallot-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

    @Resource(name = "requestallot-manager")
    private RequestallotManager requestallotManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);
        BasePojo dtoObj = DtoHelper.getDtoFromMap(viewMap, dtoClass);
        String uuid = (String) BeanUtil.getProperty(dtoObj, ScmPurchaseReqAttribute.UUID);
        String principalName = (String) BeanUtil.getProperty(dtoObj, ScmPurchaseReqAttribute.PRINCIPAL);
        ScmPurchaseReq scmPurchaseReq = PersistenceFactory.getInstance().findByPK(ScmPurchaseReq.class, uuid);
        if (StringUtil.isNotEmpty(principalName)) {
            if (!principalName.equals(scmPurchaseReq.getPrincipal())) {
                sendMsg(dtoObj);
            }
        }
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    protected void sendMsg(BasePojo dtoObj) {
        super.afterAction(dtoObj);
        Map map = BeanUtil.bean2Map(dtoObj);
        String userId=(String)map.get(ScmPurchaseReqAttribute.PRINCIPAL);
        //发送消息
        ReceiptsMeta receiptsMeta = new ReceiptsMeta();
        List lstSendToUserId = new ArrayList<>();
        lstSendToUserId.add(userId);
        receiptsMeta.setLstSendToUserId(lstSendToUserId);
        String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
        String templateNo = "REQUEST_ALLOT_INFO";
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);
    }
}
