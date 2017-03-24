package com.csnt.scdp.bizmodules.modules.common.helper;

import com.csnt.scdp.framework.helper.ExpandFieldHelper;

import java.util.Map;

/**
 * Description:  ErpExpandFieldHelper
 * Copyright: © 2015 Aardwolf Studio. All rights reserved.
 * Company:CSNT中海网络科技股份有限公司
 *
 * @author liujingyuan
 * @version 1.0
 */
public class ErpExpandFieldHelper {

    /**
     * get expand field value
     *
     * @param expandType eg: USER
     * @param dataUuid   eg: user uuid
     * @param expandCode eg: NC_CODE
     * @return value @String
     */
    public static String getExpandFieldValue(String expandType, String dataUuid, String expandCode) {
        Map expandMap = ExpandFieldHelper.getExpandFields(expandType, dataUuid);
        return (String) expandMap.get(expandCode);
    }

}