package com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.services.impl;

import com.csnt.scdp.bizmodules.attributes.CommonAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.scm.*;
import com.csnt.scdp.bizmodules.entityattributes.scm.PrmBlacklistMonthAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierBankCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierCAttribute;
import com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto;
import com.csnt.scdp.bizmodules.modules.scm.fadsuppliermapping.dto.FadSupplierMappingDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.dto.ScmSukpplierKeyCDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.dto.ScmSupplierBankCDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.dto.ScmSupplierCDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.dto.ScmSupplierContactsCDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.services.intf.SupplierinfochangeManager;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSukpplierKeyDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSupplierBankDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSupplierContactsDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSupplierDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierlimit.dto.ScmSupplierLimitDto;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.framework.util.UUIDUtil;
import com.csnt.scdp.sysmodules.entity.FmCurrency;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:  SupplierinfochangeManagerImpl
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-07-28 17:10:24
 */

@Scope("singleton")
@Service("supplierinfochange-manager")
public class SupplierinfochangeManagerImpl implements SupplierinfochangeManager {
    @Override
    public void setExtraData(Map out) {
        //1.设置显示数据
        Map scmSupplierMap = (Map) out.get("scmSupplierCDto");
        String registeredCapitalCurrency = (String) scmSupplierMap.get("registeredCapitalCurrency");
        String fixedAssetCurrency = (String) scmSupplierMap.get("fixedAssetCurrency");
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        if (StringUtil.isNotEmpty(registeredCapitalCurrency)) {
            Map paramMap1 = new HashMap<>();
            paramMap1.put("currencyCode", registeredCapitalCurrency);
            List reList = pcm.findByAnyFields(FmCurrency.class, paramMap1, null);
            if (ListUtil.isNotEmpty(reList)) {
                scmSupplierMap.put("registeredCapitalCurrencyName", ((FmCurrency) reList.get(0)).getCurrencyDesc());
            }
        }
        if (StringUtil.isNotEmpty(fixedAssetCurrency)) {
            Map paramMap2 = new HashMap<>();
            paramMap2.put("currencyCode", fixedAssetCurrency);
            List reList = pcm.findByAnyFields(FmCurrency.class, paramMap2, null);
            if (ListUtil.isNotEmpty(reList)) {
                scmSupplierMap.put("fixedAssetCurrencyName", ((FmCurrency) reList.get(0)).getCurrencyDesc());
            }
        }
        List<Map> prmBlackMonthList = (List) scmSupplierMap.get("prmBlacklistMonthDto");
        if (ListUtil.isNotEmpty(prmBlackMonthList)) {
            List complainDepartmentList = new ArrayList<>();
            List complainList = new ArrayList<>();
            for (Map map : prmBlackMonthList) {
                String complainDepartment = (String) map.get("prmProjectMainId");
                if (StringUtil.isNotEmpty(complainDepartment)) {
                    complainDepartmentList.add(map.get("prmProjectMainId"));
                }
                String complainant = (String) map.get(PrmBlacklistMonthAttribute.COMPLAINANT);
                if (StringUtil.isNotEmpty(complainant)) {
                    complainList.add(map.get(PrmBlacklistMonthAttribute.COMPLAINANT));
                }
            }
            if (ListUtil.isNotEmpty(complainDepartmentList)) {
                QueryCondition condition = new QueryCondition(CommonAttribute.UUID, "in", null);
                condition.setValueList(complainDepartmentList);
                List paramList = new ArrayList<>();
                paramList.add(condition);
                List<PrmProjectMain> projectList = pcm.findByAnyFields(PrmProjectMain.class, paramList, null);
                Map<String, PrmProjectMain> prmProjectMainMap = new HashMap<String, PrmProjectMain>();
                if (ListUtil.isNotEmpty(projectList)) {
                    projectList.forEach(t -> prmProjectMainMap.put(t.getUuid(), t));
                    prmBlackMonthList.forEach(p -> {
                        if (prmProjectMainMap.containsKey(p.get("prmProjectMainId"))) {
                            PrmProjectMain main = prmProjectMainMap.get(p.get("prmProjectMainId"));
                            p.put("complainDepartmentName", main.getProjectName());
                        }
                    });
                }
            }
            if (ListUtil.isNotEmpty(complainList)) {
                QueryCondition condition = new QueryCondition(ScdpUserAttribute.USER_ID, "in", null);
                condition.setValueList(complainList);
                List paramList = new ArrayList<>();
                paramList.add(condition);
                List<ScdpUser> scdpUserList = pcm.findByAnyFields(ScdpUser.class, paramList, null);
                if (ListUtil.isNotEmpty(scdpUserList)) {
                    for (Map map : prmBlackMonthList) {
                        for (ScdpUser p : scdpUserList) {
                            if (map.get(PrmBlacklistMonthAttribute.COMPLAINANT).equals(p.getUserId())) {
                                map.put(PrmBlacklistMonthAttribute.COMPLAINANT + "Name", p.getUserName());
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    public void cancelEffectSupplier(Map inMap) {
        String uuid = (String) inMap.get(CommonAttribute.UUID);
        if (StringUtil.isNotEmpty(uuid)) {
            List paramList = new ArrayList<>();
            paramList.add(uuid);
            DAOMeta daoMeta = DAOHelper.getDAO("supplierinfochange-dao", "cancel_effect_supplier", paramList);
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            pcm.updateByNativeSQL(daoMeta);
        } else {
            throw new BizException("没有UUID！");
        }
    }

    /**
     * 根据供应商、开户银行、银行账户维护供应商信息
     *
     * @param supplierName 供应商名称
     * @param bank         开户银行
     * @param bankId       银行账户
     * @param flag         0.合格供方（仅供应链部改）：年度合格供方，有名单
     *                     1.普通供方（仅供应链部改）：有合同的供应商，S/w,有资质
     *                     2.零星供方（仅供应链部改）：无合同的供应商，L，无资质，零星报销(零星供方可能是在个人报销时进来的，银行账号允许为空)
     *                     3.报销供方（财务退回---事业部改；供应链部改）：非项目/项目的报销的客商
     *                     4.其他（财务退回---事业部改；供应链部改）：保证金类的请款（属于上家）的客商（当财务/事业部提供资质，供应链部就可以改为普通供方）
     */
    @Override
    public String generateSupplierByFlag(String supplierName, String bank, String bankId, String flag) {
        String uuid = "";
        if (StringUtil.isEmpty(supplierName) || (StringUtil.isEmpty(supplierName.trim()))) {
            throw new BizException("供方名称不能为空！");
        }
        if ("2".equals(flag) && (StringUtil.isEmpty(bankId) || (StringUtil.isEmpty(bankId.trim())))) {//零星供方可能是在个人报销时进来的，银行账号允许为空
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            Map conditionMap = new HashMap<>();
            conditionMap.put(ScmSupplierCAttribute.COMPLETE_NAME, supplierName.trim());
            List<ScmSupplierC> scmSupplierList = pcm.findByAnyFields(ScmSupplierC.class, conditionMap, null);
            if (ListUtil.isNotEmpty(scmSupplierList)) {
                uuid = scmSupplierList.get(0).getUuid();
            } else {
                uuid = UUIDUtil.getUUID();
                ScmSupplierCDto scmSupplierDto = new ScmSupplierCDto();
                scmSupplierDto.setUuid(uuid);
                String reqNo = NumberingHelper.getNumbering("SCM_SUPPLIER_CODE", null);
                scmSupplierDto.setSupplierCode(reqNo);
                scmSupplierDto.setCompleteName(supplierName);
                scmSupplierDto.setSimpleName(supplierName);
                scmSupplierDto.setState("2");
                scmSupplierDto.setSupplierType("2");
                scmSupplierDto.setSupplierGenre(flag);
                scmSupplierDto.setEditflag("+");
                DtoHelper.CascadeSave(scmSupplierDto);
            }
        } else {
            if (StringUtil.isEmpty(bank) || (StringUtil.isEmpty(bank.trim()))) {
                throw new BizException("银行名称不能为空！");
            }
            if (StringUtil.isEmpty(bankId) || (StringUtil.isEmpty(bankId.trim()))) {
                throw new BizException("银行账号不能为空！");
            }
            //验证银行账号是否有重复，若重复，校验存在的供应商书否与当前供应商不一致，不一致则抛出提示
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            Map conditionMap = new HashMap<>();
            conditionMap.put(ScmSupplierBankCAttribute.ACCOUNT_NO, bankId.trim());
            List<ScmSupplierBankC> supplierBankList = pcm.findByAnyFields(ScmSupplierBankC.class, conditionMap, null);
            if (ListUtil.isNotEmpty(supplierBankList)) {
                String supplierId = supplierBankList.get(0).getScmSupplierId();
                ScmSupplier scmSupplier = pcm.findByPK(ScmSupplier.class, supplierId);
                if (!scmSupplier.getCompleteName().trim().equals(supplierName)) {
                    throw new BizException("该银行账号已经存在！");
                }
            } else {
                conditionMap.clear();
                conditionMap.put(ScmSupplierCAttribute.COMPLETE_NAME, supplierName.trim());
                List<ScmSupplier> scmSupplierList = pcm.findByAnyFields(ScmSupplier.class, conditionMap, null);

                if (ListUtil.isNotEmpty(scmSupplierList)) {
                    ScmSupplierBankC scmSupplierBank = new ScmSupplierBankC();
                    scmSupplierBank.setBankName(bank.trim());
                    scmSupplierBank.setAccountNo(bankId.trim());
                    scmSupplierBank.setScmSupplierId(scmSupplierList.get(0).getUuid());
                    scmSupplierBank.setIsEffect("1");
                    pcm.insert(scmSupplierBank);
                } else {
                    uuid = UUIDUtil.getUUID();
                    ScmSupplierCDto scmSupplierDto = new ScmSupplierCDto();
                    scmSupplierDto.setUuid(uuid);
                    String reqNo = NumberingHelper.getNumbering("SCM_SUPPLIER_CODE", null);
                    scmSupplierDto.setSupplierCode(reqNo);
                    scmSupplierDto.setCompleteName(supplierName);
                    scmSupplierDto.setSimpleName(supplierName);
                    scmSupplierDto.setState("2");
                    scmSupplierDto.setSupplierType("2");
                    scmSupplierDto.setSupplierGenre(flag);
                    ScmSupplierBankCDto scmSupplierBankDto = new ScmSupplierBankCDto();
                    scmSupplierBankDto.setBankName(bank);
                    scmSupplierBankDto.setAccountNo(bankId);
                    scmSupplierBankDto.setIsEffect("1");
                    scmSupplierBankDto.setEditflag("+");
                    List bankList = new ArrayList<>();
                    bankList.add(scmSupplierBankDto);
                    scmSupplierDto.setScmSupplierBankCDto(bankList);
                    scmSupplierDto.setEditflag("+");
                    DtoHelper.CascadeSave(scmSupplierDto);
                }
            }
        }

        return uuid;
    }

    @Override
    public List<ScmSupplier> getBankBelongedScmSupplier(String bankId) {
        List<ScmSupplier> rtn = null;
        if (StringUtil.isNotEmpty(bankId)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            Map conditionMap = new HashMap<>();
            conditionMap.put(ScmSupplierBankCAttribute.ACCOUNT_NO, bankId.trim());
            List<ScmSupplierBankC> supplierBankList = pcm.findByAnyFields(ScmSupplierBankC.class, conditionMap, null);
            if (ListUtil.isNotEmpty(supplierBankList)) {
                List supplierId2 = supplierBankList.stream().filter(t -> StringUtil.isNotEmpty(t.getScmSupplierId())).map(ScmSupplierBankC::getScmSupplierId).collect(Collectors.toList());
                if (ListUtil.isNotEmpty(supplierId2)) {
                    QueryCondition condition1 = new QueryCondition();
                    condition1.setField(CommonAttribute.UUID);
                    condition1.setOperator("in");
                    condition1.setValueList(supplierId2);
                    List<QueryCondition> lstCondition = new ArrayList<QueryCondition>();
                    lstCondition.add(condition1);
                    rtn = PersistenceFactory.getInstance().findByAnyFields(ScmSupplier.class, lstCondition, null);
                }
            }
        }
        return rtn;
    }

    @Override
    public void supplierNameValidate(String completeName, String simpleName) {
        DAOMeta daoMeta = new DAOMeta();
        String sql = "SELECT S.COMPLETE_NAME, NVL(S.SIMPLE_NAME,S.COMPLETE_NAME) AS SIMPLE_NAME FROM SCM_SUPPLIER S WHERE S.COMPLETE_NAME ='" + completeName + "'  OR S.SIMPLE_NAME = '" + simpleName + "'";
        daoMeta.setStrSql(sql);
        List lstSupplier = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (!lstSupplier.isEmpty() && lstSupplier.size() != 0) {
            StringBuffer sb = new StringBuffer();
            String completeNameQ = (((Map) lstSupplier.get(0)).get("completeName")).toString();
            String simpleNameQ = (((Map) lstSupplier.get(0)).get("simpleName")).toString();
            if (completeName.equals(completeNameQ)) {
                sb.append("公司全称,");
            }
            if (simpleName.equals(simpleNameQ)) {
                sb.append("公司简称,");
            }
            sb.append("已经存在！");
            throw new BizException(sb.toString());
        }

    }

    public void supplierNameValidateMouseLeave (String completeName, String simpleName, String supplierCode) {
        DAOMeta daoMeta = new DAOMeta();
        String sql = "SELECT S.SUPPLIER_CODE, S.SUPPLIER_GENRE, S.COMPLETE_NAME, NVL(S.SIMPLE_NAME,S.COMPLETE_NAME) AS SIMPLE_NAME FROM SCM_SUPPLIER S WHERE S.COMPLETE_NAME ='" + completeName + "'  OR S.SIMPLE_NAME = '" + simpleName + "'";
        daoMeta.setStrSql(sql);
        List lstSupplier = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstSupplier)) {
            for (int i = 0; i < lstSupplier.size(); i++) {
                if (!(supplierCode.equals((((Map) lstSupplier.get(i)).get("supplierCode")).toString()))) {
                    StringBuffer sb = new StringBuffer();
                    String completeNameQ = (((Map) lstSupplier.get(i)).get("completeName")).toString();
                    String simpleNameQ = (((Map) lstSupplier.get(i)).get("simpleName")).toString();
                    String supplierGenre = ""+(((Map) lstSupplier.get(i)).get("supplierGenre"));
                    String supplierGenreName = "";
                    if ("0".equals(supplierGenre)) {
                        supplierGenreName = "合格供方";
                    }else if ("1".equals(supplierGenre)) {
                        supplierGenreName = "普通供方";
                    }else if ("2".equals(supplierGenre)) {
                        supplierGenreName = "零星供方";
                    }else if ("3".equals(supplierGenre)) {
                        supplierGenreName = "报销贷方";
                    } else  {
                        supplierGenreName = "其他";
                    }
                    if (completeName.equals(completeNameQ)) {
                        sb.append("公司全称已存在,");
                    }
                    if (simpleName.equals(simpleNameQ)) {
                        sb.append("公司简称已存在,");
                    }
                    sb.append("供方类型为" + supplierGenreName + ",");
                    if ("其他".equals(supplierGenreName)){
                        sb.append("请在客商变更界面中进行变更");
                    }else {
                        sb.append("请在供应商变更界面中进行变更");
                    }
                    throw new BizException(sb.toString());
                }

            }

        }
    }
    public ScmSupplierCDto scmSupplierCChange(String uuid) {
        ScmSupplierDto inputDto = (ScmSupplierDto) DtoHelper.findDtoByPK(ScmSupplierDto.class, uuid);
        if (inputDto == null) {
            return null;
        }
        ScmSupplierCDto outputDto = new ScmSupplierCDto();
        BeanUtil.bean2Bean(inputDto, outputDto);
        outputDto.setPuuid(inputDto.getUuid());

        List<ScmSupplierBankDto> scmSupplierBankDtoList = inputDto.getScmSupplierBankDto();
        if (ListUtil.isNotEmpty(scmSupplierBankDtoList)) {
            List<ScmSupplierBankCDto> scmSupplierBankCDtoList = new ArrayList<ScmSupplierBankCDto>();
            for (ScmSupplierBankDto scmSupplierBankDto : scmSupplierBankDtoList) {
                ScmSupplierBankCDto scmSupplierBankCDtoChange = new ScmSupplierBankCDto();
                BeanUtil.bean2Bean(scmSupplierBankDto, scmSupplierBankCDtoChange);
                scmSupplierBankCDtoChange.setPuuid(scmSupplierBankDto.getUuid());
                scmSupplierBankCDtoList.add(scmSupplierBankCDtoChange);
            }
            outputDto.setScmSupplierBankCDto(scmSupplierBankCDtoList);
        }

        List<ScmSukpplierKeyDto> scmSupplierKeyDtoList = inputDto.getScmSukpplierKeyDto();
        if (ListUtil.isNotEmpty(scmSupplierKeyDtoList)) {
            List<ScmSukpplierKeyCDto> scmSupplierKeyCDtoList = new ArrayList<ScmSukpplierKeyCDto>();
            for (ScmSukpplierKeyDto scmSupplierKeyDto : scmSupplierKeyDtoList) {
                ScmSukpplierKeyCDto scmSupplierKeyCDtoChange = new ScmSukpplierKeyCDto();
                BeanUtil.bean2Bean(scmSupplierKeyDto, scmSupplierKeyCDtoChange);
                scmSupplierKeyCDtoChange.setPuuid(scmSupplierKeyDto.getUuid());
                scmSupplierKeyCDtoList.add(scmSupplierKeyCDtoChange);
            }
            outputDto.setScmSukpplierKeyCDto(scmSupplierKeyCDtoList);
        }

        List<ScmSupplierContactsDto> scmSupplierContactsDtoList = inputDto.getScmSupplierContactsDto();
        if (ListUtil.isNotEmpty(scmSupplierContactsDtoList)) {
            List<ScmSupplierContactsCDto> scmSupplierContactsCDtoList = new ArrayList<ScmSupplierContactsCDto>();
            for (ScmSupplierContactsDto scmSupplierContactsDto : scmSupplierContactsDtoList) {
                ScmSupplierContactsCDto scmSupplierContactsCDtoChange = new ScmSupplierContactsCDto();
                BeanUtil.bean2Bean(scmSupplierContactsDto, scmSupplierContactsCDtoChange);
                scmSupplierContactsCDtoChange.setPuuid(scmSupplierContactsDto.getUuid());
                scmSupplierContactsCDtoList.add(scmSupplierContactsCDtoChange);
            }
            outputDto.setScmSupplierContactsCDto(scmSupplierContactsCDtoList);
        }
        List<CdmFileRelationDto> cdmFileRelationDtoList = inputDto.getCdmFileRelationDto();
        if (ListUtil.isNotEmpty(cdmFileRelationDtoList)) {
            List<CdmFileRelationDto> CdmFileRelationDtoCList = new ArrayList<CdmFileRelationDto>();
            for (CdmFileRelationDto cdmFileRelationDto : cdmFileRelationDtoList) {
                CdmFileRelationDto cdmFileRelationDtoChange = new CdmFileRelationDto();
                BeanUtil.bean2Bean(cdmFileRelationDto, cdmFileRelationDtoChange);
                CdmFileRelationDtoCList.add(cdmFileRelationDtoChange);
            }
            outputDto.setCdmFileRelationDto(CdmFileRelationDtoCList);
        }
        return outputDto;
    }

    public void scmSupplierChange(String uuid) {
        ScmSupplierCDto inputDto = (ScmSupplierCDto) DtoHelper.findDtoByPK(ScmSupplierCDto.class, uuid);
        Map mapSupplierCode = new HashMap();
        mapSupplierCode.put("supplierCode", inputDto.getSupplierCode());
        ScmSupplierDto outputDto = (ScmSupplierDto) DtoHelper.findByAnyFields(ScmSupplierDto.class, mapSupplierCode, null).get(0);
        String uuuid = outputDto.getUuid().toString();
        String CompleteName = outputDto.getCompleteName();


        List<ScmSupplierBankCDto> scmSupplierBankCDtoList = inputDto.getScmSupplierBankCDto();
        if (ListUtil.isNotEmpty(outputDto.getScmSupplierBankDto())) {
            List<ScmSupplierBankDto> scmMainSupplierBankDtoList = outputDto.getScmSupplierBankDto();
            for (ScmSupplierBankDto dto : scmMainSupplierBankDtoList) {
                dto.setEditflag("-");
            }
        }
        if (ListUtil.isNotEmpty(outputDto.getScmSupplierContactsDto())) {
            List<ScmSupplierContactsDto> scmMainSupplierBankDtoList = outputDto.getScmSupplierContactsDto();
            for (ScmSupplierContactsDto dto : scmMainSupplierBankDtoList) {
                dto.setEditflag("-");
            }
        }
        if (ListUtil.isNotEmpty(outputDto.getScmSukpplierKeyDto())) {
            List<ScmSukpplierKeyDto> scmMainSupplierBankDtoList = outputDto.getScmSukpplierKeyDto();
            for (ScmSukpplierKeyDto dto : scmMainSupplierBankDtoList) {
                dto.setEditflag("-");
            }
        }
        if (ListUtil.isNotEmpty(outputDto.getCdmFileRelationDto())) {
            List<CdmFileRelationDto> scmMainSupplierBankDtoList = outputDto.getCdmFileRelationDto();
            for (CdmFileRelationDto dto : scmMainSupplierBankDtoList) {
                dto.setEditflag("-");
            }
        }
        outputDto.setEditflag("*");
        DtoHelper.CascadeSave(outputDto);

        BeanUtil.bean2Bean(inputDto, outputDto);
        outputDto.setUuid(uuuid);
        outputDto.setState("2");
        if (ListUtil.isNotEmpty(scmSupplierBankCDtoList)) {
            List<ScmSupplierBankDto> scmSupplierBankDtoList = new ArrayList<ScmSupplierBankDto>();
            for (ScmSupplierBankCDto scmSupplierBankCDto : scmSupplierBankCDtoList) {
                ScmSupplierBankDto scmSupplierBankDtoChange = new ScmSupplierBankDto();
                BeanUtil.bean2Bean(scmSupplierBankCDto, scmSupplierBankDtoChange);
                scmSupplierBankDtoChange.setScmSupplierId(uuuid);
                scmSupplierBankDtoList.add(scmSupplierBankDtoChange);
                scmSupplierBankDtoChange.setEditflag("+");
            }
            outputDto.setScmSupplierBankDto(scmSupplierBankDtoList);
        }

        List<ScmSukpplierKeyCDto> scmSupplierKeyCDtoList = inputDto.getScmSukpplierKeyCDto();
        if (ListUtil.isNotEmpty(scmSupplierKeyCDtoList)) {
            List<ScmSukpplierKeyDto> scmSupplierKeyDtoList = new ArrayList<ScmSukpplierKeyDto>();
            for (ScmSukpplierKeyCDto scmSupplierKeyCDto : scmSupplierKeyCDtoList) {
                ScmSukpplierKeyDto scmSupplierKeyDtoChange = new ScmSukpplierKeyDto();
                BeanUtil.bean2Bean(scmSupplierKeyCDto, scmSupplierKeyDtoChange);
                scmSupplierKeyDtoChange.setScmSupplierId(uuuid);
                scmSupplierKeyDtoList.add(scmSupplierKeyDtoChange);
                scmSupplierKeyDtoChange.setEditflag("+");
            }
            outputDto.setScmSukpplierKeyDto(scmSupplierKeyDtoList);
        }

        List<ScmSupplierContactsCDto> scmSupplierContactsCDtoList = inputDto.getScmSupplierContactsCDto();
        if (ListUtil.isNotEmpty(scmSupplierContactsCDtoList)) {
            List<ScmSupplierContactsDto> scmSupplierContactsDtoList = new ArrayList<ScmSupplierContactsDto>();
            for (ScmSupplierContactsCDto scmSupplierContactsCDto : scmSupplierContactsCDtoList) {
                ScmSupplierContactsDto scmSupplierContactsCDtohange = new ScmSupplierContactsDto();
                BeanUtil.bean2Bean(scmSupplierContactsCDto, scmSupplierContactsCDtohange);
                scmSupplierContactsCDtohange.setScmSupplierId(uuuid);
                scmSupplierContactsDtoList.add(scmSupplierContactsCDtohange);
                scmSupplierContactsCDtohange.setEditflag("+");
            }
            outputDto.setScmSupplierContactsDto(scmSupplierContactsDtoList);
        }
        List<CdmFileRelationDto> cdmFileRelationDtoList = inputDto.getCdmFileRelationDto();
        if (ListUtil.isNotEmpty(cdmFileRelationDtoList)) {
            for (int i = cdmFileRelationDtoList.size()-1; i >=0; i--) {
                if ("SCM_SUPPLIER_CHANGE".equals(cdmFileRelationDtoList.get(i).getFileClassify())) {
                    cdmFileRelationDtoList.remove(i);
                }
            }
            List<CdmFileRelationDto> CdmFileRelationDtoCList = new ArrayList<CdmFileRelationDto>();
            for (CdmFileRelationDto cdmFileRelationDto : cdmFileRelationDtoList) {
                CdmFileRelationDto cdmFileRelationDtoChange = new CdmFileRelationDto();
                BeanUtil.bean2Bean(cdmFileRelationDto, cdmFileRelationDtoChange);
                cdmFileRelationDtoChange.setDataId(uuid);
                CdmFileRelationDtoCList.add(cdmFileRelationDtoChange);
                cdmFileRelationDtoChange.setEditflag("+");
            }
            outputDto.setCdmFileRelationDto(CdmFileRelationDtoCList);
        }
        outputDto.setEditflag("*");
        DtoHelper.CascadeSave(outputDto);
        if (!(inputDto.getCompleteName().equals(CompleteName))) {
            FadSupplierMappingDto fadSupplierMappingDto = new FadSupplierMappingDto();
            fadSupplierMappingDto.setSupplierFromName(CompleteName);
            fadSupplierMappingDto.setSupplierToLastUuid(outputDto.getUuid());
            fadSupplierMappingDto.setDataFrom(Integer.valueOf(0));
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            Map conditionMap = new HashMap<>();
            ScmSupplier scmSupplier = pcm.findByPK(ScmSupplier.class, outputDto.getUuid());
            conditionMap.put("supplierFromName", inputDto.getCompleteName());
            List<FadSupplierMapping> fadSupplierMapping = pcm.findByAnyFields(FadSupplierMapping.class, conditionMap, null);
            if (ListUtil.isNotEmpty(fadSupplierMapping)) {
                fadSupplierMappingDto.setSupplierToUuid(fadSupplierMapping.get(0).getSupplierToUuid());
            } else {
                fadSupplierMappingDto.setSupplierToUuid(outputDto.getUuid());
            }
            fadSupplierMappingDto.setEditflag("+");
            DtoHelper.CascadeSave(fadSupplierMappingDto);
        }
    }

    public void bankValidate(List<String> bankId, String puuid) {
        //验证银行账号是否有重复，若重复，校验存在的供应商书否与当前供应商不一致，不一致则抛出提示
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map conditionMap = new HashMap<>();
        conditionMap.put(ScmSupplierBankCAttribute.ACCOUNT_NO, bankId);
        List<ScmSupplierBank> supplierBankList = pcm.findByAnyFields(ScmSupplierBank.class, conditionMap, null);
        if (ListUtil.isNotEmpty(supplierBankList)) {
            for (int i = 0; i < supplierBankList.size(); i++) {
                String supplierId = supplierBankList.get(i).getScmSupplierId();
                if (StringUtil.isNotEmpty(puuid)) {
                    if (!(puuid.equals(supplierId))) {
                        throw new BizException("该银行账号已经存在！");
                    }
                }else{
                    throw new BizException("该银行账号已经存在！");
                }
            }
        }
    }
}