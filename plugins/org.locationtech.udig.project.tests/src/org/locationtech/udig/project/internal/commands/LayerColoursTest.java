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
package org.locationtech.udig.project.internal.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;
import org.locationtech.udig.catalog.IGeoResource;
import org.locationtech.udig.catalog.tests.CatalogTests;
import org.locationtech.udig.project.IMap;
import org.locationtech.udig.project.internal.Layer;
import org.locationtech.udig.project.internal.Map;
import org.locationtech.udig.project.tests.support.AbstractProjectTestCase;
import org.locationtech.udig.project.tests.support.MapTests;
import org.locationtech.udig.ui.palette.ColourScheme;
import org.locationtech.udig.ui.tests.support.UDIGTestUtil;

public class LayerColoursTest extends AbstractProjectTestCase {

    // TODO: ensure removed colors are reused (they currently are not)
    // TODO: use more unique identifier than layer ID for scheme mapping key

    @Test
    public void testColourScheme() throws Exception {
        // add and remove some layers, and make sure the colours are proper
        Map map = MapTests.createDefaultMap("test", 2, true, new Dimension(10, 10)); //$NON-NLS-1$
        IGeoResource resource1 = CatalogTests
                .createGeoResource(UDIGTestUtil.createDefaultTestFeatures("type1", 4), false); //$NON-NLS-1$
        IGeoResource resource2 = CatalogTests
                .createGeoResource(UDIGTestUtil.createDefaultTestFeatures("type2", 4), false); //$NON-NLS-1$
        IGeoResource resource3 = CatalogTests
                .createGeoResource(UDIGTestUtil.createDefaultTestFeatures("type3", 4), false); //$NON-NLS-1$
        IGeoResource resource4 = CatalogTests
                .createGeoResource(UDIGTestUtil.createDefaultTestFeatures("type4", 4), false); //$NON-NLS-1$
        IGeoResource resource5 = CatalogTests
                .createGeoResource(UDIGTestUtil.createDefaultTestFeatures("type5", 4), false); //$NON-NLS-1$
        IGeoResource resource6 = CatalogTests
                .createGeoResource(UDIGTestUtil.createDefaultTestFeatures("type6", 4), false); //$NON-NLS-1$
        assertEquals(1, map.getMapLayers().size());

        assertEquals(8, map.getColourScheme().getColourPalette().getMaxColors());

        List<IGeoResource> resources = new ArrayList<>();
        resources.add(resource1);

        assertTrue(coloursAreUnique(map.getColourScheme()));

        addLayer(resource1, map);
        assertEquals(2, map.getMapLayers().size());
        assertTrue(coloursAreUnique(map.getColourScheme()));

        removeLayer(map, 0);
        assertEquals(1, map.getMapLayers().size());
        assertTrue(coloursAreUnique(map.getColourScheme()));

        addLayer(resource2, map);
        assertEquals(2, map.getMapLayers().size());
        assertTrue(coloursAreUnique(map.getColourScheme()));

        addLayer(resource3, map);
        assertEquals(3, map.getMapLayers().size());
        assertTrue(coloursAreUnique(map.getColourScheme()));

        addLayer(resource4, map);
        assertEquals(4, map.getMapLayers().size());
        assertTrue(coloursAreUnique(map.getColourScheme()));

        addLayer(resource5, map);
        assertEquals(5, map.getMapLayers().size());
        assertTrue(coloursAreUnique(map.getColourScheme()));

        removeLayer(map, 2);
        assertEquals(4, map.getMapLayers().size());
        assertTrue(coloursAreUnique(map.getColourScheme()));

        addLayer(resource6, map);
        assertEquals(5, map.getMapLayers().size());
        assertTrue(coloursAreUnique(map.getColourScheme()));

        addLayer(resource3, map);
        assertEquals(6, map.getMapLayers().size());
        assertTrue(coloursAreUnique(map.getColourScheme()));

    }

    private boolean coloursAreUnique(ColourScheme scheme) {
        int sizeScheme = scheme.getSizeScheme();
        for (int i = 0; i < sizeScheme; i++) {
            Color colour1 = scheme.getColour(i);
            for (int j = i + 1; j < sizeScheme; j++) {
                Color colour2 = scheme.getColour(j);
                if (colour1.equals(colour2)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void addLayer(IGeoResource resource, IMap map) throws Exception {
        AddLayersCommand addCommand = new AddLayersCommand(getResource(resource));
        addCommand.setMap(map);
        addCommand.run(new NullProgressMonitor());
    }

    private void removeLayer(IMap map, int index) throws Exception {
        DeleteLayerCommand deleteCommand = new DeleteLayerCommand(
                (Layer) map.getMapLayers().get(index));
        deleteCommand.setMap(map);
        deleteCommand.run(new NullProgressMonitor());
    }

    private List<IGeoResource> getResource(IGeoResource resource) {
        List<IGeoResource> resources = new ArrayList<>();
        resources.add(resource);
        return resources;
    }

}
