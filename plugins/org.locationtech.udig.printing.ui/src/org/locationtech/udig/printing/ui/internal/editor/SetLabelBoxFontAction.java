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

import java.awt.Color;
import java.awt.Font;

import org.locationtech.udig.printing.model.impl.LabelBoxPrinter;
import org.locationtech.udig.printing.ui.IBoxEditAction;
import org.locationtech.udig.printing.ui.internal.editor.parts.BoxPart;
import org.locationtech.udig.ui.graphics.AWTSWTImageUtils;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;

/**
 * Changes the font of a LabelBoxPrinter
 * 
 * @author jesse
 * @since 1.1.0
 */
public class SetLabelBoxFontAction implements IBoxEditAction {

    private BoxPart owner;
    private FontDialog dialog;

    public Command getCommand() {
        return new Command(){
            private Font oldFont = getBoxPrinter().getFont();
            private Font newFont = AWTSWTImageUtils.swtFontToAwt(dialog.getFontList()[0]);
            private RGB oldFontRgb = new RGB(0, 0, 0);
            private RGB newFontRgb = dialog.getRGB();

            @Override
            public void execute() {
                getBoxPrinter().setFont(newFont);
                if (newFontRgb != null) {
                    getBoxPrinter().setFontColor(
                            new Color(newFontRgb.red, newFontRgb.green, newFontRgb.blue));
                }
                owner.refresh();
            }

            @Override
            public void undo() {
                getBoxPrinter().setFont(oldFont);
                getBoxPrinter().setFontColor(
                        new Color(oldFontRgb.red, oldFontRgb.green, oldFontRgb.blue));
            }
        };
    }

    protected LabelBoxPrinter getBoxPrinter() {
        return (LabelBoxPrinter) owner.getBoxPrinter();
    }

    public void init( BoxPart owner ) {
        this.owner = owner;
    }

    public boolean isDone() {
        return true;
    }

    public void perform() {
        dialog = new FontDialog(Display.getCurrent().getActiveShell());
        Font oldFont = getBoxPrinter().getFont();
        if (oldFont != null) {
            FontData fData = new FontData(oldFont.getName(), oldFont.getSize(), oldFont.getStyle());
            dialog.setFontList(new FontData[]{fData});
            Color fontColor = getBoxPrinter().getFontColor();
            if (fontColor != null) {
                RGB rbg = new RGB(fontColor.getRed(), fontColor.getGreen(), fontColor.getBlue());
                dialog.setRGB(rbg);
            }
        }
        dialog.open();
    }

}
