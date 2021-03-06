package com.fr.plugin.chart.treemap;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.chart.multilayer.VanChartMultiPiePlotPane;

/**
 * Created by Fangjie on 2016/7/11.
 */
public class VanChartTreeMapPlotPane extends VanChartMultiPiePlotPane {
    public static final String TITLE = Inter.getLocText("Plugin-ChartF_NewTreeMap");
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/plugin/chart/treemap/images/treeMap.png"
        };
    }

    @Override
    protected String[] getTypeTipName() {
        return new String[]{
                Inter.getLocText("Plugin-ChartF_TreeMapChart")
        };
    }

    @Override
    protected String getPlotTypeID() {
        return VanChartTreeMapPlot.VAN_CHART_TREE_MAP_PLOT_ID;
    }

    @Override
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_NewTreeMap");
    }

    protected Plot getSelectedClonedPlot(){
        VanChartTreeMapPlot newPlot = null;
        Chart[] treeMapVanChartTypes = TreeMapIndependentVanChart.TreeMapVanChartTypes;
        for(int i = 0, len = treeMapVanChartTypes.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (VanChartTreeMapPlot) treeMapVanChartTypes[i].getPlot();
            }
        }

        Plot cloned = null;
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In treeMapChart");
        }
        return cloned;
    }


    /**
     * 防止新建其他图表从而切换很卡
     * @return
     */
    public Chart getDefaultChart() {
        return TreeMapIndependentVanChart.TreeMapVanChartTypes[0];
    }
}
