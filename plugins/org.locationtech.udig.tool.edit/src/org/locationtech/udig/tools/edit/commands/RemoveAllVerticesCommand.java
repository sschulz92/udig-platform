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
package org.locationtech.udig.tools.edit.commands;

import org.locationtech.udig.project.command.AbstractCommand;
import org.locationtech.udig.project.command.UndoableMapCommand;
import org.locationtech.udig.tools.edit.EditToolHandler;
import org.locationtech.udig.tools.edit.support.EditBlackboard;
import org.locationtech.udig.tools.edit.support.EditGeom;
import org.locationtech.udig.tools.edit.support.Point;
import org.locationtech.udig.tools.edit.support.PrimitiveShape;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Removes all vertices from EditGeom's shell.
 * 
 * @author Jesse
 * @since 1.1.0
 */
public class RemoveAllVerticesCommand extends AbstractCommand implements UndoableMapCommand {

    private EditToolHandler handler;
    private EditGeom oldGeom;


    public RemoveAllVerticesCommand( EditToolHandler handler ) {
        this.handler=handler;
    }

    public void run( IProgressMonitor monitor ) throws Exception {
        EditGeom geom = handler.getCurrentGeom();

        oldGeom=new EditGeom(geom);
        for( Point point : geom.getShell() ) {
            geom.getEditBlackboard().removeCoords(point.getX(), point.getY(), geom.getShell() );
        }
    }

    public String getName() {
        return "RemoveAllVerticesCommand"; //$NON-NLS-1$
    }

    public void rollback( IProgressMonitor monitor ) throws Exception {
        EditBlackboard bb = oldGeom.getEditBlackboard();
        EditGeom geom = bb.newGeom(oldGeom.getFeatureIDRef().get(), oldGeom.getShapeType());
        for( Point p : oldGeom.getShell() ) {
            bb.addPoint(p.getX(), p.getY(), geom.getShell());
        }
        
        for( PrimitiveShape shape : oldGeom.getHoles() ) {
            PrimitiveShape hole = geom.newHole();
            for( Point p : shape ) {
                bb.addPoint(p.getX(), p.getY(), hole);
            }
        }

    }

}
