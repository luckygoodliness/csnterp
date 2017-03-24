package com.csnt.scdp.bizmodules.modules.scm.fadsuppliermapping.action;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectBudgetCD;
import com.csnt.scdp.bizmodules.entity.scm.FadSupplierMapping;
import com.csnt.scdp.bizmodules.modules.scm.fadsuppliermapping.dto.FadSupplierMappingDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsupplierlimitchange.services.intf.ScmsupplierlimitchangeManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
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
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("fadsuppliermapping-replacesupplierid")
@Transactional
public class ReplaceSupplierId implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ReplaceSupplierId.class);

    @Resource(name = "scmsupplierlimitchange-manager")
    private ScmsupplierlimitchangeManager scmsupplierlimitchangeManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        List<HashMap> uuid = (List<HashMap>) inMap.get("uuidList");
        List<String> uuidList = new ArrayList<>();
        for (int i = 0; i < uuid.size(); i++) {
            String uuidss = (String) (uuid.get(i).get("uuid"));
            uuidList.add(uuidss);
        }
        String supplierid = (String) inMap.get("supplieid");
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        if (ListUtil.isNotEmpty(uuid)) {
            Map conditionMap = new HashMap<>();
            conditionMap.put("uuid", uuidList);
            List fadSupplierMappingDtoList = DtoHelper.findByAnyFields(FadSupplierMappingDto.class, conditionMap, null);
            List<BasePojo> dtoUpdateObjs = new ArrayList<BasePojo>();
            for (Object o : fadSupplierMappingDtoList) {
                FadSupplierMappingDto dto = (FadSupplierMappingDto) o;
                dto.setSupplierToUuid(supplierid);
                dto.setEditflag("*");
                dtoUpdateObjs.add(dto);

            }
            DtoHelper.batchUpdate(dtoUpdateObjs, FadSupplierMapping.class);
        }
        return null;
    }
}
