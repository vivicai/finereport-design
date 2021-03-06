package com.fr.design.designer.properties.mobile;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.beans.IntrospectionException;
import java.util.ArrayList;

import com.fr.base.FRContext;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.*;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.gui.itable.PropertyGroup;
import com.fr.design.gui.xtable.ReportAppPropertyGroupModel;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.widget.editors.InChangeBooleanEditor;
import com.fr.general.Inter;

/**
 * 将body的控件列表中再加入手机重布局选项
 */
public class BodyAppRelayoutTable extends AbstractPropertyTable {

	private XCreator xCreator;
	private FormDesigner designer;

	public BodyAppRelayoutTable(XCreator xCreator) {
		this.xCreator = xCreator;
	}
	
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		CRPropertyDescriptor[] propertyTableEditor = {
				new CRPropertyDescriptor("appRelayout", this.xCreator.toData().getClass()).setEditorClass(InChangeBooleanEditor.class)
						.setI18NName(Inter.getLocText("FR-Designer-App_ReLayout"))
		};
		return propertyTableEditor;
	}

    /**
     * 初始化属性表组
     * @param source    控件
     */
	public void initPropertyGroups(Object source) {

		this.designer = WidgetPropertyPane.getInstance().getEditingFormDesigner();
        this.setFillsViewportHeight(false);

		groups = new ArrayList<PropertyGroup>();
		CRPropertyDescriptor[] propertyTableEditor = null;

		try {
			propertyTableEditor = supportedDescriptor();
		}catch (IntrospectionException e) {
			FRContext.getLogger().error(e.getMessage());
		}

		groups.add(new PropertyGroup(new ReportAppPropertyGroupModel(Inter.getLocText("FR-Designer_Properties_Mobile"), xCreator, propertyTableEditor, designer)));

		setModel(new BeanTableModel());
		this.repaint();
	}


	/**
	 * 单元格tooltip
	 * 属性名悬浮提示 
	 * 
	 * @param event 鼠标点击事件
	 * @return 单元格tooltip
	 */
	public String getToolTipText(MouseEvent event) {
		int row = BodyAppRelayoutTable.super.rowAtPoint(event.getPoint());
		int column = BodyAppRelayoutTable.super.columnAtPoint(event.getPoint());
		if(row != -1 && column == 0){
			return String.valueOf(this.getValueAt(row, column));
		}
		return null;
	}
	
    /**
     * 触发控件编辑事件
     */
    @Override
	public void firePropertyEdit() {
		designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_EDITED);
	}
}