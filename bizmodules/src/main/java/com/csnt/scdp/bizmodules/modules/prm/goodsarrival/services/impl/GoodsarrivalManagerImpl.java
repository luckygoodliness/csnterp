package com.csnt.scdp.bizmodules.modules.prm.goodsarrival.services.impl;

import com.csnt.scdp.bizmodules.attributes.CommonAttribute;
import com.csnt.scdp.bizmodules.entity.prm.PrmGoodsArrival;
import com.csnt.scdp.bizmodules.entity.scm.ScmContractDetail;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmGoodsArrivalAttribute;
import com.csnt.scdp.bizmodules.modules.prm.goodsarrival.services.intf.GoodsarrivalManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Description:  GoodsarrivalManagerImpl
 * Copyright: © 2015 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2015-10-15 12:30:07
 */

@Scope("singleton")
@Service("goodsarrival-manager")
public class GoodsarrivalManagerImpl implements GoodsarrivalManager {
    //根据uuid获取合同明细表数据
    public List getScmContractDetailForUUID(String uuid) {
        DAOMeta daoMeta = new DAOMeta();
        String sql = "   select t1.uuid,\n" +
                "          t1.material_name,\n" +
                "          t1.model,\n" +
                "          t1.units,\n" +
                "          t1.amount,\n" +
                "          t1.remark,\n" +
                "          t2.project_id\n" +
                "     from scm_contract_detail t1\n" +
                "     left join scm_contract t2\n" +
                "       on t1.scm_contract_id = t2.uuid " +
                "     where t1.uuid  in ('" + uuid + "')";
        daoMeta.setStrSql(sql);
        List lstObjInfo = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return lstObjInfo;
    }

