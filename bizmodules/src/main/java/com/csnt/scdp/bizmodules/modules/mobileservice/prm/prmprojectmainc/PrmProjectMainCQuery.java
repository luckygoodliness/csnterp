package com.csnt.scdp.bizmodules.modules.mobileservice.prm.prmprojectmainc;

import com.csnt.scdp.bizmodules.attributes.CommonAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileDtoAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileModulePathAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileTerminalAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadCashReqInvoiceAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetDetailCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpI18NHelper;
import com.csnt.scdp.bizmodules.modules.mobileservice.ERPMobileTerminalBaseVariableCollectionAction;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalQuery;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmBudgetDetailCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmProjectMainCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf.PrmprojectmaincManager;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by lijx on 2016/9/12.
 */
@Scope("singleton")
@Controller("mobile-terminal-query-prm_main")
@Transactional
public class PrmProjectMainCQuery  extends ERPMobileTerminalBaseVariableCollectionAction {
    @Resource(name = "mobile-terminal-query")
    private MobileTerminalQuery mobileTerminalQuery;
    @Resource(name = "prmprojectmainc-manager")
    private PrmprojectmaincManager prmprojectmaincManager;
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map out = new LinkedHashMap<>();
        Map itemMap = new LinkedHashMap<>();
        Map extraGridMap = new LinkedHashMap<>();
        inMap.put("dto", ErpMobileDtoAttribute.definitionKey2Dto.get("Prm_Main_Dto"));
        Map basePojoMap = super.perform(inMap);
        String workFlowDefinitionKey = (String) inMap.get(WorkFlowAttribute.WORKFLOW_DEFINITION_KEY);
        BasePojo basePojo = (BasePojo) basePojoMap.get(ErpMobileTerminalAttribute.BASE_POJO);
        PrmProjectMainCDto projectMainCDto = (PrmProjectMainCDto) basePojo;

        prmprojectmaincManager.loadExtraDescField(projectMainCDto);

        String prmCodeType = "PRM_CODE_TYPE";
        if(StringUtil.isNotEmpty(projectMainCDto.getTaxType()))
            prmCodeType = prmCodeType + "_" + projectMainCDto.getTaxType();
        else
            prmCodeType = prmCodeType + "_PP";


