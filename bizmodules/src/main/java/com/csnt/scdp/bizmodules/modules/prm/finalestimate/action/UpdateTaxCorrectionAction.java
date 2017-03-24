package com.csnt.scdp.bizmodules.modules.prm.finalestimate.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmFinalEstimate;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmFinalEstimateAttribute;
import com.csnt.scdp.bizmodules.modules.prm.finalestimate.services.intf.FinalestimateManager;
import com.csnt.scdp.bizmodules.modules.prm.prmexamperiod.services.intf.PrmExamPeriodManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 20:02:44
 */

@Scope("singleton")
@Controller("final-estimate-update-tax-correction")
@Transactional
public class UpdateTaxCorrectionAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(UpdateTaxCorrectionAction.class);

    @Resource(name = "finalestimate-manager")
    private FinalestimateManager finalestimateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before

        String uuid = (String) inMap.get(CommonAttribute.UUID);
        BigDecimal taxCorrection = new BigDecimal(String.valueOf(inMap.get(PrmFinalEstimateAttribute.TAX_CORRECTION)));
        PrmFinalEstimate finalEstimate = PersistenceFactory.getInstance().findByPK(PrmFinalEstimate.class, uuid);
        if (finalEstimate != null) {
            PrmExamPeriodManager prmExamPeriodManager = BeanFactory.getBean("prmexamperiod-manager".toLowerCase());

            BigDecimal taxDiff = MathUtil.sub(taxCorrection, finalEstimate.getTaxCorrection());
            finalEstimate.setTaxCorrection(taxCorrection);
            finalEstimate.setSquareCost(MathUtil.add(finalEstimate.getSquareCost(), taxDiff));
            finalEstimate.setSquareGrossProfit(MathUtil.sub(finalEstimate.getSquareGrossProfit(), taxDiff));
            if (finalEstimate.getReviseTaxDate() == null) {
                finalEstimate.setReviseTaxDate(DateUtil.parseDate(DateUtil.formatDate(new Date(), "yyyy-MM-dd"),
                        "yyyy-MM-dd"));
            }
            if (finalEstimate.getExamRTaxDate() == null) {
                finalEstimate.setExamRTaxDate(prmExamPeriodManager.getExamDateFromAppointedDate(new Date()));
            }
            PersistenceFactory.getInstance().update(finalEstimate);

            if (BillStateAttribute.CMD_BILL_STATE_APPROVED.equals(finalEstimate.getState())) {
                String roleName = "运管部生产主管";
                String sql = "select * from v_user_roles where role_name=? ";
                List lstParam = new ArrayList();
                lstParam.add(roleName);
                DAOMeta daoMeta = new DAOMeta(sql, lstParam);
                List<Map<String, Object>> lstUser = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                if (ListUtil.isNotEmpty(lstUser)) {
                    List<String> lstUserId = lstUser.stream().map(x -> (String) x.get(ScdpUserAttribute.USER_ID)).collect
                            (Collectors.toList());
                    ReceiptsMeta receiptsMeta = new ReceiptsMeta();
                    receiptsMeta.setLstSendToUserId(lstUserId);
                    MsgSendHelper.sendMsg(BeanUtil.bean2Map(finalEstimate), ScdpMsgAttribute.MSG_TYPE_IMSG,
                            "PRM_SQUARE_UPDATE_APPROVED_TAX", receiptsMeta);
                }
            }
        }
        //Do After
        return new HashMap();
    }
}
