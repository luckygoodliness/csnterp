package com.csnt.scdp.bizmodules.modules.mobileservice.scm.scmcontractchange;

import com.csnt.scdp.bizmodules.attributes.ErpMobileModulePathAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileTerminalAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.*;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpI18NHelper;
import com.csnt.scdp.bizmodules.modules.mobileservice.ERPMobileTerminalBaseVariableCollectionAction;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalQuery;
import com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.dto.ScmContractChangeDDto;
import com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.dto.ScmContractChangeDto;
import com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.services.impl.ScmcontractchangeManagerImpl;
import com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.services.intf.ScmcontractchangeManager;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.ObjectUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by xiezf on 2016/9/14.
 * SCM_CONTRACT_CHANGE_MODULEPATH
 * 采购变更申请
 */
@Scope("singleton")
@Controller("mobile-terminal-query-scm_purchase_change")
@Transactional
public class ScmContractChangeQuery extends ERPMobileTerminalBaseVariableCollectionAction {

    @Resource(name = "mobile-terminal-query")
    private MobileTerminalQuery mobileTerminalQuery;

    private BigDecimal originalValueTotal;
    private BigDecimal newValueTotal;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new LinkedHashMap<>();//返回结果
        Map itemMap = new LinkedHashMap<>();//主子表数据
        Map extraGridMap = new LinkedHashMap<>();//子表数据
        Map basePojoMap = super.perform(inMap);
        String workFlowDefinitionKey = (String) inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
        BasePojo basePojo = (BasePojo) basePojoMap.get(ErpMobileTerminalAttribute.BASE_POJO);
        ScmContractChangeDto scmContractChangeDto = (ScmContractChangeDto) basePojo;

        originalValueTotal = BigDecimal.valueOf(0.0);
        newValueTotal = BigDecimal.valueOf(0.0);

        ScmcontractchangeManager scmcontractchangeManager = new ScmcontractchangeManagerImpl();
        Map contractInfo = scmcontractchangeManager.getContractInfo(scmContractChangeDto.getScmContractId());

        //2.设置负责人的显示
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        String createByName = "";
        String createByCode = scmContractChangeDto.getCreateBy();
        if (StringUtil.isNotEmpty(createByCode)) {
            Map paramMap = new HashMap<>();
            paramMap.put(ScdpUserAttribute.USER_ID, createByCode);
            List<ScdpUser> list = pcm.findByAnyFields(ScdpUser.class, paramMap, null);
            if (ListUtil.isNotEmpty(list)) {
                createByName = list.get(0).getUserName();
            }
        }

