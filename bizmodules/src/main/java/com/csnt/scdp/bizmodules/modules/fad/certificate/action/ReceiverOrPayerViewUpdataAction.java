package com.csnt.scdp.bizmodules.modules.fad.certificate.action;

import com.csnt.scdp.bizmodules.entity.fad.FadReceiverOrPayerView;
import com.csnt.scdp.bizmodules.entity.prm.PrmCustomer;
import com.csnt.scdp.bizmodules.entity.scm.ScmContractInvoice;
import com.csnt.scdp.bizmodules.entity.scm.ScmSupplier;
import com.csnt.scdp.bizmodules.entityattributes.fad.FadReceiverOrPayerViewAttribute;
import com.csnt.scdp.bizmodules.entityattributes.scm.ScmContractInvoiceAttribute;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.sysmodules.entity.ScdpUser;
import com.csnt.scdp.sysmodules.entityattributes.ScdpUserAttribute;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.csnt.scdp.framework.commonaction.crud.*;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Reese on 2015/10/14.
 */
@Scope("singleton")
@Controller("certificate-receiverOrPayerViewUpdata")
@Transactional
public class ReceiverOrPayerViewUpdataAction extends CommonLoadAction implements IAction {

    private static ILogTracer tracer = LogTracerFactory.getInstance(ReceiverOrPayerViewUpdataAction.class);
    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map result = new HashMap<>();
        String receiverOrPayerType = certificateManager.isNullReturnEmpty(inMap.get("receiverOrPayerType"));
        String receiverOrPayerId = certificateManager.isNullReturnEmpty(inMap.get("receiverOrPayerId"));
        String receiverOrPayerCode = certificateManager.isNullReturnEmpty(inMap.get("receiverOrPayerCode"));
        String receiverOrPayerName = certificateManager.isNullReturnEmpty(inMap.get("receiverOrPayerName"));
        String receiverOrPayerNcCode = certificateManager.isNullReturnEmpty(inMap.get("receiverOrPayerNcCode"));

        if (certificateManager.isNullReturnEmpty(receiverOrPayerType).equals("SCM_SUPPLIER")) {
            ScmSupplier scmSupplier = certificateManager.isNullReturnEmpty(receiverOrPayerId).length() > 0 ? PersistenceFactory.getInstance().findByPK(ScmSupplier.class, receiverOrPayerId) : null;
            scmSupplier.setNcCode(receiverOrPayerNcCode);
            PersistenceFactory.getInstance().update(scmSupplier);
        } else if (certificateManager.isNullReturnEmpty(receiverOrPayerType).equals("PRM_CUSTOMER")) {
            PrmCustomer prmCustomer = certificateManager.isNullReturnEmpty(receiverOrPayerId).length() > 0 ? PersistenceFactory.getInstance().findByPK(PrmCustomer.class, receiverOrPayerId) : null;
            prmCustomer.setNcCode(receiverOrPayerNcCode);
            PersistenceFactory.getInstance().update(prmCustomer);
        } else if (certificateManager.isNullReturnEmpty(receiverOrPayerType).equals("SCDP_USER")) {
            Map<String, Object> scdpUserConditionsMap = new HashMap<String, Object>();
            scdpUserConditionsMap.put(ScdpUserAttribute.USER_ID, receiverOrPayerCode);
            List<ScdpUser> scdpUserList = certificateManager.isNullReturnEmpty(receiverOrPayerCode).length() > 0 ? PersistenceFactory.getInstance().findByAnyFields(ScdpUser.class, scdpUserConditionsMap, null) : new ArrayList<>();
            ScdpUser scdpUser = (ScdpUser) scdpUserList.get(0);
            String sql = "UPDATE SCDP_EXPAND_ROW SET EXPAND_VALUE = '" + receiverOrPayerNcCode + "' WHERE EXPAND_CODE='NC_NUMBER' AND DATA_UUID = '" + certificateManager.isNullReturnEmpty(scdpUser.getUuid()) + "'";
            DAOMeta daoMeta = new DAOMeta();
            daoMeta.setStrSql(sql);
            daoMeta.setNeedFilter(false);
            if (!(PersistenceFactory.getInstance().updateByNativeSQL(daoMeta) > 0)) {
                sql = "INSERT INTO SCDP_EXPAND_ROW(UUID,EXPAND_ID,EXPAND_CODE,DATA_UUID,EXPAND_VALUE,COMPANY_UUID,CREATE_BY,CREATE_TIME,UPDATE_BY,UPDATE_TIME,IS_VOID,LOC_TIMEZONE,TBL_VERSION)\n" +
                        "SELECT\n" +
                        "SYS_GUID() UUID,\n" +
                        "(SELECT UUID FROM SCDP_EXPAND_COLUMN WHERE EXPAND_CODE='NC_NUMBER') EXPAND_ID,\n" +
                        "'NC_NUMBER' EXPAND_CODE,\n" +
                        "'" + certificateManager.isNullReturnEmpty(scdpUser.getUuid()) + "' DATA_UUID,\n" +
                        "'" + receiverOrPayerNcCode + "' EXPAND_VALUE,\n" +
                        "(SELECT COMPANY_UUID FROM SCDP_EXPAND_COLUMN WHERE EXPAND_CODE='NC_NUMBER') COMPANY_UUID,\n" +
                        "(SELECT CREATE_BY FROM SCDP_EXPAND_COLUMN WHERE EXPAND_CODE='NC_NUMBER') CREATE_BY,\n" +
                        "sysdate CREATE_TIME,\n" +
                        "(SELECT UPDATE_BY FROM SCDP_EXPAND_COLUMN WHERE EXPAND_CODE='NC_NUMBER') UPDATE_BY,\n" +
                        "sysdate UPDATE_TIME,\n" +
                        "0 IS_VOID,\n" +
                        "(SELECT LOC_TIMEZONE FROM SCDP_EXPAND_COLUMN WHERE EXPAND_CODE='NC_NUMBER') LOC_TIMEZONE,\n" +
                        "SYS_GUID() TBL_VERSION\n" +
                        "FROM\n" +
                        "DUAL";
                daoMeta = new DAOMeta();
                daoMeta.setStrSql(sql);
                daoMeta.setNeedFilter(false);
                PersistenceFactory.getInstance().updateByNativeSQL(daoMeta);
            }
        }

        return result;
    }
}
