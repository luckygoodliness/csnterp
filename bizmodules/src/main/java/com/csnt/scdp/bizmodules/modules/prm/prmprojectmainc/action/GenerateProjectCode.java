package com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.action;

import com.csnt.scdp.bizmodules.entity.prm.PrmProjectMainC;
import com.csnt.scdp.bizmodules.entityattributes.prm.PrmProjectMainCAttribute;
import com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.services.intf.PrmprojectmaincManager;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.ObjectUtil;
import com.csnt.scdp.framework.util.StringUtil;
import com.csnt.scdp.sysmodules.entityattributes.ScdpFmNumberPoolAttribute;
import com.csnt.scdp.sysmodules.helper.NumberingHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
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
 * @timestamp 2015-09-27 15:28:19
 */

@Scope("singleton")
@Controller("prmprojectmainc-generate-projectcode")
@Transactional
public class GenerateProjectCode implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(GenerateProjectCode.class);

    @Resource(name = "prmprojectmainc-manager")
    private PrmprojectmaincManager prmprojectmaincManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        //Do before
        Map out = new HashMap();
        String uuid = (String) inMap.get(CommonAttribute.UUID);
        String step = (String) inMap.get("step");
        String projectName = (String) inMap.get("projectName");
        String contractorOffice = (String) inMap.get("contractorOffice");
        String projectCode = null;
        if ("0".equals(step)) {
            //预览
            String stampType = (String) inMap.get(PrmProjectMainCAttribute.STAMP_TYPE);
            String prmCode = (String) inMap.get(PrmProjectMainCAttribute.PRM_CODE);
            projectCode = prmprojectmaincManager.generateProjectCode(uuid, stampType, prmCode, true);
        } else if ("1".equals(step)) {
            //新生成
            String stampType = (String) inMap.get(PrmProjectMainCAttribute.STAMP_TYPE);
            String prmCode = (String) inMap.get(PrmProjectMainCAttribute.PRM_CODE);
            projectCode = (String) inMap.get(PrmProjectMainCAttribute.PROJECT_CODE);
            String projectCodePreview = (String) inMap.get(PrmProjectMainCAttribute.PROJECT_CODE_PREVIEW);
            //未预览或者预览后未做修改，则从序号生成器中重新生成
            if (StringUtil.isEmpty(projectCode)) {
                projectCode = prmprojectmaincManager.generateProjectCode(uuid, stampType, prmCode);
            } else if (projectCode.length() >= 5) {
                if (ObjectUtil.beSame(projectCode.substring(projectCode.length() - 5), projectCodePreview
                        .substring(projectCodePreview.length() - 5))) {
                    prmprojectmaincManager.generateProjectCode(uuid, stampType, prmCode);
                }
            }

            PrmProjectMainC headerDto = PersistenceFactory.getInstance().findByPK(PrmProjectMainC.class, uuid);
            String prmProjectMainId = headerDto.getPrmProjectMainId();
            prmprojectmaincManager.updateProjectCode(uuid, prmProjectMainId, projectCode);
            if (StringUtil.isEmpty(prmProjectMainId)) {
                prmprojectmaincManager.invokeApproveAction(uuid);
            }
            //发送消息给出纳
            prmprojectmaincManager.sendMsg(projectCode, projectName, contractorOffice, step);
        } else if ("2".equals(step)) {
            //更新
            projectCode = (String) inMap.get(PrmProjectMainCAttribute.PROJECT_CODE);
            PrmProjectMainC headerDto = PersistenceFactory.getInstance().findByPK(PrmProjectMainC.class, uuid);
            String prmProjectMainId = headerDto.getPrmProjectMainId();
            prmprojectmaincManager.updateProjectCode(uuid, prmProjectMainId, projectCode);

            String newSeqNo = getSeqNo(projectCode);
            if (StringUtil.isNotEmpty(newSeqNo) && newSeqNo.length() >= 4) {
                //increase seq
                String newCodeType = getCodeType(projectCode);
                String newStampType = getStampType(projectCode);
                Map mapInput = new HashMap();
                mapInput.put("SYSDATE", new Date());
                mapInput.put("STAMPTYPE", newStampType);
                mapInput.put("SIJITYPE", newCodeType);
                String previewCode = NumberingHelper.getNumbering("PROJECT_CODE", mapInput, true);
                String previewSeqNo = getSeqNo("-" + previewCode);
                if (ObjectUtil.beSame(previewSeqNo, newSeqNo)) {
                    NumberingHelper.getNumbering("PROJECT_CODE", mapInput);
                }
            }

            String oldProjectCode = headerDto.getProjectCode();
            String oldSeqNo = getSeqNo(oldProjectCode);
            if (StringUtil.isNotEmpty(oldSeqNo) && oldSeqNo.length() >= 4) {
                //rollback seq
                String oldCodeType = getCodeType(oldProjectCode);
                String querySql = "select * from scdp_fm_number_pool where pool_code=? and pool_key=? ";
                List lstParam = new ArrayList();
                lstParam.add("PROJECT_CODE");
                lstParam.add(oldCodeType);
                DAOMeta queryMeta = new DAOMeta(querySql, lstParam);
                List<Map<String, Object>> lstResult = PersistenceFactory.getInstance().findByNativeSQL(queryMeta);
                if (ListUtil.isNotEmpty(lstResult)) {
                    String numStr = String.valueOf(lstResult.get(0).get(ScdpFmNumberPoolAttribute.CURRENT_NUMBER));
                    for (int i = numStr.length(); i < 4; i++) {
                        numStr = "0" + numStr;
                    }
                    if (ObjectUtil.beSame(oldSeqNo, numStr)) {
                        String updateSql = "update scdp_fm_number_pool pool set current_number=current_number-1 where pool_code=? and pool_key=? ";
                        List lstParam2 = new ArrayList();
                        lstParam2.add("PROJECT_CODE");
                        lstParam2.add(oldCodeType);
                        DAOMeta updateMeta = new DAOMeta(updateSql, lstParam2);
                        PersistenceFactory.getInstance().updateByNativeSQL(updateMeta);

                    }

                }
            }
            //发送消息给出纳
            prmprojectmaincManager.sendMsg(projectCode, projectName, contractorOffice, step);
        }

        out.put(PrmProjectMainCAttribute.PROJECT_CODE, projectCode);
        //Do After
        return out;
    }

    private String getStampType(String projectCode) {
        if (StringUtil.isNotEmpty(projectCode)) {
            int taxIndex = projectCode.indexOf("+");
            if (taxIndex < 0) {
                taxIndex = projectCode.indexOf("-");
            }
            if (taxIndex < 0) {
                taxIndex = projectCode.indexOf("/");
            }
            if (taxIndex > -1 && projectCode.length() > taxIndex + 5) {
                return projectCode.substring(taxIndex + 1, taxIndex + 2);
            }
        }
        return null;
    }

    private String getSeqNo(String projectCode) {
        if (StringUtil.isNotEmpty(projectCode)) {
            int taxIndex = projectCode.indexOf("+");
            if (taxIndex < 0) {
                taxIndex = projectCode.indexOf("-");
            }
            if (taxIndex < 0) {
                taxIndex = projectCode.indexOf("/");
            }
            if (taxIndex > -1 && projectCode.length() > taxIndex + 5) {
                return projectCode.substring(taxIndex + 3);
            }
        }
        return null;
    }

    private String getCodeType(String projectCode) {
        if (StringUtil.isNotEmpty(projectCode)) {
            int taxIndex = projectCode.indexOf("+");
            if (taxIndex < 0) {
                taxIndex = projectCode.indexOf("-");
            }
            if (taxIndex < 0) {
                taxIndex = projectCode.indexOf("/");
            }
            if (taxIndex > -1 && projectCode.length() > taxIndex + 5) {
                return projectCode.substring(taxIndex + 2, taxIndex + 3);
            }
        }
        return null;
    }

}
