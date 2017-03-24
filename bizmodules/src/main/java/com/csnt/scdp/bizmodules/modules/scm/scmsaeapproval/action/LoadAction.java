package com.csnt.scdp.bizmodules.modules.scm.scmsaeapproval.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmSae;
import com.csnt.scdp.bizmodules.entity.scm.ScmSaeObject;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierLimitChangeAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmSupplierLimitChangeDAttribute;
import com.csnt.scdp.bizmodules.modules.scm.scmsae.dto.ScmSaeObjectDto;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.scmsaeapproval.services.intf.ScmsaeapprovalManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-30 19:12:22
 */

@Scope("singleton")
@Controller("scmsaeapproval-load")
@Transactional
public class LoadAction extends CommonLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "scmsaeapproval-manager")
    private ScmsaeapprovalManager scmsaeapprovalManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        Map scmSaeApprovalDtoMap = (Map) out.get("scmSaeApprovalDto");
        List<Map> scmSaeApprovalDDtoList = (List) scmSaeApprovalDtoMap.get("scmSaeApprovalDDto");
        if (ListUtil.isNotEmpty(scmSaeApprovalDDtoList)) {
            if (StringUtil.isNotEmpty(scmSaeApprovalDtoMap.get("curYear").toString())) {
                List<ScmSae> ScmSaeList = new ArrayList<>();
                Integer curYear = (Integer) scmSaeApprovalDtoMap.get("curYear");
                Map mapContractId = new HashMap();
                mapContractId.put("curYear", curYear);
                ScmSaeList = pcm.findByAnyFields(ScmSae.class, mapContractId, null);
                mapContractId.remove("curYear");
                List<String> scmSaeIdList = new ArrayList<>();
                List<String> supplierIdList = new ArrayList<>();
                List list = new ArrayList();
                if (ListUtil.isNotEmpty(ScmSaeList)) {
                    for (int i = 0; i < ScmSaeList.size(); i++) {
                        scmSaeIdList.add(ScmSaeList.get(i).getUuid());
                    }
                    for (int i = 0; i < scmSaeApprovalDDtoList.size(); i++) {
                        supplierIdList.add(scmSaeApprovalDDtoList.get(i).get("supplierId").toString());
                    }
                    mapContractId.put("uuid", supplierIdList);
                    List<ScmSupplier>  scmSupplier =pcm.findByAnyFields(ScmSupplier.class, mapContractId, null);
                    mapContractId.clear();
                    mapContractId.put("scmSaeId", scmSaeIdList);
                    list = pcm.findByAnyFields(ScmSaeObject.class, mapContractId, null);
                    List<ScmSaeObject> listDto = new ArrayList<>();
                    for (Object o : list) {
                        ScmSaeObject s = (ScmSaeObject) o;
                        ScmSaeObjectDto dto = new ScmSaeObjectDto();
                        try {
                            BeanUtils.copyProperties(dto, s);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        String sql = " select t.name from scm_material_class t where t.code = ? ";
                        List lstParams = new ArrayList<>();
                        lstParams.add(dto.getMaterialCode());
                        DAOMeta daoMeta1 = new DAOMeta(sql, lstParams);
                        List lst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta1);
                        if (lst != null && lst.size() > 0) {
                            dto.setMaterialClassName((String) ((Map) lst.get(0)).get("name"));
                        }
                        listDto.add(dto);
                    }
                    List lis = new ArrayList<>();
                    for (int i = 0; i < scmSaeApprovalDDtoList.size(); i++) {
                        for (int j = 0; j < scmSupplier.size(); j++) {
                              if (scmSaeApprovalDDtoList.get(i).get("supplierId").equals(scmSupplier.get(j).getUuid())){
                                  scmSaeApprovalDDtoList.get(i).put("supplierName", scmSupplier.get(j).getCompleteName());
                              }
                        }
                        for (int j = 0; j <listDto.size() ; j++) {
                            if (scmSaeApprovalDDtoList.get(i).get("supplierId").equals(listDto.get(j).getSupplierId())){
                                lis.add(listDto.get(j));
                            }
                        }
                    }
                    scmSaeApprovalDtoMap.put("scmSaeObjectDto",lis);
                }
            }

        }
        if (null != scmSaeApprovalDtoMap.get("curYear")) {
            String taxRate = scmSaeApprovalDtoMap.get("curYear").toString();
            if (taxRate != null) {
                String taxRateDesc = FMCodeHelper.getDescByCode(taxRate, "CDM_YEAR");
                scmSaeApprovalDtoMap.put("curYear", taxRateDesc);
            }
        }

        //Do After
        return out;
    }
}
