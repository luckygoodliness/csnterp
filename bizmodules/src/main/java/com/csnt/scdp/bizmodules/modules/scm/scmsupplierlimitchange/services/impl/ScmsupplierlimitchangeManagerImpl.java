package com.csnt.scdp.bizmodules.modules.scm.scmsupplierlimitchange.services.impl;

import com.csnt.scdp.bizmodules.entity.scm.ScmSupplierLimit;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplierLimitChange;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplierLimitChangeD;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplierLimitDetail;
import com.csnt.scdp.bizmodules.modules.scm.scmsupplierlimitchange.dto.ScmSupplierLimitChangeDDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsupplierlimitchange.dto.ScmSupplierLimitChangeDto;
import com.csnt.scdp.bizmodules.modules.scm.scmsupplierlimitchange.services.intf.ScmsupplierlimitchangeManager;
import com.csnt.scdp.bizmodules.modules.scm.supplierlimit.dto.ScmSupplierLimitDetailDto;
import com.csnt.scdp.bizmodules.modules.scm.supplierlimit.dto.ScmSupplierLimitDto;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.helper.DAOHelper;
import com.csnt.scdp.framework.helper.DtoHelper;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  ScmsupplierlimitchangeManagerImpl
 * Copyright: © 2016 CSNT. All rights reserved.
 * Company: CSNT
 *
 * @author code-generator
 * @version 1.0
 * @timestamp 2016-07-22 16:17:18
 */

@Scope("singleton")
@Service("scmsupplierlimitchange-manager")
public class ScmsupplierlimitchangeManagerImpl implements ScmsupplierlimitchangeManager {

    public List<Map<String, Object>> selectCurAmount(String uuid) {
        DAOMeta daoMeta = new DAOMeta();
        String reqSql = "SELECT SC.SUPPLIER_CODE," +
                " SUM(SC.AMOUNT) AS CUR_AMOUNT," +
                " COUNT(1) AS CUR_VOLUME" +
                " FROM SCM_CONTRACT SC" +
                " WHERE SC.STATE = 2" +
                " and exists (select 1 FROM SCM_SUPPLIER_LIMIT_CHANGE_D  D WHERE D.SCM_SUPPLIER_LIMIT_CHANGE_ID='" + uuid + "' AND SC.SUPPLIER_CODE=D.SCM_SUPPLIER_ID)" +
                " AND SC.SUPPLIER_CODE IS NOT NULL" +
                " GROUP BY SC.SUPPLIER_CODE";
        daoMeta.setStrSql(reqSql);
        List<Map<String, Object>> reqList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return reqList;
    }

    public List<Map<String, Object>> selectContract(String uuid) {
        String selfCondition = "T.STATE = 2" +
                " and exists (select 1 " +
                "FROM SCM_SUPPLIER_LIMIT_CHANGE_D D " +
                "WHERE D.SCM_SUPPLIER_LIMIT_CHANGE_ID = '" + uuid + "'" +
                "AND T.SUPPLIER_CODE = D.SCM_SUPPLIER_ID)" +
                "AND T.SUPPLIER_CODE IS NOT NULL";
        DAOMeta daoMeta = DAOHelper.getDAO("scmcontract-dao", "common_query", null);
        String sql = daoMeta.getStrSql();
        sql = sql.replace("and ${selfconditions}", selfCondition);
        sql = sql.replace("${conditions}", "");
        sql = sql.replace("and ${voidfilter}", "");
        sql = sql + " order by nvl(effective_date,create_time+1/3) desc,scm_contract_code desc";
        daoMeta.setStrSql(sql);
        List<Map<String, Object>> contractList = PersistenceFactory.getInstance().findByNativeSQL(daoMeta);
        return contractList;
    }

    public void replaceMxxAmount(String uuid) {
        ScmSupplierLimitChangeDto scmSupplierLimitChangeDto = (ScmSupplierLimitChangeDto) DtoHelper.findDtoByPK(ScmSupplierLimitChangeDto.class, uuid);
        PersistenceCrudManager pcm = PersistenceFactory.getInstance();
        List<ScmSupplierLimit> scmSupplierList = new ArrayList<>();
        Map mapContractId = new HashMap();
        mapContractId.put("year", scmSupplierLimitChangeDto.getYear());
        ScmSupplierLimitDto scmSupplierLimitDto = (ScmSupplierLimitDto) DtoHelper.findByAnyFields(ScmSupplierLimitDto.class, mapContractId, null).get(0);
        if (ListUtil.isNotEmpty(scmSupplierLimitChangeDto.getScmSupplierLimitChangeDDto())) {
            List<ScmSupplierLimitDetailDto> scmSupplierLimitDetailDtoList = scmSupplierLimitDto.getScmSupplierLimitDetailDto();
            for (int i = 0; i < scmSupplierLimitChangeDto.getScmSupplierLimitChangeDDto().size(); i++) {
                boolean flag = true;
                ScmSupplierLimitChangeDDto scmSupplierLimitChangeDDto = scmSupplierLimitChangeDto.getScmSupplierLimitChangeDDto().get(i);
                if (ListUtil.isNotEmpty(scmSupplierLimitDto.getScmSupplierLimitDetailDto())) {
                    for (int j = 0; j < scmSupplierLimitDto.getScmSupplierLimitDetailDto().size(); j++) {
                        ScmSupplierLimitDetailDto scmSupplierLimitDetailDto = scmSupplierLimitDto.getScmSupplierLimitDetailDto().get(j);
                        if (scmSupplierLimitChangeDDto.getScmSupplierId().equals(scmSupplierLimitDetailDto.getScmSupplierId())) {
                            if (StringUtil.isNotEmpty(scmSupplierLimitChangeDDto.getMaxAmount())) {
                                scmSupplierLimitDetailDto.setMaxAmount(scmSupplierLimitChangeDDto.getMaxAmount());
                            }
                            if (StringUtil.isNotEmpty(scmSupplierLimitChangeDDto.getMaxVolume())) {
                                scmSupplierLimitDetailDto.setMaxVolume(scmSupplierLimitChangeDDto.getMaxVolume());
                            }
                            flag = false;
                            scmSupplierLimitDetailDto.setEditflag("*");
                        }
                    }
                }
                if (flag) {
                    ScmSupplierLimitDetailDto scmSupplierLimitDetailDto = new ScmSupplierLimitDetailDto();
                    scmSupplierLimitDetailDto.setScmSupplierId(scmSupplierLimitChangeDDto.getScmSupplierId());
                    scmSupplierLimitDetailDto.setScmSupplierLimitId(scmSupplierLimitDto.getUuid());
                    if (StringUtil.isNotEmpty(scmSupplierLimitChangeDDto.getMaxAmount())) {
                        scmSupplierLimitDetailDto.setMaxAmount(scmSupplierLimitChangeDDto.getMaxAmount());
                    }
                    if (StringUtil.isNotEmpty(scmSupplierLimitChangeDDto.getMaxVolume())) {
                        scmSupplierLimitDetailDto.setMaxVolume(scmSupplierLimitChangeDDto.getMaxVolume());
                    }
                    scmSupplierLimitDetailDto.setRemark(scmSupplierLimitChangeDDto.getRemark());
                    scmSupplierLimitDetailDto.setEditflag("+");
                    scmSupplierLimitDetailDtoList.add(scmSupplierLimitDetailDto);
                }
            }
            scmSupplierLimitDto.setEditflag("*");
            DtoHelper.CascadeSave(scmSupplierLimitDto);
        }

    }
}