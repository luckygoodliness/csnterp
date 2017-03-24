package com.csnt.scdp.bizmodules.modules.scm.scmsaeappraisercase.action;

import com.csnt.scdp.bizmodules.modules.common.intf.CommonServiceManager;
import com.csnt.scdp.bizmodules.modules.prm.prmorgrolefilter.dto.ScdpOrgDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsaeappraisercase.dto.ScmSaeAppraiserCaseDDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsaeappraisercase.dto.ScmSaeAppraiserCaseDto;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.ObjectUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.scm.scmsaeappraisercase.services.intf.ScmsaeappraisercaseManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Description:  LoadAction
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-11-02 09:56:31
 */

@Scope("singleton")
@Controller("scmsaeappraisercase-load")
@Transactional
public class LoadAction extends CommonLoadAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(LoadAction.class);

	@Resource(name = "scmsaeappraisercase-manager")
	private ScmsaeappraisercaseManager scmsaeappraisercaseManager;

	@Resource(name = "erp-common-service-manager")
	private CommonServiceManager commonServiceManager;

	@Override
	public Map perform(Map inMap) throws BizException, SysException {
		//Do before
		Map out = super.perform(inMap);
		//Do After
		return out;
	}

	@Override
	protected void afterAction(BasePojo dtoObj) {
		ScmSaeAppraiserCaseDto scmSaeAppraiserCaseDto = (ScmSaeAppraiserCaseDto)dtoObj;

		if(scmSaeAppraiserCaseDto != null) {
			List<ScmSaeAppraiserCaseDDto> scmSaeAppraiserCaseDDtoList = scmSaeAppraiserCaseDto.getScmSaeAppraiserCaseDDto();
			if(ListUtil.isNotEmpty(scmSaeAppraiserCaseDDtoList)) {
				scmSaeAppraiserCaseDDtoList.forEach(scmSaeAppraiserCaseDDto -> {
					//填充用户名
					String appraiser = scmSaeAppraiserCaseDDto.getAppraiser();
					if (StringUtils.isNotBlank(appraiser)) {
						List<ScdpUser> obj = commonServiceManager.findUserByUserId(appraiser);
						if (ListUtil.isNotEmpty(obj)) {
							String name = obj.get(0).getUserName();
							String orgUuid = obj.get(0).getOrgUuid();
							ScdpOrgDto scdpOrgDto = (ScdpOrgDto) DtoHelper.findDtoByPK(ScdpOrgDto.class, orgUuid);
							scmSaeAppraiserCaseDDto.setAppraiserDesc(name);
							scmSaeAppraiserCaseDDto.setOfficeIdDesc(scdpOrgDto.getOrgName());
						}
					}

				});
			}
		}

	}
}
