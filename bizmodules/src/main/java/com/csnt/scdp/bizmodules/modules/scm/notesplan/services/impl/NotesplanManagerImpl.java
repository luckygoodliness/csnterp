package com.csnt.scdp.bizmodules.modules.scm.notesplan.services.impl;

import com.csnt.scdp.bizmodules.entity.scm.ScmNotesPlan;
import com.csnt.scdp.bizmodules.entity.scm.ScmNotesPlanDetail;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmNotesPlanAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmNotesPlanDetailAttribute;
import com.csnt.scdp.bizmodules.modules.scm.notesplan.dto.ScmNotesPlanDetailDto;
import com.csnt.scdp.bizmodules.modules.scm.notesplan.services.intf.NotesplanManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.ExpandFieldHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Description:  NotesplanManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-11-04 15:02:50
 */

@Scope("singleton")
@Service("notesplan-manager")
public class NotesplanManagerImpl implements NotesplanManager {

    @Override
    public Map loadDataDefault(Map inMap) {
        //1.获取uuid
        String uuid = (String) inMap.get(CommonAttribute.UUID);
        if (StringUtil.isEmpty(uuid)) {
            throw new BizException("未选择月度记录！");
        }
        //2.获取到年份和状态
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        ScmNotesPlan scmNotesPlan = pcm.findByPK(ScmNotesPlan.class, uuid);
        if (scmNotesPlan == null) {
            throw new BizException("月度记录不存在！");
        }
        //3.根据是否锁定状态来加载不同的数据
        String islock = scmNotesPlan.getIslock();
        List lstReturn = new ArrayList();
        if (StringUtil.isEmpty(islock)) {
            islock = ScmNotesPlanAttribute.ISLOCK_VALUE_NO;
        }
        List listParam = new ArrayList();
        DAOMeta daoMeta = new DAOMeta();
        if (ScmNotesPlanAttribute.ISLOCK_VALUE_YES.equals(islock)) {
            //3.1如果锁定
            listParam.add(uuid);
            daoMeta = DAOHelper.getDAO("notesplan-dao", "locked_detail_filter_query", listParam);
            String sql = setConditionsForLocked(daoMeta, inMap);
            daoMeta.setStrSql(sql);
        } else if (ScmNotesPlanAttribute.ISLOCK_VALUE_NO.equals(islock)) {
            //3.2.如果没锁定
            listParam.add(uuid);
            listParam.add(uuid);
            daoMeta = DAOHelper.getDAO("notesplan-dao", "unlocked_detail_filter_query", listParam);
            String sql = setConditionsForUnlocked(daoMeta, inMap);
            daoMeta.setStrSql(sql);
        }
        List<Map<String, Object>> list = pcm.findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(list)) {
            Map childFilter = new HashMap();
            list.forEach(scmNotesPlanDetail -> {
                ScmNotesPlanDetailDto scmNotesPlanDetailDto = (ScmNotesPlanDetailDto) BeanUtil.map2Dto(scmNotesPlanDetail, ScmNotesPlanDetailDto.class);
                recurFindDto(scmNotesPlanDetailDto, childFilter);
                lstReturn.add(scmNotesPlanDetailDto);
            });
        }
        Map map = new HashMap();
        map.put("scmNotesPlanDetailDto", lstReturn);
        return map;
    }

    @Override
    public Map save(Map inMap) {
        String uuid = (String) inMap.get("uuid");
        List selectedRecords = (ArrayList) inMap.get("selectedRecords");
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        //1.判断uuid
        if (StringUtil.isEmpty(uuid)) {
            throw new BizException("未选择期号记录！");
        }
        for (Object selectedRecord : selectedRecords) {
            Map record = (Map) selectedRecord;
            String detailUuid = (String) record.get("uuid");
            String scmContractCode = (String) record.get("scmContractCode");
            ScmNotesPlanDetail scmNotesPlanDetail = new ScmNotesPlanDetail();
            scmNotesPlanDetail.setScmContractCode(scmContractCode);
            scmNotesPlanDetail.setScmNotesPlanId(uuid);
            //财务预估
            Object conclusionLineFinancial = record.get("conclusionLineFinancial");
            if (conclusionLineFinancial != null) {
                scmNotesPlanDetail.setConclusionLineFinancial(new BigDecimal((long) conclusionLineFinancial));
            }
            //外部结题比例
            Object conclusionRateOut = record.get("conclusionRateOut");
            if (conclusionRateOut != null) {
                scmNotesPlanDetail.setConclusionRateOut(new BigDecimal((long) conclusionRateOut));
            }
            if (StringUtil.isEmpty(detailUuid)) {//1.如果数据是新增
                DAOMeta daoMeta = new DAOMeta();
                daoMeta.setStrSql("SELECT 1 FROM SCM_NOTES_PLAN_DETAIL WHERE SCM_CONTRACT_CODE='" + scmContractCode + "' AND SCM_NOTES_PLAN_ID= " + detailUuid);
                if (pcm.findByNativeSQL(daoMeta).size() == 0) {
                    pcm.insert(scmNotesPlanDetail);
                }
            } else {//2.如果是更新
                pcm.update(scmNotesPlanDetail);
            }
        }
        Map out = new HashMap<>();
        return out;
    }

    @Override
    public Map markTime(Map inMap) {
        String uuid = (String) inMap.get("uuid");
        Map selectedRecord = (Map) inMap.get("selectedRecord");
        String detailUuid = (String) selectedRecord.get("uuid");
        String scmContractCode = (String) selectedRecord.get("scmContractCode");
        Object scmContractAmount = selectedRecord.get("scmContractAmount");
        Object scmContractInvoice = selectedRecord.get("scmContractInvoice");
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        //1.判断uuid
        if (StringUtil.isEmpty(uuid)) {
            throw new BizException("未选择期号记录！");
        }
        //2.判断scmContractCode
        if (StringUtil.isEmpty(scmContractCode)) {
            throw new BizException("合同号有误");
        }
        //3.获取传进来的合同记录
        List paramScmContract = new ArrayList<>();
        paramScmContract.add(scmContractCode);
        DAOMeta scmContractDaoMeta = DAOHelper.getDAO("notesplan-dao", "simple_query_from_view", paramScmContract);
        List<Map<String, Object>> listScmContract = pcm.findByNativeSQL(scmContractDaoMeta);
        if (ListUtil.isEmpty(listScmContract)) {
            throw new BizException("未找到对应合同信息！");
        }
        //3.记录时间
        ScmNotesPlanDetail scmNotesPlanDetail = new ScmNotesPlanDetail();
        if (StringUtil.isNotEmpty(detailUuid)) {//1.如果数据是新增
            scmNotesPlanDetail = pcm.findByPK(ScmNotesPlanDetail.class, detailUuid);
            if (scmNotesPlanDetail == null) {
                throw new BizException("没有该条详细记录!");
            }
        }
        //设置实体
        DecimalFormat df = new DecimalFormat("#.00");
        //财务预估
        Object conclusionLineFinancial = selectedRecord.get("conclusionLineFinancial");
        if (conclusionLineFinancial != null) {
            scmNotesPlanDetail.setConclusionLineFinancial(new BigDecimal(conclusionLineFinancial.toString()));
        }
        //外部结题比例
        Object conclusionRateOut = selectedRecord.get("conclusionRateOut");
        if (conclusionRateOut != null) {
            BigDecimal temp = new BigDecimal(conclusionRateOut.toString());
            if (temp.compareTo(new BigDecimal(100)) > 0) {
                throw new BizException("外部结题比例不能大于100！");
            }
            scmNotesPlanDetail.setConclusionRateOut(new BigDecimal(df.format(temp)));
        }
        scmNotesPlanDetail.setScmNotesPlanId(uuid);
        Date date = new Date();
        //已催办
        scmNotesPlanDetail.setMarkTime(date);
        //合同编号
        scmNotesPlanDetail.setScmContractCode(scmContractCode);

        //供应商名称
        Object supplierCode = selectedRecord.get("supplierCode");
        if (supplierCode != null) {
            scmNotesPlanDetail.setSupplierId(supplierCode.toString());
        }
        Object supplierName = selectedRecord.get("supplierName");
        if (supplierName != null) {
            scmNotesPlanDetail.setSupplierName(supplierName.toString());
        }
        //项目
        Object prmProjectMainId = selectedRecord.get("prmProjectMainId");
        if (prmProjectMainId != null) {
            scmNotesPlanDetail.setPrmProjectMainId(prmProjectMainId.toString());
        }
        //内部合同基线
        Object conclusionLineIn = selectedRecord.get("conclusionLineIn");
        if (conclusionLineIn != null) {
            scmNotesPlanDetail.setConclusionLineIn(new BigDecimal(df.format(new BigDecimal(conclusionLineIn.toString()))));
        }
        //外部结题基线
        Object conclusionLineOut = selectedRecord.get("conclusionLineOut");
        if (conclusionLineOut != null) {
            scmNotesPlanDetail.setConclusionLineOut(new BigDecimal(conclusionLineOut.toString()));
        }
        //合同执行基线
        Object conclusionLineContract = selectedRecord.get("conclusionLineContract");
        if (conclusionLineContract != null) {
            scmNotesPlanDetail.setConclusionLineContract(new BigDecimal(conclusionLineContract.toString()));
        }
        //内部结题比例
        Object conclusionRateIn = selectedRecord.get("conclusionRateIn");
        if (conclusionRateIn != null) {
            BigDecimal temp = new BigDecimal(conclusionRateIn.toString());
            if (temp.compareTo(new BigDecimal(100)) > 0) {
                throw new BizException("内部结题比例不能大于100！");
            }
            scmNotesPlanDetail.setConclusionRateIn(new BigDecimal(df.format(temp)));
        }
        //合同执行比例
        Object conclusionRateContract = selectedRecord.get("conclusionRateContract");
        if (conclusionRateContract != null) {
            BigDecimal temp = new BigDecimal(conclusionRateContract.toString());
            if (temp.compareTo(new BigDecimal(100)) > 0) {
                throw new BizException("合同执行比例不能大于100！");
            }
            scmNotesPlanDetail.setConclusionRateContract(new BigDecimal(df.format(temp)));
        }
        //收款比例
        Object conclusionRateReceipt = selectedRecord.get("conclusionRateReceipt");
        if (conclusionRateReceipt != null) {
            BigDecimal temp = new BigDecimal(conclusionRateReceipt.toString());
            if (temp.compareTo(new BigDecimal(100)) > 0) {
                throw new BizException("收款比例不能大于100！");
            }
            scmNotesPlanDetail.setConclusionRateReceipt(new BigDecimal(df.format(temp)));
        }
        //项目ID
        Object projectId = selectedRecord.get("projectId");
        if (projectId != null) {
            scmNotesPlanDetail.setPrmProjectMainId(projectId.toString());
        }
        //已支付
        Object paid = selectedRecord.get("paid");
        if (paid != null) {
            scmNotesPlanDetail.setPaid(new BigDecimal(paid.toString()));
        }
        if (scmContractAmount != null) {
            scmNotesPlanDetail.setScmContractAmount(new BigDecimal((long) scmContractAmount));
        }
        if (scmContractInvoice != null) {
            scmNotesPlanDetail.setScmContractInvoice(new BigDecimal((long) scmContractInvoice));
        }
        if (StringUtil.isEmpty(detailUuid)) {//1.如果数据是新增
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql("SELECT 1 FROM SCM_NOTES_PLAN_DETAIL WHERE SCM_CONTRACT_CODE='" + scmContractCode + "' AND SCM_NOTES_PLAN_ID= " + detailUuid);
            if (pcm.findByNativeSQL(daoMeta).size() == 0) {
                pcm.insert(scmNotesPlanDetail);
            }
        } else {//2.如果是更新
            pcm.update(scmNotesPlanDetail);
        }
        Map out = new HashMap<>();
        return out;
    }

    @Override
    public Map addNewIssue(Map inMap) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String monthStr = (month >= 10 ? month : "0" + month).toString();
        String dayStr = (day >= 10 ? day : "0" + day).toString();
        String issueNumber = year + "" + monthStr + "" + dayStr;
        List paramList = new ArrayList<>();
        paramList.add(issueNumber);
        DAOMeta daoMeta = DAOHelper.getDAO("notesplan-dao", "query_exist_by_issue_number", paramList);
        List list = pcm.findByNativeSQL(daoMeta);
        if (ListUtil.isEmpty(list)) {
            ScmNotesPlan scmNotesPlan = new ScmNotesPlan();
            scmNotesPlan.setIssueNumber(issueNumber);
            scmNotesPlan.setIslock(ScmNotesPlanAttribute.ISLOCK_VALUE_NO);
            pcm.insert(scmNotesPlan);
        } else {
            throw new BizException("已存在记录！");
        }
        Map out = new HashMap<>();
        return out;
    }

    @Override
    public Map lockIssue(Map inMap) {
        Map out = new HashMap<>();
        String uuid = (String) inMap.get(CommonAttribute.UUID);
        if (StringUtil.isNotEmpty(uuid)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            List paramList = new ArrayList<>();
            paramList.add(uuid);
            DAOMeta daoMeta = DAOHelper.getDAO("notesplan-dao", "lock_issue", paramList);
            pcm.updateByNativeSQL(daoMeta);
        } else {
            throw new BizException("没有UUID！");
        }
        return null;
    }

    public void recurFindDto(BasePojo dtoObj, Map childFilter) {
        Field[] fields = dtoObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            String strFieldType = field.getType().getSimpleName();
            if ("Map".equalsIgnoreCase(strFieldType) && StringUtil.isNotEmpty(BeanUtil.getExpandFieldType(field))) {
                String expandType = BeanUtil.getExpandFieldType(field);
                String uuid = (String) BeanUtil.getProperty(dtoObj, "uuid");
                if (StringUtil.isEmpty(uuid) || StringUtil.isEmpty(expandType)) continue;
                Map expandField = ExpandFieldHelper.getExpandFields(expandType, uuid);
                BeanUtil.setProperty(dtoObj, field.getName(), expandField);
            } else if ("List".equalsIgnoreCase(strFieldType)) {
                Class subDtoClass = (Class) ((ParameterizedTypeImpl) (field.getGenericType())).getActualTypeArguments()[0];
                Class subPojoClass = BeanUtil.getPojoClassByDto(subDtoClass);
                if (subPojoClass == null) continue;
                Map mapCon = new HashMap();
                mapCon.put(BeanUtil.getFkByBasePojo(field), BeanUtil.getProperty(dtoObj, BeanUtil.getFkByMappingBasePojo(field)));
                mapCon.putAll(childFilter);
                PersistenceCrudManager pcm = PersistenceFactory.getInstance();
                List subPojoLsts = pcm.findByAnyFields(subPojoClass, mapCon, null);
                List subDtoLsts = new ArrayList();
                for (Object pojoItem : subPojoLsts) {
                    BasePojo dtoItem = BeanFactory.getObject(subDtoClass);
                    BeanUtil.bean2Bean(pojoItem, dtoItem);
                    subDtoLsts.add(dtoItem);
                    recurFindDto(dtoItem, childFilter);
                }
                if (ListUtil.isNotEmpty(subDtoLsts)) {
                    BeanUtil.setProperty(dtoObj, field.getName(), subDtoLsts);
                }
            } else if ("BaseBean".equalsIgnoreCase(strFieldType)) {
                Class subDtoClass = (Class) ((ParameterizedTypeImpl) (field.getGenericType())).getActualTypeArguments()[0];
                Class subPojoClass = BeanUtil.getPojoClassByDto(subDtoClass);
                Map mapCon = new HashMap();
                mapCon.put(BeanUtil.getFkByBasePojo(field), BeanUtil.getProperty(dtoObj, BeanUtil.getFkByMappingBasePojo(field)));
                PersistenceCrudManager pcm = PersistenceFactory.getInstance();
                List subPojoLsts = pcm.findByAnyFields(subPojoClass, mapCon, null);
                Object pojoItem = subPojoLsts.get(0);
                BasePojo dtoItem = BeanFactory.getObject(subDtoClass);
                BeanUtil.bean2Bean(pojoItem, dtoItem);
                recurFindDto(dtoItem, childFilter);
                BeanUtil.setProperty(dtoObj, field.getName(), dtoItem);
            }
        }
    }

    public String setConditionsForLocked(DAOMeta daoMeta, Map inMap) {
        String sql = daoMeta.getStrSql();
        Map<String, Object> condMap = (Map) inMap.get("queryConditions");
        if (MapUtil.isEmpty(condMap)) {
            sql = sql.replace("${SELFCONDITIONS}", " 1=1 ");
        } else {
            String selfSql = " 1=1 ";
            String officeId = (String) condMap.get("officeId");
            if (StringUtil.isNotEmpty(officeId)) {
                selfSql += " AND T.OFFICE_ID= '" + officeId + "'";
            }
            String projectId = (String) condMap.get("projectId");
            if (StringUtil.isNotEmpty(projectId)) {
                selfSql += " AND T.PROJECT_ID= '" + projectId + "'";
            }
            String supplierCode = (String) condMap.get("supplierCode");
            if (StringUtil.isNotEmpty(supplierCode)) {
                selfSql += " AND T.SUPPLIER_CODE= '" + supplierCode + "'";
            }
            String createBy = (String) condMap.get("createBy");
            if (StringUtil.isNotEmpty(createBy)) {
                selfSql += " AND T.CREATE_BY= '" + createBy + "'";
            }
            String conclusion = (String) condMap.get("conclusion");
            if (StringUtil.isNotEmpty(conclusion)) {
                if (ScmNotesPlanDetailAttribute.CONCLUSION_VALUE_IN.equals(conclusion)) {
                    selfSql += " AND B.CONCLUSION_LINE_IN>0 ";
                } else if (ScmNotesPlanDetailAttribute.CONCLUSION_VALUE_OUT.equals(conclusion)) {
                    selfSql += " AND B.CONCLUSION_LINE_OUT>0 ";
                } else if (ScmNotesPlanDetailAttribute.CONCLUSION_VALUE_OUT.equals(conclusion)) {
                    selfSql += " AND B.CONCLUSION_LINE_CONTRACT>0 ";
                }
            }
            sql = sql.replace("${SELFCONDITIONS}", selfSql);
        }
        return sql;
    }

    public String setConditionsForUnlocked(DAOMeta daoMeta, Map inMap) {
        String sql = daoMeta.getStrSql();
        Map<String, Object> condMap = (Map) inMap.get("queryConditions");
        if (MapUtil.isEmpty(condMap)) {
            sql = sql.replace("${SELFCONDITIONS}", " 1=1 ");
        } else {
            String selfSql = " 1=1 ";
            String officeId = (String) condMap.get("officeId");
            if (StringUtil.isNotEmpty(officeId)) {
                selfSql += " AND T.OFFICE_ID= '" + officeId + "'";
            }
            String projectId = (String) condMap.get("projectId");
            if (StringUtil.isNotEmpty(projectId)) {
                selfSql += " AND T.PROJECT_ID= '" + projectId + "'";
            }
            String supplierCode = (String) condMap.get("supplierCode");
            if (StringUtil.isNotEmpty(supplierCode)) {
                selfSql += " AND T.SUPPLIER_CODE= '" + supplierCode + "'";
            }
            String createBy = (String) condMap.get("createBy");
            if (StringUtil.isNotEmpty(createBy)) {
                selfSql += " AND T.CREATE_BY= '" + createBy + "'";
            }
            String conclusion = (String) condMap.get("conclusion");
            if (StringUtil.isNotEmpty(conclusion)) {
                if (ScmNotesPlanDetailAttribute.CONCLUSION_VALUE_IN.equals(conclusion)) {
                    selfSql += " AND A.CONCLUSION_LINE_IN>0 ";
                } else if (ScmNotesPlanDetailAttribute.CONCLUSION_VALUE_OUT.equals(conclusion)) {
                    selfSql += " AND (T.AMOUNT*CONCLUSION_RATE_OUT-T.FAD_INVOICE_MONEY)>0 ";
                } else if (ScmNotesPlanDetailAttribute.CONCLUSION_VALUE_CONTRACT.equals(conclusion)) {
                    selfSql += " AND T.CONCLUSION_LINE_CONTRACT>0 ";
                }
            }
            sql = sql.replace("${SELFCONDITIONS}", selfSql);
        }
        return sql;
    }
}