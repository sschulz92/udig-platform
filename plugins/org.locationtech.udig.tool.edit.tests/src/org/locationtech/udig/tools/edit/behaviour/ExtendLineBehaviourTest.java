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
package org.locationtech.udig.tools.edit.behaviour;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.locationtech.udig.project.ui.render.displayAdapter.MapMouseEvent;
import org.locationtech.udig.tools.edit.EditState;
import org.locationtech.udig.tools.edit.EventType;
import org.locationtech.udig.tools.edit.support.EditGeom;
import org.locationtech.udig.tools.edit.support.Point;
import org.locationtech.udig.tools.edit.support.TestHandler;

import org.junit.Test;

/**
 * Extend a line test
 * @author jones
 * @since 1.1.0
 */
public class ExtendLineBehaviourTest {

    /*
     * Test method for 'org.locationtech.udig.tools.edit.behaviour.ExtendLineBehaviour.isValid(EditToolHandler, MapMouseEvent, EventType)'
     */
    @Test
    public void testIsValid() throws Exception {
        TestHandler handler=new TestHandler();
        handler.getTestEditBlackboard().util.setVertexRadius(4);
        
        StartExtendLineBehaviour behavior=new StartExtendLineBehaviour();

        handler.setCurrentState(EditState.MODIFYING);

        //Current Shape must be set
        MapMouseEvent event = new MapMouseEvent(null, 10,10, MapMouseEvent.NONE, MapMouseEvent.NONE, MapMouseEvent.BUTTON1);
        assertFalse(behavior.isValid(handler, event, EventType.RELEASED));
        
        EditGeom editGeom = handler.getEditBlackboard().getGeoms().get(0);
        handler.setCurrentShape(editGeom.getShell());
        handler.getEditBlackboard().addPoint(0,0, handler.getCurrentShape());
        handler.getEditBlackboard().addPoint(11,11, handler.getCurrentShape());
        
        event = new MapMouseEvent(null, 10,10, MapMouseEvent.NONE, MapMouseEvent.NONE, MapMouseEvent.BUTTON1);
        assertTrue(behavior.isValid(handler, event, EventType.RELEASED));
        
        // no buttons should be down
        event = new MapMouseEvent(null, 10,10, MapMouseEvent.NONE, MapMouseEvent.BUTTON2, MapMouseEvent.BUTTON1);
        assertFalse(behavior.isValid(handler, event, EventType.RELEASED));
        // button2 isn't legal
        event = new MapMouseEvent(null, 10,10, MapMouseEvent.NONE, MapMouseEvent.NONE, MapMouseEvent.BUTTON2);
        assertFalse(behavior.isValid(handler, event, EventType.RELEASED));

        // no modifiers only
        event = new MapMouseEvent(null, 10,10, MapMouseEvent.ALT_DOWN_MASK, MapMouseEvent.NONE, MapMouseEvent.BUTTON1);
        assertFalse(behavior.isValid(handler, event, EventType.RELEASED));

        // doesn't work with BUSY
        handler.setCurrentState(EditState.BUSY);
        event = new MapMouseEvent(null, 10,10, MapMouseEvent.NONE, MapMouseEvent.NONE, MapMouseEvent.BUTTON1);
        assertFalse(behavior.isValid(handler, event, EventType.RELEASED));
        
        // doesn't work with CREATING
        handler.setCurrentState(EditState.CREATING);
        event = new MapMouseEvent(null, 10,10, MapMouseEvent.NONE, MapMouseEvent.NONE, MapMouseEvent.BUTTON1);
        assertFalse(behavior.isValid(handler, event, EventType.RELEASED));
        
        // should work, just checking state is still good;
        handler.setCurrentState(EditState.NONE);
        event = new MapMouseEvent(null, 10,10, MapMouseEvent.NONE, MapMouseEvent.NONE, MapMouseEvent.BUTTON1);
        assertTrue(behavior.isValid(handler, event, EventType.RELEASED));
        
        // doesn't work with event pressed
        assertFalse(behavior.isValid(handler, event, EventType.PRESSED));

        // not valid if not over end point
        event = new MapMouseEvent(null, 20,10, MapMouseEvent.NONE, MapMouseEvent.NONE, MapMouseEvent.BUTTON1);
        assertFalse(behavior.isValid(handler, event, EventType.RELEASED));
       
        // valid over first point
        event = new MapMouseEvent(null, 0,0, MapMouseEvent.NONE, MapMouseEvent.NONE, MapMouseEvent.BUTTON1);
        assertTrue(behavior.isValid(handler, event, EventType.RELEASED));
        
    }

    /*
     * Test method for 'org.locationtech.udig.tools.edit.behaviour.ExtendLineBehaviour.getCommand(EditToolHandler, MapMouseEvent, EventType)'
     */
    @Test
    public void testGetCommand() throws Exception {
        TestHandler handler=new TestHandler();
        handler.getTestEditBlackboard().util.setVertexRadius(4);
        
        StartExtendLineBehaviour behavior=new StartExtendLineBehaviour();
        handler.getBehaviours().add(behavior);
        handler.setCurrentState(EditState.MODIFYING);

        EditGeom editGeom = handler.getEditBlackboard().getGeoms().get(0);
        handler.setCurrentShape(editGeom.getShell());
        handler.getEditBlackboard().addPoint(0,0, handler.getCurrentShape());
        handler.getEditBlackboard().addPoint(11,11, handler.getCurrentShape());
        

        MapMouseEvent event = new MapMouseEvent(null, 0,0, MapMouseEvent.NONE, MapMouseEvent.NONE, MapMouseEvent.BUTTON1);
        assertTrue(behavior.isValid(handler, event, EventType.RELEASED));
        
        handler.handleEvent(event, EventType.RELEASED);
        
        assertEquals(Point.valueOf(11,11), handler.getCurrentShape().getPoint(0));
        assertEquals(EditState.CREATING, handler.getCurrentState());

        handler.setCurrentState(EditState.NONE);
        event = new MapMouseEvent(null, 10,10, MapMouseEvent.NONE, MapMouseEvent.NONE, MapMouseEvent.BUTTON1);
        assertTrue(behavior.isValid(handler, event, EventType.RELEASED));
        
        handler.handleEvent(event, EventType.RELEASED);
        assertEquals(Point.valueOf(0,0), handler.getCurrentShape().getPoint(0));
        assertEquals(EditState.CREATING, handler.getCurrentState());
        
        handler.setCurrentState(EditState.NONE);
        
        handler.handleEvent(event, EventType.RELEASED);
        assertEquals(Point.valueOf(0,0), handler.getCurrentShape().getPoint(0));
        assertEquals(EditState.CREATING, handler.getCurrentState());
    }

}
