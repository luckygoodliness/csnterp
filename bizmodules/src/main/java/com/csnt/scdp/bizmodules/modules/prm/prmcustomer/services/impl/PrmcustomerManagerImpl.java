package com.csnt.scdp.bizmodules.modules.prm.prmcustomer.services.impl;

import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmCustomerAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmcustomer.services.intf.PrmcustomerManager;
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
 * Description:  PrmcustomerManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-29 10:33:22
 */

@Scope("singleton")
@Service("prmcustomer-manager")
public class PrmcustomerManagerImpl implements PrmcustomerManager {
    public String createPrmcustomerByName(String name) {
        String uuid = null;
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(PrmCustomerAttribute.CUSTOMER_NAME, name);
        List<PrmCustomer> lstPrmCustomer = PersistenceFactory.getInstance().findByAnyFields(PrmCustomer.class, mapConditions, null);
        if (ListUtil.isNotEmpty(lstPrmCustomer)) {
            uuid = lstPrmCustomer.get(0).getUuid();
        } else {
            PrmCustomer customer = new PrmCustomer();
            customer.setCustomerName(name);
            Object obj = PersistenceFactory.getInstance().insert(customer);
            uuid = (String) obj;
        }
        return uuid;
    }

    public void sendMsg(String uuid, String customerName, String type) {
        String sql = "SELECT * FROM V_USER_ROLES T WHERE ROLE_NAME ='出纳'";
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
        String templateNo = null;
        if (type.equals("noCustomer")) {
            templateNo = "NC_CUSTOMER_INFO_CHECKED";
        } else {
            templateNo = "CUSTOMER_INFO_CHECKED";
        }
        Map map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("customerName", customerName);
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);

    }
}