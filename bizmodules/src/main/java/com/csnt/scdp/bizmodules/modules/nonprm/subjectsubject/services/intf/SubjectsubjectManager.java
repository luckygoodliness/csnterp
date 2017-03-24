package com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.services.intf;

import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Description:  SubjectsubjectManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 18:59:15
 */
@Scope("singleton")
@Service("subjectSubject-subjectSubjectmanager")
@Transactional
public interface SubjectsubjectManager {
    //判断费用名称是否被引用，是则不删除
    public boolean checkDelete(String uuid);

    /**
     * 检查数据在数据库是否存在
     *
     * @param officeId         部门id
     * @param financialSubject 费用名称
     * @return 数据库中是否存在该费用名称和部门id 是则返false  否则true
     */
    public boolean checkDataUnique(String officeId, String financialSubject);

    /**
     * 根据费用代码查找对象
     *
     * @param fsCode
     * @return
     */
    public List<SubjectSubject> getObjByFSCode(String fsCode);

    /**
     * 根据部门代码查找对象
     *
     * @param orgCode
     * @return
     */
    public List<ScdpOrg> getObjsByOrgCode(String orgCode);

    /**
     * 获取组织代码
     *
     * @param uuid 部门编号
     * @return 组织代码
     */
    public String getOrgCode(String uuid);

    /**
     * @param mapCondition
     * @return
     */
    public List<SubjectSubject> getObjects(Map<String, Object> mapCondition);

    /**
     * 根据部门ID查找对象
     *
     * @param officeId 部门ID
     * @return subjectSubject对象
     */
    public List<SubjectSubject> getObjsByOfficeId(String officeId);

    List<SubjectSubject> getOfficeFixedSubject(String officeId, String fixedKey, String value);

    /**
     * 根据费用名称查找对象
     *
     * @param subjectName
     * @return
     */
    public List<SubjectSubject> getObjsBySubjectName(String subjectName);


}