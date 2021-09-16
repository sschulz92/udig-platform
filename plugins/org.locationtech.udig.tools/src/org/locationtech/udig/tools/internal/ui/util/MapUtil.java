/**
 * uDig - User Friendly Desktop Internet GIS client
 * http://udig.refractions.net
 * (C) 2012, Refractions Research Inc.
 * (C) 2006, Axios Engineering S.L. (Axios)
 * (C) 2006, County Council of Gipuzkoa, Department of Environment and Planning
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Axios BSD
 * License v1.0 (http://udig.refractions.net/files/asd3-v10.html).
 */
package org.locationtech.udig.tools.internal.ui.util;

import java.util.Collections;
import java.util.List;

import org.locationtech.udig.catalog.IGeoResource;
import org.locationtech.udig.project.ILayer;
import org.locationtech.udig.project.IMap;
import org.locationtech.udig.project.render.IViewportModel;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

import org.locationtech.udig.tools.internal.mediator.AppGISAdapter;

/**
 * Map Util
 * <p>
 * Utility functions for Map object
 * </p>
 * @author Mauricio Pazos (www.axios.es)
 * @author Aritz Davila (www.axios.es)
 * @since 1.1.0
 */
public final class MapUtil {
    

    private MapUtil(){
        // util class
    }
    /**
     * @param map
     * @return the Coordinate Reference System of map
     */
    public static CoordinateReferenceSystem getCRS( IMap map ) {
        assert map != null;
        
        IViewportModel viewportModel = map.getViewportModel();
        CoordinateReferenceSystem mapCrs = viewportModel.getCRS();
        return mapCrs;
    }
    
	/**
	 * Adds a new layer to map using the georesource of the feature store
	 * 
	 * @param map
	 * @param geoResource
	 * 
	 * @return the new layer
	 */
	public static ILayer addLayerToMap(IMap map, IGeoResource geoResource) {

		int index = map.getMapLayers().size();
		List<? extends ILayer> listLayer = AppGISAdapter.addLayersToMap(map, Collections.singletonList(geoResource),
					index);

		assert listLayer.size() == 1; // creates only one layer

		ILayer layer = listLayer.get(0);

		return layer;
	}
	/**
	 * Returns the selected layer of Map
	 * @param map
	 * @return the selected layer 
	 */
	public static ILayer getSelectedLayer(IMap map) {
		
		if(map == null) return null;
		
		ILayer layer = map.getEditManager().getSelectedLayer();
		return layer;
	}
}
