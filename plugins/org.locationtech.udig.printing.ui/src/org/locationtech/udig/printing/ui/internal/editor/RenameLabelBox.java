/**
 * uDig - User Friendly Desktop Internet GIS client
 * http://udig.refractions.net
 * (C) 2004, Refractions Research Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 */
package org.locationtech.udig.printing.ui.internal.editor;

import org.locationtech.udig.printing.model.impl.LabelBoxPrinter;
import org.locationtech.udig.printing.ui.IBoxEditAction;
import org.locationtech.udig.printing.ui.internal.editor.figures.BoxFigure;
import org.locationtech.udig.printing.ui.internal.editor.parts.BoxPart;
import org.locationtech.udig.printing.ui.internal.editor.parts.LabelCellEditorLocator;
import org.locationtech.udig.printing.ui.internal.editor.parts.LabelDirectEditManager;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Text;

/**
 * Changes the label in a LabelBox
 * 
 * @author Jesse
 * @since 1.1.0
 */
public class RenameLabelBox implements IBoxEditAction {

    private BoxPart owner;
    private String value;
    private boolean disposed = false;

    public void perform() {
        value = null;
        disposed = false;
        BoxFigure nodeFigure = (BoxFigure) owner.getFigure();
        LabelDirectEditManager manager = new LabelDirectEditManager(owner, TextCellEditor.class,
                new LabelCellEditorLocator(nodeFigure), nodeFigure){

            private boolean committing;

            @Override
            protected void initCellEditor() {
                super.initCellEditor();
                Text text = (Text) getCellEditor().getControl();
                text.setText(getText());
            }


            @Override
            protected void commit() {
                if (committing)
                    return;
                committing = true;
                try {
                    eraseFeedback();
                    value = (String) getCellEditor().getValue();
                } finally {
                    bringDown();
                    committing = false;
                }

            }
            @Override
            protected void bringDown() {
                super.bringDown();
                disposed = true;
            }
        };

        manager.show();
    }
    

    private String getText( ) {
        return getLabelBoxPrinter().getText();
    }

    public Command getCommand() {
        if (value == null)
            return null;
        return new Command(){

            private LabelBoxPrinter labelBox = getLabelBoxPrinter();
            private String oldValue = getLabelBoxPrinter().getText();
            private String newValue = value;

            @Override
            public boolean canExecute() {
                return isDone();
            }

            @Override
            public void execute() {
                labelBox.setText(newValue);
            }

            @Override
            public void redo() {
                labelBox.setText(newValue);
            }

            @Override
            public void undo() {
                labelBox.setText(oldValue);
            }
        };
    }
    
    private LabelBoxPrinter getLabelBoxPrinter(){
        return (LabelBoxPrinter) owner.getBoxPrinter();
    }

    public void init( BoxPart owner ) {
        this.owner = owner;
    }

    public boolean isDone() {
        return disposed;
    }

}
