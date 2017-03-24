package com.csnt.scdp.bizmodules.modules.prm.prmunknownreceipts.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmCustomerAttribute;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.prm.prmunknownreceipts.services.intf.PrmunknownreceiptsManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
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
 * @timestamp 2015-09-26 16:52:15
 */

@Scope("singleton")
@Controller("prmunknownreceipts-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "prmunknownreceipts-manager")
    private PrmunknownreceiptsManager prmunknownreceiptsManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map prmUnknownReceiptsDto = (Map) out.get("prmUnknownReceiptsDto");
        loadPayerDesc(pcm, prmUnknownReceiptsDto);
        loadPrmReceipts(prmUnknownReceiptsDto);

        //Do After
        return out;
    }

    private void loadPrmReceipts(Map prmUnknownReceiptsDto) {
        String uuid = (String)prmUnknownReceiptsDto.get("uuid");
        String sql = "select pre.*, pro.project_name project_name, fmcode.code_desc money_type_desc, prc.customer_name customer_name, org.org_name dept_name\n" +
                "  From prm_receipts pre\n" +
                "  left join prm_project_main pro on pre.prm_project_main_id = pro.uuid\n" +
                "  left join scdp_code fmcode  on fmcode.code_type = 'MONEY_TYPE' and fmcode.sys_code = pre.money_type\n" +
                "  left join prm_customer prc on pre.customer_id = prc.customer_code " +
                "  left join scdp_org org on pre.department_code = org.org_code" +
                "  where pre.prm_unknown_receipts_id = ?";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List lstParam = new ArrayList();
        lstParam.add(uuid);
        daoMeta.setLstParams(lstParam);
        List lstReceiptsInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        prmUnknownReceiptsDto.put("prmReceiptsDto", lstReceiptsInfo);
    }

    private void loadPayerDesc(PersistenceCrudManager pcm, Map prmUnknownReceiptsDto) {
        if (MapUtil.isNotEmpty(prmUnknownReceiptsDto)) {
            String payer = (String) prmUnknownReceiptsDto.get("payer");
            List lstPartyId = new ArrayList();
            List<Map> prmReceiptsList = new ArrayList();
            if(StringUtil.isNotEmpty(payer)){
                lstPartyId.add(payer);
                if(prmUnknownReceiptsDto.get("prmReceiptsDto")!=null){
                    prmReceiptsList = (List)prmUnknownReceiptsDto.get("prmReceiptsDto");
                    for(Map s:prmReceiptsList){
                        lstPartyId.add(s.get("customerId"));
                    }
                }
                Map mapPartyId = new HashMap();
                mapPartyId.put(PrmCustomerAttribute.UUID, lstPartyId);
                List<PrmCustomer> lstParty = pcm.findByAnyFields(PrmCustomer.class, mapPartyId, null);
                if (ListUtil.isNotEmpty(lstParty)) {
                    for (PrmCustomer party : lstParty) {
                        if (party.getUuid().equals(payer)) {
                            prmUnknownReceiptsDto.put("payerDesc", party.getCustomerName());
                        }
                        for(Map s:prmReceiptsList){
                            if (party.getUuid().equals(s.get("customerId"))) {
                                s.put("customerName", party.getCustomerName());
                            }
                        }
                    }
                }
            }
        }
    }
}
