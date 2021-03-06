package com.fr.plugin.chart.designer.other;

import com.fr.plugin.chart.attr.plot.VanChartPlot;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mengao on 2017/4/7.
 */
public class VanChartInteractivePaneWithMapZoom extends VanChartInteractivePaneWithOutSort {
    @Override
    protected JPanel createZoomPaneContent(JPanel zoomWidgetPane, JPanel zoomGesturePane, JPanel changeEnablePane, JPanel zoomTypePane, VanChartPlot plot) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.add(zoomWidgetPane, BorderLayout.NORTH);
        panel.add(zoomGesturePane, BorderLayout.CENTER);
        return panel;
    }
}
