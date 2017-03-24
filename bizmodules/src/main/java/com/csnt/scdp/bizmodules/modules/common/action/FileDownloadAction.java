package com.csnt.scdp.bizmodules.modules.common.action;

import com.csnt.scdp.bizmodules.modules.common.intf.CommonServiceManager;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2015/10/23.
 */
@Scope("singleton")
@Controller("erp-common-file-download")
@Transactional
public class FileDownloadAction implements IAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(FileDownloadAction.class);

    @Resource(name = "erp-common-service-manager")
    private CommonServiceManager commonServiceManager;

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        Map outMap = new HashMap();
        List retUrlList = new ArrayList();
        List fileList = (List) inMap.get("fileList");
        if (ListUtil.isNotEmpty(fileList)) {
            for (Object o : fileList) {
                String fileUrl = commonServiceManager.fileDownload((String) o);
                if (!StringUtil.isEmpty(fileUrl)) {
                    retUrlList.add(fileUrl);
                }
            }
        }
        outMap.put("URL_LIST", retUrlList);
        return outMap;
    }
}

