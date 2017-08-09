package com.fr.design.widget.ui;

import javax.swing.*;

import com.fr.data.Dictionary;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.accessibles.AccessibleDictionaryEditor;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.ListEditor;
import com.fr.general.Inter;

import java.awt.*;

public class ListEditorDefinePane extends WriteUnableRepeatEditorPane<ListEditor> {
	private UICheckBox needHeadCheckBox;
	private AccessibleDictionaryEditor dictPane;

	public ListEditorDefinePane() {
		this.initComponents();
	}

	@Override
	protected void initComponents() {
		super.initComponents();
	}

	protected Component[] createDicPane(){
		dictPane = new AccessibleDictionaryEditor();
		return new Component[]{new UILabel(Inter.getLocText("FR-Designer_DS-Dictionary")), dictPane};
	}


	@Override
	protected JPanel setThirdContentPane() {
		JPanel contenter = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane_First0();
		JPanel centerPane = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane_First0();
		centerPane.add(needHeadCheckBox = new UICheckBox(Inter.getLocText("List-Need_Head")));
		needHeadCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		contenter.add(centerPane);
		return contenter;
	}
	
	@Override
	protected String title4PopupWindow() {
		return "List";
	}

	@Override
	protected void populateSubWriteUnableRepeatBean(ListEditor e) {
		needHeadCheckBox.setSelected(e.isNeedHead());
		this.dictPane.setValue(e.getDictionary());
	}

	@Override
	protected ListEditor updateSubWriteUnableRepeatBean() {
		ListEditor ob = new ListEditor();

		ob.setNeedHead(needHeadCheckBox.isSelected());
		ob.setDictionary((Dictionary) this.dictPane.getValue());

		return ob;
	}

    @Override
    public DataCreatorUI dataUI() {
        return null;
    }
}