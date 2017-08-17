package com.fr.plugin.chart.designer.type;

import com.fr.chart.base.AttrFillStyle;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Legend;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.chartglyph.DataSheet;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.MultilineLabel;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.general.Background;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.js.NameJavaScriptGroup;
import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.base.VanChartZoom;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.vanchart.VanChart;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractVanChartTypePane extends AbstractChartTypePane{
    private static final long serialVersionUID = 7743244512351499265L;
    private UICheckBox largeModelCheckBox;

    protected Boolean samePlot;

    //新图表暂时还没有平面3d，渐变高光等布局。
    @Override
    protected String[] getTypeLayoutPath() {
        return new String[0];
    }

    @Override
    protected String[] getTypeLayoutTipName() {
        return new String[0];
    }

    protected Component[][] getComponentsWithLargeData(JPanel typePane){
        largeModelCheckBox = new UICheckBox(Inter.getLocText("Plugin-ChartF_OpenLargeDataModel"));
        MultilineLabel prompt = new MultilineLabel(Inter.getLocText("Plugin-ChartF_LargeDataModelPrompt"));
        prompt.setForeground(Color.red);
        JPanel largeDataPane = new JPanel(new BorderLayout());
        largeDataPane.add(largeModelCheckBox, BorderLayout.CENTER);
        largeDataPane.add(prompt, BorderLayout.SOUTH);

        return new Component[][]{
                new Component[]{typePane},
                new Component[]{largeDataPane}
        };
    }

    //适用一种图表只有一种类型的
    public void populateBean(Chart chart) {
        typeDemo.get(0).isPressing = true;
        checkDemosBackground();
    }

    /**
     * 保存界面属性
     */
    public void updateBean(Chart chart) {
        checkTypeChange();
        VanChartPlot oldPlot = (VanChartPlot)chart.getPlot();
        VanChartPlot newPlot = (VanChartPlot)getSelectedClonedPlot();
        samePlot = accept(chart);
        if(typeChanged && samePlot){
            //同一中图表切换不同类型
            cloneOldPlot2New(oldPlot, newPlot);
            chart.setPlot(newPlot);
            resetChartAttr4SamePlot(chart);
        } else if(!samePlot){
            //不同的图表类型切換
            resetChartAttr(chart, newPlot);
            //切换图表时，数据配置不变,分类个数也不变
            newPlot.setCategoryNum(oldPlot.getCategoryNum());

        }
    }

    protected void resetChartAttr4SamePlot(Chart chart){
        resetRefreshMoreLabelAttr((VanChart) chart);
    }

    protected void resetChartAttr(Chart chart, Plot newPlot){
        chart.setPlot(newPlot);
        if(newPlot.isSupportZoomDirection() && !newPlot.isSupportZoomCategoryAxis()){
            ((VanChart)chart).setVanChartZoom(new VanChartZoom());
        }
        //重置工具栏选项
        ((VanChart)chart).setVanChartTools(createVanChartTools());
        //重置标题选项
        resetTitleAttr(chart);
        //重置监控刷新选项
        resetRefreshMoreLabelAttr((VanChart)chart);

    }

    //默认有标题
    protected void resetTitleAttr(Chart chart){
        VanChartPlot vanChartPlot = (VanChartPlot) chart.getPlot();
        chart.setTitle(vanChartPlot.getDefaultTitle());
    }


    //重置监控刷新面板
    protected void resetRefreshMoreLabelAttr(VanChart chart){
        chart.setRefreshMoreLabel(chart.getDefaultAutoAttrtooltip(chart));
    }

    protected VanChartTools createVanChartTools() {
        return new VanChartTools();
    }

    protected void checkTypeChange(){
        for(int i = 0; i < typeDemo.size(); i++){
            if(typeDemo.get(i).isPressing && i != lastTypeIndex){
                typeChanged = true;
                lastTypeIndex = i;
                break;
            }
            typeChanged = false;
        }
    }

    /**
     * 同一个图表， 类型之间切换
     */
    protected void cloneOldPlot2New(Plot oldPlot, Plot newPlot) {
        try {
            if (oldPlot.getLegend() != null) {
                newPlot.setLegend((Legend) oldPlot.getLegend().clone());
            }
            cloneOldConditionCollection(oldPlot, newPlot);

            cloneHotHyperLink(oldPlot, newPlot);

            if (oldPlot.getPlotFillStyle() != null) {
                newPlot.setPlotFillStyle((AttrFillStyle)oldPlot.getPlotFillStyle().clone());
            }
            newPlot.setPlotStyle(oldPlot.getPlotStyle());
            if (oldPlot.getDataSheet() != null) {
                newPlot.setDataSheet((DataSheet)oldPlot.getDataSheet().clone());
            }

            if (oldPlot.getBackground() != null) {
                newPlot.setBackground((Background)oldPlot.getBackground().clone());
            }
            if (oldPlot.getBorderColor() != null) {
                newPlot.setBorderColor(oldPlot.getBorderColor());
            }
            newPlot.setBorderStyle(oldPlot.getBorderStyle());
            newPlot.setRoundRadius(oldPlot.getRoundRadius());
            newPlot.setAlpha(oldPlot.getAlpha());
            newPlot.setShadow(oldPlot.isShadow());

            ((VanChartPlot)newPlot).setCategoryNum( ((VanChartPlot)oldPlot).getCategoryNum());

        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error in change plot");
        }
    }

    protected void cloneHotHyperLink(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException{
        if (oldPlot.getHotHyperLink() != null) {
            newPlot.setHotHyperLink((NameJavaScriptGroup)oldPlot.getHotHyperLink().clone());
        }
    }

    protected void cloneOldDefaultAttrConditionCollection(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException{
        if (oldPlot.getConditionCollection() != null) {
            ConditionCollection newCondition = new ConditionCollection();
            newCondition.setDefaultAttr((ConditionAttr) oldPlot.getConditionCollection().getDefaultAttr().clone());
            newPlot.setConditionCollection(newCondition);
        }
    }

    protected void cloneOldConditionCollection(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException{
        if (oldPlot.getConditionCollection() != null) {
            newPlot.setConditionCollection((ConditionCollection)oldPlot.getConditionCollection().clone());
        }
    }
}