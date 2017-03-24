package com.csnt.scdp.bizmodules.modules.fad.certificate.action;

import com.atomikos.icatch.SysException;
import com.csnt.scdp.bizmodules.modules.fad.certificate.services.intf.CertificateManager;
import com.csnt.scdp.framework.commonaction.crud.CommonLoadAction;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.dao.DAOMeta;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuwm on 2016/8/17.
 */
@Scope("singleton")
@Controller("certificate-CollectionUpdate")
@Transactional
public class CollectionUpdateAction extends CommonLoadAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(CollectionUpdateAction.class);
    @Resource(name = "certificate-manager")
    private CertificateManager certificateManager;
    @Override
    public Map perform(Map inMap) throws BizException,SysException{
        Map out=new HashMap();
        String businessId=certificateManager.isNullReturnEmpty(inMap.get("businessId"));
        String originalFormCode=certificateManager.isNullReturnEmpty(inMap.get("originalFormCode"));
        String uuid=certificateManager.isNullReturnEmpty(inMap.get("uuid"));
        String sql="UPDATE FAD_CERTIFICATE SET BUSINESS_ID='"+businessId+"',ORIGINAL_FORM_CODE='"+originalFormCode+"'WHERE UUID='"+uuid+"'";
        DAOMeta doMetal=new DAOMeta();
        doMetal.setStrSql(sql);
        doMetal.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(doMetal);
        String sqlReceipts="UPDATE PRM_RECEIPTS SET STATE='"+4+"' WHERE UUID='"+businessId+"'";
        DAOMeta doReceipts=new DAOMeta();
        doReceipts.setStrSql(sqlReceipts);
        doReceipts.setNeedFilter(false);
        PersistenceFactory.getInstance().updateByNativeSQL(doReceipts);
        return out;
    }

}
