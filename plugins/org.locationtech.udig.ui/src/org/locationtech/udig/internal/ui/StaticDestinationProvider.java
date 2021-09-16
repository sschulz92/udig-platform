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
package org.locationtech.udig.internal.ui;

import org.eclipse.swt.dnd.DropTargetEvent;


/**
 * Simple Destination Provider that returns the object passed as the constructor.
 * @author jones
 * @since 1.0.0
 */
public class StaticDestinationProvider implements IDropTargetProvider {
    private Object destination;
    public StaticDestinationProvider(Object destination) {
        this.destination=destination;
    }
    public Object getTarget(DropTargetEvent event) {
        return destination;
    }

}
