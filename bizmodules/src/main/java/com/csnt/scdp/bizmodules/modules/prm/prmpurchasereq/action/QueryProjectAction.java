package com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.action;

import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.framework.commonaction.crud.CommonQueryAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据所属项目查询接货人，联系方式，和上一次使用的地址
 * Created by xiezf on 2016/8/10.
 */
@Scope("singleton")
@Controller("prmpurchasereq-projectquery")
@Transactional
public class QueryProjectAction extends CommonQueryAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(QueryAction.class);

    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //项目id
        String prmProjectMainId = (String)inMap.get("prmProjectMainId");
        Map out = new HashMap<>();
        String sql = "select t1.ARRIVE_LOCATION,\n" +
                "                t1.CONSIGNEE,\n" +
                "                t1.CONTACT_WAY\n" +
                "  FROM (select distinct PRM_PURCHASE_REQ_ID,\n" +
                "               ARRIVE_LOCATION,\n" +
                "               CONSIGNEE,\n" +
                "               CONTACT_WAY,\n" +
                "               update_time\n" +
                "          from PRM_PURCHASE_REQ_DETAIL\n" +
                "         order by update_time desc) t1\n" +
                " WHERE t1.PRM_PURCHASE_REQ_ID in\n" +
                "       (select t2.uuid\n" +
                "          from PRM_PURCHASE_REQ t2\n" +
                "         where t2.PRM_PROJECT_MAIN_ID = '" + prmProjectMainId + "')\n" +
                "   and rownum = 1";


        DAOMeta daoMeta=new DAOMeta();
        daoMeta.setStrSql(sql);
        daoMeta.setNeedFilter(false);
        List<Map<String,Object>> retList= PersistenceFactory.getInstance().findByNativeSQL(daoMeta);

        if (!ListUtil.isEmpty(retList)){
            out.put("hasData",true);
            //上一次的接货地点
            out.put("lastArriverLocation",retList.get(0).get("arriveLocation"));
            //收货人
            out.put("consignee",retList.get(0).get("consignee"));
            //联系方式
            out.put("contactWay",retList.get(0).get("contactWay"));
        }else{
            out.put("hasData",false);
        }
        return out;
    }
}
