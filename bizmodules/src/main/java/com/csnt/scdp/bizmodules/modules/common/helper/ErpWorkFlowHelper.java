package com.csnt.scdp.bizmodules.modules.common.helper;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.csnt.scdp.bizmodules.attributes.ErpWorkFlowAttribute;
import com.csnt.scdp.bizmodules.entity.mobile.MobileRegInfo;
import com.csnt.scdp.bizmodules.entityattributes.mobile.MobileRegInfoAttribute;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.core.workflow.intf.IWorkFlow;
import com.csnt.scdp.framework.helper.DBHelper;
import com.csnt.scdp.framework.helper.SysconfigHelper;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.*;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpRoleAttribute;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.helper.OnlineUserHelper;
import com.csnt.scdp.sysmodules.helper.OrgHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import com.csnt.scdp.sysmodules.modules.sys.org.dto.ScdpOrgTreeDto;
import org.apache.commons.collections.map.HashedMap;
import org.apache.cxf.common.util.CacheMap;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:  ErpWorkFlowHelper
 * Copyright: © 2015 Aardwolf Studio. All rights reserved.
 * Company:CSNT中海网络科技股份有限公司
 *
 * @author Jia Baiqiang
 * @version 1.0
 */
public class ErpWorkFlowHelper {
    public static CacheMap<String, Map> roleUserInfoMap = new CacheMap();
    private static ILogTracer tracer = LogTracerFactory.getInstance(ErpWorkFlowHelper.class);

    //根据用户去查询各种角色对应用户对应信息
    public static Map loadUserInfoByUserID(Map inMap) {
        String userId = "";
        String orgUuid = "";
        String companyUuid = "";
        String deptCode = (String) inMap.get(ErpWorkFlowAttribute.PROCESS_DEPT_CODE);
        if (StringUtil.isNotEmpty(deptCode)) {
            orgUuid = OrgHelper.getOrgIdByCode(deptCode);
            ScdpOrgTreeDto orgTreeDto = OrgHelper.getCompanyByDepartUuid(orgUuid);
            companyUuid = orgTreeDto.getUuid();
        } else {
            userId = (String) inMap.get(ErpWorkFlowAttribute.WF_USER_ID);
            String processInstanceId = (String) inMap.get(ErpWorkFlowAttribute.PROCESS_INSTANCE_ID);
            if (StringUtil.isEmpty(userId) && StringUtil.isNotEmpty(processInstanceId)) {//根据发起人来取对应的角色
                IWorkFlow workFlowImpl = BeanFactory.getBean(CommonAttribute.WORK_FLOW_IMPL);
                userId = workFlowImpl.getStartUserId(processInstanceId);
            }
            if (StringUtil.isEmpty(userId)) {//取当前用户
                userId = UserHelper.getUserId();
                orgUuid = UserHelper.getOrgUuid();
                deptCode = UserHelper.getOrgCode();
                companyUuid = UserHelper.getCompanyUuid();
            } else {//用户不是当前用户先查询用户的companyuuid及dept uuid
                String sql = "select distinct suer.uuid,suer.company_uuid, sorg.org_code, sorg.org_type, sorg.org_name, suer.org_uuid\n" +
                        "  from scdp_user suer\n" +
                        "  left join scdp_org sorg on suer.org_uuid = sorg.uuid\n" +
                        "  where suer.user_id = ?";
                DAOMeta daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                List lstParam = new ArrayList();
                lstParam.add(userId);
                daoMeta.setLstParams(lstParam);
                List lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
                orgUuid = (String) ((Map) lstUserInfo.get(0)).get(ErpWorkFlowAttribute.ORG_UUID);
                deptCode = (String) ((Map) lstUserInfo.get(0)).get(ErpWorkFlowAttribute.ORG_CODE);
                companyUuid = (String) ((Map) lstUserInfo.get(0)).get(ErpWorkFlowAttribute.COMPANY_UUID);
            }
        }
        roleUserInfoMap.clear();
        return loadAllCompanyUserRoleInfo(orgUuid, companyUuid, deptCode);
    }

    private static Map loadAllCompanyUserRoleInfo(String orgUuid, String companyUuid, String deptCode) {
        Map resultMap = new HashMap();
        resultMap.putAll(loadCompanyUserRoleInfo(companyUuid));//查询公司level信息 查询结果：公司分管领导，部门领导，管理部门角色用户
        resultMap.putAll(loadDeptInfoByUserDept(orgUuid, deptCode, resultMap));//查询部门level信息
        return resultMap;
    }

    //load company leader(公司领导) info
    private static Map loadCompanyUserRoleInfo(String companyUuid) {
        Map mapCompanyUsers = loadCompanyUserByRoles();//查询公司相关角色对应用户userid   查询管理部门所有用户
        Map companyVp = loadOtherDivisionVp();//查询非事业部门分管领导  查询公司分管领导
        if (MapUtil.isNotEmpty(companyVp)) {
            mapCompanyUsers.putAll(companyVp);
        }
        if (StringUtil.isNotEmpty(mapCompanyUsers)) {
            roleUserInfoMap.put(companyUuid, mapCompanyUsers);
        }
        return mapCompanyUsers;//汇总的公司级别的用户信息
    }

