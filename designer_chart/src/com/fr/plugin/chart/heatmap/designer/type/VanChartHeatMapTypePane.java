package com.fr.plugin.chart.heatmap.designer.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.heatmap.HeatMapIndependentVanChart;
import com.fr.plugin.chart.heatmap.VanChartHeatMapPlot;
import com.fr.plugin.chart.map.designer.type.VanChartMapPlotPane;
import com.fr.plugin.chart.map.designer.type.VanChartMapSourceChoosePane;
import com.fr.plugin.chart.map.server.CompatibleGEOJSONHelper;

/**
 * Created by Mitisky on 16/10/20.
 */
public class VanChartHeatMapTypePane extends VanChartMapPlotPane {
    public static final String TITLE = Inter.getLocText("Plugin-ChartF_NewHeatMap");

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/plugin/chart/heatmap/images/heatmap.png"
        };
    }

    @Override
    protected String[] getTypeTipName() {
        return new String[]{
                Inter.getLocText("Plugin-ChartF_HeatMap")
        };
    }

    /**
     * 获取各图表类型界面ID, 本质是plotID
     *
     * @return 图表类型界面ID
     */
    @Override
    protected String getPlotTypeID() {
        return VanChartHeatMapPlot.VAN_CHART_HEAT_MAP_ID;
    }

    /**
     * title应该是一个属性，不只是对话框的标题时用到，与其他组件结合时，也会用得到
     *
     * @return 绥化狂标题
     */
    @Override
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_NewHeatMap");
    }

    @Override
    protected VanChartMapSourceChoosePane createSourceChoosePane() {
        return new VanChartHeatMapSourceChoosePane();
    }

    //适用一种图表只有一种类型的
    public void populateBean(Chart chart) {
        typeDemo.get(0).isPressing = true;
        VanChartHeatMapPlot plot = (VanChartHeatMapPlot)chart.getPlot();
        populateSourcePane(plot);

        boolean enabled = !CompatibleGEOJSONHelper.isDeprecated(plot.getGeoUrl());
        GUICoreUtils.setEnabled(this.getTypePane(), enabled);
        GUICoreUtils.setEnabled(this.getSourceChoosePane().getSourceComboBox(), enabled);

        checkDemosBackground();
    }

    //热力地图不全屏
    @Override
    protected VanChartTools createVanChartTools() {
        VanChartTools tools = new VanChartTools();
        tools.setSort(false);
        tools.setExport(false);
        tools.setFullScreen(false);
        return tools;
    }

    protected Plot getSelectedClonedPlot(){
        Chart chart = getDefaultChart();
        VanChartHeatMapPlot newPlot = (VanChartHeatMapPlot)chart.getPlot();

        Plot cloned = null;
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        }
        return cloned;
    }

    protected Chart[] getDefaultCharts() {
        return HeatMapIndependentVanChart.HeatMapVanCharts;
    }

    public Chart getDefaultChart() {
        return HeatMapIndependentVanChart.HeatMapVanCharts[0];
    }
}
