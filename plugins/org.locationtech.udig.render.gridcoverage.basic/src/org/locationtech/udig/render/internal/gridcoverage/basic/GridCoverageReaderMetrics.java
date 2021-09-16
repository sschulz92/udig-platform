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
package org.locationtech.udig.render.internal.gridcoverage.basic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.locationtech.udig.catalog.ID;
import org.locationtech.udig.core.MinMaxScaleCalculator;
import org.locationtech.udig.project.ILayer;
import org.locationtech.udig.project.internal.ProjectPlugin;
import org.locationtech.udig.project.internal.render.Renderer;
import org.locationtech.udig.project.preferences.PreferenceConstants;
import org.locationtech.udig.project.render.AbstractRenderMetrics;
import org.locationtech.udig.project.render.IRenderContext;
import org.locationtech.udig.project.render.IRenderMetricsFactory;
import org.locationtech.udig.style.sld.SLDContent;
import org.locationtech.udig.ui.graphics.SLDs;

import org.eclipse.jface.preference.IPreferenceStore;
import org.geotools.styling.Style;
import org.geotools.util.Range;

/**
 * Creates a Metrics object for the basic gridcoverage renderer
 *
 * @author jeichar
 * @since 0.3
 */
public class GridCoverageReaderMetrics extends AbstractRenderMetrics {

    /*
     * list of styles the renderer is expecting to find and use
     */
    private static List<String> listExpectedStyleIds(){
        ArrayList<String> styleIds = new ArrayList<String>();
        styleIds.add("org.locationtech.udig.style.sld");
        styleIds.add("org.locationtech.udig.style.cache");
        return styleIds;
    }

    /**
     * Construct <code>BasicGridCoverageMetrics</code>.
     *
     * @param context2
     * @param factory
     */
    public GridCoverageReaderMetrics( IRenderContext context2, GridCoverageReaderMetricsFactory factory) {
        super( context2, factory, listExpectedStyleIds());
        this.resolutionMetric = RES_PIXEL; // reads just what is needed for the screen

        // DATA SOURCE PERFORMANCE INDICATORS
        ID id = context.getGeoResource().getID();
        // Boolean memory = (Boolean) context.getLayer().getStyleBlackboard().get("org.locationtech.udig.style.cache");

        if( id.isMemory() ){
            // in memory grid coverage (example a temporary image froma WPS)
            this.latencyMetric = LATENCY_MEMORY;
            this.timeToDrawMetric = DRAW_IMAGE_MEMORY;
        }
        else if (id.isFile() ){
            String filename = id.toBaseFile();
            if( filename.toLowerCase().endsWith("jpg") ||
                    filename.toLowerCase().endsWith("jpeg")){
                this.latencyMetric = LATENCY_LOCAL;
                this.timeToDrawMetric = DRAW_IMAGE_COMPRESSED;
            }
            else {
                this.latencyMetric = LATENCY_MEMORY_CACHE; // use of JAI tile cache
                this.timeToDrawMetric = DRAW_IMAGE_INDEX;
            }
        }
        else {
            this.latencyMetric = LATENCY_NETWORK;
            this.timeToDrawMetric = DRAW_IMAGE_RAW;
        }
    }

    public Renderer createRenderer() {
        Renderer r=new GridCoverageReaderRenderer();
        r.setContext(context);
        return r;
    }

    /**
     * @see org.locationtech.udig.project.render.RenderMetrics#getRenderContext()
     */
    public IRenderContext getRenderContext() {
        return context;
    }

    /**
     * @see org.locationtech.udig.project.render.IRenderMetrics#getRenderMetricsFactory()
     */
    public IRenderMetricsFactory getRenderMetricsFactory() {
        return factory;
    }

    public boolean canAddLayer( ILayer layer ) {
        return false;
    }

    public boolean canStyle( String styleID, Object value ) {
        if( "org.locationtech.udig.style.cache".equals(styleID)){
            if( Boolean.FALSE.equals( value )){
                return true; // user turned off caching
            }
            else if ( Boolean.TRUE.equals( value )){
                return false; // not for us!
            }
            else {
                return true; // we should be a good default
            }
        }
        // although we expect SLDContent; we are willing to work with any Style
        //
        if( value != null && value instanceof Style){
            Style style = (Style) value;
            return SLDs.rasterSymbolizer( style ) != null;
        }
        return false;
    }

//    public boolean isOptimized() {
//        return false;
//    }

    public Set<Range<Double>> getValidScaleRanges() {
        Object value = context.getLayer().getStyleBlackboard().get(SLDContent.ID);
        if( value == null ) {
            return new HashSet<Range<Double>>();
        }
        if( value instanceof Style ){
            Style style = (Style) value;
            return MinMaxScaleCalculator.getValidScaleRanges(style);
        }
        else {
            System.out.println("Unexpected "+value.getClass()+" for "+SLDContent.ID+":"+value);
            return new HashSet<Range<Double>>();
        }

    }

}
