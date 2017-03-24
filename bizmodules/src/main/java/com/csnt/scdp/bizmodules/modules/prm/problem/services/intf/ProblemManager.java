package com.csnt.scdp.bizmodules.modules.prm.problem.services.intf;

import com.csnt.scdp.bizmodules.entity.prm.PrmBrief;
import com.csnt.scdp.bizmodules.entity.prm.PrmProblem;

import java.util.Map;

/**
 * Description:  ProblemManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 19:30:04
 */
public interface ProblemManager {
    //1.额外设置需要返回的数据
    void setExtraData(Map outMap);
    //根据uuid获取问题申报数据
    public PrmProblem getPrmProblemForUUID(String uuid);

}