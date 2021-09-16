/**
 * uDig - User Friendly Desktop Internet GIS client
 * http://udig.refractions.net
 * (C) 2011, Refractions Research Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 */
package org.locationtech.udig.tutorials.toolview;

import java.awt.Color;
import java.awt.Shape;
import java.util.List;

import org.locationtech.udig.mapgraphic.MapGraphic;
import org.locationtech.udig.mapgraphic.MapGraphicContext;
import org.locationtech.udig.ui.graphics.ViewportGraphics;

import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * Render all Referenced envelopes on the map as red semi-transparent rectangles.  The SendAlertTool will place
 * the alerts on the blackboard
 */
public class ShowAlertsMapGraphic implements MapGraphic {
	
	public static final String ALERTS_KEY = "ALERTS";
	public static final String EXTENSION_ID = "org.locationtech.udig.tutorials.tool-view.showalertmapgraphic";

	@Override
	public void draw(MapGraphicContext context) {
		@SuppressWarnings("unchecked")
		List<ReferencedEnvelope> alerts = (List<ReferencedEnvelope>) context.getLayer().getBlackboard().get(ALERTS_KEY);
		if(alerts == null) return;
		
		ViewportGraphics graphics = context.getGraphics();
		
		for (ReferencedEnvelope referencedEnvelope : alerts) {
			Shape shape = context.toShape(referencedEnvelope);
			graphics.setColor(new Color(255,0,0,50));
			graphics.fill(shape);
			graphics.setColor(Color.RED);
			graphics.draw(shape);
		}
	}

}
