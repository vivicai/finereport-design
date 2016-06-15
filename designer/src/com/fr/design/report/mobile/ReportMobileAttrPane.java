package com.fr.design.report.mobile;

import com.fr.base.mobile.MobileFitAttrState;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.report.mobile.ElementCaseMobileAttr;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Administrator on 2016/5/12/0012.
 */
public class ReportMobileAttrPane extends BasicBeanPane<ElementCaseMobileAttr> {

    //横屏设置面板
    private MobileRadioGroupPane horizionPane;
    //竖屏设置面板
    private MobileRadioGroupPane verticalPane;
    //缩放设置面板
    private MobileRadioCheckPane radioCheckPane;

    public ReportMobileAttrPane() {
        initComponents();
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel borderPane = FRGUIPaneFactory.createTitledBorderPane(this.title4PopupWindow());

        JPanel fitOpsPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        horizionPane = new MobileRadioGroupPane(Inter.getLocText("FR-Designer_Mobile-Horizontal"));
        verticalPane = new MobileRadioGroupPane(Inter.getLocText("FR-Designer_Mobile-Vertical"));
        radioCheckPane = new MobileRadioCheckPane(Inter.getLocText("FR-Designer_Mobile-Zoom"));
        fitOpsPane.add(horizionPane, BorderLayout.NORTH);
        fitOpsPane.add(verticalPane, BorderLayout.CENTER);
        fitOpsPane.add(radioCheckPane, BorderLayout.SOUTH);
        borderPane.add(fitOpsPane);
        this.add(borderPane);
    }

    @Override
    public void populateBean(ElementCaseMobileAttr ob) {
        if (ob == null) {
            ob = new ElementCaseMobileAttr();
        }

        horizionPane.populateBean(ob.getHorziontalAttr());
        verticalPane.populateBean(ob.getVerticalAttr());
        radioCheckPane.populateBean(ob.isZoom());
    }

    @Override
    public ElementCaseMobileAttr updateBean() {
        MobileFitAttrState horizonState = horizionPane.updateBean();
        MobileFitAttrState verticalState = verticalPane.updateBean();
        boolean isZoom = radioCheckPane.updateBean();
        return new ElementCaseMobileAttr(horizonState, verticalState, isZoom);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_Fit-App");
    }
}
