package com.fr.plugin.chart.drillmap.designer.data.comp;

import com.fr.base.chart.BaseChart;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.drillmap.data.DrillMapDefinition;
import com.fr.plugin.chart.map.designer.data.MapDataPaneHelper;
import com.fr.plugin.chart.type.MapType;
import com.fr.plugin.chart.map.data.MapDataHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitisky on 16/6/20.
 * 钻取地图数据配置界面中 和钻取层级平级的数据界面
 */
public class DrillMapDataPane extends BasicBeanPane<ChartCollection> {
    private UIComboBoxPane<ChartCollection> dataDefinitionType;//数据定义方式:底层数据汇总/各层级分别指定

    private SingleLayerDataDefinitionPane bottomDataDefinitionPane;//底层数据汇总方式定义钻取地图数据
    private EachLayerDataDefinitionPane eachLayerDataDefinitionPane;//各层级分别指定

    private ChartDataPane parent;

    public DrillMapDataPane(final AttributeChangeListener listener,final ChartDataPane parent) {
        this.parent = parent;
        bottomDataDefinitionPane = new SingleLayerDataDefinitionPane(listener, parent);
        eachLayerDataDefinitionPane = new EachLayerDataDefinitionPane(listener, parent);

        dataDefinitionType = new UIComboBoxPane<ChartCollection>() {
            @Override
            protected List<FurtherBasicBeanPane<? extends ChartCollection>> initPaneList() {

                List<FurtherBasicBeanPane<? extends ChartCollection>> paneList = new ArrayList<FurtherBasicBeanPane<? extends ChartCollection>>();
                paneList.add(bottomDataDefinitionPane);
                paneList.add(eachLayerDataDefinitionPane);
                return paneList;
            }

            @Override
            protected String title4PopupWindow() {
                return null;
            }
        };

        this.setLayout(new BorderLayout());
        this.add(dataDefinitionType, BorderLayout.CENTER);
    }

    public List<MapType> getCurrentMapTypeList() {
        return eachLayerDataDefinitionPane.getCurrentMapTypeList();
    }

    /**
     * 设置是否关联单元格数据.
     *
     * @param supportCellData
     */
    public void setSupportCellData(boolean supportCellData) {
        bottomDataDefinitionPane.setSupportCellData(supportCellData);
        eachLayerDataDefinitionPane.setSupportCellData(supportCellData);
    }

    /**
     * Populate.
     *
     * @param ob
     */
    @Override
    public void populateBean(ChartCollection ob) {

        dataDefinitionType.setSelectedIndex(MapDataPaneHelper.isFromBottomData(ob) ? 0 : 1);

        ChartCollection bottomDataChartCollection = MapDataPaneHelper.getBottomDataDrillMapChartCollection(ob);
        bottomDataDefinitionPane.populateBean(bottomDataChartCollection);
        eachLayerDataDefinitionPane.populateBean(ob);

        parent.initAllListeners();
    }

    /**
     * Update.
     */
    @Override
    public ChartCollection updateBean() {
        return null;
    }

    @Override
    public void updateBean(ChartCollection ob) {
        DrillMapDefinition drillMapDefinition = MapDataPaneHelper.getDrillMapDefinition(ob);
        if(drillMapDefinition == null){
            drillMapDefinition = new DrillMapDefinition();
            ob.getSelectedChart().setFilterDefinition(drillMapDefinition);
        }
        if(dataDefinitionType.getSelectedIndex() == 0){
            drillMapDefinition.setFromBottomData(true);
            ChartCollection temp = new ChartCollection(new Chart()){
                @Override
                public void addFunctionRecord(BaseChart chart) {
                }
            };
            bottomDataDefinitionPane.updateBean(temp);
            drillMapDefinition.setBottomDataDefinition(temp.getSelectedChart().getFilterDefinition());
        } else {
            drillMapDefinition.setFromBottomData(false);
            eachLayerDataDefinitionPane.updateBean(ob);
        }
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Chart-Use_Data");
    }
}
