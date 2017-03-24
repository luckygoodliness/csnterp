package com.csnt.scdp.bizmodules.modules.prm.receipts.action;

import com.csnt.scdp.bizmodules.entity.prm.*;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmCustomerAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.prm.receipts.dto.PrmReceiptsDto;
import com.csnt.scdp.bizmodules.modules.prm.receipts.services.intf.ReceiptsManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 09:19:57
 */

@Scope("singleton")
@Controller("receipts-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "receipts-manager")
    private ReceiptsManager receiptsManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        Map prmReceiptsDto = (Map) out.get("prmReceiptsDto");
        if (MapUtil.isNotEmpty(prmReceiptsDto)) {
            String prmProjectMainId = (String) prmReceiptsDto.get("prmProjectMainId");
            if (StringUtil.isNotEmpty(prmProjectMainId)) {
                PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, prmProjectMainId);
                prmReceiptsDto.put("projectName", prmProjectMain.getProjectName());
                prmReceiptsDto.put("projectCode", prmProjectMain.getProjectCode());
                prmReceiptsDto.put("isPreProject", prmProjectMain.getIsPreProject());
            }
            String isInternal = StringUtil.replaceNull(prmReceiptsDto.get("isInternal"));
            if("1".equals(isInternal)){
                String prmContractId = (String) prmReceiptsDto.get("prmContractId");
                Map internalMap = receiptsManager.loadInternalInfo(prmContractId);
                prmReceiptsDto.put("internalOffice", StringUtil.replaceNull(internalMap.get("internalOffice")));
                String internalOfficeCode=OrgHelper.getOrgNameByCode(StringUtil.replaceNull(internalMap.get("internalOffice")));
                prmReceiptsDto.put("internalOfficeDesc", internalOfficeCode);
            }
            List lstPartyId = new ArrayList();
            String payer = (String) prmReceiptsDto.get("payer");
            if (StringUtil.isNotEmpty(payer)) {
                lstPartyId.add(payer);
            }
            String customerId = (String) prmReceiptsDto.get("customerId");
            if (StringUtil.isNotEmpty(customerId)) {
                lstPartyId.add(customerId);
            }

            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            Map mapPartyId = new HashMap();
            if (ListUtil.isNotEmpty(lstPartyId)) {
                mapPartyId.put(PrmCustomerAttribute.UUID, lstPartyId);
                List<PrmCustomer> lstParty = pcm.findByAnyFields(PrmCustomer.class, mapPartyId, null);
                if (ListUtil.isNotEmpty(lstParty)) {
                    for (PrmCustomer party : lstParty) {
                        if (party.getUuid().equals(customerId)) {
                            prmReceiptsDto.put("customerIdDesc", party.getCustomerName());
                        }
                        if (party.getUuid().equals(payer)) {
                            prmReceiptsDto.put("payerDesc", party.getCustomerName());
                        }
                    }
                }
            }
            String prmUnknownReceiptsId = (String) prmReceiptsDto.get("prmUnknownReceiptsId");
            if (StringUtil.isNotEmpty(prmUnknownReceiptsId)) {
                PrmUnknownReceipts prmUnknownReceipts = PersistenceFactory.getInstance().findByPK(PrmUnknownReceipts.class, prmUnknownReceiptsId);
                prmReceiptsDto.put("prmUnknownReceiptsIdDesc", prmUnknownReceipts.getReceiptNo());
            }
            String departmentCode = (String) prmReceiptsDto.get("departmentCode");
            if (StringUtil.isNotEmpty(departmentCode)) {
                prmReceiptsDto.put("departmentCodeDesc", OrgHelper.getOrgNameByCode(departmentCode));
            }
            String prmContractId = (String) prmReceiptsDto.get("prmContractId");
            if(StringUtil.isNotEmpty(prmContractId)){
                PrmContract prmContract = PersistenceFactory.getInstance().findByPK(PrmContract.class, prmContractId);
                prmReceiptsDto.put("prmContractIdDesc", prmContract.getContractName());
            }
            //已审核、已生成凭证、已发NC的项目收款需要显示应收账款余额 modify by lijx 2016-08-11
            String state = (String) prmReceiptsDto.get("state");
                String sql ="SELECT (select sum(pb.INVOICE_MONEY) " +
                        "           from PRM_BILLING pb " +
                        "          where pb.state = '2' and pb.BILL_TYPE=0 " +
                        "            AND PB.PRM_PROJECT_MAIN_ID = ?) - " +
                        "        (select SUM(PR.ACTUAL_MONEY) " +
                        "           from PRM_RECEIPTS PR " +
                        "          WHERE PR.PRM_PROJECT_MAIN_ID = ? " +
                        "            AND (PR.STATE = '2' OR PR.STATE = '8' OR PR.STATE = '4')) as RECE_BALANCE " +
                        "   FROM DUAL where exists (select 1  from PRM_BILLING pb  " +
                        " where pb.state = '2' and pb.BILL_TYPE=0 AND PB.PRM_PROJECT_MAIN_ID = ? )";
                List lstParams = new ArrayList<>();
                lstParams.add((String) prmReceiptsDto.get("prmProjectMainId"));
                lstParams.add((String) prmReceiptsDto.get("prmProjectMainId"));
                lstParams.add((String) prmReceiptsDto.get("prmProjectMainId"));
                DAOMeta daoMeta1 = new DAOMeta(sql, lstParams);
                List lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta1);
                if(lstResult!=null && lstResult.size()>0){
                    BigDecimal bd = (BigDecimal) ((Map) lstResult.get(0)).get("receBalance");
                    if(bd != null) {
                        prmReceiptsDto.put("receivableBalance", bd.compareTo(new BigDecimal(0))<0?0:bd);
                    }
                }else{
                    prmReceiptsDto.put("receivableBalance", 0);
                }
            }

        //Do After
        return out;
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        PrmReceiptsDto receiptsDto = (PrmReceiptsDto)dtoObj;
        receiptsManager.loadExtraDescField(receiptsDto.getPrmReceiptsScmInvoiceDto());
    }
}
