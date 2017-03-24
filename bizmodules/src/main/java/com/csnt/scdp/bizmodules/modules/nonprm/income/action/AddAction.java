package com.csnt.scdp.bizmodules.modules.nonprm.income.action;

import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncome;
import com.csnt.scdp.bizmodules.entity.nonprm.NonProjectIncomeIn;
import com.csnt.scdp.bizmodules.entity.nonprm.SubjectSubject;
import com.csnt.scdp.bizmodules.modules.nonprm.income.dto.NonProjectIncomeBalanceDto;
import com.csnt.scdp.bizmodules.modules.nonprm.income.dto.NonProjectIncomeDto;
import com.csnt.scdp.bizmodules.modules.nonprm.income.dto.NonProjectIncomeInDto;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.bizmodules.modules.nonprm.income.services.intf.IncomeManager;


import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Description:  AddAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-24 17:37:27
 */

@Scope("singleton")
@Controller("income-add")
@Transactional
public class AddAction extends CommonAddAction {
	private static ILogTracer tracer = LogTracerFactory.getInstance(AddAction.class);

	@Resource(name = "income-manager")
	private IncomeManager incomeManager;

    private  PersistenceCrudManager persistenceCrudManager;
	@Override
	public Map perform(Map inMap) throws BizException, SysException {

        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);

        BasePojo dtoObj = DtoHelper.getDtoFromMap(viewMap, dtoClass);
        NonProjectIncomeBalanceDto balanceDto = (NonProjectIncomeBalanceDto)dtoObj;

        List<NonProjectIncomeInDto> lstIncomeIn = balanceDto.getNonProjectIncomeInDto();
        List<NonProjectIncomeDto> lstObj = balanceDto.getNonProjectIncomeDto();
        Calendar date = Calendar.getInstance();
        int y = date.get(Calendar.YEAR);
        String year = Integer.toString(y);
        if (ListUtil.isNotEmpty(lstIncomeIn)){
            for (int i = 0; i < lstIncomeIn.size(); i++) {
                NonProjectIncomeIn incomeIn = new NonProjectIncomeIn();
                incomeIn.setSubject(lstIncomeIn.get(i).getSubject());
                incomeIn.setYear(year);
                incomeIn.setSubjectOfficeId(lstIncomeIn.get(i).getSubjectOfficeId());
                incomeIn.setSubjectOfficeName(lstIncomeIn.get(i).getSubjectOfficeName());
                incomeIn.setAppliedValue(lstIncomeIn.get(i).getAppliedValue());
                incomeIn.setAssignedValue(lstIncomeIn.get(i).getAssignedValue());
                incomeIn.setOccurredValue(lstIncomeIn.get(i).getOccurredValue());
                incomeIn.setFirstInstance(lstIncomeIn.get(i).getFirstInstance());
                incomeManager.save(incomeIn);
            }
        }

        if(lstObj.size()>0){
            for(int i = 1; i<lstObj.size()-1; i++){
                NonProjectIncome income = new NonProjectIncome();
                income.setSubject(lstObj.get(i).getSubject());
                income.setAppliedValue(lstObj.get(i).getAppliedValue());
                income.setAssignedValue(lstObj.get(i).getAssignedValue());
                income.setOccurredValue(lstObj.get(i).getOccurredValue());
                income.setFirstInstance(lstObj.get(i).getFirstInstance());
                income.setYear(year);
                incomeManager.save(income);
            }
        }
//        String dtoClass = (String) inMap.get(CommonAttribute.DTO_CLASS_NAME);
//        Map viewMap = (Map) inMap.get(CommonAttribute.VIEW_DATA);
//        DtoHelper.getDtoFromMap(viewMap, dtoClass);
//        Map map = (Map)viewMap.get("nonProjectIncomeBlanceDto");
////        Map blance = (Map)map.get("nonProjectIncomeBlanceDto");
//        List lstObj = (List) map.get("nonProjectIncomeDto");
////        Map lstObj2 = (Map) map.get("nonProjectIncome2Dto");
////        inMap.put("nonProjectIncomeDto",lstObj);
////        inMap.put("nonProjectIncome2Dto",lstObj2);
//        if(lstObj.size()>0){
//            for(int i =0; i<lstObj.size(); i++){
//                Map incomes = (Map)(lstObj.get(i));
//                if (incomes != null){
//                    NonProjectIncome income = (NonProjectIncome)DtoHelper.getDtoFromMap(viewMap, dtoClass);
////                    incomes.getClass().newInstance();
////                    BeanUtil.populateBean((Map) lstObj.get(i), objectTemp);
////                    listRetun.add(objectTemp);
//
//                }
//
////                List<NonProjectIncome> lstIncome = new ArrayList<Object>();
////                for (int i = 0; i < list.size(); i++) {
////                    Object objectTemp = object.getClass().newInstance();
////                    BeanUtil.populateBean((Map) list.get(i), objectTemp);
////                    listRetun.add(objectTemp);
////                }
////                List incomes = (List)(lstObj.get(i));
////                incomes.get("subject");
////                if( income.getSubject() != null ){
////                    String name = income.getSubject();
////                    if( "部门非项目支出".equals(name) ){
////
////                    }else{
////                        persistenceCrudManager.batchInsert(lstObj);
////                    }
////                }
//
////                income.setAppliedValue(((NonProjectIncome)lstObj.get(i)).getAppliedValue());
////                income.setAppliedValue(((NonProjectIncome)lstObj.get(i)).getAppliedValue());
////                incomeManager.save(lstObj.get(i));
//            }
//        }
//		//Do before
//		Map out = super.perform(inMap);
		//Do After
		return null;
	}
}
