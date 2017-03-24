package com.csnt.scdp.bizmodules.modules.fad.payreq.action;

import com.csnt.scdp.bizmodules.modules.common.helper.ErpCodeHelper;
import com.csnt.scdp.framework.attributes.CommonAttribute;
import com.csnt.scdp.framework.commonaction.CommonFileDownloadAction;
import com.csnt.scdp.framework.commonaction.crud.CommonExportXlsAction;
import com.csnt.scdp.framework.core.actionfacade.intf.IAction;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.core.logtracer.LogTracerFactory;
import com.csnt.scdp.framework.core.logtracer.intf.ILogTracer;
import com.csnt.scdp.framework.helper.XlsStreamHelper;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2016/12/16.
 */
@Scope("singleton")
@Controller("payreq-detail-export")
@Transactional
@SuppressWarnings("unchecked")
public class ExportDetailXlsAction extends CommonFileDownloadAction {
    private static ILogTracer tracer = LogTracerFactory.getInstance(ExportDetailXlsAction.class);

    @Override
    public Map perform(Map inMap) throws BizException, SysException {
        HttpServletResponse response = (HttpServletResponse) inMap.get(CommonAttribute.HTTP_RESPONSE);

        String exportFileName = (String) inMap.get(CommonAttribute.EXPORT_FILE_NAME);
        inMap.put(CommonAttribute.DOWNLOAD_FILE_NAME, exportFileName + ".xlsx");
        super.perform(inMap);
        //Do After
        OutputStream os;
        try {
            os = response.getOutputStream();
        } catch (IOException e) {
            tracer.error(e);
            throw new SysException(e);
        }

        String rootSize = (String) inMap.get("rootSize");
        inMap.put(CommonAttribute.TOTALPROPERTY, new Integer(rootSize));

        List<String> queryResultColumnWidths = (List<String>) inMap.get(CommonAttribute.QUERY_RESULT_COLUMN_WIDTHS);

        List<Double> queryResultColumnWidth = queryResultColumnWidths.stream().map(t -> new Double(t)).collect(Collectors.toList());

        inMap.put(CommonAttribute.QUERY_RESULT_COLUMN_WIDTHS, queryResultColumnWidth);

        List dataList = (List) inMap.get(CommonAttribute.DATAROOT);
        List states = ErpCodeHelper.findByCodeType("FAD_BILL_STATE");
        Map<String, String> sm = new HashMap<String, String>();
        states.stream().forEach(t -> {
            String s = (String) ((Map) (t)).get("sysCode");
            if (!sm.containsKey(s)) {
                sm.put(s, (String) ((Map) (t)).get("codeDesc"));
            }
        });

        dataList.stream().forEach(t -> {
            String s = (String) ((Map) t).get("state");
            if (sm.containsKey(s)) {
                ((Map) t).put("state", sm.get(s));
            }
            if (((Map) t).get("certificateCreateTime") != null) {
                String certificateCreateTime = ((String) ((Map) t).get("certificateCreateTime")).replace("T", " ");
                ((Map) t).put("certificateCreateTime", certificateCreateTime);
            }
            if (((Map) t).get("createTime") != null) {
                String createTime = ((String) ((Map) t).get("createTime")).replace("T", " ");
                ((Map) t).put("createTime", createTime);
            }
        });
        XlsStreamHelper.exportXls2OutputStream(os, inMap);

        return null;
    }
}
