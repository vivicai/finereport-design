package com.fr.plugin.chart.designer.component.border;

import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.plugin.chart.VanChartAttrHelper;
import com.fr.plugin.chart.base.AttrBorderWithAlpha;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mitisky on 16/5/19.
 * 边框,线型/颜色/不透明度
 */
public class VanChartBorderWithAlphaPane extends VanChartBorderPane{
    private UINumberDragPane transparent;

    @Override
    protected void initComponents() {
        transparent = new UINumberDragPane(0,100);
        this.add(new JSeparator(), BorderLayout.SOUTH);

        super.initComponents();
    }

    @Override
    protected Component[][] getUseComponent() {
        return new Component[][]{
                new Component[]{null,null},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_LineStyle")),currentLineCombo},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Color_Color")),currentLineColorPane},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Alpha")), transparent}
        };
    }

    public void populate(AttrBorderWithAlpha attr) {
        if(attr == null){
            return;
        }
        super.populate(attr);
        transparent.populateBean(attr.getAlpha() * VanChartAttrHelper.PERCENT);
    }

    @Override
    public AttrBorderWithAlpha update() {
        AttrBorderWithAlpha attrBorderWithAlpha = new AttrBorderWithAlpha();
        super.update(attrBorderWithAlpha);
        attrBorderWithAlpha.setAlpha(transparent.updateBean()/ VanChartAttrHelper.PERCENT);
        return attrBorderWithAlpha;
    }
}
