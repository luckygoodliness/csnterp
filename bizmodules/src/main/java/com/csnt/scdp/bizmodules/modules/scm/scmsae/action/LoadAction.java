package com.csnt.scdp.bizmodules.modules.scm.scmsae.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePackage;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePlanDetail;
import com.csnt.scdp.bizmodules.entity.scm.*;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSaeAttribute;
import com.csnt.scdp.bizmodules.modules.common.intf.CommonServiceManager;
import com.csnt.scdp.bizmodules.modules.prm.prmorgrolefilter.dto.ScdpOrgDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsae.dto.ScmSaeDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsae.dto.ScmSaeFormDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsae.dto.ScmSaeObjectDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsaetask.dto.ScmSaeTaskDto;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.ObjectUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpFileManager;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.scmsae.services.intf.ScmsaeManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Description:  LoadAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-23 19:38:15
 */

@Scope("singleton")
@Controller("scmsae-load")
@Transactional
public class LoadAction extends CommonLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "scmsae-manager")
    private ScmsaeManager scmsaeManager;

    @Resource(name = "erp-common-service-manager")
    private CommonServiceManager commonServiceManager;

    Map<String, ScmSaeObjectDto> scmSaeObjectDtoCache = new HashMap<String, ScmSaeObjectDto>();
    Map<String, String> userCache = new HashMap<String, String>();
    Map<String, String> orgCache = new HashMap<String, String>();

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        Map scmSaeMap = (Map) out.get("scmSaeDto");
        Object curYear = scmSaeMap.get("curYear");
        if (curYear != null) {
            String curYearDesc = FMCodeHelper.getDescByCode(curYear.toString(), "CDM_YEAR");
            scmSaeMap.put("curYear", curYearDesc);
        }

        //Do After
        return out;
    }

    /**
     * 获取版块、合同数量、金额 字段
     *
     * @param supplierId
     * @param materialCode
     * @return
     */
    private ScmSaeObjectDto getObjectInfo(String supplierId, String materialCode) {
        String sql = "SELECT R.SUPPLIER_ID_NAME AS SUPPLIER_NAME," +
                "       R.MATERIAL_CLASS_NAME AS MATERIAL_NAME," +
                "       R.CONTRACT_NUM, \n" +
                "       R.OPERATIVE_SEGMENTS, \n" +
                "       R.AMOUNT\n" +
                "FROM VW_SCM_SAE_OBJECT R\n" +
                "WHERE R.MATERIAL_CLASS_CODE = '" + materialCode + "'\n" +
                "AND R.SUPPLIER_ID = '" + supplierId + "'";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        List<Map<String, Object>> resultList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        ScmSaeObjectDto objectDto = new ScmSaeObjectDto();
        if (ListUtil.isNotEmpty(resultList)) {
            Map<String, Object> objectMap = resultList.get(0);
            objectDto.setSupplierName(objectMap.get("supplierName").toString());
            objectDto.setMaterialClassName(objectMap.get("materialName").toString());
            objectDto.setOperativeSegments(objectMap.get("operativeSegments").toString());
            objectDto.setAmount((BigDecimal) objectMap.get("amount"));
            objectDto.setContractNum((BigDecimal) objectMap.get("contractNum"));
        }

        return objectDto;
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        ScmSaeDto scmSaeDto = (ScmSaeDto) dtoObj;

        if (scmSaeDto != null) {
            if (StringUtil.isNotEmpty(scmSaeDto.getSaiCase())) {
                ScmSaeCase saiCase = pcm.findByPK(ScmSaeCase.class, scmSaeDto.getSaiCase());
                if (saiCase != null) {
                    scmSaeDto.setSaiCaseDesc(saiCase.getCaseName());
                }
            }
            if (StringUtil.isNotEmpty(scmSaeDto.getWaiCase())) {
                ScmSaeCase waiCase = pcm.findByPK(ScmSaeCase.class, scmSaeDto.getWaiCase());
                if (waiCase != null) {
                    scmSaeDto.setWaiCaseDesc(waiCase.getCaseName());
                }
            }
            if (StringUtil.isNotEmpty(scmSaeDto.getAppraiserCase())) {
                ScmSaeAppraiserCase appraiserCase = pcm.findByPK(ScmSaeAppraiserCase.class, scmSaeDto.getAppraiserCase());
                if (appraiserCase != null) {
                    scmSaeDto.setAppraiserCaseDesc(appraiserCase.getCaseName());
                }
            }


            List<ScmSaeObjectDto> scmSaeObjectDtoList = scmSaeDto.getScmSaeObjectDto();

            List scmSaeTaskIdList = new ArrayList<>();
            Map<String, String> scmSaeTaskIdMap = new HashMap<>();
            if (ListUtil.isNotEmpty(scmSaeObjectDtoList)) {
                scmSaeObjectDtoList.forEach(scmSaeObjectDto -> {
                    List<ScmSaeFormDto> scmSaeFormDtoList = scmSaeObjectDto.getScmSaeFormDto();
                    if (ListUtil.isNotEmpty(scmSaeFormDtoList)) {
                        scmSaeFormDtoList.forEach(scmSaeFormDto -> {
                            if (!scmSaeTaskIdList.contains(scmSaeFormDto.getScmSaeTaskId())) {
                                scmSaeTaskIdList.add(scmSaeFormDto.getScmSaeTaskId());
                            }
                        });
                    }
                });
            }

            if (ListUtil.isNotEmpty(scmSaeTaskIdList)) {
                List<ScmSaeTask> scmSaeTasks = new ArrayList<>();
                QueryCondition condition = new QueryCondition();
                condition.setField("uuid");
                condition.setOperator("in");
                for (int i = 0; i < scmSaeTaskIdList.size(); i += 900) {
                    condition.setValueList(scmSaeTaskIdList.subList(i, i + 900 <= scmSaeTaskIdList.size() ? i + 900 : scmSaeTaskIdList.size()));
                    List<QueryCondition> queryConditions = new ArrayList<>();
                    queryConditions.add(condition);
                    List<ScmSaeTask> scmSaeTaskSub = PersistenceFactory.getInstance().findByAnyFields(ScmSaeTask.class, queryConditions, null);
                    scmSaeTasks.addAll(scmSaeTaskSub);
                }
                if (ListUtil.isNotEmpty(scmSaeTasks)) {
                    for (ScmSaeTask scmSaeTask : scmSaeTasks) {
                        scmSaeTaskIdMap.put(scmSaeTask.getUuid(), scmSaeTask.getState());
                    }
                }
            }

            if (ListUtil.isNotEmpty(scmSaeObjectDtoList)) {
                scmSaeObjectDtoList.forEach(scmSaeObjectDto -> {
                    String key = scmSaeObjectDto.getMaterialCode() + "&&" + scmSaeObjectDto.getSupplierId();
                    ScmSaeObjectDto cacheObj = null;
                    if (scmSaeObjectDtoCache.containsKey(key)) {
                        cacheObj = scmSaeObjectDtoCache.get(key);
                    } else {
                        cacheObj = getObjectInfo(scmSaeObjectDto.getSupplierId(), scmSaeObjectDto.getMaterialCode());
                        //放入缓存
                        scmSaeObjectDtoCache.put(key, cacheObj);
                    }
                    if (cacheObj != null) {
                        scmSaeObjectDto.setSupplierName(cacheObj.getSupplierName());
                        scmSaeObjectDto.setMaterialClassName(cacheObj.getMaterialClassName());
                        scmSaeObjectDto.setOperativeSegments(cacheObj.getOperativeSegments());
                        scmSaeObjectDto.setAmount(cacheObj.getAmount());
                        scmSaeObjectDto.setContractNum(cacheObj.getContractNum());
                    }

                    if (scmSaeObjectDto.getMaterialType() == 0) {
                        scmSaeObjectDto.setMaterialTypeName("采购");
                    } else if (scmSaeObjectDto.getMaterialType() == 1) {
                        scmSaeObjectDto.setMaterialTypeName("外协");
                    } else {
                        scmSaeObjectDto.setMaterialTypeName("");
                    }
                    List<ScmSaeFormDto> scmSaeFormDtoList = scmSaeObjectDto.getScmSaeFormDto();
                    if (ListUtil.isNotEmpty(scmSaeFormDtoList)) {

                        scmSaeFormDtoList.forEach(scmSaeFormDto -> {
                            //填充用户名
                            String userId = scmSaeFormDto.getUserCode();
                            if (StringUtils.isNotBlank(userId)) {
                                if (userCache.containsKey(userId)) {
                                    String name = userCache.get(userId);
                                    String orgName = orgCache.get(userId);
                                    scmSaeFormDto.setUserCodeDesc(name);
                                    scmSaeFormDto.setOfficeIdDesc(orgName);
                                } else {
                                    List<ScdpUser> obj = commonServiceManager.findUserByUserId(userId);
                                    if (ListUtil.isNotEmpty(obj)) {
                                        String name = obj.get(0).getUserName();
                                        String orgUuid = obj.get(0).getOrgUuid();
                                        scmSaeFormDto.setUserCodeDesc(name);
                                        userCache.put(userId, name);
                                        if (StringUtil.isNotEmpty(orgUuid)) {
                                            ScdpOrgDto scdpOrgDto = (ScdpOrgDto) DtoHelper.findDtoByPK(ScdpOrgDto.class, orgUuid);
                                            scmSaeFormDto.setOfficeIdDesc("" + scdpOrgDto.getOrgName());
                                            orgCache.put(userId, "" + scdpOrgDto.getOrgName());
                                        }
                                    }
                                }

                            }
                            //状态
                            if (scmSaeFormDto.getScmSaeTaskId() == null) {
                                scmSaeFormDto.setState("未发放");
                            } else {
                                String scmSaeTaskId = scmSaeFormDto.getScmSaeTaskId();
                                if ("0".equals(scmSaeTaskIdMap.get(scmSaeTaskId))) {
                                    scmSaeFormDto.setState("已发放");
                                } else if ("1".equals(scmSaeTaskIdMap.get(scmSaeTaskId))) {
                                    scmSaeFormDto.setState("已评价");
                                }
                            }


                        });
                    }
                });
            }
        }

    }
}