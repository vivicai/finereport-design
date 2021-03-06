package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.base.Utils;
import com.fr.chart.web.ChartHyperRelateFloatLink;
import com.fr.design.DesignModelAdapter;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.hyperlink.AbstractHyperLinkPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.stable.ParameterProvider;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-12-28 下午03:02:43
 *          类说明: 图表超链  关联悬浮元素
 */
public class ChartHyperRelateFloatLinkPane extends AbstractHyperLinkPane<ChartHyperRelateFloatLink> {
    private static final long serialVersionUID = -3308412003405587689L;

    private UITextField itemNameTextField;

    private UIComboBox floatNameBox;

    public ChartHyperRelateFloatLinkPane() {
        this.initComponent();
    }

    public ChartHyperRelateFloatLinkPane(HashMap hyperLinkEditorMap, boolean needRenamePane) {
        super(hyperLinkEditorMap, needRenamePane);
        this.initComponent();
    }

    private void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();

        if (needRenamePane()) {
            itemNameTextField = new UITextField();
            this.add(GUICoreUtils.createNamedPane(itemNameTextField, Inter.getLocText("FR-Designer-Hyperlink_Name") + ":"), BorderLayout.NORTH);
        }

        this.add(centerPane, BorderLayout.CENTER);
        floatNameBox = new UIComboBox(getFloatNames());
        floatNameBox.setPreferredSize(new Dimension(90, 20));

        JPanel pane = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
        pane.add(new UILabel(Inter.getLocText("M_Insert-Float") + ":"));
        pane.add(floatNameBox);

        Border boder = new LineBorder(UIConstants.TITLED_BORDER_COLOR);
        Font font = null;
        TitledBorder border = new TitledBorder(boder, Inter.getLocText(new String[]{"Related", "M_Insert-Float"}), 4, 2, font, new Color(1, 159, 222));
        // 圆角不行
        centerPane.setBorder(border);

        centerPane.add(pane, BorderLayout.NORTH);

        parameterViewPane = new ReportletParameterViewPane(getChartParaType(), getValueEditorPane(), getValueEditorPane());
        parameterViewPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer_Parameter")));
        parameterViewPane.setPreferredSize(new Dimension(500, 200));
        this.add(parameterViewPane, BorderLayout.SOUTH);
    }

    private String[] getFloatNames() {
        DesignModelAdapter adapter = DesignModelAdapter.getCurrentModelAdapter();
        if (adapter != null) {
            return adapter.getFloatNames();
        }
        return new String[0];
    }

    @Override
    public void populateBean(ChartHyperRelateFloatLink ob) {
        if (ob == null) {
            return;
        }

        if (itemNameTextField != null) {
            itemNameTextField.setText(ob.getItemName());
        }

        floatNameBox.setSelectedItem(ob.getRelateCCName());

        List parameterList = this.parameterViewPane.update();
        parameterList.clear();

        ParameterProvider[] parameters = ob.getParameters();
        parameterViewPane.populate(parameters);
    }

    @Override
    public ChartHyperRelateFloatLink updateBean() {
        ChartHyperRelateFloatLink chartLink = new ChartHyperRelateFloatLink();
        updateBean(chartLink);
        if (itemNameTextField != null) {
            chartLink.setItemName(this.itemNameTextField.getText());
        }
        return chartLink;
    }

    public void updateBean(ChartHyperRelateFloatLink chartLink) {

        if (floatNameBox.getSelectedItem() != null) {
            chartLink.setRelateCCName(Utils.objectToString(floatNameBox.getSelectedItem()));
        }

        List parameterList = this.parameterViewPane.update();
        if (parameterList != null && !parameterList.isEmpty()) {
            ParameterProvider[] parameters = new ParameterProvider[parameterList.size()];
            parameterList.toArray(parameters);

            chartLink.setParameters(parameters);
        } else {
            chartLink.setParameters(null);
        }
        if (itemNameTextField != null) {
            chartLink.setItemName(this.itemNameTextField.getText());
        }
    }

    @Override
    public String title4PopupWindow() {
        return Inter.getLocText(new String[]{"Related", "M_Insert-Float"});
    }

    public static class ChartNoRename extends ChartHyperRelateFloatLinkPane {
        protected boolean needRenamePane() {
            return false;
        }
    }
}