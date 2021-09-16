/**
 * uDig - User Friendly Desktop Internet GIS client
 * http://udig.refractions.net
 * (C) 2004, Refractions Research Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 *
 */
package org.locationtech.udig.printing.ui.internal.template;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

/**
 * Implementation of an A0 size Template in portrait mode.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class A0PortraitTemplate extends AbstractPrinterPageTemplate {

    protected Rectangle getPaperSize() {
        Rectangle a0 = PageSize.A0;
        return a0;
    }

    public String getAbbreviation() {
        return "A0P";
    }

}
