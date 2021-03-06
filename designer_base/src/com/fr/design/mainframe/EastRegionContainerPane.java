package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIButtonUI;
import com.fr.design.gui.icontainer.UIEastResizableContainer;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.VerticalFlowLayout;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.utils.gui.GUIPaintUtils;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class EastRegionContainerPane extends UIEastResizableContainer {
    private static EastRegionContainerPane THIS;
    private Map<String, PropertyItem> propertyItemMap;
    private CardLayout propertyCard;
    private JPanel leftPane;
    private JPanel rightPane;
    private FixedPopupPane currentPopupPane;
    private static final int CONTAINER_WIDTH = 286;
    private static final int TAB_WIDTH = 38;
    private static final int TAB_BUTTON_WIDTH = 32;
    private static final int TAB_BUTTON_HEIGHT = 28;
    private static final int CONTENT_WIDTH = CONTAINER_WIDTH - TAB_WIDTH;
    private static final int POPUP_TOOLPANE_HEIGHT = 27;
    private static final int ARROW_RANGE_START = CONTENT_WIDTH - 30;
    // 弹出对话框高度
    private static final int POPUP_MIN_HEIGHT = 145;
    private static final int POPUP_DEFAULT_HEIGHT = 356;
    public static final String KEY_CELL_ELEMENT = "cellElement";
    public static final String KEY_CELL_ATTR = "cellAttr";
    public static final String KEY_FLOAT_ELEMENT = "floatElement";
    public static final String KEY_WIDGET_SETTINGS = "widgetSettings";
    public static final String KEY_CONDITION_ATTR = "conditionAttr";
    public static final String KEY_HYPERLINK = "hyperlink";
    public static final String KEY_WIDGET_LIB = "widgetLib";
    public static final String KEY_AUTHORITY_EDITION = "authorityEdition";
    public static final String KEY_CONFIGURED_ROLES = "editedRoles";
    public static final String DEFAULT_PANE = "defaultPane";
    public static final String DEFAULT_AUTHORITY_PANE = "defaultAuthorityPane";

    private JPanel defaultPane;  // "无可用配置项"面板
    private JPanel defaultAuthorityPane;  // "该元素不支持权限编辑"
    private PropertyItem selectedItem;  // 当前被选中的属性配置项

    public enum PropertyMode {
        REPORT,  // 报表
        REPORT_PARA,  // 报表参数面板
        REPORT_FLOAT,  // 报表悬浮元素
        FORM,  // 表单
        FORM_REPORT,  // 表单报表块
        POLY,  // 聚合报表
        POLY_REPORT,  // 聚合报表-报表块
        POLY_CHART,  // 聚合报表-图表块
        AUTHORITY_EDITION,  // 权限编辑
        AUTHORITY_EDITION_DISABLED  // 权限编辑
    }
    private PropertyMode currentMode;  // 当前模式（根据不同模式，显示不同的可用面板）


    /**
     * 得到实例
     *
     * @return
     */
    public static final EastRegionContainerPane getInstance() {
        if (THIS == null) {
            THIS = new EastRegionContainerPane();
            THIS.setLastContainerWidth(DesignerEnvManager.getEnvManager().getLastEastRegionContainerWidth());
        }
        return THIS;
    }

    public EastRegionContainerPane() {
        super();
        initPropertyItemList();
        defaultPane = getDefaultPane(Inter.getLocText("FR-Designer_No_Settings_Available"));
        defaultAuthorityPane = getDefaultPane(Inter.getLocText("FR-Designer_Not_Support_Authority_Edit"));
        switchMode(PropertyMode.REPORT);
        setContainerWidth(CONTAINER_WIDTH);
    }

    private void initPropertyItemList() {
        propertyItemMap = new LinkedHashMap<>();  // 有序map
        // 单元格元素
        PropertyItem cellElement = new PropertyItem(KEY_CELL_ELEMENT, Inter.getLocText("FR-Designer_Cell_Element"),
                "cellelement", new PropertyMode[]{PropertyMode.REPORT, PropertyMode.REPORT_PARA, PropertyMode.REPORT_FLOAT, PropertyMode.POLY, PropertyMode.POLY_CHART},
                new PropertyMode[]{PropertyMode.REPORT, PropertyMode.FORM_REPORT, PropertyMode.POLY_REPORT});
        // 单元格属性
        PropertyItem cellAttr = new PropertyItem(KEY_CELL_ATTR, Inter.getLocText("FR-Designer_Cell_Attributes"),
                "cellattr", new PropertyMode[]{PropertyMode.REPORT, PropertyMode.REPORT_PARA, PropertyMode.REPORT_FLOAT, PropertyMode.POLY, PropertyMode.POLY_CHART},
                new PropertyMode[]{PropertyMode.REPORT, PropertyMode.FORM_REPORT, PropertyMode.POLY_REPORT});
        // 悬浮元素
        PropertyItem floatElement = new PropertyItem(KEY_FLOAT_ELEMENT, Inter.getLocText("FR-Designer_Float_Element"),
                "floatelement", new PropertyMode[]{PropertyMode.REPORT, PropertyMode.REPORT_PARA, PropertyMode.REPORT_FLOAT, PropertyMode.POLY, PropertyMode.POLY_CHART},
                new PropertyMode[]{PropertyMode.REPORT, PropertyMode.REPORT_FLOAT, PropertyMode.POLY_REPORT});
        // 控件设置
        PropertyItem widgetSettings = new PropertyItem(KEY_WIDGET_SETTINGS, Inter.getLocText("FR-Designer-Widget_Settings"),
                "widgetsettings", new PropertyMode[]{PropertyMode.REPORT, PropertyMode.REPORT_PARA, PropertyMode.REPORT_FLOAT, PropertyMode.FORM, PropertyMode.POLY},
                new PropertyMode[]{PropertyMode.REPORT, PropertyMode.REPORT_PARA, PropertyMode.FORM, PropertyMode.POLY_REPORT, PropertyMode.POLY_CHART});
        // 条件属性
        PropertyItem conditionAttr = new PropertyItem(KEY_CONDITION_ATTR, Inter.getLocText("FR-Designer_Condition_Attributes"),
                "conditionattr", new PropertyMode[]{PropertyMode.REPORT, PropertyMode.REPORT_PARA, PropertyMode.REPORT_FLOAT, PropertyMode.POLY, PropertyMode.POLY_CHART},
                new PropertyMode[]{PropertyMode.REPORT, PropertyMode.FORM_REPORT, PropertyMode.POLY_REPORT});
        // 超级链接
        PropertyItem hyperlink = new PropertyItem(KEY_HYPERLINK, Inter.getLocText("FR-Designer_Hyperlink"),
                "hyperlink", new PropertyMode[]{PropertyMode.REPORT, PropertyMode.REPORT_PARA, PropertyMode.REPORT_FLOAT, PropertyMode.POLY, PropertyMode.POLY_CHART},
                new PropertyMode[]{PropertyMode.REPORT, PropertyMode.REPORT_FLOAT, PropertyMode.FORM_REPORT, PropertyMode.POLY_REPORT});
        // 组件库
        PropertyItem widgetLib = new PropertyItem(KEY_WIDGET_LIB, Inter.getLocText("FR-Designer_Widget_Library"),
                "widgetlib", new PropertyMode[]{PropertyMode.FORM},
                new PropertyMode[]{PropertyMode.FORM});
        // 权限编辑
        PropertyItem authorityEdition = new PropertyItem(KEY_AUTHORITY_EDITION, Inter.getLocText("FR-Designer_Permissions_Edition"),
                "authorityedit", new PropertyMode[]{PropertyMode.AUTHORITY_EDITION_DISABLED},
                new PropertyMode[]{PropertyMode.AUTHORITY_EDITION});
        // 已配置角色
        PropertyItem configuredRoles = new PropertyItem(KEY_CONFIGURED_ROLES, Inter.getLocText("FR-Designer_Configured_Roles"),
                "configuredroles", new PropertyMode[]{PropertyMode.AUTHORITY_EDITION_DISABLED},
                new PropertyMode[]{PropertyMode.AUTHORITY_EDITION});

        propertyItemMap.put(KEY_CELL_ELEMENT, cellElement);
        propertyItemMap.put(KEY_CELL_ATTR, cellAttr);
        propertyItemMap.put(KEY_FLOAT_ELEMENT, floatElement);
        propertyItemMap.put(KEY_WIDGET_SETTINGS, widgetSettings);
        propertyItemMap.put(KEY_CONDITION_ATTR, conditionAttr);
        propertyItemMap.put(KEY_HYPERLINK, hyperlink);
        propertyItemMap.put(KEY_WIDGET_LIB, widgetLib);
        propertyItemMap.put(KEY_AUTHORITY_EDITION, authorityEdition);
        propertyItemMap.put(KEY_CONFIGURED_ROLES, configuredRoles);
    }

    // "无可用配置项"面板
    private JPanel getDefaultPane(String prompt) {
        JPanel defaultPane = new JPanel();
        UILabel label = new UILabel(prompt);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        defaultPane.setLayout(new BorderLayout());
        defaultPane.add(label, BorderLayout.CENTER);
        return defaultPane;
    }

    public void updateCellElementState(boolean isSelectedOneCell) {
        PropertyItem cellElement = propertyItemMap.get(KEY_CELL_ELEMENT);
        if (isSelectedOneCell) {
            enableCellElementPane(cellElement);
        } else {  // 如果选中多个单元格，禁用单元格元素 tab
            disableCellElementPane(cellElement);
            refreshRightPane();
        }
    }

    // 禁用单元格元素tab
    private void disableCellElementPane(PropertyItem cellElement) {
        cellElement.setEnabled(false);
        if (cellElement.isPoppedOut()) {
            cellElement.popupDialog.showDefaultPane();
        }
    }
    // 禁用单元格元素tab
    private void enableCellElementPane(PropertyItem cellElement) {
        cellElement.setEnabled(true);
        if (cellElement.isPoppedOut()) {
            cellElement.popupDialog.replaceContentPane(cellElement);
        }
    }

    private void initContentPane() {
        initRightPane();
        initLeftPane();
    }

    // 右侧属性面板
    private void initRightPane() {
        rightPane = new JPanel();
        propertyCard = new CardLayout();
        rightPane.setBackground(Color.green);
        rightPane.setLayout(propertyCard);
        for (PropertyItem item : propertyItemMap.values()) {
            if (item.isPoppedOut() || !item.isVisible()) {
                continue;
            }
            rightPane.add(item.getName(), item.getPropertyPanel());
        }
        rightPane.add(DEFAULT_PANE, defaultPane);
        rightPane.add(DEFAULT_AUTHORITY_PANE, defaultAuthorityPane);

        replaceRightPane(rightPane);
        refreshRightPane();
    }

    // 左侧按钮面板
    private void initLeftPane() {
        leftPane = new JPanel();
        leftPane.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 4, 4));
        for (PropertyItem item : propertyItemMap.values()) {
            if (item.isPoppedOut() || !item.isVisible()) {
                continue;
            }
            leftPane.add(item.getButton());
        }

        leftPane.setBackground(UIConstants.PROPERTY_PANE_BACKGROUND);
        replaceLeftPane(leftPane);
    }

    public void switchMode(PropertyMode mode) {
        if (currentMode != null && currentMode.equals(mode)) {
            return;
        }
        currentMode = mode;
        updateAllPropertyPane();
    }

    public void updateAllPropertyPane() {
        updatePropertyItemMap();
        initContentPane();
    }

    private void updatePropertyItemMap() {
        for (PropertyItem item : propertyItemMap.values()) {
            item.updateStatus();
        }
    }

    // 弹出面板时，更新框架内容
    private void removeItem(PropertyItem propertyItem) {
        leftPane.remove(propertyItem.getButton());
        rightPane.remove(propertyItem.getPropertyPanel());
        refreshRightPane();
        refreshContainer();
    }

    @Override
    public void onResize() {
        if (!isRightPaneVisible()) {
            resetPropertyIcons();
        } else {
            refreshRightPane();
        }
        for (PropertyItem item : propertyItemMap.values()) {
            item.onResize();
        }
    }

    public EastRegionContainerPane(JPanel leftPane, JPanel rightPane) {
        super(leftPane, rightPane);
    }

    public void replaceUpPane(JComponent pane) {
        replaceCellElementPane(pane);
    }

    public void replaceDownPane(JComponent pane) {
        replaceCellAttrPane(pane);
    }

    public JComponent getUpPane() {
        return getCellElementPane();
    }

    public JComponent getDownPane() {
        return getCellAttrPane();
    }

    public void replaceCellElementPane(JComponent pane) {
        propertyItemMap.get(KEY_CELL_ELEMENT).replaceContentPane(pane);
    }

    public JComponent getCellElementPane() {
        return propertyItemMap.get(KEY_CELL_ELEMENT).getContentPane();
    }

    public void replaceCellAttrPane(JComponent pane) {
        propertyItemMap.get(KEY_CELL_ATTR).replaceContentPane(pane);
    }

    public JComponent getCellAttrPane() {
        return propertyItemMap.get(KEY_CELL_ATTR).getContentPane();
    }

    public void replaceFloatElementPane(JComponent pane) {
        propertyItemMap.get(KEY_FLOAT_ELEMENT).replaceContentPane(pane);
    }

    public JComponent getFloatElementPane() {
        return propertyItemMap.get(KEY_FLOAT_ELEMENT).getContentPane();
    }

    public void replaceWidgetSettingsPane(JComponent pane) {
        propertyItemMap.get(KEY_WIDGET_SETTINGS).replaceContentPane(pane);
    }

    public JComponent getWidgetSettingsPane() {
        return propertyItemMap.get(KEY_WIDGET_SETTINGS).getContentPane();
    }

    public void replaceConditionAttrPane(JComponent pane) {
        propertyItemMap.get(KEY_CONDITION_ATTR).replaceContentPane(pane);
    }

    public JComponent getConditionAttrPane() {
        return propertyItemMap.get(KEY_CONDITION_ATTR).getContentPane();
    }

    public void replaceHyperlinkPane(JComponent pane) {
        propertyItemMap.get(KEY_HYPERLINK).replaceContentPane(pane);
    }

    public JComponent getHyperlinkPane() {
        return propertyItemMap.get(KEY_HYPERLINK).getContentPane();
    }

    public void replaceWidgetLibPane(JComponent pane) {
        propertyItemMap.get(KEY_WIDGET_LIB).replaceContentPane(pane);
    }

    public JComponent getWidgetLibPane() {
        return propertyItemMap.get(KEY_WIDGET_LIB).getContentPane();
    }

    public void replaceAuthorityEditionPane(JComponent pane) {
        propertyItemMap.get(KEY_AUTHORITY_EDITION).replaceContentPane(pane);
    }

    public JComponent getAuthorityEditionPane() {
        return propertyItemMap.get(KEY_AUTHORITY_EDITION).getContentPane();
    }

    public void replaceConfiguredRolesPane(JComponent pane) {
        propertyItemMap.get(KEY_CONFIGURED_ROLES).replaceContentPane(pane);
    }

    public JComponent getConfiguredRolesPane() {
        return propertyItemMap.get(KEY_CONFIGURED_ROLES).getContentPane();
    }

    public void addParameterPane(JComponent paraPane) {
        propertyItemMap.get(KEY_WIDGET_SETTINGS).replaceHeaderPane(paraPane);
    }

    public void setParameterHeight(int height) {
        // stub
    }

    public static void main(String[] args){
        JFrame jf = new JFrame("test");
//        jf = new JFrame("test");

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel cc = new JPanel();
        cc.setBackground(Color.WHITE);
//        JPanel leftPane = new JPanel();
//        leftPane.setBackground(Color.yellow);
//        JPanel rightPane = new JPanel();
//        rightPane.setBackground(Color.green);
//
//        JButton b1, b2;
//        b1 = new JButton("b1");
//        b2 = new JButton("b2");
//        b1.setPreferredSize(new Dimension(40, 40));
//        b2.setPreferredSize(new Dimension(40, 40));
//        leftPane.add(b1);
//        leftPane.add(b2);
//        leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.Y_AXIS));

        JPanel content = (JPanel)jf.getContentPane();
//        content.setLayout(null);
        content.add(cc, BorderLayout.CENTER);
        content.add(new EastRegionContainerPane(), BorderLayout.EAST);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(400, 400);
        jf.setVisible(true);
    }

    public void removeParameterPane() {
        propertyItemMap.get(KEY_WIDGET_SETTINGS).removeHeaderPane();
    }

    public void switchTabTo(String tabName) {
        PropertyItem propertyItem = propertyItemMap.get(tabName);
        if (propertyItem == null) {
            return;
        }
        if (propertyItem.isVisible() && propertyItem.isEnabled() && !propertyItem.isPoppedOut()) {
            propertyCard.show(rightPane, tabName);
            propertyItem.setTabButtonSelected();
        }
    }

    /**
     * 刷新右面板
     */
    public void refreshRightPane() {
        // 可继承，就继承
        if (selectedItem != null && selectedItem.isVisible() && selectedItem.isEnabled() && !selectedItem.isPoppedOut()) {
            selectedItem.setTabButtonSelected();
            propertyCard.show(rightPane, selectedItem.getName());
            return;
        }

        // 不可继承时，选中第一个可用 tab
        boolean hasAvailableTab = false;
        boolean hasEnabledTab = false;
        for (String name : propertyItemMap.keySet()) {
            PropertyItem propertyItem = propertyItemMap.get(name);
            if (propertyItem.isVisible() && propertyItem.isEnabled()) {
                hasEnabledTab = true;
                if (!propertyItem.isPoppedOut()) {
                    propertyCard.show(rightPane, name);  // 显示第一个可用tab
                    propertyItem.setTabButtonSelected();
                    hasAvailableTab = true;
                    break;
                }
            }
        }
        // 无可用 tab 时，显示提示文字
        if (!hasAvailableTab) {
            resetPropertyIcons();
            if (!hasEnabledTab && BaseUtils.isAuthorityEditing()) {
                propertyCard.show(rightPane, DEFAULT_AUTHORITY_PANE);
            } else {
                propertyCard.show(rightPane, DEFAULT_PANE);
            }
        }
    }

    public void refreshDownPane() {
    }

    private void refreshContainer() {
        validate();
        repaint();
        revalidate();
    }

    public int getToolPaneY() {
        return 0;
    }

    private void hideCurrentPopupPane() {
        if (currentPopupPane != null && currentPopupPane.isVisible()) {
            currentPopupPane.setVisible(false);
        }
    }


    private void resetPropertyIcons() {
        for (PropertyItem item : propertyItemMap.values()) {
            item.resetButtonIcon();
        }
    }

    public boolean isConditionAttrPaneEnabled() {
        return propertyItemMap.get(KEY_CONDITION_ATTR).isEnabled();
    }

    public boolean isWidgetSettingsPaneEnabled() {
        return propertyItemMap.get(KEY_WIDGET_SETTINGS).isEnabled();
    }

    public boolean isCellAttrPaneEnabled() {
        return propertyItemMap.get(KEY_CELL_ATTR).isEnabled();
    }


    class PropertyItem {
        private UIButton button;
        private String name;  // 用于 card 切换
        private String title;  // 用于显示
        private JComponent propertyPanel;
        private JComponent contentPane;
        private JComponent headerPane;  // 在contentPane 上方，可以用于显示参数面板
        private Container contentArea;  // 包含 headerPane 和 contentPane
        private FixedPopupPane popupPane;  // 左侧固定弹出框
        private PopupToolPane popupToolPane;  // 弹出工具条
        private PopupDialog popupDialog;  // 弹出框
        private boolean isPoppedOut = false;  // 是否弹出
        private boolean isVisible = true;  // 是否可见
        private boolean isEnabled = true;  // 是否可用
        private Set<PropertyMode> visibleModes;
        private Set<PropertyMode> enableModes;
        private static final int MAX_PARA_HEIGHT = 240;

        // 完整icon路径为 ICON_BASE_DIR + btnIconName + iconSuffix
        private static final String ICON_BASE_DIR = "/com/fr/design/images/buttonicon/propertiestab/";
        private static final String ICON_SUFFIX_NORMAL = "_normal.png";
        private static final String ICON_SUFFIX_DISABLED = "_disabled.png";
        private static final String ICON_SUFFIX_SELECTED = "_selected.png";
        private String btnIconName;
        private String iconSuffix = ICON_SUFFIX_NORMAL;  // normal, diabled, selected, 三者之一
        private final Color selectedBtnBackground = new Color(0xF5F5F7);
        private Color originBtnBackground;

        public PropertyItem(String name, String title, String btnIconName, PropertyMode[] visibleModes, PropertyMode[] enableModes) {
            this.name = name;
            this.title = title;
            this.btnIconName = btnIconName;
            initButton();
            initPropertyPanel();
            initModes(visibleModes, enableModes);
        }

        private void initModes(PropertyMode[] visibleModes, PropertyMode[] enableModes) {
            this.enableModes = new HashSet<>();
            this.visibleModes = new HashSet<>();
            for (PropertyMode enableMode : enableModes) {
                this.enableModes.add(enableMode);
            }
            for (PropertyMode visibleMode : visibleModes) {
                this.visibleModes.add(visibleMode);
            }
            this.visibleModes.addAll(this.enableModes);  // 可用必可见
        }

        public void updateStatus() {
            setEnabled(enableModes.contains(currentMode));
            setVisible(visibleModes.contains(currentMode));
            if (isPoppedOut()) {
                if (!isVisible()) {
                    popToFrame();
                } else if (!isEnabled()) {
                    popupDialog.showDefaultPane();
                }
            }
        }

        public void reAddContentArea() {
            propertyPanel.add(contentArea, BorderLayout.CENTER);
        }

        public boolean isVisible() {
            return isVisible;
        }

        public void setVisible(boolean isVisible) {
            this.isVisible = isVisible;
        }

        public boolean isEnabled() {
            return isEnabled;
        }

        // 选项不可用
        public void setEnabled(boolean isEnabled) {
            this.isEnabled = isEnabled;
            button.setEnabled(isEnabled);
        }

        private void initPropertyPanel() {
            propertyPanel = new JPanel();
            propertyPanel.setBackground(Color.pink);
            contentPane = generateContentPane();
            popupToolPane = new PopupToolPane(this, PopupToolPane.DOWN_BUTTON);
            headerPane = new JPanel();
            headerPane.setPreferredSize(new Dimension(headerPane.getPreferredSize().width, 0));  // 默认隐藏
            contentArea = new JPanel(new BorderLayout());
            contentArea.add(headerPane, BorderLayout.NORTH);
            contentArea.add(contentPane, BorderLayout.CENTER);
            propertyPanel.setLayout(new BorderLayout());
            propertyPanel.add(popupToolPane, BorderLayout.NORTH);
            propertyPanel.add(contentArea, BorderLayout.CENTER);
        }

        public boolean isPoppedOut() {
            return isPoppedOut;
        }

        public JComponent generateContentPane() {
            JComponent contentPane = new JPanel();
            UIButton testBtn = new UIButton(name);
            testBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setEnabled(!button.isEnabled());
                }
            });
            contentPane.add(testBtn);
            return contentPane;
        }

        public void replaceContentPane(JComponent pane) {
            contentArea.remove(this.contentPane);
            contentArea.add(this.contentPane = pane);
            if (popupDialog != null && isPoppedOut) {
                popupDialog.replaceContentPane(this);
            }
            if (popupPane != null && popupPane.isVisible()) {
                popupPane.replaceContentPane(contentArea);
            }

            refreshContainer();
        }

        public JComponent getContentPane() {
            return contentPane;
        }

        public Container getContentArea() {
            return contentArea;
        }

        public void replaceHeaderPane(JComponent pane) {
            contentArea.remove(headerPane);
            int height = Math.min(pane.getPreferredSize().height, MAX_PARA_HEIGHT);
            pane.setPreferredSize(new Dimension(pane.getPreferredSize().width, height));
            headerPane = pane;
            contentArea.add(headerPane, BorderLayout.NORTH);

            refreshContainer();
        }

        public void removeHeaderPane() {
            contentArea.remove(headerPane);
            refreshContainer();
        }

        public JComponent getHeaderPane() {
            return headerPane;
        }

        public void onResize() {
            if (isRightPaneVisible()) {
                hideCurrentPopupPane();
                replaceContentPane(contentPane);
            } else if(popupPane != null && popupPane.isVisible()) {
                popupPane.replaceContentPane(contentArea);
            }
        }

        private String getBtnIconUrl() {
            return ICON_BASE_DIR + btnIconName + iconSuffix;
        }

        public void resetButtonIcon() {
            if (iconSuffix.equals(ICON_SUFFIX_SELECTED)) {
                iconSuffix = ICON_SUFFIX_NORMAL;
                button.setIcon(BaseUtils.readIcon(getBtnIconUrl()));
                button.setBackground(originBtnBackground);
                button.setOpaque(false);
            }
        }

        public void setTabButtonSelected() {
            resetPropertyIcons();
            iconSuffix = ICON_SUFFIX_SELECTED;
            button.setIcon(BaseUtils.readIcon(getBtnIconUrl()));
            button.setBackground(selectedBtnBackground);
            button.setOpaque(true);
            selectedItem = this;
        }

        private boolean isTabButtonSelected() {
            return button.getBackground() == selectedBtnBackground;
        }

        private void initButton() {
            button = new UIButton(BaseUtils.readIcon(getBtnIconUrl())) {
                public Dimension getPreferredSize() {
                    return new Dimension(TAB_BUTTON_WIDTH, TAB_BUTTON_HEIGHT);
                }
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                }
            };
            button.set4LargeToolbarButton();
            button.setUI(new UIButtonUI() {
                @Override
                protected void doExtraPainting(UIButton b, Graphics2D g2d, int w, int h, String selectedRoles) {
                    if (isPressed(b) && b.isPressedPainted()) {
                        Color pressColor = isTabButtonSelected() ? UIConstants.TAB_BUTTON_PRESS_SELECTED : UIConstants.TAB_BUTTON_PRESS;
                        GUIPaintUtils.fillPressed(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), pressColor);
                    } else if (isRollOver(b)) {
                        Color hoverColor = isTabButtonSelected() ? UIConstants.TAB_BUTTON_HOVER_SELECTED : UIConstants.TAB_BUTTON_HOVER;
                        GUIPaintUtils.fillRollOver(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), b.isPressedPainted(), hoverColor);
                    } else if (b.isNormalPainted()) {
                        GUIPaintUtils.fillNormal(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), b.isPressedPainted());
                    }
                }
            });
            originBtnBackground = button.getBackground();
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isRightPaneVisible()) {
                        propertyCard.show(rightPane, name);
                    } else {
                        popupFixedPane();
                    }
                    setTabButtonSelected();
                }
            });
            button.setToolTipText(title);
        }

        public UIButton getButton() {
            return button;
        }

        public String getName() {
            return name;
        }

        public String getTitle() {
            return title;
        }

        public JComponent getPropertyPanel() {
            return propertyPanel;
        }

        // 固定弹窗
        public void popupFixedPane() {
            if (popupPane == null) {
                popupPane = new FixedPopupPane(this);
            }
            if (popupPane.isVisible()) {
                popupPane.setVisible(false);
            } else {
                hideCurrentPopupPane();
                currentPopupPane = popupPane;
                GUICoreUtils.showPopupMenu(popupPane, button, -popupPane.getPreferredSize().width, 0);
            }
        }

        // 弹出对话框
        public void popupDialog() {
            if (isPoppedOut) {
                return;
            }
            isPoppedOut = true;
            if (popupDialog == null) {
                popupDialog = new PopupDialog(this);
            } else {
                popupDialog.replaceContentPane(this);
                popupDialog.adjustLocation();
                popupDialog.setVisible(true);
            }
            removeItem(this);
        }

        public void popToFrame() {
            if (isPoppedOut) {
                isPoppedOut = false;
                popupDialog.setVisible(false);
                reAddContentArea();
                initContentPane();
                onResize();
                if (isEnabled()) {
                    propertyCard.show(rightPane, getName());
                    setTabButtonSelected();
                }
                refreshContainer();
            }
        }
    }

    private class FixedPopupPane extends JPopupMenu {
        private Container contentPane;
        private PropertyItem propertyItem;

        FixedPopupPane(PropertyItem propertyItem) {
            this.propertyItem = propertyItem;
            contentPane = propertyItem.getContentArea();
            this.setLayout(new BorderLayout());
            this.add(new PopupToolPane(propertyItem), BorderLayout.NORTH);
            this.add(contentPane, BorderLayout.CENTER);
            this.setOpaque(false);
            setPreferredSize(new Dimension(CONTAINER_WIDTH - TAB_WIDTH, POPUP_DEFAULT_HEIGHT));
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
            if (visible == true) {
                replaceContentPane(propertyItem.getContentArea());
            } else {
                propertyItem.reAddContentArea();
            }
        }

        public void menuSelectionChanged(boolean isIncluded) {
        }

        public Container getContentPane() {
            return contentPane;
        }

        public void replaceContentPane(Container pane) {
            this.remove(this.contentPane);
            this.add(this.contentPane = pane);
            refreshContainer();
        }

        private void refreshContainer() {
            validate();
            repaint();
            revalidate();
        }
    }

    // 弹出属性面板的工具条
    private class PopupToolPane extends JPanel {
        private String title;
        private PropertyItem propertyItem;
        private String buttonType;
        private JDialog parentDialog;  // 如果不在对话框中，值为null
        private Color originColor;  // 初始背景
        private JPanel contentPane;
        private boolean isMovable = false;
        private Point mouseDownCompCoords;  // 存储按下左键的位置，移动对话框时会用到

        private static final int MIN_X = -150;
        private static final int MAX_X_SHIFT = 50;
        private static final int MAX_Y_SHIFT = 50;

        private static final String NO_BUTTON = "NoButton";
        private static final String UP_BUTTON = "UpButton";
        private static final String DOWN_BUTTON = "DownButton";

        private MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
                if (mouseDownCompCoords == null) {
                    contentPane.setBackground(originColor);
                }
                repaint();
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getX() >= ARROW_RANGE_START) {
                    onPop();
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords = null;
                if (!getBounds().contains(e.getPoint())) {
                    contentPane.setBackground(originColor);
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getX() < ARROW_RANGE_START) {
                    mouseDownCompCoords = e.getPoint();
                }
            }
        };

        private MouseMotionListener mouseMotionListener = new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (e.getX() >= ARROW_RANGE_START) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else if (isMovable) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    contentPane.setBackground(UIConstants.POPUP_TITLE_BACKGROUND);
                } else {
                    setCursor(Cursor.getDefaultCursor());
                }
                repaint();
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isMovable && mouseDownCompCoords != null) {
                    Point currCoords = e.getLocationOnScreen();
                    int x = currCoords.x - mouseDownCompCoords.x;
                    int y = currCoords.y - mouseDownCompCoords.y;
                    //屏幕可用区域
                    Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

                    int minY = screen.y;
                    int maxX = Toolkit.getDefaultToolkit().getScreenSize().width - MAX_X_SHIFT;
                    int maxY = Toolkit.getDefaultToolkit().getScreenSize().height - MAX_Y_SHIFT;
                    if (x < MIN_X) {
                        x = MIN_X;
                    } else if (x > maxX) {
                        x = maxX;
                    }
                    if (y < minY) {
                        y = minY;
                    } else if (y > maxY) {
                        y = maxY;
                    }
                    // 移动到屏幕边缘时，需要校正位置
                    parentDialog.setLocation(x, y);
                }
            }
        };

        public PopupToolPane(PropertyItem propertyItem) {
            this(propertyItem, NO_BUTTON);
        }

        public PopupToolPane(PropertyItem propertyItem, String buttonType) {
            super();
            this.propertyItem = propertyItem;
            this.title = propertyItem.getTitle();
            originColor = UIConstants.UI_TOOLBAR_COLOR;

            contentPane = new JPanel();
            contentPane.setBackground(originColor);
            contentPane.setLayout(new BorderLayout());
            UILabel label = new UILabel(title);
            contentPane.add(label, BorderLayout.WEST);
            contentPane.setBorder(new EmptyBorder(5, 10, 5, 0));

            setLayout(new BorderLayout());
            add(contentPane, BorderLayout.CENTER);
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.TOOLBAR_BORDER_COLOR));
            initToolButton(buttonType);
        }

        public void setParentDialog(JDialog parentDialog) {
            this.parentDialog = parentDialog;
            isMovable = true;
        }

        private void initToolButton(final String buttonType) {
            this.buttonType = buttonType;
            if (StringUtils.isEmpty(buttonType) || buttonType.equals(NO_BUTTON)) {
                return;
            }
            // validate
            if (!buttonType.equals(UP_BUTTON) && !buttonType.equals(DOWN_BUTTON)) {
                throw new IllegalArgumentException("unknown button type: " + buttonType);
            }
            addMouseMotionListener(mouseMotionListener);
            addMouseListener(mouseListener);
        }

        // 触发弹入、弹出
        private void onPop() {
            if (buttonType.equals(DOWN_BUTTON)) {
                propertyItem.popupDialog();
            } else if (buttonType.equals(UP_BUTTON)) {
                propertyItem.popToFrame();
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(super.getPreferredSize().width, POPUP_TOOLPANE_HEIGHT);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Image button;
            g.setColor(new Color(69, 135, 255));
            g.setFont(FRFont.getInstance().applySize(14));
            if (buttonType.equals(NO_BUTTON)) {
                return;
            }
            if (buttonType.equals(DOWN_BUTTON)) {
                button = UIConstants.POP_BUTTON_DOWN;
            } else {
                button = UIConstants.POP_BUTTON_UP;
            }
            g.drawImage(button, ARROW_RANGE_START + 8, 4, 16, 16, null);
        }
    }

    private class PopupDialog extends JDialog {
        private Container container;
        private static final int RESIZE_RANGE = 8;
        private Cursor originCursor;
        private Cursor southResizeCursor = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
        private Point mouseDownCompCoords;
        private JPanel contentWrapper;

        private Container contentPane;
        private JPanel defaultPane;  // 无可用配置项
        private PropertyItem propertyItem;
        public PopupDialog(PropertyItem propertyItem) {
            super(DesignerContext.getDesignerFrame());
            container = getContentPane();
            setUndecorated(true);
            this.propertyItem = propertyItem;
            PopupToolPane popupToolPane = new PopupToolPane(propertyItem, PopupToolPane.UP_BUTTON);
            popupToolPane.setParentDialog(this);
            contentPane = propertyItem.getContentArea();

            contentWrapper = new JPanel(new BorderLayout());
            contentWrapper.add(popupToolPane, BorderLayout.NORTH);
            contentWrapper.add(contentPane, BorderLayout.CENTER);
            contentWrapper.setBorder(BorderFactory.createLineBorder(UIConstants.PROPERTY_DIALOG_BORDER));

            JPanel horizontalToolPane = new JPanel() {
                @Override
                public void paint(Graphics g) {
                    g.drawImage(UIConstants.DRAG_BAR, 0, 0, getWidth(), getHeight(), null);
                    g.drawImage(UIConstants.DRAG_DOT, (getWidth() - RESIZE_RANGE) / 2, 3, RESIZE_RANGE, 5, null);
                }
            };
            contentWrapper.add(horizontalToolPane, BorderLayout.SOUTH);

            container.add(contentWrapper, BorderLayout.CENTER);
            setSize(CONTENT_WIDTH, POPUP_DEFAULT_HEIGHT + RESIZE_RANGE);
            adjustLocation();

            initListener();
            this.setVisible(true);

            defaultPane = getDefaultPane(Inter.getLocText("FR-Designer_No_Settings_Available"));
        }

        public void showDefaultPane() {
            replaceContentPane(defaultPane);
        }

        public void adjustLocation() {
            this.setLocation(
                    getLeftPane().getLocationOnScreen().x - CONTENT_WIDTH,
                    DesignerContext.getDesignerFrame().getLocationOnScreen().y + 228
            );
        }

        public void replaceContentPane(PropertyItem propertyItem) {
            this.propertyItem = propertyItem;
            replaceContentPane(propertyItem.getContentArea());
        }

        public void replaceContentPane(Container contentPane) {
            contentWrapper.remove(this.contentPane);
            contentWrapper.add(this.contentPane = contentPane, BorderLayout.CENTER);
            refreshContainer();
        }

        private void refreshContainer() {
            validate();
            repaint();
            revalidate();
        }

        private void initListener() {
            addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (mouseDownCompCoords != null) {
                        Rectangle bounds = getBounds();
                        Point currCoords = e.getLocationOnScreen();
                        bounds.height = currCoords.y - mouseDownCompCoords.y + bounds.height;
                        // 校正位置
                        if (bounds.height < POPUP_MIN_HEIGHT) {
                            bounds.height = POPUP_MIN_HEIGHT;
                        }
                        mouseDownCompCoords.y = currCoords.y;
                        setBounds(bounds);
                    }
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    if (originCursor == null) {  // 记录最初的光标
                        originCursor = getCursor();
                    }
                    if (e.getY() > getHeight() - RESIZE_RANGE) {
                        setCursor(southResizeCursor);
                    } else {
                        // 还原
                        if (mouseDownCompCoords == null && getCursor().equals(southResizeCursor)) {
                            setCursor(originCursor);
                        }
                    }

                    repaint();
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (getCursor().equals(southResizeCursor)) {
                        mouseDownCompCoords = e.getLocationOnScreen();
                    }
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    mouseDownCompCoords = null;
                }
            });
        }
    }
}