    //load business division(事业部) info
    public static Map loadBusinessDivisionInfo(String orgUuid, String deptCode) {
        if (roleUserInfoMap.containsKey(orgUuid)) {
            return roleUserInfoMap.get(orgUuid);
        } else {
            String role1 = ErpWorkFlowAttribute.BZ_ZJL;
            String role2 = ErpWorkFlowAttribute.BZ_FZJL;
            String role3 = ErpWorkFlowAttribute.BZ_ZG;
            Map mapDeptUsers = loadUserByDeptUuidAndRole(orgUuid, null);
            String deptVp = "";
            if (mapDeptUsers.containsKey(role1)) {
                deptVp = deptVp + ErpWorkFlowAttribute.COMMA + mapDeptUsers.get(role1);
            }
            if (mapDeptUsers.containsKey(role2)) {
                deptVp = deptVp + ErpWorkFlowAttribute.COMMA + mapDeptUsers.get(role2);
            }
            if (mapDeptUsers.containsKey(role3)) {
                deptVp = deptVp + ErpWorkFlowAttribute.COMMA + mapDeptUsers.get(role3);
            }
            deptVp = deptVp.replaceFirst(ErpWorkFlowAttribute.COMMA, "");
            if (StringUtil.isNotEmpty(deptVp)) {
                mapDeptUsers.put(ErpWorkFlowAttribute.LEADER_BZ, deptVp);
            }
            String vp = loadBizDivisionVP(deptCode);//查询事业部分管领导
            if (StringUtil.isNotEmpty(vp)) {
                mapDeptUsers.put(ErpWorkFlowAttribute.VP_BZ, deptVp);
                mapDeptUsers.put(ErpWorkFlowAttribute.VP, vp);
            }
            if (MapUtil.isNotEmpty(mapDeptUsers)) {
                roleUserInfoMap.put(orgUuid, mapDeptUsers);
            }
            return mapDeptUsers;
        }
    }

    //load manager division(管理部门) info
    private static Map loadanagerDivisionInfo(String orgUuid, String deptCode) {
        if (roleUserInfoMap.containsKey(orgUuid)) {
            return roleUserInfoMap.get(orgUuid);
        } else {
            return loadDeptUserInfoAndDeptLeader(orgUuid, deptCode);
        }
    }

    //查询部门相关角色对应用户及部门领导
    private static Map loadDeptUserInfoAndDeptLeader(String orgUuid, String deptCode) {
        Map mapDeptUsers = loadUserByDeptUuidAndRole(orgUuid, deptCode);
        if (MapUtil.isNotEmpty(mapDeptUsers)) {
            roleUserInfoMap.put(orgUuid, mapDeptUsers);
        }
        return mapDeptUsers;
    }

    //load handler division leader(归口部门领导)
    //从《非项目管理-》非项目费用内容分配》这个取出
    public static List loadHandlerDivisionLeader(List lstFeeType) {
        //1. 固定资产,低值易耗品,无形资产-->资产部
        //2. 培训,劳务-->人力资源部
        //3. 管理部门归口会务费-->总经办
        //4. 事业部归口会务费-->运管部
        if (ListUtil.isNotEmpty(lstFeeType)) {
            String sql = "select distinct nps.office_id, org.uuid from non_project_subject_subject nps " +
                    "left join scdp_org org on nps.office_id = org.org_code where nps.financial_subject_code in(" + StringUtil.joinForSqlIn(lstFeeType, ErpWorkFlowAttribute.COMMA) + ")";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            List lstParam = new ArrayList();
            daoMeta.setLstParams(lstParam);
            List lstTempHandlerDept = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if (ListUtil.isNotEmpty(lstTempHandlerDept)) {
                List lstHandlerDepts = new ArrayList();
                for (Iterator it = lstTempHandlerDept.iterator(); it.hasNext(); ) {
                    Map mapDept = (Map) it.next();
                    String orgUuid = (String) mapDept.get(com.csnt.scdp.bizmodules.attributes.CommonAttribute.UUID);
                    if (roleUserInfoMap.containsKey(orgUuid)) {
                        String leader = (String) ((Map) roleUserInfoMap.get(orgUuid)).get(ErpWorkFlowAttribute.DEPT_LEADER);
                        lstHandlerDepts.add(leader);
                    } else {
                        String deptCode = (String) mapDept.get(ErpWorkFlowAttribute.OFFICE_ID);
                        if (StringUtil.isNotEmpty(deptCode)) {
                            String leader = (String) loadDeptInfoByUserDept(orgUuid, deptCode, null).get(ErpWorkFlowAttribute.DEPT_LEADER);
                            lstHandlerDepts.add(leader);
                        }
                    }
                }
                return lstHandlerDepts;
            }
        }
        return null;
    }