        Map hostMap = new LinkedHashMap<>();
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractChangeAttribute.SCM_CONTRACT_ID, ErpMobileModulePathAttribute.SCM_CONTRACT_CHANGE_MODULEPATH), contractInfo.get("scmContractCode"));
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractChangeAttribute.FAD_SUBJECT_CODE, ErpMobileModulePathAttribute.SCM_CONTRACT_CHANGE_MODULEPATH), contractInfo.get("fadSubjectCode"));
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractChangeAttribute.FAD_SUBJECT_NAME, ErpMobileModulePathAttribute.SCM_CONTRACT_CHANGE_MODULEPATH), contractInfo.get("fadSubjectName"));
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractChangeAttribute.SUPPLIER_NAME, ErpMobileModulePathAttribute.SCM_CONTRACT_CHANGE_MODULEPATH), contractInfo.get("supplierName"));
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractChangeAttribute.ORIGINAL_VALUE, ErpMobileModulePathAttribute.SCM_CONTRACT_CHANGE_MODULEPATH), MathUtil.formatDecimalToString(scmContractChangeDto.getOriginalValue(), "##,##0.00"));
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractChangeAttribute.NEW_VALUE, ErpMobileModulePathAttribute.SCM_CONTRACT_CHANGE_MODULEPATH), MathUtil.formatDecimalToString(scmContractChangeDto.getNewValue(), "##,##0.00"));
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractChangeAttribute.CLOSE_CHANGE, ErpMobileModulePathAttribute.SCM_CONTRACT_CHANGE_MODULEPATH), "1".equals(scmContractChangeDto.getCloseChange())? "是":"否");
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractChangeAttribute.CREATE_BY, ErpMobileModulePathAttribute.SCM_CONTRACT_CHANGE_MODULEPATH), createByName);
        hostMap.put(ErpI18NHelper.englishToChinese(ScmContractChangeAttribute.CHANGE_REASON, ErpMobileModulePathAttribute.SCM_CONTRACT_CHANGE_MODULEPATH), scmContractChangeDto.getChangeReason());

        extraGridMap.put(ErpMobileTerminalAttribute.EXTRA_GRID, this.collectExtraGrid(scmContractChangeDto));

        hostMap.put("变更前意向总价",
                MathUtil.formatDecimalToString(originalValueTotal, "##,##0.00"));
        hostMap.put("变更后意向总价",
                MathUtil.formatDecimalToString(newValueTotal, "##,##0.00"));

        itemMap.putAll(hostMap);
        itemMap.putAll(extraGridMap);

        out.put(ErpMobileTerminalAttribute.ITEM, itemMap);

        //附件表
        out.putAll(this.collectAttachemtVariable(scmContractChangeDto));

        out.put(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE, scmContractChangeDto.getOfficeId());

        return out;
    }

    private Map collectExtraGrid(ScmContractChangeDto scmContractChangeDto){
        Map resultMap = new HashMap<>();

        BigDecimal originalTotal = BigDecimal.valueOf(0);
        BigDecimal newTotal = BigDecimal.valueOf(0);
        BigDecimal backTotal = BigDecimal.valueOf(0);

        String sql = " SELECT NVL(T.PL_AMOUNT - T.PR_AMOUNT+NVL(T.HANDLE_AMOUNT,0), 0) AS PLAN_AMOUNT, T.*, PK.PACKAGE_NAME\n" +
                "  FROM (select SCCD.*,\n" +
                "               NVL(PL.AMOUNT, 0) AS PL_AMOUNT,\n" +
                "               NVL((SELECT SUM(PR.HANDLE_AMOUNT)\n" +
                "                     FROM PRM_PURCHASE_REQ_DETAIL PR\n" +
                "                    WHERE PR.PRM_PURCHASE_PLAN_DETAIL_ID = PL.UUID\n" +
                "                      AND (PR.ISFALLBACK <> '1' OR PR.ISFALLBACK IS NULL)),\n" +
                "                   0) AS PR_AMOUNT\n" +
                "          from SCM_CONTRACT_CHANGE_D SCCD\n" +
                "          LEFT JOIN  PRM_PURCHASE_PLAN_DETAIL PL \n" +
                "          ON PL.UUID = SCCD.PRM_PURCHASE_PLAN_DETAIL_ID\n" +
                "         WHERE SCCD.scm_Contract_Change_Id = ?) T\n" +
                "           left join PRM_PURCHASE_PACKAGE PK on T.PURCHASE_PACKAGE_ID = PK.UUID\n" +
                "            left join PRM_PURCHASE_REQ R on R.UUID = T.PRM_PURCHASE_REQ_ID";
        List lstParams = new ArrayList<>();
        lstParams.add(scmContractChangeDto.getUuid());
        DAOMeta daoMeta1 = new DAOMeta(sql,lstParams );
        List lst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta1);

        List<Map> scmContractChangeDDtoInfo = new ArrayList<>();
        List<ScmContractChangeDDto> scmContractChangeDDtoList = scmContractChangeDto.getScmContractChangeDDto();
        if (ListUtil.isNotEmpty(scmContractChangeDDtoList)) {
            for (ScmContractChangeDDto scmContractChangeDDto : scmContractChangeDDtoList) {
                Map map = new LinkedHashMap<>();
                map.put("状态", FMCodeHelper.getDescByCode(scmContractChangeDDto.getChangeState(), "SCM_CHANGE_STATE") );
                if (ListUtil.isNotEmpty(lst)){
                    for(Object obj : lst){
                        HashMap m = (HashMap) obj;
                        if (m.get("uuid").toString().equals(scmContractChangeDDto.getUuid())) {
                            map.put("包名",m.get("packageName"));
                            map.put("序号",scmContractChangeDDto.getSerialNumber());
                            map.put("采购预算(元)",MathUtil.formatDecimalToString(scmContractChangeDDto.getPurchaseBudgetMoney(), "##,##0.00"));
                            map.put("可申请数量",m.get("planAmount"));
                            break;
                        }
                    }
                }

                map.put("名称",scmContractChangeDDto.getName());
                map.put("型号",scmContractChangeDDto.getModel());
                map.put("厂家",scmContractChangeDDto.getFactory());
                map.put("数量",scmContractChangeDDto.getAmount());
                map.put("预算单价",MathUtil.formatDecimalToString(scmContractChangeDDto.getBudgetPrice(), "##,##0.00"));
                map.put("意向单价",MathUtil.formatDecimalToString(scmContractChangeDDto.getExpectedPrice(), "##,##0.00"));
                scmContractChangeDDtoInfo.add(map);

                if (scmContractChangeDDto.getChangeState().equals("0")) {
                    originalTotal = originalTotal.add(scmContractChangeDDto.getAmount().multiply(scmContractChangeDDto.getBudgetPrice()));
                } else if (scmContractChangeDDto.getChangeState().equals("1")) {
                    newTotal = newTotal.add(scmContractChangeDDto.getAmount().multiply(scmContractChangeDDto.getBudgetPrice()));
                } else if (scmContractChangeDDto.getChangeState().equals("2")) {
                    backTotal = backTotal.add(scmContractChangeDDto.getAmount().multiply(scmContractChangeDDto.getBudgetPrice()));
                }
            }
        }
        resultMap.put("合同明细", scmContractChangeDDtoInfo);
        originalValueTotal = originalTotal.add(backTotal);
        newValueTotal = originalTotal.add(newTotal);
        return resultMap;
    }

    private Map collectAttachemtVariable(ScmContractChangeDto scmContractChangeDto) {

        Map resultMap = new HashMap<>();
        List<Map> attachmentInfo = new ArrayList<Map>();
        List<CdmFileRelationDto> cdmFileRelationDtoList = scmContractChangeDto.getCdmFileRelationDto();
        resultMap.putAll(mobileTerminalQuery.getAttachmentsInfo(cdmFileRelationDtoList));
        return resultMap;
    }
}
