package com.fr.design.extra;

import com.fr.base.TemplateUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.plugin.basic.version.Version;
import com.fr.plugin.basic.version.VersionIntervalFactory;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.context.PluginMarker;

import com.fr.plugin.error.PluginErrorCode;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.view.PluginView;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ibm on 2017/5/25.
 */
public class PluginUtils {
    
    private static final String ERROR_CODE_I18N_PREFIX = "FR-Plugin_Error_";


    public static PluginMarker createPluginMarker(String pluginInfo) {
        //todo 判空
        String[] plugin = pluginInfo.split("_");
        PluginMarker pluginMarker = PluginMarker.create(plugin[0], plugin[1]);
        return pluginMarker;
    }

    public static JSONObject getLatestPluginInfo(String pluginID) throws Exception {
        String result = "";
        String plistUrl = SiteCenter.getInstance().acquireUrlByKind("plugin.searchAPI");
        if (StringUtils.isNotEmpty(plistUrl)) {
            StringBuilder url = new StringBuilder(plistUrl);
            if (StringUtils.isNotBlank(pluginID)) {
                url.append("?keyword=").append(pluginID);
            }
            try {
                HttpClient httpClient = new HttpClient(url.toString());
                httpClient.asGet();
                result = httpClient.getResponseText();
            } catch (Exception e) {
                FRLogger.getLogger().error(e.getMessage());
            }
        } else {
            result = PluginConstants.CONNECTION_404;
        }
        JSONObject resultJSONObject = new JSONObject(result);
        JSONArray resultArr = resultJSONObject.getJSONArray("result");
        JSONObject latestPluginInfo = JSONObject.create();
        latestPluginInfo = (JSONObject) resultArr.get(0);
        return latestPluginInfo;
    }

    public static String transPluginsToString(List<PluginContext> plugins) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (PluginContext plugin : plugins) {
            JSONObject jo = new JSONObject();
            jo.put("id", plugin.getID());
            jo.put("version", plugin.getVersion());
            jsonArray.put(jo);
        }
        return jsonArray.toString();
    }

    public static void downloadShopScripts(String id, Process<Double> p) throws Exception {
        HttpClient httpClient = new HttpClient(getDownloadPath(id));
        if (httpClient.getResponseCode() == HttpURLConnection.HTTP_OK) {
            int totalSize = httpClient.getContentLength();
            InputStream reader = httpClient.getResponseStream();
            String temp = StableUtils.pathJoin(PluginConstants.DOWNLOAD_PATH, PluginConstants.TEMP_FILE);
            StableUtils.makesureFileExist(new File(temp));
            FileOutputStream writer = new FileOutputStream(temp);
            byte[] buffer = new byte[PluginConstants.BYTES_NUM];
            int bytesRead = 0;
            int totalBytesRead = 0;

            while ((bytesRead = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, bytesRead);
                buffer = new byte[PluginConstants.BYTES_NUM];
                totalBytesRead += bytesRead;
                p.process(totalBytesRead / (double) totalSize);
            }
            reader.close();
            writer.flush();
            writer.close();
        } else {
            throw new com.fr.plugin.PluginVerifyException(Inter.getLocText("FR-Designer-Plugin_Connect_Server_Error"));
        }
    }

    private static String getDownloadPath(String id) throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        HttpClient httpClient = new HttpClient(SiteCenter.getInstance().acquireUrlByKind("shop.plugin.scripts"));
        httpClient.asGet();
        String resText = httpClient.getResponseText();
        JSONObject resultJSONObject = new JSONObject(resText);
        String scriptUrl = resultJSONObject.optString("result");
        String charSet = EncodeConstants.ENCODING_UTF_8;
        scriptUrl = URLDecoder.decode(URLDecoder.decode(scriptUrl, charSet), charSet);

        return scriptUrl;
    }

    public static boolean isPluginMatch(PluginView pluginView, String text) {
        return StringUtils.contains(pluginView.getID(), text)
                || StringUtils.contains(pluginView.getName(), text)
                || StringUtils.contains(pluginView.getVersion(), text)
                || StringUtils.contains(pluginView.getEnvVersion(), text)
                || StringUtils.contains(pluginView.getVendor(), text)
                || StringUtils.contains(pluginView.getDescription(), text)
                || StringUtils.contains(pluginView.getChangeNotes(), text);

    }

    public static String pluginToHtml(PluginView pluginView) {
        String pluginName = Inter.getLocText("FR-Plugin-Plugin_Name");
        String pluginVersion = Inter.getLocText("FR-Plugin-Plugin_Version");
        String startVersion = Inter.getLocText("FR-Plugin-Start_Version");
        String developer = Inter.getLocText("FR-Plugin_Developer");
        String desc = Inter.getLocText("FR-Plugin-Function_Description");
        String updateLog = Inter.getLocText("FR-Plugin-Update_Log");
        Map<String, String> map = new HashMap<String, String>();

        map.put("name", pluginName);
        map.put("name_value", pluginView.getName());

        map.put("version", pluginVersion);
        map.put("version_value", pluginView.getVersion());

        map.put("env", startVersion);
        map.put("env_value", pluginView.getEnvVersion());

        map.put("dev", developer);
        map.put("dev_value", pluginView.getVendor());

        map.put("fun", desc);
        map.put("fun_value", pluginView.getDescription());

        map.put("update", updateLog);
        map.put("update_value", pluginView.getDescription());

        try {
            return TemplateUtils.renderTemplate("/com/fr/plugin/plugin.html", map);
        } catch (IOException e) {
            return StringUtils.EMPTY;
        }
    }
    
    public static String getMessageByErrorCode(PluginErrorCode errorCode) {
        if(errorCode == PluginErrorCode.None){
            return "";
        }
        
        return Inter.getLocText(getInterKeyByErrorCode(errorCode));
    }
    
    private static String getInterKeyByErrorCode(PluginErrorCode errorCode) {
        
        return ERROR_CODE_I18N_PREFIX + errorCode.getDescription();
    }
    
    public static PluginMarker getInstalledPluginMarkerByID(String pluginID) {
        
        PluginContext context = PluginManager.getContext(pluginID);
        if (context != null) {
            return context.getMarker();
        }
        return null;
    }

    /**
     * 在不同设计器版本下展示不同插件
     * @return 插件
     */
    public static JSONArray filterPluginsFromVersion(JSONArray oriJSONArray) throws Exception{
        JSONArray resultJSONArray =  JSONArray.create();
        for(int i = 0; i < oriJSONArray.length(); i++){
            JSONObject jo = oriJSONArray.getJSONObject(i);
            String envVersion = jo.optString("envversion");
            if(isCompatibleCurrentEnv(envVersion)){
                resultJSONArray.put(jo);
            }
        }
        return resultJSONArray;
    }

    private static boolean isCompatibleCurrentEnv(String envVersion){
        return VersionIntervalFactory.create(envVersion).contain(Version.currentEnvVersion());
    }
}
