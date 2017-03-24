package com.csnt.scdp.bizmodules.modules.workflow.scm.scmqualifiedsupplier;

import com.csnt.scdp.bizmodules.entity.scm.ScmSaeApproval;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplierLimitChangeD;
import com.csnt.scdp.bizmodules.modules.scm.scmsaeapproval.dto.ScmSaeApprovalDDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsaeapproval.dto.ScmSaeApprovalDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsupplierlimitchange.services.intf.ScmsupplierlimitchangeManager;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSupplierDto;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
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
@Controller("scm_qualified_supplier-after-fixed")
@Transactional

public class ScmQualifiedSupplierAfterFixedAction extends ERPWorkFlowBaseVariableCollectionAction {

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map dataInfo = super.perform(inMap);
        Map mapVar = new HashMap<>();
        String uuid = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        ScmSaeApprovalDto scmSaeApprovalDto = (ScmSaeApprovalDto) DtoHelper.findDtoByPK(ScmSaeApprovalDto.class, uuid);
        List<ScmSaeApprovalDDto> scmSupplierBankCDtoList = scmSaeApprovalDto.getScmSaeApprovalDDto();
        List<String> list = new ArrayList<String>();
        if (ListUtil.isNotEmpty(scmSupplierBankCDtoList)) {
            for (int i = 0; i < scmSupplierBankCDtoList.size(); i++) {
                list.add(scmSupplierBankCDtoList.get(i).getSupplierId());
            }
            Map conditionMap = new HashMap<>();
            conditionMap.put("uuid", list);
            List<BasePojo> supplierList = DtoHelper.findByAnyFields(ScmSupplierDto.class, conditionMap, null);
            List<BasePojo> dtoUpdateObjs = new ArrayList<BasePojo>();
            for (int i = 0; i < scmSupplierBankCDtoList.size(); i++) {
                for (Object o : supplierList) {
                    ScmSupplierDto dto = (ScmSupplierDto) o;
                    if (scmSupplierBankCDtoList.get(i).getSupplierId().equals(dto.getUuid())) {
                        dto.setSupplierGenre("0");
                        dto.setLevelCode(scmSupplierBankCDtoList.get(i).getSupplierGenre());
                        dto.setEditflag("*");
                        dtoUpdateObjs.add(dto);
                    }
                }
            }
            DtoHelper.batchUpdate(dtoUpdateObjs, ScmSupplier.class);
        }
        return mapVar;
    }
}