package com.csnt.scdp.bizmodules.modules.fad.payreq.action;

import com.csnt.scdp.bizmodules.attributes.MessageTemplateNo;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqC;
import com.csnt.scdp.bizmodules.entity.fad.FadPayReqH;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadPayReqHAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.fad.payreq.services.intf.PayreqManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created on 2016/8/3.
 */
@Scope("singleton")
@Controller("payreq-msg")
@Transactional
public class SendMsgAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(SendMsgAction.class);

    @Resource(name = "payreq-manager")
    private PayreqManager payreqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String action = (String) inMap.get("action");
        String uuid = (String) inMap.get("uuid");
        List fadUUids = (List) inMap.get("fadUUids");
        if (StringUtil.isNotEmpty(action)) {
            FadPayReqH reqH = payreqManager.getFadPayReqHbyUuid(uuid);
            switch (action) {
                case "confirmationOfArrival":
                    sendMsgForConfirmationOfArrival(reqH, fadUUids);
                    break;
            }
        }
        return rsMap;
    }


    private void sendMsgForConfirmationOfArrival(FadPayReqH reqH, List fadUUids) {
        if (ListUtil.isNotEmpty(fadUUids)) {
            List<FadPayReqC> reqcList = payreqManager.getFadPayReqCbyUuids(fadUUids);
            if (ListUtil.isNotEmpty(reqcList)) {
                // 针对集合里的数据，若到货确认金额小于已付金额+（本次支付金额-本次已申请金额）
                String filerUuid = StringUtil.joinForSqlIn(fadUUids, ",");
                String sql = "SELECT REQ.*,\n" +
                        "       S.SCM_PAID_MONEY,\n" +
                        "       S.CONTRACT_NATURE,\n" +
                        "       S.SCM_CONTRACT_CODE,\n" +
                        "       M.PROJECT_MANAGER,\n" +
                        "       M.PROJECT_NAME,\n" +
                        "       M.PROJECT_CODE,\n" +
                        "       A.CHECKED_MONEY\n" +
                        "  FROM (SELECT * FROM FAD_PAY_REQ_C C WHERE uuid in (" + filerUuid + ")) REQ\n" +
                        "  LEFT JOIN VW_SCM_CONTRACT_EXECUTE_SIMPLE S\n" +
                        "    ON REQ.SCM_CONTRACT_ID = S.UUID\n" +
                        "  LEFT JOIN PRM_PROJECT_MAIN M\n" +
                        "    ON S.PROJECT_ID = M.UUID\n" +
                        "  LEFT JOIN VW_SCM_CONTRACT_ARRIVAL A\n" +
                        "    ON S.UUID = A.SCM_CONTRACT_ID\n" +
                        " WHERE S.CONTRACT_NATURE = '0'\n" +
                        "   AND NVL(REQ.AUDIT_MONEY, 0) - NVL(REQ.CASH_REQ_VALUE, 0) >\n" +
                        "       NVL(A.CHECKED_MONEY,0) ";
                DAOMeta daoMeta = new DAOMeta();
                daoMeta.setNeedFilter(false);
                daoMeta.setStrSql(sql);
                List<Map<String, Object>> scms = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                if (ListUtil.isNotEmpty(scms)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                    scms.stream().forEach(t -> {
                        List<String> userIdLst = new ArrayList<String>();
                        String receipt = "";
                        String projectManager = (String) t.get("projectManager");
                        String createBy = (String) t.get("createBy");
                        if (StringUtil.isNotEmpty(projectManager)) {
                            receipt = projectManager;
                        } else {
                            receipt = createBy;
                        }
                        userIdLst.add(receipt);
                        if (StringUtil.isNotEmpty(receipt)) {
                            ReceiptsMeta receiptsMeta = new ReceiptsMeta();
                            receiptsMeta.setLstSendToUserId(userIdLst);
                            String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
                            String templateNo = MessageTemplateNo.IMSG_PRM_CONFIRM_ARRIVAL;
                            Map map = new HashMap<>();

                            String operator = ErpUserHelper.getUserNameById(createBy);

                            if (createBy.equals(projectManager)) {
                                map.put("operator", "您");
                            } else {
                                map.put("operator", operator);
                            }

                            if (t.get("createTime") != null) {
                                String createTime = "";
                                try {
                                    createTime = sdf.format(t.get("createTime"));
                                } catch (Exception e) {
                                }
                                map.put("operateTime", createTime);
                            }

                            if (StringUtil.isNotEmpty(reqH.getReqType())) {
                                String type = "支付";
                                if (FadPayReqHAttribute.REQ_TYPE_MONTH.toString().equals(reqH.getReqType())) {
                                    type = "月度支付";
                                } else if (FadPayReqHAttribute.REQ_TYPE_TEMP.toString().equals(reqH.getReqType())) {
                                    type = "临时支付";
                                }
                                map.put("payType", type);
                            }
                            String projectCode = (String) t.get("projectCode");
                            if (StringUtil.isNotEmpty(projectCode)) {
                                map.put("projectCode", projectCode);
                            }
                            String projectName = (String) t.get("projectName");
                            if (StringUtil.isNotEmpty(projectName)) {
                                map.put("projectName", projectName);
                            }
                            String runningNo = reqH.getReqno();
                            if (StringUtil.isNotEmpty(runningNo)) {
                                map.put("runningNo", runningNo);
                            }
                            MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);
                        }
                    });
                }
            }

        }
    }
}
