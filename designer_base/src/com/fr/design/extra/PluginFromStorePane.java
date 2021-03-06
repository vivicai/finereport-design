package com.fr.design.extra;

import com.fr.base.ConfigManager;
import com.fr.base.FRContext;
import com.fr.design.extra.tradition.callback.UpdateOnlineCallback;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.json.JSONObject;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.control.PluginTaskResult;
import com.fr.plugin.manage.control.ProgressCallback;
import com.fr.plugin.view.PluginView;
import com.fr.stable.StringUtils;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.List;

/**
 * @author richie
 * @date 2015-03-10
 * @since 8.0
 */
public class PluginFromStorePane extends PluginAbstractLoadingViewPane<List<PluginView>, Void> {
    private UILabel errorMsgLabel;
    private UITabbedPane tabbedPane;
    private PluginControlPane controlPane;

    private static final int LISTNUM1 = 1;
    private static final int LISTNUM100 = 100;


    public PluginFromStorePane(final UITabbedPane tabbedPane) {
        super(tabbedPane);
        this.tabbedPane = tabbedPane;
    }

    /**
     * 创建成功页面
     *
     * @return 创建的页面对象
     */
    public JPanel createSuccessPane() {
        return new PluginStatusCheckCompletePane() {


            @Override
            public void pressInstallButton() {
                doUpdateOnline(this);
            }

            @Override
            public void pressInstallFromDiskButton() {
                installFromDiskFile();
            }

            @Override
            public String textForInstallButton() {
                return Inter.getLocText("FR-Designer-Plugin_Install");
            }


            @Override
            public String textForInstallFromDiskButton() {
                return Inter.getLocText("FR-Designer-Plugin_Install_From_Local");
            }

            @Override
            public JPanel centerPane() {
                controlPane = new PluginControlPane();
                final PluginStatusCheckCompletePane s = this;
                controlPane.addPluginSelectionListener(new PluginSelectListener() {
                    @Override
                    public void valueChanged(PluginView plugin) {
                        s.setInstallButtonEnable(true);
                    }
                });
                return controlPane;
            }
        };
    }

    /**
     * 创建错误页面
     *
     * @return 创建的页面对象
     */
    @Override
    public JPanel createErrorPane() {
        errorMsgLabel = new UILabel();
        errorMsgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        return new PluginStatusCheckCompletePane() {

            @Override
            public void pressInstallButton() {

            }

            @Override
            public void pressInstallFromDiskButton() {
                installFromDiskFile();
            }

            @Override
            public String textForInstallButton() {
                return Inter.getLocText("FR-Designer-Plugin_Install");
            }


            @Override
            public String textForInstallFromDiskButton() {
                return Inter.getLocText("FR-Designer-Plugin_Install_From_Local");
            }

            @Override
            public JComponent centerPane() {
                return errorMsgLabel;
            }
        };
    }

    /**
     * 加载数据
     *
     * @return 插件
     */
    public List<PluginView> loadData() throws Exception {
        //Thread.sleep(3000);
        return PluginsReaderFromStore.readPlugins();
    }

    /**
     * 加载成功处理
     *
     * @param plugins 插件
     */
    public void loadOnSuccess(List<PluginView> plugins) {
        controlPane.loadPlugins(plugins);
        tabbedPane.setTitleAt(2, Inter.getLocText("FR-Designer-Plugin_All_Plugins") + "(" + plugins.size() + ")");
    }

    /**
     * 加载失败
     *
     * @param e 异常消息
     */
    public void loadOnFailed(Exception e) {
        errorMsgLabel.setText(e.getCause().getMessage());
    }

    protected void installFromDiskFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("zip", "zip"));
        int returnValue = fileChooser.showOpenDialog(PluginFromStorePane.this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            final File chosenFile = fileChooser.getSelectedFile();
            installFromDiskZipFile(chosenFile);

        }
    }


    private void installFromDiskZipFile(File chosenFile) {
        try {
            PluginManager.getController().install(chosenFile, new ProgressCallback() {
                @Override
                public void updateProgress(String description, double progress) {

                }

                @Override
                public void done(PluginTaskResult result) {
                    if (result.isSuccess()) {
                        FRLogger.getLogger().info(Inter.getLocText("FR-Designer-Plugin_Install_Success"));
                        JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Install_Successful"));
                    } else {
                        JOptionPane.showMessageDialog(null, PluginUtils.getMessageByErrorCode(result.errorCode()), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                    }                }
            });
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(PluginFromStorePane.this, e1.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doUpdateOnline(final PluginStatusCheckCompletePane pane) {
        if (!StringUtils.isNotEmpty(ConfigManager.getProviderInstance().getBbsUsername())) {
            LoginCheckContext.fireLoginCheckListener();
        }
        if (StringUtils.isNotEmpty(ConfigManager.getProviderInstance().getBbsUsername())) {
            PluginView plugin = controlPane.getSelectedPlugin();
            String id = null;
            if (plugin != null) {
                id = plugin.getID();
            }

            try {
                PluginMarker pluginMarker = PluginMarker.create(id, plugin.getVersion());
                JSONObject latestPluginInfo = PluginUtils.getLatestPluginInfo(id);
                String latestPluginVersion = (String)latestPluginInfo.get("version");
                PluginMarker toPluginMarker = PluginMarker.create(id, latestPluginVersion);
                PluginManager.getController().download(pluginMarker, new UpdateOnlineCallback(pluginMarker, toPluginMarker, pane));
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }

        }
    }

    /**
     * 正在加载页的标题
     *
     * @return 标题字符串
     */
    public String textForLoadingLabel() {
        return Inter.getLocText("FR-Designer-Plugin_Load_Plugins_From_Server");
    }

    /**
     * 从磁盘安装按钮的提示
     *
     * @return 按钮标题字符串
     */
    @Override
    public String textForInstallFromDiskFileButton() {
        return Inter.getLocText("FR-Designer-Plugin_Install_From_Local");
    }

    @Override
    protected String title4PopupWindow() {
        return "All";
    }
}