package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.action;

import com.csnt.scdp.bizmodules.entity.prm.*;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmBudgetAccessoryCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmBudgetOutsourceCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmBudgetPrincipalCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmProjectMainCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf.PrmprojectmaincManager;
import com.csnt.scdp.framework.commonaction.crud.CommonAddAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-27 15:28:19
 */

@Scope("singleton")
@Controller("prmprojectmainc-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

    @Resource(name = "prmprojectmainc-manager")
    private PrmprojectmaincManager prmprojectmaincManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    @Override
    protected void beforeAction(BasePojo dtoObj) {
        //dtoObj 获取页面数据数据
        PrmProjectMainCDto prmProjectMainC = (PrmProjectMainCDto) dtoObj;
        String detailType = ((PrmProjectMainC) dtoObj).getDetailType();
        if (!prmprojectmaincManager.validateProjectName(prmProjectMainC)) {
            throw new BizException("项目名称或项目简称已经存在,添加失败！");
        }
        if (PrmProjectMainAttribute.PRM_DETAIL_ALL.equals(detailType)) {
            //校验序号是否重复
            List lstMessage = prmprojectmaincManager.validateSerialNumber(prmProjectMainC);
            if (ListUtil.isNotEmpty(lstMessage)) {
                throw new BizException(StringUtil.joinList(lstMessage, "\\n"));
            }
        }
        if (StringUtil.isNotEmpty(prmProjectMainC.getPrmProjectMainId())) {
            PrmProjectMain main = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, prmProjectMainC.getPrmProjectMainId());
            if (main != null) {
                if (main.getIsPreProject() == 0 && prmProjectMainC.getIsPreProject() == 1) {
                    throw new BizException("已立项项目不能改为预立项！");
                }
            }
        }
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        //dtoObj 获取页面数据数据
        PrmProjectMainCDto prmProjectMainC = (PrmProjectMainCDto) dtoObj;

        List<PrmBudgetOutsourceCDto> outsourceCDto = prmProjectMainC.getPrmBudgetOutsourceCDto();
        if (ListUtil.isNotEmpty(outsourceCDto)) {
            List<PrmBudgetOutsourceCDto> outsourceSplitCDto =
                    outsourceCDto.stream().filter(t -> StringUtil.isNotEmpty(t.getSplitFromUuidNo())).collect(Collectors.toList());

            if (ListUtil.isNotEmpty(outsourceSplitCDto)) {
                Map<String, List<PrmBudgetOutsourceCDto>> splitOutsourceMap =
                        outsourceCDto.stream().filter(t -> StringUtil.isEmpty(t.getSplitFromUuidNo()) && StringUtil.isNotEmpty(t.getSerialNumber()))
                                .collect(Collectors.groupingBy(PrmBudgetOutsourceCDto::getSerialNumber));

                outsourceSplitCDto.stream().forEach(t -> {
                    if (splitOutsourceMap.containsKey(t.getSplitFromUuidNo())) {
                        PrmBudgetOutsourceC c = PersistenceFactory.getInstance().findByPK(PrmBudgetOutsourceC.class, t.getUuid());
                        c.setSplitFromUuid(splitOutsourceMap.get(t.getSplitFromUuidNo()).get(0).getUuid());
                        PersistenceFactory.getInstance().update(c);
                    }
                });
            }
        }

        List<PrmBudgetPrincipalCDto> principalCDto = prmProjectMainC.getPrmBudgetPrincipalCDto();
        if (ListUtil.isNotEmpty(principalCDto)) {
            List<PrmBudgetPrincipalCDto> principalSplitCDto =
                    principalCDto.stream().filter(t -> StringUtil.isNotEmpty(t.getSplitFromUuidNo())).collect(Collectors.toList());

            if (ListUtil.isNotEmpty(principalSplitCDto)) {
                Map<String, List<PrmBudgetPrincipalCDto>> splitPrincipalMap =
                        principalCDto.stream().filter(t -> StringUtil.isEmpty(t.getSplitFromUuidNo()) && StringUtil.isNotEmpty(t.getSerialNumber()))
                                .collect(Collectors.groupingBy(PrmBudgetPrincipalCDto::getSerialNumber));

                principalSplitCDto.stream().forEach(t -> {
                    if (splitPrincipalMap.containsKey(t.getSplitFromUuidNo())) {
                        PrmBudgetPrincipalC c = PersistenceFactory.getInstance().findByPK(PrmBudgetPrincipalC.class, t.getUuid());
                        c.setSplitFromUuid(splitPrincipalMap.get(t.getSplitFromUuidNo()).get(0).getUuid());
                        PersistenceFactory.getInstance().update(c);
                    }
                });
            }
        }

        List<PrmBudgetAccessoryCDto> accessoryCDto = prmProjectMainC.getPrmBudgetAccessoryCDto();
        if (ListUtil.isNotEmpty(accessoryCDto)) {
            List<PrmBudgetAccessoryCDto> accessorySplitCDto =
                    accessoryCDto.stream().filter(t -> StringUtil.isNotEmpty(t.getSplitFromUuidNo())).collect(Collectors.toList());

            if (ListUtil.isNotEmpty(accessorySplitCDto)) {
                Map<String, List<PrmBudgetAccessoryCDto>> splitAccessoryMap =
                        accessoryCDto.stream().filter(t -> StringUtil.isEmpty(t.getSplitFromUuidNo()) && StringUtil.isNotEmpty(t.getSerialNumber()))
                                .collect(Collectors.groupingBy(PrmBudgetAccessoryCDto::getSerialNumber));

                accessorySplitCDto.stream().forEach(t -> {
                    if (splitAccessoryMap.containsKey(t.getSplitFromUuidNo())) {
                        PrmBudgetAccessoryC c = PersistenceFactory.getInstance().findByPK(PrmBudgetAccessoryC.class, t.getUuid());
                        c.setSplitFromUuid(splitAccessoryMap.get(t.getSplitFromUuidNo()).get(0).getUuid());
                        PersistenceFactory.getInstance().update(c);
                    }
                });
            }
        }
    }

}
