package com.fr.design.widget.ui.designer.component;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.AbstractMarginWidget;
import com.fr.form.ui.PaddingMargin;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/8/3.
 */
public class PaddingBoundPane extends BasicPane{
    protected UISpinner top;
    protected UISpinner bottom;
    protected UISpinner left;
    protected UISpinner right;

    public PaddingBoundPane() {
        initBoundPane();
    }

    public void initBoundPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        top = new UISpinner(0, 1000, 1, 0);
        bottom = new UISpinner(0, 1000, 1, 0);
        left = new UISpinner(0, 1000, 1, 0);
        right = new UISpinner(0, 1000, 1, 0);
        top.setGlobalName(Inter.getLocText("FR-Designer_Layout-Padding"));
        bottom.setGlobalName(Inter.getLocText("FR-Designer_Layout-Padding"));
        left.setGlobalName(Inter.getLocText("FR-Designer_Layout-Padding"));
        right.setGlobalName(Inter.getLocText("FR-Designer_Layout-Padding"));
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p};
        double[] columnSize = {p, f, f};
        int[][] rowCount = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Layout-Padding")), top, bottom},
                new Component[]{null, new UILabel(Inter.getLocText("FR-Designer_Top"), SwingConstants.CENTER), new UILabel(Inter.getLocText("FR-Designer_Bottom"), SwingConstants.CENTER)},
                new Component[]{null, left, right},
                new Component[]{null, new UILabel(Inter.getLocText("FR-Designer_Left"), SwingConstants.CENTER), new UILabel(Inter.getLocText("FR-Designer_Right"), SwingConstants.CENTER)},
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_L2, IntervalConstants.INTERVAL_L1);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        this.add(panel);
    }


    public void update(AbstractMarginWidget marginWidget) {
        marginWidget.setMargin(new PaddingMargin((int)top.getValue(), (int)left.getValue(), (int)bottom.getValue(), (int)right.getValue() ));
    }

    protected String title4PopupWindow() {
        return "";
    }

    public void populate(AbstractMarginWidget marginWidget) {
        PaddingMargin paddingMargin = marginWidget.getMargin();
        top.setValue(paddingMargin.getTop());
        bottom.setValue(paddingMargin.getBottom());
        left.setValue(paddingMargin.getLeft());
        right.setValue(paddingMargin.getRight());
    }


}
