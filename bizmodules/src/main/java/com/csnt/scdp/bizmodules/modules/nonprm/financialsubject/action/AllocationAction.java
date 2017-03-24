package com.csnt.scdp.bizmodules.modules.nonprm.financialsubject.action;

import com.csnt.scdp.bizmodules.attributes.ExpandFieldName;
import com.csnt.scdp.bizmodules.attributes.ExpandFieldType;
import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.bizmodules.entityattributes.nonprm.NonProjectSubjectSubjectAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpExpandFieldHelper;
import com.csnt.scdp.bizmodules.modules.nonprm.financialsubject.services.intf.FinancialsubjectManager;
import com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf.SubjectsubjectManager;
import com.csnt.scdp.framework.commonaction.crud.CommonLoadAction;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Reese on 2015/10/8.
 */

@Scope("singleton")
@Controller("finalcialsubject-allocationAction")
@Transactional
//分配
public class AllocationAction extends CommonLoadAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(AllocationAction.class);

    @Resource(name = "financialsubject-manager")
    private FinancialsubjectManager financialsubjectManager;

    @Resource(name = "subjectsubject-manager")
    private SubjectsubjectManager subjectsubjectManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {

        List<Map> subjectlst = (List) inMap.get("subjectlst");//接收费用名称
        List deptlst = (List) inMap.get("deptlst");//接收部门编号

        if (subjectlst.size() > 0 && deptlst.size() > 0) {
            for (int i = 0; i < subjectlst.size(); i++) {
                for (int j = 0; j < deptlst.size(); j++) {
                    String subjectType = subjectlst.get(i).get("financialCombo").toString();
                    if ("非项目".equals(subjectType)) {
                        //费用代码
                        String fsCode = (subjectlst.get(i)).get("subjectNo").toString();
                        String fsName = subjectlst.get(i).get("subjectName").toString();
                        String officeMark = ErpExpandFieldHelper.getExpandFieldValue(ExpandFieldType.ORG, deptlst.get(j).toString(), ExpandFieldName.BUGDET_CODE);

                        String financialSubjectCode = officeMark + "-" + fsCode;

                        SubjectSubject subject = new SubjectSubject();
                        subject.setFinancialSubject(fsName);
                        subject.setIsActive(new Integer("1"));
                        subject.setOfficeId(OrgHelper.getOrgCodeById(deptlst.get(j).toString()));
                        subject.setFinancialSubjectCode(financialSubjectCode);
                        Object seq = (subjectlst.get(i)).get("seqNo");
                        if (seq != null) {
                            Integer s = new Integer(seq.toString());
                            subject.setSeqNo(s);
                        }

                        //判断这个费用是否已经分配给该部门，若已经分配，则不作分配
                        Map<String, Object> mapConditions = new HashMap<String, Object>();
                        mapConditions.put(NonProjectSubjectSubjectAttribute.FINANCIAL_SUBJECT_CODE, financialSubjectCode);
                        List<SubjectSubject> lstObj = subjectsubjectManager.getObjects(mapConditions);
                        if (ListUtil.isEmpty(lstObj)) {
                            //将数据插入数据库
                            financialsubjectManager.save(subject);
                        }
                    }
                }
            }
        } else {
            throw new BizException("请选择一条记录！", new HashMap());
        }
        return null;
    }
}