    //load biz division VP(公司事业部分管领导)
    private static String loadBizDivisionVP(String deptCode) {
        //分管领导的关系配置在基础代码表里面
//        1、杨忆明：交通1、2、3部、交通信息化部、产品分公司
//        2、林亦雯：航运1、2部、运维部、资产部
//        3、瞿辉：供应链部
//        4、吴琦：计财部
//        5、高庆：安防部、运管部
//        6、周晓梅：人力资源部、自动化部
        String vp = FMCodeHelper.getDescByCode(deptCode, ErpWorkFlowAttribute.COMPANY_VP);
        return vp;
    }

    //load dept leader, 如果一个leader既是公司领导也是部门领导的时候，可以在基础代码表里面加以设置，用下面的方法去取
    //如果不是兼任的这种，不需要做这种设置的
    private static String loadDeptLeader(String deptCode) {
        //如果一个leader既是公司领导也是部门领导的时候，可以在基础代码表里面加以设置，用下面的方法去取
        String leader = FMCodeHelper.getDescByCode(deptCode, ErpWorkFlowAttribute.DEPT_LEADER_CODE);
        return leader;
    }

    //用来设置哪些角色是部门领导
    private static List loadDeptLeaderMapping() {
        List lstResult = new ArrayList();
        String leader = FMCodeHelper.getDescByCode(ErpWorkFlowAttribute.DEPT_LEADER_ROLE, ErpWorkFlowAttribute.DEPT_LEADER_CODE);//查询各部门
        if (StringUtil.isNotEmpty(leader)) {
            lstResult = StringUtil.splitAsList(leader, ErpWorkFlowAttribute.COMMA);
        }
        return lstResult;
    }

    //查询非事业部公司分管领导
    private static Map loadOtherDivisionVp() {
        List lstDeptCodes = new ArrayList();
        lstDeptCodes.add(ErpWorkFlowAttribute.CSNT_JCB);//计财部
        lstDeptCodes.add(ErpWorkFlowAttribute.CSNT_ZCB);//资产部
        lstDeptCodes.add(ErpWorkFlowAttribute.CSNT_YGB);//运管部
        lstDeptCodes.add(ErpWorkFlowAttribute.CSNT_RLZYB);//人力资源部
        lstDeptCodes.add(ErpWorkFlowAttribute.CSNT_GYLB);//供应链部
        String sql = "select fmcode.sys_code, fmcode.code_desc from scdp_code fmcode where fmcode.code_type='COMPANY_VP' and fmcode.sys_code in (" + StringUtil.joinForSqlIn(lstDeptCodes, ErpWorkFlowAttribute.COMMA) + ")";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List lstParam = new ArrayList();
        daoMeta.setLstParams(lstParam);
        List lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        Map mapResult = new HashMap();
        for (Iterator<Map> it = lstUserInfo.iterator(); it.hasNext(); ) {
            Map mapUserInfo = it.next();
            String deptCode = (String) mapUserInfo.get(ErpWorkFlowAttribute.SYS_CODE);
            String desc = (String) mapUserInfo.get(ErpWorkFlowAttribute.CODE_DESC);
            if (StringUtil.isNotEmpty(desc)) {
                if (deptCode.equals(ErpWorkFlowAttribute.CSNT_JCB)) {//计财部
                    mapResult.put(ErpWorkFlowAttribute.VP_JCB, desc);
                } else if (deptCode.equals(ErpWorkFlowAttribute.CSNT_ZCB)) {//资产部
                    mapResult.put(ErpWorkFlowAttribute.VP_ZCB, desc);
                } else if (deptCode.equals(ErpWorkFlowAttribute.CSNT_YGB)) {//运营部
                    mapResult.put(ErpWorkFlowAttribute.VP_YGB, desc);
                } else if (deptCode.equals(ErpWorkFlowAttribute.CSNT_RLZYB)) {//人力资源部
                    mapResult.put(ErpWorkFlowAttribute.VP_HR, desc);
                } else if (deptCode.equals(ErpWorkFlowAttribute.CSNT_GYLB)) {//人力资源部
                    mapResult.put(ErpWorkFlowAttribute.VP_GYLB, desc);
                }
            }
        }
        return mapResult;
    }

