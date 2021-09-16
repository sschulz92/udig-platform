/**
 * uDig - User Friendly Desktop Internet GIS client
 * http://udig.refractions.net
 * (C) 2012, Refractions Research Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 */
package org.locationtech.udig.project.internal.commands;

import java.util.List;

import org.locationtech.udig.project.ILegendItem;
import org.locationtech.udig.project.command.AbstractCommand;
import org.locationtech.udig.project.command.UndoableMapCommand;
import org.locationtech.udig.project.internal.Folder;
import org.locationtech.udig.project.internal.Map;
import org.locationtech.udig.project.internal.Messages;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Deletes a Folder item from the map. More specifically, deletes a Folder item from the LegendItem
 * list of the map.
 * 
 * @author Naz Chan (LISAsoft)
 * @since 1.3.1
 */
public class DeleteFolderItemCommand extends AbstractCommand implements UndoableMapCommand {

    /**
     * Folder to be deleted
     */
    private Folder folder;

    private Object parent;

    /**
     * Index of the folder to be deleted in the LegendItem list
     */
    private int index;

    public DeleteFolderItemCommand(Folder folder) {
        this.folder = folder;
    }

    @Override
    public String getName() {
        return Messages.DeleteFolderItemCommand_Name;
    }

    @Override
    public void run(IProgressMonitor monitor) throws Exception {
        initializeDelete();
        if (parent != null) {
            
            if (parent instanceof Map) {
                getMap().getLegend().remove(folder);
            } else if (parent instanceof Folder) {
                final Folder parentFolder = (Folder) parent;
                parentFolder.getItems().remove(folder);
            }
        }
        // TODO - Save currently selected item
        // TODO - If currently selected item is folder, select next item
    }

    @Override
    public void rollback(IProgressMonitor monitor) throws Exception {
        getMap().getLegend().add(index, folder);
        // TODO - Restore currently selected item
    }

    /**
     * Checks if folder is existing in the map and initialises isInMap and index variables.
     */
    private void initializeDelete() {
        final Map map = getMap();
        final List<ILegendItem> items = map.getLegend();
        if (items.contains(folder)) {
            parent = getMap();
            index = items.indexOf(folder);
        } else {
            for (ILegendItem item : items) {
                if (item instanceof Folder) {
                    final boolean isSet = setParent((Folder) item); 
                    if (isSet) {
                        break;
                    }
                }
            }    
        }
    }

    private boolean setParent(Folder parentFolder) {
        final List<ILegendItem> items = parentFolder.getItems();
        if (items.contains(folder)) {
            parent = parentFolder;
            index = items.indexOf(folder);
            return true;
        } else {
            for (ILegendItem item : items) {
                if (item instanceof Folder) {
                    final boolean isSet = setParent((Folder) item); 
                    if (isSet) {
                        return true;
                    }
                }
            }    
        }
        return false;
    }
    
}
