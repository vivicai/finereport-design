package com.fr.plugin.chart.structure.desinger.other;

import com.fr.chart.base.AttrBackground;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.plugin.chart.base.AttrFloatColor;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrNode;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.designer.other.condition.item.*;
import com.fr.plugin.chart.structure.VanChartStructureDataPoint;
import com.fr.plugin.chart.structure.VanChartStructurePlot;

import java.awt.*;

/**
 * Created by shine on 2017/2/15.
 */
public class VanChartStructureConditionPane extends DataSeriesConditionPane {

    public VanChartStructureConditionPane(Plot plot) {
        super(plot);
    }

    protected void initComponents() {
        super.initComponents();
        //添加全部条件属性后被遮挡
        liteConditionPane.setPreferredSize(new Dimension(300, 400));
    }

    @Override
    protected ChartConditionPane createListConditionPane() {
        return new ChartConditionPane(){
            @Override
            public String[] columns2Populate() {
                return new String[]{
                        VanChartStructureDataPoint.NODEID,
                        VanChartStructureDataPoint.PARENTID,
                        VanChartStructureDataPoint.SERIESNAME,
                        VanChartStructureDataPoint.NODENAME,
                        ChartConstants.VALUE
                };
            }
        };
    }

    @Override
    protected void addBasicAction() {
        classPaneMap.put(AttrBackground.class, new VanChartSeriesColorConditionPane(this));
        classPaneMap.put(AttrTooltip.class, new VanChartTooltipConditionPane(this, plot));
        classPaneMap.put(AttrLabel.class, new VanChartLabelConditionPane(this, plot));
        classPaneMap.put(AttrFloatColor.class, new VanChartFloatColorConditionPane(this));
        classPaneMap.put(AttrNode.class, new VanChartStructureNodeConditionPane(this));
    }

    protected void addStyleAction() {
    }

    /**
     * 返回图表class对象
     * @return class对象
     */
    public Class<? extends Plot> class4Correspond() {
        return VanChartStructurePlot.class;
    }
}

