package com.csnt.scdp.bizmodules.modules.scm.purchasereq.services.intf;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePlanDetailDto;
import com.csnt.scdp.framework.core.dao.DAOMeta;

import java.util.List;
import java.util.Map;

/**
 * Description:  PurchasereqManager
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-23 14:46:35
 */
public interface PurchasereqManager {

    String processSearchCondition(DAOMeta daoMeta, Map inMap);

    List<Map<String, Object>> getPurchaseReqDetailByProjectId(String prmProjectMainId);

    Map addScalarMapForWorkFlowCommentLoad();

    boolean isContract(List<PrmPurchaseReqDetail> prmPurchaseReqDetails,String uuid);

}