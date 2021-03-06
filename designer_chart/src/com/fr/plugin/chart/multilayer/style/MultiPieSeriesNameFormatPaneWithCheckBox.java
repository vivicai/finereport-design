package com.fr.plugin.chart.multilayer.style;

import com.fr.general.Inter;
import com.fr.plugin.chart.designer.component.format.SeriesNameFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;

/**
 * Created by Fangjie on 2016/6/15.
 */
public class MultiPieSeriesNameFormatPaneWithCheckBox extends SeriesNameFormatPaneWithCheckBox {
    private static final long serialVersionUID = 6456517419221601327L;

    public MultiPieSeriesNameFormatPaneWithCheckBox(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected String getCheckBoxText() {
        return Inter.getLocText("Plugin-ChartF_MultiPie_Series_Name");
    }
}
