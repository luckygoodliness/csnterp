package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.action;

import com.csnt.scdp.bizmodules.entity.prm.*;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetAccessoryAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetOutsourceAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetOutsourceCAttribute;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmBudgetPrincipalAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf.PrmprojectmaincManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.bo.QueryCondition;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.MapUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entity.ScdpOrg;
import com.csnt.scdp.sysmodules.entityattributes.ScdpOrgAttribute;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by as on 2017/2/28.
 */
@Scope("singleton")
@Controller("prmprojectmainc-mark")
@Transactional
public class MarkAction implements IAction {
    @Resource(name = "prmprojectmainc-manager")
    private PrmprojectmaincManager prmprojectmaincManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap();
        String markType = (String) inMap.get("markType");
        //标记项目
        if (markType.equals("mark")) {
            markProjectIsStamp(inMap,"1");
        //取消标记
        } else if (markType.equals("unMark")) {
            markProjectIsStamp(inMap,"");
        }
        //Do After
        return out;
    }
    //标记或取消  项目中 外协预算、设备材料、辅助材料
    public void markProjectIsStamp(Map inMap,String isStamp) {
        String uuid=(String) inMap.get("uuid");
        //外协预算prmBudgetOutsourceC
        String outsourceUuids = (String) inMap.get("outsourceUuids");
        List<PrmBudgetOutsourceC> lstOutsource = new ArrayList<PrmBudgetOutsourceC>();
        if (outsourceUuids.length() > 0){
            String[] ouuids = outsourceUuids.substring(0, outsourceUuids.length() - 1).split(",");
            List lstOuuids=new ArrayList<String>();
            for (int i = 0; i < ouuids.length; i++) {
                lstOuuids.add(ouuids[i]);
            }
            QueryCondition condition1 = new QueryCondition();
            condition1.setField(CommonAttribute.UUID);
            condition1.setOperator("in");
            condition1.setValueList(lstOuuids);
            List<QueryCondition> lstCondition = new ArrayList<QueryCondition>();
            lstCondition.add(condition1);
            List<PrmBudgetOutsourceC> prmBudgetOutsourceC=PersistenceFactory.getInstance().findByAnyFields(PrmBudgetOutsourceC.class, lstCondition, null);
            if(ListUtil.isNotEmpty(prmBudgetOutsourceC)) {
                for (PrmBudgetOutsourceC pbo : prmBudgetOutsourceC) {
                    if(!isStamp.equals(pbo.getIsStamp()) && uuid.equals(pbo.getPrmProjectMainCId())) {
                        pbo.setIsStamp(isStamp);
                        lstOutsource.add(pbo);
                    }
                }
            }

        }

        //设备材料 PrmBudgetPrincipalC
        String principalUuids = (String)inMap.get("principalUuids");
        List<PrmBudgetPrincipalC> lstPrincipal=new ArrayList<PrmBudgetPrincipalC>();
        if (principalUuids.length() > 0) {
            String[] puuids = principalUuids.substring(0, principalUuids.length() - 1).split(",");
            List lstPuuids=new ArrayList<String>();
            for (int i = 0; i < puuids.length; i++) {
                lstPuuids.add(puuids[i]);
            }
            QueryCondition condition1 = new QueryCondition();
            condition1.setField(CommonAttribute.UUID);
            condition1.setOperator("in");
            condition1.setValueList(lstPuuids);
            List<QueryCondition> lstCondition = new ArrayList<QueryCondition>();
            lstCondition.add(condition1);
            List<PrmBudgetPrincipalC> prmBudgetPrincipalC=PersistenceFactory.getInstance().findByAnyFields(PrmBudgetPrincipalC.class, lstCondition, null);
            if(ListUtil.isNotEmpty(prmBudgetPrincipalC)) {
                for (PrmBudgetPrincipalC pbp : prmBudgetPrincipalC) {
                    if(!isStamp.equals(pbp.getIsStamp()) && uuid.equals(pbp.getPrmProjectMainCId())) {
                        pbp.setIsStamp(isStamp);
                        lstPrincipal.add(pbp);
                    }
                }
            }
        }
        //辅助材料PrmBudgetAccessoryC
        String accessoryUuids = (String)inMap.get("accessoryUuids");
        List<PrmBudgetAccessoryC> lstAccessory=new ArrayList<PrmBudgetAccessoryC>();
        if (accessoryUuids.length() > 0) {
            String[] auuids = accessoryUuids.substring(0, accessoryUuids.length() - 1).split(",");
            List lstAuuids=new ArrayList<String>();
            for (int i = 0; i < auuids.length; i++) {
                lstAuuids.add(auuids[i]);
            }
            QueryCondition condition1 = new QueryCondition();
            condition1.setField(CommonAttribute.UUID);
            condition1.setOperator("in");
            condition1.setValueList(lstAuuids);
            List<QueryCondition> lstCondition = new ArrayList<QueryCondition>();
            lstCondition.add(condition1);
            List<PrmBudgetAccessoryC> lst=PersistenceFactory.getInstance().findByAnyFields(PrmBudgetAccessoryC.class, lstCondition, null);
            if(ListUtil.isNotEmpty(lst)) {
                for (PrmBudgetAccessoryC pba : lst) {
                    if(!isStamp.equals(pba.getIsStamp()) && uuid.equals(pba.getPrmProjectMainCId())) {
                        pba.setIsStamp(isStamp);
                        lstAuuids.add(pba);
                    }
                }
            }
        }
        if(ListUtil.isNotEmpty(lstOutsource)) {
            PersistenceFactory.getInstance().batchUpdate(lstOutsource);
        }
        if(ListUtil.isNotEmpty(lstPrincipal)) {
            PersistenceFactory.getInstance().batchUpdate(lstPrincipal);
        }
        if(ListUtil.isNotEmpty(lstAccessory)) {
            PersistenceFactory.getInstance().batchUpdate(lstAccessory);
        }
    }


}
