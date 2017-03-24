package com.csnt.scdp.bizmodules.modules.prm.goodsarrival.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmGoodsArrival;
import com.csnt.scdp.bizmodules.modules.prm.goodsarrival.services.intf.GoodsarrivalManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/4.
 */
@Scope("singleton")
@Controller("goodsarrival-fill")
@Transactional
public class FillAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(FillAction.class);

    @Resource(name = "goodsarrival-manager")
    private GoodsarrivalManager goodsarrivalManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        List uuidLst = (List) inMap.get("uuidLst");
        String uuid = "";
        //1.判断是否存在全部确认到货的数据，如果有则不继续操作
        if (ListUtil.isNotEmpty(uuidLst)) {
            uuid = uuidLst.toString().replace(" ", "").replace("[", "").replace("]", "").replace(",", "','");
        } else {
            throw new BizException("没有选择记录！");
        }
        //只有数据不存在有全部确认到货的数据，才可以批量到货
        DAOMeta daoMeta = DAOHelper.getDAO("goodsarrival-dao", "query_if_all_confirm_records", null);
        String sql = daoMeta.getStrSql();
        sql = sql.replace("${selfConditions}", " UUID IN ('" + uuid + "')");
        daoMeta.setStrSql(sql);
        List tempList = pcm.findByNativeSQL(daoMeta);
        if (ListUtil.isNotEmpty(tempList)) {
            if (tempList.size() == uuidLst.size()) {
                throw new BizException("已经全部确认到货！");
            } else {
                throw new BizException("记录中存在已经完全确认到货的数据！");
            }
        }
        //如果前台传过来的actualAmount为空，则获取每条记录应该到货的数量
        List actualAmount = (List) inMap.get("actualAmount");
        List restAmountList = new ArrayList<>();
        if (ListUtil.isEmpty(actualAmount)) {
            DAOMeta restAmountDAOMeta = DAOHelper.getDAO("goodsarrival-dao", "get_rest_amount", null);
            String sql2 = restAmountDAOMeta.getStrSql();
            sql2 = sql2.replace("${selfConditions}", " UUID IN ('" + uuid + "')");
            restAmountDAOMeta.setStrSql(sql2);
            restAmountList = pcm.findByNativeSQL(restAmountDAOMeta);
        }
        Date date = new Date();
        String name = (String) inMap.get("name");
        List<PrmGoodsArrival> prmGoodsArrivalLst = new ArrayList<PrmGoodsArrival>();
        //根据合同明细表向到货确认表添加数据
        List<Map> scmContractDetailList = goodsarrivalManager.getScmContractDetailForUUID(uuid);
        if (ListUtil.isNotEmpty(scmContractDetailList)) {
            for (int i = 0; i < scmContractDetailList.size(); i++) {
                boolean continueFlag = false;
                PrmGoodsArrival prmGoodsArrival = new PrmGoodsArrival();
                String detailUuid = (String) scmContractDetailList.get(i).get("uuid");
                if (StringUtil.isNotEmpty(detailUuid)) {
                    prmGoodsArrival.setscmContractDetailId(detailUuid);
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
                if (ListUtil.isNotEmpty(actualAmount)) {
                    prmGoodsArrival.setAmount(new BigDecimal(actualAmount.get(i)+""));
                } else {
                    for (Object o : restAmountList) {
                        Map m = (Map) o;
                        String tmpId = (String) m.get("uuid");
                        BigDecimal tmpAmount = new BigDecimal(m.get("lastAmount").toString());
                        if (tmpAmount.compareTo(new BigDecimal(0)) <= 0) {
                            continueFlag = true;
                        }
                        if (tmpId.equals(detailUuid)) {
                            prmGoodsArrival.setAmount(tmpAmount);
                            break;
                        }
                    }
                    if (continueFlag) {
                        continue;
                    }
                }
                if (StringUtil.isNotEmpty(name)) {
                    prmGoodsArrival.setRegistrant(name);
                }
                prmGoodsArrivalLst.add(prmGoodsArrival);
            }
            pcm.batchInsert(prmGoodsArrivalLst);
            //更新结算状态
            goodsarrivalManager.updateIsClosed(uuid);
            //如果是单条确认，则把运费项更新进到货确认表
            if (uuidLst.size() == 1) {
                goodsarrivalManager.confirmFreight(uuid, name);
            }
        }
        return null;
    }
}
