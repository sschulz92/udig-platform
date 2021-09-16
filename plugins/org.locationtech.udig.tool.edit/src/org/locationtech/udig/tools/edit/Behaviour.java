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
package org.locationtech.udig.tools.edit;

import org.locationtech.udig.project.command.UndoableMapCommand;


/**
 * This is a Stategy object for the (@link org.locationtech.udig.tools.edit.latest.EditToolHandler} behaviour. 
 * Each Behaviour is valid in a particular context and will be run by the 
 * (@link org.locationtech.udig.tools.edit.latest.EditToolHandler} if the isValid method returns true.  
 * 
 * @author jones
 * @since 1.1.0
 */
public interface Behaviour {

    /**
     * Called to determine whether this Behaviour is applicable and should be run.
     * @param handler handler that calls this Behaviour
     * @return true if this mode is applicable and should be run.
     */
    public boolean isValid( EditToolHandler handler );

    /**
     * The action to be performed by this Behaviour.  This action takes place in the event thread so it must
     * perform quickly.
     *
     * @param handler handler that calls this Behaviour
     * @return Command that will be executed in order to perform the behaviour 
     */
    public UndoableMapCommand getCommand( EditToolHandler handler );

    /**
     * This method is called if an exception occurs during the execution of the run method.  
     * <p>
     * This method should:
     * <ol>
     * <li>Rollback the changes made during the run method</li>
     * <li>Log the error in the plugin's log</li>
     * </ol>
     * @param error Error that occurred
     * @param command Command retrieved from getCommandMethod.  May be null if exception occurred while
     * executing getCommand();
     */
    public void handleError( EditToolHandler handler, Throwable error, UndoableMapCommand command );
}
