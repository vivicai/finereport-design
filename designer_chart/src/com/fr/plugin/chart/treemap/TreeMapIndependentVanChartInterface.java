package com.fr.plugin.chart.treemap;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.plugin.chart.designer.other.VanChartInteractivePaneWithOutSort;
import com.fr.plugin.chart.designer.other.VanChartOtherPane;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.multilayer.MultiPieIndependentVanChartInterface;
import com.fr.plugin.chart.treemap.style.VanChartTreeMapSeriesPane;

/**
 * Created by Fangjie on 2016/7/11.
 */
public class TreeMapIndependentVanChartInterface extends MultiPieIndependentVanChartInterface {
    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartTreeMapPlotPane();
    }

    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/treeMap.png";
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartTreeMapSeriesPane(parent, plot);
    }

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

    public String getPlotTypeTitle4PopupWindow(){
        return VanChartTreeMapPlotPane.TITLE;
    }
}
