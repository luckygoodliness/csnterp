package com.csnt.scdp.bizmodules.modules.prm.purchaseplan.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmMaterialClass;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchasePackageAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchasePackageRecordAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmMaterialClassAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePackageDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePackageRecordDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.services.intf.PurchaseplanManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.fasterxml.jackson.databind.deser.Deserializers;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lenovo on 2016/9/8.
 */
@Scope("singleton")
@Controller("show-revise-record")
@Transactional
public class ShowReviseRecord implements IAction {


    @Resource(name = "purchaseplan-manager")
    private PurchaseplanManager purchaseplanManager;

    @Override
    public Map perform(Map map) throws BizException, SysException {
        Map result = new HashMap<>();
        String uuid = (String) map.get(PrmPurchasePackageRecordAttribute.UUID);
        if (StringUtil.isEmpty(uuid)) {
            throw new BizException("请选择一条数据");
        }
        Map condition = new HashMap<>();
        condition.put(PrmPurchasePackageRecordAttribute.PRM_PURCHASE_PACKAGE_ID, uuid);
        List<BasePojo> basePojos = DtoHelper.findByAnyFields(PrmPurchasePackageRecordDto.class, condition, PrmPurchasePackageRecordAttribute.REVISED_TIME);
        List<PrmPurchasePackageRecordDto> prmPurchasePackageRecordDtos = new ArrayList<>();
        if (ListUtil.isNotEmpty(basePojos)) {
            List<String> lstMaterialClassCode = new ArrayList<>();
            for (BasePojo basePojo : basePojos) {
                PrmPurchasePackageRecordDto prmPurchasePackageRecordDto = (PrmPurchasePackageRecordDto) basePojo;
                String materialClassCode = prmPurchasePackageRecordDto.getMaterialClassCode();
                if (StringUtil.isNotEmpty(materialClassCode)) {
                    lstMaterialClassCode.add(materialClassCode);
                }
            }
            Map condition1 = new HashMap<>();
            condition.put(ScmMaterialClassAttribute.CODE, lstMaterialClassCode);
            List<ScmMaterialClass> lstMaterialClass = PersistenceFactory.getInstance().findByAnyFields(ScmMaterialClass.class, condition1, null);
            for (BasePojo basePojo : basePojos) {
                PrmPurchasePackageRecordDto prmPurchasePackageRecordDto = (PrmPurchasePackageRecordDto) basePojo;
                if (ListUtil.isNotEmpty(lstMaterialClass)) {
                    for (ScmMaterialClass materialClass : lstMaterialClass) {
                        if (materialClass.getCode().equals(prmPurchasePackageRecordDto.getMaterialClassCode())) {
                            prmPurchasePackageRecordDto.setMaterialClassCodeDesc(materialClass.getName());
                        }
                    }
                }
                String revisedBy = prmPurchasePackageRecordDto.getRevisedBy();
                ScdpUser user = ErpUserHelper.findUserByUserId(revisedBy);
                if (user != null) {
                    String userName = user.getUserName();
                    prmPurchasePackageRecordDto.setRevisedByDesc(userName);
                }
                prmPurchasePackageRecordDtos.add(prmPurchasePackageRecordDto);
            }
        }
        result.put("prmPurchasePackageRecordDtos", prmPurchasePackageRecordDtos);
        return result;
    }
}
