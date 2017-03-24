package com.csnt.scdp.bizmodules.modules.scm.supplierlimit.services.intf;

import com.csnt.scdp.bizmodules.entity.scm.ScmSupplierLimit;
import com.fr.third.org.apache.poi.hssf.record.formula.functions.Int;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description:  SupplierlimitManager
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-07-19 15:14:12
 */
public interface SupplierlimitManager {
    List<Map<String, Object>> selectCurAmount(String uuid) ;
    List<ScmSupplierLimit> selectYear(Integer year);
    List<Map<String, Object>> selectTime(String beginTime, String endTime);

}