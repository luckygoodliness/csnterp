package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.bizmodules.entity.fad.FadInvoice;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadInvoiceAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractInvoiceAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.common.intf.CommonServiceManager;
import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadInvoiceDto;
import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.ScmContractInvoiceDto;
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.dto.ScmContractDto;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
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
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 14:13:12
 */

@Scope("singleton")
@Controller("invoice-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "invoice-manager")
    private InvoiceManager invoiceManager;
    @Resource(name = "erp-common-service-manager")
    private CommonServiceManager commonServiceManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map out = super.perform(inMap);
        //相关合同grid合同号与合同总额赋值
        Map fadInvoiceMap = (Map) out.get(FadInvoiceAttribute.FAD_INVOICE_DTO);
        List userLit = new ArrayList();
        String renderPerson = (String) fadInvoiceMap.get("renderPerson");
        String createBy = (String) fadInvoiceMap.get("createBy");
        if(StringUtil.isNotEmpty(renderPerson)){
            userLit.add(renderPerson);
        }
        if(StringUtil.isNotEmpty(createBy)){
            userLit.add(createBy);
        }
        if (ListUtil.isNotEmpty(userLit)) {
            List<ScdpUser> userList = commonServiceManager.findUserByUserIds(userLit);
            if (!ListUtil.isEmpty(userList)) {
                for(ScdpUser s : userList){
                    if(s.getUserId().equals(renderPerson)){
                        fadInvoiceMap.put("renderPersonDesc", s.getUserName());
                    }
                    if(s.getUserId().equals(createBy)){
                        fadInvoiceMap.put("createBy", s.getUserName());
                    }
                }
            }
        }
        if (fadInvoiceMap.get("prmProjectMainId") != null && fadInvoiceMap.get("prmProjectMainId") != "") {
            String prmProjectMainId = (String) fadInvoiceMap.get("prmProjectMainId");
            PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, prmProjectMainId);
            fadInvoiceMap.put("prmProjectMainIdDesc", prmProjectMain.getProjectName());
        }
        String taxRate = fadInvoiceMap.get("taxRate").toString();
        if (taxRate != null) {
            String taxRateDesc = FMCodeHelper.getDescByCode(taxRate, "FAD_TAX_RATE");
            fadInvoiceMap.put("taxRateDesc", taxRateDesc);
        }
        if (fadInvoiceMap != null) {
            String orgCode = (String) fadInvoiceMap.get("officeId");
            if (orgCode != null) {
                String orgName = OrgHelper.getOrgNameByCode(orgCode);
                fadInvoiceMap.put("officeIdDesc", orgName);
            }
        }

        List<Map> scmContractInvoiceDtoList = (List) fadInvoiceMap.get("scmContractInvoiceDto");
        List<Map> fadCashReqInvoiceDtoList = (List) fadInvoiceMap.get("fadCashReqInvoiceDto");
        List contractId = new ArrayList();
        List fadCashReqId = new ArrayList();
        StringBuffer contractIdSb=new StringBuffer();
        if ( !ListUtil.isEmpty(scmContractInvoiceDtoList)) {
            for (int i=0;i<scmContractInvoiceDtoList.size();i++) {
                Map m =scmContractInvoiceDtoList.get(i);
                String scmContractId = m.get("scmContractId").toString();
                contractId.add(scmContractId);
                if(i==0){
                    contractIdSb.append("'"+scmContractId+"'");
                }else {
                    contractIdSb.append(",'"+scmContractId+"'");
                }
            }
        }
        if (!ListUtil.isEmpty(fadCashReqInvoiceDtoList)) {
            for (Map m : fadCashReqInvoiceDtoList) {
                fadCashReqId.add(m.get("fadCashReqId"));
            }
        }

