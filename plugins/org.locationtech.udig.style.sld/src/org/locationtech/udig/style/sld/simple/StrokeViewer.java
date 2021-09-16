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
package org.locationtech.udig.style.sld.simple;

import java.awt.Color;
import java.text.MessageFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleBuilder;
import org.locationtech.udig.style.sld.AbstractSimpleConfigurator;
import org.locationtech.udig.style.sld.internal.Messages;
import org.locationtech.udig.ui.ColorEditor;
import org.locationtech.udig.ui.graphics.SLDs;

/**
 * Allows editing/viewing of a Style Layer Descriptor "Stroke".
 * <p>
 * Here is the pretty picture: <pre><code>
 *          +-+ +-------+ +------+ +------+
 *    Line: |x| | color | |size\/| |100%\/|
 *          +-+ +-------+ +------+ +------+
 * </code></pre>
 * </p>
 * <p>
 * Workflow:
 * <ol>
 * <li>createControl( parent ) - set up controls
 * <li>setStroke( stroke, mode ) - provide content from SimpleStyleConfigurator
 *    <ol>
 *    <li> Symbolizer values copied into fields based on mode
 *    <li> fields copied into controls
 *    <li> controls enabled based on mode & fields
 *    </ol>
 * <li>Listener.widgetSelected/modifyText - User performs an "edit"
 * <li>Listener.sync( SelectionEvent ) - update fields with values of controls
 * <li>fire( SelectionSevent ) - notify SimpleStyleConfigurator of change
 * <li>getStroke( StyleBuilder ) - construct a Stroke based on fields
 * </ul>
 * </p>  
 * @author Jody Garnett
 * @since 1.0.0
 */
public class StrokeViewer {
    boolean enabled = false;
    Color color = null;
    double width = Double.NaN;
    double opacity = Double.NaN;

    Button on;
    ColorEditor chooser;
    Combo size;
    Combo percent;

    private class Listener implements SelectionListener, ModifyListener {
        public void widgetSelected( SelectionEvent e ) {
            sync(e);
        };
        public void widgetDefaultSelected( SelectionEvent e ) {
            sync(e);
        };
        public void modifyText( final ModifyEvent e ) {
            sync(AbstractSimpleConfigurator.selectionEvent(e));
        };
        private void sync( SelectionEvent cause ) {
            try {
                StrokeViewer.this.enabled = StrokeViewer.this.on.getSelection();
                StrokeViewer.this.color = StrokeViewer.this.chooser.getColor();
                try {
                    StrokeViewer.this.width = Integer.parseInt(StrokeViewer.this.size.getText());
                } catch (NumberFormatException nan) {
                    // well lets just leave width alone
                }
                try {
                    String ptext = StrokeViewer.this.percent.getText();
                    if (ptext.endsWith("%")) { //$NON-NLS-1$
                        ptext = ptext.substring(0, ptext.length() - 1);
                        StrokeViewer.this.opacity = Double.parseDouble(ptext);
                        StrokeViewer.this.opacity /= 100.0;
                    } else {
                        StrokeViewer.this.opacity = Double.parseDouble(ptext);
                        if(StrokeViewer.this.opacity > 1) {
                            StrokeViewer.this.opacity /= 100.0;
                        }
                    }
                } catch (NumberFormatException nan) {
                    // well lets just leave opacity alone
                }
                fire(cause); // everything worked
            } catch (Throwable t) {
                // meh - should we of rolled back?
            } finally {
                StrokeViewer.this.chooser.setEnabled(StrokeViewer.this.enabled);
                StrokeViewer.this.size.setEnabled(StrokeViewer.this.enabled);
                StrokeViewer.this.percent.setEnabled(StrokeViewer.this.enabled);
            }
        }

    };
    Listener sync = new Listener();

    /** TODO: replace w/ support for more then one listener - when needed */
    SelectionListener listener = null;

    /**
     * TODO summary sentence for createControl ...
     * 
     * @param parent
     * @param klisten 
     * @return Generated composite
     */
    public Composite createControl(Composite parent, KeyListener klisten) {
        Composite part = AbstractSimpleConfigurator.subpart(parent, Messages.SimpleStyleConfigurator_line_label );

        this.on = new Button(part, SWT.CHECK);

        this.chooser = new ColorEditor(part, this.sync);

        this.size = new Combo(part, SWT.DROP_DOWN);
        this.size.setItems(new String[]{"1", "2", "3", "5", "10"}); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        this.size.setTextLimit(2);
        this.size.addKeyListener(klisten);
        this.size.setToolTipText(Messages.StrokeViewer_size_tooltip); 

        this.percent = new Combo(part, SWT.DROP_DOWN);
        this.percent.setItems(new String[]{"0%", "25%", "50%", "75%", "100%"}); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
        this.percent.setTextLimit(3);
        this.percent.addKeyListener(klisten);
        this.percent.setToolTipText(Messages.StrokeViewer_percent_tooltip); 
        return part;
    }

