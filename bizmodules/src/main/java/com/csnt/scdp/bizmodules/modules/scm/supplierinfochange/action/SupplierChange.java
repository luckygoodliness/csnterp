package com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmSupplierC;
import com.csnt.scdp.bizmodules.modules.scm.scmsupplierlimitchange.services.intf.ScmsupplierlimitchangeManager;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.dto.ScmSupplierBankCDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.dto.ScmSupplierCDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.services.intf.SupplierinfochangeManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.helper.DtoHelper;
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
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("supplierchange-supplierchange")
@Transactional
public class SupplierChange implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(SupplierChange.class);

    @Resource(name = "supplierinfochange-manager")
    private SupplierinfochangeManager supplierinfochangeManager;
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String uuid = (String) inMap.get("uuid");
        ScmSupplierCDto scmSupplierCDto = (ScmSupplierCDto) DtoHelper.findDtoByPK(ScmSupplierCDto.class, uuid);
        Map mapSupplierCode = new HashMap();
        List<String> bankId = new ArrayList<String>();
        List<ScmSupplierBankCDto> scmSupplierBankCDtoList = scmSupplierCDto.getScmSupplierBankCDto();
        if (ListUtil.isNotEmpty(scmSupplierBankCDtoList)) {
            for (int i = 0; i < scmSupplierBankCDtoList.size(); i++) {
                if (!(mapSupplierCode.containsKey(scmSupplierBankCDtoList.get(i).getAccountNo().toString()))) {
                    mapSupplierCode.put(scmSupplierBankCDtoList.get(i).getAccountNo().toString(), scmSupplierBankCDtoList.get(i).getAccountNo().toString());
                    bankId.add(scmSupplierBankCDtoList.get(i).getAccountNo().toString());
                } else {
                    throw new BizException("银行账号不能重复！");
                }
            }
            supplierinfochangeManager.bankValidate(bankId, scmSupplierCDto.getPuuid());
        }

        if (StringUtil.isNotEmpty(uuid)) {
            supplierinfochangeManager.scmSupplierChange(uuid);
        }
        return null;
    }
}
