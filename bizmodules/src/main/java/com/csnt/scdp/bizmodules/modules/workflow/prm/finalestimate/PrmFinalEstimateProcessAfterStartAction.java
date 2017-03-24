package com.csnt.scdp.bizmodules.modules.workflow.prm.finalestimate;

import com.csnt.scdp.bizmodules.entity.prm.PrmFinalEstimate;
import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMain;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.util.ListUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("prm_final_estimates-after-start")
@Transactional
public class PrmFinalEstimateProcessAfterStartAction implements IAction {
    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map map = new HashedMap();
        String uuid = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        PersistenceCrudManager pcm =PersistenceFactory.getInstance();
        PrmFinalEstimate prmFinalEstimate= pcm.findByPK(PrmFinalEstimate.class, uuid);
        if(null!=prmFinalEstimate){
            String prmMainUuid=prmFinalEstimate.getPrmProjectMainId();
            String sql="SELECT PPM.UUID," +
                    "       NVL(PBD.CONTRACT_MONEY,0) AS CONTRACT_MONEY," +
                    "       NVL((SELECT SUM(PCM.CONTRACT_NOW_MONEY)" +
                    "             FROM PRM_CONTRACT_DETAIL PCM" +
                    "            WHERE PPM.UUID = PCM.PRM_PROJECT_MAIN_ID)," +
                    "           0) AS CONTRACT_TOTAL_MONEY" +
                    "  FROM PRM_PROJECT_MAIN PPM  LEFT JOIN PRM_BUDGET_DETAIL PBD ON PBD.PRM_PROJECT_MAIN_ID=PPM.UUID" +
                    " WHERE PPM.UUID = '"+prmMainUuid+"' AND PBD.BUDGET_CODE='CONTRACT_TOTAL'";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            List<Map<String, Object>> projectInfos = pcm.findByNativeSQL(daoMeta);
            if(ListUtil.isNotEmpty(projectInfos)){
                Map<String, Object> result = projectInfos.get(0);
                BigDecimal contractMoney =new BigDecimal(""+result.get("contractMoney"));
                BigDecimal contractTotalMoney =new BigDecimal(""+result.get("contractTotalMoney"));
                if(contractMoney.compareTo(contractTotalMoney)!=0){
                    throw new BizException("项目关联合同合计与立项预算明细合同合计不相等，不允许提交！");
                }
            }

        }
        return map;
    }
}
