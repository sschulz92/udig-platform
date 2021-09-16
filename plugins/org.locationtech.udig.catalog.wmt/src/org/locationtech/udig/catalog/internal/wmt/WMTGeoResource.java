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
package org.locationtech.udig.catalog.internal.wmt;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.locationtech.udig.catalog.IGeoResource;
import org.locationtech.udig.catalog.IGeoResourceInfo;
import org.locationtech.udig.catalog.internal.wmt.wmtsource.WMTSource;
import org.locationtech.udig.catalog.internal.wmt.wmtsource.WMTSourceFactory;
import org.locationtech.udig.core.internal.CorePlugin;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;


public class WMTGeoResource extends IGeoResource {
    
    public static final String DEFAULT_ID = "blank"; //$NON-NLS-1$

    private WMTService wmtService;
    private String resourceId;
    
    private WMTSource source;
    
    private Throwable msg;
    
    public WMTGeoResource(WMTService service, String resourceId) {
        this.service = service;
        this.wmtService = service;
        
        if (resourceId.equals(WMTGeoResource.DEFAULT_ID)) {
            // is this a OSMCloudMadeSource or CSSource?
            String sourceInitData = getSourceInitDataFromUrl(wmtService.getIdentifier());
            
            // if yes, let's use the init-data as resource-id
            if(sourceInitData != null) {
                this.resourceId = sourceInitData;
            } else {
                this.resourceId = resourceId; 
            }
        } else {
            this.resourceId = resourceId;            
        }
        
        this.source = null;
    }
    
    /**
     * Gets the init data from an url:
     * 
     * wmt://localhost/wmt/org.locationtech.udig.catalog.internal.wmt.wmtsource.OSMCloudMadeSource/3
     *  -->
     *  3 
     *
     *  or
     *  
     *  wmt://localhost/wmt/org.locationtech.udig.catalog.internal.wmt.wmtsource.CSSource/tile.openstreetmap.org/{z}/{x}/{y}.png/2/18
     *  -->
     *  tile.openstreetmap.org/{z}/{x}/{y}.png/2/18
     *  
     * @param url
     * @return
     */
    private String getSourceInitDataFromUrl(URL url) {
        String className = WMTSourceFactory.getClassFromUrl(url);
        String styleId = url.toString().replace(WMTService.ID, "").replace(className, "");  //$NON-NLS-1$ //$NON-NLS-2$ 
        
        if (!styleId.isEmpty()) {
            return styleId.replaceFirst("/", "");//$NON-NLS-1$ //$NON-NLS-2$
        } else {
            return null;
        }
    }

    /**
     * Returns the WMTSource object connected to this GeoResource
     *
     * @return
     */
    public WMTSource getSource(){
        if (source == null) {
            synchronized (this) {
                if (source == null) {
                    try {                        
                        source = WMTSourceFactory.createSource(wmtService, 
                                wmtService.getIdentifier(), resourceId);       
                    } catch(Throwable t) {
                        WMTPlugin.log("[WMTSource] Creating source failed: wmtService.getIdentifier() + " + //$NON-NLS-1$
                        		"#" + resourceId , t); //$NON-NLS-1$
                        
                        source = null;
                        msg = t;
                    }
                }
            }
        }
        
        return source;
    }
    
    public String getTitle() {
        return getSource().getName();   
    }
    
    /*
     * @see org.locationtech.udig.catalog.IResolve#canResolve(java.lang.Class)
     */
    public <T> boolean canResolve( Class<T> adaptee ) {
        return adaptee != null
                && (adaptee.isAssignableFrom(WMTSource.class)
                        || super.canResolve(adaptee));
    }

    /*
     * @see org.locationtech.udig.catalog.IGeoResource#resolve(java.lang.Class,
     *      org.eclipse.core.runtime.IProgressMonitor)
     */
    public <T> T resolve( Class<T> adaptee, IProgressMonitor monitor ) throws IOException {
        if (monitor == null)
            monitor = new NullProgressMonitor();

        if (adaptee == null) {
            throw new NullPointerException("No adaptor specified" ); //$NON-NLS-1$
        }        
        if (adaptee.isAssignableFrom(WMTSource.class)) {
            return adaptee.cast(getSource());
        }

        return super.resolve(adaptee, monitor);
    }


    /*
     * @see org.locationtech.udig.catalog.IGeoResourceInfo#createInfo(java.lang.Class,
     *      org.eclipse.core.runtime.IProgressMonitor
     */
    protected IGeoResourceInfo createInfo( IProgressMonitor monitor ) throws IOException {
        if (info == null){
            synchronized (this) {
                if (info == null){
                    info = new WMTGeoResourceInfo(this, monitor);
                }
            }
        }
        return info;
    }


    /*
     * @see org.locationtech.udig.catalog.IResolve#getStatus()
     */
    public Status getStatus() {
        if (msg != null) {
            return Status.BROKEN;
        } else if (source == null){
            return Status.NOTCONNECTED;
        } else {
            return Status.CONNECTED;
        }
    }

    /*
     * @see org.locationtech.udig.catalog.IResolve#getMessage()
     */
    public Throwable getMessage() {
        return msg;
    }

    /*
     * @see org.locationtech.udig.catalog.IResolve#getIdentifier()
     */
    public URL getIdentifier() {
        try {
            return new URL(null,
                    service.getIdentifier().toString() + "#" + resourceId, CorePlugin.RELAXED_HANDLER); //$NON-NLS-1$
        } catch (MalformedURLException e) {
            WMTPlugin.log("[WMTGeoResource.getIdentifier] Construction the id failed: " + resourceId, e); //$NON-NLS-1$
        }

        return service.getIdentifier();
    }

}
