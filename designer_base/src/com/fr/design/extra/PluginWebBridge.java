package com.fr.design.extra;

import com.fr.base.ConfigManager;
import com.fr.base.FRContext;
import com.fr.design.RestartHelper;
import com.fr.design.bbs.BBSLoginUtils;
import com.fr.design.dialog.UIDialog;
import com.fr.design.extra.exe.GetPluginCategoriesExecutor;
import com.fr.design.extra.exe.GetPluginFromStoreExecutor;
import com.fr.design.extra.exe.GetPluginPrefixExecutor;
import com.fr.design.extra.exe.PluginLoginExecutor;
import com.fr.design.extra.exe.ReadUpdateOnlineExecutor;
import com.fr.design.extra.exe.SearchOnlineExecutor;
import com.fr.design.extra.exe.callback.JSCallback;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.utils.concurrent.ThreadFactoryBuilder;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.manage.PluginManager;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 开放给Web组件的接口,用于安装,卸载,更新以及更改插件可用状态
 */
public class PluginWebBridge {
    private static final String THREAD_NAME_TEMPLATE = "pluginbridge-thread-%s";
    private static final String ACTION = "action";
    private static final String KEYWORD = "keyword";
    private static final int COREPOOLSIZE = 3;
    private static final int MAXPOOLSIZE = 5;

    private static PluginWebBridge helper;

    private UIDialog uiDialog;
    private ACTIONS action;

    private Map<String, Object> config;
    private WebEngine webEngine;

    private UILabel uiLabel;

    private ExecutorService threadPoolExecutor = new ThreadPoolExecutor(COREPOOLSIZE, MAXPOOLSIZE,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(COREPOOLSIZE),
            new ThreadFactoryBuilder().setNameFormat(THREAD_NAME_TEMPLATE).build());

    /**
     * 动作枚举
     */
    public enum ACTIONS {
        SEARCH("search");
        private String context;

        ACTIONS(String context) {
            this.context = context;
        }

        public String getContext() {
            return context;
        }
    }

    public static PluginWebBridge getHelper() {
        if (helper != null) {
            return helper;
        }
        synchronized (PluginWebBridge.class) {
            if (helper == null) {
                helper = new PluginWebBridge();
            }
            return helper;
        }
    }

    public static PluginWebBridge getHelper(WebEngine webEngine) {
        getHelper();
        helper.setEngine(webEngine);
        return helper;
    }

    private PluginWebBridge() {
    }

