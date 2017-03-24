package com.csnt.scdp.bizmodules.modules.fad.certificate.action;

import com.csnt.scdp.bizmodules.entity.fad.FadReceiverOrPayerView;
import com.csnt.scdp.bizmodules.entity.scm.ScmContractInvoice;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadReceiverOrPayerViewAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractInvoiceAttribute;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Reese on 2015/10/14.
 */
@Scope("singleton")
@Controller("certificate-receiverOrPayerViewLoad")
@Transactional
public class ReceiverOrPayerViewLoadAction extends CommonLoadAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(ReceiverOrPayerViewLoadAction.class);
    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap<>();
        String receiverOrPayerType = certificateManager.isNullReturnEmpty(inMap.get("receiverOrPayerType"));
        String receiverOrPayerId = certificateManager.isNullReturnEmpty(inMap.get("receiverOrPayerId"));
        String receiverOrPayerCode = certificateManager.isNullReturnEmpty(inMap.get("receiverOrPayerCode"));
        String receiverOrPayerName = certificateManager.isNullReturnEmpty(inMap.get("receiverOrPayerName"));

        //FAD_RECEIVER_OR_PAYER_VIEW(收付款对象视图)
        Map<String, Object> fadReceiverOrPayerViewConditionsMap = new HashMap<String, Object>();
        if (certificateManager.isNullReturnEmpty(receiverOrPayerType).length() > 0) {
            fadReceiverOrPayerViewConditionsMap.put(FadReceiverOrPayerViewAttribute.RECEIVER_OR_PAYER_TYPE, receiverOrPayerType);
        }
        if (certificateManager.isNullReturnEmpty(receiverOrPayerId).length() > 0) {
            fadReceiverOrPayerViewConditionsMap.put(FadReceiverOrPayerViewAttribute.UUID, receiverOrPayerId);
        }
        if (certificateManager.isNullReturnEmpty(receiverOrPayerCode).length() > 0) {
            fadReceiverOrPayerViewConditionsMap.put(FadReceiverOrPayerViewAttribute.RECEIVER_OR_PAYER_CODE, receiverOrPayerCode);
        }
        if ((certificateManager.isNullReturnEmpty(receiverOrPayerType).equals("SCM_SUPPLIER")) && (!(certificateManager.isNullReturnEmpty(receiverOrPayerId).length() > 0)) && (!(certificateManager.isNullReturnEmpty(receiverOrPayerCode).length() > 0))) {
            if (certificateManager.isNullReturnEmpty(receiverOrPayerName).length() > 0) {
                fadReceiverOrPayerViewConditionsMap.put(FadReceiverOrPayerViewAttribute.RECEIVER_OR_PAYER_NAME, receiverOrPayerName);
            }
        }
        List<FadReceiverOrPayerView> fadReceiverOrPayerViewList = ((certificateManager.isNullReturnEmpty(receiverOrPayerType).length() > 0) && (certificateManager.isNullReturnEmpty(receiverOrPayerId).length() > 0 || certificateManager.isNullReturnEmpty(receiverOrPayerCode).length() > 0 || certificateManager.isNullReturnEmpty(receiverOrPayerName).length() > 0)) ? PersistenceFactory.getInstance().findByAnyFields(FadReceiverOrPayerView.class, fadReceiverOrPayerViewConditionsMap, null) : new ArrayList<>();
        if (fadReceiverOrPayerViewList.size() > 0) {
            FadReceiverOrPayerView fadReceiverOrPayerView = fadReceiverOrPayerViewList.get(0);
            result.put("receiverOrPayerNcCode", certificateManager.isNullReturnEmpty(fadReceiverOrPayerView.getNcCode()));
            result.put("receiverOrPayerId", certificateManager.isNullReturnEmpty(fadReceiverOrPayerView.getUuid()));
            result.put("receiverOrPayerCode", certificateManager.isNullReturnEmpty(fadReceiverOrPayerView.getReceiverOrPayerCode()));
            result.put("receiverOrPayerName", certificateManager.isNullReturnEmpty(fadReceiverOrPayerView.getReceiverOrPayerName()));
            result.put("departmentCode", certificateManager.isNullReturnEmpty(fadReceiverOrPayerView.getDepartmentCode()));
        }

        return result;
    }
}
