package com.fr.plugin.chart.gantt.designer.style.tooltip;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.style.FormatPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrTooltipContent;
import com.fr.plugin.chart.base.format.AttrTooltipDurationFormat;
import com.fr.plugin.chart.base.format.AttrTooltipFormat;
import com.fr.plugin.chart.designer.PlotFactory;
import com.fr.plugin.chart.designer.component.VanChartTooltipContentPane;
import com.fr.plugin.chart.designer.component.format.SeriesNameFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.component.format.VanChartFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.gantt.attr.AttrGanttTooltipContent;
import com.fr.plugin.chart.base.format.IntervalTimeFormat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by hufan on 2017/1/13.
 */
public class VanChartGanttTooltipContentPane extends VanChartTooltipContentPane {
    private VanChartFormatPaneWithCheckBox processesFormatPane;
    private VanChartDateFormatPaneWithCheckBox startTimeFormatPane;
    private VanChartDateFormatPaneWithCheckBox endTimeFormatPane;
    private VanChartFormatComBoxWithCheckBox durationFormatPane;
    private VanChartFormatPaneWithCheckBox progressFormatPane;

    public VanChartGanttTooltipContentPane(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    protected void initFormatPane(VanChartStylePane parent, JPanel showOnPane){
        processesFormatPane = new VanChartFormatPaneWithCheckBox(parent, showOnPane){
            @Override
            protected String getCheckBoxText() {
                return Inter.getLocText("Plugin-ChartF_Project_Name");
            }
        };
        seriesNameFormatPane = new SeriesNameFormatPaneWithCheckBox(parent, showOnPane);
        startTimeFormatPane = new VanChartDateFormatPaneWithCheckBox(parent, showOnPane){
            @Override
            protected String getCheckBoxText() {
                return Inter.getLocText("Plugin-ChartF_Start_Time");
            }
        };
        endTimeFormatPane = new VanChartDateFormatPaneWithCheckBox(parent, showOnPane){
            @Override
            protected String getCheckBoxText() {
                return Inter.getLocText("Plugin-ChartF_End_Time");
            }
        };
        durationFormatPane = new VanChartFormatComBoxWithCheckBox();
        progressFormatPane = new VanChartFormatPaneWithCheckBox(parent, showOnPane){
            @Override
            protected String getCheckBoxText() {
                return Inter.getLocText("Plugin-ChartF_Process");
            }
        };
    }

    protected Component[][] getPaneComponents(){
        return new Component[][]{
                new Component[]{processesFormatPane,null},
                new Component[]{seriesNameFormatPane,null},
                new Component[]{startTimeFormatPane,null},
                new Component[]{endTimeFormatPane,null},
                new Component[]{durationFormatPane, null},
                new Component[]{progressFormatPane, null}
        };
    }

    protected double[] getRowSize(double p){
        return new double[]{p,p,p,p,p,p};
    }

    @Override
    protected void populateFormatPane(AttrTooltipContent attrTooltipContent) {
        if (attrTooltipContent instanceof AttrGanttTooltipContent){
            AttrGanttTooltipContent ganttTooltipContent = (AttrGanttTooltipContent) attrTooltipContent;
            processesFormatPane.populate(ganttTooltipContent.getProcessesFormat());
            seriesNameFormatPane.populate(ganttTooltipContent.getSeriesFormat());
            startTimeFormatPane.populate(ganttTooltipContent.getStartTimeFormat());
            endTimeFormatPane.populate(ganttTooltipContent.getEndTimeFormat());
            durationFormatPane.populate(ganttTooltipContent.getDurationFormat());
            progressFormatPane.populate(ganttTooltipContent.getProgressFormat());
        }
    }

    protected void updateFormatPane(AttrTooltipContent attrTooltipContent) {
        if (attrTooltipContent instanceof AttrGanttTooltipContent){
            AttrGanttTooltipContent ganttTooltipContent = (AttrGanttTooltipContent) attrTooltipContent;
            processesFormatPane.update(ganttTooltipContent.getProcessesFormat());
            seriesNameFormatPane.update(ganttTooltipContent.getSeriesFormat());
            startTimeFormatPane.update(ganttTooltipContent.getStartTimeFormat());
            endTimeFormatPane.update(ganttTooltipContent.getEndTimeFormat());
            durationFormatPane.update(ganttTooltipContent.getDurationFormat());
            progressFormatPane.update(ganttTooltipContent.getProgressFormat());
        }
    }

    public boolean isDirty() {
        return processesFormatPane.isDirty()
                || seriesNameFormatPane.isDirty()
                || startTimeFormatPane.isDirty()
                || endTimeFormatPane.isDirty()
                || durationFormatPane.isDirty()
                || progressFormatPane.isDirty();
    }

    public void setDirty(boolean isDirty) {
        processesFormatPane.setDirty(isDirty);
        seriesNameFormatPane.setDirty(isDirty);
        startTimeFormatPane.setDirty(isDirty);
        endTimeFormatPane.setDirty(isDirty);
        durationFormatPane.setDirty(isDirty);
        progressFormatPane.setDirty(isDirty);
    }

    protected AttrTooltipContent createAttrTooltip() {
        return new AttrGanttTooltipContent();
    }

    private abstract class VanChartDateFormatPaneWithCheckBox extends VanChartFormatPaneWithCheckBox {
        public VanChartDateFormatPaneWithCheckBox(VanChartStylePane parent, JPanel showOnPane) {
            super(parent, showOnPane);
        }

        @Override
        protected FormatPane createFormatPane(){
            return PlotFactory.createAutoFormatPane();
        }
    }

    private class VanChartFormatComBoxWithCheckBox extends JPanel{
        private UICheckBox isSelectedBox;
        private UIComboBox formatComBox;

        private boolean isDirty;

        private VanChartFormatComBoxWithCheckBox() {
            this.setLayout(new BorderLayout());
            isSelectedBox = new UICheckBox(Inter.getLocText("Plugin-ChartF_Duration_Time"));
            formatComBox = new UIComboBox(IntervalTimeFormat.getFormats());
            isSelectedBox.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    isDirty = true;
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            formatComBox.setPreferredSize(new Dimension(40,20));
            this.add(isSelectedBox, BorderLayout.CENTER);
            this.add(formatComBox, BorderLayout.EAST);
        }

        private boolean isDirty() {
            return isDirty;
        }

        private void setDirty(boolean isDirty) {
            this.isDirty = isDirty;
        }

        private void populate(AttrTooltipFormat tooltipFormat) {
            if(tooltipFormat instanceof AttrTooltipDurationFormat) {
                this.isSelectedBox.setSelected(tooltipFormat.isEnable());
                formatComBox.setSelectedItem(((AttrTooltipDurationFormat) tooltipFormat).getIntervalTimeFormat());
            }
        }

        private void update(AttrTooltipFormat tooltipFormat) {
            if(tooltipFormat instanceof AttrTooltipDurationFormat) {
                tooltipFormat.setEnable(isSelectedBox.isSelected());
                ((AttrTooltipDurationFormat) tooltipFormat).setIntervalTimeFormat((IntervalTimeFormat) formatComBox.getSelectedItem());
            }
        }
    }
}
