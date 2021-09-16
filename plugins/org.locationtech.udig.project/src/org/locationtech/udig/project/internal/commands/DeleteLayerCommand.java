/**
 * uDig - User Friendly Desktop Internet GIS client
 * http://udig.refractions.net
 * (C) 2004-2012, Refractions Research Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 */
package org.locationtech.udig.project.internal.commands;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.locationtech.udig.project.ILayer;
import org.locationtech.udig.project.command.AbstractCommand;
import org.locationtech.udig.project.command.UndoableMapCommand;
import org.locationtech.udig.project.internal.EditManager;
import org.locationtech.udig.project.internal.Layer;
import org.locationtech.udig.project.internal.Map;
import org.locationtech.udig.project.internal.Messages;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Deletes a layer.
 * 
 * @author Jesse
 * @since 1.0.0
 */
public class DeleteLayerCommand extends AbstractCommand implements UndoableMapCommand {

    private Layer layer;

    private int index;

    private Map map;

    private boolean wasSelected;

    /**
     * Construct <code>DeleteLayerCommand</code>.
     * 
     * @param map
     */
    public DeleteLayerCommand( Layer layer ) {
        this.map = layer.getMapInternal();
        this.layer = layer;
    }

    /**
     * @see org.locationtech.udig.project.command.UndoableCommand#rollback()
     */
    public void rollback( IProgressMonitor monitor ) throws Exception {
        if( layer==null )
            return;
        map.getLayersInternal().add(index, layer);
        if( wasSelected )
            map.getEditManagerInternal().setSelectedLayer(layer);
    }

    /**
     * @see org.locationtech.udig.project.command.MapCommand#run()
     */
    public void run( IProgressMonitor monitor ) throws Exception {
        if( !map.getLayersInternal().contains(layer) ){
            layer=null;
            return ;
        }
        List<Layer> layers = map.getLayersInternal();
        index=layers.indexOf(layer);
        wasSelected=selectNewLayer( map, Collections.singleton(layer));
        layers.remove(layer);
        map.getColourScheme().removeItem(layer.getID().toString(), layer.getDefaultColor()); //remove from scheme
    }

    /**
     * Selects a new layer if the selected layer is being removed.  The algorithm will try to select the bottom layer above the
     * selected layer or if not possible then the top layer below (that isn't being removed).
     *
     * @param layers Layers in map
     * @param toRemove layers being removed
     * @param indexOfSelectedLayer
     * @return true if the selected layer has been changed
     */
    static boolean selectNewLayer( Map map, Collection<? extends ILayer> toRemove) {
        EditManager editManager = map.getEditManagerInternal();
        List<Layer> layers = map.getLayersInternal();
        Layer selectedLayer = editManager.getSelectedLayer();
        if( !toRemove.contains(selectedLayer ) )
            // nothing to be done since it is not going to be removed
            return false; 
        
        // try to select bottom layer above the selected layer that is not being deleted.  If
        // not possible then select top layer below the selected layer
        for( int i=layers.indexOf(selectedLayer)+1; i<layers.size() ; i++ ){
            Layer layer=layers.get(i);
            if( !toRemove.contains(layer) ){
                editManager.setSelectedLayer(layer);
                return true;
            }
        }
        for( int i=layers.indexOf(selectedLayer)-1; i>=0 ; i-- ){
            Layer layer=layers.get(i);
            if( !toRemove.contains(layer) ){
                editManager.setSelectedLayer(layer);
                return true;
            }
        }
        
        editManager.setSelectedLayer(null);
        return true;
    }

    /**
     * @see org.locationtech.udig.project.command.MapCommand#getName()
     */
    public String getName() {
        return MessageFormat.format(
                Messages.DeleteLayerCommand_deleteLayer, new Object[]{layer.getName()}); 
    }

}
