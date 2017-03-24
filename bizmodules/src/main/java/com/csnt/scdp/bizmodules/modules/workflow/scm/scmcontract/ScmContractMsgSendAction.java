package com.csnt.scdp.bizmodules.modules.workflow.scm.scmcontract;

import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Map;

/**
 * Created by jiabq on 2016/9/2.
 */
@Scope("singleton")
@Controller("scm_contract_send_msg")
@Transactional(Transactional.TxType.REQUIRES_NEW)

public class ScmContractMsgSendAction implements IAction{
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
        String templateNo = "SCM_SUPPLIER_LIMIT_MSG";
        Map map = (Map)inMap.get("map");
        ReceiptsMeta receiptsMeta = (ReceiptsMeta)inMap.get("meta");
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);
        return null;
    }
}
