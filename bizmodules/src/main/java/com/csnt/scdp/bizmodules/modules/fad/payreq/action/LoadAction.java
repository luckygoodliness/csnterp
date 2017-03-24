package com.csnt.scdp.bizmodules.modules.fad.payreq.action;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmReceiptsAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
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
@Controller("payreq-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "payreq-manager")
    private PayreqManager payreqManager;

    @Resource(name = "receipts-manager")
    private ReceiptsManager receiptsManager;

    @Resource(name = "prmblacklistmonth-manager")
    private PrmblacklistmonthManager prmblacklistmonthManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        //填充子表的合同代码、项目名称、合同金额、合同已付账款
        FadPayReqHDto fadPayReqHDto = (FadPayReqHDto) dtoObj;
        if (ListUtil.isNotEmpty(fadPayReqHDto.getFadPayReqCDto())) {
            payreqManager.setObjectPlusInfo(fadPayReqHDto);

            List<FadPayReqCDto> rtn = fadPayReqHDto.getFadPayReqCDto();
            //在审批未通过之前，事业部不能查看供应链部提交的数据
            fadPayReqHDto.setFadPayReqCDto(rtn);
            if (ListUtil.isNotEmpty(fadPayReqHDto.getFadPayReqCDto())) {
                List<String> uuids = fadPayReqHDto.getFadPayReqCDto().stream().filter(t -> StringUtil.isNotEmpty(t.getCreateBy())).map(t -> t.getCreateBy()).distinct().collect(Collectors.toList());
                Map<String, ScdpUser> su = ErpUserHelper.findUserByUserIds(uuids);

                List<String> prmUUids = fadPayReqHDto.getFadPayReqCDto().stream().filter(t -> StringUtil.isNotEmpty(t.getCreateBy())).map(t -> t.getProjectMainId()).distinct().collect(Collectors.toList());
                Map<String, Map> pr = receiptsManager.loadPrmAmountAndReceiptsByUUids(prmUUids);

                Map<String, Map> certificateNo = payreqManager.getFadCertificateNo(fadPayReqHDto);

                //修改创建人、合同预计金额、合同实际收款金额
                fadPayReqHDto.getFadPayReqCDto().forEach(t ->
                        {
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
