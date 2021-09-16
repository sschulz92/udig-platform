/**
 * uDig - User Friendly Desktop Internet GIS client
 * http://udig.refractions.net
 * (C) 2004, Refractions Research Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 *
 */
package org.locationtech.udig.project.internal.commands;

import org.locationtech.udig.project.ILayer;
import org.locationtech.udig.project.command.AbstractCommand;
import org.locationtech.udig.project.command.UndoableMapCommand;
import org.locationtech.udig.project.internal.Layer;
import org.locationtech.udig.project.internal.Messages;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Add a layer to a map.
 * @author Jesse
 * @since 1.0.0
 */
public class AddLayerCommand extends AbstractCommand implements UndoableMapCommand {

    private Layer layer;
    private int index = -1;
    private ILayer selectedLayer;
    /**
     * Construct <code>AddLayerCommand</code>.
     *
     * @param layer the layer that will be added.
     */
    public AddLayerCommand( Layer layer ) {
        this( layer, -1 );
    }

    /**
     * Construct <code>AddLayerCommand</code>.
     *
     * @param layer the layer that will be added.
     * @param index the zorder that the layer will be added.
     */
    public AddLayerCommand( Layer layer, int index ) {
        this.layer = layer;
        this.index = index;
    }

    /**
     * Remove the layer that was added during execution.
     *
     * @see org.locationtech.udig.project.command.UndoableCommand#rollback()
     */
    public void rollback( IProgressMonitor monitor ) throws Exception {
        getMap().getLayersInternal().remove(layer);
        getMap().getEditManagerInternal().setSelectedLayer((Layer) selectedLayer);
    }

    /**
     * Adds a layer to the map. Defensive programming is recommended but command framework protects
     * against exceptions raised in commands.
     *
     * @see org.locationtech.udig.project.command.MapCommand#run()
     */
    public void run( IProgressMonitor monitor ) throws Exception {
        selectedLayer=getMap().getEditManager().getSelectedLayer();
        if (index < 0 || index > getMap().getLayersInternal().size()){
            getMap().getLayersInternal().add(layer);
        }
        else{
            getMap().getLayersInternal().add(index, layer);
        }
    }

    /**
     * Each command has a name that is displayed with the undo/redo buttons.
     *
     * @see org.locationtech.udig.project.command.MapCommand#getName()
     */
    public String getName() {
        return Messages.AddLayerCommand_Name + layer.getName();
    }

}
