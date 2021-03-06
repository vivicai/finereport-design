package com.fr.plugin.chart.custom.style;


import com.fr.chart.chartattr.Chart;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;

import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Fangjie on 2016/4/22.
 */
public class VanChartCustomLabelPane extends BasicScrollPane<Chart> {
    private VanChartCustomPlotLabelTabPane labelPane;
    private Chart chart;
    protected VanChartStylePane parent;
    private static final long serialVersionUID = -5449293740965811991L;

    public VanChartCustomLabelPane(VanChartStylePane parent) {
        super();
        this.parent = parent;
    }

    @Override
    protected JPanel createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        if(chart == null) {
            return contentPane;
        }
        initLabelPane((VanChartCustomPlot) chart.getPlot());

        contentPane.add(labelPane, BorderLayout.NORTH);
        return contentPane;
    }

    private void initLabelPane(VanChartCustomPlot plot) {
        labelPane = new VanChartCustomPlotLabelTabPane(plot, parent);
    }

    @Override
    public void populateBean(Chart chart) {
        this.chart = chart;

        if(labelPane == null){
            this.remove(leftcontentPane);
            layoutContentPane();
            parent.initAllListeners();
        }

        if(labelPane != null) {
            labelPane.populateBean((VanChartCustomPlot)chart.getPlot());
        }
    }


    @Override
    public void updateBean(Chart chart) {
        if(chart == null) {
            return;
        }

        labelPane.updateBean((VanChartCustomPlot)chart.getPlot());
    }

    @Override
    protected String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_LABEL_TITLE;
    }
}
