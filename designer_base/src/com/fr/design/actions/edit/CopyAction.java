/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit;

import com.fr.base.BaseUtils;
import com.fr.design.actions.TemplateComponentAction;
import com.fr.design.designer.TargetComponent;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

/**
 * Copy.
 */
public class CopyAction extends TemplateComponentAction {
    public CopyAction(TargetComponent t) {
        super(t);

        this.setName(Inter.getLocText("M_Edit-Copy"));
        this.setMnemonic('C');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/copy.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, DEFAULT_MODIFIER));
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        TargetComponent tc = getEditingComponent();
        if (tc != null) {
            tc.copy();
        }
        return false;
    }
}