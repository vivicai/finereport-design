package com.fr.plugin.chart.map.designer.data.component.table;

import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.general.Inter;
import com.fr.plugin.chart.map.data.VanMapTableDefinitionProvider;
import com.fr.plugin.chart.map.designer.data.component.LongitudeLatitudeAndArea;
import com.fr.plugin.chart.map.designer.data.contentpane.table.VanPointMapPlotTableDataContentPane;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by hufan on 2016/12/21.
 */
public class LineMapAreaPane extends PointMapAreaPane {
    private static final int V_GAP = 10;
    protected AreaPane endAreaPane;
    public LineMapAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        super(parentPane);
    }

    protected JPanel createContentPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        initAreaPane(parentPane);
        initEndAreaPane(parentPane);

        JPanel content = new JPanel(new BorderLayout(0, V_GAP));
        content.add(areaPane, BorderLayout.NORTH);
        content.add(endAreaPane, BorderLayout.CENTER);
        return content;
    }

    protected void initAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        areaPane = new AreaPane(parentPane){
            protected Component[][] getComponent () {
                return new Component[][]{
                        new Component[]{new BoldFontTextLabel(Inter.getLocText(new String[]{"Plugin-ChartF_Start", "FR-Chart-Area_Name"})), areaNameCom}
                };
            }
        };
    }

    protected void initEndAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        endAreaPane = new AreaPane(parentPane){
            protected Component[][] getComponent () {
                return new Component[][]{
                        new Component[]{new BoldFontTextLabel(Inter.getLocText(new String[]{"Plugin-ChartF_End", "FR-Chart-Area_Name"})), areaNameCom}
                };
            }
        };
    }

    @Override
    public void refreshBoxListWithSelectTableData(List list) {
        super.refreshBoxListWithSelectTableData(list);
        endAreaPane.refreshBoxListWithSelectTableData(list);
    }

    @Override
    public boolean isSelectedItem() {
        return super.isSelectedItem()
                && endAreaPane.isSelectedItem();
    }

    @Override
    public void populate(VanMapTableDefinitionProvider mapTableDefinitionProvider) {
        super.populate(mapTableDefinitionProvider);

        LongitudeLatitudeAndArea endLongLatArea = new LongitudeLatitudeAndArea();
        endLongLatArea.setArea(mapTableDefinitionProvider.getEndAreaName());
        endLongLatArea.setLongitude(mapTableDefinitionProvider.getEndLongitude());
        endLongLatArea.setLatitude(mapTableDefinitionProvider.getEndLatitude());

        endAreaPane.populate(endLongLatArea);
    }

    @Override
    public void update(VanMapTableDefinitionProvider mapTableDefinitionProvider) {
        super.update(mapTableDefinitionProvider);

        LongitudeLatitudeAndArea endLongLatArea = endAreaPane.update();
        mapTableDefinitionProvider.setEndAreaName(endLongLatArea.getArea() == null ? null : endLongLatArea.getArea().toString());
        mapTableDefinitionProvider.setEndLongitude(endLongLatArea.getLongitude() == null ? null : endLongLatArea.getLongitude().toString());
        mapTableDefinitionProvider.setEndLatitude(endLongLatArea.getLatitude() == null ? null : endLongLatArea.getLatitude().toString());
    }

    @Override
    public void checkBoxUse(boolean hasUse) {
        super.checkBoxUse(hasUse);
        endAreaPane.checkBoxUse(hasUse);
    }

    @Override
    public void clearAllBoxList() {
        super.clearAllBoxList();
        endAreaPane.clearAllBoxList();
    }
}
