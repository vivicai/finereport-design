package com.fr.plugin.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.designer.component.VanChartLineTypePane;

import javax.swing.*;

/**
 * 折线图，线型相关条件属性
 */
public class VanChartLineTypeConditionPane extends AbstractNormalMultiLineConditionPane{
    private static final long serialVersionUID = 1924676751313839477L;
    private VanChartLineTypePane lineTypePane;

    @Override
    protected String getItemLabelString() {
        return Inter.getLocText("Plugin-ChartF_LineStyle");
    }

    @Override
    protected JPanel initContentPane() {
        lineTypePane = createLinePane();
        return lineTypePane;
    }

    public VanChartLineTypeConditionPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
    }

    protected VanChartLineTypePane createLinePane() {
        return new VanChartLineTypePane();
    }

    /**
     * 条件属性item的名称
     * @return item的名称
     */
    public String nameForPopupMenuItem() {
        return Inter.getLocText("Plugin-ChartF_LineStyle");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void setDefault() {
        lineTypePane.populate(new VanChartAttrLine());
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof VanChartAttrLine) {
            lineTypePane.populate((VanChartAttrLine) condition);
        }
    }

    public DataSeriesCondition update() {
        return lineTypePane.update();
    }
}