    //根据部门代号去load信息
    private static Map loadDeptInfoByUserDept(String orgUuid, String deptCode, Map companyInfo) {
        Map returnMap = new HashMap();
        if (!ErpOrgHelper.isBizDivision(orgUuid)) {
            returnMap = loadanagerDivisionInfo(orgUuid, deptCode);
            if (deptCode.equals(ErpWorkFlowAttribute.CSNT_JCB)) {//计财部
                if (companyInfo != null && companyInfo.containsKey(ErpWorkFlowAttribute.VP_JCB)) {
                    returnMap.put(ErpWorkFlowAttribute.VP, companyInfo.get(ErpWorkFlowAttribute.VP_JCB));
                }
            } else if (deptCode.equals(ErpWorkFlowAttribute.CSNT_ZCB)) {//资产部
                if (companyInfo != null && companyInfo.containsKey(ErpWorkFlowAttribute.VP_ZCB)) {
                    returnMap.put(ErpWorkFlowAttribute.VP, companyInfo.get(ErpWorkFlowAttribute.VP_ZCB));
                }
            } else if (deptCode.equals(ErpWorkFlowAttribute.CSNT_YGB)) {//运营部
                if (companyInfo != null && companyInfo.containsKey(ErpWorkFlowAttribute.VP_YGB)) {
                    returnMap.put(ErpWorkFlowAttribute.VP, companyInfo.get(ErpWorkFlowAttribute.VP_YGB));
                }
            } else if (deptCode.equals(ErpWorkFlowAttribute.CSNT_RLZYB)) {//人力资源部
                if (companyInfo != null && companyInfo.containsKey(ErpWorkFlowAttribute.VP_HR)) {
                    returnMap.put(ErpWorkFlowAttribute.VP, companyInfo.get(ErpWorkFlowAttribute.VP_HR));
                }
            } else if (deptCode.equals(ErpWorkFlowAttribute.CSNT_GYLB)) {//供应链部
                if (companyInfo != null && companyInfo.containsKey(ErpWorkFlowAttribute.VP_GYLB)) {
                    returnMap.put(ErpWorkFlowAttribute.VP, companyInfo.get(ErpWorkFlowAttribute.VP_GYLB));
                }
            }
        } else {//事业部
            returnMap = loadBusinessDivisionInfo(orgUuid, deptCode);
        }
        //如果是公司领导兼任的，可以在这里处理
        if (!returnMap.containsKey(ErpWorkFlowAttribute.DEPT_LEADER) || StringUtil.isEmpty(returnMap.get(ErpWorkFlowAttribute.DEPT_LEADER))) {
            String leader = loadDeptLeader(deptCode);
            returnMap.put(ErpWorkFlowAttribute.DEPT_LEADER, companyInfo.get(leader));
        }
        return returnMap;
    }

    //根据部门uuid查询角色对应用户及部门领导
    private static Map loadUserByDeptUuidAndRole(String deptUuid, String deptCode) {
        List lstParam = new ArrayList();
        lstParam.add(deptUuid);
        String sql = "select distinct euser.user_id,erole.role_name\n" +
                "  from scdp_user euser\n" +
                "  left join v_user_roles erole on euser.user_id = erole.user_id\n" +
                "  where euser.is_active = 1\n" +
                "  and (euser.is_void = 0 or euser.is_void is null)\n" +
                "  and euser.org_uuid = ? order by erole.role_name";
        return loadUserByRoles(lstParam, sql, deptCode);
    }

    //查询公司角色对应用户
    private static Map loadCompanyUserByRoles() {
        List lstParam = new ArrayList();
        //查询公司管理部门所有 用户角色 对应
        String sql = "select distinct euser.user_id,erole.role_name\n" +
                " from scdp_user euser\n" +
                " left join v_user_roles erole on euser.user_id = erole.user_id\n" +
                " where euser.is_active = 1\n" +
                " and (euser.is_void = 0 or euser.is_void is null)\n" +
                " and euser.org_uuid in (select distinct r.data_uuid\n" +
                " from SCDP_EXPAND_COLUMN t\n" +
                " left join SCDP_EXPAND_ROW r on t.uuid = r.expand_id\n" +
                " where t.expand_type = 'ORG' and r.expand_value='管理部') order by erole.role_name";
        return loadUserByRoles(lstParam, sql, null);
    }

    //查询角色对应用户，并把指定的角色放到部门领导中   根据角色整理用户
    private static Map loadUserByRoles(List lstParam, String sql, String deptCode) {
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setLstParams(lstParam);
        List lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        Map mapDeptUsers = new HashMap();
        if (ListUtil.isNotEmpty(lstUserInfo)) {
            String strUserIds = "";
            String rolename = "";
            List lstDeptLeader = loadDeptLeaderMapping();//部门领导角色名字
            //循环用户角色，把角色用户一一对应变成一（角色）对多（用户）
            for (Iterator it = lstUserInfo.iterator(); it.hasNext(); ) {
                Map mapInfo = (Map) it.next();
                String tempRoleName = (String) mapInfo.get(ErpWorkFlowAttribute.ROLE_NAME);
                String userId = (String) mapInfo.get(ErpWorkFlowAttribute.USER_ID);

                if (StringUtil.isNotEmpty(rolename)) {
                    if (rolename.equals(tempRoleName)) {
                        strUserIds = strUserIds + ErpWorkFlowAttribute.COMMA + userId;
                    } else {
                        mapDeptUsers.put(rolename, strUserIds);
                        rolename = tempRoleName;
                        strUserIds = userId;
                    }
                } else {
                    rolename = tempRoleName;
                    strUserIds = userId;
                }
                //根据部门领导角色取到部门领导
                if (ListUtil.isNotEmpty(lstDeptLeader)) {
                    if (StringUtil.isNotEmpty(deptCode)) {
                        if (lstDeptLeader.contains(rolename + ("_" + deptCode))) {
                            mapDeptUsers.put(ErpWorkFlowAttribute.DEPT_LEADER, userId);//把部门领导放进来
                        }
                    } else if (lstDeptLeader.contains(rolename)) {
                        mapDeptUsers.put(ErpWorkFlowAttribute.DEPT_LEADER, userId);//把部门领导放进来
                    }
                }
            }
            if (StringUtil.isNotEmpty(rolename) && !mapDeptUsers.containsKey(rolename)) {
                mapDeptUsers.put(rolename, strUserIds);
            }
        }
        return mapDeptUsers;
    }

