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
package org.locationtech.udig.printing.model;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * A Box is a conceptual object that can appear on a page. It is an extension
 * of an <code>Element</code> and provides methods that allow a Box to draw
 * itself and support connections to other boxes.
 *
 * Implementors should directly implement this interface or one of its
 * implementations. DecoratorBox in particular.
 *
 * @author Richard Gould
 * @since 0.3
 * @see Element
 * @model
 */
public interface Box extends Element, IAdaptable {

    /**
     * This is used by the GEF system to access the source connections on this Box
     *
     * @return a List of Connections that use this Box as a source
     * @model resolveProxy="false" type="org.locationtech.udig.printing.model.Connection"
     */
    public List<Connection> getSourceConnections();

    /**
     * This is used by the GEF system to access the target connections on this Box
     *
     * @return a List of Connections that use this Box as a target
     * @model resolveProxy="false" type="org.locationtech.udig.printing.model.Connection"
     */
    public List<Connection> getTargetConnections();

    /**
     *
     * Adds a Connection to this Box
     * @see Connection
     * @param connection the connection to add to this Box
     */
    void add(Connection connection);

    /**
     *
     * Removes a Connection to this Box
     * @see Connection
     * @param connection the connection to be removed
     */
    void remove(Connection connection);

    /**
     * Gets the object responsible for drawing the preview and printing the contents of this box.
     *
     * @return Gets the object responsible for drawing the preview and printing the contents of this box.
     * @model
     */
    BoxPrinter getBoxPrinter();

	/**
     * Sets the value of the '{@link org.locationtech.udig.printing.model.Box#getBoxPrinter <em>Box Printer</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Box Printer</em>' attribute.
     * @see #getBoxPrinter()
     * @generated
     */
    void setBoxPrinter(BoxPrinter value);

    /**
     * Can be called to notify listeners that a event has occurred.
     *
     * @param eventData
     */
    void notifyPropertyChange( PropertyChangeEvent event);

    /**
     * Adds a listener to the box.  Each listener will only be added once.
     *
     * @param l the listener to add.
     */
    void addPropertyChangeListener( IPropertyChangeListener l);

    /**
     * removes a listener from the box.
     *
     * @param l the listener to remove.
     */
    void removePropertyChangeListener( IPropertyChangeListener l);

    /**
     * Gets the id for the box.
     *
     * @return the id for the box.
     * @model
     */
    String getID();

	/**
     * Sets the value of the '{@link org.locationtech.udig.printing.model.Box#getID <em>ID</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>ID</em>' attribute.
     * @see #getID()
     * @generated
     */
    void setID(String value);

}
