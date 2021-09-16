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

import org.locationtech.udig.catalog.IGeoResource;
import org.locationtech.udig.project.render.AbstractRenderMetrics;
import org.locationtech.udig.project.render.ICompositeRenderContext;
import org.locationtech.udig.project.render.IRenderContext;
import org.locationtech.udig.project.render.IRenderMetricsFactory;
import org.locationtech.udig.project.render.IRenderer;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;


/**
 * The RenderMetricFactory Implementation for the BasicGridCoverageRenderer Extension.
 *
 * @author Jesse Eichar
 * @version $Revision: 1.9 $
 */
public class GridCoverageReaderMetricsFactory implements IRenderMetricsFactory {

    /**
     * Ensures that we can get an AbstractGridCoverage2DReader out of this class.
     *
     * @see org.locationtech.udig.project.render.RenderMetricsFactory#canRender(org.locationtech.udig.project.render.RenderTools)
     * @param context
     * @return true if we can render the provided context using BasicGridCoverageRenderer
     */
    public boolean canRender( IRenderContext context ) {
        if (context instanceof ICompositeRenderContext) {
            return false;
        }
        IGeoResource geoResource = context.getGeoResource();
        if (geoResource.canResolve(AbstractGridCoverage2DReader.class)) {
            return true; // a reader is available!
        }
        return false;
    }

    /**
     * Strategy object used to indicate how well a renderer can draw.
     *
     * @see org.locationtech.udig.project.render.RenderMetricsFactory#createMetrics(org.locationtech.udig.project.render.RenderTools)
     * @param context
     * @return render metrics for the provided context
     */
    public AbstractRenderMetrics createMetrics( IRenderContext context ) {
        return new GridCoverageReaderMetrics(context, this);
    }

    public Class< ? extends IRenderer> getRendererType() {
        return GridCoverageReaderRenderer.class;
    }

}
