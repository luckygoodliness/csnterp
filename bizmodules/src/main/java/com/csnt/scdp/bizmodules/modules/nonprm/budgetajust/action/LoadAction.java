package com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.action;

import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.bizmodules.modules.common.action.ErpLoadAction;
import com.csnt.scdp.bizmodules.modules.nonprm.budget.services.intf.BudgetManager;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustCDDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustCDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.dto.NonProjectBudgetAjustHDto;
import com.csnt.scdp.bizmodules.modules.nonprm.budgetajust.services.intf.BudgetajustManager;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf.SubjectsubjectManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 16:43:12
 */

@Scope("singleton")
@Controller("budgetajust-load")
@Transactional
public class LoadAction extends ErpLoadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

    @Resource(name = "budgetajust-manager")
    private BudgetajustManager budgetajustManager;

    @Resource(name = "budget-manager")
    private BudgetManager budgetManager;

    @Resource(name = "subjectsubject-manager")
    private SubjectsubjectManager subjectsubjectManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        Map out = super.perform(inMap);
        Map dtoMap = (Map) out.get("nonProjectBudgetAjustHDto");
        if (dtoMap != null) {
            String orgCode = (String) dtoMap.get("officeId");
            if(orgCode!=null){
                String orgName = OrgHelper.getOrgNameByCode(orgCode);
                dtoMap.put("officeIdDesc", orgName);
            }
        }
        return out;
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        NonProjectBudgetAjustHDto hdto = (NonProjectBudgetAjustHDto) dtoObj;

        List lstAjustCdto = hdto.getNonProjectBudgetAjustCDto();
        hdto.setNonProjectBudgetAjustCDDto(new ArrayList<NonProjectBudgetAjustCDDto>());
        hdto.setNonProjectBudgetAjustCD2Dto(new ArrayList<NonProjectBudgetAjustCDDto>());
        if (ListUtil.isNotEmpty(lstAjustCdto)) {
            for (int i = 0; i < lstAjustCdto.size(); i++) {
                NonProjectBudgetAjustCDto budgetAjustCDto = (NonProjectBudgetAjustCDto) lstAjustCdto.get(i);
                //获取费用代码 存入数据库的是SubjectSubject表的uuid
                String fsCode = budgetAjustCDto.getFinancialSubjectCode();
                //fsCode 作为 SubjectSubject模块的 uuid查找费用科目代号和费用名称
                SubjectSubject sub = PersistenceFactory.getInstance()
                        .findByPK(SubjectSubject.class, fsCode);
                if (sub != null) {
                    String financialName = sub.getFinancialSubject();
                    String financialSubjectCode = sub.getFinancialSubjectCode();
                    budgetAjustCDto.setSubjectCode(financialSubjectCode);
                    budgetAjustCDto.setSubjectName(financialName);
                    if ("管理费用".equals(financialName)) {
                        String cid = budgetAjustCDto.getUuid();
                        List<NonProjectBudgetAjustCDDto> lstBudgetAjustCD = (List) budgetajustManager.getObjsByCid(cid);
                        hdto.setNonProjectBudgetAjustCDDto(lstBudgetAjustCD);
                    }
                    if ("固定资产添置".equals(financialName)) {
                        String cid = budgetAjustCDto.getUuid();
                        List<NonProjectBudgetAjustCDDto> lstBudgetAjustCD2 = (List) budgetajustManager.getObjsByCid(cid);
                        hdto.setNonProjectBudgetAjustCD2Dto(lstBudgetAjustCD2);
                    }
                }
            }
        }
    }
}
