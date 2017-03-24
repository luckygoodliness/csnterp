package com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.impl;

import com.csnt.scdp.bizmodules.attributes.CommonAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.scm.FadSupplierMapping;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplierBank;
import com.csnt.scdp.bizmodules.entityattributes.scm.PrmBlacklistMonthAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierBankAttribute;
import com.csnt.scdp.bizmodules.modules.scm.fadsuppliermapping.dto.FadSupplierMappingDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSupplierBankDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSupplierDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.DtoHelper;
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
 * Description:  SupplierinforManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 16:02:01
 */

@Scope("singleton")
@Service("supplierinfor-manager")
public class SupplierinforManagerImpl implements SupplierinforManager {

    @Override
    public void setExtraData(Map out) {
        //1.设置显示数据
        Map scmSupplierMap = (Map) out.get("scmSupplierDto");
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
            DAOMeta daoMeta = DAOHelper.getDAO("supplierinfor-dao", "cancel_effect_supplier", paramList);
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
            conditionMap.put(ScmSupplierAttribute.COMPLETE_NAME, supplierName.trim());
            List<ScmSupplier> scmSupplierList = pcm.findByAnyFields(ScmSupplier.class, conditionMap, null);
            if (ListUtil.isNotEmpty(scmSupplierList)) {
                uuid = scmSupplierList.get(0).getUuid();
            } else {
                uuid = UUIDUtil.getUUID();
                ScmSupplierDto scmSupplierDto = new ScmSupplierDto();
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
            conditionMap.put(ScmSupplierBankAttribute.ACCOUNT_NO, bankId.trim());
            List<ScmSupplierBank> supplierBankList = pcm.findByAnyFields(ScmSupplierBank.class, conditionMap, null);
            if (ListUtil.isNotEmpty(supplierBankList)) {
                String supplierId = supplierBankList.get(0).getScmSupplierId();
                ScmSupplier scmSupplier = pcm.findByPK(ScmSupplier.class, supplierId);
                if (!scmSupplier.getCompleteName().trim().equals(supplierName)) {
                    throw new BizException("该银行账号已经存在！");
                }
            } else {
                conditionMap.clear();
                conditionMap.put(ScmSupplierAttribute.COMPLETE_NAME, supplierName.trim());
                List<ScmSupplier> scmSupplierList = pcm.findByAnyFields(ScmSupplier.class, conditionMap, null);

                if (ListUtil.isNotEmpty(scmSupplierList)) {
                    ScmSupplierBank scmSupplierBank = new ScmSupplierBank();
                    scmSupplierBank.setBankName(bank.trim());
                    scmSupplierBank.setAccountNo(bankId.trim());
                    scmSupplierBank.setScmSupplierId(scmSupplierList.get(0).getUuid());
                    scmSupplierBank.setIsEffect("1");
                    pcm.insert(scmSupplierBank);
                } else {
                    uuid = UUIDUtil.getUUID();
                    ScmSupplierDto scmSupplierDto = new ScmSupplierDto();
                    scmSupplierDto.setUuid(uuid);
                    String reqNo = NumberingHelper.getNumbering("SCM_SUPPLIER_CODE", null);
                    scmSupplierDto.setSupplierCode(reqNo);
                    scmSupplierDto.setCompleteName(supplierName);
                    scmSupplierDto.setSimpleName(supplierName);
                    scmSupplierDto.setState("2");
                    scmSupplierDto.setSupplierType("2");
                    scmSupplierDto.setSupplierGenre(flag);
                    ScmSupplierBankDto scmSupplierBankDto = new ScmSupplierBankDto();
                    scmSupplierBankDto.setBankName(bank);
                    scmSupplierBankDto.setAccountNo(bankId);
                    scmSupplierBankDto.setIsEffect("1");
                    scmSupplierBankDto.setEditflag("+");
                    List bankList = new ArrayList<>();
                    bankList.add(scmSupplierBankDto);
                    scmSupplierDto.setScmSupplierBankDto(bankList);
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
            conditionMap.put(ScmSupplierBankAttribute.ACCOUNT_NO, bankId.trim());
            List<ScmSupplierBank> supplierBankList = pcm.findByAnyFields(ScmSupplierBank.class, conditionMap, null);
            if (ListUtil.isNotEmpty(supplierBankList)) {
                List supplierId2 = supplierBankList.stream().filter(t -> StringUtil.isNotEmpty(t.getScmSupplierId())).map(ScmSupplierBank::getScmSupplierId).collect(Collectors.toList());
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
    public void supplierNameValidate(String completeName, String simpleName, String supplierCode) {
        DAOMeta daoMeta = new DAOMeta();
        String sql = "SELECT S.SUPPLIER_CODE,S.COMPLETE_NAME, NVL(S.SIMPLE_NAME,S.COMPLETE_NAME) AS SIMPLE_NAME FROM SCM_SUPPLIER S WHERE S.COMPLETE_NAME ='" + completeName + "'  OR S.SIMPLE_NAME = '" + simpleName + "'";
        daoMeta.setStrSql(sql);
        List lstSupplier = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);

        if (!lstSupplier.isEmpty() && lstSupplier.size() != 0) {
            for (int i = 0; i < lstSupplier.size(); i++) {
                if (!(supplierCode.equals((((Map) lstSupplier.get(i)).get("supplierCode")).toString()))) {
                    StringBuffer sb = new StringBuffer();
                    String completeNameQ = (((Map) lstSupplier.get(i)).get("completeName")).toString();
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
        }
    }

    public void fadSupplierMappingChange(String uuid, String completeName) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        ScmSupplier scmSupplier = pcm.findByPK(ScmSupplier.class, uuid);
        if (!(scmSupplier.getCompleteName().equals(completeName))) {
            FadSupplierMappingDto fadSupplierMappingDto = new FadSupplierMappingDto();
            fadSupplierMappingDto.setSupplierFromName(scmSupplier.getCompleteName());
            fadSupplierMappingDto.setSupplierToLastUuid(uuid);
            fadSupplierMappingDto.setDataFrom(Integer.valueOf(0));
            Map conditionMap = new HashMap<>();
            conditionMap.put("supplierFromName", completeName);
            List<FadSupplierMapping> fadSupplierMapping = pcm.findByAnyFields(FadSupplierMapping.class, conditionMap, null);
            if (ListUtil.isNotEmpty(fadSupplierMapping)) {
                fadSupplierMappingDto.setSupplierToUuid(fadSupplierMapping.get(0).getSupplierToUuid());
            } else {
                fadSupplierMappingDto.setSupplierToUuid(uuid);
            }
            fadSupplierMappingDto.setEditflag("+");
            DtoHelper.CascadeSave(fadSupplierMappingDto);
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
}