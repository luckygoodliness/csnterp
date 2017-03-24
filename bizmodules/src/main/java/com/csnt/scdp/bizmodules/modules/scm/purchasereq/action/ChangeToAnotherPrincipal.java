package com.csnt.scdp.bizmodules.modules.scm.purchasereq.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmPurchaseReq;
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  QueryAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 14:13:12
 */

@Scope("singleton")
@Controller("purchasereq-change-to-another-principal")
@Transactional
public class ChangeToAnotherPrincipal extends CommonQueryAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ChangeToAnotherPrincipal.class);

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        String uuid = (String) inMap.get("uuid");
        ScmPurchaseReq scmPurchaseReq = PersistenceFactory.getInstance().findByPK(ScmPurchaseReq.class, uuid);
        scmPurchaseReq.setPrincipal("");
        PersistenceFactory.getInstance().update(scmPurchaseReq);

        //消息推送
        String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
        String templateNo = "SCM_PURCHASER_REQ_CHANGE";
        String sql = "SELECT * FROM V_USER_ROLES T WHERE ROLE_NAME = '供应链部主任'";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List<Map<String, Object>> lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        List<String> userIdLst = new ArrayList<>();
        ReceiptsMeta receiptsMeta = new ReceiptsMeta();
        if (ListUtil.isNotEmpty(lstUserInfo)) {
            for (int i = 0; i < lstUserInfo.size(); i++) {
                String userId = (String) lstUserInfo.get(i).get("userId");
                userIdLst.add(userId);
            }
            receiptsMeta.setLstSendToUserId(userIdLst);
        }
        String principal = UserHelper.getUserName();
        Map map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("code", scmPurchaseReq.getPurchaseReqNo());
        map.put("principal", principal);
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);
        return null;
    }
}
