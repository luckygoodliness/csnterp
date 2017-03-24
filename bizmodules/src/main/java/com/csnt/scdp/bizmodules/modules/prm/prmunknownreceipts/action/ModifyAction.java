package com.csnt.scdp.bizmodules.modules.prm.prmunknownreceipts.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entity.prm.PrmUnknownReceipts;
import com.csnt.scdp.framework.attributes.CommonAttribute;
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
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.prm.prmunknownreceipts.services.intf.PrmunknownreceiptsManager;

import javax.annotation.Resource;
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
 * @timestamp 2015-09-26 16:52:15
 */

@Scope("singleton")
@Controller("prmunknownreceipts-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

    @Resource(name = "prmunknownreceipts-manager")
    private PrmunknownreceiptsManager prmunknownreceiptsManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);
        Map prmUnknownReceiptsDto = (Map) viewMap.get("prmUnknownReceiptsDto");
        Map out = super.perform(inMap);
        //Do After
        String uuid = (String) prmUnknownReceiptsDto.get("uuid");
        PrmUnknownReceipts prmUnknownReceipts= PersistenceFactory.getInstance().findByPK(PrmUnknownReceipts.class,uuid);
        String payer=prmUnknownReceipts.getPayer();



        return out;
    }
    @Override
    protected void afterAction(BasePojo dtoObj) {
        super.afterAction(dtoObj);
        Map map=BeanUtil.bean2Map(dtoObj);
        String uuid = (String) map.get("uuid");
        String payer = (String) map.get("payer");
        String payerDesc =null;
        String receiptNo = (String) map.get("receiptNo");
        String money=String.valueOf(map.get("money"));

        if(StringUtil.isNotEmpty(payer)){
            PrmCustomer prmCustomer=PersistenceFactory.getInstance().findByPK(PrmCustomer.class,payer);
            payerDesc=prmCustomer.getCustomerName();
        }
        prmunknownreceiptsManager.sendMsg(uuid,receiptNo,money,payerDesc);
    }
}
