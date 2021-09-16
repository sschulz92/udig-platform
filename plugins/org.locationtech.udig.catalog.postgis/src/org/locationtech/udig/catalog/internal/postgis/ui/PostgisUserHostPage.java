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
package org.locationtech.udig.catalog.internal.postgis.ui;

import org.locationtech.udig.catalog.service.database.UserHostPage;

/**
 * The first of a two page wizard for connecting to a postgis. This page requires the user enter
 * host, port, username and password.
 * 
 * @author jesse
 * @since 1.1.0
 */
public class PostgisUserHostPage extends UserHostPage {

    public PostgisUserHostPage( ) {
        super(new PostgisServiceDialect());
    }
}
