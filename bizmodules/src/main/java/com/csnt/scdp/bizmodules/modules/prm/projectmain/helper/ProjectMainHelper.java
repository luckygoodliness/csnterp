package com.csnt.scdp.bizmodules.modules.prm.projectmain.helper;

/**
 * Description:  ProjectMainHelper
 * Copyright: © 2015 Aardwolf Studio. All rights reserved.
 * Company:CSNT中海网络科技股份有限公司
 *
 * @author liujingyuan
 * @version 1.0
 */
public class ProjectMainHelper {
    public static String getChangeVersionDesc(Integer changeVersion) {
        String changeVersionDesc = "";
        if (changeVersion != null) {
            if (Integer.valueOf(-1).equals(changeVersion)) {
                changeVersionDesc = "立项新增";
            } else if (Integer.valueOf(0).equals(changeVersion)) {
                changeVersionDesc = "变更新增";
            } else {
                changeVersionDesc = "第" + changeVersion + "次变更";
            }
        }
        return changeVersionDesc;
    }
}