//        List<ScmContract> scmContractList = new ArrayList();
        List<ScmContractDto> lstScmContractDto = new ArrayList<>();
        if (ListUtil.isNotEmpty(contractId)) {
//            Map mapContractId = new HashMap();
//            mapContractId.put(ScmContractAttribute.UUID, contractId);
//            scmContractList = pcm.findByAnyFields(ScmContract.class, mapContractId, null);
//            List<Map> lstParam = new ArrayList();
            DAOMeta daoMeta = DAOHelper.getDAO("invoice-dao", "vw_scm_contract_execute_query", null);
            String sql = daoMeta.getStrSql();
            sql = sql + " where uuid in ("+contractIdSb+")";
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            List<Map<String, Object>> lstVwScmContractExecuteMap = pcm.findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstVwScmContractExecuteMap)) {
                lstScmContractDto = lstVwScmContractExecuteMap.stream()
                        .map(x -> (ScmContractDto) BeanUtil.map2Dto(x, ScmContractDto.class)).collect(Collectors.toList());
            }
        }
        Map<String, ScmContractDto> scmContractDtoMap = new HashMap();
        if (ListUtil.isNotEmpty(lstScmContractDto)) {
            lstScmContractDto.forEach(t -> scmContractDtoMap.put(t.getUuid(), t));
            scmContractInvoiceDtoList.forEach(s -> {
                if (scmContractDtoMap.containsKey(s.get(ScmContractInvoiceAttribute.SCM_CONTRACT_ID))) {
                    ScmContractDto sc = scmContractDtoMap.get(s.get(ScmContractInvoiceAttribute.SCM_CONTRACT_ID));
                    s.put(ScmContractInvoiceAttribute.SCM_CONTRACT_CODE, sc.getScmContractCode());

                    //如果标记位isStamp为1，则将标记源的课题代号覆盖到合同关联的采购明细的项目代号。功能暂时隐藏，不放出。需求没有要求实时更新原有界面值。
//                    String scmContractCode = sc.getScmContractCode();
//                    if (StringUtil.isNotEmpty(scmContractCode)) {
//                        // 根据一组发票号逐条取出来对应的采购明细的标记位
//                        String strSql = "SELECT distinct SC.SCM_CONTRACT_CODE, \n" +
//                                "       PPRD.IS_STAMP,\n" +
//                                "       PPRD.STAMP_PROJECT_UUID,\n" +
//                                "       SC.UUID as Scuuid,\n" +
//                                "       M.PROJECT_CODE\n" +
//                                "  from  SCM_CONTRACT SC \n" +
//                                "  left join PRM_PURCHASE_REQ_DETAIL PPRD \n" +
//                                "  on SC.UUID = PPRD.SCM_CONTRACT_ID\n" +
//                                "  LEFT JOIN PRM_PROJECT_MAIN M \n" +
//                                "  ON PPRD.STAMP_PROJECT_UUID = M.UUID\n" +
//                                "  WHERE SC.SCM_CONTRACT_CODE = '" + scmContractCode + "'";
//                        DAOMeta daoMeta = new DAOMeta(strSql);
//                        List resultList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
//                        if (ListUtil.isNotEmpty(resultList)) {
//                            for (int i = 0; i < resultList.size(); i++) {
//                                Map result = (Map) resultList.get(i);
//                                String isStamp = (String) result.get("isStamp");
//                                String projectCode = (String) result.get("projectCode");
//                                if ("1".equals(isStamp)) {
//                                    s.put(ScmContractInvoiceAttribute.FAD_SUBJECT_CODE, projectCode);
//                                } else {
//                                    s.put(ScmContractInvoiceAttribute.FAD_SUBJECT_CODE, sc.getSubjectCodeQ());
//                                }
//                            }
//                        }
//                    }

                    s.put(ScmContractInvoiceAttribute.FAD_SUBJECT_CODE, sc.getSubjectCodeQ());
                    s.put(ScmContractInvoiceAttribute.PROJECT_NAME, sc.getProjectName());
                    s.put(ScmContractInvoiceAttribute.SUBJECT_NAME, sc.getSubjectName());
                    s.put(ScmContractInvoiceAttribute.OFFICE_ID, sc.getOfficeId());
                    s.put(ScmContractInvoiceAttribute.OFFICE_NAME, sc.getOrgName());
                    s.put(ScmContractInvoiceAttribute.CONTRACT_TOTAL_VALUE, sc.getAmount());
                    s.put(ScmContractInvoiceAttribute.CONTRACT_SUBJECT_CODE, sc.getSubjectCode());
                    s.put(ScmContractInvoiceAttribute.FAD_INVOICE_MONEY, sc.getFadInvoiceMoney());
                    s.put(ScmContractInvoiceAttribute.FAD_INVOICE_MONEY_L, sc.getFadInvoiceMoneyL());
                }
            });
        }

//        if (ListUtil.isNotEmpty(scmContractList)) {
//            for(ScmContract scmContract:scmContractList){
//                for(Map s:scmContractInvoiceDtoList){
//                    if (s.get(ScmContractInvoiceAttribute.SCM_CONTRACT_ID).equals(scmContract.getUuid())) {
//                        s.put(ScmContractInvoiceAttribute.SCM_CONTRACT_CODE, scmContract.getScmContractCode());
//                        s.put(ScmContractInvoiceAttribute.CONTRACT_TOTAL_VALUE,scmContract.getAmount());
//                        s.put(ScmContractInvoiceAttribute.CONTRACT_SUBJECT_CODE,scmContract.getSubjectCode());
//                    }
//                }
//            }
//        }

        List<Map> fadCashReqList = invoiceManager.getCashReqInfo(fadCashReqId);
        Map<String, Map> fadCashReq = new HashMap();
        if (ListUtil.isNotEmpty(fadCashReqList)) {
            fadCashReqList.forEach(t -> fadCashReq.put(t.get("uuid").toString(), t));
            fadCashReqInvoiceDtoList.forEach(s -> {
                if (fadCashReq.containsKey(s.get("fadCashReqId"))) {
                    Map fm = new HashMap(fadCashReq.get(s.get("fadCashReqId")));
                    s.put("runningNo", fm.get("runningNo"));//对应请款流水号
                    s.put("reqMoney", fm.get("money"));//对应请款金额
                    s.put("clearancedMoney", fm.get("clearancedMoney"));//对应请款金额
//                    s.put("clearanceMoney", fm.get("clearanceMoney"));//对应请款金额
                    s.put("cashContract", fm.get("cashContract"));//对应请款合同
                    s.put("cashVoucher", fm.get("cashVoucher"));//对应请款凭证
                }
            });
        }

//        List<Map> fadCashReqList = invoiceManager.getCashReqInfo(fadCashReqId);
//        if (ListUtil.isNotEmpty(fadCashReqList)) {
//            for (Map m : fadCashReqList) {
//                for (Map s : fadCashReqInvoiceDtoList) {
//                    if (s.get("fadCashReqId").equals(m.get("uuid"))) {
//                        s.put("runningNo", m.get("runningNo"));//对应请款流水号
//                        s.put("reqMoney", m.get("money"));//对应请款金额
//                        s.put("cashContract", m.get("cashContract"));//对应请款合同
//                        s.put("cashVoucher", m.get("cashVoucher"));//对应请款凭证
//                    }
//                }
//            }
//        }
        return out;
    }
}
