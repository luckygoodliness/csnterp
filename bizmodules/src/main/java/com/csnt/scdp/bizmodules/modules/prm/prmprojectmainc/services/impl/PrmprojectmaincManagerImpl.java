package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.impl;

import com.csnt.scdp.bizmodules.attributes.BillStateAttribute;
import com.csnt.scdp.bizmodules.attributes.CommonAttribute;
import com.csnt.scdp.bizmodules.attributes.ContractTaxType;
import com.csnt.scdp.bizmodules.attributes.ExpandFieldName;
import com.csnt.scdp.bizmodules.attributes.ExpandFieldType;
import com.csnt.scdp.bizmodules.attributes.PrmBudgetCodes;
import com.csnt.scdp.bizmodules.attributes.PrmCodeTypes;
import com.csnt.scdp.bizmodules.attributes.PrmCodes;
import com.csnt.scdp.bizmodules.attributes.TaxType;
import com.csnt.scdp.bizmodules.entity.nonprm.FinancialSubject;
import com.csnt.scdp.bizmodules.entity.prm.PrmAssociatedUnits;
import com.csnt.scdp.bizmodules.entity.prm.PrmAssociatedUnitsC;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetAccessory;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetAccessoryC;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetDetail;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetDetailC;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetOutsource;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetOutsourceC;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetPrincipal;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetPrincipalC;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetRun;
import com.csnt.scdp.bizmodules.entity.prm.PrmBudgetRunC;
import com.csnt.scdp.bizmodules.entity.prm.PrmContract;
import com.csnt.scdp.bizmodules.entity.prm.PrmContractC;
import com.csnt.scdp.bizmodules.entity.prm.PrmContractDetail;
import com.csnt.scdp.bizmodules.entity.prm.PrmContractDetailC;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entity.prm.PrmMemberDetailP;
import com.csnt.scdp.bizmodules.entity.prm.PrmMemberDetailPC;
import com.csnt.scdp.bizmodules.entity.prm.PrmPayDetailP;
import com.csnt.scdp.bizmodules.entity.prm.PrmPayDetailPC;
import com.csnt.scdp.bizmodules.entity.prm.PrmProgressDetailP;
import com.csnt.scdp.bizmodules.entity.prm.PrmProgressDetailPC;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMainC;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePackage;
import com.csnt.scdp.bizmodules.entity.prm.PrmPurchasePlanDetail;
import com.csnt.scdp.bizmodules.entity.prm.PrmQsP;
import com.csnt.scdp.bizmodules.entity.prm.PrmQsPC;
import com.csnt.scdp.bizmodules.entity.prm.PrmReceiptsDetailP;
import com.csnt.scdp.bizmodules.entity.prm.PrmReceiptsDetailPC;
import com.csnt.scdp.bizmodules.entity.prm.PrmSquareDetailP;
import com.csnt.scdp.bizmodules.entity.prm.PrmSquareDetailPC;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.FinancialSubjectAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmAssociatedUnitsAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmAssociatedUnitsCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetDetailAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetDetailCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetOutsourceCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetPrincipalAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchasePackageAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmPurchasePlanDetailAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpExpandFieldHelper;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpUserHelper;
import com.csnt.scdp.bizmodules.modules.nonprm.financialsubject.services.intf.FinancialsubjectManager;
import com.csnt.scdp.bizmodules.modules.prm.contractc.services.intf.ContractCManager;
import com.csnt.scdp.bizmodules.modules.prm.prmexamperiod.services.intf.PrmExamPeriodManager;
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
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf.PrmprojectmaincManager;
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
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.CacheHelper;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.helper.MessageHelper;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ArrayUtil;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.JsonUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.MathUtil;
import com.csnt.scdp.framework.util.ObjectUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import com.csnt.scdp.sysmodules.helper.OnlineUserHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:  PrmprojectmaincManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-27 15:28:19
 */

@Scope("singleton")
@Service("prmprojectmainc-manager")
public class PrmprojectmaincManagerImpl implements PrmprojectmaincManager {
    private static ILogTracer tracer = LogTracerFactory.getInstance(PrmprojectmaincManagerImpl.class);

    @Resource(name = "financialsubject-manager")
    private FinancialsubjectManager financialsubjectManager;

    @Resource(name = "contract-c-manager")
    private ContractCManager contractCManager;

    @Resource(name = "purchaseplan-manager")
    private PurchaseplanManager purchaseplanManager;

    public String syncMainChangeToMain(String mainChangeUuid) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmProjectMainC mainChangeEntity = pcm.findByPK(PrmProjectMainC.class, mainChangeUuid);
        if (mainChangeEntity == null) {
            return null;
        }
        String mainUuid = mainChangeEntity.getPrmProjectMainId();
        boolean isInsert = false;
        if (StringUtil.isEmpty(mainUuid)) {
            //do insert
            isInsert = true;
            PrmProjectMain mainEntity = new PrmProjectMain();
            BeanUtil.bean2Bean(mainChangeEntity, mainEntity);
            mainEntity.setUuid(null);
            mainEntity.setState(null);
            mainEntity.setPurchasePlanState(BillStateAttribute.CMD_BILL_STATE_NEW);
            pcm.insert(mainEntity);
            mainEntity = pcm.findByPK(PrmProjectMain.class, mainEntity.getUuid());
            mainEntity.setCreateBy(mainChangeEntity.getCreateBy());
            mainEntity.setCreateTime(mainChangeEntity.getCreateTime());
            pcm.update(mainEntity);
            //write the main uuid to main change header
            mainUuid = mainEntity.getUuid();
            mainChangeEntity.setPrmProjectMainId(mainUuid);
            pcm.update(mainChangeEntity);
        } else {
            //sync header
            PrmProjectMain mainEntity = pcm.findByPK(PrmProjectMain.class, mainUuid);
            String oldState = mainEntity.getState();
            BeanUtil.bean2Bean(mainChangeEntity, mainEntity);
            mainEntity.setUuid(mainUuid);
            mainEntity.setState(oldState);
            pcm.update(mainEntity);

            pcm.update(mainChangeEntity);
        }

        syncProjectDetail(mainChangeUuid, mainUuid, PrmContractDetailC.class, PrmContractDetail.class, isInsert,
                PrmProjectMainAttribute.PRM_CONTRACT_ID, PrmProjectMainAttribute.PRM_CONTRACT_ID);

        syncMemberDetail(mainChangeUuid, mainUuid, isInsert);
        syncProjectDetail(mainChangeUuid, mainUuid, PrmPayDetailPC.class, PrmPayDetailP.class, isInsert);
        syncProjectDetail(mainChangeUuid, mainUuid, PrmProgressDetailPC.class, PrmProgressDetailP.class, isInsert);
        syncProjectDetail(mainChangeUuid, mainUuid, PrmSquareDetailPC.class, PrmSquareDetailP.class, isInsert);
        syncProjectDetail(mainChangeUuid, mainUuid, PrmReceiptsDetailPC.class, PrmReceiptsDetailP.class, isInsert);
        syncProjectDetail(mainChangeUuid, mainUuid, PrmQsPC.class, PrmQsP.class, isInsert);
        syncProjectDetail(mainChangeUuid, mainUuid, PrmAssociatedUnitsC.class, PrmAssociatedUnits.class, isInsert);
        syncProjectDetail(mainChangeUuid, mainUuid, PrmBudgetDetailC.class, PrmBudgetDetail.class, isInsert);
        syncProjectDetail(mainChangeUuid, mainUuid, PrmBudgetPrincipalC.class, PrmBudgetPrincipal.class, isInsert);
        syncProjectDetail(mainChangeUuid, mainUuid, PrmBudgetAccessoryC.class, PrmBudgetAccessory.class, isInsert);
        syncProjectDetail(mainChangeUuid, mainUuid, PrmBudgetOutsourceC.class, PrmBudgetOutsource.class, isInsert);
        syncProjectDetail(mainChangeUuid, mainUuid, PrmBudgetRunC.class, PrmBudgetRun.class, isInsert);

