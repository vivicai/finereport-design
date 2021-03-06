package com.fr.plugin.chart.funnel.designer;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.PiePlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.plugin.chart.designer.data.OneDimensionalPlotReportDataContentPane;
import com.fr.plugin.chart.designer.other.VanChartInteractivePaneWithOutSort;
import com.fr.plugin.chart.designer.other.VanChartOtherPane;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.funnel.designer.other.VanChartFunnelConditionPane;
import com.fr.plugin.chart.funnel.designer.style.VanChartFunnelSeriesPane;
import com.fr.plugin.chart.funnel.designer.type.VanChartFunnelTypePane;
import com.fr.plugin.chart.vanchart.AbstractIndependentVanChartUI;

/**
 * Created by Mitisky on 16/10/10.
 */
public class FunnelIndependentVanChartInterface extends AbstractIndependentVanChartUI {
    /**
     * 图表的类型定义界面类型，就是属性表的第一个界面
     *
     * @return 图表的类型定义界面类型
     */
    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartFunnelTypePane();
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent){
        return new PiePlotTableDataContentPane(parent);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent){
        return new OneDimensionalPlotReportDataContentPane(parent);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartFunnelSeriesPane(parent, plot);
    }

    /**
     * 图表的属性界面数组
     * @return 属性界面
     */
    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener){
        VanChartStylePane stylePane = new VanChartStylePane(listener);
        VanChartOtherPane otherPane = new VanChartOtherPane(){
            @Override
            protected BasicBeanPane<Chart> createInteractivePane() {
                return new VanChartInteractivePaneWithOutSort();
            }
        };

        return new AbstractChartAttrPane[]{stylePane, otherPane};
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new VanChartFunnelConditionPane(plot);
    }

    /**
     * 图标路径
     *
     * @return 图标路径
     */
    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/funnel.png";
    }

    public String getPlotTypeTitle4PopupWindow(){
        return VanChartFunnelTypePane.TITLE;
    }
}
