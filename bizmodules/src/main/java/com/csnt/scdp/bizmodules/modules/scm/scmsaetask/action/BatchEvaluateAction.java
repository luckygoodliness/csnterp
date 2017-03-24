package com.csnt.scdp.bizmodules.modules.scm.scmsaetask.action;

import com.csnt.scdp.bizmodules.modules.scm.scmsae.dto.ScmSaeFormDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsaetask.dto.ScmSaeTaskDto;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.helper.DtoHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by linzp on 2016/9/2.
 */
@Scope("singleton")
@Controller("scmsaetask-batchevaluate")
@Transactional
public class BatchEvaluateAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(BatchEvaluateAction.class);

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        String uuids = inMap.get("uuids").toString();
        String[] uuidArr = uuids.split(",");
        String comprehensive = inMap.get("comprehensive").toString();
        String item1 = inMap.get("item1").toString();
        String item2 = inMap.get("item2").toString();
        String item3 = inMap.get("item3").toString();
        String item4 = inMap.get("item4").toString();
        String item5 = inMap.get("item5").toString();
        String item6 = inMap.get("item6").toString();
        String item7 = inMap.get("item7").toString();
        String item8 = inMap.get("item8").toString();
        String item9 = inMap.get("item9").toString();
        String item10 = inMap.get("item10").toString();
        String item11 = inMap.get("item11").toString();
        String item12 = inMap.get("item12").toString();
        String item13 = inMap.get("item13").toString();
        String item14 = inMap.get("item14").toString();
        String item15 = inMap.get("item15").toString();

        for(int i = 0; i < uuidArr.length; i++) {
            ScmSaeFormDto scmSaeFormDto = (ScmSaeFormDto)DtoHelper.findDtoByPK(ScmSaeFormDto.class, uuidArr[i]);
            scmSaeFormDto.setComprehensive(new BigDecimal(comprehensive));

            if(!"MEIYOU".equals(item1)) {
                scmSaeFormDto.setItem1(Integer.valueOf(item1));
            }

            if(!"MEIYOU".equals(item2)) {
                scmSaeFormDto.setItem2(Integer.valueOf(item2));
            }

            if(!"MEIYOU".equals(item3)) {
                scmSaeFormDto.setItem3(Integer.valueOf(item3));
            }

            if(!"MEIYOU".equals(item4)) {
                scmSaeFormDto.setItem4(Integer.valueOf(item4));
            }

            if(!"MEIYOU".equals(item5)) {
                scmSaeFormDto.setItem5(Integer.valueOf(item5));
            }

            if(!"MEIYOU".equals(item6)) {
                scmSaeFormDto.setItem6(Integer.valueOf(item6));
            }

            if(!"MEIYOU".equals(item7)) {
                scmSaeFormDto.setItem7(Integer.valueOf(item7));
            }

            if(!"MEIYOU".equals(item8)) {
                scmSaeFormDto.setItem8(Integer.valueOf(item8));
            }

            if(!"MEIYOU".equals(item9)) {
                scmSaeFormDto.setItem9(Integer.valueOf(item9));
            }

            if(!"MEIYOU".equals(item10)) {
                scmSaeFormDto.setItem10(Integer.valueOf(item10));
            }

            if(!"MEIYOU".equals(item11)) {
                scmSaeFormDto.setItem11(Integer.valueOf(item11));
            }

            if(!"MEIYOU".equals(item12)) {
                scmSaeFormDto.setItem12(Integer.valueOf(item12));
            }

            if(!"MEIYOU".equals(item13)) {
                scmSaeFormDto.setItem13(Integer.valueOf(item13));
            }

            if(!"MEIYOU".equals(item14)) {
                scmSaeFormDto.setItem14(Integer.valueOf(item14));
            }

            if(!"MEIYOU".equals(item15)) {
                scmSaeFormDto.setItem15(Integer.valueOf(item15));
            }

            scmSaeFormDto.setEditflag("*");
            DtoHelper.CascadeSave(scmSaeFormDto);
        }

        return null;
    }
}
