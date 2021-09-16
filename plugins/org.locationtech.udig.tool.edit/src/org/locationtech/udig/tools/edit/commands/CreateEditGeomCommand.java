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

import java.util.Collections;

import org.locationtech.udig.core.IBlockingProvider;
import org.locationtech.udig.project.command.AbstractCommand;
import org.locationtech.udig.project.command.UndoableMapCommand;
import org.locationtech.udig.tool.edit.internal.Messages;
import org.locationtech.udig.tools.edit.support.EditBlackboard;
import org.locationtech.udig.tools.edit.support.EditGeom;
import org.locationtech.udig.tools.edit.support.EditUtils;
import org.locationtech.udig.tools.edit.support.PrimitiveShape;
import org.locationtech.udig.tools.edit.support.ShapeType;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Calls newGeom on the EditBlackboard.
 * 
 * @author jones
 * @since 1.1.0
 */
public class CreateEditGeomCommand extends AbstractCommand implements UndoableMapCommand,
    IBlockingProvider<EditGeom>{

    private String fid;
    private EditBlackboard blackboard;
    private EditGeom geom;
    private ShapeType shapeType;

    /**
     * New Instance
     * 
     * @param blackboard the blackboard to creat the new geom on.
     * @param fid the string to use as the feature id of the new Geom
     */
    public CreateEditGeomCommand( EditBlackboard blackboard, String fid ) {
        this(blackboard, fid, ShapeType.UNKNOWN);
    }

    /**
     * New Instance
     * 
     * @param blackboard the blackboard to creat the new geom on.
     * @param fid the string to use as the feature id of the new Geom
     * @param shapeType the type of shape to create.
     */
    public CreateEditGeomCommand( EditBlackboard blackboard, String fid, ShapeType shapeType ) {
        this.blackboard=blackboard;
        this.fid=fid;
        this.shapeType=shapeType;
        }

    public void run( IProgressMonitor monitor ) throws Exception {
        this.geom=blackboard.newGeom(fid, shapeType);
    }

    public String getName() {
        return Messages.CreateEditGeomCommand_name;
    }

    public void rollback( IProgressMonitor monitor ) throws Exception {
        blackboard.removeGeometries(Collections.singleton(geom));
    }

    public EditGeom get(IProgressMonitor monitor, Object... params) {
        return geom;
    }

    public IBlockingProvider<EditGeom> getEditGeomProvider() {
        return new EditUtils.StaticEditGeomProvider(geom);
    }
    public IBlockingProvider<PrimitiveShape> getShapeProvider() {
        return new ShapeProvider();
    }
    
    class ShapeProvider implements IBlockingProvider<PrimitiveShape>{

        public PrimitiveShape get(IProgressMonitor monitor, Object... params) {
            return geom.getShell();
        }
        
    }

}
