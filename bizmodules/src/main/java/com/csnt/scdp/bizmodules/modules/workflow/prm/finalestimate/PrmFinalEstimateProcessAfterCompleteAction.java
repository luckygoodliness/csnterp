package com.csnt.scdp.bizmodules.modules.workflow.prm.finalestimate;

import com.csnt.scdp.bizmodules.entity.prm.PrmFinalEstimate;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.modules.prm.prmexamperiod.services.intf.PrmExamPeriodManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiabq on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_final_estimates-after-fixed")
@Transactional

public class PrmFinalEstimateProcessAfterCompleteAction extends ERPWorkFlowBaseVariableCollectionAction {

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map mapResult = new HashMap();
        String uuid = (String) inMap.get("businessKey");
        PrmFinalEstimate prmFinalEstimate = PersistenceFactory.getInstance().findByPK(PrmFinalEstimate.class, uuid);

        if (null != prmFinalEstimate) {
            String squareType = prmFinalEstimate.getSquareType();
            //完工结算检查税金是否修正过了
            if ("1".equals(squareType)) {
                if (StringUtil.isEmpty(prmFinalEstimate.getTaxCorrection())) {
                    throw new BizException("完工结算完结：修正税金为空，请修正税金！");
                }
            } else if ("3".equals(squareType)) {
                if (StringUtil.isEmpty(prmFinalEstimate.getTaxCorrection())) {
                    throw new BizException("项目中止：修正税金为空，请修正税金！");
                }
            }

            if (StringUtil.isNotEmpty(squareType)) {
                String prmMainUuid = prmFinalEstimate.getPrmProjectMainId();
                PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, prmMainUuid);
                if (!squareType.equals(prmProjectMain.getState())) {
                    prmProjectMain.setState(squareType);
                    PersistenceFactory.getInstance().update(prmProjectMain);
                }
            }
            //update square time or examDate
            if (prmFinalEstimate.getSquareDate() == null || prmFinalEstimate.getExamDate() == null) {
                PrmExamPeriodManager prmExamPeriodManager = BeanFactory.getBean("prmexamperiod-manager".toLowerCase());

                if (prmFinalEstimate.getSquareDate() == null) {
                    prmFinalEstimate.setSquareDate(DateUtil.parseDate(DateUtil.formatDate(new Date(), "yyyy-MM-dd"),
                            "yyyy-MM-dd"));
                }
                if (prmFinalEstimate.getExamDate() == null) {
                    Date ExamDate = prmExamPeriodManager.getExamDateFromAppointedDate(new Date());
                    prmFinalEstimate.setExamDate(ExamDate);
                }
                PersistenceFactory.getInstance().update(prmFinalEstimate);
            }

/*            if ("1".equals(squareType)) {
                String roleName = "内部核算管理员";
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
                    MsgSendHelper.sendMsg(BeanUtil.bean2Map(prmFinalEstimate), ScdpMsgAttribute.MSG_TYPE_IMSG,
                            "PRM_FINAL_ESTIMATE_FINISHED_TAX", receiptsMeta);
                }
            }*/
        }

        return mapResult;
    }
}
