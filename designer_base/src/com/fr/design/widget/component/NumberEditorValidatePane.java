package com.fr.design.widget.component;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.NumberEditor;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by kerry on 2017/9/10.
 */
public class NumberEditorValidatePane extends JPanel {
    private UICheckBox allowDecimalsCheckBox;
    private UICheckBox allowNegativeCheckBox;
    private UICheckBox setMaxValueCheckBox;
    private UICheckBox setMinValueCheckBox;
    private UISpinner maxValueSpinner;
    private UISpinner minValueSpinner;
    private UISpinner decimalLength;
    private JPanel limitNumberPane;
    private UITextField errorMsgTextField;
    private JPanel errorMsgTextFieldPane;

    private ActionListener allowDecimalsListener;

    private ActionListener allowNegativeListener;

    public ActionListener setMaxListener;

    private ActionListener setMinListener;

    private ChangeListener maxValueChangeListener;

    private ChangeListener minValueChangeListener;

    public NumberEditorValidatePane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        initComponent();
    }

    private void initComponent() {
        initListeners();
        this.allowDecimalsCheckBox = new UICheckBox(Inter.getLocText("Allow_Decimals"));
        allowDecimalsCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.allowDecimalsCheckBox.addActionListener(allowDecimalsListener);
        this.decimalLength = new UISpinner(0, Integer.MAX_VALUE, 1, 16);
        this.allowNegativeCheckBox = new UICheckBox(Inter.getLocText("Allow_Negative"));
        this.allowNegativeCheckBox.addActionListener(allowNegativeListener);
        allowNegativeCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.setMaxValueCheckBox = new UICheckBox(Inter.getLocText("Need_Max_Value"), false);
        setMaxValueCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.maxValueSpinner = new UISpinner(-Double.MAX_VALUE, Double.MAX_VALUE, 1D, 0D);
        this.setMaxValueCheckBox.addActionListener(setMaxListener);
        this.maxValueSpinner.addChangeListener(maxValueChangeListener);
        this.setMinValueCheckBox = new UICheckBox(Inter.getLocText("Need_Min_Value"), false);
        this.minValueSpinner = new UISpinner(-Double.MAX_VALUE, Double.MAX_VALUE, 1D, 0D);
        minValueSpinner.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.setMinValueCheckBox.addActionListener(setMinListener);
        this.minValueSpinner.addChangeListener(minValueChangeListener);
        setMinValueCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        initErrorMsgPane();
        JPanel errorMsgBorderPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        errorMsgBorderPane.setBorder(BorderFactory.createEmptyBorder(0, IntervalConstants.INTERVAL_L5, IntervalConstants.INTERVAL_L1, 0));
        errorMsgBorderPane.add(errorMsgTextFieldPane, BorderLayout.CENTER);
        UILabel numberLabel = new UILabel(Inter.getLocText(new String[]{"FR-Designer_Double", "Numbers"}));
        limitNumberPane = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{numberLabel, decimalLength}}, TableLayoutHelper.FILL_LASTCOLUMN, 18, 7);
        limitNumberPane.setBorder(BorderFactory.createEmptyBorder(0, IntervalConstants.INTERVAL_L5, 0, 0));
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{allowDecimalsCheckBox, null},
                new Component[]{limitNumberPane, null},
                new Component[]{allowNegativeCheckBox, null},
                new Component[]{setMaxValueCheckBox, maxValueSpinner},
                new Component[]{setMinValueCheckBox, minValueSpinner},
                new Component[]{errorMsgBorderPane, null},
        };
        double[] rowSize = {p, p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_L2, IntervalConstants.INTERVAL_L1);
        this.add(panel);
    }

    private void initErrorMsgPane() {
        errorMsgTextField = new UITextField();
        errorMsgTextFieldPane = TableLayoutHelper.createGapTableLayoutPane(
                new Component[][]{new Component[]{new UILabel(Inter.getLocText("FR-Designer_Widget_Error_Tip")), errorMsgTextField}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L2, IntervalConstants.INTERVAL_L1);
    }


    private void initListeners() {
        allowDecimalsListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (allowDecimalsCheckBox.isSelected()) {
                    limitNumberPane.setVisible(true);
                    limitNumberPane.setPreferredSize(new Dimension(215, 20));
                } else {
                    limitNumberPane.setVisible(false);
                    limitNumberPane.setPreferredSize(new Dimension(0, 0));
                }
            }
        };

        allowNegativeListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (allowNegativeCheckBox.isSelected()) {
                    minValueSpinner.getTextField().setMinValue(-Double.MAX_VALUE);
                    if (!setMinValueCheckBox.isSelected()) {
                        maxValueSpinner.getTextField().setMinValue(-Double.MAX_VALUE);
                    }
                } else {
                    minValueSpinner.getTextField().setMinValue(0.0);
                    if (!setMinValueCheckBox.isSelected()) {
                        maxValueSpinner.getTextField().setMinValue(0.0);
                    }
                    Double minValue = Double.parseDouble("" + minValueSpinner.getValue());
                    Double maxValue = Double.parseDouble("" + maxValueSpinner.getValue());
                    if (minValue < 0.0) {
                        minValueSpinner.setValue(0.0);
                    }
                    if (maxValue < 0.0) {
                        maxValueSpinner.setValue(0.0);
                    }
                }
            }
        };


        setMaxListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (setMaxValueCheckBox.isSelected()) {
                    maxValueSpinner.setEnabled(true);
                    Double value = new Double(0);
                    if (setMinValueCheckBox.isSelected()) {
                        Double minValue = Double.parseDouble("" + minValueSpinner.getValue());
                        if (minValue > value) {
                            value = minValue;
                        }
                    }
                    maxValueSpinner.setValue(value);
                } else {
                    maxValueSpinner.setEnabled(false);
                    minValueSpinner.getTextField().setMaxValue(Double.MAX_VALUE);
                }
            }
        };


        setMinListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (setMinValueCheckBox.isSelected()) {
                    minValueSpinner.setEnabled(true);
                    Double value = new Double(0);
                    if (setMaxValueCheckBox.isSelected()) {
                        Double maxValue = Double.parseDouble("" + maxValueSpinner.getValue());
                        if (maxValue < value) {
                            value = maxValue;
                        }
                    }
                    minValueSpinner.setValue(value);
                    maxValueSpinner.getTextField().setMinValue(value);
                } else {
                    minValueSpinner.setEnabled(false);
                    maxValueSpinner.getTextField().setMinValue(allowNegativeCheckBox.isSelected() ? (-Double.MAX_VALUE) : new Double(0));
                }
            }
        };

        maxValueChangeListener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (setMaxValueCheckBox.isSelected()) {
                    if (maxValueSpinner.getValue() >= minValueSpinner.getValue()) {
                        minValueSpinner.getTextField().setMaxValue(Double.parseDouble("" + maxValueSpinner.getValue()));
                    } else {
                        maxValueSpinner.setValue(minValueSpinner.getValue());
                    }
                }
            }
        };

        minValueChangeListener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (setMinValueCheckBox.isSelected()) {
                    if (minValueSpinner.getValue() <= maxValueSpinner.getValue()) {
                        maxValueSpinner.getTextField().setMinValue(Double.parseDouble("" + minValueSpinner.getValue()));
                    } else {
                        minValueSpinner.setValue(maxValueSpinner.getValue());
                    }
                }
            }
        };
    }

    public void populate(NumberEditor e) {
        allowDecimalsCheckBox.setSelected(e.isAllowDecimals());
        if (e.isAllowDecimals()) {
            this.decimalLength.setValue(e.getMaxDecimalLength());
        } else {
            this.limitNumberPane.setVisible(false);
        }

        allowNegativeCheckBox.setSelected(e.isAllowNegative());
        if (e.getMaxValue() == Double.MAX_VALUE) {
            setMaxValueCheckBox.setSelected(false);
            maxValueSpinner.setValue(new Double(Double.MAX_VALUE));
            maxValueSpinner.setEnabled(false);
        } else {
            setMaxValueCheckBox.setSelected(true);
            maxValueSpinner.setEnabled(true);
            maxValueSpinner.setValue(new Double(e.getMaxValue()));
        }

        if (e.getMinValue() == -Double.MAX_VALUE) {
            setMinValueCheckBox.setSelected(false);
            minValueSpinner.setValue(new Double(-Double.MAX_VALUE));
            minValueSpinner.setEnabled(false);

        } else {
            setMinValueCheckBox.setSelected(true);
            minValueSpinner.setEnabled(true);
            minValueSpinner.setValue(new Double(e.getMinValue()));
        }
        if(setMinValueCheckBox.isSelected() || setMaxValueCheckBox.isSelected()){
            errorMsgTextFieldPane.setVisible(true);
            errorMsgTextField.setText(e.getRegErrorMessage());
        }else{
            errorMsgTextFieldPane.setVisible(false);
            errorMsgTextField.setText(StringUtils.EMPTY);
        }
    }

    public void update(NumberEditor ob) {
        ob.setAllowDecimals(allowDecimalsCheckBox.isSelected());
        if (allowDecimalsCheckBox.isSelected()) {
            ob.setMaxDecimalLength((int) this.decimalLength.getValue());
        }

        ob.setAllowNegative(allowNegativeCheckBox.isSelected());
        if (setMaxValueCheckBox.isSelected()) {
            if (maxValueSpinner.getValue() < maxValueSpinner.getTextField().getMinValue()) {
                ob.setMaxValue(Double.parseDouble(StringUtils.EMPTY + maxValueSpinner.getTextField().getMinValue()));
                maxValueSpinner.getTextField().setValue(maxValueSpinner.getTextField().getMaxValue());
            } else {
                ob.setMaxValue(Double.parseDouble(StringUtils.EMPTY + maxValueSpinner.getValue()));
            }
        } else {
            ob.setMaxValue(Double.MAX_VALUE);
        }

        if (setMinValueCheckBox.isSelected()) {
            if (minValueSpinner.getValue() > minValueSpinner.getTextField().getMaxValue()) {
                ob.setMinValue(Double.parseDouble(StringUtils.EMPTY + minValueSpinner.getTextField().getMaxValue()));
                minValueSpinner.getTextField().setValue(minValueSpinner.getTextField().getMaxValue());
            } else {
                ob.setMinValue(Double.parseDouble(StringUtils.EMPTY + minValueSpinner.getValue()));
            }
        } else {
            ob.setMinValue(-Double.MAX_VALUE);
        }
        if(setMinValueCheckBox.isSelected() || setMaxValueCheckBox.isSelected()){
            errorMsgTextFieldPane.setVisible(true);
        }else{
            errorMsgTextFieldPane.setVisible(false);
            errorMsgTextField.setText(StringUtils.EMPTY);
        }
        ob.setRegErrorMessage(errorMsgTextField.getText());
    }
}
