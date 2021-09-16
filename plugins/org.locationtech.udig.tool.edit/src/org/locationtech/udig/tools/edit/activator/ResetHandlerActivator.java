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
package org.locationtech.udig.tools.edit.activator;

import org.locationtech.udig.tools.edit.Activator;
import org.locationtech.udig.tools.edit.EditState;
import org.locationtech.udig.tools.edit.EditToolHandler;

/**
 * Resets the state of the EditToolHandler.
 * 
 * @author Jesse
 * @since 1.1.0
 */
public class ResetHandlerActivator implements Activator {

    public void activate( EditToolHandler handler ) {
        handler.setCurrentShape(null);
        handler.setCurrentState(EditState.NONE);
    }

    public void deactivate( EditToolHandler handler ) {
    }

    public void handleActivateError( EditToolHandler handler, Throwable error ) {
    }

    public void handleDeactivateError( EditToolHandler handler, Throwable error ) {
    }

}
