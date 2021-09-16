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

import org.locationtech.udig.catalog.IGeoResourceInfo;

import org.eclipse.core.runtime.IProgressMonitor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

class WWGeoResourceInfo extends IGeoResourceInfo {
    /** WMSResourceInfo resource field */
    private final WWGeoResource resource;
   
    WWGeoResourceInfo(WWGeoResource geoResourceImpl, IProgressMonitor monitor) throws IOException {
        this.resource = geoResourceImpl;
        
        this.title = this.resource.getTitle();        
        this.bounds = this.resource.getSource().getBounds();                  
    }
    
    @Override
    public CoordinateReferenceSystem getCRS() {
        return this.resource.getSource().getTileCrs();
    }
}
