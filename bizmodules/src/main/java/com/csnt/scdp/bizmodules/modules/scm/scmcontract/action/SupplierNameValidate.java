package com.csnt.scdp.bizmodules.modules.scm.scmcontract.action;

import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSupplierDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.DateUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:32:42
 */

@Scope("singleton")
@Controller("scmcontract-supplierNameValidate")
@Transactional
public class SupplierNameValidate implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(SupplierNameValidate.class);
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        StringBuffer sb = new StringBuffer();
        String supplierCode = (String) inMap.get("supplierCode");
        Date date=new Date();
        if (StringUtil.isEmpty(supplierCode)){
            supplierCode="";
        }
        ScmSupplierDto scmSupplierDto=(ScmSupplierDto)DtoHelper.findDtoByPK(ScmSupplierDto.class, supplierCode);
        String msg="";
        if(scmSupplierDto!=null) {
            if (ListUtil.isEmpty(scmSupplierDto.getScmSupplierBankDto())) {
                sb.append("缺少银行账号！");
            }
            if (ListUtil.isEmpty(scmSupplierDto.getScmSupplierContactsDto())) {
                sb.append("缺少供应商联系人！");
            }
            if (ListUtil.isEmpty(scmSupplierDto.getCdmFileRelationDto())) {
                sb.append("缺少供应商资质！");
            } else {
                for (int i = 0; i < scmSupplierDto.getCdmFileRelationDto().size(); i++) {
                    if(scmSupplierDto.getCdmFileRelationDto().get(i).getEnddate()!=null) {
                        if (date.after(scmSupplierDto.getCdmFileRelationDto().get(i).getEnddate())) {
                            sb.append("资质文件："+scmSupplierDto.getCdmFileRelationDto().get(i).getFileName() + "已经过期！");
                            break;
                        }
                    }
                }
            }
            if(sb.toString().length()>0){
                msg="此供应商"+sb.toString();
            }
        }
        rsMap.put("msg", msg);
        return rsMap;
    }
}
