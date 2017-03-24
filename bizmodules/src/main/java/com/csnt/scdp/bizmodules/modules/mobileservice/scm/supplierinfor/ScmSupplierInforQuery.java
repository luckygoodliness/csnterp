package com.csnt.scdp.bizmodules.modules.mobileservice.scm.supplierinfor;

import com.csnt.scdp.bizmodules.attributes.ErpMobileModulePathAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileTerminalAttribute;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectBudgetAjustCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectBudgetAjustHAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierBankAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierContactsAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierContactsCAttribute;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpI18NHelper;
import com.csnt.scdp.bizmodules.modules.fad.cashreq.dto.FadCashReqDto;
import com.csnt.scdp.bizmodules.modules.mobileservice.ERPMobileTerminalBaseVariableCollectionAction;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalQuery;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustCDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustHDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSupplierBankDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSupplierContactsDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSupplierDto;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by xiezf on 2016/9/13.
 * SCM_SUPPLIER_INFOR_MODULEPATH
 * 供方新增
 */
@Scope("singleton")
@Controller("mobile-terminal-query-scm_supplier_apporve")
@Transactional
public class ScmSupplierInforQuery extends ERPMobileTerminalBaseVariableCollectionAction {

    @Resource(name = "mobile-terminal-query")
    private MobileTerminalQuery mobileTerminalQuery;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        Map out = new LinkedHashMap<>();
        Map itemMap = new LinkedHashMap<>();
        Map extraGridMap = new LinkedHashMap<>();

        Map basePojoMap = super.perform(inMap);
        BasePojo basePojo = (BasePojo) basePojoMap.get(ErpMobileTerminalAttribute.BASE_POJO);
        ScmSupplierDto scmSupplierDto = (ScmSupplierDto) basePojo;

        Map hostMap = new LinkedHashMap<>();
        hostMap.put(ErpI18NHelper.englishToChinese(ScmSupplierAttribute.TAX_REGISTRATION_NO, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH), scmSupplierDto.getTaxRegistrationNo());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmSupplierAttribute.COMPLETE_NAME, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH), scmSupplierDto.getCompleteName());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmSupplierAttribute.AP_NAME, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH), scmSupplierDto.getApName());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmSupplierAttribute.FIXED_ASSET, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH), scmSupplierDto.getFixedAsset());
        hostMap.put(ErpI18NHelper.englishToChinese(ScmSupplierAttribute.TAX_TYPES, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH), FMCodeHelper.getDescByCode(scmSupplierDto.getTaxTypes(), "SCM_SUPPLIER_TAX_TYPES"));
        hostMap.put(ErpI18NHelper.englishToChinese(ScmSupplierAttribute.SUPPLIER_GENRE, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH), FMCodeHelper.getDescByCode(scmSupplierDto.getSupplierGenre(), "SCM_SUPPLIER_GENRE") );
        itemMap.putAll(hostMap);

        //填充子表 scmSupplierContactsDto,scmSupplierBankDto
        extraGridMap.put(ErpMobileTerminalAttribute.EXTRA_GRID, this.collectExtraGrid(scmSupplierDto));
        itemMap.putAll(extraGridMap);

        //cdmFileRelationDto 附件
        out.putAll(this.collectAttachemtVariable(scmSupplierDto));
        out.put(ErpMobileTerminalAttribute.ITEM, itemMap);

        return out;
    }
    //填充子表 scmSupplierContactsDto,scmSupplierBankDto
    private Map collectExtraGrid(ScmSupplierDto scmSupplierDto){
        Map resultMap = new HashMap<>();

//      scmSupplierContactsDto
        List<Map> scmSupplierContactsDtoInfo = new ArrayList<>();
        List<ScmSupplierContactsDto> scmSupplierContactsDtoList = scmSupplierDto.getScmSupplierContactsDto();
        if (ListUtil.isNotEmpty(scmSupplierContactsDtoList)) {
            for (ScmSupplierContactsDto scmSupplierContactsDto : scmSupplierContactsDtoList) {
                Map map = new LinkedHashMap<>();
                                                                                            //contacts post phone mobilePhone qq weixin email remark
                map.put(ErpI18NHelper.englishToChinese(ScmSupplierContactsAttribute.CONTACTS, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH),scmSupplierContactsDto.getContacts());
                map.put(ErpI18NHelper.englishToChinese(ScmSupplierContactsAttribute.POST, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH),scmSupplierContactsDto.getPost());
                map.put(ErpI18NHelper.englishToChinese(ScmSupplierContactsAttribute.PHONE, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH),scmSupplierContactsDto.getPhone());
                map.put(ErpI18NHelper.englishToChinese(ScmSupplierContactsAttribute.MOBILE_PHONE, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH),scmSupplierContactsDto.getMobilePhone());
                map.put(ErpI18NHelper.englishToChinese(ScmSupplierContactsAttribute.QQ, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH),scmSupplierContactsDto.getQq());
                map.put(ErpI18NHelper.englishToChinese(ScmSupplierContactsAttribute.WEIXIN, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH),scmSupplierContactsDto.getWeixin());
                map.put(ErpI18NHelper.englishToChinese(ScmSupplierContactsAttribute.EMAIL, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH),scmSupplierContactsDto.getEmail());
                map.put(ErpI18NHelper.englishToChinese(ScmSupplierContactsAttribute.REMARK, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH),scmSupplierContactsDto.getRemark());
                scmSupplierContactsDtoInfo.add(map);
            }
        }
        resultMap.put("供应商联系人", scmSupplierContactsDtoInfo);

        //scmSupplierBankDto
        List<Map> scmSupplierBankDtoInfo = new ArrayList<>();
        List<ScmSupplierBankDto> scmSupplierBankDtoList = scmSupplierDto.getScmSupplierBankDto();
        if (ListUtil.isNotEmpty(scmSupplierBankDtoList)) {
            for (ScmSupplierBankDto scmSupplierBankDto : scmSupplierBankDtoList) {
                Map map = new LinkedHashMap<>();
                map.put(ErpI18NHelper.englishToChinese(ScmSupplierBankAttribute.BANK_NAME, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH),scmSupplierBankDto.getBankName());
                map.put(ErpI18NHelper.englishToChinese(ScmSupplierBankAttribute.BANK_ADDRESS, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH),scmSupplierBankDto.getBankAddress());
                map.put(ErpI18NHelper.englishToChinese(ScmSupplierBankAttribute.ACCOUNT_NO, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH),scmSupplierBankDto.getAccountNo());
                map.put(ErpI18NHelper.englishToChinese(ScmSupplierBankAttribute.IS_EFFECT, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH),"1".equals(scmSupplierBankDto.getIsEffect())?"是":"否");
                map.put(ErpI18NHelper.englishToChinese(ScmSupplierBankAttribute.REMARK, ErpMobileModulePathAttribute.SCM_SUPPLIER_INFOR_MODULEPATH),scmSupplierBankDto.getRemark());
                scmSupplierBankDtoInfo.add(map);
            }
        }
        resultMap.put("银行账号", scmSupplierBankDtoInfo);

        return resultMap;
    }

    private Map collectAttachemtVariable(ScmSupplierDto scmSupplierDto) {
        Map resultMap = new HashMap<>();
        List<Map> attachmentInfo = new ArrayList<Map>();
        List<CdmFileRelationDto> cdmFileRelationDtoList = scmSupplierDto.getCdmFileRelationDto();
        resultMap.putAll(mobileTerminalQuery.getAttachmentsInfo(cdmFileRelationDtoList));
        return resultMap;
    }
}
