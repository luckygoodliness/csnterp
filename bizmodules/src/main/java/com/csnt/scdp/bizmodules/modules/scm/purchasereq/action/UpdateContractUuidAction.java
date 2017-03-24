package com.csnt.scdp.bizmodules.modules.scm.purchasereq.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.dto.PrmPurchaseReqDetailDto;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.dto.ScmContractDto;
import com.csnt.scdp.framework.commonaction.crud.CommonModifyAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Description:  ModifyAction
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-09-26 15:30:52
 */

@Scope("singleton")
@Controller("prmpurchasereqdtl-update-contract-uuid")
@Transactional
public class UpdateContractUuidAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(UpdateContractUuidAction.class);

    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap();
        String scmContractId = (String) inMap.get("scmContractId");
        String contractCode = null;
        if (StringUtil.isNotEmpty(scmContractId)) {
            ScmContract scmContract = (ScmContract) DtoHelper.findDtoByPK(ScmContractDto.class, scmContractId);
            contractCode = scmContract.getScmContractCode();
            synAdditionalFieldsToContract(inMap, scmContractId);//更新询价单信息
        }
        insertOrUpdateDetail(inMap, contractCode);
        reQueryDetailInfo(inMap, out, scmContractId);
        //Do After
        return out;
    }

    private void insertOrUpdateDetail(Map inMap, String contractCode) {
        //把contract的uuid更新到prmPurchaseReqDetail表里面， 同时插入拆分的记录
        Map newAddData = (Map) inMap.get("newAddData");
        List updateData = (List) inMap.get("prmPurchaseReqDetailDto");
        List lstPrmDetail = new ArrayList();
        if (MapUtil.isNotEmpty(newAddData)) {
            //这样做的目的是为了避免如果两个人同时操作的时候，其中一个人把报价单号修改之后，另一个人操作的contractno号可能会不一致
            newAddData.put("scmContractCode", contractCode);
            lstPrmDetail.add(BeanUtil.map2Dto(newAddData, PrmPurchaseReqDetailDto.class));
            DtoHelper.batchAdd(lstPrmDetail, PrmPurchaseReqDetail.class);
        }
        if (ListUtil.isNotEmpty(updateData)) {
            List lstAdd = new ArrayList();
            for (Iterator it = updateData.iterator(); it.hasNext(); ) {
                Map mapUpdate = (Map) it.next();
                lstAdd.add(BeanUtil.map2Dto(mapUpdate, PrmPurchaseReqDetailDto.class));
            }
            DtoHelper.batchUpdate(lstAdd, PrmPurchaseReqDetail.class);
        }
    }

    private void reQueryDetailInfo(Map inMap, Map out, String scmContractId) {//update by lijl(要看到该询价下的所有明细)
//        String scmPurchaseReqUuid = (String) inMap.get("scmPurchaseReqUuid");
//        if (StringUtil.isNotEmpty(scmPurchaseReqUuid)) {
            //查询更新后的记录，更新到前台
            Map condition = new HashMap();
//            condition.put("scmPurchaseReqId", scmPurchaseReqUuid);
            condition.put("scmContractId", scmContractId);

            List lstBp = DtoHelper.findByAnyFields(PrmPurchaseReqDetailDto.class, condition, null);
            out.put("prmPurchaseReqDetailDto", lstBp);
//        }
    }

    private void synAdditionalFieldsToContract(Map inMap, String scmContractId) {
        //把下面几个信息写到contract表
        String officeId = (String) inMap.get("officeId");
        Object purchasePackageId = inMap.get("purchasePackageId");
        String projectId = (String) inMap.get("projectId");
        Long isProject = (Long) inMap.get("isProject");
        String bugdetId = (String) inMap.get("bugdetId");
        String subjectCode = (String) inMap.get("subjectCode");
        //如果三个项目有一个不为空，就要清空contract里面数据
        if (StringUtil.isNotEmpty(projectId) || StringUtil.isNotEmpty(officeId) || StringUtil.isNotEmpty(purchasePackageId)) {
            ScmContract scmContract = (ScmContract) DtoHelper.findDtoByPK(ScmContractDto.class, scmContractId);
            if (scmContract != null) {
                scmContract.setOfficeId(officeId);
                if (purchasePackageId != null) {
                    scmContract.setPurchasePackageId(purchasePackageId.toString());
                }
                scmContract.setProjectId(projectId);
                scmContract.setIsProject(isProject == null ? 0 : isProject.intValue());
                scmContract.setBugdetId(bugdetId);
                scmContract.setSubjectCode(subjectCode);
                List lstUpdate = new ArrayList();
                lstUpdate.add(scmContract);
                DtoHelper.batchUpdate(lstUpdate, ScmContract.class);
            }
        }
    }
}
