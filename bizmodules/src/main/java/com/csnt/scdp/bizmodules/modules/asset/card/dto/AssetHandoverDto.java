package com.csnt.scdp.bizmodules.modules.asset.card.dto;

import com.csnt.scdp.bizmodules.modules.asset.check.dto.AssetAnnualCheckDto;


import com.csnt.scdp.bizmodules.modules.asset.maintain.dto.AssetMaintainDto;
import com.csnt.scdp.bizmodules.modules.asset.transfer.dto.AssetTransferDto;
import com.csnt.scdp.framework.dto.CascadeChilds;
import com.csnt.scdp.framework.dto.PojoMapping;
import com.csnt.scdp.bizmodules.entity.asset.AssetHandover;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import java.util.List;


/**
 * Description:  AssetCardDto
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generate
 * @version 1.0
 * @timestamp 2015-09-22 10:18:39
 */
@PojoMapping(PojoClass = "com.csnt.scdp.bizmodules.entity.asset.AssetHandover")
public class AssetHandoverDto extends AssetHandover {
}