        return mainUuid;
    }

    private void syncMemberDetail(String mainChangeUuid, String mainUuid, boolean isInsert) {
        //remove the cache first
        removeProjectFilterCache(mainChangeUuid, mainUuid);
        //sync the member second
        syncProjectDetail(mainChangeUuid, mainUuid, PrmMemberDetailPC.class, PrmMemberDetailP.class, isInsert);
    }

    public void syncProjectDetail(String mainChangeUuid, String mainUuid, Class revisedClazz, Class oldClazz,
                                  boolean isInsert) {
        syncProjectDetail(mainChangeUuid, mainUuid, revisedClazz, oldClazz, isInsert, ProjectmainManager.LAST_UUID, ProjectmainManager.UUID);
    }

    public void syncProjectDetail(String mainChangeUuid, String mainUuid, Class revisedClazz, Class oldClazz,
                                  boolean isInsert, String linkNewField, String linkOldField) {

        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map mapCondition = new HashMap();
        List<BasePojo> lstExisted = new ArrayList<BasePojo>();
        if (!isInsert) {
            mapCondition.put(PrmAssociatedUnitsAttribute.PRM_PROJECT_MAIN_ID, mainUuid);
            lstExisted = pcm.findByAnyFields(oldClazz, mapCondition, null);
            if (ListUtil.isEmpty(lstExisted)) {
                lstExisted = new ArrayList<>();
            }
        }
        mapCondition.clear();
        mapCondition.put(PrmAssociatedUnitsCAttribute.PRM_PROJECT_MAIN_C_ID, mainChangeUuid);
        List<BasePojo> lstChangeDetail = pcm.findByAnyFields(revisedClazz, mapCondition, null);
        if (ListUtil.isEmpty(lstChangeDetail)) {
            lstChangeDetail = new ArrayList();
        }
        List<BasePojo> lstUpdate = new ArrayList<BasePojo>();
        List<BasePojo> lstDelete = new ArrayList<BasePojo>();
        List<BasePojo> lstInsert = new ArrayList<BasePojo>();
        for (BasePojo detail : lstExisted) {
            boolean found = false;
            for (BasePojo detailC : lstChangeDetail) {
                if (ObjectUtil.beSame(BeanUtil.getProperty(detail, linkOldField), BeanUtil.getProperty(detailC, linkNewField))) {
                    found = true;
                    break;
                }
            }
            if (found) {
                lstUpdate.add(detail);
            } else {
                lstDelete.add(detail);
            }
        }
        for (BasePojo detailC : lstChangeDetail) {
            boolean found = false;
            for (BasePojo detail : lstUpdate) {
                if (ObjectUtil.beSame(BeanUtil.getProperty(detail, linkOldField), BeanUtil.getProperty(detailC, linkNewField))) {
                    found = true;
                    String oldUuid = (String) BeanUtil.getProperty(detail, CommonAttribute.UUID);
                    BeanUtil.bean2Bean(detailC, detail);
                    BeanUtil.setProperty(detail, CommonAttribute.UUID, oldUuid);
                    BeanUtil.setProperty(detail, PrmProjectMainCAttribute.PRM_PROJECT_MAIN_ID, mainUuid);
                    break;
                }
            }
            if (!found) {
                BasePojo newDetail = com.csnt.scdp.framework.core.spring.BeanFactory.getObject(oldClazz);
                BeanUtil.bean2Bean(detailC, newDetail);
                BeanUtil.setProperty(newDetail, PrmProjectMainCAttribute.PRM_PROJECT_MAIN_ID, mainUuid);
                //don't need reset the uuid, system will copy the new item's uuid to main project detail.
                if (revisedClazz == PrmBudgetOutsourceC.class) {
                    String spliUuid = ((PrmBudgetOutsource) newDetail).getSplitFromUuid();
                    if (StringUtil.isNotEmpty(spliUuid)) {
                        Optional<BasePojo> base = lstChangeDetail.stream().filter(t -> spliUuid.equals(((PrmBudgetOutsourceC) t).getUuid())).findFirst();
                        if (base.isPresent()) {
                            String splitUuid = ((PrmBudgetOutsourceC) base.get()).getLastUuid();
                            ((PrmBudgetOutsource) newDetail).setSplitFromUuid(splitUuid);
                        }
                    }
                }

                if (revisedClazz == PrmBudgetPrincipalC.class) {
                    String spliUuid = ((PrmBudgetPrincipal) newDetail).getSplitFromUuid();
                    if (StringUtil.isNotEmpty(spliUuid)) {
                        Optional<BasePojo> base = lstChangeDetail.stream().filter(t -> spliUuid.equals(((PrmBudgetPrincipalC) t).getUuid())).findFirst();
                        if (base.isPresent()) {
                            String splitUuid = ((PrmBudgetPrincipalC) base.get()).getLastUuid();
                            ((PrmBudgetPrincipal) newDetail).setSplitFromUuid(splitUuid);
                        }
                    }
                }

                if (revisedClazz == PrmBudgetAccessoryC.class) {
                    String spliUuid = ((PrmBudgetAccessory) newDetail).getSplitFromUuid();
                    if (StringUtil.isNotEmpty(spliUuid)) {
                        Optional<BasePojo> base = lstChangeDetail.stream().filter(t -> spliUuid.equals(((PrmBudgetAccessoryC) t).getUuid())).findFirst();
                        if (base.isPresent()) {
                            String splitUuid = ((PrmBudgetAccessoryC) base.get()).getLastUuid();
                            ((PrmBudgetAccessory) newDetail).setSplitFromUuid(splitUuid);
                        }
                    }
                }

                lstInsert.add(newDetail);
            }
        }
        if (ListUtil.isNotEmpty(lstDelete)) {
            pcm.batchDelete(lstDelete);
        }
        if (ListUtil.isNotEmpty(lstUpdate)) {
            pcm.batchUpdate(lstUpdate);
        }
        if (ListUtil.isNotEmpty(lstInsert)) {
            pcm.batchInsert(lstInsert);
        }
    }

    @Override
    public String generateProjectCode(String uuid, String stampType, String prmCode, boolean... preview) throws
            BizException {
        StringBuffer sb = new StringBuffer();

        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmProjectMainC header = pcm.findByPK(PrmProjectMainC.class, uuid);

        String deptProjectCode = null;
        if (PrmCodes.FIXED_ASSERT.equals(prmCode)) {
            deptProjectCode = PrmCodes.FIXED_ASSERT_CN;
        } else if (PrmCodes.RAISE_VOTE.equals(prmCode)) {
            deptProjectCode = PrmCodes.RAISE_VOTE_CN;
        } else if (PrmCodes.ZU_LING.equals(prmCode)) {
            deptProjectCode = PrmCodes.ZU_LING_CN;
        } else if (PrmCodes.YAN_FA.equals(prmCode)) {
            deptProjectCode = PrmCodes.YAN_FA_CN;
        } else {
            String officeCode = header.getContractorOffice();
            String officeUUid = OrgHelper.getOrgIdByCode(officeCode);
            deptProjectCode = ErpExpandFieldHelper.getExpandFieldValue(ExpandFieldType.ORG, officeUUid,
                    ExpandFieldName.PROJECT_CODE);
        }
        if (StringUtil.isEmpty(deptProjectCode)) {
            throw new BizException(MessageHelper.getMessage("MSG_PRM_GEN_PROJECT_MAIN_C_DEPT_PROJECT_CODE_EMPTY"));
        } else {
            sb.append(deptProjectCode);
        }

        Date establishDate = header.getEstablishDate();
        if (establishDate == null) {
            throw new BizException(MessageHelper.getMessage("MSG_PRM_GEN_PROJECT_MAIN_C_ESTABLISH_DATE_EMPTY"));
        } else {
            int year = establishDate.getYear() % 10;
            int month = establishDate.getMonth() + 1;
            sb.append(year);
            sb.append(getMonthCode(month));
        }

        Date scheduledEndDate = header.getScheduledEndDate();
        int year = scheduledEndDate.getYear() % 10;
        sb.append(year);
        sb.append(getMonthCode(scheduledEndDate.getMonth() + 1));

        sb.append(getTaxSymbol(uuid, header.getTaxType(), prmCode));

        Map mapInput = new HashMap();
        mapInput.put("SYSDATE", new Date());
        mapInput.put("STAMPTYPE", stampType);
        mapInput.put("SIJITYPE", prmCode);
        String newCode = NumberingHelper.getNumbering("PROJECT_CODE", mapInput, preview);
        sb.append(newCode);

        String projectCode = sb.toString();


        return projectCode;
    }

    private String getTaxSymbol(String uuid, String taxType, String prmCodeType) {
        String contractTaxType = getPrmChangeTaxType(uuid);
        if (ContractTaxType.BUSINESS_TAX.equals(contractTaxType)) {
            if (TaxType.PP.equals(taxType)) {
                return "+";
            } else if (TaxType.ZP.equals(taxType)) {
                return "-";
            } else if (TaxType.MS.equals(taxType)) {
                return "/";
            } else if (TaxType.YFNW.equals(taxType)) {
                return "/";
            } else {
                throw new BizException(MessageHelper.getMessage("MSG_PRM_GEN_PROJECT_MAIN_C_TAX_TYPE_ERROR"));
            }
        } else {
            if (ArrayUtil.contains(PrmCodes.HYPHEN_GROUP, prmCodeType)) {
                return PrmCodes.HYPHEN;
            } else if (ArrayUtil.contains(PrmCodes.SLASH_GROUP, prmCodeType)) {
                return PrmCodes.SLASH;
            } else {
                throw new BizException(MessageHelper.getMessage("MSG_PRM_GEN_PROJECT_MAIN_C_TAX_TYPE_ERROR"));
            }
        }
    }

    public void updateProjectCode(String mainChangeUuid, String mainUuid, String projectCode) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        List lstParam = new ArrayList<>();
        lstParam.add(projectCode);
        lstParam.add(mainChangeUuid);
        lstParam.add(mainUuid);
        DAOMeta daoMeta = DAOHelper.getDAO("prmprojectmainc-dao", "update-project-main-change-code", lstParam);
        pcm.updateByNativeSQL(daoMeta);
        if (StringUtil.isNotEmpty(mainUuid)) {
            lstParam.clear();
            lstParam.add(projectCode);
            lstParam.add(mainUuid);
            DAOMeta daoMeta2 = DAOHelper.getDAO("prmprojectmainc-dao", "update-project-main-code", lstParam);
            pcm.updateByNativeSQL(daoMeta2);
        }
    }

    public void invokeApproveAction(String uuid) {

        PrmProjectMainC mainCEntity = PersistenceFactory.getInstance().findByPK(PrmProjectMainC.class, uuid);
        String prmProjectMainId = mainCEntity.getPrmProjectMainId();
        String detailType = mainCEntity.getDetailType();
        if (PrmProjectMainAttribute.PRM_MEMBER_DETAIL.equals(detailType)) {
            syncMemberDetail(uuid, prmProjectMainId, false);
        } else if (PrmProjectMainAttribute.PRM_PROGRESS_DETAIL.equals(detailType)) {
            syncProjectDetail(uuid, prmProjectMainId, PrmProgressDetailPC.class, PrmProgressDetailP.class, false);
        } else if (PrmProjectMainAttribute.PRM_PAY_DETAIL.equals(detailType)) {
            syncProjectDetail(uuid, prmProjectMainId, PrmPayDetailPC.class, PrmPayDetailP.class, false);
        } else if (PrmProjectMainAttribute.PRM_SQUARE_DETAIL.equals(detailType)) {
            syncProjectDetail(uuid, prmProjectMainId, PrmSquareDetailPC.class, PrmSquareDetailP.class, false);
        } else if (PrmProjectMainAttribute.PRM_RECEIPTS_DETAIL.equals(detailType)) {
            syncProjectDetail(uuid, prmProjectMainId, PrmReceiptsDetailPC.class, PrmReceiptsDetailP.class, false);
        } else {
//            syncPurchasePackageBudgetMoney(uuid, prmProjectMainId);
            String mainUuid = syncMainChangeToMain(uuid);

            //sync the purchase plan detail
            syncMainToPurchasePlan(mainUuid, mainCEntity);

            //同步报的预算金额移至最后
            syncPurchasePackageBudgetMoney(prmProjectMainId);

            //sync info to contract
            syncInfoToContract(uuid, mainUuid);
        }
    }

    /**
     * 同步包预算金额
     *
     * @param prmProjectMainId
     */
    private void syncPurchasePackageBudgetMoney(String prmProjectMainId) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        String sqlOutSource = "update prm_purchase_plan_detail t1 set (name,amount,budget_price,purchase_budget_money," +
                "unit,remark,is_stamp)=(select supplier_code,amount,price,total_value,unit,remark,is_stamp from prm_budget_outsource t2" +
                " where t2.uuid=t1.prm_budget_ref_id) " +
                "where t1.prm_budget_type='OUTSOURCE' and t1.prm_project_main_id=? ";
        String sqlPrincipal = "update prm_purchase_plan_detail t1 set (name,amount,budget_price,purchase_budget_money," +
                "unit,remark,factory,model,is_stamp)=(select equipment_name,amount,scheming_price,scheming_total_value,unit," +
                "remark,factory,equipment_model,is_stamp from prm_budget_principal t2 " +
                "where t2.uuid=t1.prm_budget_ref_id) " +
                "where t1.prm_budget_type='PRINCIPAL' and t1.prm_project_main_id=? ";
        String sqlAccessory = "update prm_purchase_plan_detail t1 set (name,amount,budget_price,purchase_budget_money," +
                "model,remark,is_stamp)=(select accessory_name,amount,price,total_value,accessory_model,remark,is_stamp " +
                "from prm_budget_accessory t2 where t2.uuid=t1.prm_budget_ref_id) " +
                "where t1.prm_budget_type='ACCESSORY' and t1.prm_project_main_id=? ";
        String sqlRun = "update prm_purchase_plan_detail t1\n" +
                "   set ( unit, amount, budget_price, purchase_budget_money, remark) =\n" +
                "       (select unit,\n" +
                "               amount,\n" +
                "               price,\n" +
                "               total_value,\n" +
                "               remark\n" +
                "          from prm_budget_RUN t2\n" +
                "         where t2.uuid = t1.prm_budget_ref_id)\n" +
                " where t1.prm_budget_type = 'RUN'\n" +
                "   and t1.prm_project_main_id = ?";


        String sqlPackage1 = "update prm_purchase_package t1 set t1.pending_money=(" +
                "select sum(t2.purchase_budget_money) from prm_purchase_plan_detail t2 " +
                "where t1.uuid=t2.purchase_package_id) " +
                "where t1.prm_project_main_id=? ";

        String sqlPackage2 = "update prm_purchase_package t1 set t1.package_budget_money=t1.pending_money " +
                "where t1.prm_project_main_id=? and t1.package_state in ('2','4')";
        List lstParam = new ArrayList<>();
        lstParam.add(prmProjectMainId);
        pcm.updateByNativeSQL(new DAOMeta(sqlOutSource, lstParam));
        pcm.updateByNativeSQL(new DAOMeta(sqlPrincipal, lstParam));
        pcm.updateByNativeSQL(new DAOMeta(sqlAccessory, lstParam));
        pcm.updateByNativeSQL(new DAOMeta(sqlRun, lstParam));
        pcm.updateByNativeSQL(new DAOMeta(sqlPackage1, lstParam));
        pcm.updateByNativeSQL(new DAOMeta(sqlPackage2, lstParam));
    }

    private void syncInfoToContract(String uuid, String mainUuid) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmProjectMain projectMain = pcm.findByPK(PrmProjectMain.class, mainUuid);
        PrmProjectMainC prmProjectMainC = pcm.findByPK(PrmProjectMainC.class, uuid);
        //sync contract amount to contract
        Map condition = new HashMap();
        condition.put(PrmProjectMainCAttribute.PRM_PROJECT_MAIN_ID, projectMain.getUuid());
        List<PrmContractDetail> lstContractDetail = pcm.findByAnyFields(PrmContractDetail.class, condition, null);
        if (ListUtil.isNotEmpty(lstContractDetail)) {
            lstContractDetail.forEach(x -> {
                PrmContract contract = pcm.findByPK(PrmContract.class, x.getPrmContractId());
                if (contract != null) {
                    if (!ObjectUtil.beSame(contract.getTaxType(), projectMain.getTaxType())
                            || !ObjectUtil.beSame(contract.getPrmCodeType(), projectMain.getPrmCodeType())) {
                        contract.setTaxType(projectMain.getTaxType());
                        contract.setPrmCodeType(projectMain.getPrmCodeType());
                        pcm.update(contract);
                    }
                    if (contract.getConfirmedDate() == null) {
                        contract.setConfirmedDate(new Date());
                        pcm.update(contract);
                    }
                    if (prmProjectMainC.getProjectName() != null && !prmProjectMainC.getProjectName().equals(contract.getProjectName())) {
                        contract.setProjectName(prmProjectMainC.getProjectName());
                        pcm.update(contract);
                    }
                    if (contract.getExamDate() == null) {
                        PrmExamPeriodManager prmExamPeriodManager = BeanFactory.getBean("prmexamperiod-manager".toLowerCase());
                        if (PrmProjectMainAttribute.PRM_DETAIL_NEW.equals(prmProjectMainC.getDetailType())) {
                            contract.setExamDate(prmExamPeriodManager.getExamDateFromAppointedDate(prmProjectMainC.getEstablishDate()));
                        } else {
                            contract.setExamDate(prmExamPeriodManager.getExamDateFromAppointedDate(new Date()));
                        }

                        pcm.update(contract);
                    }
                }
            });
        }

    }


    @Override
    public void updateApproveFields(String uuid) throws BizException {
        StringBuffer sb = new StringBuffer();
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmProjectMainC header = pcm.findByPK(PrmProjectMainC.class, uuid);

        if (StringUtil.isEmpty(header.getProjectSerialNo())) {
            String officeCode = header.getContractorOffice();
            String officeUUid = OrgHelper.getOrgIdByCode(officeCode);
            String deptProjectCode = ErpExpandFieldHelper.getExpandFieldValue(ExpandFieldType.ORG, officeUUid,
                    ExpandFieldName.PROJECT_CODE);
//            sb.append(DateUtil.formatDate(OnlineUserHelper.getUserCurrentDate(), "yyyy"));
            if (StringUtil.isEmpty(deptProjectCode)) {
                throw new BizException(MessageHelper.getMessage("MSG_PRM_GEN_PROJECT_MAIN_C_DEPT_PROJECT_CODE_EMPTY_2"));
            } else {
//                sb.append(deptProjectCode);
            }
            Map params = new HashMap();
            params.put("DEPT_SHORT_CODE", deptProjectCode);
            String autoNo = NumberingHelper.getNumbering("PROJECT_SERIAL_NO", params);
            sb.append(autoNo);
        }
        header.setProjectSerialNo(sb.toString());
        if (header.getEstablishDate() == null) {
            header.setEstablishDate(DateUtil.parseDate(DateUtil.formatDate(OnlineUserHelper.getUserCurrentDate(),
                    "yyyy-MM-dd"), "yyyy-MM-dd"));
        }
        pcm.update(header);

        //update contract confirmed date if empty.
        this.updateContractConfirmedDate(uuid);
        this.updateContractProjectName(uuid);
    }

    public void updateContractConfirmedDate(String prmProjectMainCId) {
        if (StringUtil.isEmpty(prmProjectMainCId)) {
            return;
        }
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmProjectMainC projectMainC = pcm.findByPK(PrmProjectMainC.class, prmProjectMainCId);
        if (projectMainC == null) {
            return;
        }
        String updateContractSql = "update prm_contract t1 set t1.confirmed_date=? " +
                "where t1.confirmed_date is null " +
                " and t1.uuid in (select prm_contract_id from prm_contract_detail_c where prm_project_main_c_id=?)";
        List lstParam = new ArrayList<>();
        lstParam.add(projectMainC.getEstablishDate());
        lstParam.add(prmProjectMainCId);
        DAOMeta daoMeta = new DAOMeta(updateContractSql, lstParam);
        pcm.updateByNativeSQL(daoMeta);
    }

    public void updateContractProjectName(String prmProjectMainCId) {
        if (StringUtil.isEmpty(prmProjectMainCId)) {
            return;
        }
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmProjectMainC projectMainC = pcm.findByPK(PrmProjectMainC.class, prmProjectMainCId);
        if (projectMainC == null) {
            return;
        }
        String updateContractSql = "update prm_contract t1 set t1.project_name=? " +
                "where t1.confirmed_date is null " +
                " and t1.uuid in (select prm_contract_id from prm_contract_detail_c where prm_project_main_c_id=?)";
        List lstParam = new ArrayList<>();
        lstParam.add(projectMainC.getProjectName());
        lstParam.add(prmProjectMainCId);
        DAOMeta daoMeta = new DAOMeta(updateContractSql, lstParam);
        pcm.updateByNativeSQL(daoMeta);
    }

    private String getMonthCode(int month) {
        if (month == 10) {
            return "A";
        } else if (month == 11) {
            return "B";
        } else if (month == 12) {
            return "C";
        } else {
            return String.valueOf(month);
        }
    }

    public PrmProjectMainCDto wrapperProjectMainToChange(String uuid, String detailType) {
        PrmProjectMainDto inputDto = (PrmProjectMainDto) DtoHelper.findDtoByPK(PrmProjectMainDto.class, uuid);
        if (inputDto == null) {
            return null;
        }

        PrmProjectMainCDto outputDto = new PrmProjectMainCDto();
        BeanUtil.bean2Bean(inputDto, outputDto);
        outputDto.setPrmProjectMainId(inputDto.getUuid());
        outputDto.setDetailType(detailType);
        outputDto.setState(BillStateAttribute.CMD_BILL_STATE_NEW);

        //取上一次变更到到现在为止发生的合同变更的变更原因

        Map<String, List<PrmContractC>> rtnContractRs = contractCManager.getContractCbyMainDto(inputDto);

        if (MapUtil.isNotEmpty(rtnContractRs)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            StringBuilder remark = new StringBuilder("");
            for (Map.Entry<String, List<PrmContractC>> entry : rtnContractRs.entrySet()) {
                entry.getValue().forEach(t -> {
                    remark.append(sdf.format(t.getAuditTime()) + ",");
                    remark.append(t.getContractName() + " 发生变更：");
                    remark.append(t.getRemark() + "\n");
                });
            }
            outputDto.setReviseReason(remark.toString());
        }

        if (PrmProjectMainAttribute.PRM_DETAIL_ALL.equals(detailType)
                || PrmProjectMainAttribute.PRM_MEMBER_DETAIL.equals(detailType)) {
            List<PrmMemberDetailPDto> lstMemberDetail = inputDto.getPrmMemberDetailPDto();
            if (ListUtil.isNotEmpty(lstMemberDetail)) {
                List<PrmMemberDetailPCDto> lstMemberDetailChange = new ArrayList<PrmMemberDetailPCDto>();
                for (PrmMemberDetailPDto memberDetail : lstMemberDetail) {
                    PrmMemberDetailPCDto memberDetailChange = new PrmMemberDetailPCDto();
                    BeanUtil.bean2Bean(memberDetail, memberDetailChange);
                    memberDetailChange.setLastUuid(memberDetail.getUuid());
                    lstMemberDetailChange.add(memberDetailChange);
                }
                outputDto.setPrmMemberDetailPCDto(lstMemberDetailChange);
            }
        }

        if (PrmProjectMainAttribute.PRM_DETAIL_ALL.equals(detailType)
                || PrmProjectMainAttribute.PRM_PAY_DETAIL.equals(detailType)) {
            List<PrmPayDetailPDto> lstPayDetail = inputDto.getPrmPayDetailPDto();
            if (ListUtil.isNotEmpty(lstPayDetail)) {
                List<PrmPayDetailPCDto> lstPayDetailChange = new ArrayList<PrmPayDetailPCDto>();
                for (PrmPayDetailPDto payDetail : lstPayDetail) {
                    PrmPayDetailPCDto payDetailChange = new PrmPayDetailPCDto();
                    BeanUtil.bean2Bean(payDetail, payDetailChange);
                    payDetailChange.setLastUuid(payDetail.getUuid());
                    lstPayDetailChange.add(payDetailChange);
                }
                outputDto.setPrmPayDetailPCDto(lstPayDetailChange);
            }
        }

        if (PrmProjectMainAttribute.PRM_DETAIL_ALL.equals(detailType)
                || PrmProjectMainAttribute.PRM_PROGRESS_DETAIL.equals(detailType)) {
            List<PrmProgressDetailPDto> lstProgressDetail = inputDto.getPrmProgressDetailPDto();
            if (ListUtil.isNotEmpty(lstProgressDetail)) {
                List<PrmProgressDetailPCDto> lstProgressDetailChange = new ArrayList<PrmProgressDetailPCDto>();
                for (PrmProgressDetailPDto progressDetail : lstProgressDetail) {
                    PrmProgressDetailPCDto progressDetailChange = new PrmProgressDetailPCDto();
                    BeanUtil.bean2Bean(progressDetail, progressDetailChange);
                    progressDetailChange.setLastUuid(progressDetail.getUuid());
                    lstProgressDetailChange.add(progressDetailChange);
                }
                outputDto.setPrmProgressDetailPCDto(lstProgressDetailChange);
            }
        }

        if (PrmProjectMainAttribute.PRM_DETAIL_ALL.equals(detailType)
                || PrmProjectMainAttribute.PRM_SQUARE_DETAIL.equals(detailType)) {
            List<PrmSquareDetailPDto> lstSpuareDetail = inputDto.getPrmSquareDetailPDto();
            if (ListUtil.isNotEmpty(lstSpuareDetail)) {
                List<PrmSquareDetailPCDto> lstSquareDetailChange = new ArrayList<PrmSquareDetailPCDto>();
                for (PrmSquareDetailPDto squareDetail : lstSpuareDetail) {
                    PrmSquareDetailPCDto squareDetailChange = new PrmSquareDetailPCDto();
                    BeanUtil.bean2Bean(squareDetail, squareDetailChange);
                    squareDetailChange.setLastUuid(squareDetail.getUuid());
                    lstSquareDetailChange.add(squareDetailChange);
                }
                outputDto.setPrmSquareDetailPCDto(lstSquareDetailChange);
            }
        }

        if (PrmProjectMainAttribute.PRM_DETAIL_ALL.equals(detailType)
                || PrmProjectMainAttribute.PRM_RECEIPTS_DETAIL.equals(detailType)) {
            List<PrmReceiptsDetailPDto> lstReceiptsDetail = inputDto.getPrmReceiptsDetailPDto();
            if (ListUtil.isNotEmpty(lstReceiptsDetail)) {
                List<PrmReceiptsDetailPCDto> lstReceiptsDetailChange = new ArrayList<PrmReceiptsDetailPCDto>();
                for (PrmReceiptsDetailPDto receiptsDetail : lstReceiptsDetail) {
                    PrmReceiptsDetailPCDto receiptsDetailChange = new PrmReceiptsDetailPCDto();
                    BeanUtil.bean2Bean(receiptsDetail, receiptsDetailChange);
                    receiptsDetailChange.setLastUuid(receiptsDetail.getUuid());
                    lstReceiptsDetailChange.add(receiptsDetailChange);
                }
                outputDto.setPrmReceiptsDetailPCDto(lstReceiptsDetailChange);
            }
        }

        if (PrmProjectMainAttribute.PRM_DETAIL_ALL.equals(detailType)) {

            List<PrmContractDetailDto> lstContractDetail = inputDto.getPrmContractDetailDto();
            if (ListUtil.isNotEmpty(lstContractDetail)) {
                List<PrmContractDetailCDto> lstContractDetailC = new ArrayList<PrmContractDetailCDto>();
                for (PrmContractDetail contractDetail : lstContractDetail) {
                    PrmContractDetailCDto contractDetailC = new PrmContractDetailCDto();
                    BeanUtil.bean2Bean(contractDetail, contractDetailC);
                    PrmContract contract = PersistenceFactory.getInstance().findByPK(PrmContract.class, contractDetail
                            .getPrmContractId());
                    if (contract != null) {
                        contractDetailC.setCustomerId(contract.getCustomerId());
                        contractDetailC.setContractNowMoney(contract.getContractNowMoney());
                    }
                    lstContractDetailC.add(contractDetailC);
                }
                outputDto.setPrmContractDetailCDto(lstContractDetailC);
            }

            List<PrmQsPDto> lstQsP = inputDto.getPrmQsPDto();
            if (ListUtil.isNotEmpty(lstQsP)) {
                List<PrmQsPCDto> lstQsPChange = new ArrayList<PrmQsPCDto>();
                for (PrmQsPDto qsP : lstQsP) {
                    PrmQsPCDto qsPChange = new PrmQsPCDto();
                    BeanUtil.bean2Bean(qsP, qsPChange);
                    qsPChange.setLastUuid(qsP.getUuid());
                    lstQsPChange.add(qsPChange);
                }
                outputDto.setPrmQsPCDto(lstQsPChange);
            }

            List<PrmAssociatedUnitsDto> lstAssocicatedUnits = inputDto.getPrmAssociatedUnitsDto();
            if (ListUtil.isNotEmpty(lstAssocicatedUnits)) {
                List<PrmAssociatedUnitsCDto> lstAssociatedUnitChange = new ArrayList<PrmAssociatedUnitsCDto>();
                for (PrmAssociatedUnitsDto associatedUnits : lstAssocicatedUnits) {
                    PrmAssociatedUnitsCDto associatedUnitsChange = new PrmAssociatedUnitsCDto();
                    BeanUtil.bean2Bean(associatedUnits, associatedUnitsChange);
                    associatedUnitsChange.setLastUuid(associatedUnits.getUuid());
                    lstAssociatedUnitChange.add(associatedUnitsChange);
                }
                outputDto.setPrmAssociatedUnitsCDto(lstAssociatedUnitChange);
            }

            List<PrmBudgetDetailDto> lstBudgetDetail = inputDto.getPrmBudgetDetailDto();
            if (ListUtil.isNotEmpty(lstBudgetDetail)) {
                List<PrmBudgetDetailCDto> lstBudgetDetailChange = new ArrayList<PrmBudgetDetailCDto>();
                for (PrmBudgetDetailDto budgetDetail : lstBudgetDetail) {
                    PrmBudgetDetailCDto budgetDetailChange = new PrmBudgetDetailCDto();
                    BeanUtil.bean2Bean(budgetDetail, budgetDetailChange);
                    budgetDetailChange.setLastUuid(budgetDetail.getUuid());
                    lstBudgetDetailChange.add(budgetDetailChange);
                }
                outputDto.setPrmBudgetDetailCDto(lstBudgetDetailChange);
            }

            List<PrmBudgetOutsourceDto> lstBudgetOutSource = inputDto.getPrmBudgetOutsourceDto();
            if (ListUtil.isNotEmpty(lstBudgetOutSource)) {
                List<PrmBudgetOutsourceCDto> lstBudgetOutsourceChange = new ArrayList<PrmBudgetOutsourceCDto>();
                for (PrmBudgetOutsourceDto budgetOutsource : lstBudgetOutSource) {
                    PrmBudgetOutsourceCDto budgetOutsourceChange = new PrmBudgetOutsourceCDto();
                    BeanUtil.bean2Bean(budgetOutsource, budgetOutsourceChange);
                    budgetOutsourceChange.setLastUuid(budgetOutsource.getUuid());
                    budgetOutsourceChange.setSplitFromUuid(null);
                    lstBudgetOutsourceChange.add(budgetOutsourceChange);
                }
                outputDto.setPrmBudgetOutsourceCDto(lstBudgetOutsourceChange);
            }

            List<PrmBudgetPrincipalDto> lstBudgetPrincipal = inputDto.getPrmBudgetPrincipalDto();
            if (ListUtil.isNotEmpty(lstBudgetPrincipal)) {
                List<PrmBudgetPrincipalCDto> lstBudgetPrincipalChange = new ArrayList<PrmBudgetPrincipalCDto>();
                for (PrmBudgetPrincipalDto budgetPrincipal : lstBudgetPrincipal) {
                    PrmBudgetPrincipalCDto budgetPrincipalChange = new PrmBudgetPrincipalCDto();
                    BeanUtil.bean2Bean(budgetPrincipal, budgetPrincipalChange);
                    budgetPrincipalChange.setLastUuid(budgetPrincipal.getUuid());
                    budgetPrincipalChange.setSplitFromUuid(null);
                    lstBudgetPrincipalChange.add(budgetPrincipalChange);
                }
                outputDto.setPrmBudgetPrincipalCDto(lstBudgetPrincipalChange);
            }

            List<PrmBudgetAccessoryDto> lstBudgetAccessory = inputDto.getPrmBudgetAccessoryDto();
            if (ListUtil.isNotEmpty(lstBudgetAccessory)) {
                List<PrmBudgetAccessoryCDto> lstBudgetAccessoryChange = new ArrayList<PrmBudgetAccessoryCDto>();
                for (PrmBudgetAccessoryDto budgetAccessory : lstBudgetAccessory) {
                    PrmBudgetAccessoryCDto budgetAccessoryChange = new PrmBudgetAccessoryCDto();
                    BeanUtil.bean2Bean(budgetAccessory, budgetAccessoryChange);
                    budgetAccessoryChange.setLastUuid(budgetAccessory.getUuid());
                    budgetAccessoryChange.setSplitFromUuid(null);
                    lstBudgetAccessoryChange.add(budgetAccessoryChange);
                }
                outputDto.setPrmBudgetAccessoryCDto(lstBudgetAccessoryChange);
            }

            List<PrmBudgetRunDto> lstBudgetRun = inputDto.getPrmBudgetRunDto();
            if (ListUtil.isNotEmpty(lstBudgetRun)) {
                List<PrmBudgetRunCDto> lstBudgetRunChange = new ArrayList<PrmBudgetRunCDto>();
                for (PrmBudgetRunDto budgetRun : lstBudgetRun) {
                    PrmBudgetRunCDto budgetRunChange = new PrmBudgetRunCDto();
                    BeanUtil.bean2Bean(budgetRun, budgetRunChange);
                    budgetRunChange.setLastUuid(budgetRun.getUuid());
                    lstBudgetRunChange.add(budgetRunChange);
                }
                outputDto.setPrmBudgetRunCDto(lstBudgetRunChange);
            }
        }

        loadExtraDescField(outputDto);
        return outputDto;
    }

    public void loadExtraDescField(PrmProjectMainCDto projectMainCDto) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        if (StringUtil.isNotEmpty(projectMainCDto.getContractorOffice())) {
            projectMainCDto.setContractorOfficeDesc(OrgHelper.getOrgNameByCode(projectMainCDto.getContractorOffice()));
        }
        String projectManagerId = projectMainCDto.getProjectManager();
        List<String> lstUserId = new ArrayList<String>();

        List<PrmMemberDetailPCDto> lstMember = projectMainCDto.getPrmMemberDetailPCDto();
        if (ListUtil.isNotEmpty(lstMember)) {
            lstUserId = lstMember.stream().filter(x -> StringUtil.isNotEmpty(x.getStaffId()))
                    .map(x -> x.getStaffId()).distinct().collect(Collectors.toList());
        }
        if (StringUtil.isNotEmpty(projectManagerId) && !lstUserId.contains(projectManagerId)) {
            lstUserId.add(projectManagerId);
        }

        projectMainCDto.setProjectManagerRoleUuid(getProjectManagerRoleUuid());

        List<PrmQsPCDto> lstQsp = projectMainCDto.getPrmQsPCDto();
        if (ListUtil.isNotEmpty(lstQsp)) {
            for (PrmQsPCDto qsPCDto : lstQsp) {
                if (StringUtil.isNotEmpty(qsPCDto.getSafePrincipal()) && !lstUserId.contains(qsPCDto.getSafePrincipal())) {
                    lstUserId.add(qsPCDto.getSafePrincipal());
                }
                if (StringUtil.isNotEmpty(qsPCDto.getSafeContact()) && !lstUserId.contains(qsPCDto.getSafeContact())) {
                    lstUserId.add(qsPCDto.getSafeContact());
                }
                if (StringUtil.isNotEmpty(qsPCDto.getQualityPrincipal()) && !lstUserId.contains(qsPCDto.getQualityPrincipal())) {
                    lstUserId.add(qsPCDto.getQualityPrincipal());
                }
                if (StringUtil.isNotEmpty(qsPCDto.getQualityContact()) && !lstUserId.contains(qsPCDto.getQualityContact())) {
                    lstUserId.add(qsPCDto.getQualityContact());
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
                        projectMainCDto.setProjectManagerDesc(user.getUserName());
                    }

                    if (ListUtil.isNotEmpty(lstQsp)) {
                        for (PrmQsPCDto qsPCDto : lstQsp) {
                            if (StringUtil.isNotEmpty(qsPCDto.getSafePrincipal()) && user.getUserId().equals(qsPCDto
                                    .getSafePrincipal())) {
                                qsPCDto.setSafePrincipalDesc(user.getUserName());
                            }
                            if (StringUtil.isNotEmpty(qsPCDto.getSafeContact()) && user.getUserId().equals(qsPCDto.getSafeContact())) {
                                qsPCDto.setSafeContactDesc(user.getUserName());
                            }
                            if (StringUtil.isNotEmpty(qsPCDto.getQualityPrincipal()) && user.getUserId().equals(qsPCDto.getQualityPrincipal())) {
                                qsPCDto.setQualityPrincipalDesc(user.getUserName());
                            }
                            if (StringUtil.isNotEmpty(qsPCDto.getQualityContact()) && user.getUserId().equals(qsPCDto.getQualityContact())) {
                                qsPCDto.setQualityContactDesc(user.getUserName());
                            }
                        }
                    }
                }

                if (ListUtil.isNotEmpty(lstMember)) {
                    lstMember.stream().filter(x -> StringUtil.isNotEmpty(x.getStaffId())).forEach(x -> {
                        Optional<ScdpUser> userInfo = lstUser.stream().filter(y -> y.getUserId().equals(x.getStaffId()))
                                .findFirst();
                        if (userInfo.isPresent()) {
                            x.setStaffIdDesc(userInfo.get().getUserName());
                        }
                    });
                }
            }
        }
        List<String> lstPartyId = new ArrayList<>();
        List<String> lstContractId = new ArrayList<>();

        List<PrmContractDetailCDto> lstContractDetail = projectMainCDto.getPrmContractDetailCDto();
        if (ListUtil.isNotEmpty(lstContractDetail)) {
            for (PrmContractDetailCDto contractDetailDto : lstContractDetail) {
                if (StringUtil.isNotEmpty(contractDetailDto.getPrmContractId())) {
                    lstContractId.add(contractDetailDto.getPrmContractId());
                }
                if (StringUtil.isNotEmpty(contractDetailDto.getCustomerId())) {
                    lstPartyId.add(contractDetailDto.getCustomerId());
                }
            }
        }

        List<PrmCustomer> lstParty = new ArrayList<>();
        List<Map<String, Object>> mapParty = new ArrayList<>();
        List<PrmContract> lstContract = new ArrayList<>();
        if (ListUtil.isNotEmpty(lstPartyId)) {
            //已经删除的老业主单位需要code转name，所以采用sql查询的方式查出所有数据，包括is_void=1的
            String sql = "select * from prm_customer t where t.uuid in (" + StringUtil.joinForSqlIn(lstPartyId, ",") + ")";
            mapParty = pcm.findByNativeSQL(new DAOMeta(sql));
            Map mapContractId = new HashMap();
            mapContractId.put(CommonAttribute.UUID, lstContractId);
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
            for (PrmContractDetailCDto contractDetailDto : lstContractDetail) {
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
                            contractDetailDto.setInnerPurchaseReqId(contract.getInnerPurchaseReqId());
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

        List<PrmBudgetDetailCDto> lstDetail = projectMainCDto.getPrmBudgetDetailCDto();
        if (ListUtil.isNotEmpty(lstDetail)) {
            List<Map<String, Object>> lstSubject = financialsubjectManager.getPrmSubjectCodes();
            String vatType = getPrmChangeTaxType(projectMainCDto);
            for (PrmBudgetDetailCDto detailCDto : lstDetail) {
                if (ListUtil.isNotEmpty(lstSubject)) {
                    for (Map<String, Object> subjectMap : lstSubject) {
                        String budgetCode = (String) subjectMap.get(FinancialSubjectAttribute.SUBJECT_NO);
                        if (budgetCode != null && budgetCode.equals(detailCDto.getBudgetCode())) {
                            detailCDto.setSubjectComment((String) subjectMap.get(FinancialSubjectAttribute.DESCP));
                            break;
                        }
                    }
                }
                if (ContractTaxType.VAT_TAX.equals(vatType)) {
                    if (ArrayUtil.contains(PrmBudgetCodes.CAL_EXCLUDING_VAT_ITEMS, detailCDto.getBudgetCode())) {
                        detailCDto.setExcludingVatAmount(MathUtil.sub(detailCDto.getCostControlMoney(), detailCDto.getVatAmount()));
                    } else if (PrmBudgetCodes.TECHNICAL_FEE_H.equals(detailCDto.getBudgetCode())) {
                        detailCDto.setExcludingVatAmount(MathUtil.sub(detailCDto.getContractMoney(), detailCDto.getVatAmount()));
                    }
                }
            }
        }
        if (!PrmProjectMainAttribute.PRM_DETAIL_NEW.equals(projectMainCDto.getDetailType())) {
            fillPurchasePlanDetailInfo(projectMainCDto);
            fillBudgetTotalValue(projectMainCDto);
            fillBudgetSplitFromSeqNo(projectMainCDto);
            fillBudgetRunLockedMoney(projectMainCDto);
        }
    }

    //填充主材、辅材、外协的科目属性
    private void fillPurchasePlanDetailInfo(PrmProjectMainCDto projectMainCDto) {
        PurchaseplanManager purchaseplanManager = BeanFactory.getBean("purchaseplan-manager");
        Map<String, Map<String, Map<String, Object>>> purchaseInfoMap = purchaseplanManager.getPurchasePlanDetailInfo
                (projectMainCDto.getPrmProjectMainId());
        if (MapUtil.isNotEmpty(purchaseInfoMap)) {
            if (purchaseInfoMap.containsKey(PrmProjectMainAttribute.OUTSOURCE) &&
                    projectMainCDto.getPrmBudgetOutsourceCDto() != null) {
                Map<String, Map<String, Object>> purchaseDetailMap = purchaseInfoMap.get(PrmProjectMainAttribute
                        .OUTSOURCE);

                Map<String, List<PrmBudgetOutsourceCDto>> outSource = projectMainCDto.getPrmBudgetOutsourceCDto().stream().collect(Collectors.groupingBy(PrmBudgetOutsourceCDto::getUuid));
                projectMainCDto.getPrmBudgetOutsourceCDto().stream().forEach(t -> {
                    if (StringUtil.isNotEmpty(t.getLastUuid()) && purchaseDetailMap.containsKey(t.getLastUuid())) {
                        t.setSubjectProperty((String) purchaseDetailMap.get(t.getLastUuid()).get("subjectProperty"));
                        t.setLockedAmount((BigDecimal) purchaseDetailMap.get(t.getLastUuid()).get("lockedAmount"));
                        t.setLockedMoney((BigDecimal) purchaseDetailMap.get(t.getLastUuid()).get("lockedMoney"));
                        t.setPackageNo((BigDecimal) purchaseDetailMap.get(t.getLastUuid()).get("packageNo"));
                        t.setPackageName((String) purchaseDetailMap.get(t.getLastUuid()).get("packageName"));
                    } else if (StringUtil.isNotEmpty(t.getSplitFromUuid())) {
                        if (outSource.containsKey(t.getSplitFromUuid())) {
                            PrmBudgetOutsourceCDto c = outSource.get(t.getSplitFromUuid()).get(0);
                            if (StringUtil.isNotEmpty(c.getLastUuid()) && purchaseDetailMap.containsKey(c.getLastUuid())) {
                                t.setSubjectProperty((String) purchaseDetailMap.get(c.getLastUuid()).get("subjectProperty"));
                                t.setLockedAmount((BigDecimal) purchaseDetailMap.get(c.getLastUuid()).get("lockedAmount"));
                                t.setLockedMoney((BigDecimal) purchaseDetailMap.get(c.getLastUuid()).get("lockedMoney"));
                                t.setPackageNo((BigDecimal) purchaseDetailMap.get(c.getLastUuid()).get("packageNo"));
                                t.setPackageName((String) purchaseDetailMap.get(c.getLastUuid()).get("packageName"));
                            }
                        }
                    }
                });
            }
            if (purchaseInfoMap.containsKey(PrmProjectMainAttribute.PRINCIPAL) &&
                    projectMainCDto.getPrmBudgetPrincipalCDto() != null) {
                Map<String, Map<String, Object>> purchaseDetailMap = purchaseInfoMap.get(PrmProjectMainAttribute
                        .PRINCIPAL);

                Map<String, List<PrmBudgetPrincipalCDto>> principal = projectMainCDto.getPrmBudgetPrincipalCDto().stream().collect(Collectors.groupingBy(PrmBudgetPrincipalCDto::getUuid));
                projectMainCDto.getPrmBudgetPrincipalCDto().stream().forEach(t -> {
                    if (StringUtil.isNotEmpty(t.getLastUuid()) && purchaseDetailMap.containsKey(t.getLastUuid())) {
                        t.setSubjectProperty((String) purchaseDetailMap.get(t.getLastUuid()).get("subjectProperty"));
                        t.setLockedAmount((BigDecimal) purchaseDetailMap.get(t.getLastUuid()).get("lockedAmount"));
                        t.setLockedMoney((BigDecimal) purchaseDetailMap.get(t.getLastUuid()).get("lockedMoney"));
                        t.setPackageNo((BigDecimal) purchaseDetailMap.get(t.getLastUuid()).get("packageNo"));
                        t.setPackageName((String) purchaseDetailMap.get(t.getLastUuid()).get("packageName"));
                    } else {
                        if (principal.containsKey(t.getSplitFromUuid())) {
                            PrmBudgetPrincipalCDto c = principal.get(t.getSplitFromUuid()).get(0);
                            if (StringUtil.isNotEmpty(c.getLastUuid()) && purchaseDetailMap.containsKey(c.getLastUuid())) {
                                t.setSubjectProperty((String) purchaseDetailMap.get(c.getLastUuid()).get("subjectProperty"));
                                t.setLockedAmount((BigDecimal) purchaseDetailMap.get(c.getLastUuid()).get("lockedAmount"));
                                t.setLockedMoney((BigDecimal) purchaseDetailMap.get(c.getLastUuid()).get("lockedMoney"));
                                t.setPackageNo((BigDecimal) purchaseDetailMap.get(c.getLastUuid()).get("packageNo"));
                                t.setPackageName((String) purchaseDetailMap.get(c.getLastUuid()).get("packageName"));
                            }
                        }
                    }
                });
            }
            if (purchaseInfoMap.containsKey(PrmProjectMainAttribute.ACCESSORY) &&
                    projectMainCDto.getPrmBudgetAccessoryCDto() != null) {
                Map<String, Map<String, Object>> purchaseDetailMap = purchaseInfoMap.get(PrmProjectMainAttribute
                        .ACCESSORY);

                Map<String, List<PrmBudgetAccessoryCDto>> accessory = projectMainCDto.getPrmBudgetAccessoryCDto().stream().collect(Collectors.groupingBy(PrmBudgetAccessoryCDto::getUuid));

                projectMainCDto.getPrmBudgetAccessoryCDto().stream().forEach(t -> {
                    if (StringUtil.isNotEmpty(t.getLastUuid()) && purchaseDetailMap.containsKey(t.getLastUuid())) {
                        t.setSubjectProperty((String) purchaseDetailMap.get(t.getLastUuid()).get("subjectProperty"));
                        t.setLockedAmount((BigDecimal) purchaseDetailMap.get(t.getLastUuid()).get("lockedAmount"));
                        t.setLockedMoney((BigDecimal) purchaseDetailMap.get(t.getLastUuid()).get("lockedMoney"));
                        t.setPackageNo((BigDecimal) purchaseDetailMap.get(t.getLastUuid()).get("packageNo"));
                        t.setPackageName((String) purchaseDetailMap.get(t.getLastUuid()).get("packageName"));
                    } else {
                        if (accessory.containsKey(t.getSplitFromUuid())) {
                            PrmBudgetAccessoryCDto c = accessory.get(t.getSplitFromUuid()).get(0);
                            if (StringUtil.isNotEmpty(c.getLastUuid()) && purchaseDetailMap.containsKey(c.getLastUuid())) {
                                t.setSubjectProperty((String) purchaseDetailMap.get(c.getLastUuid()).get("subjectProperty"));
                                t.setLockedAmount((BigDecimal) purchaseDetailMap.get(c.getLastUuid()).get("lockedAmount"));
                                t.setLockedMoney((BigDecimal) purchaseDetailMap.get(c.getLastUuid()).get("lockedMoney"));
                                t.setPackageNo((BigDecimal) purchaseDetailMap.get(c.getLastUuid()).get("packageNo"));
                                t.setPackageName((String) purchaseDetailMap.get(c.getLastUuid()).get("packageName"));
                            }
                        }
                    }
                });
            }
        }
    }

    //填充原始预算金额
    private void fillBudgetTotalValue(PrmProjectMainCDto projectMainCDto) {
        if (StringUtil.isNotEmpty(projectMainCDto.getPrmProjectMainId())) {
            if (ListUtil.isNotEmpty(projectMainCDto.getPrmBudgetOutsourceCDto())) {
                List<PrmBudgetOutsourceCDto> prmBudgetOutsourceCDto = projectMainCDto.getPrmBudgetOutsourceCDto().stream().filter(
                        t -> StringUtil.isNotEmpty(t.getLastUuid())).collect(Collectors.toList());
                if (ListUtil.isNotEmpty(prmBudgetOutsourceCDto)) {
                    Map<String, BigDecimal> preTotalValue = loadBudgetPreTotalValue(PrmProjectMainAttribute.OUTSOURCE, projectMainCDto.getPrmProjectMainId());
                    if (MapUtil.isNotEmpty(preTotalValue)) {
                        prmBudgetOutsourceCDto.forEach(t -> {
                            if (preTotalValue.containsKey(t.getLastUuid())) {
                                t.setPreTotalValue(preTotalValue.get(t.getLastUuid()));
                            }
                        });
                    }
                }
            }

            if (ListUtil.isNotEmpty(projectMainCDto.getPrmBudgetPrincipalCDto())) {
                List<PrmBudgetPrincipalCDto> prmBudgetPrincipalCDto = projectMainCDto.getPrmBudgetPrincipalCDto().stream().filter(
                        t -> StringUtil.isNotEmpty(t.getLastUuid())).collect(Collectors.toList());
                if (ListUtil.isNotEmpty(prmBudgetPrincipalCDto)) {
                    Map<String, BigDecimal> preTotalValue = loadBudgetPreTotalValue(PrmProjectMainAttribute.PRINCIPAL, projectMainCDto.getPrmProjectMainId());
                    if (MapUtil.isNotEmpty(preTotalValue)) {
                        prmBudgetPrincipalCDto.forEach(t -> {
                            if (preTotalValue.containsKey(t.getLastUuid())) {
                                t.setPreTotalValue(preTotalValue.get(t.getLastUuid()));
                            }
                        });
                    }
                }
            }
            if (ListUtil.isNotEmpty(projectMainCDto.getPrmBudgetAccessoryCDto())) {
                List<PrmBudgetAccessoryCDto> prmBudgetAccessoryCDto = projectMainCDto.getPrmBudgetAccessoryCDto().stream().filter(
                        t -> StringUtil.isNotEmpty(t.getLastUuid())).collect(Collectors.toList());
                if (ListUtil.isNotEmpty(prmBudgetAccessoryCDto)) {
                    Map<String, BigDecimal> preTotalValue = loadBudgetPreTotalValue(PrmProjectMainAttribute.ACCESSORY, projectMainCDto.getPrmProjectMainId());
                    if (MapUtil.isNotEmpty(preTotalValue)) {
                        prmBudgetAccessoryCDto.forEach(t -> {
                            if (preTotalValue.containsKey(t.getLastUuid())) {
                                t.setPreTotalValue(preTotalValue.get(t.getLastUuid()));
                            }
                        });
                    }
                }
            }
        }
    }

    private Map<String, BigDecimal> loadBudgetPreTotalValue(String type, String mainUuid) {
        Map<String, BigDecimal> rtn = new HashMap<String, BigDecimal>();
        String sql = null;
        if (type.equals(PrmProjectMainAttribute.OUTSOURCE)) {
            sql = "select t.uuid, t.total_value  from prm_budget_outsource t  where (t.is_void = '0' or t.is_void is null)    and t.prm_project_main_id=? ";
        } else if (type.equals(PrmProjectMainAttribute.ACCESSORY)) {
            sql = "select t.uuid, t.total_value  from prm_budget_accessory t  where (t.is_void = '0' or t.is_void is null)    and t.prm_project_main_id=? ";
        } else if (type.equals(PrmProjectMainAttribute.PRINCIPAL)) {
            sql = "select t.uuid, t.scheming_total_value as total_value  from prm_budget_principal t  where (t.is_void = '0' or t.is_void is null)    and t.prm_project_main_id=? ";
        }
        if (StringUtil.isNotEmpty(sql)) {
            List lstParam = new ArrayList();
            lstParam.add(mainUuid);
            DAOMeta daoMeta = new DAOMeta(sql, lstParam, false);
            daoMeta.setNeedFilter(false);
            PersistenceFactory.getInstance().findByNativeSQL(daoMeta).forEach(t -> {
                if (!rtn.containsKey(t.get("uuid").toString())) {
                    BigDecimal totalValue = BigDecimal.ZERO;
                    if (t.get("totalValue") != null) {
                        totalValue = (BigDecimal) t.get("totalValue");
                    }
                    rtn.put(t.get("uuid").toString(), totalValue);
                }
            });
        }

        return rtn;
    }

    //填充原始预算金额
    private void fillBudgetSplitFromSeqNo(PrmProjectMainCDto projectMainCDto) {
        if (ListUtil.isNotEmpty(projectMainCDto.getPrmBudgetOutsourceCDto())) {
            List<PrmBudgetOutsourceCDto> outsourceCDto = projectMainCDto.getPrmBudgetOutsourceCDto().stream().
                    filter(t -> StringUtil.isNotEmpty(t.getSplitFromUuid()) && StringUtil.isEmpty(t.getLastUuid())).collect(Collectors.toList());
            if (ListUtil.isNotEmpty(outsourceCDto)) {
                Map<String, List<PrmBudgetOutsourceCDto>> outsourceCDtoMap = projectMainCDto.getPrmBudgetOutsourceCDto().stream()
                        .collect(Collectors.groupingBy(PrmBudgetOutsourceCDto::getUuid));
                outsourceCDto.forEach(t -> {
                    if (outsourceCDtoMap.containsKey(t.getSplitFromUuid())) {
                        t.setSplitFromUuidNo(outsourceCDtoMap.get(t.getSplitFromUuid()).get(0).getSerialNumber());
                    }
                });
            }
        }
        if (ListUtil.isNotEmpty(projectMainCDto.getPrmBudgetPrincipalCDto())) {
            List<PrmBudgetPrincipalCDto> principalCDto = projectMainCDto.getPrmBudgetPrincipalCDto().stream().
                    filter(t -> StringUtil.isNotEmpty(t.getSplitFromUuid()) && StringUtil.isEmpty(t.getLastUuid())).collect(Collectors.toList());
            if (ListUtil.isNotEmpty(principalCDto)) {
                Map<String, List<PrmBudgetPrincipalCDto>> principalCDtoMap = projectMainCDto.getPrmBudgetPrincipalCDto().stream()
                        .collect(Collectors.groupingBy(PrmBudgetPrincipalCDto::getUuid));
                principalCDto.forEach(t -> {
                    if (principalCDtoMap.containsKey(t.getSplitFromUuid())) {
                        t.setSplitFromUuidNo(principalCDtoMap.get(t.getSplitFromUuid()).get(0).getSerialNumber());
                    }
                });
            }
        }
        if (ListUtil.isNotEmpty(projectMainCDto.getPrmBudgetAccessoryCDto())) {
            List<PrmBudgetAccessoryCDto> accessoryCDto = projectMainCDto.getPrmBudgetAccessoryCDto().stream().
                    filter(t -> StringUtil.isNotEmpty(t.getSplitFromUuid()) && StringUtil.isEmpty(t.getLastUuid())).collect(Collectors.toList());
            if (ListUtil.isNotEmpty(accessoryCDto)) {
                Map<String, List<PrmBudgetAccessoryCDto>> accessoryCDtoMap = projectMainCDto.getPrmBudgetAccessoryCDto().stream()
                        .collect(Collectors.groupingBy(PrmBudgetAccessoryCDto::getUuid));
                accessoryCDto.forEach(t -> {
                    if (accessoryCDtoMap.containsKey(t.getSplitFromUuid())) {
                        t.setSplitFromUuidNo(accessoryCDtoMap.get(t.getSplitFromUuid()).get(0).getSerialNumber());
                    }
                });
            }
        }
    }

    private void fillBudgetRunLockedMoney(PrmProjectMainCDto projectMainCDto) {
        if (StringUtil.isNotEmpty(projectMainCDto.getPrmProjectMainId())) {
            if (ListUtil.isNotEmpty(projectMainCDto.getPrmBudgetRunCDto())) {
                PersistenceCrudManager pcm = PersistenceFactory.getInstance();
                List lstParam = new ArrayList();
                lstParam.add(projectMainCDto.getPrmProjectMainId());
                DAOMeta daoMeta = DAOHelper.getDAO("prmprojectmainc-dao", "prm-actual-budget", lstParam);
                daoMeta.setNeedFilter(false);
                List<Map<String, Object>> lstActualBudget = pcm.findByNativeSQL(daoMeta);
                Map<String, Map<String, Object>> mapActualBudget = lstActualBudget.stream()
                        .filter(t -> PrmProjectMainAttribute.RUN.equals((String) (((Map) t).get("budgetFirstType")))).filter(t -> ((Map) t).get("packageId") == null)
                        .collect(Collectors.toMap(t -> (String) (((Map) t).get(PrmBudgetDetailCAttribute.BUDGET_CODE)), t -> t));
                if (MapUtil.isNotEmpty(mapActualBudget)) {
                    projectMainCDto.getPrmBudgetRunCDto().forEach(t -> {
                        if (mapActualBudget.containsKey(t.getFinancialSubjectCode())) {
                            BigDecimal lockedBudget = (BigDecimal) mapActualBudget.get(t.getFinancialSubjectCode()).get(PrmBudgetDetailCAttribute.LOCKED_BUDGET);
                            t.setLockedMoney(lockedBudget);
                        } else {
                            t.setLockedMoney(BigDecimal.ZERO);
                        }
                    });
                }
            }
        }
    }

    public void updateHeaderAndDetailAmount(String uuid) throws BizException {

        String taxType = getPrmChangeTaxType(uuid);
        if (ContractTaxType.BUSINESS_TAX.equals(taxType)) {
            updateAmountForBusinessTax(uuid);
        } else if (ContractTaxType.VAT_TAX.equals(taxType)) {
            updateAmountForVatTax(uuid);
        }
    }

    private void updateAmountForBusinessTax(String uuid) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmProjectMainC header = pcm.findByPK(PrmProjectMainC.class, uuid);
        if (header == null) {
            throw new BizException(MessageHelper.getMessage("MSG_PRM_PROJECT_MAIN_C_NOT_EXIST_ERROR"));
        }

        Map condition = new HashMap<>();
        condition.put(PrmBudgetDetailCAttribute.PRM_PROJECT_MAIN_C_ID, uuid);
        List<PrmBudgetDetailC> lstDetail = pcm.findByAnyFields(PrmBudgetDetailC.class, condition, "seq_no asc");
        List<PrmBudgetPrincipalC> lstPrincipal = pcm.findByAnyFields(PrmBudgetPrincipalC.class, condition, "seq_no asc");
        List<PrmBudgetAccessoryC> lstAccessory = pcm.findByAnyFields(PrmBudgetAccessoryC.class, condition, "seq_no asc");
        List<PrmBudgetOutsourceC> lstOutsource = pcm.findByAnyFields(PrmBudgetOutsourceC.class, condition, "seq_no asc");
        List<PrmBudgetRunC> lstRun = pcm.findByAnyFields(PrmBudgetRunC.class, condition, "seq_no asc");

        double totalPrincipal = 0d;
        double totalAccessory = 0d;
        double totalOutsource = 0d;
        double totalRun = 0d;
        if (ListUtil.isNotEmpty(lstPrincipal)) {
            totalPrincipal = lstPrincipal.stream().filter(x -> x.getContractTotalValue() != null).map(x -> x
                    .getSchemingTotalValue().doubleValue())
                    .reduce((sum, x) -> sum + x).get();
        }
        if (ListUtil.isNotEmpty(lstAccessory)) {
            totalAccessory = lstAccessory.stream().filter(x -> x.getTotalValue() != null).map(x -> x
                    .getTotalValue().doubleValue())
                    .reduce((sum, x) -> sum + x).get();
        }
        if (ListUtil.isNotEmpty(lstOutsource)) {
            totalOutsource = lstOutsource.stream().filter(x -> x.getTotalValue() != null).map(x -> x
                    .getTotalValue().doubleValue())
                    .reduce((sum, x) -> sum + x).get();
        }
        if (ListUtil.isNotEmpty(lstRun)) {
            totalRun = lstRun.stream().filter(x -> x.getTotalValue() != null).map(x -> x
                    .getTotalValue().doubleValue())
                    .reduce((sum, x) -> sum + x).get();
        }

        if (ListUtil.isNotEmpty(lstDetail)) {

            List<Map<String, Object>> lstSubject = financialsubjectManager.getPrmSubjectCodes();
            //1 is contract column value
            //2 is joint column value
            //3 is cost column value
            BigDecimal positiveAmount1 = BigDecimal.ZERO;
            BigDecimal positiveAmount2 = BigDecimal.ZERO;
            BigDecimal negativeAmount1 = BigDecimal.ZERO;
            BigDecimal negativeAmount2 = BigDecimal.ZERO;
            for (Iterator<PrmBudgetDetailC> iterator = lstDetail.iterator(); iterator.hasNext(); ) {
                PrmBudgetDetailC detail = iterator.next();
                String budgetCode = detail.getBudgetCode();
                if (PrmBudgetCodes.PRINCIPAL.equals(budgetCode)) {
                    detail.setCostControlMoney(BigDecimal.valueOf(totalPrincipal).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                if (PrmBudgetCodes.ACCESSORY.equals(budgetCode)) {
                    detail.setCostControlMoney(BigDecimal.valueOf(totalAccessory).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                if (PrmBudgetCodes.OUTSOURCE.equals(budgetCode)) {
                    detail.setCostControlMoney(BigDecimal.valueOf(totalOutsource).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                if (PrmBudgetCodes.RUN.equals(budgetCode)) {
                    detail.setCostControlMoney(BigDecimal.valueOf(totalRun).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                detail.setVatAmount(null);
                BigDecimal contractMoney = detail.getContractMoney();
                BigDecimal jointDesignMoney = detail.getJointDesignMoney();
                if (ArrayUtil.contains(PrmBudgetCodes.POSITIVE_TOTAL_ITEMS, budgetCode)) {
                    if (contractMoney != null) {
                        positiveAmount1 = positiveAmount1.add(contractMoney);
                    }
                    if (jointDesignMoney != null) {
                        positiveAmount2 = positiveAmount2.add(jointDesignMoney);
                    }
                }
                if (ArrayUtil.contains(PrmBudgetCodes.NEGATIVE_TOTAL_ITEMS, budgetCode)) {
                    if (contractMoney != null) {
                        negativeAmount1 = negativeAmount1.add(contractMoney);
                    }
                    if (jointDesignMoney != null) {
                        negativeAmount2 = negativeAmount2.add(jointDesignMoney);
                    }
                }
            }

            BigDecimal projectMoney1 = positiveAmount1.subtract(negativeAmount1);
            BigDecimal projectMoney2 = positiveAmount2.subtract(negativeAmount2);
            PrmBudgetDetailC estimatedCostDetail = null;
            PrmBudgetDetailC plannedProfitDetail = null;
            BigDecimal costTotal3 = BigDecimal.ZERO;
            for (Iterator<PrmBudgetDetailC> iterator = lstDetail.iterator(); iterator.hasNext(); ) {
                PrmBudgetDetailC detail = iterator.next();
                String budgetCode = detail.getBudgetCode();
                if (PrmBudgetCodes.CONTRACT_TOTAL.equals(budgetCode)) {
                    detail.setContractMoney(positiveAmount1);
                    detail.setJointDesignMoney(positiveAmount2);
//                    detail.setCostControlMoney(positiveAmount1);
                } else if (PrmBudgetCodes.PROJECT_MONEY.equals(budgetCode)) {
                    detail.setContractMoney(projectMoney1);
                    detail.setJointDesignMoney(projectMoney2);
//                    detail.setCostControlMoney(projectMoney1);
                } else if (PrmBudgetCodes.ESTIMATED_COST.equals(budgetCode)) {
                    estimatedCostDetail = detail;
                } else if (PrmBudgetCodes.PLANNED_PROFIT.equals(budgetCode)) {
                    plannedProfitDetail = detail;
                } else {
                    if (ArrayUtil.contains(PrmBudgetCodes.TAX_FLEX_RATE_ITEMS, budgetCode)) {
                        BigDecimal rate = getTaxRate(lstSubject, budgetCode, header.getTaxType());
                        if (rate != null) {
                            detail.setCostControlMoney(MathUtil.mul(projectMoney1, rate));
                        } else {
                            detail.setCostControlMoney(BigDecimal.ZERO);
                        }
                    }
                }
                if (ArrayUtil.contains(PrmBudgetCodes.COST_TOTAL_ITEMS, budgetCode)) {
                    BigDecimal costControlMoney = detail.getCostControlMoney();
                    if (costControlMoney != null) {
                        costTotal3 = costTotal3.add(costControlMoney);
                    }
                }
            }
            if (estimatedCostDetail != null) {
                estimatedCostDetail.setCostControlMoney(costTotal3);
            }
            if (plannedProfitDetail != null) {
                plannedProfitDetail.setCostControlMoney(projectMoney1.subtract(costTotal3));
            }

            header.setProjectMoney(projectMoney1);
            header.setCostControlMoney(costTotal3);
            pcm.batchUpdate(lstDetail);
            pcm.update(header);
        }
    }

    private void updateAmountForVatTax(String uuid) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmProjectMainC header = pcm.findByPK(PrmProjectMainC.class, uuid);
        if (header == null) {
            throw new BizException(MessageHelper.getMessage("MSG_PRM_PROJECT_MAIN_C_NOT_EXIST_ERROR"));
        }

        Map condition = new HashMap<>();
        condition.put(PrmBudgetDetailCAttribute.PRM_PROJECT_MAIN_C_ID, uuid);
        List<PrmBudgetDetailC> lstDetail = pcm.findByAnyFields(PrmBudgetDetailC.class, condition, "seq_no asc");
        List<PrmBudgetPrincipalC> lstPrincipal = pcm.findByAnyFields(PrmBudgetPrincipalC.class, condition, "seq_no asc");
        List<PrmBudgetAccessoryC> lstAccessory = pcm.findByAnyFields(PrmBudgetAccessoryC.class, condition, "seq_no asc");
        List<PrmBudgetOutsourceC> lstOutsource = pcm.findByAnyFields(PrmBudgetOutsourceC.class, condition, "seq_no asc");
        List<PrmBudgetRunC> lstRun = pcm.findByAnyFields(PrmBudgetRunC.class, condition, "seq_no asc");

        double totalPrincipal = 0d;
        double totalAccessory = 0d;
        double totalOutsource = 0d;
        double totalRun = 0d;
        if (ListUtil.isNotEmpty(lstPrincipal)) {
            totalPrincipal = lstPrincipal.stream().filter(x -> x.getContractTotalValue() != null).map(x -> x
                    .getSchemingTotalValue().doubleValue())
                    .reduce((sum, x) -> sum + x).get();
        }
        if (ListUtil.isNotEmpty(lstAccessory)) {
            totalAccessory = lstAccessory.stream().filter(x -> x.getTotalValue() != null).map(x -> x
                    .getTotalValue().doubleValue())
                    .reduce((sum, x) -> sum + x).get();
        }
        if (ListUtil.isNotEmpty(lstOutsource)) {
            totalOutsource = lstOutsource.stream().filter(x -> x.getTotalValue() != null).map(x -> x
                    .getTotalValue().doubleValue())
                    .reduce((sum, x) -> sum + x).get();
        }
        if (ListUtil.isNotEmpty(lstRun)) {
            totalRun = lstRun.stream().filter(x -> x.getTotalValue() != null).map(x -> x
                    .getTotalValue().doubleValue())
                    .reduce((sum, x) -> sum + x).get();
        }

        if (ListUtil.isNotEmpty(lstDetail)) {

            List<Map<String, Object>> lstSubject = financialsubjectManager.getPrmSubjectCodes();
            //1 is contract column value
            //2 is joint column value
            //3 is cost column value
            BigDecimal positiveAmount1 = BigDecimal.ZERO;
            BigDecimal positiveAmount2 = BigDecimal.ZERO;
            BigDecimal negativeAmount1 = BigDecimal.ZERO;
            BigDecimal negativeAmount2 = BigDecimal.ZERO;
            BigDecimal totalVatAmount = BigDecimal.ZERO;
            String codeType = header.getPrmCodeType();//代号类型
            for (Iterator<PrmBudgetDetailC> iterator = lstDetail.iterator(); iterator.hasNext(); ) {
                PrmBudgetDetailC detail = iterator.next();
                String budgetCode = detail.getBudgetCode();
                if (PrmBudgetCodes.PRINCIPAL.equals(budgetCode)) {
                    detail.setCostControlMoney(BigDecimal.valueOf(totalPrincipal).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                if (PrmBudgetCodes.ACCESSORY.equals(budgetCode)) {
                    detail.setCostControlMoney(BigDecimal.valueOf(totalAccessory).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                if (PrmBudgetCodes.OUTSOURCE.equals(budgetCode)) {
                    detail.setCostControlMoney(BigDecimal.valueOf(totalOutsource).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                if (PrmBudgetCodes.RUN.equals(budgetCode)) {
                    detail.setCostControlMoney(BigDecimal.valueOf(totalRun).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                BigDecimal contractMoney = detail.getContractMoney();
                BigDecimal jointDesignMoney = detail.getJointDesignMoney();
                if (ArrayUtil.contains(PrmBudgetCodes.POSITIVE_TOTAL_ITEMS, budgetCode)) {
                    if (contractMoney != null) {
                        positiveAmount1 = positiveAmount1.add(contractMoney);
                    }
                    if (jointDesignMoney != null) {
                        positiveAmount2 = positiveAmount2.add(jointDesignMoney);
                    }
                }
                if (ArrayUtil.contains(PrmBudgetCodes.NEGATIVE_TOTAL_ITEMS, budgetCode)) {
                    if (contractMoney != null) {
                        negativeAmount1 = negativeAmount1.add(contractMoney);
                    }
                    if (jointDesignMoney != null) {
                        negativeAmount2 = negativeAmount2.add(jointDesignMoney);
                    }
                }
                if (ArrayUtil.contains(PrmBudgetCodes.CAL_VAT_ITEMS, budgetCode)) {
                    BigDecimal vatRate = getTaxRate(lstSubject, budgetCode, codeType);
                    BigDecimal vatAmount = MathUtil.div(
                            MathUtil.mul(detail.getCostControlMoney(), vatRate),
                            MathUtil.add(BigDecimal.ONE, vatRate));
                    detail.setVatAmount(vatAmount);
                    totalVatAmount = MathUtil.add(totalVatAmount, vatAmount);
                }
                if (PrmBudgetCodes.TECHNICAL_FEE_H.equals(budgetCode)) {
                    BigDecimal vatRate = getTaxRate(lstSubject, budgetCode, codeType);
                    BigDecimal vatAmount = MathUtil.div(
                            MathUtil.mul(detail.getContractMoney(), vatRate),
                            MathUtil.add(BigDecimal.ONE, vatRate));
                    detail.setVatAmount(vatAmount);
                    totalVatAmount = MathUtil.add(totalVatAmount, vatAmount);
                }
            }

            BigDecimal projectMoney1 = positiveAmount1.subtract(negativeAmount1);
            BigDecimal projectMoney2 = positiveAmount2.subtract(negativeAmount2);
            PrmBudgetDetailC estimatedCostDetail = null;
            PrmBudgetDetailC plannedProfitDetail = null;
            BigDecimal costTotal3 = BigDecimal.ZERO;
            for (Iterator<PrmBudgetDetailC> iterator = lstDetail.iterator(); iterator.hasNext(); ) {
                PrmBudgetDetailC detail = iterator.next();
                String budgetCode = detail.getBudgetCode();
                if (PrmBudgetCodes.CONTRACT_TOTAL.equals(budgetCode)) {
                    detail.setContractMoney(positiveAmount1);
                    detail.setJointDesignMoney(positiveAmount2);
//                    detail.setCostControlMoney(positiveAmount1);
                } else if (PrmBudgetCodes.PROJECT_MONEY.equals(budgetCode)) {
                    detail.setContractMoney(projectMoney1);
                    detail.setJointDesignMoney(projectMoney2);
//                    detail.setCostControlMoney(projectMoney1);
                } else if (PrmBudgetCodes.ESTIMATED_COST.equals(budgetCode)) {
                    estimatedCostDetail = detail;
                } else if (PrmBudgetCodes.PLANNED_PROFIT.equals(budgetCode)) {
                    plannedProfitDetail = detail;
                } else {
                    if (ArrayUtil.contains(PrmBudgetCodes.VAT_FLEX_RATE_ITEMS, budgetCode)) {
                        BigDecimal rate = getTaxRate(lstSubject, budgetCode, codeType);
                        if (rate != null) {
                            detail.setCostControlMoney(MathUtil.mul(projectMoney1, rate));
                        } else {
                            detail.setCostControlMoney(BigDecimal.ZERO);
                        }
                    } else if (PrmBudgetCodes.TAX_NO_STAMP.equals(budgetCode)) {
                        if (PrmCodeTypes.JI_GAI.equals(budgetCode)) {
                            detail.setCostControlMoney(BigDecimal.ZERO);
                        } else {
                            BigDecimal rate1 = getTaxRate(lstSubject, budgetCode, codeType);
                            BigDecimal rate2 = getTaxRate(lstSubject, budgetCode, "SURCHARGE");
                            BigDecimal taxNoStamp = MathUtil.mul(
                                    MathUtil.sub(MathUtil.div(MathUtil.mul(projectMoney1, rate1, 6),
                                            MathUtil.add(BigDecimal.ONE, rate1)), totalVatAmount)
                                    , MathUtil.add(BigDecimal.ONE, rate2)
                            );
                            if (MathUtil.compareTo(taxNoStamp, BigDecimal.ZERO) < 0) {
                                taxNoStamp = BigDecimal.ZERO;
                            }
                            detail.setCostControlMoney(taxNoStamp);
                        }
                    }
                }
                if (ArrayUtil.contains(PrmBudgetCodes.COST_TOTAL_ITEMS, budgetCode)) {
                    BigDecimal costControlMoney = detail.getCostControlMoney();
                    if (costControlMoney != null) {
                        costTotal3 = costTotal3.add(costControlMoney);
                    }
                }
            }
            if (estimatedCostDetail != null) {
                estimatedCostDetail.setCostControlMoney(costTotal3);
            }
            if (plannedProfitDetail != null) {
                plannedProfitDetail.setCostControlMoney(projectMoney1.subtract(costTotal3));
            }

            header.setProjectMoney(projectMoney1);
            header.setCostControlMoney(costTotal3);
            pcm.batchUpdate(lstDetail);
            pcm.update(header);
        }
    }

    private BigDecimal getTaxRate(List<Map<String, Object>> lstSubject, String budgetCode, String type) {
        for (Map<String, Object> finSubject : lstSubject) {
            if (budgetCode.equals(finSubject.get(FinancialSubjectAttribute.SUBJECT_NO))) {
                String descp = (String) finSubject.get(FinancialSubjectAttribute.DESCP);
                if (StringUtil.isNotEmpty(descp)) {
                    Map ratemap = (Map) JsonUtil.deserialize(descp);
                    Object rate = ratemap.get(type);
                    if (StringUtil.isEmpty(rate)) {
                        rate = ratemap.get("*");
                    }
                    if (StringUtil.isNotEmpty(rate)) {
                        return MathUtil.mul(new BigDecimal(String.valueOf(rate)), BigDecimal.valueOf(0.01), 6);
                    }
                }
            }
        }
        return null;
    }

    //remark:对外协、主材、辅材，
    //若其未分包，同步到明细即可
    //若其已经分包，对预算变更拆分的数据添加到拆分前所属包，新添加的明细需要添加一个包，包的金额大于指定值，为刚性，否则为弹性
    public void syncMainToPurchasePlan(String projectUuid, PrmProjectMainC mianC) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmProjectMain prmProjectMain = pcm.findByPK(PrmProjectMain.class, projectUuid);
        if (prmProjectMain == null) {
            throw new BizException(MessageHelper.getMessage("MSG_PRM_PROJECT_MAIN_C_NOT_EXIST_ERROR_2"));
        }
        Map mapCondition = new HashMap();
        mapCondition.put(PrmBudgetDetailAttribute.PRM_PROJECT_MAIN_ID, projectUuid);
        List<PrmPurchasePlanDetail> lstExists = pcm.findByAnyFields(PrmPurchasePlanDetail.class, mapCondition, null);

        List<PrmPurchasePlanDetail> lstInsert = new ArrayList<PrmPurchasePlanDetail>();
        List<PrmPurchasePlanDetail> lstNewOutsourceInsert = new ArrayList<PrmPurchasePlanDetail>();
        List<PrmPurchasePlanDetail> lstNewPrincipalInsert = new ArrayList<PrmPurchasePlanDetail>();
        List<PrmPurchasePlanDetail> lstNewAccessoryInsert = new ArrayList<PrmPurchasePlanDetail>();
        List<PrmPurchasePlanDetail> lstNewRunInsert = new ArrayList<PrmPurchasePlanDetail>();

        Map<String, List<PrmPurchasePlanDetail>> budgetGroup = lstExists.stream().collect(Collectors.groupingBy(x -> x
                .getPrmBudgetType()));//已经同步的
        List<PrmBudgetOutsource> lstPrmOutsource = pcm.findByAnyFields(PrmBudgetOutsource.class, mapCondition, "seq_no " +
                "asc");
        List<PrmBudgetPrincipal> lstPrmPrincipal = pcm.findByAnyFields(PrmBudgetPrincipal.class, mapCondition, "seq_no " +
                "asc");
        List<PrmBudgetAccessory> lstPrmAccessory = pcm.findByAnyFields(PrmBudgetAccessory.class, mapCondition, "seq_no " +
                "asc");
        List<PrmBudgetRun> lstPrmRun = pcm.findByAnyFields(PrmBudgetRun.class, mapCondition, "seq_no " +
                "asc");//所有的运行费

        List<PrmPurchasePlanDetail> lstPurOutsource = budgetGroup.containsKey(PrmBudgetCodes.OUTSOURCE) ? budgetGroup.get
                (PrmBudgetCodes.OUTSOURCE) : new ArrayList<PrmPurchasePlanDetail>();
        List<PrmPurchasePlanDetail> lstPurPrincipal = budgetGroup.containsKey(PrmBudgetCodes.PRINCIPAL) ? budgetGroup.get
                (PrmBudgetCodes.PRINCIPAL) : new ArrayList<PrmPurchasePlanDetail>();
        ;
        List<PrmPurchasePlanDetail> lstPurAccessory = budgetGroup.containsKey(PrmBudgetCodes.ACCESSORY) ? budgetGroup.get
                (PrmBudgetCodes.ACCESSORY) : new ArrayList<PrmPurchasePlanDetail>();
        ;
        List<PrmPurchasePlanDetail> lstPurRun = budgetGroup.containsKey(PrmBudgetCodes.RUN) ? budgetGroup.get
                (PrmBudgetCodes.RUN) : new ArrayList<PrmPurchasePlanDetail>();//如果有则取现在有的，如果没有为空
        ;
        if (ListUtil.isNotEmpty(lstPrmOutsource)) {
            Map<String, List<PrmPurchasePlanDetail>> refIdMap = lstPurOutsource.stream()
                    .filter(t -> StringUtil.isNotEmpty(t.getPurchasePackageId()))
                    .collect(Collectors.groupingBy(PrmPurchasePlanDetail::getPrmBudgetRefId));

            List<PrmPurchasePlanDetail> lstNonExist1 = lstPrmOutsource.stream().filter(x -> x.getAmount() != null && x
                    .getAmount().doubleValue() > 0 && lstPurOutsource.stream().noneMatch(y
                    -> x.getUuid().equals(y.getPrmBudgetRefId()))).map(x -> {
                PrmPurchasePlanDetail newDetail = new PrmPurchasePlanDetail();
                newDetail.setPrmBudgetRefId(x.getUuid());
                newDetail.setPrmBudgetType(PrmBudgetCodes.OUTSOURCE);
                newDetail.setPrmProjectMainId(projectUuid);
                newDetail.setSerialNumber(x.getSerialNumber());
                newDetail.setRemark(x.getRemark());
                newDetail.setName(x.getSupplierCode());
                newDetail.setAmount(x.getAmount());
                newDetail.setBudgetPrice(x.getPrice());
                newDetail.setPurchaseBudgetMoney(x.getTotalValue());
                newDetail.setUnit(x.getUnit());
                //同步编辑字段
                newDetail.setIsStamp(x.getIsStamp());
                newDetail.setCompanyCode(prmProjectMain.getCompanyCode());
                newDetail.setDepartmentCode(prmProjectMain.getContractorOffice());
                if (MapUtil.isNotEmpty(refIdMap) && refIdMap.containsKey(x.getSplitFromUuid())) {
                    newDetail.setSubPackageNo(refIdMap.get(x.getSplitFromUuid()).get(0).getSubPackageNo());
                    newDetail.setPurchasePackageId(refIdMap.get(x.getSplitFromUuid()).get(0).getPurchasePackageId());
                    newDetail.setPurchaseLevel(refIdMap.get(x.getSplitFromUuid()).get(0).getPurchaseLevel());
                    newDetail.setSubjectProperty(refIdMap.get(x.getSplitFromUuid()).get(0).getSubjectProperty());
                }
//                newDetail.setSeqNo(++maxSeqNo);
                return newDetail;
            }).collect(Collectors.toList());
            lstInsert.addAll(lstNonExist1);
            if (!PrmProjectMainAttribute.PRM_DETAIL_NEW.equals(mianC.getDetailType())) {
                lstNewOutsourceInsert.addAll(lstNonExist1.stream().filter(t -> StringUtil.isEmpty(t.getPurchasePackageId())).collect(Collectors.toList()));
            }
        }

        if (ListUtil.isNotEmpty(lstPrmPrincipal)) {
            Map<String, List<PrmPurchasePlanDetail>> refIdMap = lstPurPrincipal.stream()
                    .filter(t -> StringUtil.isNotEmpty(t.getPurchasePackageId()))
                    .collect(Collectors.groupingBy(PrmPurchasePlanDetail::getPrmBudgetRefId));

            List<PrmPurchasePlanDetail> lstNonExist2 = lstPrmPrincipal.stream().filter(x -> x.getAmount() != null && x
                    .getAmount().doubleValue() > 0 && lstPurPrincipal.stream()
                    .noneMatch(y -> x.getUuid().equals(y
                            .getPrmBudgetRefId()))).map(x -> {
                PrmPurchasePlanDetail newDetail = new PrmPurchasePlanDetail();
                newDetail.setPrmBudgetRefId(x.getUuid());
                newDetail.setPrmBudgetType(PrmBudgetCodes.PRINCIPAL);
                newDetail.setPrmProjectMainId(projectUuid);
                newDetail.setSerialNumber(x.getSerialNumber());
                newDetail.setRemark(x.getRemark());
                newDetail.setFactory(x.getFactory());
                newDetail.setAmount(x.getAmount());
                //todo
                newDetail.setBudgetPrice(x.getSchemingPrice());
                newDetail.setPurchaseBudgetMoney(x.getSchemingTotalValue());
                newDetail.setName(x.getEquipmentName());
                newDetail.setModel(x.getEquipmentModel());
//                newDetail.setArriveDate(x.getArrivalDate());
                newDetail.setUnit(x.getUnit());
                //同步编辑字段
                newDetail.setIsStamp(x.getIsStamp());
                newDetail.setCompanyCode(prmProjectMain.getCompanyCode());
                newDetail.setDepartmentCode(prmProjectMain.getContractorOffice());
                if (MapUtil.isNotEmpty(refIdMap) && refIdMap.containsKey(x.getSplitFromUuid())) {
                    newDetail.setSubPackageNo(refIdMap.get(x.getSplitFromUuid()).get(0).getSubPackageNo());
                    newDetail.setPurchasePackageId(refIdMap.get(x.getSplitFromUuid()).get(0).getPurchasePackageId());
                    newDetail.setPurchaseLevel(refIdMap.get(x.getSplitFromUuid()).get(0).getPurchaseLevel());
                    newDetail.setSubjectProperty(refIdMap.get(x.getSplitFromUuid()).get(0).getSubjectProperty());
                }

//                newDetail.setSeqNo(++maxSeqNo);
                return newDetail;
            }).collect(Collectors.toList());
            lstInsert.addAll(lstNonExist2);
            if (!PrmProjectMainAttribute.PRM_DETAIL_NEW.equals(mianC.getDetailType())) {
                lstNewPrincipalInsert.addAll(lstNonExist2.stream().filter(t -> StringUtil.isEmpty(t.getPurchasePackageId())).collect(Collectors.toList()));
            }
        }

        if (ListUtil.isNotEmpty(lstPrmAccessory)) {
            Map<String, List<PrmPurchasePlanDetail>> refIdMap = lstPurAccessory.stream()
                    .filter(t -> StringUtil.isNotEmpty(t.getPurchasePackageId()))
                    .collect(Collectors.groupingBy(PrmPurchasePlanDetail::getPrmBudgetRefId));

            List<PrmPurchasePlanDetail> lstNonExist3 = lstPrmAccessory.stream().filter(x -> x.getAmount() != null && x
                    .getAmount().doubleValue() > 0 && lstPurAccessory.stream()
                    .noneMatch(y -> x.getUuid().equals(y.getPrmBudgetRefId()))).map(x -> {
                PrmPurchasePlanDetail newDetail = new PrmPurchasePlanDetail();
                newDetail.setPrmBudgetRefId(x.getUuid());
                newDetail.setPrmBudgetType(PrmBudgetCodes.ACCESSORY);
                newDetail.setPrmProjectMainId(projectUuid);
                newDetail.setSerialNumber(x.getSerialNumber());
                newDetail.setRemark(x.getRemark());
                newDetail.setAmount(x.getAmount());
                newDetail.setBudgetPrice(x.getPrice());
                newDetail.setPurchaseBudgetMoney(x.getTotalValue());
                newDetail.setName(x.getAccessoryName());
                newDetail.setModel(x.getAccessoryModel());
                //同步编辑字段
                newDetail.setIsStamp(x.getIsStamp());
                newDetail.setCompanyCode(prmProjectMain.getCompanyCode());
                newDetail.setDepartmentCode(prmProjectMain.getContractorOffice());
                if (MapUtil.isNotEmpty(refIdMap) && refIdMap.containsKey(x.getSplitFromUuid())) {
                    newDetail.setSubPackageNo(refIdMap.get(x.getSplitFromUuid()).get(0).getSubPackageNo());
                    newDetail.setPurchasePackageId(refIdMap.get(x.getSplitFromUuid()).get(0).getPurchasePackageId());
                    newDetail.setPurchaseLevel(refIdMap.get(x.getSplitFromUuid()).get(0).getPurchaseLevel());
                    newDetail.setSubjectProperty(refIdMap.get(x.getSplitFromUuid()).get(0).getSubjectProperty());
                }

//                newDetail.setUnit(x.getUnit());
//                newDetail.setSeqNo(++maxSeqNo);
                return newDetail;
            }).collect(Collectors.toList());
            lstInsert.addAll(lstNonExist3);
            if (!PrmProjectMainAttribute.PRM_DETAIL_NEW.equals(mianC.getDetailType())) {
                lstNewAccessoryInsert.addAll(lstNonExist3.stream().filter(t -> StringUtil.isEmpty(t.getPurchasePackageId())).collect(Collectors.toList()));
            }
        }


        if (ListUtil.isNotEmpty(lstPrmRun)) {
            Map condition = new HashMap<>();
            condition.put(FinancialSubjectAttribute.SUBJECT_TYPE, "2");
            List<FinancialSubject> financialSubjects = pcm.findByAnyFields(FinancialSubject.class, condition, null);
            Map<String, List<PrmPurchasePlanDetail>> refIdMap = lstPurPrincipal.stream()
                    .filter(t -> StringUtil.isNotEmpty(t.getPurchasePackageId()))
                    .collect(Collectors.groupingBy(PrmPurchasePlanDetail::getPrmBudgetRefId));

            List<PrmPurchasePlanDetail> lstNonExist4 = lstPrmRun.stream().filter(x -> x.getAmount() != null && x
                    .getAmount().doubleValue() > 0 && lstPurRun.stream()
                    .noneMatch(y -> x.getUuid().equals(y
                            .getPrmBudgetRefId()))).map(x -> {
                PrmPurchasePlanDetail newDetail = new PrmPurchasePlanDetail();
                newDetail.setPrmBudgetRefId(x.getUuid());
                newDetail.setPrmBudgetType(PrmBudgetCodes.RUN);
                newDetail.setPrmProjectMainId(projectUuid);
                newDetail.setSerialNumber(x.getSerialNumber());
                newDetail.setRemark(x.getRemark());
                newDetail.setAmount(x.getAmount());
                newDetail.setBudgetPrice(x.getPrice());
                newDetail.setPurchaseBudgetMoney(x.getTotalValue());
                Map<String, List<FinancialSubject>> financialSubject = financialSubjects.stream().filter(z -> z.getSubjectNo().equals(x.getFinancialSubjectCode())).collect(Collectors.groupingBy(FinancialSubject::getSubjectNo));
                String name = financialSubject.get(x.getFinancialSubjectCode()).get(0).getSubjectName();
                newDetail.setName(name);
                newDetail.setUnit(x.getUnit());
                newDetail.setCompanyCode(prmProjectMain.getCompanyCode());
                newDetail.setDepartmentCode(prmProjectMain.getContractorOffice());
                return newDetail;
            }).collect(Collectors.toList());
            lstInsert.addAll(lstNonExist4);
            if (!PrmProjectMainAttribute.PRM_DETAIL_NEW.equals(mianC.getDetailType())) {
                lstNewRunInsert.addAll(lstNonExist4.stream().filter(t -> StringUtil.isEmpty(t.getPurchasePackageId())).collect(Collectors.toList()));
            }
        }


        Map<String, List<String>> listMap = lstExists.stream().filter(t -> StringUtil.isNotEmpty(t.getPurchasePackageId())).
                collect(Collectors.groupingBy(PrmPurchasePlanDetail::getPrmBudgetType,
                        Collectors.mapping(PrmPurchasePlanDetail::getPurchasePackageId, Collectors.toList())));

        boolean needOutsourcePachageCreate = ListUtil.isNotEmpty(lstNewOutsourceInsert);
        boolean needPrincipalPachageCreate = ListUtil.isNotEmpty(lstNewPrincipalInsert);
        boolean needAccessoryPachageCreate = ListUtil.isNotEmpty(lstNewAccessoryInsert);
        boolean needRunPachageCreate = ListUtil.isNotEmpty(lstNewRunInsert);
        String departmentCode = prmProjectMain.getContractorOffice();
        String purchasePlanState = prmProjectMain.getPurchasePlanState();
        //采购计划时已审核且新增明细(主材、辅材、外协)，则在采购计划中自动新增包
        if ("2".equals(purchasePlanState) && (needOutsourcePachageCreate || needPrincipalPachageCreate || needAccessoryPachageCreate)) {
            mapCondition.clear();
            mapCondition.put(PrmPurchasePackageAttribute.PRM_PROJECT_MAIN_ID, projectUuid);
            List<PrmPurchasePackage> lstExistedPackage = pcm.findByAnyFields(PrmPurchasePackage.class, mapCondition, null);
            int count = lstExistedPackage.size();
            if (needOutsourcePachageCreate) {
                long countO = 0;
                if (MapUtil.isNotEmpty(listMap) && listMap.containsKey(PrmBudgetCodes.OUTSOURCE)) {
                    countO = listMap.get(PrmBudgetCodes.OUTSOURCE).stream().distinct().count();
                }
                createPrmPackage(PrmBudgetCodes.OUTSOURCE, lstNewOutsourceInsert, projectUuid, "外协" + (countO + 1), count, departmentCode);
                count++;
            }

            if (needPrincipalPachageCreate) {
                PrmPurchasePlanDetail max = lstNewPrincipalInsert.get(0);
                BigDecimal value = BigDecimal.ZERO;
                for (PrmPurchasePlanDetail detail : lstNewPrincipalInsert) {
                    BigDecimal money = MathUtil.mul(detail.getBudgetPrice(), detail.getAmount());
                    if (value.compareTo(money) < 0) {
                        value = money;
                        max = detail;
                    }
                }
                String name = max.getName();
                if (StringUtil.isNotEmpty(name)) {
                    Map<String, List<String>> listName = lstExistedPackage.stream()
                            .filter(t -> StringUtil.isNotEmpty(t.getPackageName()))
                            .collect(Collectors.groupingBy(PrmPurchasePackage::getPackageName,
                                    Collectors.mapping(PrmPurchasePackage::getUuid, Collectors.toList())));
                    if (MapUtil.isNotEmpty(listName) && listName.containsKey(name)) {
                        name = name + (listName.get(name).size() + 1);
                    }
                } else {
                    long countP = 0;
                    if (MapUtil.isNotEmpty(listMap) && listMap.containsKey(PrmBudgetCodes.PRINCIPAL)) {
                        countP = listMap.get(PrmBudgetCodes.PRINCIPAL).stream().distinct().count();
                    }
                    name = "主材" + (countP + 1);
                }
                createPrmPackage(PrmBudgetCodes.PRINCIPAL, lstNewPrincipalInsert, projectUuid, name, count, departmentCode);
                count++;
            }
            if (needAccessoryPachageCreate) {
                long countA = 0;
                if (MapUtil.isNotEmpty(listMap) && listMap.containsKey(PrmBudgetCodes.ACCESSORY)) {
                    countA = listMap.get(PrmBudgetCodes.ACCESSORY).stream().distinct().count();
                }
                createPrmPackage(PrmBudgetCodes.ACCESSORY, lstNewAccessoryInsert, projectUuid, "辅材" + (countA + 1), count, departmentCode);
                count++;
            }
        }

        if (ListUtil.isNotEmpty(lstInsert)) {
            int maxSeqNo = lstExists.stream().filter(x -> x.getSeqNo() != null).mapToInt(x -> x.getSeqNo()).max().orElse(0);
            for (PrmPurchasePlanDetail detail : lstInsert) {
                detail.setSeqNo(++maxSeqNo);
            }
            pcm.batchInsert(lstInsert);
        }

        //采购计划，如果老项目没有同步运行费，则同步运行费明细，并且新增两个包
        if ("2".equals(purchasePlanState) && needRunPachageCreate) {
            Map<String, String> fadInvoiceInfo = new HashMap<String, String>();
            fadInvoiceInfo.putAll(purchaseplanManager.getInvoiceInfoByMainId(projectUuid));
            List<String> hadReimb = new ArrayList<String>();
            if (fadInvoiceInfo.containsKey("ENGINEERING_INSURANCE")) {
                hadReimb.add("ENGINEERING_INSURANCE");
            }

            if (fadInvoiceInfo.containsKey("HOUSE_RENT")) {
                hadReimb.add("HOUSE_RENT");
            }

            if (fadInvoiceInfo.containsKey("HIRE_CAR")) {
                hadReimb.add("HIRE_CAR");
            }
            if (ListUtil.isEmpty(hadReimb)) {
                purchaseplanManager.partPackageRun(projectUuid, "reverse");
            } else {
                purchaseplanManager.partPackageRun(projectUuid, hadReimb);
            }
        }
    }

    private void createPrmPackage(String type, List<PrmPurchasePlanDetail> lst, String mainUuid, String packageName, int packageCount, String departmentCode) {
        if (ListUtil.isNotEmpty(lst) && StringUtil.isNotEmpty(mainUuid)) {
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            Double pachageBudget = lst.stream().filter(t -> t.getPurchaseBudgetMoney() != null).mapToDouble(t -> t.getPurchaseBudgetMoney().doubleValue()).sum();
            PrmPurchasePackage prmPurchasePackage = new PrmPurchasePackage();
            prmPurchasePackage.setPackageState("2");
            prmPurchasePackage.setPackageNo(packageCount + 1);
            prmPurchasePackage.setPackageName(packageName);
            prmPurchasePackage.setDescription("项目变更自动添加");
            BigDecimal budget = BigDecimal.valueOf(pachageBudget);
            prmPurchasePackage.setPackageBudgetMoney(budget);
            if (type.equals(PrmBudgetCodes.PRINCIPAL)) {
                prmPurchasePackage.setPurchaseLevel("1");
                prmPurchasePackage.setMaterialClassCode("0-001");
            } else if (type.equals(PrmBudgetCodes.OUTSOURCE)) {
                prmPurchasePackage.setPurchaseLevel("1");
                prmPurchasePackage.setMaterialClassCode("0-001");
            } else {
                prmPurchasePackage.setPurchaseLevel("2");
                prmPurchasePackage.setMaterialClassCode("0-003");
            }
            prmPurchasePackage.setSubjectProperty("2");
            prmPurchasePackage.setPrmProjectMainId(mainUuid);
            prmPurchasePackage.setPendingMoney(budget);
            prmPurchasePackage.setDepartmentCode(departmentCode);
            Object packageUuid = pcm.insert(prmPurchasePackage);
            for (PrmPurchasePlanDetail detail : lst) {
                detail.setPurchasePackageId((String) packageUuid);
                detail.setSubPackageNo(BigDecimal.valueOf(prmPurchasePackage.getPackageNo()));
                detail.setPurchaseLevel(prmPurchasePackage.getPurchaseLevel());
                detail.setSubjectProperty(prmPurchasePackage.getSubjectProperty());
            }
        }
    }

    public List<PrmBudgetOutsourceC> reloadOutsourceFromPurchasePlan(String projectUuid, List<Map<String, Object>>
            lstPrmBudget) {
        List<PrmBudgetOutsourceC> lstReturn = new ArrayList<>();
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map mapCondition = new HashMap();
        mapCondition.put(PrmBudgetDetailAttribute.PRM_PROJECT_MAIN_ID, projectUuid);
        List<PrmPurchasePlanDetail> lstExists = pcm.findByAnyFields(PrmPurchasePlanDetail.class, mapCondition, null);
        if (ListUtil.isNotEmpty(lstExists)) {
            List<PrmPurchasePlanDetail> lstPurDetail = lstExists.stream().filter(x -> PrmBudgetCodes.OUTSOURCE.equals(x
                    .getPrmBudgetType()) && x.getBudgetPrice() != null).collect(Collectors.toList());
            lstReturn = lstPrmBudget.stream().map(x -> {
                List<PrmPurchasePlanDetail> lstDetail = lstPurDetail.stream().filter(y -> x.get
                        (PrmProjectMainCAttribute.LAST_UUID)
                        .equals(y.getPrmBudgetRefId())).collect(Collectors.toList());
                if (ListUtil.isNotEmpty(lstDetail)) {
                    PrmBudgetOutsourceC budgetDetail = new PrmBudgetOutsourceC();
                    budgetDetail.setLastUuid((String) x.get(PrmProjectMainCAttribute.LAST_UUID));
                    double totalAmount = lstDetail.stream().mapToDouble(z -> z.getPurchaseBudgetMoney().doubleValue()).sum();
                    budgetDetail.setTotalValue(BigDecimal.valueOf(totalAmount));
                    BigDecimal amount = new BigDecimal(String.valueOf(x.get(PrmBudgetOutsourceCAttribute.AMOUNT)));
                    if (amount.doubleValue() != 0) {
                        BigDecimal price = budgetDetail.getTotalValue().divide(amount, 2, BigDecimal.ROUND_HALF_UP);
                        budgetDetail.setPrice(price);
                    }
                    return budgetDetail;
                } else {
                    return null;
                }
            }).filter(x -> x != null).collect(Collectors.toList());
        }
        return lstReturn;
    }

    public List<PrmBudgetPrincipalC> reloadPrincipalFromPurchasePlan(String projectUuid, List<Map<String, Object>>
            lstPrmBudget) {
        List<PrmBudgetPrincipalC> lstReturn = new ArrayList<>();
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map mapCondition = new HashMap();
        mapCondition.put(PrmBudgetDetailAttribute.PRM_PROJECT_MAIN_ID, projectUuid);
        List<PrmPurchasePlanDetail> lstExists = pcm.findByAnyFields(PrmPurchasePlanDetail.class, mapCondition, null);
        if (ListUtil.isNotEmpty(lstExists)) {
            List<PrmPurchasePlanDetail> lstPurDetail = lstExists.stream().filter(x -> PrmBudgetCodes.PRINCIPAL.equals(x
                    .getPrmBudgetType()) && x.getBudgetPrice() != null).collect(Collectors.toList());
            lstReturn = lstPrmBudget.stream().map(x -> {
                List<PrmPurchasePlanDetail> lstDetail = lstPurDetail.stream().filter(y -> x.get
                        (PrmProjectMainCAttribute.LAST_UUID)
                        .equals(y.getPrmBudgetRefId())).collect(Collectors.toList());
                if (ListUtil.isNotEmpty(lstDetail)) {
                    PrmBudgetPrincipalC budgetDetail = new PrmBudgetPrincipalC();
                    budgetDetail.setLastUuid((String) x.get(PrmProjectMainCAttribute.LAST_UUID));
                    double totalAmount = lstDetail.stream().mapToDouble(z -> z.getPurchaseBudgetMoney().doubleValue()).sum();
                    budgetDetail.setSchemingTotalValue(BigDecimal.valueOf(totalAmount));
                    BigDecimal amount = new BigDecimal(String.valueOf(x.get(PrmBudgetOutsourceCAttribute.AMOUNT)));
                    if (amount.doubleValue() != 0) {
                        BigDecimal price = budgetDetail.getSchemingTotalValue().divide(amount, 2, BigDecimal.ROUND_HALF_UP);
                        budgetDetail.setSchemingPrice(price);
                    }
                    return budgetDetail;
                } else {
                    return null;
                }
            }).filter(x -> x != null).collect(Collectors.toList());
        }
        return lstReturn;
    }

    public List<PrmBudgetAccessoryC> reloadAccessoryFromPurchasePlan(String projectUuid, List<Map<String, Object>>
            lstPrmBudget) {
        List<PrmBudgetAccessoryC> lstReturn = new ArrayList<PrmBudgetAccessoryC>();
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map mapCondition = new HashMap();
        mapCondition.put(PrmBudgetDetailAttribute.PRM_PROJECT_MAIN_ID, projectUuid);
        List<PrmPurchasePlanDetail> lstExists = pcm.findByAnyFields(PrmPurchasePlanDetail.class, mapCondition, null);
        if (ListUtil.isNotEmpty(lstExists)) {
            List<PrmPurchasePlanDetail> lstPurDetail = lstExists.stream().filter(x -> PrmBudgetCodes.ACCESSORY.equals(x
                    .getPrmBudgetType()) && x.getBudgetPrice() != null).collect(Collectors.toList());
            lstReturn = lstPrmBudget.stream().map(x -> {
                List<PrmPurchasePlanDetail> lstDetail = lstPurDetail.stream().filter(y -> x.get
                        (PrmProjectMainCAttribute.LAST_UUID)
                        .equals(y.getPrmBudgetRefId())).collect(Collectors.toList());
                if (ListUtil.isNotEmpty(lstDetail)) {
                    PrmBudgetAccessoryC budgetDetail = new PrmBudgetAccessoryC();
                    budgetDetail.setLastUuid((String) x.get(PrmProjectMainCAttribute.LAST_UUID));
                    double totalAmount = lstDetail.stream().mapToDouble(z -> z.getPurchaseBudgetMoney().doubleValue()).sum();
                    budgetDetail.setTotalValue(BigDecimal.valueOf(totalAmount));
                    BigDecimal amount = new BigDecimal(String.valueOf(x.get(PrmBudgetOutsourceCAttribute.AMOUNT)));
                    if (amount.doubleValue() != 0) {
                        BigDecimal price = budgetDetail.getTotalValue().divide(amount, 2, BigDecimal.ROUND_HALF_UP);
                        budgetDetail.setPrice(price);
                    }
                    return budgetDetail;
                } else {
                    return null;
                }
            }).filter(x -> x != null).collect(Collectors.toList());
        }
        return lstReturn;
    }

    @Override
    public boolean validateProjectName(PrmProjectMainC prmProjectMainC) {
        String uuid = prmProjectMainC.getUuid();
        String prmProjectMainId = prmProjectMainC.getPrmProjectMainId();
        String projectName = prmProjectMainC.getProjectName();
        String projectShortName = prmProjectMainC.getProjectShortName();
        List lstParam = new ArrayList();
        lstParam.add(projectName);
        lstParam.add(projectShortName);
        lstParam.add(projectName);
        lstParam.add(projectShortName);
        lstParam.add(projectName);
        lstParam.add(projectShortName);
        DAOMeta daoMeta = DAOHelper.getDAO("prmprojectmainc-dao", "validate_project_name", lstParam);
        daoMeta.setNeedFilter(false);
        List<Map<String, Object>> lstProjectMainC = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstProjectMainC.stream().noneMatch(x -> {
            String _uuid = (String) x.get(CommonAttribute.UUID);
            String _prmProjectMainId = (String) x.get(PrmProjectMainCAttribute.PRM_PROJECT_MAIN_ID);
            String _projectName = (String) x.get(PrmProjectMainCAttribute.PROJECT_NAME);
            String _projectShortName = (String) x.get(PrmProjectMainCAttribute.PROJECT_SHORT_NAME);
            if ((StringUtil.isEmpty(uuid) || (StringUtil.isNotEmpty(uuid) && !uuid.equals(_uuid)))
                    && (StringUtil.isEmpty(prmProjectMainId) || (StringUtil.isNotEmpty(prmProjectMainId) && !prmProjectMainId.equals
                    (_prmProjectMainId)))) {
                if ((StringUtil.isNotEmpty(projectName) && projectName.equals(_projectName))
                        || (StringUtil.isNotEmpty(projectShortName) && projectShortName.equals(_projectShortName))) {
                    return true;
                }
            }
            return false;
        });
    }

    private void removeProjectFilterCache(String mainChangeUuid, String mainUuid) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map mapCondition = new HashMap();
        List<PrmMemberDetailP> lstP = new ArrayList<>();
        mapCondition.put(PrmAssociatedUnitsAttribute.PRM_PROJECT_MAIN_ID, mainUuid);
        lstP = pcm.findByAnyFields(PrmMemberDetailP.class, mapCondition, null);
        if (ListUtil.isEmpty(lstP)) {
            lstP = new ArrayList<>();
        }

        mapCondition.clear();
        mapCondition.put(PrmAssociatedUnitsCAttribute.PRM_PROJECT_MAIN_C_ID, mainChangeUuid);
        List<PrmMemberDetailPC> lstPC = pcm.findByAnyFields(PrmMemberDetailPC.class, mapCondition, null);
        if (ListUtil.isEmpty(lstPC)) {
            lstPC = new ArrayList();
        }

        List<PrmMemberDetailP> chkLstP = ListUtil.isEmpty(lstP) ? new ArrayList<>() : lstP;
        List<PrmMemberDetailPC> chkLstPC = ListUtil.isEmpty(lstPC) ? new ArrayList<>() : lstPC;

        Set<String> userIdSet = new HashSet<String>();

        for (PrmMemberDetailP dp : chkLstP) {
            boolean extFlag = false;
            for (PrmMemberDetailPC dpc : chkLstPC) {
                if (dp.getStaffId().equals(dpc.getStaffId()) && dp.getPost().equals(dpc.getPost())) {
                    extFlag = true;
                    break;
                }
            }
            if (!extFlag) {
                userIdSet.add(dp.getStaffId());
            }
        }

        for (PrmMemberDetailPC dpc : chkLstPC) {
            boolean extFlag = false;
            for (PrmMemberDetailP dp : chkLstP) {
                if (dp.getStaffId().equals(dpc.getStaffId()) && dp.getPost().equals(dpc.getPost())) {
                    extFlag = true;
                    break;
                }
            }
            if (!extFlag) {
                userIdSet.add(dpc.getStaffId());
            }
        }

        for (String userId : userIdSet) {
            CacheHelper.remove("ERP_PROJECT_FILTER", userId);
        }
    }

    public String getProjectManagerRoleUuid() {
        String sql = "select uuid from scdp_role sr where (sr.company_uuid = ? or ? = '*') and role_name=? ";
        List lstParam = new ArrayList<>();
        lstParam.add(UserHelper.getCompanyUuid());
        lstParam.add(UserHelper.getCompanyUuid());
        lstParam.add("项目经理");
        DAOMeta daoMeta = new DAOMeta(sql, lstParam, false);
        List<Map<String, Object>> lstRole = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return Optional.ofNullable(lstRole).map(x -> (String) x.get(0).get(com.csnt.scdp.framework.attributes
                .CommonAttribute.UUID))
                .orElse(null);
    }

    @Override
    public List<String> validateSerialNumber(PrmProjectMainCDto prmProjectMainC) {

        PurchaseplanManager purchaseplanManager = BeanFactory.getBean("purchaseplan-manager");
        List<String> lstMessage = new ArrayList<String>();
        Map mapCondition = new HashMap();
        mapCondition.put(PrmPurchasePlanDetailAttribute.PRM_PROJECT_MAIN_ID, prmProjectMainC
                .getPrmProjectMainId());
        List<PrmPurchasePlanDetail> lstPurchasePlanDetail = PersistenceFactory.getInstance().findByAnyFields
                (PrmPurchasePlanDetail.class, mapCondition, null);

        if (ListUtil.isNotEmpty(prmProjectMainC.getPrmBudgetOutsourceCDto())) {
            lstMessage.addAll(validateSerialNumber(prmProjectMainC.getPrmBudgetOutsourceCDto(), lstPurchasePlanDetail,
                    PrmBudgetCodes.OUTSOURCE));
        }
        if (ListUtil.isNotEmpty(prmProjectMainC.getPrmBudgetPrincipalCDto())) {
            lstMessage.addAll(validateSerialNumber(prmProjectMainC.getPrmBudgetPrincipalCDto(), lstPurchasePlanDetail,
                    PrmBudgetCodes.PRINCIPAL));
        }
        if (ListUtil.isNotEmpty(prmProjectMainC.getPrmBudgetAccessoryCDto())) {
            lstMessage.addAll(validateSerialNumber(prmProjectMainC.getPrmBudgetAccessoryCDto(), lstPurchasePlanDetail,
                    PrmBudgetCodes.ACCESSORY));
        }
        return lstMessage;
    }

    private List<String> validateSerialNumber(List lstBasePojo, List<PrmPurchasePlanDetail>
            lstPurchasePlanDetail, String budgetType) {
        List<String> lstMessage = new ArrayList<>();
        List<String> lstInsertSerialNo = new ArrayList<>();
        Map<String, String> mapModifiedSerialNo = new HashMap<String, String>();
        lstBasePojo.forEach(x -> {
            String serialNo = (String) BeanUtil.getProperty(x, PrmBudgetPrincipalAttribute.SERIAL_NUMBER);
            String lastUuid = (String) BeanUtil.getProperty(x, PrmProjectMainCAttribute.LAST_UUID);
            String editFlag = (String) BeanUtil.getProperty(x, com.csnt.scdp.framework.attributes.CommonAttribute.EDITFLAG);
            if (StringUtil.isNotEmpty(serialNo)) {
                if (StringUtil.isEmpty(lastUuid)) {
                    lstInsertSerialNo.add(serialNo);
                } else if (isModifiedFlag(editFlag)) {
                    mapModifiedSerialNo.put(lastUuid, serialNo);
                }
            }
        });

        List<String> lstDuplicated = new ArrayList<>();
        List<String> lstMissing = new ArrayList<>();

        lstPurchasePlanDetail.stream().filter(x -> budgetType.equals(x.getPrmBudgetType())).forEach(x -> {
            String serialNumber = x.getSerialNumber();
            String budgetUuid = x.getPrmBudgetRefId();
            if (lstInsertSerialNo.contains(serialNumber)
                    || (mapModifiedSerialNo.containsValue(serialNumber)
                    && !mapModifiedSerialNo.containsKey(budgetUuid))) {
                lstDuplicated.add(serialNumber);
            }
        });
        mapModifiedSerialNo.keySet().stream().forEach(x -> {
            List<String> purchaseNo = lstPurchasePlanDetail.stream()
                    .filter(y -> budgetType.equals(y.getPrmBudgetType())
                            && x.equals(y.getPrmBudgetRefId())
                            && StringUtil.isNotEmpty(y.getSerialNumber()))
                    .map(y -> y.getSerialNumber()).collect(Collectors.toList());
            if (ListUtil.isNotEmpty(purchaseNo) && !purchaseNo.contains(mapModifiedSerialNo.get(x))) {
                lstMissing.add(mapModifiedSerialNo.get(x));
            }
        });

        String budgetTypeDesc = FMCodeHelper.getDescByCode(budgetType, "PRM_BUDGET_TYPE");
        if (ListUtil.isNotEmpty(lstDuplicated)) {
            lstMessage.add(budgetTypeDesc + "的序号已经被其它主材拆分使用：" + StringUtil.joinList(lstDuplicated, ", "));
        }
        if (ListUtil.isNotEmpty(lstMissing)) {
            lstMessage.add(budgetTypeDesc + "的序号在对应的采购计划明细中找不到：" + StringUtil.joinList(lstMissing, ", "));
        }
        return lstMessage;
    }

    @Override
    public List<String> validateSplitItemTotalValue(PrmProjectMainCDto prmProjectMainC) {
        List<String> lstMessage = new ArrayList<String>();
        fillBudgetTotalValue(prmProjectMainC);
        if (ListUtil.isNotEmpty(prmProjectMainC.getPrmBudgetOutsourceCDto())) {
            Map<String, Double> rtnMap = prmProjectMainC.getPrmBudgetOutsourceCDto().stream()
                    .filter(t -> StringUtil.isNotEmpty(t.getSplitFromUuid()))
                    .collect(Collectors.groupingBy(PrmBudgetOutsourceCDto::getSplitFromUuid,
                            Collectors.summingDouble(t -> t.getTotalValue().doubleValue())));
            if (MapUtil.isNotEmpty(rtnMap)) {
                prmProjectMainC.getPrmBudgetOutsourceCDto().stream()
                        .filter(t -> StringUtil.isNotEmpty(t.getLastUuid()))
                        .forEach(t -> {
                            if (rtnMap.containsKey(t.getUuid())) {
                                BigDecimal total = t.getTotalValue() == null ? BigDecimal.ZERO : t.getTotalValue();
                                if (MathUtil.round(t.getPreTotalValue().subtract(total).doubleValue(), 2) < MathUtil.round(rtnMap.get(t.getUuid()), 2)) {
                                    lstMessage.add("外协项目：序号为" + t.getSerialNumber() + "，名称为：" + t.getSupplierCode() + "，原始预算为" + t.getPreTotalValue().doubleValue() +
                                            "，小于拆分项目！" + MathUtil.add(rtnMap.get(t.getUuid()), total.doubleValue()) + "\n");
                                }
                            }
                        });
            }
        }
        if (ListUtil.isNotEmpty(prmProjectMainC.getPrmBudgetPrincipalCDto())) {
            Map<String, Double> rtnMap = prmProjectMainC.getPrmBudgetPrincipalCDto().stream()
                    .filter(t -> StringUtil.isNotEmpty(t.getSplitFromUuid()))
                    .collect(Collectors.groupingBy(PrmBudgetPrincipalCDto::getSplitFromUuid,
                            Collectors.summingDouble(t -> t.getSchemingTotalValue().doubleValue())));
            if (MapUtil.isNotEmpty(rtnMap)) {
                prmProjectMainC.getPrmBudgetPrincipalCDto().stream()
                        .filter(t -> StringUtil.isNotEmpty(t.getLastUuid()))
                        .forEach(t -> {
                            if (rtnMap.containsKey(t.getUuid())) {
                                BigDecimal total = t.getSchemingTotalValue() == null ? BigDecimal.ZERO : t.getSchemingTotalValue();
                                if (MathUtil.round(t.getPreTotalValue().subtract(total).doubleValue(), 2) < MathUtil.round(rtnMap.get(t.getUuid()), 2)) {
                                    lstMessage.add("主材项目：序号为" + t.getSerialNumber() + "，名称为：" + t.getEquipmentName() + "，原始预算为" + t.getPreTotalValue().doubleValue() +
                                            "，小于拆分项目！" + MathUtil.add(rtnMap.get(t.getUuid()), total.doubleValue()) + "\n");
                                }
                            }
                        });
            }
        }
        if (ListUtil.isNotEmpty(prmProjectMainC.getPrmBudgetAccessoryCDto())) {
            Map<String, Double> rtnMap = prmProjectMainC.getPrmBudgetAccessoryCDto().stream()
                    .filter(t -> StringUtil.isNotEmpty(t.getSplitFromUuid()))
                    .collect(Collectors.groupingBy(PrmBudgetAccessoryCDto::getSplitFromUuid,
                            Collectors.summingDouble(t -> t.getTotalValue().doubleValue())));
            if (MapUtil.isNotEmpty(rtnMap)) {
                prmProjectMainC.getPrmBudgetAccessoryCDto().stream()
                        .filter(t -> StringUtil.isNotEmpty(t.getLastUuid()))
                        .forEach(t -> {
                            if (rtnMap.containsKey(t.getUuid())) {
                                BigDecimal total = t.getTotalValue() == null ? BigDecimal.ZERO : t.getTotalValue();
                                if (MathUtil.round(t.getPreTotalValue().subtract(total).doubleValue(), 2) < MathUtil.round(rtnMap.get(t.getUuid()), 2)) {
                                    lstMessage.add("辅材项目：序号为" + t.getSerialNumber() + ",名称为：" + t.getAccessoryName() + "，原始预算为" + t.getPreTotalValue().doubleValue() +
                                            "，小于拆分项目！" + MathUtil.add(rtnMap.get(t.getUuid()), total.doubleValue()) + "\n");
                                }
                            }
                        });
            }
        }
        return lstMessage;
    }

    private boolean isModifiedFlag(String editflag) {
        return !"-".equals(editflag);
    }

    //项目生产代号时发送消息给出纳
    @Override
    public void sendMsg(String projectCode, String projectName, String contractorOffice, String step) {
        String sql = "SELECT * FROM V_USER_ROLES T WHERE ROLE_NAME ='出纳'";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List<Map<String, Object>> lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        List<String> userIdLst = new ArrayList<String>();
        ReceiptsMeta receiptsMeta = new ReceiptsMeta();
        if (ListUtil.isNotEmpty(lstUserInfo)) {
            for (int i = 0; i < lstUserInfo.size(); i++) {
                String userId = (String) lstUserInfo.get(i).get("userId");
                userIdLst.add(userId);
            }
            receiptsMeta.setLstSendToUserId(userIdLst);
        }
        String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
        String templateNo = null;
        if ("1".equals(step)) {
            templateNo = "PRM_PROJECT_MAIN_GENERATE";
        } else if ("2".equals(step)) {
            templateNo = "PRM_PROJECT_MAIN_UPDATE";
        }
        Map map = new HashMap<>();
        map.put("projectCode", projectCode);
        map.put("contractorOffice", contractorOffice);
        map.put("projectName", projectName);
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);

    }

    //重大项目发送消息
    @Override
    public void sendMsg(String uuid, String projectName, String type) {
        //发送消息
        String sql = "SELECT * FROM V_USER_ROLES T WHERE ROLE_NAME IN('供应链部主任','供应链部采购计划主管')";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List<Map<String, Object>> lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        List<String> userIdLst = new ArrayList<>();
        ReceiptsMeta receiptsMeta = new ReceiptsMeta();
        if (ListUtil.isNotEmpty(lstUserInfo)) {
            for (int i = 0; i < lstUserInfo.size(); i++) {
                String userId = (String) lstUserInfo.get(i).get("userId");
                userIdLst.add(userId);
            }
            receiptsMeta.setLstSendToUserId(userIdLst);
        }

        Map map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("projectName", projectName);
        String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
        String templateNo = "PRM_PROJECT_MAIN_MAJOR";
        ;
        if (type.equals("add")) {
            map.put("menu", "PROJECT_MAIN_CHANGE_NEW");
        } else if (type.equals("reverse")) {
            map.put("menu", "PROJECT_MAIN_CHANGE");
        }
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);

    }

    public String getPrmChangeTaxType(String prmProjectMainCId) {
        if (StringUtil.isEmpty(prmProjectMainCId)) {
            return ContractTaxType.BUSINESS_TAX;
        }
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        PrmProjectMainC projectMainC = pcm.findByPK(PrmProjectMainC.class, prmProjectMainCId);
        if (projectMainC == null) {
            return ContractTaxType.BUSINESS_TAX;
        }
        return getPrmChangeTaxType(projectMainC);
    }

    @Override
    public void handlePrmCodeType(PrmProjectMainCDto prmProjectMainCDto) {
        String uuid = prmProjectMainCDto.getUuid();
        PrmProjectMainC prmProjectMainC = PersistenceFactory.getInstance().findByPK(PrmProjectMainC.class, uuid);
        // 是否需要判断只有营改增的项目才需要发消息
        if (!ObjectUtil.beSame(prmProjectMainCDto.getPrmCodeType(), prmProjectMainC.getPrmCodeType())) {
            //判断是不是运管部生产主管，然后发消息
            List<String> lstRoleName = ErpUserHelper.getUserRoleInfo(UserHelper.getUserId());
            if (ListUtil.isNotEmpty(lstRoleName) && lstRoleName.contains("运管部生产主管")) {
                Map root = BeanUtil.bean2Map(prmProjectMainCDto);
                root.put("oldPrmCodeTypeDesc", FMCodeHelper.getDescByCode(prmProjectMainC.getPrmCodeType(), "PRM_CODE_TYPE_PP"));
                root.put("prmCodeTypeDesc", FMCodeHelper.getDescByCode(prmProjectMainCDto.getPrmCodeType(), "PRM_CODE_TYPE_PP"));

                ReceiptsMeta meta = new ReceiptsMeta();
                List<String> lstSendToUserId = ErpUserHelper.getUserIdsByRoleName("计财部主任");
                lstSendToUserId.addAll(ErpUserHelper.getUserIdsByRoleName("计财部分管副主任"));
                // 计财部分管副主任 逻辑
                meta.setLstSendToUserId(lstSendToUserId);
                MsgSendHelper.sendMsg(root, ScdpMsgAttribute.MSG_TYPE_IMSG, "PRM_MODIFY_PRM_CODE_TYPE", meta);
            }
        }
    }

    public String getPrmChangeTaxType(PrmProjectMainC projectMainC) {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        String contractId = projectMainC.getPrmContractId();
        Date compareDate = DateUtil.parseDate("2016-05-01", "yyyy-MM-dd");
        if (StringUtil.isEmpty(contractId)) {
            Date establishDate = projectMainC.getEstablishDate();
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
    public boolean isProjectMainChangeDepartInner(String prmProjectMainCId) {
        boolean rtn = true;
        PrmProjectMainCDto prmProjectMainCDto = (PrmProjectMainCDto) DtoHelper.findDtoByPK(PrmProjectMainCDto.class, prmProjectMainCId);
        if (StringUtil.isEmpty(prmProjectMainCDto.getPrmProjectMainId())) {
            rtn = false;
        }
        if (rtn) {
            PrmProjectMainDto prmProjectMainDto = (PrmProjectMainDto) DtoHelper.findDtoByPK(PrmProjectMainDto.class, prmProjectMainCDto.getPrmProjectMainId());
            //先校验主数据是否发生变化
            rtn = !checkRecordsHeadChanged(prmProjectMainCDto, prmProjectMainDto);
            //所有表格是否存在新增（除拆分）的数据
            rtn = (rtn) && (!checkRecordsDtoQuantityChanged(prmProjectMainCDto, prmProjectMainDto));
            //主材、辅材、外协是否存在数量或金额变大的情况
            rtn = (rtn) &&
                    (!checkRecordsDtoFieldLarged(prmProjectMainCDto.getPrmBudgetOutsourceCDto(), prmProjectMainDto.getPrmBudgetOutsourceDto(),
                            BUDGET_OUTSOURCE_QUANTITY_FIELDS, LAST_UUID, UUID));
            rtn = (rtn) &&
                    (!checkRecordsDtoFieldLarged(prmProjectMainCDto.getPrmBudgetPrincipalCDto(), prmProjectMainDto.getPrmBudgetPrincipalDto(),
                            BUDGET_PRINCIPAL_QUANTITY_FIELDS, LAST_UUID, UUID));
            rtn = (rtn) &&
                    (!checkRecordsDtoFieldLarged(prmProjectMainCDto.getPrmBudgetAccessoryCDto(), prmProjectMainDto.getPrmBudgetAccessoryDto(),
                            BUDGET_ACCESSORY_QUANTITY_FIELDS, LAST_UUID, UUID));
            //是否存在变动的数据（除了主材、辅材、外协)
            rtn = (rtn) && (!checkRecordsDtoContentChanged(prmProjectMainCDto, prmProjectMainDto));

        }
        return rtn;
    }

    private boolean checkRecordsHeadChanged(PrmProjectMainCDto prmProjectMainCDto, PrmProjectMainDto prmProjectMainDto) {
        boolean rtn = false;

        String[] HEADER_IGNORE_FIELDS = new String[]{
                PrmProjectMainCAttribute.STATE,
                PrmProjectMainCAttribute.DETAIL_TYPE,
                PrmProjectMainCAttribute.PRM_PROJECT_MAIN_ID,
                PrmProjectMainCAttribute.DEPARTMENT_CODE,
                PrmProjectMainCAttribute.AUDIT_TIME,
                com.csnt.scdp.framework.attributes.CommonAttribute.UUID,
                "class",
                "iuuid",
                com.csnt.scdp.framework.attributes.CommonAttribute.TBL_VERSION,
                com.csnt.scdp.framework.attributes.CommonAttribute.CREATE_BY,
                com.csnt.scdp.framework.attributes.CommonAttribute.CREATE_TIME,
                com.csnt.scdp.framework.attributes.CommonAttribute.UPDATE_BY,
                com.csnt.scdp.framework.attributes.CommonAttribute.UPDATE_TIME,
                PrmProjectMainCAttribute.REVISE_REASON
        };
        String[] NOT_ALLOW_LARGEN = new String[]{
                PrmProjectMainCAttribute.COST_CONTROL_MONEY,
        };

        PrmProjectMainC currPojo = new PrmProjectMainC();
        BeanUtil.bean2Bean(prmProjectMainCDto, currPojo);
        PrmProjectMain oldPojo = new PrmProjectMain();
        BeanUtil.bean2Bean(prmProjectMainDto, oldPojo);

        Map<String, Object> currHeaderMap = BeanUtil.bean2Map(currPojo);
        Map<String, Object> oldHeaderMap = BeanUtil.bean2Map(oldPojo);

        Set<String> keySet = currHeaderMap.keySet();
        for (String key : keySet) {
            if (!ObjectUtil.beSame(currHeaderMap.get(key), oldHeaderMap.get(key))) {
                if (!ArrayUtil.contains(HEADER_IGNORE_FIELDS, key)) {
                    if (ArrayUtil.contains(NOT_ALLOW_LARGEN, key)) {
                        BigDecimal curr = currHeaderMap.get(key) == null ? BigDecimal.ZERO : (BigDecimal) currHeaderMap.get(key);
                        BigDecimal old = oldHeaderMap.get(key) == null ? BigDecimal.ZERO : (BigDecimal) oldHeaderMap.get(key);
                        if (curr.compareTo(old) > 0) {
                            rtn = true;
                            break;
                        }
                    } else {
                        rtn = true;
                        break;
                    }
                }
            }
        }
        return rtn;
    }

    private boolean checkRecordsDtoQuantityChanged(PrmProjectMainCDto prmProjectMainCDto, PrmProjectMainDto prmProjectMainDto) {
        boolean isAnyChanged = false;
        //合同发生变化
        if (!isAnyChanged) {
            isAnyChanged = isQuantityChanged(prmProjectMainCDto.getPrmContractDetailCDto(), prmProjectMainDto.getPrmContractDetailDto());
            if (!isAnyChanged && ListUtil.isNotEmpty(prmProjectMainCDto.getPrmContractDetailCDto())) {
                for (PrmContractDetailCDto cdto : prmProjectMainCDto.getPrmContractDetailCDto()) {
                    if (!prmProjectMainDto.getPrmContractDetailDto().stream().anyMatch(t -> cdto.getPrmContractId().equals(t.getPrmContractId()))) {
                        isAnyChanged = true;
                        break;
                    }
                }
            }
        }
        //主材非拆分变化
        if ((!isAnyChanged) && (prmProjectMainCDto.getPrmBudgetPrincipalCDto() != null)
                && prmProjectMainCDto.getPrmBudgetPrincipalCDto().stream().anyMatch(t -> StringUtil.isEmpty(t.getLastUuid()) && StringUtil.isEmpty(t.getSplitFromUuid()))) {
            isAnyChanged = true;
        }

        //外协非拆分变化
        if ((!isAnyChanged) && (prmProjectMainCDto.getPrmBudgetOutsourceCDto() != null)
                && prmProjectMainCDto.getPrmBudgetOutsourceCDto().stream().anyMatch(t -> StringUtil.isEmpty(t.getLastUuid()) && StringUtil.isEmpty(t.getSplitFromUuid()))) {
            isAnyChanged = true;
        }

        //辅材非拆分变化
        if ((!isAnyChanged) && (prmProjectMainCDto.getPrmBudgetAccessoryCDto() != null)
                && prmProjectMainCDto.getPrmBudgetAccessoryCDto().stream().anyMatch(t -> StringUtil.isEmpty(t.getLastUuid()) && StringUtil.isEmpty(t.getSplitFromUuid()))) {
            isAnyChanged = true;
        }

        //立项预算变更
        if ((!isAnyChanged) && (isQuantityChanged(prmProjectMainCDto.getPrmBudgetDetailCDto(), prmProjectMainDto.getPrmBudgetDetailDto()) ||
                (prmProjectMainCDto.getPrmBudgetDetailCDto() != null
                        && prmProjectMainCDto.getPrmBudgetDetailCDto().stream().anyMatch(t -> StringUtil.isEmpty(t.getLastUuid()))))) {
            isAnyChanged = true;
        }
        //运行成本预算变更
        if ((!isAnyChanged) && (isQuantityChanged(prmProjectMainCDto.getPrmBudgetDetailCDto(), prmProjectMainDto.getPrmBudgetDetailDto()) ||
                (prmProjectMainCDto.getPrmBudgetDetailCDto() != null
                        && prmProjectMainCDto.getPrmBudgetDetailCDto().stream().anyMatch(t -> StringUtil.isEmpty(t.getLastUuid()))))) {
            isAnyChanged = true;
        }

        //项目人员计划变更
        if ((!isAnyChanged) && (isQuantityChanged(prmProjectMainCDto.getPrmBudgetDetailCDto(), prmProjectMainDto.getPrmBudgetDetailDto()) ||
                (prmProjectMainCDto.getPrmMemberDetailPCDto() != null
                        && prmProjectMainCDto.getPrmMemberDetailPCDto().stream().anyMatch(t -> StringUtil.isEmpty(t.getLastUuid()))))) {
            isAnyChanged = true;
        }

        //收款计划变更
        if ((!isAnyChanged) && (isQuantityChanged(prmProjectMainCDto.getPrmReceiptsDetailPCDto(), prmProjectMainDto.getPrmReceiptsDetailPDto()) ||
                (prmProjectMainCDto.getPrmReceiptsDetailPCDto() != null
                        && prmProjectMainCDto.getPrmReceiptsDetailPCDto().stream().anyMatch(t -> StringUtil.isEmpty(t.getLastUuid()))))) {
            isAnyChanged = true;
        }

        //结算计划变更
        if ((!isAnyChanged) && (isQuantityChanged(prmProjectMainCDto.getPrmSquareDetailPCDto(), prmProjectMainDto.getPrmSquareDetailPDto()) ||
                (prmProjectMainCDto.getPrmSquareDetailPCDto() != null
                        && prmProjectMainCDto.getPrmSquareDetailPCDto().stream().anyMatch(t -> StringUtil.isEmpty(t.getLastUuid()))))) {
            isAnyChanged = true;
        }

        //进度计划变更
        if ((!isAnyChanged) && (isQuantityChanged(prmProjectMainCDto.getPrmProgressDetailPCDto(), prmProjectMainDto.getPrmProgressDetailPDto()) ||
                (prmProjectMainCDto.getPrmProgressDetailPCDto() != null
                        && prmProjectMainCDto.getPrmProgressDetailPCDto().stream().anyMatch(t -> StringUtil.isEmpty(t.getLastUuid()))))) {
            isAnyChanged = true;
        }
        //开支计划变更
        if ((!isAnyChanged) && (isQuantityChanged(prmProjectMainCDto.getPrmPayDetailPCDto(), prmProjectMainDto.getPrmPayDetailPDto()) ||
                (prmProjectMainCDto.getPrmPayDetailPCDto() != null
                        && prmProjectMainCDto.getPrmPayDetailPCDto().stream().anyMatch(t -> StringUtil.isEmpty(t.getLastUuid()))))) {
            isAnyChanged = true;
        }
        //关联单位变更
        if ((!isAnyChanged) && (isQuantityChanged(prmProjectMainCDto.getPrmAssociatedUnitsCDto(), prmProjectMainDto.getPrmAssociatedUnitsDto()) ||
                (prmProjectMainCDto.getPrmAssociatedUnitsCDto() != null
                        && prmProjectMainCDto.getPrmAssociatedUnitsCDto().stream().anyMatch(t -> StringUtil.isEmpty(t.getLastUuid()))))) {
            isAnyChanged = true;
        }

        //质量安全计划变更
        if ((!isAnyChanged) && (isQuantityChanged(prmProjectMainCDto.getPrmQsPCDto(), prmProjectMainDto.getPrmQsPDto()) ||
                (prmProjectMainCDto.getPrmQsPCDto() == null
                        && prmProjectMainCDto.getPrmQsPCDto().stream().anyMatch(t -> StringUtil.isEmpty(t.getLastUuid()))))) {
            isAnyChanged = true;
        }
        return isAnyChanged;
    }

    private boolean isQuantityChanged(List beChecked, List checked) {
        boolean rtn = false;
        rtn = rtn || ((ListUtil.isEmpty(beChecked) ^ ListUtil.isEmpty(checked)));
        if ((!rtn) && ListUtil.isNotEmpty(beChecked) && ListUtil.isNotEmpty(checked)) {
            rtn = (beChecked.size() != checked.size());
        }
        return rtn;
    }

    private boolean checkRecordsDtoFieldLarged(List lstCurrObj, List lstPreviousObj, String[] compareFields, String linkField1, String linkField2) {
        boolean result = false;
        if (lstCurrObj != null) {
            for (Object obj : lstCurrObj) {
                Object currLinkValue = BeanUtil.getProperty(obj, linkField1);
                Object source = lstPreviousObj.stream().filter(y -> {
                    Object previousLinkValue = BeanUtil.getProperty(y, linkField2);
                    return ObjectUtil.beSame(currLinkValue, previousLinkValue);
                }).findFirst().orElse(null);

                if (source != null) {
                    for (String field : compareFields) {
                        Object currObj = BeanUtil.getProperty(obj, field);
                        Object preObj = BeanUtil.getProperty(source, field);
                        try {
                            BigDecimal cb = new BigDecimal(currObj.toString());
                            BigDecimal pb = new BigDecimal(preObj.toString());
                            result = cb.compareTo(pb) > 0;
                        } catch (Exception e) {
                            result = true;
                            break;
                        }
                        if (result) {
                            break;
                        }
                    }
                    if (result) {
                        break;
                    }
                }
            }
        }
        return result;
    }


    private boolean checkRecordsDtoContentChanged(PrmProjectMainCDto prmProjectMainCDto, PrmProjectMainDto prmProjectMainDto) {
        boolean rtn = false;
        if ((!rtn) &&
                checkRecordsDtoContentChanged(prmProjectMainCDto.getPrmContractDetailCDto(),
                        prmProjectMainDto.getPrmContractDetailDto(), ProjectmainManager.CONTRACT_DETAIL_FIELD,
                        PrmProjectMainCAttribute.PRM_CONTRACT_ID, PrmProjectMainCAttribute.PRM_CONTRACT_ID)) {
            rtn = true;
        }

        if ((!rtn) &&
                checkRecordsDtoContentChanged(prmProjectMainCDto.getPrmAssociatedUnitsCDto(),
                        prmProjectMainDto.getPrmAssociatedUnitsDto(), ProjectmainManager.CONTRACT_DETAIL_FIELD,
                        PrmBudgetDetailCAttribute.LAST_UUID, CommonAttribute.UUID)) {
            rtn = true;
        }

        if ((!rtn) &&
                checkRecordsDtoContentChanged(prmProjectMainCDto.getPrmMemberDetailPCDto(),
                        prmProjectMainDto.getPrmMemberDetailPDto(), ProjectmainManager.CONTRACT_DETAIL_FIELD,
                        PrmBudgetDetailCAttribute.LAST_UUID, CommonAttribute.UUID)) {
            rtn = true;
        }

        if ((!rtn) &&
                checkRecordsDtoContentChanged(prmProjectMainCDto.getPrmPayDetailPCDto(),
                        prmProjectMainDto.getPrmPayDetailPDto(), ProjectmainManager.CONTRACT_DETAIL_FIELD,
                        PrmBudgetDetailCAttribute.LAST_UUID, CommonAttribute.UUID)) {
            rtn = true;
        }

        if ((!rtn) &&
                checkRecordsDtoContentChanged(prmProjectMainCDto.getPrmProgressDetailPCDto(),
                        prmProjectMainDto.getPrmProgressDetailPDto(), ProjectmainManager.CONTRACT_DETAIL_FIELD,
                        PrmBudgetDetailCAttribute.LAST_UUID, CommonAttribute.UUID)) {
            rtn = true;
        }

        if ((!rtn) &&
                checkRecordsDtoContentChanged(prmProjectMainCDto.getPrmSquareDetailPCDto(),
                        prmProjectMainDto.getPrmSquareDetailPDto(), ProjectmainManager.CONTRACT_DETAIL_FIELD,
                        PrmBudgetDetailCAttribute.LAST_UUID, CommonAttribute.UUID)) {
            rtn = true;
        }

        if ((!rtn) &&
                checkRecordsDtoContentChanged(prmProjectMainCDto.getPrmReceiptsDetailPCDto(),
                        prmProjectMainDto.getPrmReceiptsDetailPDto(), ProjectmainManager.CONTRACT_DETAIL_FIELD,
                        PrmBudgetDetailCAttribute.LAST_UUID, CommonAttribute.UUID)) {
            rtn = true;
        }

        if ((!rtn) &&
                checkRecordsDtoContentChanged(prmProjectMainCDto.getPrmQsPCDto(),
                        prmProjectMainDto.getPrmQsPDto(), ProjectmainManager.CONTRACT_DETAIL_FIELD,
                        PrmBudgetDetailCAttribute.LAST_UUID, CommonAttribute.UUID)) {
            rtn = true;
        }

        if ((!rtn) &&
                checkRecordsDtoContentChanged(prmProjectMainCDto.getPrmBudgetDetailCDto(),
                        prmProjectMainDto.getPrmBudgetDetailDto(), ProjectmainManager.CONTRACT_DETAIL_FIELD,
                        PrmBudgetDetailCAttribute.LAST_UUID, CommonAttribute.UUID)) {
            rtn = true;
        }

        if ((!rtn) &&
                checkRecordsDtoContentChanged(prmProjectMainCDto.getPrmBudgetDetailCDto(),
                        prmProjectMainDto.getPrmBudgetDetailDto(), ProjectmainManager.CONTRACT_DETAIL_FIELD,
                        PrmBudgetDetailCAttribute.LAST_UUID, CommonAttribute.UUID)) {
            rtn = true;
        }
        if ((!rtn) &&
                checkRecordsDtoContentChanged(prmProjectMainCDto.getPrmBudgetRunCDto(),
                        prmProjectMainDto.getPrmBudgetRunDto(), ProjectmainManager.CONTRACT_DETAIL_FIELD,
                        PrmBudgetDetailCAttribute.LAST_UUID, CommonAttribute.UUID)) {
            rtn = true;
        }
        return rtn;
    }

    private boolean checkRecordsDtoContentChanged(List lstCurrObj, List lstPreviousObj, String[] compareFields, String linkField1, String linkField2) {
        boolean result = false;
        if (ListUtil.isNotEmpty(lstCurrObj) && ListUtil.isEmpty(lstPreviousObj) ||
                ListUtil.isEmpty(lstCurrObj) && ListUtil.isNotEmpty(lstPreviousObj)
                ) {
            result = true;
        } else if (ListUtil.isEmpty(lstCurrObj) && ListUtil.isEmpty(lstPreviousObj)) {
            return false;
        }
        if (!result) {
            for (Object obj : lstCurrObj) {
                Object currLinkValue = BeanUtil.getProperty(obj, linkField1);
                Object source = lstPreviousObj.stream().filter(y -> {
                    Object previousLinkValue = BeanUtil.getProperty(y, linkField2);
                    return ObjectUtil.beSame(currLinkValue, previousLinkValue);
                }).findFirst().orElse(null);

                if (source == null) {
                    for (String field : compareFields) {
                        if (!ObjectUtil.beSame(BeanUtil.getProperty(obj, field), BeanUtil.getProperty(source, field))) {
                            result = true;
                            break;
                        }
                    }
                    if (result) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    //重大项目发送消息
    @Override
    public void sendMsgToManager(String uuid, String projectCode) {
        //发送消息
        String sql = "SELECT * FROM V_USER_ROLES T WHERE ROLE_NAME IN('运管部经营主管')";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List<Map<String, Object>> lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        List<String> userIdLst = new ArrayList<>();
        ReceiptsMeta receiptsMeta = new ReceiptsMeta();
        if (ListUtil.isNotEmpty(lstUserInfo)) {
            for (int i = 0; i < lstUserInfo.size(); i++) {
                String userId = (String) lstUserInfo.get(i).get("userId");
                userIdLst.add(userId);
            }
            receiptsMeta.setLstSendToUserId(userIdLst);
        }

        Map map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("projectCode", projectCode);
        String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
        String templateNo = "PRM_PROJECT_REVERSE";
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);
    }

}
