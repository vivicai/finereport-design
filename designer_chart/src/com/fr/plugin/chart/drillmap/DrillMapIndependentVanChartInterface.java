package com.fr.plugin.chart.drillmap;

import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.plugin.chart.designer.other.VanChartOtherPane;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.drillmap.designer.data.VanChartDrillMapDataPane;
import com.fr.plugin.chart.drillmap.designer.other.VanChartDrillMapOtherPane;
import com.fr.plugin.chart.drillmap.designer.type.VanChartDrillMapPlotPane;
import com.fr.plugin.chart.map.MapIndependentVanChartInterface;
import com.fr.plugin.chart.map.designer.style.VanChartMapStylePane;

/**
 * Created by Mitisky on 16/6/20.
 */
public class DrillMapIndependentVanChartInterface extends MapIndependentVanChartInterface{

    /**
     * 图标路径
     *
     * @return 图标路径
     */
    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/drillmap.png";
    }
    /**
     * 图表的类型定义界面类型，就是属性表的第一个界面
     *
     * @return 图表的类型定义界面类型
     */
    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartDrillMapPlotPane();
    }

    @Override
    public ChartDataPane getChartDataPane(AttributeChangeListener listener) {
        return new VanChartDrillMapDataPane(listener);
    }

    /**
     * 图表的属性界面数组
     * @return 属性界面
     */
    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener){
        VanChartStylePane stylePane = new VanChartMapStylePane(listener);
        VanChartOtherPane otherPane = new VanChartDrillMapOtherPane();
        return new AbstractChartAttrPane[]{stylePane, otherPane};
    }

    public String getPlotTypeTitle4PopupWindow(){
        return VanChartDrillMapPlotPane.TITLE;
    }
}
