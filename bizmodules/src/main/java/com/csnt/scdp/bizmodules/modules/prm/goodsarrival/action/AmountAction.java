package com.csnt.scdp.bizmodules.modules.prm.goodsarrival.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmGoodsArrival;
import com.csnt.scdp.bizmodules.modules.prm.goodsarrival.services.intf.GoodsarrivalManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by John on 2015/11/18.
 */
@Scope("singleton")
@Controller("goodsarrival-amount")
@Transactional
public class AmountAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AmountAction.class);

    @Resource(name = "goodsarrival-manager")
    private GoodsarrivalManager goodsarrivalManager;

    @Override
    //计算剩余到货数量
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new HashMap<>();
        List uuidLst = (List) inMap.get("uuidLst");
        String uuid = "";
        if (ListUtil.isNotEmpty(uuidLst)) {
            for (int i = 0; i < uuidLst.size(); i++) {
                uuid = uuidLst.get(i).toString();
            }
        } else {
            throw new BizException("没有选择记录！");
        }
        String totalAmount = inMap.get("totalAmount")+"";
        List<Map> scmContractDetailList = goodsarrivalManager.getScmContractDetailForUUID(uuid);
        if (ListUtil.isNotEmpty(scmContractDetailList)) {
            for (int i = 0; i < scmContractDetailList.size(); i++) {
                BigDecimal amount = new BigDecimal(scmContractDetailList.get(i).get("amount") + "");
                BigDecimal remainAmount = amount.subtract(new BigDecimal(totalAmount));

                out.put("remainAmount", remainAmount);
            }
        }


        return out;
    }
}
