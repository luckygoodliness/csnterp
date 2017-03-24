package com.csnt.scdp.bizmodules.modules.prm.problem.services.impl;

import com.csnt.scdp.bizmodules.entity.prm.PrmBrief;
import com.csnt.scdp.bizmodules.entity.prm.PrmProblem;
import com.csnt.scdp.bizmodules.entity.prm.PrmProblemFeedback;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProblemAttribute;
import com.csnt.scdp.bizmodules.modules.prm.problem.services.intf.ProblemManager;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  ProblemManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 19:30:04
 */

@Scope("singleton")
@Service("problem-manager")
public class ProblemManagerImpl implements ProblemManager {
    @Override
    public void setExtraData(Map outMap) {
        Map dtoMap = (Map) outMap.get("prmProblemDto");
        if (dtoMap != null) {
            //1.设置项目名的显示
            String projectName = "";
            String prmProjectMainId = (String)dtoMap.get("prmProjectMainId");
            PrmProjectMain prmProjectMain = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, prmProjectMainId);
            if(prmProjectMain!=null){
                projectName = prmProjectMain.getProjectName();
            }
            //2.设置部门
            String departmentCode = (String) dtoMap.get("departmentCode");
            if (StringUtil.isNotEmpty(departmentCode)) {
                dtoMap.put("departmentCodeDesc", OrgHelper.getOrgNameByCode((String) dtoMap.get("departmentCode")));
            }
            dtoMap.put(PrmProblemAttribute.PROJECT_NAME, projectName);
            //设置提出人名的显示
            String postPersonName = "";
            String postPerson = (String)dtoMap.get("postPerson");
            if(postPerson!=null){
                Map paramMap = new HashMap<>();
                paramMap.put(ScdpUserAttribute.USER_ID, postPerson);
                List<ScdpUser> list = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, paramMap, null);
                if (ListUtil.isNotEmpty(list)) {
                    postPersonName = list.get(0).getUserName();
                }
            }
            dtoMap.put(PrmProblemAttribute.POST_PERSON_NAME, postPersonName);
           //设置协助人名的显示
            String providerName = "";
            String provider = (String)dtoMap.get("provider");
            if(provider!=null){
                Map paramMap = new HashMap<>();
                paramMap.put(ScdpUserAttribute.USER_ID, provider);
                List<ScdpUser> list = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, paramMap, null);
                if (ListUtil.isNotEmpty(list)) {
                    providerName = list.get(0).getUserName();
                }
            }
            dtoMap.put(PrmProblemAttribute.PROVIDER_NAME ,providerName);
            //设置Grid后续协助人名的显示
            List<PrmProblemFeedback> prmProblemFeedbackList = (List) dtoMap.get("prmProblemFeedbackDto");
            if (ListUtil.isNotEmpty(prmProblemFeedbackList)) {
                for (int i = 0; i < prmProblemFeedbackList.size(); i++) {
                    String dealPersonName = "";
                    String dealPerson =(String)((Map)prmProblemFeedbackList.get(i)).get("dealPerson");
                    if(dealPerson!=null){
                        Map paramMap = new HashMap<>();
                        paramMap.put(ScdpUserAttribute.USER_ID, dealPerson);
                        List<ScdpUser> list = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, paramMap, null);
                        if (ListUtil.isNotEmpty(list)) {
                            dealPersonName = list.get(0).getUserName();
                          }
                       }
                    ((Map) prmProblemFeedbackList.get(i)).put(PrmProblemAttribute.DEAL_PERSON_NAME ,dealPersonName);
                    }

                }
            //设置Grid创建人名的显示
            if (ListUtil.isNotEmpty(prmProblemFeedbackList)) {
                for (int i = 0; i < prmProblemFeedbackList.size(); i++) {
                    String createByName = "";
                    String createBy =(String)((Map)prmProblemFeedbackList.get(i)).get("createBy");
                    if(createBy!=null){
                        Map paramMap = new HashMap<>();
                        paramMap.put(ScdpUserAttribute.USER_ID, createBy);
                        List<ScdpUser> list = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, paramMap, null);
                        if (ListUtil.isNotEmpty(list)) {
                            createByName = list.get(0).getUserName();
                        }
                    }
                    ((Map) prmProblemFeedbackList.get(i)).put(PrmProblemAttribute.CREATE_BY_NAME ,createByName);
                }

            }
            }
        }
    //根据uuid获取问题申报数据
       @Override
       public PrmProblem getPrmProblemForUUID(String uuid){
           PrmProblem prmProblem = PersistenceFactory.getInstance().findByPK(PrmProblem.class, uuid);
           if (prmProblem!=null) {
               return prmProblem;
           }
           return null;
       }
    }

