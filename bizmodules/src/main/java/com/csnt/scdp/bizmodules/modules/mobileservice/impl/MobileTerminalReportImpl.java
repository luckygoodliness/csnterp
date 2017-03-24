package com.csnt.scdp.bizmodules.modules.mobileservice.impl;

import com.csnt.scdp.bizmodules.attributes.CommonAttribute;
import com.csnt.scdp.bizmodules.attributes.ErpMobileTerminalAttribute;
import com.csnt.scdp.bizmodules.entity.mobile.MobileRegInfo;
import com.csnt.scdp.bizmodules.entityattributes.mobile.MobileRegInfoAttribute;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalReport;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.CacheHelper;
import com.csnt.scdp.framework.helper.SysconfigHelper;
import com.csnt.scdp.framework.helper.UserHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.JsonUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpRole;
import com.csnt.scdp.sysmodules.helper.FMCodeHelper;
import com.csnt.scdp.sysmodules.helper.OnlineUserHelper;
import com.csnt.scdp.sysmodules.modules.sys.role.dto.ScdpRoleDto;
import com.csnt.scdp.sysmodules.modules.sys.role.services.intf.SysRoleManager;
import com.fr.fs.base.entity.User;
import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.struts2.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.jws.WebService;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lenovo on 2016/9/23.
 */
@Scope("singleton")
@Service("Mobile")
@Transactional
@WebService(endpointInterface = "com.csnt.scdp.bizmodules.modules.mobileservice.intf.MobileTerminalReport")
public class MobileTerminalReportImpl implements MobileTerminalReport {
    private static ILogTracer tracer = LogTracerFactory.getInstance(MobileTerminalReportImpl.class);
    private final String[][] reports = {{"部门年度财务指标报表用户", "BMNDCWZBWCZD_APP"}, {"年度财务指标完成进度", "NDCWZBWCZD_APP"}};

    public Map getReport() {
        Map result = new LinkedHashMap<>();
        List lstResult = new ArrayList<>();
        String strSQL = "SELECT * FROM SCDP_ROLE WHERE UUID IN (" +
                UserHelper.getRoles().stream().map(x -> "'" + x + "'").collect(Collectors.joining(",")) + ")";
        List<ScdpRole> roles = PersistenceFactory.getInstance().findByNativeSQL(new DAOMeta(strSQL)).stream()
                .map(x -> (ScdpRole) BeanUtil.map2Dto(x, ScdpRole.class))
                .collect(Collectors.toList());

        String previewUrl = SysconfigHelper.getProperty("mobile_fineReport_preview_url");
        for (String[] t : reports) {
            if (roles.stream().filter(x -> x.getRoleDesc() != null && t[1].equals(x.getRoleDesc())).findFirst().isPresent()) {
                Map annul = new LinkedHashMap<>();
                int year = Calendar.getInstance().get(Calendar.YEAR);
                annul.put("title", t[0]);
                annul.put("url", previewUrl + "ReportServer?reportlet=" + t[1] + ".cpt" + "&officeId=" + UserHelper.getOrgCode());
                annul.put(ErpMobileTerminalAttribute.IS_DIRECT, true);
                lstResult.add(annul);
            }
        }

        result.put("reports", lstResult);
        try {
            System.out.println(JSONUtil.serialize(result));
        } catch (Exception e) {

        }
        return result;

    }


    @Override
    public HashMap uploadRegID(String pushToken) {
        Map pushMap = new HashMap<>();
        HashMap retMap = new HashMap<>();
        int retVal = 0;

        if (StringUtil.isEmpty(pushToken)) {
            retMap.put("result", -2);
            return retMap;
        } else {
            try {
                Map dataMap = (Map) JsonUtil.deserialize(pushToken);
                pushToken = dataMap.get("pushToken").toString();

                PersistenceCrudManager pcm = PersistenceFactory.getInstance();
                Map condition = new HashMap<>();
                String userId = UserHelper.getUserId();
//                condition.put(MobileRegInfoAttribute.REG_ID, pushToken);
                condition.put(MobileRegInfoAttribute.LOGIN_USER_NAME, userId);
                List<MobileRegInfo> mobileRegInfos = pcm.findByAnyFields(MobileRegInfo.class, condition, null);
                if (ListUtil.isEmpty(mobileRegInfos)) {
                    MobileRegInfo mobileRegInfo = new MobileRegInfo();
                    mobileRegInfo.setLoginUserName(userId);
                    mobileRegInfo.setRegId(pushToken);
                    pcm.insert(mobileRegInfo);
                } else {
                    MobileRegInfo mobileRegInfo = mobileRegInfos.get(0);
                    mobileRegInfo.setRegId(pushToken);
                    pcm.update(mobileRegInfo);
                }
                if (StringUtil.isEmpty(pushToken)) {
                    retMap.put("result", -2);
                    return retMap;
                }
            } catch (Exception e) {
                tracer.error(e);
                retMap.put("result", -2);
                return retMap;
            }
        }

        pushMap.put("pushToken", pushToken);
        String extraInfo = OnlineUserHelper.getCurrentOnlineUser().getExtraInfoJson();
        if (StringUtil.isEmpty(extraInfo)) {
            extraInfo = JsonUtil.serialize(pushMap);
        } else {
            Map oriMap = new HashMap<>();
            try {
                oriMap.putAll((Map) JsonUtil.deserialize(extraInfo));
            } catch (Exception e) {
                tracer.error(e);
                retVal = -1;
            }
            oriMap.putAll(pushMap);
            extraInfo = JsonUtil.serialize(oriMap);
        }
        OnlineUserHelper.getCurrentOnlineUser().setExtraInfoJson(extraInfo);

        retMap.put("result", retVal);
        return retMap;
    }
}
