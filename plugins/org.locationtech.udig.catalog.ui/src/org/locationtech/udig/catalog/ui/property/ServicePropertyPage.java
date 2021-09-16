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
package org.locationtech.udig.catalog.ui.property;

import net.miginfocom.swt.MigLayout;
import org.locationtech.udig.catalog.IGeoResource;
import org.locationtech.udig.catalog.IGeoResourceInfo;
import org.locationtech.udig.catalog.IService;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * Property Page providing a summary of {@link IGeoResource} items.
 * <p>
 * As this is a generic property page we can do little more then provide a summary from the {@link IGeoResourceInfo}
 * and perhaps offer people a way to reset the cached title displayed in the catalog.
 * 
 * @author Naz Chan (LISAsoft)
 */
public class ServicePropertyPage extends PropertyPage implements IWorkbenchPropertyPage {

    private IService service;
    
    private static final String FEATURE_LABEL = "FEATURE_LABEL"; //$NON-NLS-1$
    
    public ServicePropertyPage() {
        // Nothing
    }

    @Override
    protected Control createContents(Composite parent) {
        service = (IService) getElement().getAdapter(IService.class);
        
        final Composite page = new Composite(parent, SWT.NONE);
        final String layoutConst = "fillx, insets 0"; //$NON-NLS-1$
        final String colConst = "[right]rel[grow,fill]"; //$NON-NLS-1$
        final String rowConst = ""; //$NON-NLS-1$
        page.setLayout(new MigLayout(layoutConst, colConst, rowConst));
        
        Label label = new Label(page, SWT.NONE);
        label.setText("ID:");
        
        Text text = new Text( page, SWT.SINGLE | SWT.BORDER );
        text.setText( service.getID().toString() );
        text.setEditable(false);
        text.setLayoutData("width 100:300:,wrap");
        
        label = new Label(page, SWT.NONE);
        label.setText("Title:");
        
        text = new Text( page, SWT.SINGLE | SWT.BORDER );
        
        String title = service.getTitle();
        text.setText( title != null ? title : "(automaticly generated)" );
        text.setEditable(false);
        
        return page;
        
    }
    
    @Override
    public boolean performOk() {
        // save any changes here
        return super.performOk();
    }
    
    @Override
    protected void performDefaults() {
        // undo any changes here
        super.performDefaults();
    }
    
}