    public static Map filterRoles(Map inMap, Map mapRoles) {
//        Map result = new HashMap();
//        if (inMap.containsKey("userFilter") && MapUtil.isNotEmpty(mapRoles)) {
//            String userFilter = (String) inMap.get("userFilter");
//            if (StringUtil.isNotEmpty(userFilter) && userFilter.indexOf("rolename=") != -1) {
//                List lstAllRoles = searchAllRoles();
//                List lstUserFilters = StringUtil.splitAsList(userFilter, ";");
//                for (Iterator it = lstUserFilters.iterator(); it.hasNext(); ) {
//                    String tempFilter = (String) it.next();
//                    if (tempFilter.indexOf("rolename=") != -1) {
//                        tempFilter = StringUtil.replaceAll(tempFilter, "(currdept)", "");
//                        tempFilter = StringUtil.replaceAll(tempFilter, "rolename=(", "");
//                        tempFilter = StringUtil.replaceAll(tempFilter, ")", "");
//                        if (StringUtil.isNotEmpty(tempFilter)) {
//                            List filterRoles = StringUtil.splitAsList(tempFilter, ",");
//                            if (ListUtil.isNotEmpty(filterRoles)) {
//                                for (Iterator it1 = mapRoles.keySet().iterator(); it1.hasNext(); ) {
//                                    String key = (String) it1.next();
//                                    if (filterRoles.contains(key)) {
//                                        result.put(key, mapRoles.get(key));//如果是userFilter里面设置的，要带出来
//                                    } else if (!lstAllRoles.contains(key)) {
//                                        result.put(key, mapRoles.get(key));//如果不是userFitler里面设置的，但是是通过其它的查询出来的，也要带出来
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
        return mapRoles;
//        if (MapUtil.isEmpty(result)) {
//        }
//        return result;
    }

