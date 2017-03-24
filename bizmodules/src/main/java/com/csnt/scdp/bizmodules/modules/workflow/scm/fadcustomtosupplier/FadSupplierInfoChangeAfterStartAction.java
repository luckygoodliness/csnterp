package com.csnt.scdp.bizmodules.modules.workflow.scm.fadcustomtosupplier;

import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.dto.ScmSupplierBankCDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.dto.ScmSupplierCDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.services.intf.SupplierinfochangeManager;
import com.csnt.scdp.bizmodules.modules.workflow.ERPWorkFlowBaseVariableCollectionAction;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
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
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("fad_supplier_in_fo_change-after-start")
@Transactional

public class FadSupplierInfoChangeAfterStartAction extends ERPWorkFlowBaseVariableCollectionAction {
    @Resource(name = "supplierinfochange-manager")
    private SupplierinfochangeManager supplierinfochangeManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map dataInfo = super.perform(inMap);
        Map mapVar = new HashMap<>();
        String uuid = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        String supplierGenre = (String) dataInfo.get("supplierGenre");
        //合格供方执行此方法
        if (StringUtil.isNotEmpty(supplierGenre) && supplierGenre.equals("4")) {
            //根据自己的逻辑更新state
            inMap.put(WorkFlowAttribute.SKIP_COMMON_PROCESS_AFTER_ACTION, true);
            updateState(inMap);
            ScmSupplierCDto scmSupplierCDto = (ScmSupplierCDto) DtoHelper.findDtoByPK(ScmSupplierCDto.class, uuid);
            Map mapSupplierCode = new HashMap();
            List<String> bankId = new ArrayList<String>();
            List<ScmSupplierBankCDto> scmSupplierBankCDtoList = scmSupplierCDto.getScmSupplierBankCDto();
            if (ListUtil.isNotEmpty(scmSupplierBankCDtoList)) {
                for (int i = 0; i < scmSupplierBankCDtoList.size(); i++) {
                    if (StringUtil.isNotEmpty(scmSupplierBankCDtoList.get(i).getAccountNo())) {
                        if (!(mapSupplierCode.containsKey(scmSupplierBankCDtoList.get(i).getAccountNo().toString()))) {
                            mapSupplierCode.put(scmSupplierBankCDtoList.get(i).getAccountNo().toString(), scmSupplierBankCDtoList.get(i).getAccountNo().toString());
                            bankId.add(scmSupplierBankCDtoList.get(i).getAccountNo().toString());
                        } else {
                            throw new BizException("银行账号不能重复！");
                        }
                    }
                }
                if (bankId.size() > 0) {
                    supplierinfochangeManager.bankValidate(bankId, scmSupplierCDto.getPuuid());
                }
            }

            if (StringUtil.isNotEmpty(uuid)) {
                supplierinfochangeManager.scmSupplierChange(uuid);
            }
        }

        return mapVar;
    }

    private void updateState(Map inMap) {
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        String sql = "update scm_supplier_c set state = ? where uuid = ?";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List lstParam = new ArrayList();
        lstParam.add("2");
        lstParam.add(businessKey);
        daoMeta.setLstParams(lstParam);
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        pcm.updateByNativeSQL(daoMeta);
    }

}
