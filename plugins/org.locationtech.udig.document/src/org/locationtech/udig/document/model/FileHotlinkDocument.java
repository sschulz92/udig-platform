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
package org.locationtech.udig.document.model;

import java.io.File;
import java.util.List;

import org.locationtech.udig.catalog.document.IHotlinkSource.HotlinkDescriptor;

/**
 * Document model for hotlink file documents.
 * 
 * @author Naz Chan
 */
public class FileHotlinkDocument extends AbstractHotlinkDocument {

    protected File file;

    public FileHotlinkDocument(String info, List<HotlinkDescriptor> descriptors) {
        super(info, descriptors);
    }

    @Override
    public void setInfo(String info) {
        super.setInfo(info);
        file = AbstractDocument.createFile(info);
    }

    @Override
    public Object getContent() {
        return file;
    }
    
    @Override
    public String getContentName() {
        if (!isEmpty()) {
            return file.getName();
        }
        return null;
    }

    @Override
    public boolean open() {
        return AbstractDocument.openFile(file);
    }

    @Override
    public boolean isEmpty() {
        return (file == null);
    }

}
