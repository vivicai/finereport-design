package com.fr.plugin.chart.designer.style.datasheet;

import com.fr.base.FRContext;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.DataSheet;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.plugin.chart.VanChartAttrHelper;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.plugin.chart.designer.AbstractVanChartScrollPane;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.border.VanChartBorderPane;
import com.fr.plugin.chart.designer.component.format.FormatPaneWithNormalType;
import com.fr.plugin.chart.type.AxisType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 样式-数据表
 */
public class VanChartDataSheetPane extends AbstractVanChartScrollPane<Chart> {
    private static final long serialVersionUID = 5547658195141361981L;

    private UICheckBox isShowDataSheet;
    private JPanel dataSheetPane;

    private ChartTextAttrPane textAttrPane;
    private FormatPane formatPane;
    private VanChartBorderPane borderPane;

    private class ContentPane extends JPanel {

        private static final long serialVersionUID = 5601169655874455336L;

        public ContentPane() {
            initComponents();
        }

        private void initComponents() {
            isShowDataSheet = new UICheckBox(Inter.getLocText("Plugin-ChartF_Show_Data_Sheet"));
            dataSheetPane = createDataSheetPane();

            double p = TableLayout.PREFERRED;
            double f = TableLayout.FILL;
            double[] columnSize = {f};
            double[] rowSize = {p, p, p};
            Component[][] components =creatComponent(dataSheetPane);

            JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
            this.setLayout(new BorderLayout());
            this.add(panel,BorderLayout.CENTER);

            isShowDataSheet.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    checkDataSheetPaneUse();
                }
            });
        }
    }
    protected Component[][] creatComponent(JPanel dataSheetPane){

        Component[][] components = new Component[][]{
                new Component[]{isShowDataSheet},
                new Component[]{dataSheetPane}
        };
        return components;
    }
    // 检查数据表界面是否可用.
    private void checkDataSheetPaneUse() {
        dataSheetPane.setVisible(isShowDataSheet.isSelected());
    }

    private JPanel createDataSheetPane(){
        textAttrPane = new ChartTextAttrPane();
        formatPane = new FormatPaneWithNormalType();
        borderPane = new VanChartBorderPane();

        double p = TableLayout.PREFERRED;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double d = TableLayout4VanChartHelper.DESCRIPTION_AREA_WIDTH;
        double[] columnSize = {d, e};
        double[] rowSize = {p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("FR-Base_Format"),formatPane),null},
                new Component[]{TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("FR-Designer-Widget_Style"),textAttrPane),null},
                new Component[]{TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_Border"),borderPane),null},
        };

        return TableLayout4VanChartHelper.createGapTableLayoutPane(components,rowSize,columnSize);
    }

    @Override
    /**
     * 标题
     * @return 返回标题
     */
    public String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_DATA_TITLE;
    }

    @Override
    protected JPanel createContentPane() {
        return new ContentPane();
    }

    @Override
    public void updateBean(Chart chart) {
        if(chart == null) {
            return;
        }
        Plot plot = chart.getPlot();
        if(plot == null) {
            return;
        }
        DataSheet dataSheet = plot.getDataSheet();
        if (dataSheet == null) {
            dataSheet = new DataSheet();
            dataSheet.setFormat(VanChartAttrHelper.VALUE_FORMAT);
            plot.setDataSheet(dataSheet);
        }
        dataSheet.setVisible(isShowDataSheet.isSelected());
        if(isShowDataSheet.isSelected()){
            update(dataSheet);
        }
//        plot.setDataSheet2PlotList();
    }

    @Override
    public void populateBean(Chart chart) {
        if(chart == null || chart.getPlot() == null) {
            return;
        }
        VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot)chart.getPlot();
        if(rectanglePlot.getXAxisList().size() == 1){
            if(ComparatorUtils.equals(rectanglePlot.getDefaultXAxis().getAxisType(), AxisType.AXIS_CATEGORY)
                    && rectanglePlot.getDefaultXAxis().getPosition() == VanChartConstants.AXIS_BOTTOM
                    && rectanglePlot.getCategoryNum() == 1 ){

                //只有单个分类坐标轴且坐标轴位置在下面，数据表才可以用
                isShowDataSheet.setEnabled(!rectanglePlot.isAxisRotation());

                DataSheet dataSheet = chart.getPlot().getDataSheet();
                if (dataSheet != null) {
                    isShowDataSheet.setSelected(dataSheet.isVisible());
                    populate(dataSheet);
                }
                checkDataSheetPaneUse();
                return;

            }
        }

        isShowDataSheet.setSelected(false);
        isShowDataSheet.setEnabled(false);
        checkDataSheetPaneUse();
    }

    public void populate(DataSheet dataSheet) {
        FRFont font = FRContext.getDefaultValues().getFRFont() == null ? FRFont.getInstance() : FRContext.getDefaultValues().getFRFont();
        textAttrPane.populate(dataSheet.getFont() == null ? font : dataSheet.getFont());
        formatPane.populateBean(dataSheet.getFormat());
        borderPane.populate(dataSheet);

    }

    public DataSheet update(DataSheet dataSheet) {

        dataSheet.setFont(textAttrPane.updateFRFont());
        dataSheet.setFormat(formatPane.update());
        borderPane.update(dataSheet);
        return dataSheet;
    }


}