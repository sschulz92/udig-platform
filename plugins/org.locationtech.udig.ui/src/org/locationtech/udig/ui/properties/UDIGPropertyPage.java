/**
 * uDig - User Friendly Desktop Internet GIS client
 * http://udig.refractions.net
 * (C) 2012, Refractions Research Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 */
package org.locationtech.udig.ui.properties;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * Base class containing helpful utility methods when working on your own property page.
 * 
 * @author Jody Garnett (LISAsoft)
 * @since 1.3
 */
public abstract class UDIGPropertyPage extends PropertyPage {
    
    /**
     * Access the object that owns the properties shown in this page.
     * <p>
     * This method handles both instanceof and {@link IAdaptable#getAdapter(Class)} checks in one
     * smooth method call:
     * 
     * <pre>
     * IGeoResource resource = getElement( IGeoResource.class )
     * </pre>
     * <p>
     * It can be used in conjunction with the enablesFor (as a replacement for objectClass).
     * 
     * <pre><code>
     *          &lt;enabledWhen&gt;
     *             &lt;or&gt;
     *                &lt;instanceof
     *                      value="org.locationtech.udig.project.internal.Layer"&gt;
     *                &lt;/instanceof&gt;
     *                &lt;adapt
     *                      type="org.locationtech.udig.project.internal.Layer"&gt;
     *                &lt;/adapt&gt;
     *             &lt;/or&gt;
     *          &lt;/enabledWhen&gt;
     * </code></pre>
     * 
     * @return the object that owns the properties shown in this page
     */
    protected <T> T getElement( Class<T> adaptor ){
        IAdaptable element = getElement();
        if( element != null ){
            if( adaptor.isInstance( element )){
                return adaptor.cast(element);
            }
            return adaptor.cast( element.getAdapter(adaptor) );
        }
        else {
            return null; // not available
        }
    }

}
