package com.fr.design.mainframe.bbs;

import com.fr.design.dialog.UIDialog;
import com.fr.design.extra.PluginWebBridge;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;

import com.fr.general.SiteCenter;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import javax.swing.*;

import java.awt.*;

/**
 * @author richie
 * @date 2015-04-02
 * @since 8.0
 */
public class BBSDialog extends UIDialog {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int OUTER_WIDTH = 600;
    private static final int OUTER_HEIGHT = 400;


    private JFXPanel jfxPanel;


    public BBSDialog(Frame parent) {
        super(parent);
        setUndecorated(true);
        JPanel panel = (JPanel) getContentPane();
        initComponents(panel);
        setSize(new Dimension(OUTER_WIDTH, OUTER_HEIGHT));
    }

    private void initComponents(JPanel contentPane) {
        contentPane.setLayout(new BorderLayout());
        jfxPanel = new JFXPanel();
        add(jfxPanel, BorderLayout.CENTER);
    }

    private void disableLink(final WebEngine eng) {
        try {
            // webView端不跳转 虽然webView可以指定本地浏览器打开某个链接，但是当本地浏览器跳转到指定链接的同时，webView也做了跳转，
            // 为了避免出现在一个600*400的资讯框里加载整个网页的情况，webView不跳转到新网页
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    eng.executeScript("history.go(0)");
                }
            });
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
    }

    /**
     * 打开资讯框
     *
     * @param url 资讯链接
     */
    public void showWindow(final String url) {
        GUICoreUtils.centerWindow(this);
        this.setResizable(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Group root = new Group();
                Scene scene = new Scene(root, WIDTH, HEIGHT);
                jfxPanel.setScene(scene);
                Double widthDouble = new Integer(WIDTH).doubleValue();
                Double heightDouble = new Integer(HEIGHT).doubleValue();
                WebView view = new WebView();
                view.setMinSize(widthDouble, heightDouble);
                view.setPrefSize(widthDouble, heightDouble);
                final WebEngine eng = view.getEngine();
                //webEngine的userAgent貌似支持移动设备的，任何其他浏览器的userAngent都会导致程序崩溃
                //eng.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) Apple/WebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36");
                eng.load(url);
                root.getChildren().add(view);
                eng.locationProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, final String oldValue, String newValue) {
                        disableLink(eng);
                        // webView好像默认以手机版显示网页，浏览器里过滤掉这个跳转
                        if (ComparatorUtils.equals(newValue, url) || ComparatorUtils.equals(newValue, SiteCenter.getInstance().acquireUrlByKind("bbs.mobile"))) {
                            return;
                        }
                        PluginWebBridge.getHelper().openUrlAtLocalWebBrowser(eng, newValue);
                    }
                });
                eng.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                        if (newValue == Worker.State.SUCCEEDED) {
                            JSObject obj = (JSObject) eng.executeScript("window");
                            obj.setMember("BBSWebBridge", BBSDialog.this);
                            setVisible(true);
                        }
                    }
                });
            }
        });
    }

    /**
     * 提供给web页面调用的关闭窗口
     */
    public void closeWindow() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setVisible(false);
    }

    /**
     * 略
     */
    @Override
    public void checkValid() throws Exception {

    }
}