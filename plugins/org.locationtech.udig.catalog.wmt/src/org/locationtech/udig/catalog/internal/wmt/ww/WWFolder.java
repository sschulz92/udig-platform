/**
 * uDig - User Friendly Desktop Internet GIS client
 * http://udig.refractions.net
 * (C) 2010, Refractions Research Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 */
package org.locationtech.udig.catalog.internal.wmt.ww;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.locationtech.udig.catalog.CatalogPlugin;
import org.locationtech.udig.catalog.ID;
import org.locationtech.udig.catalog.IResolve;
import org.locationtech.udig.catalog.IResolveFolder;
import org.locationtech.udig.catalog.IResolveManager;
import org.locationtech.udig.catalog.IService;
import org.locationtech.udig.catalog.internal.wmt.WMTPlugin;
import org.locationtech.udig.catalog.internal.wmt.wmtsource.ww.LayerSet;
import org.locationtech.udig.catalog.internal.wmt.wmtsource.ww.QuadTileSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;

/** 
 * Based on WMSFolder this class represents a LayerSet in the catalog.
 * 
 * @see org.locationtech.udig.catalog.internal.wmt.wmtsource.ww.LayerSet
 * 
 * @author to.srwn
 * @since 1.1.0
 */
public class WWFolder implements IResolveFolder {

    private WWService service;
    private IResolve parent;
    private LayerSet layerSet;
    private List<IResolve> members;
    private URL identifier;

    public WWFolder(WWService service, IResolve parent, LayerSet layerSet) {
        this.service = service;
        this.layerSet = layerSet;
        
        // if parent is empty, use the service as parent
        if (parent == null) {
            this.parent = service;
        } else {
            this.parent = parent;
        }
        
        members = new LinkedList<IResolve>();
        
        // add QuadTileSets
        List<QuadTileSet> quadTileSets = layerSet.getQuadTileSets();
        for (QuadTileSet quadTileSet : quadTileSets) {
            members.add(new WWGeoResource(service, this, quadTileSet));
        }
        
        // add LayerSets
        List<LayerSet> layerSets = layerSet.getChildLayerSets();
        for (LayerSet childLayerSet : layerSets) {
            members.add(new WWFolder(service, this, childLayerSet));
        }

        try {
            identifier = new URL(service.getIdentifier().toString() + "#" + layerSet.getId()); //$NON-NLS-1$

        } catch (Throwable e) {
            WMTPlugin.log(null, e);
            identifier = service.getIdentifier();
        }
    }
    
    public URL getIdentifier() {
        return identifier;
    }
    public ID getID() {
        return new ID(getIdentifier());        
    }
    public Throwable getMessage() {
        return null;
    }

    public Status getStatus() {
        return Status.CONNECTED;
    }

    public List<IResolve> members( IProgressMonitor monitor ) throws IOException {
        return members;
    }

    public IResolve parent( IProgressMonitor monitor ) throws IOException {
        return parent;
    }

    /*
     * @see org.locationtech.udig.catalog.IResolve#canResolve(java.lang.Class)
     */
    public <T> boolean canResolve( Class<T> adaptee ) {
        if (adaptee == null) {
            return false;
        }

        if (adaptee.isAssignableFrom(WWFolder.class)
                || adaptee.isAssignableFrom(LayerSet.class)) {
            return true;
        }

        return CatalogPlugin.getDefault().getResolveManager().canResolve(this, adaptee);
    }
    
    /*
     * @see org.locationtech.udig.catalog.IGeoResource#resolve(java.lang.Class,
     *      org.eclipse.core.runtime.IProgressMonitor)
     */
    public <T> T resolve( Class<T> adaptee, IProgressMonitor monitor ) throws IOException {
        if (adaptee == null) {
            throw new NullPointerException();
        }

        if (adaptee.isAssignableFrom(WWFolder.class)) {
            return adaptee.cast(this);
        }

        if (adaptee.isAssignableFrom(LayerSet.class)) {
            return adaptee.cast(layerSet);
        }

        IResolveManager rm = CatalogPlugin.getDefault().getResolveManager();
        if (rm.canResolve(this, adaptee)) {
            return rm.resolve(this, adaptee, monitor);
        }
        return null; // no adapter found (check to see if ResolveAdapter is registered?)
    }

    public String getTitle() {
        return layerSet.getName();
    }

    public IService getService( IProgressMonitor monitor ) {
        return service;
    }

    public ImageDescriptor getIcon(IProgressMonitor monitor) {
        return null;
    }

    public void dispose( IProgressMonitor monitor ) {
    }

}
