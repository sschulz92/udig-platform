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
package org.locationtech.udig.tools.edit;

import java.util.List;

import org.locationtech.udig.project.ILayer;
import org.locationtech.udig.project.ProjectBlackboardConstants;
import org.locationtech.udig.project.ui.tool.AbstractActionTool;
import org.locationtech.udig.project.ui.tool.IToolContext;

/**
 * Action that clears the selection of the map.
 * 
 * @author Jesse
 * @since 1.0.0
 */
public class ClearSelection extends AbstractActionTool {

    private EditToolHandler handler = null;

    public ClearSelection() {
    }

    public ClearSelection( EditToolHandler handler ) {

        assert handler != null : "handler can't be null.";
        this.handler = handler;
    }

    public void run() {

        IToolContext context = null;

        // if the constructor #ClearSelection(EditToolHandler) isn't called, get the context using
        // getContext, else get the handler context.

        context = (handler == null) ? getContext() : handler.getContext();

        assert context != null;
        List<ILayer> layers = context.getMap().getMapLayers();
        for( ILayer layer : layers ) {
            EditBlackboardUtil.getEditBlackboard(context, layer).clear();
            layer.getBlackboard().put(ProjectBlackboardConstants.LAYER__RENDERING_FILTER, null);
        }
        context.getMap().getBlackboard().put(EditToolHandler.CURRENT_SHAPE, null);
        context.getMap().getBlackboard().put(EditToolHandler.EDITSTATE, EditState.NONE);
        context.getMap().getBlackboard().put(ProjectBlackboardConstants.MAP__RENDERING_FILTER,
                null);
        context.sendASyncCommand(context.getEditFactory().createNullEditFeatureCommand());
        context.sendASyncCommand(context.getSelectionFactory().createNoSelectCommand());

        // this code should not be necessary as it should be caught by the events
        // IRenderManager manager = getContext().getMap().getRenderManager();
        // if(manager!=null){
        // manager.refresh(null);
        // }

    }

    public void dispose() {
    }

}
