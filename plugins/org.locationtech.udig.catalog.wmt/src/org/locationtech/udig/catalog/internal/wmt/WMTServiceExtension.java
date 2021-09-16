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

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.locationtech.udig.catalog.ServiceExtension;
import org.locationtech.udig.catalog.internal.wmt.wmtsource.WMTSource;

public class WMTServiceExtension implements ServiceExtension {
    
	public static final String KEY = "org.locationtech.udig.catalog.internal.wmt.url"; //$NON-NLS-1$
	
    /*
     * @see org.locationtech.udig.catalog.ServiceExtension#createService(java.net.URL, java.util.Map)
     */
    public WMTService createService(URL id, Map<String,Serializable> params) {
        if (params == null)
            return null;
        
        if( params.containsKey(KEY)){
            WMTPlugin.trace("[WMTService.createService] " + params.get(WMTServiceExtension.KEY), null); //$NON-NLS-1$
         
            return new WMTService(params);
        }
        
        return null;
    }
    
    /**
     * Creates a WMTService from params by extracting the service-url.
     *
     * @param params
     * @return
     */
    public WMTService createService(Map<String,Serializable> params) {
        if (params != null && params.containsKey(KEY)) {
            return createService((URL) params.get(WMTServiceExtension.KEY), params);
        }
        
        return null;
    }
    
    /**
     * Get service from WMTSource-Class
     * 
     * Usage:
     * IService = serviceExtension.createService(OSMMapnikSource.class);
     * 
     * @param sourceClass
     * @return
     */
    public WMTService createService(Class<? extends WMTSource> sourceClass) {
        URL url = WMTSource.getRelatedServiceUrl(sourceClass);        
        WMTService service = createService(null, createParams(url));
        
        return service;        
    }

    /*
     * @see org.locationtech.udig.catalog.ServiceExtension#createParams(java.net.URL)
     */
    public Map<String,Serializable> createParams(URL url) {
        //todo: check if the class exists?
        if (url != null && url.toExternalForm().startsWith(WMTService.ID)){
            Map<String,Serializable> map = new HashMap<String,Serializable>();
            map.put(KEY, url);
            
            return map;            
        }
        
        return null;
    } 
    
}
