package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMainC;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmProjectMainCDto;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf.PrmprojectmaincManager;
import com.csnt.scdp.framework.commonaction.crud.CommonModifyAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-27 15:28:19
 */

@Scope("singleton")
@Controller("prmprojectmainc-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

    @Resource(name = "prmprojectmainc-manager")
    private PrmprojectmaincManager prmprojectmaincManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = super.perform(inMap);
        //Do After
        return out;
    }

    @Override
    protected void beforeAction(BasePojo dtoObj) {
        //dtoObj 获取页面数据数据
        PrmProjectMainCDto prmProjectMainC = (PrmProjectMainCDto) dtoObj;
        String detailType = ((PrmProjectMainC) dtoObj).getDetailType();
        if (!prmprojectmaincManager.validateProjectName(prmProjectMainC)) {
            throw new BizException("项目名称或项目简称已经存在,添加失败！");
        }
        if (PrmProjectMainAttribute.PRM_DETAIL_ALL.equals(detailType)) {
            List lstMessage = prmprojectmaincManager.validateSerialNumber(prmProjectMainC);
            if (ListUtil.isNotEmpty(lstMessage)) {
                throw new BizException(StringUtil.joinList(lstMessage, "\n"));
            }
        }
        if (StringUtil.isNotEmpty(prmProjectMainC.getPrmProjectMainId())) {
            PrmProjectMain main = PersistenceFactory.getInstance().findByPK(PrmProjectMain.class, prmProjectMainC.getPrmProjectMainId());
            if (main != null) {
                if (main.getIsPreProject() == 0 && prmProjectMainC.getIsPreProject() == 1) {
                    throw new BizException("已立项项目不能改为预立项！");
                }
            }
        }
        prmprojectmaincManager.handlePrmCodeType(prmProjectMainC);
    }

    @Override
    protected void afterAction(BasePojo dtoObj) {
        PrmProjectMainCDto mainCDto = (PrmProjectMainCDto) dtoObj;
        if (mainCDto == null) {
            return;
        }
    }
}
