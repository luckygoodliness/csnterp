package com.csnt.scdp.bizmodules.modules.nonprm.financialsubject.services.intf;

import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * Description:  FinancialsubjectManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 10:37:44
 */

@Scope("singleton")
@Service("financialsubject-financialsubjectmanager")
@Transactional
public interface FinancialsubjectManager {
    //判断费用名称是否被引用，是则不删除
    public boolean checkDelete(String uuid);

    //将费用名称和部门插入到数据库
    public boolean  save(SubjectSubject subject);

    List<Map<String, Object>> getPrmSubjectCodes();
}