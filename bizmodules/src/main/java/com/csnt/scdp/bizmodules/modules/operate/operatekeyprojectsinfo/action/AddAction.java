package com.csnt.scdp.bizmodules.modules.operate.operatekeyprojectsinfo.action;

import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.operate.operatekeyprojectsinfo.services.intf.OperatekeyprojectsinfoManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-27 17:35:53
 */

@Scope("singleton")
@Controller("operatekeyprojectsinfo-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "operatekeyprojectsinfo-manager")
    private OperatekeyprojectsinfoManager operatekeyprojectsinfoManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        super.afterAction(dtoObj);
        String sql = "SELECT  T.USER_ID FROM V_USER_ROLES T WHERE ROLE_NAME=?";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List lstParam = new ArrayList();
        lstParam.add("运管部主任");
        daoMeta.setLstParams(lstParam);
        List<Map<String, Object>> lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        List<String> userIdLst=new ArrayList<>();
        ReceiptsMeta receiptsMeta = new ReceiptsMeta();
        if(ListUtil.isNotEmpty(lstUserInfo)){
            for(int i=0;i<lstUserInfo.size();i++){
                String userId=(String)lstUserInfo.get(i).get("userId");
                userIdLst.add(userId);
            }
            receiptsMeta.setLstSendToUserId(userIdLst);
        }
        String msgType= ScdpMsgAttribute.MSG_TYPE_IMSG;
        String templateNo="OPERATE_KEY_PROJECTS_INFO";
        MsgSendHelper.sendMsg(BeanUtil.bean2Map(dtoObj), msgType, templateNo, receiptsMeta);
    }
}