    private static List searchAllRoles() {
        List lstAllRoles = new ArrayList();
        String sql = "select distinct role_name from v_user_roles";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List lstParam = new ArrayList();
        daoMeta.setLstParams(lstParam);
        List lstSearchResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(lstSearchResult)) {
            for (Iterator it = lstSearchResult.iterator(); it.hasNext(); ) {
                Map resultMap = (Map) it.next();
                String roleName = (String) resultMap.get("roleName");
                lstAllRoles.add(roleName);
            }
        }
        return lstAllRoles;
    }

    public static String stateFlag(String state) {
        if (StringUtil.isNotEmpty(state)) {
            String stateDesc = FMCodeHelper.getDescByCode(state, "FAD_BILL_STATE");
            return "[" + stateDesc + "]";
        } else {
            return null;
        }

    }


    public static Map<String, List<String>> getAllRoleUserIds() {
        String sql = "select * from V_USER_ROLES where user_id is not null";
        DAOMeta meta = new DAOMeta(sql);
        List<Map<String, Object>> lstResult = PersistenceFactory.getInstance().findByNativeSQL(meta);
        return lstResult.stream().collect(Collectors.groupingBy(x -> (String) x.get(ScdpRoleAttribute.ROLE_NAME),
                Collectors.mapping(y -> (String) y.get(ScdpUserAttribute.USER_ID), Collectors.toList())));
    }

    public static List<String> parseAllAssignee(List<Map<String, Object>> lstTaskMap, Map<String, List<String>>
            roleUserIdMap) {
        Set<String> userIdSet = new HashSet<>();
        for (Map<String, Object> taskMap : lstTaskMap) {
            String assignee = (String) taskMap.get(WorkFlowAttribute.ASSIGNEE_LOWER);
            String roleName = (String) taskMap.get("groupId");
            String userId = (String) taskMap.get("userId");
            if (StringUtil.isNotEmpty(assignee)) {
                userIdSet.addAll(StringUtil.splitAsList(assignee, ","));
            }
            if (StringUtil.isNotEmpty(userId)) {
                userIdSet.addAll(StringUtil.splitAsList(userId, ","));
            }
            if (StringUtil.isNotEmpty(roleName)) {
                if (roleUserIdMap.containsKey(roleName)) {
                    userIdSet.addAll(roleUserIdMap.get(roleName));
                }
            }
        }
        return new ArrayList<>(userIdSet);
    }

    public static String getTaskName(String definitionKey, String businessKey, Map taskMap) {
        StringBuffer sb = new StringBuffer();
        String controllId = definitionKey.toLowerCase() + "-process-taskname";
        IAction action = null;
        try {
            action = (IAction) BeanFactory.getBean(controllId.toLowerCase(), new Object[0]);
            HashMap exception = new HashMap();
            exception.put(WorkFlowAttribute.BUSINESS_KEY, businessKey);
            exception.put(WorkFlowAttribute.START_USER, taskMap.get(WorkFlowAttribute.OWNER));
            Map result = action.perform(exception);
            sb.append(result.get(WorkFlowAttribute.NAME_LOWER));
        } catch (NoSuchBeanDefinitionException var12) {
            tracer.error(var12);
        }
        return sb.toString();
    }

    public static List<Map<String, Object>> getAllUnhandledTasks(Date startDate) {
        String sql = "select distinct arp.name_            title,\n" +
                "                arp.key_             definitionkey,\n" +
                "                pro.business_key_,\n" +
                "                pro.start_time_      createtime,\n" +
                "                pro.end_time_        endtime,\n" +
                "                histask.create_time_ starttime,\n" +
                "                histask.due_date_,\n" +
                "                histask.name_        task_name,\n" +
                "                histask.assignee_    assignee,\n" +
                "                histask.id_          id,\n" +
                "                histask.priority_    priority,\n" +
                "                ide.group_id_,\n" +
                "                ide.user_id_,\n" +
                "                suser.user_name      owner" +
                "  from act_ru_task histask\n" +
                "  left join ACT_HI_PROCINST pro\n" +
                "    on pro.id_ = histask.proc_inst_id_\n" +
                "  left join act_re_procdef arp\n" +
                "    on arp.id_ = pro.proc_def_id_\n" +
                "  left join act_hi_identitylink ide\n" +
                "    on histask.id_ = ide.task_id_\n" +
                "  left join scdp_user suser\n" +
                "    on suser.user_id = pro.start_user_id_" +
                " where 1 = 1\n" +
                "   and arp.key_ is not null\n" +
                "   and pro.business_key_ is not null\n" +
                "   and (histask.assignee_ is not null or\n" +
                "       (histask.assignee_ is null and\n" +
                "       (ide.type_ = 'candidate' and\n" +
                "       (ide.group_id_ is not null or ide.user_id_ is not null))))\n" +
                "   and histask.create_time_ < ?\n" +
                " order by arp.key_, pro.business_key_";
        List lstParam = new ArrayList<>();
        lstParam.add(startDate);

        DAOMeta daoMeta = new DAOMeta(sql, lstParam);
        List<Map<String, Object>> lstTask = PersistenceFactory.getInstance().findByNativeSQL(daoMeta,
                addScalarMapForQueryTask());
        return lstTask;
    }

    public static List<Map<String, Object>> getTaskByTaskId(String taskId) {
        String sql = "select distinct arp.name_            title,\n" +
                "                arp.key_             definitionkey,\n" +
                "                pro.business_key_,\n" +
                "                pro.start_time_      createtime,\n" +
                "                pro.end_time_        endtime,\n" +
                "                histask.create_time_ starttime,\n" +
                "                histask.due_date_,\n" +
                "                histask.name_        task_name,\n" +
                "                histask.assignee_    assignee,\n" +
                "                histask.id_          id,\n" +
                "                histask.priority_    priority,\n" +
                "                ide.group_id_,\n" +
                "                ide.user_id_,\n" +
                "                suser.user_name      owner" +
                "  from act_ru_task histask\n" +
                "  left join ACT_HI_PROCINST pro\n" +
                "    on pro.id_ = histask.proc_inst_id_\n" +
                "  left join act_re_procdef arp\n" +
                "    on arp.id_ = pro.proc_def_id_\n" +
                "  left join act_hi_identitylink ide\n" +
                "    on histask.id_ = ide.task_id_\n" +
                "  left join scdp_user suser\n" +
                "    on suser.user_id = pro.start_user_id_" +
                " where 1 = 1\n" +
                "   and arp.key_ is not null\n" +
                "   and pro.business_key_ is not null\n" +
                "   and (histask.assignee_ is not null or\n" +
                "       (histask.assignee_ is null and\n" +
                "       (ide.type_ = 'candidate' and\n" +
                "       (ide.group_id_ is not null or ide.user_id_ is not null))))\n" +
                "   and histask.id_ = ?\n" +
                " order by arp.key_, pro.business_key_";
        List lstParam = new ArrayList<>();
        lstParam.add(taskId);

        DAOMeta daoMeta = new DAOMeta(sql, lstParam);
        List<Map<String, Object>> lstTask = PersistenceFactory.getInstance().findByNativeSQL(daoMeta,
                addScalarMapForQueryTask());
        return lstTask;
    }

    public static List<Map<String, Object>> getTasksByBusinessKey(String definitionKey, String businessKey) {
        String sql = "select distinct arp.name_            title,\n" +
                "                arp.key_             definitionkey,\n" +
                "                pro.business_key_,\n" +
                "                pro.start_time_      createtime,\n" +
                "                pro.end_time_        endtime,\n" +
                "                histask.create_time_ starttime,\n" +
                "                histask.due_date_,\n" +
                "                histask.name_        task_name,\n" +
                "                histask.assignee_    assignee,\n" +
                "                histask.id_          id,\n" +
                "                histask.priority_    priority,\n" +
                "                ide.group_id_,\n" +
                "                ide.user_id_,\n" +
                "                suser.user_name      owner" +
                "  from act_ru_task histask\n" +
                "  left join ACT_HI_PROCINST pro\n" +
                "    on pro.id_ = histask.proc_inst_id_\n" +
                "  left join act_re_procdef arp\n" +
                "    on arp.id_ = pro.proc_def_id_\n" +
                "  left join act_hi_identitylink ide\n" +
                "    on histask.id_ = ide.task_id_\n" +
                "  left join scdp_user suser\n" +
                "    on suser.user_id = pro.start_user_id_" +
                " where 1 = 1\n" +
                "   and arp.key_ is not null\n" +
                "   and pro.business_key_ is not null\n" +
                "   and (histask.assignee_ is not null or\n" +
                "       (histask.assignee_ is null and\n" +
                "       (ide.type_ = 'candidate' and\n" +
                "       (ide.group_id_ is not null or ide.user_id_ is not null))))\n" +
                "   and arp.key_ = ?\n" +
                "   and pro.business_key_ = ?\n" +
                " order by arp.key_, pro.business_key_";
        List lstParam = new ArrayList<>();
        lstParam.add(definitionKey);
        lstParam.add(businessKey);

        DAOMeta daoMeta = new DAOMeta(sql, lstParam);
        List<Map<String, Object>> lstTask = PersistenceFactory.getInstance().findByNativeSQL(daoMeta,
                addScalarMapForQueryTask());
        return lstTask;
    }

    public static Map addScalarMapForQueryTask() {
        HashMap scalarMap = new HashMap();
        scalarMap.put(WorkFlowAttribute.TITLE, StandardBasicTypes.STRING);
        scalarMap.put(WorkFlowAttribute.DEFINTION_KEY, StandardBasicTypes.STRING);
        scalarMap.put(WorkFlowAttribute.BUSINESS_KEY_, StandardBasicTypes.STRING);
        scalarMap.put(WorkFlowAttribute.CREATETIME, StandardBasicTypes.TIMESTAMP);
        scalarMap.put(WorkFlowAttribute.ENDTIME, StandardBasicTypes.TIMESTAMP);
        scalarMap.put(WorkFlowAttribute.STARTTIME, StandardBasicTypes.TIMESTAMP);
        scalarMap.put(WorkFlowAttribute.DUE_DATE_, StandardBasicTypes.TIMESTAMP);
        scalarMap.put(WorkFlowAttribute.TASK_NAME_UPPER, StandardBasicTypes.STRING);
        scalarMap.put(WorkFlowAttribute.ASSIGNEE, StandardBasicTypes.STRING);
        scalarMap.put(WorkFlowAttribute.ID, StandardBasicTypes.STRING);
        scalarMap.put(WorkFlowAttribute.PRIORITY, StandardBasicTypes.INTEGER);
        scalarMap.put(WorkFlowAttribute.OWNER, StandardBasicTypes.STRING);
        scalarMap.put("group_id_", StandardBasicTypes.STRING);
        scalarMap.put("user_id_", StandardBasicTypes.STRING);
        return scalarMap;
    }

    public static Map addScalarMapForProcinst() {
        HashMap scalarMap = new HashMap();
        scalarMap.put(WorkFlowAttribute.PROC_INST_ID_, StandardBasicTypes.STRING);
        return scalarMap;
    }

    public static Map addScalarMapForTaskinst() {
        HashMap scalarMap = new HashMap();
        scalarMap.put(WorkFlowAttribute.ASSIGNEE_, StandardBasicTypes.STRING);
        scalarMap.put(WorkFlowAttribute.NAME_, StandardBasicTypes.STRING);
        return scalarMap;
    }

    public static Map addScalarMapForRuTask() {
        HashMap scalarMap = new HashMap();
        scalarMap.put(WorkFlowAttribute.ASSIGNEE_, StandardBasicTypes.STRING);
        return scalarMap;
    }

    public static void pushMsgToMobile(Map inMap) {
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        String subMethod = (String) inMap.get("subMethod");
        String msg = null;
        List<String> lstUserId = new ArrayList<>();
        if ("complete".equals(subMethod)) {
            String status = (String) inMap.get("status");
            if ("PROCESSING".equals(status)) {
                //已提交
                String userIds = (String) inMap.get("assignee");
                String[] userIdArray = userIds.split(";");
                lstUserId = Arrays.asList(userIdArray);
                msg = "您有新的代办事项,请及时处理!";
            } else if ("FIXED".equals(status)) {
                //已审核
            }
        } else if ("reject".equals(subMethod)) {
            Boolean closed = (Boolean) inMap.get("wf_status_closed");
            if (closed != null && closed) {
                //如果回到的是第一步，状态改成已退回
                lstUserId.add((String) inMap.get(WorkFlowAttribute.WF_REJECTION_MSG_RECEIVER));
            } else {
                //如果回到的不是第一步，状态改成已提交（退回）
                lstUserId.addAll((List) inMap.get(WorkFlowAttribute.WF_REJECTION_MSG_RECEIVERS));
            }
            msg = "您有代办事项被退回,请及时处理!";

        } else if ("start".equals(subMethod)) {
            //已提交
            String userIds = (String) inMap.get("assignee");
            String[] userIdArray = userIds.split(";");
            lstUserId = Arrays.asList(userIdArray);
            msg = "您有新的代办事项,请及时处理!";
        } else if ("cancel".equals(subMethod)) {
            Boolean closed = (Boolean) inMap.get("wf_status_closed");
            if (closed != null && closed) {
                //如果回到的是第一步，状态改成新增
            } else {
                //如果回到的不是第一步，状态改成提交
            }
        }

        try {
            List<String> devCodes = new ArrayList<>();
            if (ListUtil.isEmpty(lstUserId)) {
                return;
            } else {
                for (String userId : lstUserId) {
                    List<String> pusTokens = new ArrayList<>();
                    PersistenceCrudManager pcm = PersistenceFactory.getInstance();
                    Map condition = new HashMap<>();
                    condition.put(MobileRegInfoAttribute.LOGIN_USER_NAME, userId);
                    List<MobileRegInfo> mobileRegInfos = pcm.findByAnyFields(MobileRegInfo.class, condition, null);
                    if (ListUtil.isNotEmpty(mobileRegInfos)) {
                        for (MobileRegInfo mobileRegInfo : mobileRegInfos) {
                            pusTokens.add(mobileRegInfo.getRegId());
                        }
                    }
                    if (ListUtil.isNotEmpty(pusTokens)) {
                        for (String pusToken : pusTokens) {
                            Map pushMap=new HashMap<>();
                            pushMap.put("pushToken", pusToken);
                            String extraInfo =JsonUtil.serialize(pushMap);
                            String devCode = JsonUtil.accessMember(extraInfo, "pushToken", String.class);
                            devCodes.add(devCode);
                        }
                    } else {
                        return;
                    }
                }
            }
            String masterSecret = SysconfigHelper.getProperty("erp_mobile_masterSecret");
            String appKey = SysconfigHelper.getProperty("erp_mobile_appKey");
            JPushClient client = new JPushClient(masterSecret, appKey);
            PushPayload payload = PushPayload.newBuilder()
                    .setPlatform(Platform.all())
                    .setAudience(Audience.registrationId(devCodes))
                    .setNotification(Notification.newBuilder()
                            .addPlatformNotification(IosNotification.newBuilder()
                                    .setAlert(msg)
                                    .build())
                            .addPlatformNotification(AndroidNotification.newBuilder()
                                    .setAlert(msg)
                                    .build())
                            .build())
                    .setOptions(Options.newBuilder().setApnsProduction(true).build())
                    .build();

            try {
                PushResult result = client.sendPush(payload);
                tracer.info("Got result - " + result);

            } catch (APIConnectionException e) {
                // Connection error, should retry later
                tracer.error("Connection error, should retry later", e);

            } catch (APIRequestException e) {
                // Should review the error, and fix the request
                tracer.error("Should review the error, and fix the request", e);
                tracer.info("HTTP Status: " + e.getStatus());
                tracer.info("Error Code: " + e.getErrorCode());
                tracer.info("Error Message: " + e.getErrorMessage());
            }
        } catch (Exception e) {
            tracer.error(e);
            sendMsg(inMap, e);
        }


    }

    public static void sendMsg(Map inMap, Exception e) {
        String sql = "SELECT * FROM V_USER_ROLES T WHERE ROLE_NAME ='移动端消息管理员'";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List<Map<String, Object>> lstUserInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        List<String> userIdLst = new ArrayList<String>();
        ReceiptsMeta receiptsMeta = new ReceiptsMeta();
        if (ListUtil.isNotEmpty(lstUserInfo)) {
            for (int i = 0; i < lstUserInfo.size(); i++) {
                String userId = (String) lstUserInfo.get(i).get("userId");
                userIdLst.add(userId);
            }
            receiptsMeta.setLstSendToUserId(userIdLst);
        }
        String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
        String templateNo = "MOBILE_SEND_MESSAGE_ERROR";
        Map map = new HashMap<>();
        map.put("error", e);
        map.putAll(inMap);
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);
    }

    public static void sendMsgToRollback(Map inMap,List userList ) {
        ReceiptsMeta receiptsMeta = new ReceiptsMeta();
        receiptsMeta.setLstSendToUserId(userList);
        String msgType = ScdpMsgAttribute.MSG_TYPE_IMSG;
        String templateNo = "WF_REJECTION_MSG";
        Map map = new HashMap<>();
        map.putAll(inMap);
        MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);
    }

}