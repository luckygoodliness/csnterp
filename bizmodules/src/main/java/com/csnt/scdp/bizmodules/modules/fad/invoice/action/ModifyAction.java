package com.csnt.scdp.bizmodules.modules.fad.invoice.action;

import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadInvoiceDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierinfor.services.intf.SupplierinforManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.fad.invoice.services.intf.InvoiceManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-25 14:13:12
 */

@Scope("singleton")
@Controller("invoice-modify")
@Transactional
public class ModifyAction extends CommonModifyAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(ModifyAction.class);

	@Resource(name = "invoice-manager")
	private InvoiceManager invoiceManager;

    @Resource(name = "supplierinfor-manager")
    private SupplierinforManager supplierinforManager;

    public void validation(Map inMap) {

        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);
        String mainTabName = StringUtil.capFirst(BeanFactory.getClass(dtoClass).getSimpleName(), false);
        Map mapInput = (Map) viewMap.get(mainTabName);
        if(mapInput!=null){
            List<Map> scmContractInvoiceList = (List)mapInput.get("scmContractInvoiceDto");
            for(Map s:scmContractInvoiceList){
                BigDecimal amount;
                String editflag =s.get("editflag").toString();
                String str ="1";
                switch (StringUtil.replaceNull(editflag)) {
                    case "+":
                    case "":
                        String scmContractId = s.get("scmContractId").toString();
                        amount=new BigDecimal((s.get("amount")).toString());
                        str =invoiceManager.contractInvoiceCheckAdd(scmContractId, amount);
                        break;
                    case "-":
                        break;
                    case "*":
                        String uuid = s.get("uuid").toString();
                        amount=new BigDecimal((s.get("amount")).toString());
                        str =invoiceManager.contractInvoiceCheckModify(uuid,amount);
                    case "#":
                        break;
                }
                if(str.equals("2")){
                    throw new BizException("找不到合同！", new HashMap());
                }else if(!str.equals("1")){
                    throw new BizException("合同"+str+"发票总额已经超过合同总额，请重新分配！", new HashMap());
                }
            }
        }
    }
	@Override
	public Map perform(Map inMap) throws BizException, SysException {
        validation(inMap);
		//Do before
		Map out = super.perform(inMap);
		//Do After
		return out;
	}

    protected void beforeAction(BasePojo dtoObj) {
        FadInvoiceDto fadInvoiceDto = (FadInvoiceDto) dtoObj;
        //判断银行账户是否在供方库中存在，若存在判断收款单位与库中是否一致，不一致 则返回报错
        List<ScmSupplier> ScmSuppliers = supplierinforManager.getBankBelongedScmSupplier(fadInvoiceDto.getBankId());
        if (ListUtil.isNotEmpty(ScmSuppliers)) {
            List<ScmSupplier> tmp = ScmSuppliers.stream().filter(t -> t.getCompleteName().equals(fadInvoiceDto.getSupplierName())).collect(Collectors.toList());
            if (ListUtil.isEmpty(tmp)) {
                throw new BizException("该银行账户已在供应商：" + ScmSuppliers.get(0).getCompleteName() + " 使用，无法保存！");
            }
        }
    }
    @Override
    // 存储过程核销
    protected void afterAction(BasePojo dtoObj) {
        String id  = (((FadInvoiceDto) dtoObj).getUuid());
        invoiceManager.contractInvoiceWrittenOff("0", id);
    }
    //JAVA核销
//    protected void afterAction(BasePojo dtoObj) {
//        FadInvoiceDto dto  = (FadInvoiceDto) dtoObj;
//        invoiceManager.contractInvoiceClearance(dto);
//    }
}
