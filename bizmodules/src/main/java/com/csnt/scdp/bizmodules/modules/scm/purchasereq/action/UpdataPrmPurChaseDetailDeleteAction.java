package com.csnt.scdp.bizmodules.modules.scm.purchasereq.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmPurchaseReqDetail;
import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.dto.PrmPurchaseReqDetailDto;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.framework.commonaction.crud.CommonModifyAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
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
@Controller("delete-contract-detail")
@Transactional
public class UpdataPrmPurChaseDetailDeleteAction extends CommonModifyAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(UpdataPrmPurChaseDetailDeleteAction.class);

    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        String contractUuid = (String) inMap.get("contractUuid");
        ScmContract scmContract = PersistenceFactory.getInstance().findByPK(ScmContract.class,contractUuid);
        if(scmContract!=null){
            if(!("0".equals(scmContract.getState())||"5".equals(scmContract.getState()))){
                throw new BizException("合同已经提交或审批，不允许修改。");
            }
        }
        Map out = new HashMap();
        List prmPurchaseReqDetailDto = (List) inMap.get("prmPurchaseReqDetailDto");
        List lstPrmPurchaseReqDetailUuid = new ArrayList();
        //统计所有的uuid,批量查找相关的detail信息
        if(ListUtil.isNotEmpty(prmPurchaseReqDetailDto)){
            for (Iterator<Map> it = prmPurchaseReqDetailDto.iterator(); it.hasNext(); ) {
                Map tempMap = it.next();
                if (StringUtil.isEmpty(tempMap.get("puuid"))) {
                    lstPrmPurchaseReqDetailUuid.add(tempMap.get("uuid"));
                } else {
                    lstPrmPurchaseReqDetailUuid.add(tempMap.get("puuid"));
                }
            }
            String uuids = StringUtil.joinForSqlIn(lstPrmPurchaseReqDetailUuid, ",");
            //这个查询不能是倒序，因为后面的处理跟这个有关系
            String sql = "select distinct * from prm_purchase_req_detail where nvl(isfallback,0) =0 and (uuid in (" + uuids + ") " +
                    "or puuid in (" + uuids + ")) order by scm_contract_id,puuid asc";
            List lstParam = new ArrayList();
            DAOMeta daoMeta = new DAOMeta(sql, lstParam);
            List<Map<String, Object>> lstResult = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);

            //和查找的结果进行匹配
            rollbackAmount(prmPurchaseReqDetailDto, lstResult);
        }
        prmpurchasereqManager.cleanContract(inMap);
        return out;
    }

    private void rollbackAmount(List prmPurchaseReqDetailDto, List<Map<String, Object>> lstResult) {
        Map mapUpdateRecord = new HashMap();
        Map mapDeleteRecord = new HashMap();
        if (ListUtil.isNotEmpty(lstResult)) {
            for (Iterator<Map> it = prmPurchaseReqDetailDto.iterator(); it.hasNext(); ) {
                Map tempMap1 = it.next();
                String uuid = (String) tempMap1.get("uuid");
                String puuid = StringUtil.replaceNull(tempMap1.get("puuid"), "");
                Object tempAmount = tempMap1.get("handleAmount");
                //处理前台传入数据类型
                BigDecimal amount = processAmount(tempAmount);
                for (int i = lstResult.size() - 1; i >= 0; i--) {
                    Map tempMap2 = lstResult.get(i);
                    String uuid2 = (String) tempMap2.get("uuid");
                    String puuid2 = (String) tempMap2.get("puuid");
                    if (uuid.equals(uuid2) || puuid.equals(puuid2) || puuid.equals(uuid2) || uuid.equals(puuid2)) {
                        if (StringUtil.isEmpty(tempMap2.get("scmContractId"))) {//如果contract_id为空，则把数量相加
                            //把数据放到cache里面
                            mapUpdateRecord.put(StringUtil.isNotEmpty(puuid2) ? puuid2 : uuid2, BeanUtil.map2Dto(tempMap2, PrmPurchaseReqDetailDto.class));
                            lstResult.remove(tempMap2);
                        } else {
                            if (uuid.equals(uuid2)) {//找到了自己，做如下处理
                                if (mapUpdateRecord.containsKey(uuid2) || mapUpdateRecord.containsKey(puuid2)) {//如果cache里面已经有，直接相加，并把自己删除掉
                                    PrmPurchaseReqDetailDto ppr = (PrmPurchaseReqDetailDto) mapUpdateRecord.get(StringUtil.isNotEmpty(puuid2) ? puuid2 : uuid2);
                                    BigDecimal handleAmount = ppr.getHandleAmount();
                                    handleAmount = handleAmount == null ? new BigDecimal(0) : handleAmount.add(amount);
                                    if (handleAmount.equals(ppr.getAmount())) {
                                        handleAmount = null;
                                    }
                                    ppr.setHandleAmount(handleAmount);
                                    mapDeleteRecord.put(uuid, BeanUtil.map2Dto(tempMap2, PrmPurchaseReqDetailDto.class));
                                } else {//没有还未处理，添加处理
                                    tempMap2.put("scmContractId", null);
                                    tempMap2.put("scmContractCode", null);
                                    Object objAmount = tempMap2.get("amount");
                                    Object objHandleAmount = tempMap2.get("handleAmount");
                                    if (objAmount.equals(objHandleAmount)) {
                                        tempMap2.put("handleAmount", null);
                                    }
                                    mapUpdateRecord.put(StringUtil.isNotEmpty(puuid2) ? puuid2 :
                                            uuid2, BeanUtil.map2Dto(tempMap2, PrmPurchaseReqDetailDto.class));
                                }
                                lstResult.remove(tempMap2);
                                break;
                            }
                        }
                    }
                }
            }
            //批量更新和删除
            DtoHelper.batchUpdate(new ArrayList(mapUpdateRecord.values()), PrmPurchaseReqDetail.class);
            DtoHelper.batchDel(new ArrayList(mapDeleteRecord.values()), PrmPurchaseReqDetailDto.class);
        }
    }

    private BigDecimal processAmount(Object tempAmount) {
        BigDecimal amount = null;
        if (tempAmount instanceof Long) {
            amount = BigDecimal.valueOf((Long) tempAmount);
        } else if (tempAmount instanceof Double) {
            amount = BigDecimal.valueOf((Double) tempAmount);
        } else if (tempAmount instanceof Integer) {
            amount = BigDecimal.valueOf((Integer) tempAmount);
        }
        return amount;
    }
}
