/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.chart.gui.other;

import com.fr.base.BaseFormula;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.design.mainframe.chart.gui.ChartOtherPane;
import com.fr.general.Inter;
import com.fr.js.JavaScriptImpl;
import com.fr.js.WebHyperlink;
import com.fr.plugin.chart.designer.component.ChartUIMenuNameableCreator;
import com.fr.third.org.hsqldb.lib.HashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date: 14-10-16
 * Time: 下午2:12
 */
public class ChartDesignerInteractivePane extends ChartInteractivePane {
    public ChartDesignerInteractivePane(ChartOtherPane parent) {
        super(parent);
    }

    protected List<ChartUIMenuNameableCreator> refreshList(HashMap map) {
        List<ChartUIMenuNameableCreator> list = new ArrayList<ChartUIMenuNameableCreator>();
        java.util.HashMap<String, BaseFormula> hyperLinkEditorMap = plot.getHyperLinkEditorMap();

        list.add(new ChartUIMenuNameableCreator(hyperLinkEditorMap, Inter.getLocText("Hyperlink-Web_link"),
                new WebHyperlink(), getUseMap(map, WebHyperlink.class)));
        list.add(new ChartUIMenuNameableCreator(hyperLinkEditorMap, "FR-Designer_JavaScript", new JavaScriptImpl(), getUseMap(map, JavaScriptImpl.class)));
        list.add(new ChartUIMenuNameableCreator(hyperLinkEditorMap, Inter.getLocText("FR-Engine_Interactive-chart"), null, null));

        return list;
    }

    protected void populateAutoRefresh(Chart chart) {
        super.populateAutoRefresh(chart);
        if (chart.getFilterDefinition() != null) {
            TopDefinition definition = (TopDefinition) chart.getFilterDefinition();
            isAutoRefresh.setEnabled(definition.isSupportAutoRefresh());
            if (!isAutoRefresh.isEnabled()) {
                isAutoRefresh.setSelected(false);
            }
            autoRefreshTime.setEnabled(definition.isSupportAutoRefresh());
        }
    }
}