    /**
     * 获取打开动作配置
     *
     * @return 配置信息
     */
    public String getRunConfig() {
        if (action != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(ACTION, action.getContext());
                Set<String> keySet = config.keySet();
                for (String key : keySet) {
                    jsonObject.put(key, config.get(key).toString());
                }
            } catch (JSONException e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
            return jsonObject.toString();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 配置打开动作
     *
     * @param action 动作
     * @param config 参数
     */
    public void setRunConfig(ACTIONS action, Map<String, Object> config) {
        this.action = action;
        this.config = config;
    }

    /**
     * 清楚打开动作
     */
    public void clearRunConfig() {
        this.action = null;
        this.config = null;
    }

    /**
     * 打开时搜索
     *
     * @param keyword 关键词
     */
    public void openWithSearch(String keyword) {
        HashMap<String, Object> map = new HashMap<String, Object>(2);
        map.put(KEYWORD, keyword);
        setRunConfig(ACTIONS.SEARCH, map);
    }

    public void setEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    public void setDialogHandle(UIDialog uiDialog) {
        closeWindow();
        this.uiDialog = uiDialog;
    }

    /**
     * 从插件服务器上安装插件
     *
     * @param pluginInfo 插件的ID
     * @param callback   回调函数
     */
    public void installPluginOnline(final String pluginInfo, final JSObject callback) {
        JSCallback jsCallback = new JSCallback(webEngine, callback);
        PluginMarker pluginMarker = PluginUtils.createPluginMarker(pluginInfo);
        PluginOperateUtils.installPluginOnline(pluginMarker, jsCallback);
    }


    /**
     * 从磁盘上选择插件安装包进行安装
     *
     * @param filePath 插件包的路径
     */
    public void installPluginFromDisk(final String filePath, final JSObject callback) {
        JSCallback jsCallback = new JSCallback(webEngine, callback);
        File file = new File(filePath);
        PluginOperateUtils.installPluginFromDisk(file, jsCallback);
    }

    /**
     * 卸载当前选中的插件
     *
     * @param pluginInfo 插件信息
     */
    public void uninstallPlugin(final String pluginInfo, final boolean isForce, final JSObject callback) {
        JSCallback jsCallback = new JSCallback(webEngine, callback);
        PluginOperateUtils.uninstallPlugin(pluginInfo, isForce, jsCallback);
    }


    /**
     * 从插件服务器上更新选中的插件
     *
     * @param pluginIDs 插件集合
     */
    public void updatePluginOnline(JSObject pluginIDs, final JSObject callback) {
        JSCallback jsCallback = new JSCallback(webEngine, callback);
        String[] pluginInfos = jsObjectToStringArray(pluginIDs);
        List<PluginMarker> pluginMarkerList = new ArrayList<PluginMarker>();
        for (int i = 0; i < pluginInfos.length; i++) {
            pluginMarkerList.add(PluginUtils.createPluginMarker(pluginInfos[i]));
        }
        PluginOperateUtils.updatePluginOnline(pluginMarkerList, jsCallback);
    }

    /**
     * 从磁盘上选择插件安装包进行插件升级
     *
     * @param filePath 插件包的路径
     */
    public void updatePluginFromDisk(String filePath, final JSObject callback) {
        JSCallback jsCallback = new JSCallback(webEngine, callback);
        File file = new File(filePath);
        PluginOperateUtils.updatePluginFromDisk(file, jsCallback);
    }

    /**
     * 修改选中的插件的活跃状态
     *
     * @param pluginID 插件ID
     */
    public void setPluginActive(String pluginID, final JSObject callback) {
        JSCallback jsCallback = new JSCallback(webEngine, callback);
        PluginOperateUtils.setPluginActive(pluginID, jsCallback);
    }

    /**
     * 已安装插件检查更新
     */
    public void readUpdateOnline(final JSObject callback) {
        Task<Void> task = new PluginTask<>(webEngine, callback, new ReadUpdateOnlineExecutor());
        threadPoolExecutor.submit(task);
    }

    /**
     * 选择文件对话框
     *
     * @return 选择的文件的路径
     */
    public String showFileChooser() {
        return showFileChooserWithFilter(StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 选择文件对话框
     *
     * @param des    过滤文件描述
     * @param filter 文件的后缀
     * @return 选择的文件的路径
     * 这里换用JFileChooser会卡死,不知道为什么
     */
    public String showFileChooserWithFilter(String des, String filter) {
        FileChooser fileChooser = new FileChooser();

        if (StringUtils.isNotEmpty(filter)) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(des, filter));
        }

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile == null) {
            return null;
        }
        return selectedFile.getAbsolutePath();
    }

    /**
     * 选择文件对话框
     *
     * @param des  过滤文件描述
     * @param args 文件的后缀
     * @return 选择的文件的路径
     */
    public String showFileChooserWithFilters(String des, JSObject args) {
        FileChooser fileChooser = new FileChooser();
        String[] filters = jsObjectToStringArray(args);
        if (ArrayUtils.isNotEmpty(filters)) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(des, filters));
        }

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile == null) {
            return null;
        }
        return selectedFile.getAbsolutePath();
    }

    /**
     * 获取已经安装的插件的数组
     *
     * @return 已安装的插件组成的数组
     */
    public PluginContext[] getInstalledPlugins() {
        List<PluginContext> plugins = PluginManager.getContexts();
        return plugins.toArray(new PluginContext[plugins.size()]);
    }

    private String[] jsObjectToStringArray(JSObject obj) {
        if (obj == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        int len = (int) obj.getMember("length");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            list.add(obj.getSlot(i).toString());
        }
        return list.toArray(new String[len]);
    }

    /**
     * 搜索在线插件
     *
     * @param keyword 关键字
     */
    public void searchPlugin(String keyword, final JSObject callback) {
        Task<Void> task = new PluginTask<>(webEngine, callback, new SearchOnlineExecutor(keyword));
        threadPoolExecutor.submit(task);
    }

    /**
     * 根据条件获取在线插件的
     *
     * @param category 分类
     * @param seller   卖家性质
     * @param fee      收费类型
     * @param callback 回调函数
     */
    public void getPluginFromStore(String category, String seller, String fee, final JSObject callback) {
        Task<Void> task = new PluginTask<>(webEngine, callback, new GetPluginFromStoreExecutor(category, seller, fee));
        threadPoolExecutor.submit(task);
    }


    public void getPluginPrefix(final JSObject callback) {
        Task<Void> task = new PluginTask<>(webEngine, callback, new GetPluginPrefixExecutor());
        threadPoolExecutor.submit(task);
    }


    /**
     * 在线获取插件分类
     *
     * @param callback 回调函数
     */
    public void getPluginCategories(final JSObject callback) {
        Task<Void> task = new PluginTask<>(webEngine, callback, new GetPluginCategoriesExecutor());
        threadPoolExecutor.submit(task);
    }

    /**
     * 展示一个重启的对话框(少用,莫名其妙会有bug)
     *
     * @param message 展示的消息
     */
    public void showRestartMessage(String message) {
        int rv = JOptionPane.showOptionDialog(
                null,
                message,
                Inter.getLocText("FR-Designer-Plugin_Warning"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{Inter.getLocText("FR-Designer-Basic_Restart_Designer"), Inter.getLocText("FR-Designer-Basic_Restart_Designer_Later")},
                null
        );
        if (rv == JOptionPane.OK_OPTION) {
            RestartHelper.restart();
        }
    }

    /**
     * 关闭窗口
     */
    public void closeWindow() {
        if (uiDialog != null) {
            uiDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            uiDialog.setVisible(false);
        }
    }

    /**
     * 窗口是否无装饰(判断是否使用系统标题栏)
     */
    public boolean isCustomTitleBar() {
        if (uiDialog != null) {
            return uiDialog.isUndecorated();
        }
        return false;
    }

    /**
     * 获取系统登录的用户名
     *
     * @param callback
     */
    public String getLoginInfo(final JSObject callback) {
        registerLoginInfo(callback);
        return ConfigManager.getProviderInstance().getBbsUsername();
    }

    /**
     * 系统登录注册
     *
     * @param callback
     */
    public void registerLoginInfo(final JSObject callback) {
        JSCallback jsCallback = new JSCallback(webEngine, callback);
        PluginOperateUtils.getLoginInfo(jsCallback, uiLabel);
    }

    /**
     * 打开论坛消息界面
     */
    public void getPriviteMessage() {
        try {
            String loginUrl = SiteCenter.getInstance().acquireUrlByKind("bbs.default");
            Desktop.getDesktop().browse(new URI(loginUrl));
        } catch (Exception exp) {
            FRContext.getLogger().info(exp.getMessage());
        }
    }

    /**
     * 打开登录页面
     */
    public void loginContent() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UserLoginContext.fireLoginContextListener();
            }
        });
    }

    /**
     * 在本地浏览器里打开url
     * tips:重载的时候,需要给js调用的方法需要放在前面,否则可能不会被调用(此乃坑)
     * 所以最好的是不要重载在js可以访问的接口文件中
     *
     * @param url 要打开的地址
     */
    public void openShopUrlAtWebBrowser(String url) {
        openUrlAtLocalWebBrowser(webEngine, url);
    }

    /**
     * 在本地浏览器里打开url
     *
     * @param eng web引擎
     * @param url 要打开的地址
     */
    public void openUrlAtLocalWebBrowser(WebEngine eng, String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                //创建一个URI实例,注意不是URL
                URI uri = URI.create(url);
                //获取当前系统桌面扩展
                Desktop desktop = Desktop.getDesktop();
                //判断系统桌面是否支持要执行的功能
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    //获取系统默认浏览器打开链接
                    desktop.browse(uri);
                }
            } catch (NullPointerException e) {
                //此为uri为空时抛出异常
                FRLogger.getLogger().error(e.getMessage());
            } catch (IOException e) {
                //此为无法获取系统默认浏览器
                FRLogger.getLogger().error(e.getMessage());
            }
        }
    }


    /*-------------------------------登录部分的处理----------------------------------*/

    /**
     * 注册页面
     */
    public void registerHref() {
        try {
            Desktop.getDesktop().browse(new URI(SiteCenter.getInstance().acquireUrlByKind("bbs.register")));
        } catch (Exception e) {
            FRContext.getLogger().info(e.getMessage());
        }
    }

    /**
     * 忘记密码
     */
    public void forgetHref() {
        try {
            Desktop.getDesktop().browse(new URI(SiteCenter.getInstance().acquireUrlByKind("bbs.reset")));
        } catch (Exception e) {
            FRContext.getLogger().info(e.getMessage());
        }
    }

    public void setUILabel(UILabel uiLabel) {
        this.uiLabel = uiLabel;
    }

    /**
     * 设计器端的用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录信息标志
     */
    public void defaultLogin(String username, String password, final JSObject callback) {
        Task<Void> task = new PluginTask<>(webEngine, callback, new PluginLoginExecutor(username, password));
        threadPoolExecutor.submit(task);
    }

    /**
     * 弹出QQ授权页面
     */
    public void showQQ() {
        LoginWebBridge.getHelper().showQQ();
    }

    /**
     * 通过QQ登录后通知登录
     */
    public void ucsynLogin(long uid, String username, String password, final JSONObject callback) {
        try {
            FRContext.getCurrentEnv().writeResource(ConfigManager.getProviderInstance());
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
        uiLabel.setText(username);
    }

    /**
     * 清除用户信息
     */
    public void clearUserInfo() {
        ConfigManager.getProviderInstance().setInShowBBsName(StringUtils.EMPTY);
        BBSLoginUtils.bbsLogout();
        uiLabel.setText(Inter.getLocText("FR-Base_UnSignIn"));
    }

    /**
     * 初始化设计器部分
     */
    public void initExtraDiff(final JSObject callback) {
        //todo  初始化设计器其他部分
    }

    /**
     * 国际化
     */
    public String parseI18(final String key) {
        return Inter.getLocText(key);
    }

    /**
     * 是否是在设计器中操作
     */
    public boolean isDesigner() {
        return true;
    }

}
