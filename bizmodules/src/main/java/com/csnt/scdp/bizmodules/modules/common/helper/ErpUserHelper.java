package com.csnt.scdp.bizmodules.modules.common.helper;

import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by fisher on 2015/11/14.
 */
public class ErpUserHelper {
    public static ScdpUser getScdpUserByUserId(String userId) {
        Map mapCon = new HashMap();
        mapCon.put(ScdpUserAttribute.USER_ID, userId);
        List<ScdpUser> lst = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, mapCon, null);
        if (ListUtil.isNotEmpty(lst)) {
            return lst.get(0);
        } else {
            return null;
        }
    }

    //确认用户是否是事业部经理
    public static Boolean isSelfBizDivisionVp(String userId) {
        String sql = "select * from   v_user_roles t where  t.role_name = '事业部总经理' and    t.user_id ='" + userId + "'";
        DAOMeta daoMeta = new DAOMeta(sql);
        Integer count = PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta);
        return count > 0;
    }

    //确认用户是否是事业部经理
    public static Boolean isSelfManageDivisionVp(String userId) {
        String sql = "select * from   v_user_roles t" +
                " where  t.role_name in ('资产部主任','运管部主任','计财部主任','人力资源部主任','供应链部主任','总经办主任')" +
                " and    t.user_id ='" + userId + "'";
        DAOMeta daoMeta = new DAOMeta(sql);
        Integer count = PersistenceFactory.getInstance().findCountByNativeSQL(daoMeta);
        return count > 0;
    }

    /**
     * 获取用户为项目经理的项目信息
     *
     * @param userId 用户代码
     * @return
     */
    public static List<String> getChargedPrmProjectMainByUserId(String userId) {
        String sql = " select distinct t.prm_project_main_id\n" +
                " from   prm_member_detail_p t\n" +
                " left   join (select *\n" +
                "             from   scdp_role r\n" +
                "             where 1=1) s\n" +
                " on     t.post = s.uuid\n" +
                " where  t.prm_project_main_id is not null\n" +
                " and    t.staff_id = '" + userId + "'";
        DAOMeta daoMeta = new DAOMeta(sql);
        return PersistenceFactory.getInstance().findByNativeSQL(daoMeta).stream()
                .map(x -> x.get("prmProjectMainId").toString())
                .collect(Collectors.toList());
    }

    public static Map<String, ScdpUser> findUserByUserIds(List userIds) {
        Map<String, ScdpUser> out = new HashMap<String, ScdpUser>();
        if (ListUtil.isNotEmpty(userIds)) {
            QueryCondition condition1 = new QueryCondition();
            condition1.setField(ScdpUserAttribute.USER_ID);
            condition1.setOperator("in");
            condition1.setValueList(userIds);
            List<QueryCondition> lstCondition = new ArrayList<QueryCondition>();
            lstCondition.add(condition1);
            List<ScdpUser> ul = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, lstCondition, null);
            ul.forEach(t -> out.put(t.getUserId(), t));
        }
        return out;
    }

    public static ScdpUser findUserByUserId(String userId) {
        ScdpUser out = null;
        if (StringUtil.isNotEmpty(userId)) {
            Map<String, Object> mapConditions = new HashMap<String, Object>();
            mapConditions.put(ScdpUserAttribute.USER_ID, userId);
            List<ScdpUser> ul = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, mapConditions, null);
            if (ListUtil.isNotEmpty(ul)) {
                out = ul.get(0);
            }
        }
        return out;
    }

    //用户ID转名称
    public static String getUserNameById(String userId) {
        ScdpUser scdpUser = findUserByUserId(userId);
        String userName = null;
        if (null != scdpUser) {
            userName = scdpUser.getUserName();
        }

        return userName;
    }

    public static ScdpUser findUserByUUid(String UUid) {
        return PersistenceFactory.getInstance().findByPK(ScdpUser.class, UUid);
    }

    /**
     * 根据userId返回该用户的所有角色
     *
     * @param userId 传入用户userId
     * @return 角色List
     */
    public static List<String> getUserRoleInfo(String userId) {
        String sql = "select * from V_USER_ROLES t  where t.user_id ='" + userId + "'";
        DAOMeta daoMeta = new DAOMeta(sql);
        return PersistenceFactory.getInstance().findByNativeSQL(daoMeta).stream()
                .map(x -> x.get("roleName").toString())
                .collect(Collectors.toList());
    }

    //月度支付时 对以下角色 不进行过滤
    public static boolean checkIfFadScmNeedFilter(String userId) {
        if (UserHelper.getOrgCode().equals("CSNT_JCB") || UserHelper.getOrgCode().equals("CST_YGB")) {
            return false;
        }
        List nonNeedFilter = Arrays.asList("公司领导", "公司总会计师", "计财部主任", "供应链部主任", "公司总会计师");
        return !getUserRoleInfo(userId).stream().anyMatch(t -> nonNeedFilter.contains(t));
    }


    public static List<String> getUserIdsByRoleName(String roleName) {
        if (StringUtil.isEmpty(roleName)) {
            return null;
        } else {
            String strSql = "SELECT distinct USER_ID FROM V_USER_ROLES V1 " +
                    "inner join scdp_role t1 on t1.uuid=V1.ROLE_UUID " +
                    "WHERE t1.ROLE_NAME=? ";
            ArrayList paramList = new ArrayList();
            paramList.add(roleName);
            DAOMeta dao = new DAOMeta(strSql, paramList);
            List<Map<String, Object>> lstUserId = PersistenceFactory.getInstance().findByNativeSQL(dao, new Integer[0]);
            return (List) lstUserId.stream().map((x) -> {
                return (String) x.get("userId");
            }).distinct().collect(Collectors.toList());
        }
    }
}
