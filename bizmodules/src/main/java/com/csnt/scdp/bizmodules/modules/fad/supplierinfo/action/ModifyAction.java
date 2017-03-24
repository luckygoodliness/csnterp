package com.csnt.scdp.bizmodules.modules.fad.supplierinfo.action;

import com.csnt.scdp.bizmodules.modules.fad.supplierinfo.dto.FadSupplierDto;
import com.csnt.scdp.bizmodules.modules.fad.supplierinfo.services.intf.SupplierinfoManager;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.services.intf.SupplierinfochangeManager;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSupplierBankDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.CommonModifyAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.BeanUtil;
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
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-12-10 13:50:35
 */

@Scope("singleton")
@Controller("supplierinfo-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

    @Resource(name = "supplierinfo-manager")
    private SupplierinfoManager supplierinfoManager;
    @Resource(name = "supplierinfor-manager")
    private SupplierinforManager supplierinforManager;
    @Resource(name = "supplierinfochange-manager")
    private SupplierinfochangeManager supplierinfochangeManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);

        String mainTabName = StringUtil.capFirst(BeanFactory.getClass(dtoClass).getSimpleName(), false);

        Map mapInput = (Map) viewMap.get(mainTabName);
        Map out = super.perform(inMap);
        //Do After
        return out;
    }
    @Override
    protected void beforeAction(BasePojo dtoObj) {
        FadSupplierDto fadSupplierDto = (FadSupplierDto) dtoObj;
        supplierinforManager.supplierNameValidate(fadSupplierDto.getCompleteName(),fadSupplierDto.getSimpleName(),fadSupplierDto.getSupplierCode());
        Map mapSupplierCode = new HashMap();
        List<String> bankId = new ArrayList<String>();
        List<ScmSupplierBankDto> scmSupplierBankCDtoList = fadSupplierDto.getScmSupplierBankDto();
        if (ListUtil.isNotEmpty(scmSupplierBankCDtoList)) {
            for (int i = 0; i < scmSupplierBankCDtoList.size(); i++) {
                if (StringUtil.isNotEmpty(scmSupplierBankCDtoList.get(i).getAccountNo())) {
                    if (!(mapSupplierCode.containsKey(scmSupplierBankCDtoList.get(i).getAccountNo().toString()))) {
                        mapSupplierCode.put(scmSupplierBankCDtoList.get(i).getAccountNo().toString(), scmSupplierBankCDtoList.get(i).getAccountNo().toString());
                        bankId.add(scmSupplierBankCDtoList.get(i).getAccountNo().toString());

                    } else {
                        throw new BizException("银行账号不能重复！");
                    }
                }
            }
            if (bankId.size()>0) {
                supplierinfochangeManager.bankValidate(bankId, fadSupplierDto.getUuid());
            }
        }
        supplierinforManager.fadSupplierMappingChange(fadSupplierDto.getUuid(), fadSupplierDto.getCompleteName());
    }
}
