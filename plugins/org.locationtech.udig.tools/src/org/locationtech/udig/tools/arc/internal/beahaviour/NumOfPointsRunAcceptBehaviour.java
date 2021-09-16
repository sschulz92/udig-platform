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
package org.locationtech.udig.tools.arc.internal.beahaviour;

import org.locationtech.udig.project.ui.render.displayAdapter.MapMouseEvent;
import org.locationtech.udig.tools.edit.EditState;
import org.locationtech.udig.tools.edit.EditToolHandler;
import org.locationtech.udig.tools.edit.EventBehaviour;
import org.locationtech.udig.tools.edit.EventType;
import org.locationtech.udig.tools.edit.behaviour.AcceptOnDoubleClickBehaviour;
import org.locationtech.udig.tools.edit.support.PrimitiveShape;

/**
 * <p>
 * Requirements:
 * <ul>
 * <li>EventType==RELEASED</li>
 * <li>EditState==MODIFIED or CREATING</li>
 * <li>no modifiers</li>
 * <li>button1 clicked</li>
 * <li>no buttons down</li>
 * </ul>
 * </p>
 * <p>
 * Action:
 * <ul>
 * <li>Adds the point where click occurs <b>if</b> addPoint is true. (Default behaviour)</li>
 * <li>Runs Accept Behaviours</li>
 * <li>If current state is CREATING the changes state to MODIFYING</li>
 * </ul>
 * </p>
 * 
 * @author Aritz Davila (www.axios.es)
 * @author Mauricio Pazos (www.axios.es)
 * @since 1.1.0
 */
public class NumOfPointsRunAcceptBehaviour extends AcceptOnDoubleClickBehaviour
        implements
            EventBehaviour {

    private int numOfPointsToAccept;

    /**
     * @param numOfPointsToAccept number of points, counting the currently being added one, that
     *        must exist in the edit blackboard to accept
     */
    public NumOfPointsRunAcceptBehaviour( int numOfPointsToAccept ) {
        super();
        assert numOfPointsToAccept > 0;
        this.numOfPointsToAccept = numOfPointsToAccept;
    }

    @Override
    public boolean isValid( EditToolHandler handler, MapMouseEvent e, EventType eventType ) {
        boolean goodState = handler.getCurrentState() != EditState.NONE;
        boolean releasedEvent = eventType == EventType.RELEASED;
        boolean noModifiers = !(e.modifiersDown());
        boolean button1 = e.button == MapMouseEvent.BUTTON1;
        boolean onlyButton1Down = e.buttons - (e.buttons & MapMouseEvent.BUTTON1) == 0;

        PrimitiveShape currentShape = handler.getCurrentShape();
        boolean shapeIsSet = currentShape != null;

        if (!(shapeIsSet && goodState && releasedEvent && noModifiers && button1 && onlyButton1Down))
            return false;

        int numPoints = currentShape.getNumPoints();
        boolean ready = numPoints == numOfPointsToAccept;

        return ready;
    }

}
