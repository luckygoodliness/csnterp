package com.csnt.scdp.bizmodules.modules.workflow.scm.supplierinfochange;

import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplierLimitChangeD;
import com.csnt.scdp.bizmodules.modules.scm.scmsupplierlimitchange.services.intf.ScmsupplierlimitchangeManager;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.dto.ScmSupplierBankCDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.dto.ScmSupplierCDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.services.intf.SupplierinfochangeManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
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
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_supplier_in_fo_change-after-fixed")
@Transactional

public class ScmSupplierInFoChangeAfterFixedAction extends ERPWorkFlowBaseVariableCollectionAction {
    @Resource(name = "supplierinfochange-manager")
    private SupplierinfochangeManager supplierinfochangeManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map dataInfo = super.perform(inMap);
        Map mapVar = new HashMap<>();
        String uuid = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);

        ScmSupplierCDto scmSupplierCDto = (ScmSupplierCDto) DtoHelper.findDtoByPK(ScmSupplierCDto.class, uuid);
        Map mapSupplierCode = new HashMap();
        List<String> bankId = new ArrayList<String>();
        List<ScmSupplierBankCDto> scmSupplierBankCDtoList = scmSupplierCDto.getScmSupplierBankCDto();
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
                supplierinfochangeManager.bankValidate(bankId, scmSupplierCDto.getPuuid());
            }
        }

        if (StringUtil.isNotEmpty(uuid)) {
            supplierinfochangeManager.scmSupplierChange(uuid);
        }
        Map conditionMap = new HashMap<>();

        return mapVar;
    }

}
