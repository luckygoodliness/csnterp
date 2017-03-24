package com.csnt.scdp.bizmodules.modules.prm.prmunknownreceipts.services.impl;

import com.csnt.scdp.bizmodules.modules.prm.prmunknownreceipts.services.intf.PrmunknownreceiptsManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  PrmunknownreceiptsManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 16:52:16
 */

@Scope("singleton")
@Service("prmunknownreceipts-manager")
public class PrmunknownreceiptsManagerImpl implements PrmunknownreceiptsManager {

    @Override
    public void sendMsg(String uuid,String receiptNo, String money, String payer) {
        String sql = "SELECT * FROM V_USER_ROLES T WHERE ROLE_NAME ='事业部行政主管'";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List<Map<String, Object>> lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        List<String> userIdLst = new ArrayList<String>();
        ReceiptsMeta receiptsMeta = new ReceiptsMeta();
        if (ListUtil.isNotEmpty(lstUserInfo)) {
            for (int i = 0; i < lstUserInfo.size(); i++) {
                String userId = (String) lstUserInfo.get(i).get("userId");
                userIdLst.add(userId);
            }
            receiptsMeta.setLstSendToUserId(userIdLst);
        }
        String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
        String templateNo = "PRM_UNKNOWN_RECEIPT";
        Map map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("receiptNo", receiptNo);
        map.put("money", money);
        map.put("payer", payer);
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);

    }
}