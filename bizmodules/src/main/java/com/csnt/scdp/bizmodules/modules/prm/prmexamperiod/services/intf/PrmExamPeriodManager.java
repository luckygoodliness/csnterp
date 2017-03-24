package com.csnt.scdp.bizmodules.modules.prm.prmexamperiod.services.intf;

import com.csnt.scdp.bizmodules.entity.prm.PrmExamPeriod;

import java.util.Date;

/**
 * Created by Administrator on 2016/11/14.
 */
public interface PrmExamPeriodManager {

    Date getExamDateFromAppointedDate(Date appointedDate);

    boolean checkValidDate(PrmExamPeriod period);
}
