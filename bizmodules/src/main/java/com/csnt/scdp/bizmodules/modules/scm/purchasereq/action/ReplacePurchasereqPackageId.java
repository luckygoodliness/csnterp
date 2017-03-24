package com.csnt.scdp.bizmodules.modules.scm.purchasereq.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.dto.PrmPurchaseReqDetailDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePackageDto;
import com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePlanDetailDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsupplierlimitchange.services.intf.ScmsupplierlimitchangeManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.dto.BasePojo;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
@Controller("purchasereq-replacepurchasereqpackageid")
@Transactional
public class ReplacePurchasereqPackageId implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ReplacePurchasereqPackageId.class);

    @Resource(name = "scmsupplierlimitchange-manager")
    private ScmsupplierlimitchangeManager scmsupplierlimitchangeManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map rsMap = new HashMap();
        String prmPurchasePlanDetailId = (String) inMap.get("prmPurchasePlanDetailId");
        String packageId = (String) inMap.get("packageId");

        if(StringUtil.isEmpty(packageId)){
            throw new BizException("请选择包号");
        }

        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        if (StringUtil.isNotEmpty(prmPurchasePlanDetailId)) {
            Map conditionMap = new HashMap<>();
            conditionMap.put("prmPurchasePlanDetailId", prmPurchasePlanDetailId);
            List prmPurchaseReqDetailDtoDtoList = DtoHelper.findByAnyFields(PrmPurchaseReqDetailDto.class, conditionMap, null);
            List<BasePojo> dtoUpdateObjs = new ArrayList<BasePojo>();
            String oldPurchasePackageId="";
            for (Object o : prmPurchaseReqDetailDtoDtoList) {
                PrmPurchaseReqDetailDto dto = (PrmPurchaseReqDetailDto) o;
                if (StringUtil.isEmpty(dto.getScmContractId())){
                    oldPurchasePackageId= dto.getPurchasePackageId();
                    dto.setPurchasePackageId(packageId);
                    dto.setEditflag("*");
                    dtoUpdateObjs.add(dto);
                }else {
                    throw new BizException("本分包已采购，不可调包，请做新增合同操作");
                }
            }
            String sqls = " select 1 from scm_contract_change scc" +
                    " left join scm_contract_change_d sccd" +
                    " on scc.uuid =sccd.scm_contract_change_id" +
                    " where " +
                    " sccd.change_state='1'" +
                    " and scc.is_void ='0'" +
                    " and scc.state <> '3'" +
                    " and sccd.prm_purchase_plan_detail_id =  ? ";
            List lstParam = new ArrayList<>();
            lstParam.add(prmPurchasePlanDetailId);
            DAOMeta daoMeta = new DAOMeta(sqls, lstParam);
            daoMeta.setNeedFilter(false);
            List lst = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
            if(ListUtil.isNotEmpty(lst)){
                throw new BizException("本分包已采购(采购变更申请)，不可调包，请做新增合同操作");
            }
            DtoHelper.batchUpdate(dtoUpdateObjs, PrmPurchaseReqDetail.class);
            PrmPurchasePlanDetailDto prmPurchasePlanDetailDto= (PrmPurchasePlanDetailDto)DtoHelper.findDtoByPK(PrmPurchasePlanDetailDto.class, prmPurchasePlanDetailId);
            PrmPurchasePackageDto   newPrmPurchasePackageDto= (PrmPurchasePackageDto)DtoHelper.findDtoByPK(PrmPurchasePackageDto.class, packageId);
            prmPurchasePlanDetailDto.setPurchasePackageId(packageId);
            BigDecimal PackageNo=new  BigDecimal(newPrmPurchasePackageDto.getPackageNo());
            prmPurchasePlanDetailDto.setSubPackageNo(PackageNo);
            prmPurchasePlanDetailDto.setPurchaseLevel(newPrmPurchasePackageDto.getPurchaseLevel());
            prmPurchasePlanDetailDto.setSubjectCode(newPrmPurchasePackageDto.getMaterialClassCode());
            prmPurchasePlanDetailDto.setSubjectProperty(newPrmPurchasePackageDto.getSubjectProperty());
            prmPurchasePlanDetailDto.setEditflag("*");
            DtoHelper.CascadeSave(prmPurchasePlanDetailDto);
            String sql = "UPDATE PRM_PURCHASE_PACKAGE P" +
                    "   SET P.PACKAGE_BUDGET_MONEY = NVL(ROUND((SELECT SUM(NVL(PD.BUDGET_PRICE, 0) *" +
                    "   NVL(PD.AMOUNT, 0))" +
                    "  FROM PRM_PURCHASE_PLAN_DETAIL PD" +
                    " WHERE PD.PURCHASE_PACKAGE_ID = P.UUID),3),0)," +
                    " PENDING_MONEY  = NVL(ROUND((SELECT SUM(NVL(PD.BUDGET_PRICE, 0) *" +
                    "  NVL(PD.AMOUNT, 0))" +
                    "  FROM PRM_PURCHASE_PLAN_DETAIL PD" +
                    "   WHERE PD.PURCHASE_PACKAGE_ID = P.UUID),3),0)" +
                    " WHERE P.UUID IN ('"+packageId+"', '"+oldPurchasePackageId+"')";
            List lstParams = new ArrayList<>();
            DAOMeta daoMeta1 = new DAOMeta(sql, lstParams);
            PersistenceFactory.getInstance().updateByNativeSQL(daoMeta1);
        }
        return null;
    }
}
