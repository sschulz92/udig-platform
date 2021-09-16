/**
 * uDig - User Friendly Desktop Internet GIS client
 * https://locationtech.org/projects/technology.udig
 * (C) 2017, Eclipse Foundation
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Eclipse Distribution
 * License v1.0 (http://www.eclipse.org/org/documents/edl-v10.html).
 */
package org.locationtech.udig.project.internal.render.impl.renderercreator;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.geotools.data.FeatureSource;
import org.geotools.util.Range;
import org.locationtech.udig.project.ILayer;
import org.locationtech.udig.project.internal.render.Renderer;
import org.locationtech.udig.project.internal.render.impl.RendererImpl;
import org.locationtech.udig.project.render.AbstractRenderMetrics;
import org.locationtech.udig.project.render.IRenderContext;
import org.locationtech.udig.project.render.IRenderMetricsFactory;
import org.locationtech.udig.project.render.IRenderer;
import org.locationtech.udig.project.render.RenderException;

/**
 * For testing.  Creates a normal Renderer.  Accepts resources that resolve to FeatureSource objects.
 * 
 * The factory creates a Renderer with zero latency  and time to draw metrics
 * 
 * @author Nikolaos Pringouris <nprigour@gmail.com>
 */
public class LowLatencyFeatureRenderMetricsFactory implements IRenderMetricsFactory {


    /**
     * Define an AbstractRenderMetrics instance with the following values:
     * <ul>
     * <li>latency metric -  0</li>
     * <li>time to draw metric - 0</li>
     * </ul>
     * @author Nikolaos Pringouris <nprigour@gmail.com>
     *
     */
    public class LowLatencyFeatureRendererMetrics extends AbstractRenderMetrics {

        public LowLatencyFeatureRendererMetrics( IRenderContext context, IRenderMetricsFactory factory ) {
            super(context, factory, new ArrayList<String>());
            latencyMetric = 0;
            timeToDrawMetric = 0;
        }
      
        public boolean canAddLayer( ILayer layer ) {
            return layer.hasResource(FeatureSource.class);
        }

        public boolean canStyle( String styleID, Object value ) {
            return value instanceof SingleRendererStyleContent;
        }

        public Renderer createRenderer() {
            return new LowLatencyRenderer();
        }
        

        @SuppressWarnings("unchecked")
        public Set<Range<Double>> getValidScaleRanges() {
            return new HashSet<Range<Double>>();
        }
    }

    /**
     * Renderer used by {@link LowLatencyFeatureRenderMetricsFactory} 
     *
     */
    public class LowLatencyRenderer extends RendererImpl {

        @Override
        public void render( Graphics2D destination, IProgressMonitor monitor ) throws RenderException {
        }

        @Override
        public void render( IProgressMonitor monitor ) throws RenderException {
        }

    }
    
    public boolean canRender( IRenderContext context ) throws IOException {
        return context.getGeoResource().canResolve(FeatureSource.class);
    }

    public AbstractRenderMetrics createMetrics( IRenderContext context ) {
        return new LowLatencyFeatureRendererMetrics(context, this);
    }

    public Class< ? extends IRenderer> getRendererType() {
        return LowLatencyRenderer.class;
    }

}
