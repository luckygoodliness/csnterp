package com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.action;

import com.csnt.scdp.bizmodules.attributes.ExpandFieldName;
import com.csnt.scdp.bizmodules.attributes.ExpandFieldType;
import com.csnt.scdp.bizmodules.entity.nonprm.FinancialSubject;
import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.FinancialSubjectAttribute;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectSubjectSubjectAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractChangeAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpExpandFieldHelper;
import com.csnt.scdp.bizmodules.modules.nonprm.financialsubject.services.intf.FinancialsubjectManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.JsonUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf.SubjectsubjectManager;

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
 * @timestamp 2015-09-23 18:59:14
 */

@Scope("singleton")
@Controller("subjectsubject-add")
@Transactional
public class AddAction extends CommonAddAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);


    @Resource(name = "subjectsubject-manager")
    private SubjectsubjectManager subjectsubjectManager;

    @Override
    protected void beforeAction(BasePojo dtoObj) {
        //dtoObj 获取页面数据数据
        SubjectSubject subjectSubject = (SubjectSubject) dtoObj;

        if (subjectsubjectManager.checkDataUnique(subjectSubject.getOfficeId(),
                subjectSubject.getFinancialSubject())) {
            return;
        } else {
            throw new BizException("添加失败，数据库已有记录", new HashMap());
        }
    }

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        //获取费用名称和组织代码
        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);

        BasePojo dtoObj = DtoHelper.getDtoFromMap(viewMap, dtoClass);
        SubjectSubject subjectSubject = (SubjectSubject) dtoObj;
        //获取到费用名称
        String fsName = subjectSubject.getFinancialSubject();
        //根据费用名称获取标识（内容分配）
        Map<String, Object> mapConditions = new HashMap<String, Object>();
        mapConditions.put(FinancialSubjectAttribute.SUBJECT_NAME, fsName);
        List<FinancialSubject> lstFinancialSubject = PersistenceFactory.getInstance().
                findByAnyFields(FinancialSubject.class, mapConditions, null);

        Map mapInput = null;
        String financialSubjectCode = "";
        if (ListUtil.isEmpty(lstFinancialSubject)) {
            throw new BizException("预算名称不存在，请联系管理员！");
        }
        String subjectNo = lstFinancialSubject.get(0).getSubjectNo();
        Integer seqNo = lstFinancialSubject.get(0).getSeqNo();
        // 获取组织的预算代码
        String officeMark = ErpExpandFieldHelper.getExpandFieldValue(ExpandFieldType.ORG, OrgHelper.getOrgIdByCode(subjectSubject.getOfficeId()), ExpandFieldName.BUGDET_CODE);
        //拼成费用代码
        financialSubjectCode = officeMark + "-" + subjectNo;
        String mainTabName = StringUtil.capFirst(BeanFactory.getClass(dtoClass).getSimpleName(), false);
        mapInput = (Map) viewMap.get(mainTabName);
        mapInput.put("financialSubjectCode", financialSubjectCode);
        mapInput.put("seqNo", seqNo);
        //Do before 以上设置部门非项目科目代码
        Map out = super.perform(inMap);
        out.put("financialSubjectCode", financialSubjectCode);
        //Do After
        return out;
    }
}
