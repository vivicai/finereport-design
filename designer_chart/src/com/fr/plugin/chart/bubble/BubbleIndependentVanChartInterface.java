package com.fr.plugin.chart.bubble;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.BubblePlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.plugin.chart.bubble.data.VanChartBubblePlotTableDataContentPane;
import com.fr.plugin.chart.designer.other.VanChartOtherPane;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.vanchart.AbstractIndependentVanChartUI;

/**
 * Created by Mitisky on 16/3/31.
 */
public class BubbleIndependentVanChartInterface extends AbstractIndependentVanChartUI {
    /**
     * 图表的类型定义界面类型，就是属性表的第一个界面
     *
     * @return 图表的类型定义界面类型
     */
    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartBubblePlotPane();
    }

    /**
     * 图标路径
     *
     * @return 图标路径
     */
    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/bubble.png";
    }
    @Override
    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartBubbleSeriesPane(parent, plot);
    }

    @Override
    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent){
        if(((VanChartBubblePlot) plot).isForceBubble()){
            return super.getTableDataSourcePane(plot, parent);
        }
        return new VanChartBubblePlotTableDataContentPane(parent);
    }

    @Override
    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent){
        if(((VanChartBubblePlot) plot).isForceBubble()){
            return super.getReportDataSourcePane(plot, parent);
        }
        return new BubblePlotReportDataContentPane(parent);
    }

    /**
     * 图表的属性界面数组
     * @return 属性界面
     */
    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener){
        VanChartStylePane stylePane = new VanChartBubbleStylePane(listener);
        VanChartOtherPane otherPane = new VanChartOtherPane(){
            protected BasicBeanPane<Chart> createInteractivePane() {
                return new VanChartBubbleInteractivePane();
            }
        };
        return new AbstractChartAttrPane[]{stylePane, otherPane};
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new VanChartBubbleConditionPane(plot);
    }

    public String getPlotTypeTitle4PopupWindow(){
        return VanChartBubblePlotPane.TITLE;
    }
}
