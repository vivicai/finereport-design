package com.fr.design.extra.exe;

import com.fr.design.extra.PluginOperateUtils;
import com.fr.design.extra.PluginUtils;
import com.fr.design.extra.Process;
import com.fr.general.FRLogger;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.stable.StringUtils;

/**
 * Created by vito on 16/4/18.
 */
public class SearchOnlineExecutor implements Executor {
    private String result = StringUtils.EMPTY;
    private String keyword;

    public SearchOnlineExecutor(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String getTaskFinishMessage() {
        return result;
    }

    @Override
    public Command[] getCommands() {
        return new Command[]{
                new Command() {
                    @Override
                    public String getExecuteMessage() {
                        return StringUtils.EMPTY;
                    }

                    @Override
                    public void run(Process<String> process) {
                        try {
                            if (StringUtils.isBlank(keyword)) {
                                result = PluginOperateUtils.getRecommendPlugins();
                                return;
                            }
                            HttpClient httpClient = new HttpClient(SiteCenter.getInstance().acquireUrlByKind("shop.plugin.store") + "&keyword=" + keyword);
                            httpClient.asGet();
                            String responseText = httpClient.getResponseText();
                            JSONObject jsonObject = new JSONObject(responseText);
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONArray resultJSONArray = PluginUtils.filterPluginsFromVersion(jsonArray);
                            result = resultJSONArray.toString();
                        } catch (Exception e) {
                            FRLogger.getLogger().error(e.getMessage());
                        }
                    }
                }
        };
    }
}
