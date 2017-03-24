package com.csnt.scdp.bizmodules.modules.fad.payreq.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpWorkFlowAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmReceiptsAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpOrgHelper;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqCDto;
import com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqHDto;
import com.csnt.scdp.bizmodules.modules.fad.payreq.services.intf.PayreqManager;
import com.csnt.scdp.bizmodules.modules.prm.receipts.services.intf.ReceiptsManager;
import com.csnt.scdp.bizmodules.modules.scm.prmblacklistmonth.services.intf.PrmblacklistmonthManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import com.csnt.scdp.sysmodules.modules.sys.org.dto.ScdpOrgTreeDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
 * @timestamp 2015-09-26 17:42:24
 */

@Scope("singleton")
@Controller("payreq-month-load")
@Transactional
public class LoadMonthAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadMonthAction.class);

    @Resource(name = "payreq-manager")
    private PayreqManager payreqManager;

    @Resource(name = "receipts-manager")
    private ReceiptsManager receiptsManager;

    @Resource(name = "prmblacklistmonth-manager")
    private PrmblacklistmonthManager prmblacklistmonthManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        String userId = (String) inMap.get("userId");
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        FadPayReqHDto fadPayReqHDto = (FadPayReqHDto) dtoObj;

        if (ListUtil.isNotEmpty(fadPayReqHDto.getFadPayReqCDto())) {
            payreqManager.setObjectPlusInfo(fadPayReqHDto);

            List<FadPayReqCDto> rtn = fadPayReqHDto.getFadPayReqCDto();
            boolean isBizDivision = ErpOrgHelper.isBizDivision2(UserHelper.getOrgCode());
            boolean needFilter = ErpUserHelper.checkIfFadScmNeedFilter(UserHelper.getUserId());
            boolean isLeader = ErpUserHelper.isSelfManageDivisionVp(UserHelper.getUserId());
            String orgCode = UserHelper.getOrgCode();

            List statesAfterCommit = Arrays.asList(BillStateAttribute.FAD_BILL_STATE_SUBMITTED,
                    BillStateAttribute.FAD_BILL_STATE_APPROVED,
                    BillStateAttribute.FAD_BILL_STATE_SENT,
                    BillStateAttribute.FAD_BILL_STATE_CERTIFICATED,
                    BillStateAttribute.FAD_BILL_STATE_SUBMITTED_REJECTED);
            List statesAfterCheck = new ArrayList<>();
            statesAfterCheck.addAll(statesAfterCommit);
            statesAfterCheck.add(BillStateAttribute.FAD_BILL_STATE_CHECKING);

            rtn = rtn.stream().filter(t -> (statesAfterCommit.contains(t.getState()) && !needFilter)
                            || UserHelper.getUserId().equals(t.getCreateBy())
                            || (!isBizDivision) && isLeader && orgCode.equals(t.getDepartmentCode()) && statesAfterCheck.contains(t.getState())
                            || (isBizDivision && orgCode.equals(t.getDepartmentCode()) && statesAfterCheck.contains(t.getState()))
                            || (UserHelper.getUserId().equals("WANGCHENGGANG") && "CSNT_ZNXT".equals(t.getDepartmentCode()) && statesAfterCheck.contains(t.getState()))
            )
                    .collect(Collectors.toList());

            fadPayReqHDto.setFadPayReqCDto(rtn);
            if (ListUtil.isNotEmpty(fadPayReqHDto.getFadPayReqCDto())) {
                List<String> uuids = fadPayReqHDto.getFadPayReqCDto().stream().filter(t -> StringUtil.isNotEmpty(t.getCreateBy())).map(t -> t.getCreateBy()).distinct().collect(Collectors.toList());
                Map<String, ScdpUser> su = ErpUserHelper.findUserByUserIds(uuids);

                List<String> prmUUids = fadPayReqHDto.getFadPayReqCDto().stream().filter(t -> StringUtil.isNotEmpty(t.getCreateBy())).map(t -> t.getProjectMainId()).distinct().collect(Collectors.toList());
                Map<String, Map> pr = receiptsManager.loadPrmAmountAndReceiptsByUUids(prmUUids);

//              修改创建人、合同预计金额、合同实际收款金额
                Map<String, Map> certificateNo = payreqManager.getFadCertificateNo(fadPayReqHDto);

                fadPayReqHDto.getFadPayReqCDto().forEach(t ->
                        {
                            if (StringUtil.isNotEmpty(t.getDepartmentCode())) {
                                ScdpOrgTreeDto d =
                                        OrgHelper.getOrgById(OrgHelper.getOrgIdByCode(t.getDepartmentCode()));
                                if (d != null) {
                                    t.setDepartmentName(d.getShortCode());
                                }
                            }
                            if (su.containsKey(t.getCreateBy())) {
                                ScdpUser user = su.get(t.getCreateBy());
                                t.setCreateByName(user.getUserName());
                            }
                            if (MapUtil.isNotEmpty(pr) && pr.containsKey(t.getProjectMainId())) {
                                BigDecimal projectMoney = (BigDecimal) (pr.get(t.getProjectMainId()).get(PrmProjectMainCAttribute.PROJECT_MONEY));
                                BigDecimal actualMoney = (BigDecimal) (pr.get(t.getProjectMainId()).get(PrmReceiptsAttribute.ACTUAL_MONEY));
                                t.setPrmMoney(projectMoney);
                                t.setPrmActualMoney(actualMoney);
                            }
                            if (MapUtil.isNotEmpty(certificateNo) && certificateNo.containsKey(t.getUuid())) {
                                Map m = certificateNo.get(t.getUuid());
                                if (MapUtil.isNotEmpty(m)) {
                                    t.setCertificateNo(m.get("certificateNo").toString());
                                }
                            }
                        }
                );
            }
        }
    }
}
