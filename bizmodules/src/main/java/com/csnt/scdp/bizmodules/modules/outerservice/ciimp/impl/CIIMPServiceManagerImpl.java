package com.csnt.scdp.bizmodules.modules.outerservice.ciimp.impl;

import com.csnt.scdp.bizmodules.entity.outersystem.ErpOuterSystemConfiguration;
import com.csnt.scdp.bizmodules.entityattributes.outersystem.ErpOuterSystemConfigurationAttribute;
import com.csnt.scdp.bizmodules.modules.outerservice.ciimp.intf.CIIMPServiceManager;
import com.csnt.scdp.bizmodules.modules.outerservice.intf.OuterServiceManager;
import com.csnt.scdp.bizmodules.modules.scm.fadsuppliermapping.services.intf.FadsuppliermappingManager;
import com.csnt.scdp.framework.core.exception.BizException;
import com.csnt.scdp.framework.core.persistence.PersistenceFactory;
import com.csnt.scdp.framework.core.persistence.intf.PersistenceCrudManager;
import com.csnt.scdp.framework.core.exception.SysException;
import com.csnt.scdp.framework.util.JsonUtil;
import com.csnt.scdp.framework.util.ListUtil;
import com.csnt.scdp.framework.util.StringUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.binary.Base64;

import javax.annotation.Resource;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope("singleton")
@Service("ciimpservice-manager")
public class CIIMPServiceManagerImpl implements CIIMPServiceManager {

    @Resource(name = "outerservice-manager")
    private OuterServiceManager outerServiceManager;

    public String login() {
        ErpOuterSystemConfiguration erpOuterSystemConfiguration = outerServiceManager.getErpOuterSystemConfiguration("CIIMP");
        HashMap loginMap = new HashMap();
        loginMap.put("username", erpOuterSystemConfiguration.getUsername());
        loginMap.put("password", erpOuterSystemConfiguration.getPassword());
        String urlPath = erpOuterSystemConfiguration.getUrl() + "login";
        String xmlInfo = JsonUtil.serialize(loginMap);
        byte[] xmlData = xmlInfo.getBytes();
        InputStream instr = null;
        java.io.ByteArrayOutputStream out = null;
        try {
            URL url = new URL(urlPath);
            URLConnection urlCon = url.openConnection();
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setUseCaches(false);
            urlCon.setRequestProperty("content-Type", "application/json");
            urlCon.setRequestProperty("charset", "utf-8");
            urlCon.setRequestProperty("Content-length",
                    String.valueOf(xmlData.length));
            System.out.println(String.valueOf(xmlData.length));
            DataOutputStream printout = new DataOutputStream(
                    urlCon.getOutputStream());
            printout.write(xmlData);
            printout.flush();
            printout.close();
            instr = urlCon.getInputStream();
            byte[] bis = IOUtils.toByteArray(instr);
            String responseString = new String(bis, "UTF-8");
            String auth = erpOuterSystemConfiguration.getUsername() + ":" + responseString;
            String authStr = Base64.encodeBase64String(auth.getBytes("UTF-8"));
            authStr = "Basic " + authStr;
            return authStr;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
            } catch (Exception ex) {
                throw new SysException(ex);
            }
        }
        return null;
    }

    public String sendFinInfo(String actionUrl, Map dataMap) {
        String authStr = login();
        String input = JsonUtil.serialize(dataMap);
        String result = null;
        try {
            URL urlPost = new URL(outerServiceManager.getOutterSysUrl("CIIMP") + actionUrl);
            HttpURLConnection http = (HttpURLConnection) urlPost.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setDoInput(true);
            http.setRequestProperty("content-Type", "application/json");
            http.setRequestProperty("charset", "utf-8");
            http.setRequestProperty("Authorization", authStr);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒

            http.connect();
            OutputStream os = http.getOutputStream();
            if (StringUtil.isNotEmpty(input)) {
                os.write(input.getBytes("UTF-8"));// 传入参数
            }
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            result = new String(jsonBytes, "UTF-8");
            os.flush();
            os.close();

        } catch (Exception e) {
            throw new BizException("CIIMP service connect error for:" + e.getMessage());
        }
        return result;
    }

    public String sendFinInfo(String actionUrl, List dataLst) {
        String authStr = login();
        String input = JsonUtil.serialize(dataLst);
        String result = null;
        try {
            URL urlPost = new URL(outerServiceManager.getOutterSysUrl("CIIMP") + actionUrl);
            HttpURLConnection http = (HttpURLConnection) urlPost.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setDoInput(true);
            http.setRequestProperty("content-Type", "application/json");
            http.setRequestProperty("charset", "utf-8");
            http.setRequestProperty("Authorization", authStr);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒

            http.connect();
            OutputStream os = http.getOutputStream();
            if (StringUtil.isNotEmpty(input)) {
                os.write(input.getBytes("UTF-8"));// 传入参数
            }
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            result = new String(jsonBytes, "UTF-8");
            os.flush();
            os.close();

        } catch (Exception e) {
            throw new BizException("CIIMP service error for " + actionUrl + ". " + e.getMessage());
        }
        return result;
    }
}