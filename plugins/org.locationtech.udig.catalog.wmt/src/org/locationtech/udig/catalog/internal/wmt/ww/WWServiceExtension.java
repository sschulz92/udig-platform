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

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.locationtech.udig.catalog.IService;
import org.locationtech.udig.catalog.ServiceExtension2;
import org.locationtech.udig.catalog.wmt.internal.Messages;

/**
 * A service extension for creating WWServices,
 * based on WMSServiceExtension
 * 
 * @author to.srwn
 * @since 1.1.0
 */
public class WWServiceExtension implements ServiceExtension2 {

    public IService createService(URL id, Map<String, Serializable> params) {
        if (params == null){
            return null;
        }

        if ((!params.containsKey(WWService.WW_URL_KEY) && id == null)
                && !params.containsKey(WWService.WW_LAYERSET_KEY)) {
            return null; // nope we don't have a WW_URL_KEY
        }

        URL extractedId = extractId(params);
        if (extractedId != null) {
            if (id != null){
                return new WWService(id, params);
            }
            else {
                return new WWService(extractedId, params);
            }
        }
        return null;
    }
    private URL extractId(Map<String, Serializable> params) {
        if (params.containsKey(WWService.WW_URL_KEY)) {
            URL base = null; // base url for service

            if (params.get(WWService.WW_URL_KEY) instanceof URL) {
                base = (URL) params.get(WWService.WW_URL_KEY); // use provided url for base
            } else {
                try {
                    base = new URL((String) params.get(WWService.WW_URL_KEY)); // upcoverting
                                                                                        // string to
                                                                                        // url for
                                                                                        // base
                } catch (MalformedURLException e1) {
                    // log this?
                    e1.printStackTrace();
                    return null;
                }
                params.remove(params.get(WWService.WW_URL_KEY));
                params.put(WWService.WW_URL_KEY, base);
            }
            // params now has a valid url

            return base;
        }
        return null;
    }

    public Map<String, Serializable> createParams(URL url) {
        if (!isWWConfigFile(url)) {
            return null;
        }
        
        // valid url 
        Map<String, Serializable> params2 = new HashMap<String, Serializable>();
        params2.put(WWService.WW_URL_KEY, url);

        return params2;
    }

    public static boolean isWWConfigFile(URL url) {
        return processURL(url) == null;
    }

    public String reasonForFailure( Map<String, Serializable> params ) {
        URL id = extractId(params);
        if (id == null)
            return Messages.WWServiceExtension_NeedsKey + WWService.WW_URL_KEY
                    + Messages.WWServiceExtension_NullValue;
        return reasonForFailure(id);
    }

    public String reasonForFailure(URL url) {
        return processURL(url);
    }

    private static String processURL(URL url) {
        if (url == null) {
            return Messages.WWServiceExtension_NullURL;
        }
        
        String PROTOCOL = url.getProtocol();
        String PATH = url.getPath();
        if (PROTOCOL==null || PROTOCOL.isEmpty()) {
            return Messages.WWServiceExtension_Protocol;
        }
        
        if (PATH == null || PATH.isEmpty() || !PATH.toLowerCase().contains(".xml")) { //$NON-NLS-1$
            return Messages.Wizard_WW_Error_InvalidURL;
        }

        return null; // try it anyway
    }
}
