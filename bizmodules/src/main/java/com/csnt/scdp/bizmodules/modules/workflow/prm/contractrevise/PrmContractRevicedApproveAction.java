package com.csnt.scdp.bizmodules.modules.workflow.prm.contractrevise;

import com.csnt.scdp.bizmodules.entity.prm.PrmContract;
import com.csnt.scdp.bizmodules.entity.prm.PrmContractC;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmContractAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmexamperiod.services.intf.PrmExamPeriodManager;
import com.csnt.scdp.bizmodules.modules.prm.contractc.services.intf.ContractCManager;
import com.csnt.scdp.bizmodules.modules.prm.prmcustomer.services.intf.PrmcustomerManager;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2016/6/19.
 */
@Scope("singleton")
@Controller("prm_contract_revised-after-complete")
public class PrmContractRevicedApproveAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(PrmContractRevicedApproveAction.class);

    @Resource(name = "contract-c-manager")
    private ContractCManager contractCManager;

    @Resource(name = "prmcustomer-manager")
    private PrmcustomerManager prmcustomerManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap();
        String uuid = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        PrmContractC prmContractC = PersistenceFactory.getInstance().findByPK(PrmContractC.class, uuid);
        String prmContractId = prmContractC.getPrmContractId();
        String wfStatus = (String) inMap.get(WorkFlowAttribute.STATUS);
        if (WorkFlowAttribute.STATUS_FIXED.equals(wfStatus)) {
            List paramLst = new ArrayList();
            paramLst.add(prmContractId);
            DAOMeta daoMeta = DAOHelper.getDAO("contract-c-dao", "query_contract_all", paramLst);
            daoMeta.setNeedFilter(false);
            List lstRet = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstRet) && lstRet.size() > 1) {
                Integer currIsMajorProject = prmContractC.getIsMajorProject();
                BigDecimal preIsMajorProject = (BigDecimal) ((Map) lstRet.get(1)).get("isMajorProject");

                if (Integer.valueOf(1).equals(currIsMajorProject) && BigDecimal.valueOf(0).equals(preIsMajorProject)) {
                    contractCManager.sendMsg(prmContractId, prmContractC.getContractName());
                }
            }
            //更新审核时间
            Date auditDate = new Date();
            prmContractC.setAuditTime(auditDate);
            PrmExamPeriodManager prmExamPeriodManager = BeanFactory.getBean("prmexamperiod-manager".toLowerCase());
            Date examDate = prmExamPeriodManager.getExamDateFromAppointedDate(auditDate);
            prmContractC.setExamDate(examDate);

            //若没有业主单位，审批通过，自动添加到客户管理，同时更新合同
            if (StringUtil.isNotEmpty(prmContractC.getCustomerId())) {
                PrmCustomer customer = PersistenceFactory.getInstance().findByPK(PrmCustomer.class, prmContractC.getCustomerId());
                if (customer == null) {
                    String customerName = prmContractC.getCustomerId();
                    String customerId = prmcustomerManager.createPrmcustomerByName(customerName);
                    prmContractC.setCustomerId(customerId);
                    //send message to 运管部经营主管
                    contractCManager.sendMsgForCustomer(prmContractC.getContractName(), customerId, customerName);
                }
            }
            PersistenceFactory.getInstance().update(prmContractC);
            //同步到合同管理模块
            if (StringUtil.isNotEmpty(prmContractC.getPrmContractId())) {
                PrmContract prmContract = PersistenceFactory.getInstance().findByPK(PrmContract.class, prmContractC.getPrmContractId());
                if (prmContract != null) {
                    if (!prmContract.getCustomerId().equals(prmContractC.getCustomerId())) {
                        prmContract.setCustomerId(prmContractC.getCustomerId());
                    }
                    if (prmContract.getContractNowMoney().compareTo(prmContractC.getContractNowMoney()) != 0) {
                        prmContract.setContractNowMoney(prmContractC.getContractNowMoney());
                    }
                    if (StringUtil.isNotEmpty(prmContractC.getAffiliatedInstitutions())) {
                        if (!prmContractC.getAffiliatedInstitutions().equals(prmContract.getAffiliatedInstitutions())) {
                            prmContract.setAffiliatedInstitutions(prmContractC.getAffiliatedInstitutions());
                        }
                    }

                    if (prmContract.getContractStartDate() != prmContractC.getContractStartDate()) {
                        prmContract.setContractStartDate(prmContractC.getContractStartDate());
                    }
                    if (prmContract.getContractEndDate() != prmContractC.getContractEndDate()) {
                        prmContract.setContractEndDate(prmContractC.getContractEndDate());
                    }
                    if (prmContract.getContractSignDate() != prmContractC.getContractSignDate()) {
                        prmContract.setContractSignDate(prmContractC.getContractSignDate());
                    }

                    if (StringUtil.isEmpty(prmContractC.getProjectManager()) && StringUtil.isEmpty(prmContract.getProjectManager())) {
                    } else if (StringUtil.isEmpty(prmContractC.getProjectManager()) || StringUtil.isEmpty(prmContract.getProjectManager())) {
                        prmContract.setProjectManager(prmContractC.getProjectManager());
                    } else {
                        if (!prmContractC.getProjectManager().equals(prmContract.getProjectManager())) {
                            prmContract.setProjectManager(prmContractC.getProjectManager());
                        }
                    }
                    if (StringUtil.isEmpty(prmContractC.getGeneralEngineer()) && StringUtil.isEmpty(prmContract.getGeneralEngineer())) {
                    } else if (StringUtil.isEmpty(prmContractC.getGeneralEngineer()) || StringUtil.isEmpty(prmContract.getGeneralEngineer())) {
                        prmContract.setGeneralEngineer(prmContractC.getGeneralEngineer());
                    } else {
                        if (!prmContractC.getGeneralEngineer().equals(prmContract.getGeneralEngineer())) {
                            prmContract.setGeneralEngineer(prmContractC.getGeneralEngineer());
                        }
                    }
                    if (StringUtil.isNotEmpty(prmContractC.getBuildRegion()) && !prmContractC.getBuildRegion().equals(prmContract.getBuildRegion())) {
                        prmContract.setBuildRegion(prmContractC.getBuildRegion());
                    }
                    PersistenceFactory.getInstance().update(prmContract);
                }
            }
        }
        out.put(PrmContractAttribute.PRM_CONTRACT_C_DTO, PersistenceFactory.getInstance().findByPK
                (PrmContractC.class, uuid));
        //Do After

        return out;
    }
}
