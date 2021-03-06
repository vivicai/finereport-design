package com.fr.plugin.chart.radar;

import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.VanChartLineTypePane;
import com.fr.plugin.chart.designer.component.VanChartLineWidthPane;
import com.fr.plugin.chart.designer.style.series.VanChartAbstractPlotSeriesPane;
import com.fr.plugin.chart.type.RadarType;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mitisky on 15/12/28.
 */
public class VanChartRadarSeriesPane extends VanChartAbstractPlotSeriesPane {

    private static final long serialVersionUID = 6766916711435248193L;

    private UIButtonGroup<String> radarType;//形态，圆形还是多边形

    public VanChartRadarSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    protected JPanel getContentInPlotType(){
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p,p,p,p,p,p,p};
        double[] col = {f};

        contentPane = TableLayoutHelper.createTableLayoutPane(getPaneComponents(), row, col);
        return contentPane;
    }

    private Component[][] getPaneComponents() {
        if(plot instanceof VanChartRadarPlot && ((VanChartRadarPlot)plot).isStackChart()) {
            return new Component[][]{
                    new Component[]{getColorPane()},
                    new Component[]{createRadarTypePane()},
                    new Component[]{createBorderPane()},
            };
        }

        return new Component[][] {
                new Component[]{createRadarTypePane()},
                new Component[]{createLineTypePane()},
                new Component[]{createMarkerPane()},
                new Component[]{createAreaFillColorPane()}
        };
    }

    //设置色彩面板内容
    protected void setColorPaneContent (JPanel panel) {
        panel.add(createAlphaPane(), BorderLayout.CENTER);
    }

    private JPanel createRadarTypePane() {
        radarType = new UIButtonGroup<String>(new String[]{Inter.getLocText("Plugin-ChartF_Circle"), Inter.getLocText("Plugin-ChartF_Polygonal")},
                new String[]{RadarType.CIRCLE.getType(), RadarType.POLYGON.getType()});
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(Inter.getLocText("Plugin-ChartF_Shape"), radarType);
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("FR-Chart-Style_Present"), panel);
    }

    protected VanChartLineTypePane getLineTypePane() {
        return new VanChartLineWidthPane();
    }

    /**
     * 更新Plot的属性到系列界面
     */
    public void populateBean(Plot plot) {
        if(plot == null) {
            return;
        }
        super.populateBean(plot);
        if(plot instanceof VanChartRadarPlot){
            VanChartRadarPlot radarPlot = (VanChartRadarPlot)plot;
            radarType.setSelectedItem(radarPlot.getRadarType().getType());
        }
    }

    /**
     * 保存 系列界面的属性到Plot
     */
    public void updateBean(Plot plot) {
        if(plot == null) {
            return;
        }
        super.updateBean(plot);
        if(plot instanceof VanChartRadarPlot){
            VanChartRadarPlot radarPlot = (VanChartRadarPlot)plot;
            radarPlot.setRadarType(RadarType.parse(radarType.getSelectedItem()));
        }
    }
}