package com.csnt.scdp.bizmodules.modules.prm.projectmain.services.impl;

import com.csnt.scdp.bizmodules.attributes.ContractTaxType;
import com.csnt.scdp.bizmodules.attributes.PrmBudgetCodes;
import com.csnt.scdp.bizmodules.entity.prm.*;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetDetailAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetDetailCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmCollectionMeasureAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmAssociatedUnitsCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmBudgetAccessoryCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmBudgetDetailCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmBudgetOutsourceCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmBudgetPrincipalCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmBudgetRunCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmContractDetailCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmMemberDetailPCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmPayDetailPCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmProgressDetailPCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmProjectMainCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmQsPCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmReceiptsDetailPCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmSquareDetailPCDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmAssociatedUnitsDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmBudgetAccessoryDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmBudgetDetailDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmBudgetOutsourceDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmBudgetPrincipalDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmBudgetRunDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmContractDetailDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmMemberDetailPDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmPayDetailPDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmProgressDetailPDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmProjectMainDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmQsPDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmReceiptsDetailPDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmSquareDetailPDto;
import com.csnt.scdp.bizmodules.modules.prm.projectmain.services.intf.ProjectmainManager;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.services.intf.PurchaseplanManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.*;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:  ProjectmainManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 13:30:43
 */

