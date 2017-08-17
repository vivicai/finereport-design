package com.fr.plugin.chart.wordcloud.designer.style;

import com.fr.base.Utils;
import com.fr.base.background.ImageBackground;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.backgroundpane.ImageBackgroundQuickPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.style.series.VanChartAbstractPlotSeriesPane;
import com.fr.plugin.chart.wordcloud.CloudShapeType;
import com.fr.plugin.chart.wordcloud.VanChartWordCloudPlot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by Mitisky on 16/11/29.
 */
public class VanChartWordCloudSeriesPane extends VanChartAbstractPlotSeriesPane {
    private static final String AUTO_FONT_SIZE = Inter.getLocText("Plugin-ChartF_Auto");
    private static final String CUSTOM_FONT_SIZE = Inter.getLocText("Plugin-ChartF_Define_Size");
    private static final double MAX_ROTATION = 90;
    private static final double LABEL_SIZE = 48;
    private UIComboBox fontNameComboBox;
    private UISpinner minRotation;
    private UISpinner maxRotation;
    private UIComboBox defineFontSize;
    private JPanel fontPanel;
    private UISpinner minFontSize;
    private UISpinner maxFontSize;

    private UIComboBox cloudShape;
    private ImageBackgroundQuickPane imageBackgroundQuickPane;

    public VanChartWordCloudSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    /**
     * 在每个不同类型Plot, 得到不同类型的属性. 比如: 柱形的风格, 折线的线型曲线.
     */
    @Override
    protected JPanel getContentInPlotType() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{createWordCloudStylePane()},
                new Component[]{new JSeparator()},
                new Component[]{createCloudShapePane()}
        };

        contentPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

        return contentPane;
    }

    private JPanel createWordCloudStylePane(){
        double labelSize = LABEL_SIZE;
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        double[] centerC = {labelSize,f,p,f};
        double[] centerR = {p};

        minRotation = new UISpinner(-MAX_ROTATION,MAX_ROTATION,1,0);
        maxRotation = new
                UISpinner(-MAX_ROTATION,MAX_ROTATION,1,0);
        Component[][] centerComps = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Rotation_Angle")), minRotation,
                        new UILabel("-"), maxRotation},
        };
        JPanel centerPanel = TableLayoutHelper.createTableLayoutPane(centerComps,centerR,centerC);

        double[] northC = {labelSize,f};
        double[] northR = {p,p};
        fontNameComboBox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        defineFontSize = new UIComboBox(new String[]{AUTO_FONT_SIZE, CUSTOM_FONT_SIZE});
        Component[][] northComps = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Font")), fontNameComboBox},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Font-Size")), defineFontSize }
        };
        JPanel northPanel = TableLayoutHelper.createTableLayoutPane(northComps,northR,northC);

        minFontSize = new UISpinner(0,Double.MAX_VALUE,1,10);
        maxFontSize = new UISpinner(0,Double.MAX_VALUE,1,100);
        Component[][] fontComps = new Component[][]{
                new Component[]{null, minFontSize,
                        new UILabel("-"), maxFontSize},
        };
        fontPanel = TableLayoutHelper.createTableLayoutPane(fontComps,centerR,centerC);

        JPanel panel = new JPanel(new BorderLayout(0,4));
        panel.add(centerPanel, BorderLayout.NORTH);
        panel.add(northPanel, BorderLayout.CENTER);
        panel.add(fontPanel, BorderLayout.SOUTH);

        defineFontSize.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                checkFontPane();
            }
        });

        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Chart-Style_Name"), panel);
    }

    private JPanel createCloudShapePane() {
        cloudShape = new UIComboBox(CloudShapeType.getTypes());
        imageBackgroundQuickPane = new ImageBackgroundQuickPane(false){
            @Override
            public Dimension getPreferredSize() {
                if(cloudShape.getSelectedItem() == CloudShapeType.DEFAULT){
                    return new Dimension(0,0);
                } else {
                    return super.getPreferredSize();
                }
            }
        };

        JPanel panel = new JPanel(new BorderLayout(0,4));
        panel.add(cloudShape, BorderLayout.NORTH);
        panel.add(imageBackgroundQuickPane, BorderLayout.CENTER);

        cloudShape.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CloudShapeType type = (CloudShapeType)cloudShape.getSelectedItem();
                String path = type.getImageUrl();
                if(path != null) {
                    ImageBackground imageBackground = new ImageBackground(IOUtils.readImage(path));
                    imageBackgroundQuickPane.populateBean(imageBackground);
                } else {
                    imageBackgroundQuickPane.populateBean(new ImageBackground());
                }
                checkImagePane();
            }
        });

        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_Shape"), panel);
    }

    private void checkImagePane() {
        GUICoreUtils.setEnabled(imageBackgroundQuickPane, cloudShape.getSelectedItem() == CloudShapeType.CUSTOM);
    }

    private void checkFontPane() {
        fontPanel.setVisible(defineFontSize.getSelectedIndex() == 1);
    }

    /**
     * 更新Plot的属性到系列界面
     *
     * @param plot
     */
    @Override
    public void populateBean(Plot plot) {
        super.populateBean(plot);
        if(plot instanceof VanChartWordCloudPlot){
            VanChartWordCloudPlot wordCloudPlot = (VanChartWordCloudPlot)plot;
            fontNameComboBox.setSelectedItem(wordCloudPlot.getFont().getFamily());
            minRotation.setValue(wordCloudPlot.getMinRotation());
            maxRotation.setValue(wordCloudPlot.getMaxRotation());

            defineFontSize.setSelectedIndex(wordCloudPlot.isAutoFontSize() ? 0 : 1);
            minFontSize.setValue(wordCloudPlot.getMinFontSize());
            maxFontSize.setValue(wordCloudPlot.getMaxFontSize());

            cloudShape.setSelectedItem(wordCloudPlot.getShapeType());
            ImageBackground imageBackground = wordCloudPlot.getShapeImage();
            if(imageBackground != null) {
                imageBackgroundQuickPane.populateBean(imageBackground);
            }
        }
        checkFontPane();
        checkImagePane();
    }

    /**
     * 保存 系列界面的属性到Plot
     *
     * @param plot
     */
    @Override
    public void updateBean(Plot plot) {
        super.updateBean(plot);
        if(plot instanceof VanChartWordCloudPlot){
            VanChartWordCloudPlot wordCloudPlot = (VanChartWordCloudPlot)plot;
            wordCloudPlot.setFont(FRFont.getInstance(fontNameComboBox.getSelectedItem().toString(), Font.PLAIN, 9));
            wordCloudPlot.setMinRotation(minRotation.getValue());
            wordCloudPlot.setMaxRotation(maxRotation.getValue());

            wordCloudPlot.setAutoFontSize(defineFontSize.getSelectedIndex() == 0);
            wordCloudPlot.setMinFontSize(minFontSize.getValue());
            wordCloudPlot.setMaxFontSize(maxFontSize.getValue());

            wordCloudPlot.setShapeType((CloudShapeType) cloudShape.getSelectedItem());
            if(wordCloudPlot.getShapeType() != CloudShapeType.DEFAULT) {
                wordCloudPlot.setShapeImage((ImageBackground) imageBackgroundQuickPane.updateBean());
            } else {
                wordCloudPlot.setShapeImage(null);
            }
        }
    }
}
