package com.fr.design.chart.fun.impl;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.IndependentChartUIProvider;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.ChartsEditPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.CategoryPlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.CategoryPlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.design.mainframe.chart.gui.type.PiePlotPane;
import com.fr.general.ComparatorUtils;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * Created by mengao on 2017/4/24.
 * 用户使用第三方图表需要继承的面板抽象类
 */

@API(level = IndependentChartUIProvider.CURRENT_API_LEVEL)
public abstract class AbstractIndependentChartsUI extends AbstractProvider implements IndependentChartUIProvider {

    public String mark4Provider() {
        return getClass().getName();
    }


    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new PiePlotPane();
    }

    @Override
    public ChartDataPane getChartDataPane(AttributeChangeListener listener) {
        return new ChartDataPane(listener);
    }

    @Override
    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {
        return new CategoryPlotTableDataContentPane(parent);
    }

    @Override
    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {
        return new CategoryPlotReportDataContentPane(parent);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return null;
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(){
        return null;
    }

    public boolean equals(Object obj) {
        return obj != null && ComparatorUtils.equals(obj.getClass(), this.getClass());
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return null;
    }

    @Override
    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener){
        return new AbstractChartAttrPane[]{};
    }

    @Override
    public boolean isUseDefaultPane(){
        return false;
    }


    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/ChartF-Column.png";
    }

    @Override

    /**
     * plot面板的标题
     * 插件兼容
     */
    public String getPlotTypeTitle4PopupWindow(){
        return getPlotTypePane().title4PopupWindow();
    }

    @Override
    public ChartEditPane getChartEditPane(String plotID) {
        return new ChartsEditPane();
    }

}