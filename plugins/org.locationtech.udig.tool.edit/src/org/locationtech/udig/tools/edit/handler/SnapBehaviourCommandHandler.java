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
package org.locationtech.udig.tools.edit.handler;

import org.locationtech.udig.project.IMap;
import org.locationtech.udig.project.render.displayAdapter.IMapDisplay;
import org.locationtech.udig.project.ui.AnimationUpdater;
import org.locationtech.udig.tool.edit.internal.Messages;
import org.locationtech.udig.tools.edit.activator.GridActivator;
import org.locationtech.udig.tools.edit.animation.MessageBubble;
import org.locationtech.udig.tools.edit.preferences.PreferenceUtil;
import org.locationtech.udig.tools.edit.support.SnapBehaviour;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * Cycles through the different types of snap behaviour.  The preference is set and the new state is displayed
 * in a little pop-up.
 * 
 * @author Jesse
 * @since 1.1.0
 */
public class SnapBehaviourCommandHandler extends AbstractHandler implements IHandler{

    private final IMapDisplay mapDisplay;
    private MessageBubble messageBubble;
    private final IMap map;
    private final GridActivator activator = new GridActivator();

    public SnapBehaviourCommandHandler(IMapDisplay display, IMap map) {
        this.mapDisplay=display;
        this.map = map;
    }

    public Object execute( ExecutionEvent event ) throws ExecutionException {
        SnapBehaviour previousBehaviour = PreferenceUtil.instance().getSnapBehaviour();
        switch( previousBehaviour ) {
        case OFF:
            activator.hideGrid(map);
            previousBehaviour=SnapBehaviour.CURRENT_LAYER;
            break;
        case CURRENT_LAYER:
            activator.hideGrid(map);
            previousBehaviour=SnapBehaviour.ALL_LAYERS;
            break;
        case ALL_LAYERS:
            previousBehaviour=SnapBehaviour.GRID;
            activator.showGrid(map);
            break;
        case GRID:
            previousBehaviour=SnapBehaviour.OFF;
            activator.hideGrid(map);
            break;

        default:
            break;
        }
        PreferenceUtil.instance().setSnapBehaviour(previousBehaviour);
        displayNewStatus(previousBehaviour);
        return null;
    }

    private void displayNewStatus( SnapBehaviour snapBehaviour ) {
        Display display = Display.getCurrent();
        
        String message = null;
        switch( snapBehaviour ) {
        case OFF:            
            message=Messages.SnapBehaviourCommandHandler_off;
            break;
        case SELECTED:            
            message=Messages.SnapBehaviourCommandHandler_selected;
            break;
        case CURRENT_LAYER:            
            message=Messages.SnapBehaviourCommandHandler_current;
            break;
        case ALL_LAYERS:            
            message=Messages.SnapBehaviourCommandHandler_all;
            break;
        case GRID:            
            message=Messages.SnapBehaviourCommandHandler_grid;
            break;

        default:
            break;
        }
        if( message!=null ){
            if( messageBubble!=null && messageBubble.isValid() )
                messageBubble.setValid(false);
            Control control=(Control) mapDisplay;
            Point mouseLocation = control.toControl(display.getCursorLocation());
            messageBubble = new MessageBubble(mouseLocation.x, mouseLocation.y,
                                message, PreferenceUtil.instance().getMessageDisplayDelay());
            AnimationUpdater.runTimer(mapDisplay, messageBubble);
        }
    }

}