    /**
     * Accepts a listener that will be notified when content changes.
     * @param listener1 
     */
    public void addListener( SelectionListener listener1 ) {
        this.listener = listener1;
    }

    /**
     * Remove listener.
     * @param listener1 
     */
    public void removeListener( SelectionListener listener1 ) {
        if (this.listener == listener1)
            this.listener = null;
    }

    /**
     * TODO summary sentence for fire ...
     * 
     * @param event
     */
    protected void fire( SelectionEvent event ) {
        if (this.listener == null)
            return;
        this.listener.widgetSelected(event);
    }

    void listen( boolean listen ) {
        if (listen) {
            this.on.addSelectionListener(this.sync);
            this.chooser.addButtonSelectionListener(this.sync);
            this.size.addSelectionListener(this.sync);
            this.size.addModifyListener(this.sync);
            this.percent.addSelectionListener(this.sync);
            this.percent.addModifyListener(this.sync);
        } else {
            this.on.removeSelectionListener(this.sync);
            this.chooser.removeButtonSelectionListener(this.sync);
            this.size.removeSelectionListener(this.sync);
            this.size.removeModifyListener(this.sync);
            this.percent.removeSelectionListener(this.sync);
            this.percent.removeModifyListener(this.sync);
        }
    }

    /**
     * TODO summary sentence for setStroke ...
     * 
     * @param line
     * @param mode 
     * @param defaultColor 
     */
    public void setStroke( Stroke aLine, Mode mode, Color defaultColor ) {
        listen(false);
        try {
            boolean enabled=true;
            Stroke line=aLine;

            if ( line==null ){
                StyleBuilder builder=new StyleBuilder(); 
                line=builder.createStroke(defaultColor);
                enabled=false;
            }
            this.enabled =  enabled && (mode != Mode.NONE && line != null);
            this.color = SLDs.color(line);
            this.width = SLDs.width(line);
            this.opacity = SLDs.opacity(line);

            // Stroke is used in line, point and polygon
            this.on.setEnabled(mode != Mode.NONE);
            this.chooser.setColor(this.color);

            String text = MessageFormat.format("{0,number,#0}", this.width); //$NON-NLS-1$
            this.size.setText(text);
            this.size.select(this.size.indexOf(text));

            text = MessageFormat.format("{0,number,#0%}", this.opacity); //$NON-NLS-1$
            this.percent.setText(text);
            this.percent.select(this.percent.indexOf(text));

            this.on.setSelection(this.enabled);
            this.chooser.setEnabled(this.enabled);
            this.size.setEnabled(this.enabled);
            this.percent.setEnabled(this.enabled);
        } finally {
            listen(true); // listen to user now
        }
    }

    /**
     * Called to set up this "viewer" based on the provided symbolizer
     * 
     * @param sym public void set( LineSymbolizer sym, Mode mode ){ listen( false ); // don't sync
     *        when setting up try { this.enabled = mode != Mode.NONE || sym == null; this.color =
     *        SLDs.color( sym ); this.width = SLDs.width( sym ); this.opacity = SLDs.lineOpacity(
     *        sym ); this.on.setEnabled( mode == Mode.LINE ); // forced on for line
     *        this.chooser.setColor( this.color ); String text = MessageFormat.format(
     *        "{0,number,#0}", this.width ); //$NON-NLS-1$ this.size.setText( text );
     *        this.size.select( this.size.indexOf( text )); text = MessageFormat.format(
     *        "{0,number,#0%}", this.opacity );//(int)( opacity * 100.0 ) + "%"; //$NON-NLS-1$
     *        this.percent.setText( text ); this.percent.select( this.percent.indexOf( text ) );
     *        this.on.setSelection( this.enabled ); this.chooser.setEnabled( this.enabled );
     *        this.size.setEnabled( this.enabled ); this.percent.setEnabled( this.enabled ); }
     *        finally { listen( true ); // listen to user now } }
     */

    /**
     * TODO summary sentence for getStroke ...
     * @param build 
     * 
     * @return Stroke defined by this model
     */
    public Stroke getStroke( StyleBuilder build ) {
        if (!this.enabled)
            return null;
        if (this.opacity != Double.NaN) {
            return build.createStroke(this.color, this.width, this.opacity);
        }
        if (this.width != Double.NaN) {
            return build.createStroke(this.color, this.width);
        }
        return build.createStroke(this.color);
    }
}
