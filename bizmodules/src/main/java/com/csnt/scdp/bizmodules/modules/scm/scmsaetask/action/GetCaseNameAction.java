package com.csnt.scdp.bizmodules.modules.scm.scmsaetask.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmSaeObject;
import com.csnt.scdp.bizmodules.modules.scm.scmsae.dto.ScmSaeDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsae.dto.ScmSaeObjectDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsaecase.dto.ScmSaeCaseDDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsaecase.dto.ScmSaeCaseDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsaetask.dto.ScmSaeFormDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsaetask.dto.ScmSaeTaskDto;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by linzp on 2016/9/2.
 */
@Scope("singleton")
@Controller("scmsaetask-getcasename")
@Transactional
public class GetCaseNameAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(GetCaseNameAction.class);

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map<String, String> resultMap = new HashMap<String,String>();

        String scmSaeTaskId = inMap.get("scmSaeTaskId").toString();
        ScmSaeTaskDto scmSaeTaskDto = (ScmSaeTaskDto) DtoHelper.findDtoByPK(ScmSaeTaskDto.class, scmSaeTaskId);
        // 1、获取 task 表的物料类型
        Integer materialType = scmSaeTaskDto.getMaterialType();
        // 2、获取 form 表的 objId
        List<ScmSaeFormDto> scmSaeFormDto = scmSaeTaskDto.getScmSaeFormDto();
        String objectId = scmSaeFormDto.get(0).getScmSaeObjectId();
        // 3、获取 obj 对象
        ScmSaeObjectDto scmSaeObjectDto = (ScmSaeObjectDto) DtoHelper.findDtoByPK(ScmSaeObjectDto.class, objectId);
        // 4、获取 scm_sae 对象
        ScmSaeDto scmSaeDto = (ScmSaeDto) DtoHelper.findDtoByPK(ScmSaeDto.class, scmSaeObjectDto.getScmSaeId());
        // 5、获取 scm_sae 的 sai_case, wai_case
        List<ScmSaeCaseDDto> scmSaeCaseDDto = null;
        if(materialType == 0) {
            // 6、采购 通过 sai_case 获取 scm_sae_case
            ScmSaeCaseDto scmSaeCaseDto = (ScmSaeCaseDto) DtoHelper.findDtoByPK(ScmSaeCaseDto.class, scmSaeDto.getSaiCase());
            // 7、获取 scm_sae_case_d
            scmSaeCaseDDto = scmSaeCaseDto.getScmSaeCaseDDto();
        } else if(materialType == 1) {
            // 6、外协 通过 wai_case 获取 scm_sae_case
            ScmSaeCaseDto scmSaeCaseDto = (ScmSaeCaseDto) DtoHelper.findDtoByPK(ScmSaeCaseDto.class, scmSaeDto.getWaiCase());
            // 7、获取 scm_sae_case_d
            scmSaeCaseDDto = scmSaeCaseDto.getScmSaeCaseDDto();
        } else {
            // 扩展
        }

        if(ListUtil.isNotEmpty(scmSaeCaseDDto)) {
            for(ScmSaeCaseDDto scmSaeCaseD : scmSaeCaseDDto) {
                resultMap.put("item"+scmSaeCaseD.getSeqNo(), scmSaeCaseD.getEvaluationContent());
            }
        }

        Map<String, Map<String, String>> outMap = new HashMap<String, Map<String,String>> ();
        outMap.put("items", resultMap);

        return outMap;
    }
}