@Scope("singleton")
@Service("projectmain-manager")
public class ProjectmainManagerImpl implements ProjectmainManager {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ProjectmainManagerImpl.class);


    public Map<String, List<BasePojo>> loadProjectPlanHistory(String mainUuid) {
        Map<String, List<BasePojo>> out = new HashMap<>();
        List<String> lstParam = new ArrayList<>();
        lstParam.add(mainUuid);
        //按照创建时间升序排列
        DAOMeta daoMeta = DAOHelper.getDAO("projectmain-dao", "prm-project-main-change-approved-all", lstParam);
        daoMeta.setNeedFilter(false);
        List<Map<String, Object>> lstChange = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isEmpty(lstChange)) {
            return out;
        }
        List<String> lstProjectChangeUuid = lstChange.stream().map(x -> (String) x.get(CommonAttribute.UUID)).collect
                (Collectors.toList());
        //load detail history one by one
        List lstAssociatedHis = loadProjectPlanDetailHistory(lstProjectChangeUuid,
                PrmAssociatedUnitsC.class, PrmAssociatedUnitsDto.class, ASSOCIATED_UNITS_FIELDS);
        List lstMemberHis = loadProjectPlanDetailHistory(lstProjectChangeUuid,
                PrmMemberDetailPC.class, PrmMemberDetailPDto.class, MEMBER_DETAIL_FIELDS);
        List lstPayHis = loadProjectPlanDetailHistory(lstProjectChangeUuid,
                PrmPayDetailPC.class, PrmPayDetailPDto.class, PAY_DETAIL_FIELDS);
        List lstProgressHis = loadProjectPlanDetailHistory(lstProjectChangeUuid,
                PrmProgressDetailPC.class, PrmProgressDetailPDto.class, PROGRESS_DETAIL_FIELDS);
        List lstSquareHis = loadProjectPlanDetailHistory(lstProjectChangeUuid,
                PrmSquareDetailPC.class, PrmSquareDetailPDto.class, SQUARE_DETAIL_FIELDS);
        List lstReceiptsHis = loadProjectPlanDetailHistory(lstProjectChangeUuid,
                PrmReceiptsDetailPC.class, PrmReceiptsDetailPDto.class, RECEIPTS_DETAIL_FIELDS);
        List lstQsHis = loadProjectPlanDetailHistory(lstProjectChangeUuid,
                PrmQsPC.class, PrmQsPDto.class, QS_DETAIL_FIELDS);
        //load the userId and reset the userName
        Set<String> userIdSet = new HashSet<>();
        lstAssociatedHis.forEach(x -> {
            PrmAssociatedUnitsDto pojo = (PrmAssociatedUnitsDto) x;
            userIdSet.add(pojo.getLatestUpdateBy());
        });
        lstMemberHis.forEach(x -> {
            PrmMemberDetailPDto pojo = (PrmMemberDetailPDto) x;
            userIdSet.add(pojo.getLatestUpdateBy());
            userIdSet.add(pojo.getStaffId());
        });
        lstPayHis.forEach(x -> {
            PrmPayDetailPDto pojo = (PrmPayDetailPDto) x;
            userIdSet.add(pojo.getLatestUpdateBy());
        });
        lstProgressHis.forEach(x -> {
            PrmProgressDetailPDto pojo = (PrmProgressDetailPDto) x;
            userIdSet.add(pojo.getLatestUpdateBy());
        });
        lstSquareHis.forEach(x -> {
            PrmSquareDetailPDto pojo = (PrmSquareDetailPDto) x;
            userIdSet.add(pojo.getLatestUpdateBy());
        });
        lstReceiptsHis.forEach(x -> {
            PrmReceiptsDetailPDto pojo = (PrmReceiptsDetailPDto) x;
            userIdSet.add(pojo.getLatestUpdateBy());
        });
        lstQsHis.forEach(x -> {
            PrmQsPDto pojo = (PrmQsPDto) x;
            userIdSet.add(pojo.getLatestUpdateBy());
            userIdSet.add(pojo.getSafePrincipal());
            userIdSet.add(pojo.getSafeContact());
            userIdSet.add(pojo.getQualityPrincipal());
            userIdSet.add(pojo.getQualityContact());
        });
        //staffid, latestupdateby, qsp
        if (userIdSet.size() > 0) {
            Map mapUserId = new HashMap();
            mapUserId.put(ScdpUserAttribute.USER_ID, new ArrayList<>(userIdSet));
            List<ScdpUser> lstUser = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, mapUserId, null);
            if (ListUtil.isNotEmpty(lstUser)) {
                for (ScdpUser user : lstUser) {
                    lstAssociatedHis.forEach(x -> {
                        PrmAssociatedUnitsDto pojo = (PrmAssociatedUnitsDto) x;
                        if (user.getUserId().equals(pojo.getLatestUpdateBy())) {
                            pojo.setLatestUpdateByDesc(user.getUserName());
                        }
                    });
                    lstMemberHis.forEach(x -> {
                        PrmMemberDetailPDto pojo = (PrmMemberDetailPDto) x;
                        if (user.getUserId().equals(pojo.getLatestUpdateBy())) {
                            pojo.setLatestUpdateByDesc(user.getUserName());
                        }
                        if (user.getUserId().equals(pojo.getStaffId())) {
                            pojo.setStaffIdDesc(user.getUserName());
                        }
                        pojo.setPostDesc(this.getPostDesc(pojo.getPost()));
                        if (StringUtil.isNotEmpty(pojo.getJobShare())) {
                            pojo.setJobShareDesc(FMCodeHelper.getDescByCode(pojo.getJobShare(), "JOB_SHARE"));
                        }
                    });
                    lstPayHis.forEach(x -> {
                        PrmPayDetailPDto pojo = (PrmPayDetailPDto) x;
                        if (user.getUserId().equals(pojo.getLatestUpdateBy())) {
                            pojo.setLatestUpdateByDesc(user.getUserName());
                        }
                    });
                    lstProgressHis.forEach(x -> {
                        PrmProgressDetailPDto pojo = (PrmProgressDetailPDto) x;
                        if (user.getUserId().equals(pojo.getLatestUpdateBy())) {
                            pojo.setLatestUpdateByDesc(user.getUserName());
                        }
                    });
                    lstSquareHis.forEach(x -> {
                        PrmSquareDetailPDto pojo = (PrmSquareDetailPDto) x;
                        if (user.getUserId().equals(pojo.getLatestUpdateBy())) {
                            pojo.setLatestUpdateByDesc(user.getUserName());
                        }
                    });
                    lstReceiptsHis.forEach(x -> {
                        PrmReceiptsDetailPDto pojo = (PrmReceiptsDetailPDto) x;
                        if (user.getUserId().equals(pojo.getLatestUpdateBy())) {
                            pojo.setLatestUpdateByDesc(user.getUserName());
                        }
                    });
                    lstQsHis.forEach(x -> {
                        PrmQsPDto pojo = (PrmQsPDto) x;
                        if (user.getUserId().equals(pojo.getLatestUpdateBy())) {
                            pojo.setLatestUpdateByDesc(user.getUserName());
                        }
                        if (user.getUserId().equals(pojo.getSafePrincipal())) {
                            pojo.setSafePrincipalDesc(user.getUserName());
                        }
                        if (user.getUserId().equals(pojo.getSafeContact())) {
                            pojo.setSafeContactDesc(user.getUserName());
                        }
                        if (user.getUserId().equals(pojo.getQualityPrincipal())) {
                            pojo.setQualityPrincipalDesc(user.getUserName());
                        }
                        if (user.getUserId().equals(pojo.getQualityContact())) {
                            pojo.setQualityContactDesc(user.getUserName());
                        }
                    });
                }
            }
        }
        out.put(PrmProjectMainAttribute.PRM_ASSOCIATED_UNITS_HIS_DTO, lstAssociatedHis);
        out.put(PrmProjectMainAttribute.PRM_MEMBER_DETAIL_P_HIS_DTO, lstMemberHis);
        out.put(PrmProjectMainAttribute.PRM_PAY_DETAIL_P_HIS_DTO, lstPayHis);
        out.put(PrmProjectMainAttribute.PRM_PROGRESS_DETAIL_P_HIS_DTO, lstProgressHis);
        out.put(PrmProjectMainAttribute.PRM_SQUARE_DETAIL_P_HIS_DTO, lstSquareHis);
        out.put(PrmProjectMainAttribute.PRM_RECEIPTS_DETAIL_P_HIS_DTO, lstReceiptsHis);
        out.put(PrmProjectMainAttribute.PRM_QS_P_HIS_DTO, lstQsHis);
        //put to  return map

        return out;
    }

    private <T> List<T> loadProjectPlanDetailHistory(
            List<String> lstProjectChangeUuid, Class fromClazz, T toClazz, String[] compareFields) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        List<List<BasePojo>> lstOriGroup = new ArrayList<>();
        List<List<T>> lstHisGroup = new ArrayList<>();
        for (String projectChangeUuid : lstProjectChangeUuid) {
            Map mapCondition = new HashMap();
            mapCondition.put(PrmBudgetDetailCAttribute.PRM_PROJECT_MAIN_C_ID, projectChangeUuid);
            List<BasePojo> lstDetail = pcm.findByAnyFields(fromClazz, mapCondition, " seq_no asc ");
            if (lstDetail == null) {
                lstDetail = new ArrayList<>();
            }
            lstOriGroup.add(lstDetail);
        }

        int headerChangeTimes = lstProjectChangeUuid.size();
        //change version is the header change version
        for (int i = 0; i < headerChangeTimes; i++) {
            List<BasePojo> lstPreDetail = lstOriGroup.get(i);
            for (BasePojo currPojo : lstPreDetail) {
                List<T> lstHisDetail = new ArrayList<>();
                T newPojo1 = BeanFactory.getObject(toClazz);
                BeanUtil.bean2Bean(currPojo, newPojo1);
                BeanUtil.setProperty(newPojo1, PrmProjectMainAttribute.CHANGE_VERSION, i + 1);
                lstHisDetail.add(newPojo1);
                String currUuid = (String) BeanUtil.getProperty(currPojo, UUID);
                for (int j = i + 1; j < headerChangeTimes; j++) {
                    List<BasePojo> lstNextDetail = lstOriGroup.get(j);
                    BasePojo nextPojo = lstNextDetail.stream().filter(x -> ObjectUtil.beSame(BeanUtil.getProperty(x,
                            LAST_UUID), currUuid)).findAny().orElse(null);
                    if (nextPojo != null) {
                        if (ListUtil.isNotEmpty(getDifferentFields(currPojo, nextPojo, compareFields))) {
                            T newPojo2 = BeanFactory.getObject(toClazz);
                            BeanUtil.bean2Bean(nextPojo, newPojo2);
                            BeanUtil.setProperty(newPojo2, PrmProjectMainAttribute.CHANGE_VERSION, j + 1);
                            lstHisDetail.add(0, newPojo2);
                            currPojo = nextPojo;
                        }
                        lstNextDetail.remove(nextPojo);
                    } else {
                        BeanUtil.setProperty(lstHisDetail.get(0), PrmProjectMainAttribute
                                .CHANGE_STATUS, PrmProjectMainAttribute.CHANGE_STATUS_DELETE);
                        break;
                    }
                }
                lstHisGroup.add(lstHisDetail);
            }
        }

        lstHisGroup.forEach(x -> {
            int detailChangeTime = x.size();

            //按照变更的时间降序排列的
            for (int i = 0; i < detailChangeTime; i++) {
                T pojo = x.get(i);
                Integer headerVersion = (Integer) BeanUtil.getProperty(pojo, PrmProjectMainAttribute.CHANGE_VERSION);
                if (i == 0 && i == detailChangeTime - 1) {
                    if (headerVersion == 1) {
                        BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_VERSION, -1);
                    } else {
                        BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_VERSION, 0);
                    }
                    if (!PrmProjectMainAttribute.CHANGE_STATUS_DELETE.equals(BeanUtil.getProperty(pojo,
                            PrmProjectMainAttribute.CHANGE_STATUS))) {
                        BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_STATUS,
                                PrmProjectMainAttribute.CHANGE_STATUS_CURRENT);
                    }
                } else if (i == 0) {
                    BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_VERSION, detailChangeTime - i - 1);
                    if (!PrmProjectMainAttribute.CHANGE_STATUS_DELETE.equals(BeanUtil.getProperty(pojo,
                            PrmProjectMainAttribute.CHANGE_STATUS))) {
                        BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_STATUS,
                                PrmProjectMainAttribute.CHANGE_STATUS_CURRENT);
                    }
                } else if (i == detailChangeTime - 1) {
                    if (headerVersion == 1) {
                        BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_VERSION, -1);
                    } else {
                        BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_VERSION, 0);
                    }
                    BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_STATUS,
                            PrmProjectMainAttribute.CHANGE_STATUS_MODIFIED);
                } else {
                    BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_VERSION, detailChangeTime - i - 1);
                    BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_STATUS,
                            PrmProjectMainAttribute.CHANGE_STATUS_MODIFIED);
                }
                Date updateTime = (Date) BeanUtil.getProperty(pojo, CommonAttribute.UPDATE_TIME);
                Date createTime = (Date) BeanUtil.getProperty(pojo, CommonAttribute.CREATE_TIME);
                Date latestUpdateTime = updateTime != null ? updateTime : createTime;
                String updateBy = (String) BeanUtil.getProperty(pojo, CommonAttribute.UPDATE_BY);
                String createBy = (String) BeanUtil.getProperty(pojo, CommonAttribute.CREATE_BY);
                String latestUpdateBy = StringUtil.isNotEmpty(updateBy) ? updateBy : createBy;
                BeanUtil.setProperty(pojo, PrmProjectMainAttribute.LATEST_UPDATE_TIME, latestUpdateTime);
                BeanUtil.setProperty(pojo, PrmProjectMainAttribute.LATEST_UPDATE_BY, latestUpdateBy);
            }
        });

        final List<T> lstReturn = new ArrayList<>();
        lstHisGroup.stream().sorted((x, y) -> {
            T detailX = x.get(0);
            T detailY = y.get(0);
            String changeStatusX = (String) BeanUtil.getProperty(detailX, PrmProjectMainAttribute.CHANGE_STATUS);
            String changeStatusY = (String) BeanUtil.getProperty(detailY, PrmProjectMainAttribute.CHANGE_STATUS);
            if (ObjectUtil.beSame(changeStatusX, changeStatusY)) {
                Integer seqNoX = (Integer) BeanUtil.getProperty(detailX, CommonAttribute.SEQ_NO);
                Integer seqNoY = (Integer) BeanUtil.getProperty(detailY, CommonAttribute.SEQ_NO);
                if (seqNoX != null && seqNoY != null) {
                    return seqNoX.compareTo(seqNoY);
                } else if (seqNoX != null) {
                    return -1;
                } else if (seqNoY != null) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return getHistoryStatusIndex(changeStatusX).compareTo(getHistoryStatusIndex(changeStatusY));
            }
        }).forEach(x -> lstReturn.addAll(x));

        int i = 1;
        for (T pojo : lstReturn) {
            BeanUtil.setProperty(pojo, CommonAttribute.SEQ_NO, i++);
        }

        return lstReturn;
    }

    public Map<String, List<BasePojo>> loadProjectBudgetHistory(String mainUuid) {
        Map<String, List<BasePojo>> out = new HashMap<>();
        List<String> lstParam = new ArrayList<>();
        lstParam.add(mainUuid);
        //按照创建时间升序排列
        DAOMeta daoMeta = DAOHelper.getDAO("projectmain-dao", "prm-project-main-change-approved-all", lstParam);
        daoMeta.setNeedFilter(false);
        List<Map<String, Object>> lstChange = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isEmpty(lstChange)) {
            return out;
        }
        List<String> lstProjectChangeUuid = lstChange.stream().map(x -> (String) x.get(CommonAttribute.UUID)).collect
                (Collectors.toList());
        //load detail history one by one
        List lstBudgetDetail = loadProjectBudgetDetailHistory(lstProjectChangeUuid,
                PrmBudgetDetailC.class, PrmBudgetDetailDto.class, BUDGET_DETAIL_FIELDS);
        List lstBudgetOutsource = loadProjectBudgetDetailHistory(lstProjectChangeUuid,
                PrmBudgetOutsourceC.class, PrmBudgetOutsourceDto.class, BUDGET_OUTSOURCE_FIELDS);
        List lstBudgetPrincipal = loadProjectBudgetDetailHistory(lstProjectChangeUuid,
                PrmBudgetPrincipalC.class, PrmBudgetPrincipalDto.class, BUDGET_PRINCIPAL_FIELDS);
        List lstBudgetAccessory = loadProjectBudgetDetailHistory(lstProjectChangeUuid,
                PrmBudgetAccessoryC.class, PrmBudgetAccessoryDto.class, BUDGET_ACCESSORY_FIELDS);
        List lstBudgetRun = loadProjectBudgetDetailHistory(lstProjectChangeUuid,
                PrmBudgetRunC.class, PrmBudgetRunDto.class, BUDGET_RUN_FIELDS);
        //load the userId and reset the userName
        Set<String> userIdSet = new HashSet<>();
        lstBudgetDetail.forEach(x -> {
            PrmBudgetDetailDto pojo = (PrmBudgetDetailDto) x;
            userIdSet.add(pojo.getLatestUpdateBy());
        });
        lstBudgetOutsource.forEach(x -> {
            PrmBudgetOutsourceDto pojo = (PrmBudgetOutsourceDto) x;
            userIdSet.add(pojo.getLatestUpdateBy());
        });
        lstBudgetPrincipal.forEach(x -> {
            PrmBudgetPrincipalDto pojo = (PrmBudgetPrincipalDto) x;
            userIdSet.add(pojo.getLatestUpdateBy());
        });
        lstBudgetAccessory.forEach(x -> {
            PrmBudgetAccessoryDto pojo = (PrmBudgetAccessoryDto) x;
            userIdSet.add(pojo.getLatestUpdateBy());
        });
        lstBudgetRun.forEach(x -> {
            PrmBudgetRunDto pojo = (PrmBudgetRunDto) x;
            userIdSet.add(pojo.getLatestUpdateBy());
        });
        //staffid, latestupdateby, qsp
        if (userIdSet.size() > 0) {
            Map mapUserId = new HashMap();
            mapUserId.put(ScdpUserAttribute.USER_ID, new ArrayList<>(userIdSet));
            List<ScdpUser> lstUser = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, mapUserId, null);
            if (ListUtil.isNotEmpty(lstUser)) {
                for (ScdpUser user : lstUser) {
                    lstBudgetDetail.forEach(x -> {
                        PrmBudgetDetailDto pojo = (PrmBudgetDetailDto) x;
                        if (user.getUserId().equals(pojo.getLatestUpdateBy())) {
                            pojo.setLatestUpdateByDesc(user.getUserName());
                        }
                    });
                    lstBudgetOutsource.forEach(x -> {
                        PrmBudgetOutsourceDto pojo = (PrmBudgetOutsourceDto) x;
                        if (user.getUserId().equals(pojo.getLatestUpdateBy())) {
                            pojo.setLatestUpdateByDesc(user.getUserName());
                        }
                    });
                    lstBudgetPrincipal.forEach(x -> {
                        PrmBudgetPrincipalDto pojo = (PrmBudgetPrincipalDto) x;
                        if (user.getUserId().equals(pojo.getLatestUpdateBy())) {
                            pojo.setLatestUpdateByDesc(user.getUserName());
                        }
                    });
                    lstBudgetAccessory.forEach(x -> {
                        PrmBudgetAccessoryDto pojo = (PrmBudgetAccessoryDto) x;
                        if (user.getUserId().equals(pojo.getLatestUpdateBy())) {
                            pojo.setLatestUpdateByDesc(user.getUserName());
                        }
                    });
                    lstBudgetRun.forEach(x -> {
                        PrmBudgetRunDto pojo = (PrmBudgetRunDto) x;
                        if (user.getUserId().equals(pojo.getLatestUpdateBy())) {
                            pojo.setLatestUpdateByDesc(user.getUserName());
                        }
                        pojo.setFinancialSubjectCodeDesc(this.getFinancialSubjectCodeDesc(pojo.getFinancialSubjectCode()));
                    });
                }
            }
        }
        out.put(PrmProjectMainAttribute.PRM_BUDGET_DETAIL_HIS_DTO, lstBudgetDetail);
        out.put(PrmProjectMainAttribute.PRM_BUDGET_OUTSOURCE_HIS_DTO, lstBudgetOutsource);
        out.put(PrmProjectMainAttribute.PRM_BUDGET_PRINCIPAL_HIS_DTO, lstBudgetPrincipal);
        out.put(PrmProjectMainAttribute.PRM_BUDGET_ACCESSORY_HIS_DTO, lstBudgetAccessory);
        out.put(PrmProjectMainAttribute.PRM_BUDGET_RUN_HIS_DTO, lstBudgetRun);
        //put to  return map

        return out;
    }

    private <T> List<T> loadProjectBudgetDetailHistory(
            List<String> lstProjectChangeUuid, Class fromClazz, T toClazz, String[] compareFields) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        List<List<BasePojo>> lstOriGroup = new ArrayList<>();
        List<List<T>> lstHisGroup = new ArrayList<>();
        for (String projectChangeUuid : lstProjectChangeUuid) {
            Map mapCondition = new HashMap();
            mapCondition.put(PrmBudgetDetailCAttribute.PRM_PROJECT_MAIN_C_ID, projectChangeUuid);
            List<BasePojo> lstDetail = pcm.findByAnyFields(fromClazz, mapCondition, " seq_no asc ");
            if (lstDetail == null) {
                lstDetail = new ArrayList<>();
            }
            lstOriGroup.add(lstDetail);
        }

        int headerChangeTimes = lstProjectChangeUuid.size();
        //change version is the header change version for below logic
        for (int i = 0; i < headerChangeTimes; i++) {
            List<BasePojo> lstPreDetail = lstOriGroup.get(i);
            for (BasePojo currPojo : lstPreDetail) {
                List<T> lstHisDetail = new ArrayList<>();
                T newPojo1 = BeanFactory.getObject(toClazz);
                BeanUtil.bean2Bean(currPojo, newPojo1);
                BeanUtil.setProperty(newPojo1, PrmProjectMainAttribute.CHANGE_VERSION, i + 1);
                lstHisDetail.add(newPojo1);
                String currUuid = (String) BeanUtil.getProperty(currPojo, UUID);
                for (int j = i + 1; j < headerChangeTimes; j++) {
                    List<BasePojo> lstNextDetail = lstOriGroup.get(j);
                    BasePojo nextPojo = lstNextDetail.stream().filter(x -> ObjectUtil.beSame(BeanUtil.getProperty(x,
                            LAST_UUID), currUuid)).findAny().orElse(null);
                    if (nextPojo != null) {
                        if (ListUtil.isNotEmpty(getDifferentFields(currPojo, nextPojo, compareFields))) {
                            T newPojo2 = BeanFactory.getObject(toClazz);
                            BeanUtil.bean2Bean(nextPojo, newPojo2);
                            BeanUtil.setProperty(newPojo2, PrmProjectMainAttribute.CHANGE_VERSION, j + 1);
                            lstHisDetail.add(0, newPojo2);
                            currPojo = nextPojo;
                        }
                        lstNextDetail.remove(nextPojo);
                    } else {
                        BeanUtil.setProperty(lstHisDetail.get(0), PrmProjectMainAttribute
                                .CHANGE_STATUS, PrmProjectMainAttribute.CHANGE_STATUS_DELETE);
                        break;
                    }
                }
                lstHisGroup.add(lstHisDetail);
            }
        }

        lstHisGroup.forEach(x -> {
            int detailChangeTime = x.size();

            //按照变更的时间降序排列的
            for (int i = 0; i < detailChangeTime; i++) {
                T pojo = x.get(i);
                Integer headerVersion = (Integer) BeanUtil.getProperty(pojo, PrmProjectMainAttribute.CHANGE_VERSION);
                if (i == 0 && i == detailChangeTime - 1) {
                    if (headerVersion == 1) {
                        BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_VERSION, -1);
                    } else {
                        BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_VERSION, 0);
                    }
                    if (!PrmProjectMainAttribute.CHANGE_STATUS_DELETE.equals(BeanUtil.getProperty(pojo,
                            PrmProjectMainAttribute.CHANGE_STATUS))) {
                        BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_STATUS,
                                PrmProjectMainAttribute.CHANGE_STATUS_CURRENT);
                    }
                } else if (i == 0) {
                    BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_VERSION, detailChangeTime - i - 1);
                    if (!PrmProjectMainAttribute.CHANGE_STATUS_DELETE.equals(BeanUtil.getProperty(pojo,
                            PrmProjectMainAttribute.CHANGE_STATUS))) {
                        BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_STATUS,
                                PrmProjectMainAttribute.CHANGE_STATUS_CURRENT);
                    }
                } else if (i == detailChangeTime - 1) {
                    if (headerVersion == 1) {
                        BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_VERSION, -1);
                    } else {
                        BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_VERSION, 0);
                    }
                    BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_STATUS,
                            PrmProjectMainAttribute.CHANGE_STATUS_MODIFIED);
                } else {
                    BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_VERSION, detailChangeTime - i - 1);
                    BeanUtil.setProperty(pojo, PrmProjectMainAttribute.CHANGE_STATUS,
                            PrmProjectMainAttribute.CHANGE_STATUS_MODIFIED);
                }
                Date updateTime = (Date) BeanUtil.getProperty(pojo, CommonAttribute.UPDATE_TIME);
                Date createTime = (Date) BeanUtil.getProperty(pojo, CommonAttribute.CREATE_TIME);
                Date latestUpdateTime = updateTime != null ? updateTime : createTime;
                String updateBy = (String) BeanUtil.getProperty(pojo, CommonAttribute.UPDATE_BY);
                String createBy = (String) BeanUtil.getProperty(pojo, CommonAttribute.CREATE_BY);
                String latestUpdateBy = StringUtil.isNotEmpty(updateBy) ? updateBy : createBy;
                BeanUtil.setProperty(pojo, PrmProjectMainAttribute.LATEST_UPDATE_TIME, latestUpdateTime);
                BeanUtil.setProperty(pojo, PrmProjectMainAttribute.LATEST_UPDATE_BY, latestUpdateBy);
            }
        });

        final List<T> lstReturn = new ArrayList<>();
        lstHisGroup.stream().sorted((x, y) -> {
            T detailX = x.get(0);
            T detailY = y.get(0);
            String changeStatusX = (String) BeanUtil.getProperty(detailX, PrmProjectMainAttribute.CHANGE_STATUS);
            String changeStatusY = (String) BeanUtil.getProperty(detailY, PrmProjectMainAttribute.CHANGE_STATUS);
            if (ObjectUtil.beSame(changeStatusX, changeStatusY)) {
                String serialNumX = (String) BeanUtil.getProperty(detailX, PrmBudgetDetailAttribute.SERIAL_NUMBER);
                String serialNumY = (String) BeanUtil.getProperty(detailY, PrmBudgetDetailAttribute.SERIAL_NUMBER);
                return formatSerialNumber(serialNumX).compareTo(formatSerialNumber(serialNumY));
            } else {
                return getHistoryStatusIndex(changeStatusX).compareTo(getHistoryStatusIndex(changeStatusY));
            }
        }).forEach(x -> lstReturn.addAll(x));

        int i = 1;
        for (T pojo : lstReturn) {
            BeanUtil.setProperty(pojo, CommonAttribute.SEQ_NO, i++);
        }

        return lstReturn;
    }

    private Integer getHistoryStatusIndex(String status) {
        return ArrayUtils.indexOf(HISTORY_STATUS, status);
    }

    private List<Map<String, Object>> getDestInnerProjectInfo(String prmProjectMainId) {
        String sql = "select t1.uuid as prm_purchase_req_id,t2.uuid as prm_contract_id," +
                "t3.uuid as prm_project_main_id,t1.purchase_req_no,t2.contract_name,t2.contract_now_money," +
                "t3.project_name,t3.project_code ," +
                "(select sum(nvl(t5.handle_amount,t5.amount)*t5.expected_price) from prm_purchase_req_detail t5 " +
                "where t5.prm_purchase_req_id=t1.uuid and (t5.isfallback is null or t5.isfallback!=1) ) as " +
                "purchase_req_amount,'下家' as direction " +
                " from prm_purchase_req t1 " +
                "left join prm_contract t2 on t2.inner_purchase_req_id=t1.uuid " +
                "left join prm_contract_detail t4 on t4.prm_contract_id=t2.uuid " +
                "left join prm_project_main t3 on t3.uuid=t4.prm_project_main_id " +
                "where t1.is_inner=1 " +
                "and (t1.state is null or t1.state !='3') " +
                "and t1.prm_project_main_id=? ";
        List lstParam = new ArrayList<>();
        lstParam.add(prmProjectMainId);
        DAOMeta daoMeta = new DAOMeta(sql, lstParam);
        daoMeta.setNeedFilter(false);
        return PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
    }

    private List<Map<String, Object>> getOriginalInnerProjectInfo(String prmProjectMainId) {
        String sql = "select t3.uuid as prm_purchase_req_id,t2.uuid as prm_contract_id," +
                "t4.uuid as prm_project_main_id,t3.purchase_req_no,t2.contract_name,t2.contract_now_money," +
                "t4.project_name,t4.project_code," +
                "(select sum(nvl(t5.handle_amount,t5.amount)*t5.expected_price) from prm_purchase_req_detail t5 " +
                "where t5.prm_purchase_req_id=t3.uuid and (t5.isfallback is null or t5.isfallback!=1) ) as " +
                "purchase_req_amount,'上家' as direction " +
                "from prm_project_main t1 " +
                "inner join prm_contract_detail t5 on t5.prm_project_main_id=t1.uuid " +
                "inner join prm_contract t2 on t2.uuid=t5.prm_contract_id " +
                "inner join prm_purchase_req t3 on t3.uuid=t2.inner_purchase_req_id " +
                "inner join prm_project_main t4 on t3.prm_project_main_id=t4.uuid " +
                "where t3.is_inner=1 and t1.uuid=? ";
        List lstParam = new ArrayList<>();
        lstParam.add(prmProjectMainId);
        DAOMeta daoMeta = new DAOMeta(sql, lstParam);
        daoMeta.setNeedFilter(false);
        return PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
    }

    public String getBudgetCodeDesc(String budgetCode) {
        if (StringUtil.isEmpty(budgetCode)) {
            return "";
        }
        String sql = "select t.subject_name as codedesc,t.subject_no as code from fad_base_subject t\n" +
                "where t.subject_type='1' and t.subject_no=? ";
        List lstParam = new ArrayList();
        lstParam.add(budgetCode);
        DAOMeta daoMeta = new DAOMeta(sql, lstParam);
        List<Map<String, Object>> retList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(retList)) {
            return (String) retList.get(0).get("codedesc");
        }
        return "";
    }

    public String getPostDesc(String post) {
        if (StringUtil.isEmpty(post)) {
            return "";
        }
        String sql = "select r.uuid as code,r.role_name as codeDesc From scdp_role r where  r.uuid = ? ";
        List lstParam = new ArrayList();
        lstParam.add(post);
        DAOMeta daoMeta = new DAOMeta(sql, lstParam);
        List<Map<String, Object>> retList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(retList)) {
            return (String) retList.get(0).get("codedesc");
        }
        return "";
    }

    public String getFinancialSubjectCodeDesc(String financialSubjectCode) {
        if (StringUtil.isEmpty(financialSubjectCode)) {
            return "";
        }
        String sql = "SELECT T.SUBJECT_NAME AS CODEDESC, T.SUBJECT_NO AS CODE\n" +
                "                      FROM FAD_BASE_SUBJECT T\n" +
                "                     WHERE T.SUBJECT_TYPE = '2' and T.SUBJECT_NO=? ";
        List lstParam = new ArrayList();
        lstParam.add(financialSubjectCode);
        DAOMeta daoMeta = new DAOMeta(sql, lstParam);
        List<Map<String, Object>> retList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(retList)) {
            return (String) retList.get(0).get("codedesc");
        }
        return "";
    }

    public List<Map<String, Object>> getInnerProjectInfos(String prmProjectMainId) {
        if (StringUtil.isEmpty(prmProjectMainId)) {
            return null;
        }
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmProjectMain projectMain = pcm.findByPK(PrmProjectMain.class, prmProjectMainId);
        if (projectMain == null) {
            return null;
        }
        List<Map<String, Object>> lstReturn = new ArrayList<>();
        List lstOri = getOriginalInnerProjectInfo(projectMain.getUuid());
        List lstDest = getDestInnerProjectInfo(projectMain.getUuid());
        if (ListUtil.isNotEmpty(lstOri)) {
            lstReturn.addAll(lstOri);
        }
        if (ListUtil.isNotEmpty(lstDest)) {
            lstReturn.addAll(lstDest);
        }
        return lstReturn;
    }

    @Override
    public PrmProjectMainDto loadExtraDescField(PrmProjectMainDto projectMainDto) {

        if (projectMainDto == null) {
            return projectMainDto;
        }
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        if (StringUtil.isNotEmpty(projectMainDto.getContractorOffice())) {
            projectMainDto.setContractorOfficeDesc(OrgHelper.getOrgNameByCode(projectMainDto.getContractorOffice()));
        }

        String projectManagerId = projectMainDto.getProjectManager();
        List<String> lstUserId = new ArrayList<>();

        List<PrmMemberDetailPDto> lstMember = projectMainDto.getPrmMemberDetailPDto();
        if (ListUtil.isNotEmpty(lstMember)) {
            for (PrmMemberDetailPDto detailDto : lstMember) {
                if (StringUtil.isNotEmpty(detailDto.getPost())) {
                    String postDesc = this.getPostDesc(detailDto.getPost());
                    detailDto.setPostDesc(postDesc);
                    detailDto.setJobShareDesc(FMCodeHelper.getDescByCode(detailDto.getJobShare(), "JOB_SHARE"));
                }
            }
            lstUserId = lstMember.stream().filter(x -> StringUtil.isNotEmpty(x)).map(x -> x.getStaffId()).distinct().collect(Collectors.toList());
        }
        if (StringUtil.isNotEmpty(projectManagerId) && !lstUserId.contains(projectManagerId)) {
            lstUserId.add(projectManagerId);
        }


        List<PrmQsPDto> lstQsp = projectMainDto.getPrmQsPDto();
        if (ListUtil.isNotEmpty(lstQsp)) {
            for (PrmQsPDto qsPDto : lstQsp) {
                if (StringUtil.isNotEmpty(qsPDto.getSafePrincipal()) && !lstUserId.contains(qsPDto.getSafePrincipal())) {
                    lstUserId.add(qsPDto.getSafePrincipal());
                }
                if (StringUtil.isNotEmpty(qsPDto.getSafeContact()) && !lstUserId.contains(qsPDto.getSafeContact())) {
                    lstUserId.add(qsPDto.getSafeContact());
                }
                if (StringUtil.isNotEmpty(qsPDto.getQualityPrincipal()) && !lstUserId.contains(qsPDto.getQualityPrincipal())) {
                    lstUserId.add(qsPDto.getQualityPrincipal());
                }
                if (StringUtil.isNotEmpty(qsPDto.getQualityContact()) && !lstUserId.contains(qsPDto.getQualityContact())) {
                    lstUserId.add(qsPDto.getQualityContact());
                }
            }
        }

        if (ListUtil.isNotEmpty(lstUserId)) {
            Map mapUserId = new HashMap();
            mapUserId.put(ScdpUserAttribute.USER_ID, lstUserId);
            List<ScdpUser> lstUser = pcm.findByAnyFields(ScdpUser.class, mapUserId, null);
            if (ListUtil.isNotEmpty(lstUser)) {
                for (ScdpUser user : lstUser) {
                    if (user.getUserId().equals(projectManagerId)) {
                        projectMainDto.setProjectManagerDesc(user.getUserName());
                    }

                    if (ListUtil.isNotEmpty(lstQsp)) {
                        for (PrmQsPDto qsPDto : lstQsp) {
                            if (StringUtil.isNotEmpty(qsPDto.getSafePrincipal()) && user.getUserId().equals(qsPDto
                                    .getSafePrincipal())) {
                                qsPDto.setSafePrincipalDesc(user.getUserName());
                            }
                            if (StringUtil.isNotEmpty(qsPDto.getSafeContact()) && user.getUserId().equals(qsPDto.getSafeContact())) {
                                qsPDto.setSafeContactDesc(user.getUserName());
                            }
                            if (StringUtil.isNotEmpty(qsPDto.getQualityPrincipal()) && user.getUserId().equals(qsPDto.getQualityPrincipal())) {
                                qsPDto.setQualityPrincipalDesc(user.getUserName());
                            }
                            if (StringUtil.isNotEmpty(qsPDto.getQualityContact()) && user.getUserId().equals(qsPDto.getQualityContact())) {
                                qsPDto.setQualityContactDesc(user.getUserName());
                            }
                        }
                    }
                }
                if (ListUtil.isNotEmpty(lstMember)) {
                    lstMember.stream().forEach(x -> {
                        Optional<ScdpUser> userInfo = lstUser.stream().filter(y -> y.getUserId().equals(x.getStaffId()))
                                .findFirst();
                        if (userInfo.isPresent()) {
                            x.setStaffIdDesc(userInfo.get().getUserName());
                        }
                    });
                }
            }
        }
        List<String> lstPartyId = new ArrayList<String>();
        List<String> lstContractId = new ArrayList<String>();

        List<PrmContractDetailDto> lstContractDetail = projectMainDto.getPrmContractDetailDto();
        if (ListUtil.isNotEmpty(lstContractDetail)) {
            for (PrmContractDetailDto contractDetailDto : lstContractDetail) {
                if (StringUtil.isNotEmpty(contractDetailDto.getPrmContractId())) {
                    lstContractId.add(contractDetailDto.getPrmContractId());
                }
                if (StringUtil.isNotEmpty(contractDetailDto.getCustomerId())) {
                    lstPartyId.add(contractDetailDto.getCustomerId());
                }
            }
        }

        List<PrmContract> lstContract = new ArrayList<>();
        List<Map<String, Object>> mapParty = new ArrayList<>();
        if (ListUtil.isNotEmpty(lstPartyId)) {
            //已经删除的老业主单位需要code转name，所以采用sql查询的方式查出所有数据，包括is_void=1的
            String sql = "select * from prm_customer t where t.uuid in (" + StringUtil.joinForSqlIn(lstPartyId, ",") + ")";
            mapParty = pcm.findByNativeSQL(new DAOMeta(sql));
            Map mapContractId = new HashMap();
            mapContractId.put(com.csnt.scdp.bizmodules.attributes.CommonAttribute.UUID, lstContractId);
            lstContract = pcm.findByAnyFields(PrmContract.class, mapContractId, null);
        }

        List innerPurchaseReqIdList = new ArrayList<>();
        Map innerProjectCodeMap = new HashMap<>();
        if (ListUtil.isNotEmpty(lstContract)) {
            for (PrmContract contract : lstContract) {
                if (StringUtil.isNotEmpty(contract.getInnerPurchaseReqId())) {
                    innerPurchaseReqIdList.add(contract.getInnerPurchaseReqId());
                }
            }
            if (ListUtil.isNotEmpty(innerPurchaseReqIdList)) {
                DAOMeta daoMeta = new DAOMeta();
                String sql = "SELECT PR.UUID," +
                        "       (SELECT PM.PROJECT_CODE" +
                        "          FROM PRM_PROJECT_MAIN PM" +
                        "         WHERE PM.UUID = PR.PRM_PROJECT_MAIN_ID) AS PROJECT_CODE" +
                        "  FROM PRM_PURCHASE_REQ PR" +
                        " WHERE PR.UUID IN (" + StringUtil.joinForSqlIn(innerPurchaseReqIdList, ",") + ")";
                daoMeta.setStrSql(sql);
                List innerProjectCodeList = pcm.findByNativeSQL(daoMeta);
                for (Object o : innerProjectCodeList) {
                    Map m = (Map) o;
                    innerProjectCodeMap.put(m.get("uuid"), m.get("projectCode"));
                }
            }
        }
        if (ListUtil.isNotEmpty(lstContractDetail)) {
            for (PrmContractDetailDto contractDetailDto : lstContractDetail) {

                if (ListUtil.isNotEmpty(mapParty)) {
                    for (Map party : mapParty) {
                        if (((String) party.get("uuid")).equals(contractDetailDto.getCustomerId())) {
                            contractDetailDto.setCustomerName((String) party.get("customerName"));
                            break;
                        }
                    }
                }
                if (ListUtil.isNotEmpty(lstContract)) {
                    for (PrmContract contract : lstContract) {
                        if (contract.getUuid().equals(contractDetailDto.getPrmContractId())) {
                            contractDetailDto.setContractName(contract.getContractName());
                            contractDetailDto.setContractSignMoney(contract.getContractSignMoney());
                            contractDetailDto.setIsBusinessTax(contract.getIsBusinessTax());
                            contractDetailDto.setTaxType(contract.getTaxType());
                            contractDetailDto.setPrmCodeType(contract.getPrmCodeType());
                            if (StringUtil.isNotEmpty(contract.getInnerPurchaseReqId())) {
                                contractDetailDto.setInnerProjectCode("" + innerProjectCodeMap.get(contract.getInnerPurchaseReqId()));
                            }
                        }
                    }
                }
            }
        }

        List<PrmBudgetDetailDto> lstDetail = projectMainDto.getPrmBudgetDetailDto();
        String vatType = getPrmTaxType(projectMainDto.getUuid());
        if (ListUtil.isNotEmpty(lstDetail) && ContractTaxType.VAT_TAX.equals(vatType)) {
            for (PrmBudgetDetailDto detailDto : lstDetail) {
                String budgetCodeDesc = this.getBudgetCodeDesc(detailDto.getBudgetCode());
                detailDto.setBudgetCodeDesc(budgetCodeDesc);
                if (ArrayUtil.contains(PrmBudgetCodes.CAL_EXCLUDING_VAT_ITEMS, detailDto.getBudgetCode())) {
                    detailDto.setExcludingVatAmount(MathUtil.sub(detailDto.getCostControlMoney(), detailDto.getVatAmount()));
                } else if (PrmBudgetCodes.TECHNICAL_FEE_H.equals(detailDto.getBudgetCode())) {
                    detailDto.setExcludingVatAmount(MathUtil.sub(detailDto.getContractMoney(), detailDto.getVatAmount()));
                }
            }
        }

        fillPurchasePlanDetailInfo(projectMainDto);

        List<PrmBudgetRunDto> prmBudgetRunDtos = projectMainDto.getPrmBudgetRunDto();
        if (ListUtil.isNotEmpty(prmBudgetRunDtos)) {

            DAOMeta daoMeta = new DAOMeta();
            String sql = "SELECT  T.SUBJECT_NO AS CODE , T.SUBJECT_NAME AS CODEDESC" +
                    "                      FROM FAD_BASE_SUBJECT T" +
                    "                     WHERE T.SUBJECT_TYPE = '2' and T.is_void='0'" +
                    "                     ORDER BY T.subject_no asc";
            daoMeta.setStrSql(sql);
            List<Map<String, Object>> runBudgetTypes = pcm.findByNativeSQL(daoMeta);
            Map runBudgetTypeMap = new HashMap<>();
            for (Map runBudgetType : runBudgetTypes) {
                runBudgetTypeMap.put(runBudgetType.get("code"), runBudgetType.get("codedesc"));
            }
            for (PrmBudgetRunDto prmBudgetRunDto : prmBudgetRunDtos) {
                prmBudgetRunDto.setFinancialSubjectCodeDesc((String) runBudgetTypeMap.get(prmBudgetRunDto.getFinancialSubjectCode()));
            }
        }
        return projectMainDto;
    }

    //填充主材、辅材、外协的 采购额度剩余 purchaseLimitLeft
    private void fillPurchasePlanDetailInfo(PrmProjectMainDto projectMainDto) {
        PurchaseplanManager purchaseplanManager = BeanFactory.getBean("purchaseplan-manager");
        Map<String, Map<String, Map<String, Object>>> purchaseInfoMap = purchaseplanManager.getPurchasePlanDetailInfo
                (projectMainDto.getUuid());
        if (MapUtil.isNotEmpty(purchaseInfoMap)) {
            //外协
            if (purchaseInfoMap.containsKey(PrmProjectMainAttribute.OUTSOURCE) &&
                    projectMainDto.getPrmBudgetOutsourceDto() != null) {
                Map<String, Map<String, Object>> purchaseDetailMap = purchaseInfoMap.get(PrmProjectMainAttribute
                        .OUTSOURCE);
                Map<String, List<PrmBudgetOutsourceDto>> outSource = projectMainDto.getPrmBudgetOutsourceDto().stream().collect(Collectors.groupingBy(PrmBudgetOutsourceDto::getUuid));
                projectMainDto.getPrmBudgetOutsourceDto().stream().forEach(t -> {
                    if (StringUtil.isNotEmpty(t.getUuid()) && purchaseDetailMap.containsKey(t.getUuid())) {
                        t.setPurchaseLimitLeft(t.getTotalValue().subtract((BigDecimal) purchaseDetailMap.get(t.getUuid()).get("lockedMoney")));
                    } else if (StringUtil.isNotEmpty(t.getSplitFromUuid())) {
                        if (outSource.containsKey(t.getSplitFromUuid())) {
                            PrmBudgetOutsourceDto c = outSource.get(t.getSplitFromUuid()).get(0);
                            if (StringUtil.isNotEmpty(c.getUuid()) && purchaseDetailMap.containsKey(c.getUuid())) {
                                t.setPurchaseLimitLeft(t.getTotalValue().subtract((BigDecimal) purchaseDetailMap.get(c.getUuid()).get("lockedMoney")));
                            }
                        }
                    }
                });
            }
            //设备
            if (purchaseInfoMap.containsKey(PrmProjectMainAttribute.PRINCIPAL) &&
                    projectMainDto.getPrmBudgetPrincipalDto() != null) {
                Map<String, Map<String, Object>> purchaseDetailMap = purchaseInfoMap.get(PrmProjectMainAttribute
                        .PRINCIPAL);
                Map<String, List<PrmBudgetPrincipalDto>> principal = projectMainDto.getPrmBudgetPrincipalDto().stream().collect(Collectors.groupingBy(PrmBudgetPrincipalDto::getUuid));
                projectMainDto.getPrmBudgetPrincipalDto().stream().forEach(t -> {
                    if (StringUtil.isNotEmpty(t.getUuid()) && purchaseDetailMap.containsKey(t.getUuid())) {
                        t.setPurchaseLimitLeft(t.getSchemingTotalValue().subtract((BigDecimal) purchaseDetailMap.get(t.getUuid()).get("lockedMoney")));
                    } else {
                        if (principal.containsKey(t.getSplitFromUuid())) {
                            PrmBudgetPrincipalDto c = principal.get(t.getSplitFromUuid()).get(0);
                            if (StringUtil.isNotEmpty(c.getUuid()) && purchaseDetailMap.containsKey(c.getUuid())) {
                                t.setPurchaseLimitLeft(t.getSchemingTotalValue().subtract((BigDecimal) purchaseDetailMap.get(c.getUuid()).get("lockedMoney")));
                            }
                        }
                    }
                });
            }
            //辅材
            if (purchaseInfoMap.containsKey(PrmProjectMainAttribute.ACCESSORY) &&
                    projectMainDto.getPrmBudgetAccessoryDto() != null) {
                Map<String, Map<String, Object>> purchaseDetailMap = purchaseInfoMap.get(PrmProjectMainAttribute
                        .ACCESSORY);

                Map<String, List<PrmBudgetAccessoryDto>> accessory = projectMainDto.getPrmBudgetAccessoryDto().stream().collect(Collectors.groupingBy(PrmBudgetAccessoryDto::getUuid));

                projectMainDto.getPrmBudgetAccessoryDto().stream().forEach(t -> {
                    if (StringUtil.isNotEmpty(t.getUuid()) && purchaseDetailMap.containsKey(t.getUuid())) {
                        t.setPurchaseLimitLeft(t.getTotalValue().subtract((BigDecimal) purchaseDetailMap.get(t.getUuid()).get("lockedMoney")));
                    } else {
                        if (accessory.containsKey(t.getSplitFromUuid())) {
                            PrmBudgetAccessoryDto c = accessory.get(t.getSplitFromUuid()).get(0);
                            if (StringUtil.isNotEmpty(c.getUuid()) && purchaseDetailMap.containsKey(c.getUuid())) {
                                t.setPurchaseLimitLeft(t.getTotalValue().subtract((BigDecimal) purchaseDetailMap.get(t.getUuid()).get("lockedMoney")));
                            }
                        }
                    }
                });
            }

        }
    }

    public PrmProjectMainCDto loadAllDifferenceData(String prmProjectReviseUuid, String prmProjectMainUuid) {
        //按照创建时间升序排列
        List<String> lstParam = new ArrayList<>();
        lstParam.add(prmProjectMainUuid);
        DAOMeta daoMeta = DAOHelper.getDAO("projectmain-dao", "prm-project-main-change-all", lstParam);
        daoMeta.setNeedFilter(false);
        List<Map<String, Object>> lstChange = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        String previousUuid = null;
        if (ListUtil.isNotEmpty(lstChange) && lstChange.size() > 1) {
            for (int i = lstChange.size() - 1; i > 0; i--) {
                Map mapPrmChange = lstChange.get(i);
                if (prmProjectReviseUuid.equals(mapPrmChange.get(CommonAttribute.UUID))) {
                    previousUuid = (String) lstChange.get(i - 1).get(CommonAttribute.UUID);
                    break;
                }
            }
        }

        PrmProjectMainCDto currDto = (PrmProjectMainCDto) DtoHelper.findDtoByPK(PrmProjectMainCDto.class, prmProjectReviseUuid);
        if (StringUtil.isEmpty(previousUuid)) {
            return currDto;
        }
        PrmProjectMainCDto previousDto = (PrmProjectMainCDto) DtoHelper.findDtoByPK(PrmProjectMainCDto.class,
                previousUuid);

        PrmProjectMainC currPojo = new PrmProjectMainC();
        BeanUtil.bean2Bean(currDto, currPojo);
        PrmProjectMain oldPojo = new PrmProjectMain();
        BeanUtil.bean2Bean(previousDto, oldPojo);
        Map<String, Object> currHeaderMap = BeanUtil.bean2Map(currPojo);
        Map<String, Object> oldHeaderMap = BeanUtil.bean2Map(oldPojo);
        Set<String> keySet = currHeaderMap.keySet();
        List<String> lstChangedField = new ArrayList<String>();
        Map headerChangedValueMap = new HashMap();
        for (String key : keySet) {
            if (!ObjectUtil.beSame(currHeaderMap.get(key), oldHeaderMap.get(key))) {
                if (!ArrayUtil.contains(HEADER_IGNORE_FIELDS, key)) {
                    lstChangedField.add(key);
                    if (PrmProjectMainCAttribute.SCHEDULED_END_DATE.equals(key)) {
                        if (oldHeaderMap.get(key) != null) {
                            headerChangedValueMap.put(key, DateUtil.formatDate((Date) oldHeaderMap.get(key), "yyyy-MM-dd"));
                        } else {
                            headerChangedValueMap.put(key, null);
                        }
                    } else if (PrmProjectMainCAttribute.PROJECT_MONEY.equals(key) || PrmProjectMainCAttribute
                            .COST_CONTROL_MONEY.equals(key)) {
                        headerChangedValueMap.put(key, oldHeaderMap.get(key));
                    } else if (PrmProjectMainCAttribute.TAX_TYPE.equals(key)) {
                        if (oldHeaderMap.get(key) != null) {
                            headerChangedValueMap.put(key, FMCodeHelper.getDescByCode((String) oldHeaderMap.get(key),
                                    "CDM_TAX_TYPE"));
                        } else {
                            headerChangedValueMap.put(key, null);
                        }
                    }
                }
            }
        }
        currDto.setHeaderChangedValues(headerChangedValueMap);
        currDto.setChangedFields(StringUtil.joinList(lstChangedField, "|"));

        List<PrmContractDetailCDto> lstContractDetail = getDetailComparedRecords(currDto.getPrmContractDetailCDto(),
                previousDto.getPrmContractDetailCDto(), CONTRACT_DETAIL_FIELD, PrmContractDetailCDto.class,
                PrmProjectMainCAttribute.PRM_CONTRACT_ID, PrmProjectMainCAttribute.PRM_CONTRACT_ID);
        currDto.setPrmContractDetailCDto(lstContractDetail);

        List<PrmAssociatedUnitsCDto> lstAssociatedDetail = getDetailComparedRecords(currDto.getPrmAssociatedUnitsCDto(), previousDto
                        .getPrmAssociatedUnitsCDto(), ASSOCIATED_UNITS_FIELDS,
                PrmAssociatedUnitsCDto.class
        );
        currDto.setPrmAssociatedUnitsCDto(lstAssociatedDetail);

        List<PrmMemberDetailPCDto> lstMemberDetail = getDetailComparedRecords(currDto.getPrmMemberDetailPCDto(), previousDto
                .getPrmMemberDetailPCDto(), MEMBER_DETAIL_FIELDS, PrmMemberDetailPCDto.class);
        currDto.setPrmMemberDetailPCDto(lstMemberDetail);

        List<PrmPayDetailPCDto> lstPayDetail = getDetailComparedRecords(currDto.getPrmPayDetailPCDto(), previousDto
                .getPrmPayDetailPCDto(), PAY_DETAIL_FIELDS, PrmPayDetailPCDto.class);
        currDto.setPrmPayDetailPCDto(lstPayDetail);

        List<PrmProgressDetailPCDto> lstProgressDetail = getDetailComparedRecords(currDto.getPrmProgressDetailPCDto(), previousDto
                .getPrmProgressDetailPCDto(), PROGRESS_DETAIL_FIELDS, PrmProgressDetailPCDto.class);
        currDto.setPrmProgressDetailPCDto(lstProgressDetail);

        List<PrmSquareDetailPCDto> lstSquareDetail = getDetailComparedRecords(currDto.getPrmSquareDetailPCDto(), previousDto
                .getPrmSquareDetailPCDto(), SQUARE_DETAIL_FIELDS, PrmSquareDetailPCDto.class);
        currDto.setPrmSquareDetailPCDto(lstSquareDetail);

        List<PrmReceiptsDetailPCDto> lstReceitpsDetail = getDetailComparedRecords(currDto.getPrmReceiptsDetailPCDto(), previousDto
                .getPrmReceiptsDetailPCDto(), RECEIPTS_DETAIL_FIELDS, PrmReceiptsDetailPCDto.class);
        currDto.setPrmReceiptsDetailPCDto(lstReceitpsDetail);

        List<PrmQsPCDto> lstQsDetail = getDetailComparedRecords(currDto.getPrmQsPCDto(), previousDto
                .getPrmQsPCDto(), QS_DETAIL_FIELDS, PrmQsPCDto.class);
        currDto.setPrmQsPCDto(lstQsDetail);

        List<PrmBudgetDetailCDto> lstBudgetDetail = getDetailComparedRecords(currDto.getPrmBudgetDetailCDto(), previousDto
                .getPrmBudgetDetailCDto(), BUDGET_DETAIL_FIELDS, PrmBudgetDetailCDto.class);
        currDto.setPrmBudgetDetailCDto(lstBudgetDetail);

        List<PrmBudgetOutsourceCDto> lstOutsourceDetail = getDetailComparedRecords(currDto.getPrmBudgetOutsourceCDto(), previousDto
                .getPrmBudgetOutsourceCDto(), BUDGET_OUTSOURCE_FIELDS, PrmBudgetOutsourceCDto.class);
        currDto.setPrmBudgetOutsourceCDto(lstOutsourceDetail);

        List<PrmBudgetPrincipalCDto> lstPrincipalDetail = getDetailComparedRecords(currDto.getPrmBudgetPrincipalCDto(), previousDto
                .getPrmBudgetPrincipalCDto(), BUDGET_PRINCIPAL_FIELDS, PrmBudgetPrincipalCDto.class);
        currDto.setPrmBudgetPrincipalCDto(lstPrincipalDetail);

        List<PrmBudgetAccessoryCDto> lstAccessoryDetail = getDetailComparedRecords(currDto.getPrmBudgetAccessoryCDto(), previousDto
                .getPrmBudgetAccessoryCDto(), BUDGET_ACCESSORY_FIELDS, PrmBudgetAccessoryCDto.class);
        currDto.setPrmBudgetAccessoryCDto(lstAccessoryDetail);

        List<PrmBudgetRunCDto> lstRunDetail = getDetailComparedRecords(currDto.getPrmBudgetRunCDto(), previousDto
                .getPrmBudgetRunCDto(), BUDGET_RUN_FIELDS, PrmBudgetRunCDto.class);
        currDto.setPrmBudgetRunCDto(lstRunDetail);

        return currDto;
    }

    private List getDetailComparedRecords(List lstCurrObj, List lstPreviousObj, String[] compareFields, Class clazz) {
        return getDetailComparedRecords(lstCurrObj, lstPreviousObj, compareFields, clazz, LAST_UUID, UUID);
    }

    private List getDetailComparedRecords(List lstCurrObj, List lstPreviousObj, String[] compareFields, Class clazz,
                                          String linkField1, String linkField2) {
        List lstReturn = new ArrayList();
        if (ListUtil.isNotEmpty(lstCurrObj)) {
            lstReturn.addAll(lstCurrObj);
        }
        Integer isRevised = Integer.valueOf(1);
        if (ListUtil.isNotEmpty(lstCurrObj)) {
            lstCurrObj.forEach(x -> {
                Object currLinkValue = BeanUtil.getProperty(x, linkField1);
                boolean founded = lstPreviousObj == null ? false : lstPreviousObj.stream().anyMatch(y -> {
                    Object previousLinkValue = BeanUtil.getProperty(y, linkField1);
                    if (StringUtil.isEmpty(previousLinkValue)) {
                        previousLinkValue = BeanUtil.getProperty(y, linkField2);
                    }
                    return ObjectUtil.beSame(currLinkValue, previousLinkValue);
                });
                if (!founded) {
                    Object newObj = BeanFactory.getObject(clazz);
                    BeanUtil.bean2Bean(x, newObj);
                    BeanUtil.setProperty(newObj, PrmProjectMainAttribute.CHANGE_STATUS, PrmProjectMainAttribute.CHANGE_STATUS_NEW);
                    BeanUtil.setProperty(newObj, PrmProjectMainAttribute.IS_REVISED, isRevised);
                    lstReturn.add(newObj);
                }
            });
            lstCurrObj.forEach(x -> {
                Object currLinkValue = BeanUtil.getProperty(x, linkField1);
                Object obj = lstPreviousObj == null ? null : lstPreviousObj.stream()
                        .filter(y -> {
                            Object previousLinkValue = BeanUtil.getProperty(y, linkField1);
                            if (StringUtil.isEmpty(previousLinkValue)) {
                                previousLinkValue = BeanUtil.getProperty(y, linkField2);
                            }
                            return ObjectUtil.beSame(currLinkValue, previousLinkValue);
                        })
                        .findFirst()
                        .orElse(null);
                if (obj != null) {
                    List<String> fields = getDifferentFields(x, obj, compareFields);
                    if (ListUtil.isNotEmpty(fields)) {
                        Object newObj = BeanFactory.getObject(clazz);
                        BeanUtil.bean2Bean(x, newObj);
                        BeanUtil.setProperty(newObj, PrmProjectMainAttribute.CHANGE_STATUS, PrmProjectMainAttribute.CHANGE_STATUS_REVISE);
                        BeanUtil.setProperty(newObj, PrmProjectMainAttribute.IS_REVISED, isRevised);
                        BeanUtil.setProperty(newObj, PrmProjectMainAttribute.CHANGED_FIELDS, "|" + StringUtil.joinList(fields,
                                "|") + "|");
                        Object oldObj = BeanFactory.getObject(clazz);
                        BeanUtil.bean2Bean(obj, oldObj);
                        BeanUtil.setProperty(oldObj, PrmProjectMainAttribute.CHANGE_STATUS, PrmProjectMainAttribute.CHANGE_STATUS_ORIGINAL);
                        BeanUtil.setProperty(oldObj, PrmProjectMainAttribute.IS_REVISED, isRevised);
                        lstReturn.add(oldObj);
                        lstReturn.add(newObj);
                    }
                }
            });
        }

        if (ListUtil.isNotEmpty(lstPreviousObj)) {
            lstPreviousObj.forEach(x -> {
                Object previousLinkValue = StringUtil.isNotEmpty(BeanUtil.getProperty(x, linkField1)) ? BeanUtil
                        .getProperty(x, linkField1) : BeanUtil.getProperty(x, linkField2);
                boolean founded = lstCurrObj == null ? false : lstCurrObj.stream().anyMatch(y -> {
                    Object currLinkValue = BeanUtil.getProperty(y, linkField1);
                    return ObjectUtil.beSame(currLinkValue, previousLinkValue);
                });
                if (!founded) {
                    Object oldObj = BeanFactory.getObject(clazz);
                    BeanUtil.bean2Bean(x, oldObj);
                    BeanUtil.setProperty(oldObj, PrmProjectMainAttribute.CHANGE_STATUS, PrmProjectMainAttribute.CHANGE_STATUS_DELETE);
                    BeanUtil.setProperty(oldObj, PrmProjectMainAttribute.IS_REVISED, isRevised);
                    lstReturn.add(oldObj);
                }
            });
        }
        int i = 0;
        for (Object obj : lstReturn) {
            BeanUtil.setProperty(obj, CommonAttribute.SEQ_NO, Integer.valueOf(++i));
        }

        return lstReturn;
    }

    private List<String> getDifferentFields(Object obj1, Object obj2, String[] compareFields) {
        List<String> lstFields = new ArrayList<>();
        for (String field : compareFields) {
            Object c1 = BeanUtil.getProperty(obj1, field);
            Object c2 = BeanUtil.getProperty(obj2, field);
            if (c1 instanceof BigDecimal && c2 == null) {
                c2 = BigDecimal.ZERO;
            } else if (c1 == null && c2 instanceof BigDecimal) {
                c1 = BigDecimal.ZERO;
            }
            if (!ObjectUtil.beSame(c1, c2)) {
                lstFields.add(field);
            }
        }
        return lstFields;
    }

    private String formatSerialNumber(String serialNumber) {
        if (StringUtil.isNotEmpty(serialNumber)) {
            List<String> lstNumber = StringUtil.splitAsList(serialNumber, "-");
            StringBuffer sb = new StringBuffer();
            lstNumber.forEach(x -> {
                if (x.length() < 10) {
                    for (int i = x.length(); i < 10; i++) {
                        sb.append("0");
                    }
                }
                sb.append(x + "-");
            });
            return sb.toString();
        } else {
            return "0";
        }
    }

    public String getPrmTaxType(String prmProjectMainId) {
        if (StringUtil.isEmpty(prmProjectMainId)) {
            return ContractTaxType.BUSINESS_TAX;
        }
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmProjectMain projectMain = pcm.findByPK(PrmProjectMain.class, prmProjectMainId);
        if (projectMain == null) {
            return ContractTaxType.BUSINESS_TAX;
        }
        String contractId = projectMain.getPrmContractId();
        Date compareDate = DateUtil.parseDate("2016-05-01", "yyyy-MM-dd");
        if (StringUtil.isEmpty(contractId)) {
            Date establishDate = projectMain.getEstablishDate();
            if (establishDate == null || establishDate.compareTo(compareDate) >= 0) {
                return ContractTaxType.VAT_TAX;
            } else {
                return ContractTaxType.BUSINESS_TAX;
            }
        } else {
            PrmContract contract = pcm.findByPK(PrmContract.class, contractId);
            if (contract == null || Integer.valueOf(1).equals(contract.getIsBusinessTax())) {
                return ContractTaxType.BUSINESS_TAX;
            } else {
                return ContractTaxType.VAT_TAX;
            }
        }
    }

    @Override
    public List<String> getRelatedContractUuidsFromMain(String MainUuid) {
        Map<String, Object> con = new HashMap<String, Object>();
        con.put(PrmCollectionMeasureAttribute.PRM_PROJECT_MAIN_ID, MainUuid);
        return PersistenceFactory.getInstance().findByAnyFields(PrmContractDetail.class, con, null)
                .stream()
                .map(PrmContractDetail::getPrmContractId)
                .collect(Collectors.toList());
    }
}