    @Override
    public void setExtraData(Map outMap) {
        Map dtoMap = (Map) outMap.get("scmContractDetailDto");
        if (dtoMap != null) {
            //设置Grid后续协助人名的显示
            List<PrmGoodsArrival> prmGoodsArrivalList = (List) dtoMap.get("prmGoodsArrivalDto");
            if (ListUtil.isNotEmpty(prmGoodsArrivalList)) {
                for (int i = 0; i < prmGoodsArrivalList.size(); i++) {
                    String registrantName = "";
                    String registrant = (String) ((Map) prmGoodsArrivalList.get(i)).get("registrant");
                    if (registrant != null) {
                        Map paramMap = new HashMap<>();
                        paramMap.put(ScdpUserAttribute.USER_ID, registrant);
                        List<ScdpUser> list = PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, paramMap, null);
                        if (ListUtil.isNotEmpty(list)) {
                            registrantName = list.get(0).getUserName();
                        }
                    }
                    ((Map) prmGoodsArrivalList.get(i)).put(PrmGoodsArrivalAttribute.REGISTRANT_NAME, registrantName);
                }

            }
        }
    }

    @Override
    public void updateIsClosed(String uuids) {
        //批量更新结算状态
        if (StringUtil.isNotEmpty(uuids)) {
            List paramList = new ArrayList<>();
            paramList.add(uuids);
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            DAOMeta updateIsClosedToYes = DAOHelper.getDAO("goodsarrival-dao", "update_is_closed_yes", null);
            String sql1 = updateIsClosedToYes.getStrSql();
            sql1 = sql1.replace("${selfConditions}", " UUID IN ('" + uuids + "')");
            updateIsClosedToYes.setStrSql(sql1);
            DAOMeta updateIsClosedToNo = DAOHelper.getDAO("goodsarrival-dao", "update_is_closed_no", null);
            String sql2 = updateIsClosedToNo.getStrSql();
            sql2 = sql2.replace("${selfConditions}", " UUID IN ('" + uuids + "')");
            updateIsClosedToNo.setStrSql(sql2);
            pcm.updateByNativeSQL(updateIsClosedToYes);
            pcm.updateByNativeSQL(updateIsClosedToNo);
        }
    }

    @Override
    public void confirmFreight(String uuid, String name) {
        //1.判断参数
        if (StringUtil.isEmpty(uuid)) {
            throw new BizException("没有uuid！");
        }
        //2.根据合同明细uuid找到合同ID
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        ScmContractDetail scmContractDetail = pcm.findByPK(ScmContractDetail.class, uuid);
        String scmContractId = scmContractDetail.getScmContractId();
        if (StringUtil.isEmpty(scmContractId)) {
            throw new BizException("合同ID不能为空！");
        }
        //3.根据合同ID找到所有的明细ID
        List paramList = new ArrayList<>();
        paramList.add(scmContractId);
        DAOMeta daoMeta = DAOHelper.getDAO("goodsarrival-dao", "find_contract_detail_name_freight", paramList);
        List<Map<String, Object>> scmContractDetailList = pcm.findByNativeSQL(daoMeta);
        String detailIds = "";
        for (Map<String, Object> stringObjectMap : scmContractDetailList) {
            String scmContractDetailUuid = (String) stringObjectMap.get(CommonAttribute.UUID);
            if (StringUtil.isNotEmpty(scmContractDetailUuid)) {
                detailIds += scmContractDetailUuid + "','";
            }
        }
        //4.根据合同明细IDS找到所有的到货确认为运费的合同明细ID
        List detailIdList = new ArrayList<>();
        List<PrmGoodsArrival> prmGoodsArrivalList = new ArrayList<>();
        if (StringUtil.isNotEmpty(detailIds)) {
            detailIds = detailIds.substring(0, detailIds.length() - 3);
            DAOMeta daoMeta2 = new DAOMeta();
            daoMeta2.setStrSql("SELECT SCM_CONTRACT_DETAIL_ID FROM PRM_GOODS_ARRIVAL WHERE SCM_CONTRACT_DETAIL_ID IN '" + detailIds + "' AND MATERIAL_NAME LIKE '%运费%'");
            List<Map<String, Object>> goodsArrivalList = pcm.findByNativeSQL(daoMeta2);
            if (ListUtil.isNotEmpty(goodsArrivalList)) {
                for (Map<String, Object> stringObjectMap : goodsArrivalList) {
                    String detailId = (String) stringObjectMap.get(PrmGoodsArrivalAttribute.SCM_CONTRACT_DETAIL_ID);
                    if (StringUtil.isNotEmpty(detailId)) {
                        detailIdList.add(detailId);
                    }
                }
            }
        }
        //5.如果找到的ID在deailIdList中，则该条记录不插进数据库
        if (ListUtil.isNotEmpty(scmContractDetailList)) {
            Date date = new Date();
            for (int i = 0; i < scmContractDetailList.size(); i++) {
                boolean continueFlag = false;
                PrmGoodsArrival prmGoodsArrival = new PrmGoodsArrival();
                String detailUuid = (String) scmContractDetailList.get(i).get("uuid");
                if (StringUtil.isNotEmpty(detailUuid)) {
                    prmGoodsArrival.setscmContractDetailId(detailUuid);
                    for (Object id : detailIdList) {
                        if (detailUuid.equals(id)) {
                            continueFlag = true;
                            break;
                        }
                    }
                }
                if (continueFlag) {
                    continue;
                }
                if (StringUtil.isNotEmpty(scmContractDetailList.get(i).get("materialName"))) {
                    prmGoodsArrival.setMaterialName((String) scmContractDetailList.get(i).get("materialName"));
                }
                if (StringUtil.isNotEmpty(scmContractDetailList.get(i).get("model"))) {
                    prmGoodsArrival.setModel((String) scmContractDetailList.get(i).get("model"));
                }
                if (StringUtil.isNotEmpty(scmContractDetailList.get(i).get("units"))) {
                    prmGoodsArrival.setUnits((String) scmContractDetailList.get(i).get("units"));
                }
                if (StringUtil.isNotEmpty(scmContractDetailList.get(i).get("remark"))) {
                    prmGoodsArrival.setRemark((String) scmContractDetailList.get(i).get("remark"));
                }
                prmGoodsArrival.setArriveDate(date);
                if (StringUtil.isNotEmpty(scmContractDetailList.get(i).get("projectId"))) {
                    prmGoodsArrival.setPrmProjectMainId((String) scmContractDetailList.get(i).get("projectId"));
                }
                if (StringUtil.isNotEmpty(scmContractDetailList.get(i).get("amount"))) {
                    prmGoodsArrival.setAmount((BigDecimal) scmContractDetailList.get(i).get("amount"));
                }
                if (StringUtil.isNotEmpty(name)) {
                    prmGoodsArrival.setRegistrant(name);
                }
                prmGoodsArrivalList.add(prmGoodsArrival);
                pcm.batchInsert(prmGoodsArrivalList);
            }
        }
    }
}
