package com.csnt.scdp.bizmodules.modules.workflow.scm.scmcontract;

import com.csnt.scdp.bizmodules.entity.scm.ScmContract;
import com.csnt.scdp.bizmodules.modules.common.helper.ErpWorkFlowHelper;
import com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.services.intf.PrmpurchasereqManager;
import com.csnt.scdp.bizmodules.modules.scm.scmcontract.services.intf.ScmcontractManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.attributes.WorkFlowAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.spring.BeanFactory;
import com.csnt.scdp.framework.util.BeanUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuhd on 2015/11/9.
 */
@Scope("singleton")
@Controller("scm_contract_prepare-after-reject")
@Transactional

public class ScmContractAfterRejectAction implements IAction {
    @Resource(name = "scmcontract-manager")
    private ScmcontractManager scmcontractManager;
    @Resource(name = "prmpurchasereq-manager")
    private PrmpurchasereqManager prmpurchasereqManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //合同状态(0、合同谈判1、合同编辑2、合同明细导入3、合同审批4、审批通过5、盖章生效6、合同扫描件上传7、合同废止)
        Map map = new HashedMap();
        String businessKey = (String) inMap.get(WorkFlowAttribute.BUSINESS_KEY);
        Integer contractState = 0;
        Integer state = 0;
        if (StringUtil.isNotEmpty(inMap.get("wf_status_closed")) && (Boolean) inMap.get("wf_status_closed")) {//退回到第一步
            contractState = 1;//合同编辑
            state = 5;//退回
        } else {//不是退回到第一步
            contractState = 3;
            state = 9;//已提交（退回）
        }
        List userList = (List) inMap.get(WorkFlowAttribute.WF_REJECTION_MSG_RECEIVERS_BETWEEN_CURR_AND_BACK2);
        if (ListUtil.isNotEmpty(userList)) {
            ErpWorkFlowHelper.sendMsgToRollback(inMap, userList);
        }
        inMap.put(WorkFlowAttribute.SKIP_COMMON_PROCESS_AFTER_ACTION, true);
        if (state != 1) {
            String sql = "update scm_contract set contract_state = ?,state=? where uuid = ?";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            List lstParam = new ArrayList();
            lstParam.add(contractState);
            lstParam.add(state);
            lstParam.add(businessKey);
            daoMeta.setLstParams(lstParam);
            PersistenceCrudManager pcm = PersistenceFactory.getInstance();
            pcm.updateByNativeSQL(daoMeta);
        }
        return map;
    }
}