        //主表
        Map hostMap = new LinkedHashMap<>();
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.PROJECT_NAME,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), projectMainCDto.getProjectName());//项目名称
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.PROJECT_SHORT_NAME,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), projectMainCDto.getProjectShortName());//项目简称
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.PROJECT_CODE,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), projectMainCDto.getProjectCode());//项目代码
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.CONTRACTOR_OFFICE,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), projectMainCDto.getContractorOfficeDesc());//承担部门
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.PROJECT_MANAGER,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), projectMainCDto.getProjectManagerDesc());//项目负责人
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.SCHEDULED_BEGIN_DATE,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), projectMainCDto.getScheduledBeginDate() == null ? "" : DateUtil.formatDate(projectMainCDto.getScheduledBeginDate(), "yyyy-MM-dd"));//计划开始时间
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.SCHEDULED_END_DATE,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), projectMainCDto.getScheduledEndDate()==null?"": DateUtil.formatDate(projectMainCDto.getScheduledEndDate(), "yyyy-MM-dd"));//计划完成时间
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.PROJECT_MONEY,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), MathUtil.formatDecimalToString(projectMainCDto.getProjectMoney(), "##,##0.00"));//合同运行额
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.COST_CONTROL_MONEY,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), MathUtil.formatDecimalToString(projectMainCDto.getCostControlMoney(), "##,##0.00"));//成本控制总额
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.TAX_TYPE,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), FMCodeHelper.getDescByCode(projectMainCDto.getTaxType(), "CDM_TAX_TYPE"));//税款类别
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.PRM_CODE_TYPE,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), FMCodeHelper.getDescByCode(projectMainCDto.getPrmCodeType(), prmCodeType));//代号类型
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.IS_PRE_PROJECT,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH),  FMCodeHelper.getDescByCode(projectMainCDto.getIsPreProject()==null?"0":projectMainCDto.getIsPreProject().toString(), "IS_PRE_PROJECT"));//预立项
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.IS_MAJOR_PROJECT,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH),FMCodeHelper.getDescByCode(projectMainCDto.getIsMajorProject()==null?"0":projectMainCDto.getIsMajorProject().toString(), "IS_PRE_PROJECT"));//是否重大立项项目
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.STATE,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH),  FMCodeHelper.getDescByCode(projectMainCDto.getState(), "CDM_BILL_STATE"));//流程状态
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.ESTABLISH_DATE,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), projectMainCDto.getEstablishDate() == null ? "" : DateUtil.formatDate(projectMainCDto.getEstablishDate(), "yyyy-MM-dd"));//立项时间
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.PROJECT_SERIAL_NO,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), projectMainCDto.getProjectSerialNo());//立项序号
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.CONTRACT_DURATION,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), projectMainCDto.getContractDuration());//工期
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.FUNDS_EXPLANATION,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), projectMainCDto.getFundsExplanation());//立项经费说明
        hostMap.put(ErpI18NHelper.englishToChinese(PrmProjectMainCAttribute.REMARK,
                ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), projectMainCDto.getRemark());//备注

        itemMap.putAll(hostMap);

        //子表
        extraGridMap.put(ErpMobileTerminalAttribute.EXTRA_GRID,this.collectBudgetDetailCVariable(projectMainCDto));
        itemMap.putAll(extraGridMap);

        out.put(ErpMobileTerminalAttribute.ITEM, itemMap);

        //样例，部门code，如果工作流不是自定义的code，则不需要。。。
        out.put(ErpMobileTerminalAttribute.PROCESS_DEPT_CODE, projectMainCDto.getContractorOffice());

        return out;
    }

    private Map collectBudgetDetailCVariable(PrmProjectMainCDto projectMainCDto) {
        Map resultMap = new HashMap<>();
        List<Map> prmBudgetDetailCInfo = new ArrayList<>();
        List<PrmBudgetDetailCDto> prmBudgetDetailCDtoList = projectMainCDto.getPrmBudgetDetailCDto();
        if (ListUtil.isNotEmpty(prmBudgetDetailCDtoList)) {
            for (PrmBudgetDetailCDto prmBudgetDetailCDto : prmBudgetDetailCDtoList) {
                String sql = "select t.subject_name as codedesc from fad_base_subject t\n" +
                        "where t.subject_type='1' and t.subject_no =?  order by t.seq_no desc";
                List lstParams =  new ArrayList<>();
                lstParams.add(prmBudgetDetailCDto.getBudgetCode());
                DAOMeta daoMeta1 = new DAOMeta(sql,lstParams);
                List<Map<String,Object>> lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta1);
                if(lstResult!=null  && lstResult.size()>0){
                    prmBudgetDetailCDto.setBudgetCode( (String) ((Map)lstResult.get(0)).get("codedesc"));
                }

                Map map = new LinkedHashMap<>();
                map.put(ErpI18NHelper.englishToChinese(PrmBudgetDetailCAttribute.BUDGET_CODE, ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), prmBudgetDetailCDto.getBudgetCode());//预算项代码
                if(prmBudgetDetailCDto.getContractMoney() != null) {
                    map.put(ErpI18NHelper.englishToChinese(PrmBudgetDetailCAttribute.CONTRACT_MONEY, ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), MathUtil.formatDecimalToString(prmBudgetDetailCDto.getContractMoney(), "##,##0.00"));//原合同价
                }
                if(prmBudgetDetailCDto.getCostControlMoney() != null) {
                    map.put(ErpI18NHelper.englishToChinese(PrmBudgetDetailCAttribute.COST_CONTROL_MONEY, ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), MathUtil.formatDecimalToString(prmBudgetDetailCDto.getCostControlMoney(), "##,##0.00"));//成本控制总额
                }
                if(prmBudgetDetailCDto.getVatAmount() != null) {
                    map.put(ErpI18NHelper.englishToChinese(PrmBudgetDetailCAttribute.VAT_AMOUNT, ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), MathUtil.formatDecimalToString(prmBudgetDetailCDto.getVatAmount(), "##,##0.00"));//增值税
                }
                if(prmBudgetDetailCDto.getExcludingVatAmount() != null) {
                    map.put(ErpI18NHelper.englishToChinese(PrmBudgetDetailCAttribute.EXCLUDING_VAT_AMOUNT, ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), MathUtil.formatDecimalToString(prmBudgetDetailCDto.getExcludingVatAmount(), "##,##0.00"));//税前成本
                }
                map.put(ErpI18NHelper.englishToChinese(PrmBudgetDetailCAttribute.EXPLANATION, ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), prmBudgetDetailCDto.getExplanation());//说明
                map.put(ErpI18NHelper.englishToChinese(PrmBudgetDetailCAttribute.REMARK, ErpMobileModulePathAttribute.PRM_MAIN_MODULEPATH), prmBudgetDetailCDto.getRemark());//备注

                prmBudgetDetailCInfo.add(map);
            }

        }
        resultMap.put("立项预算明细", prmBudgetDetailCInfo);
        return resultMap;
    }
}
