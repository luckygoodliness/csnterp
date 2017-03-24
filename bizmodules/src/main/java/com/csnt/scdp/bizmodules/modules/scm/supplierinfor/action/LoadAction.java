package com.csnt.scdp.bizmodules.modules.scm.supplierinfor.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplierEvaluation;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSupplierDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSupplierEvaluationDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 16:02:00
 */

@Scope("singleton")
@Controller("supplierinfor-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "supplierinfor-manager")
    private SupplierinforManager supplierinforManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        Map scmSupplierDtotMap = (Map)out.get("scmSupplierDto");
        List<Map> ScmSupplierEvaluation = (List)scmSupplierDtotMap.get("scmSupplierEvaluationDto");
        List<String> scmContractId= new ArrayList<>();
        List<ScmContract> reList=new ArrayList<ScmContract>();
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        if (ListUtil.isNotEmpty(ScmSupplierEvaluation)) {
            for (int i = 0; i <ScmSupplierEvaluation.size() ; i++) {
                if(null!=(ScmSupplierEvaluation.get(i).get("scmContractId"))) {
                    scmContractId.add(ScmSupplierEvaluation.get(i).get("scmContractId").toString());
                }
            }
            if (ListUtil.isNotEmpty(scmContractId)) {
                Map paramMap1 = new HashMap<>();
                paramMap1.put("uuid", scmContractId);
                 reList = pcm.findByAnyFields(ScmContract.class, paramMap1, null);
            }
            for (int i = 0; i < ScmSupplierEvaluation.size(); i++) {
                if (StringUtil.isNotEmpty(ScmSupplierEvaluation.get(i).get("evaluateFrom"))) {
                    if ("0".equals(ScmSupplierEvaluation.get(i).get("evaluateFrom"))) {
                        ScmSupplierEvaluation.get(i).put("evaluateFrom","供方管理");
                    } else if ("1".equals(ScmSupplierEvaluation.get(i).get("evaluateFrom"))) {
                        ScmSupplierEvaluation.get(i).put("evaluateFrom","合同编辑");
                    } else if ("2".equals(ScmSupplierEvaluation.get(i).get("evaluateFrom"))) {
                        ScmSupplierEvaluation.get(i).put("evaluateFrom","采购变更");
                    } else if ("3".equals(ScmSupplierEvaluation.get(i).get("evaluateFrom"))) {
                        ScmSupplierEvaluation.get(i).put("evaluateFrom","到货确认");
                    }
                }
                if (ListUtil.isNotEmpty(reList)) {
                    for (int j = 0; j < reList.size(); j++) {
                        if (reList.get(j).getUuid().equals(ScmSupplierEvaluation.get(i).get("scmContractId"))){
                            ScmSupplierEvaluation.get(i).put("scmContractCode",reList.get(j).getScmContractCode());
                        }
                    }
                }
            }
        }
        //Do After
        supplierinforManager.setExtraData(out);
        return out;
    }
}
