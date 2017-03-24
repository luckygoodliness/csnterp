package com.csnt.scdp.bizmodules.modules.common.helper;

import com.csnt.scdp.framework.helper.I18NHelper;
import com.csnt.scdp.framework.util.StringUtil;

/**
 * Created by lenovo on 2016/8/16.
 */
public class ErpI18NHelper {
    public static String englishToChinese(String field, String modulePath) {
        if (StringUtil.isEmpty(field) && StringUtil.isEmpty(modulePath)) {
            return field;
        }
        String chineseField= I18NHelper.getI18NStr(field,modulePath+"/layout/resources/resource");
        return chineseField;
    }
}
