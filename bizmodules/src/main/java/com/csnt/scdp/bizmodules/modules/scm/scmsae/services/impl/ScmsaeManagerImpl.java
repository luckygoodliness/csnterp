package com.csnt.scdp.bizmodules.modules.scm.scmsae.services.impl;

import com.csnt.scdp.bizmodules.modules.scm.scmsae.dto.ScmSaeDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsae.services.intf.ScmsaeManager;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.sysmodules.entityattributes.ScdpMsgTemplateAttribute;
import com.csnt.scdp.sysmodules.helper.MsgSendHelper;
import com.csnt.scdp.sysmodules.modules.sys.msgcenter.dto.ReceiptsMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  ScmsaeManagerImpl
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-08-23 19:38:26
 */

@Scope("singleton")
@Service("scmsae-manager")
public class ScmsaeManagerImpl implements ScmsaeManager {

    @Override
    public String createScmSaeForm(String scmSaeId) {
        List lastParam = new ArrayList();
        lastParam.add(scmSaeId);
        String message = (PersistenceFactory.getInstance().getDataFromProcOrFunc("call PKG_SCM_SAE.SP_CREATE_FROM(?,?)", lastParam, PersistenceCrudManager.RETURN_DATA_TYPE.VARCHAR)).toString();
        return message;
    }

    @Override
    public String computeSaeResult(String scmSaeId, double rate) {
        List lastParam = new ArrayList();
        lastParam.add(scmSaeId);
        lastParam.add(rate);
        String message = (PersistenceFactory.getInstance().getDataFromProcOrFunc("call PKG_SCM_SAE.SP_COMPUTE_RESULT(?,?,?)", lastParam, PersistenceCrudManager.RETURN_DATA_TYPE.VARCHAR)).toString();
        return message;
    }

    @Override
    public String createScmSaeTask(String scmSaeId, String beginTime, String endTime, String remark) {
        List lastParam = new ArrayList();
        lastParam.add(scmSaeId);
        lastParam.add(beginTime);
        lastParam.add(endTime);
        lastParam.add(remark);
        sendTaskMessage(scmSaeId);
        String message = (PersistenceFactory.getInstance().getDataFromProcOrFunc("call PKG_SCM_SAE.SP_CREATE_TASK(?,?,?,?,?)", lastParam, PersistenceCrudManager.RETURN_DATA_TYPE.VARCHAR)).toString();
        return message;
    }

    // 发送考评消息给评分人
    private void sendTaskMessage(String scmSaeId) {
        ScmSaeDto scmSaeDto = (ScmSaeDto)DtoHelper.findDtoByPK(ScmSaeDto.class, scmSaeId);
        String title = scmSaeDto.getTitle();
        String sql = "SELECT DISTINCT SSF.USER_CODE AS USER_CODE\n" +
                "FROM SCM_SAE_FORM SSF\n" +
                "WHERE SSF.SCM_SAE_TASK_ID IS NULL\n" +
                "AND EXISTS (\n" +
                "    SELECT 1\n" +
                "    FROM SCM_SAE_OBJECT SSO\n" +
                "    WHERE SSO.UUID = SSF.SCM_SAE_OBJECT_ID\n" +
                "    AND SSO.SCM_SAE_ID = '"+scmSaeId+"'\n" +
                ")";
        DAOMeta daoMeta = new DAOMeta();
        daoMeta.setStrSql(sql);
        List<Map<String, Object>> userList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        String msgType = "IMSG";
        String templateNo = "SCM_SAE_TASK";
        userList.forEach(record -> {
            // 获取评价人
            List<String> userIdLst = new ArrayList<>();
            ReceiptsMeta receiptsMeta = new ReceiptsMeta();
            String userId = (String)record.get("userCode");
            userIdLst.add(userId);
            receiptsMeta.setLstSendToUserId(userIdLst);

            Map map = new HashMap<>();
            map.put("title", title);
            MsgSendHelper.sendMsg(map, msgType, templateNo, receiptsMeta);

        });
    }

}