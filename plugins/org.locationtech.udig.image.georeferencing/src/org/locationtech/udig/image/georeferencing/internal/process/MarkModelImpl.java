/* Image Georeferencing
 *
 * Axios Engineering
 *      http://www.axios.es
 *
 * (C) 2011, Axios Engineering S.L. (Axios)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Axios BSD
 * License v1.0 (http://udig.refractions.net/files/asd3-v10.html).
 */
package org.locationtech.udig.image.georeferencing.internal.process;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.graphics.Point;

import org.locationtech.jts.geom.Coordinate;

/**
 * Stores valuable info about each mark or dot.
 *
 * This class is also an {@link Observable}, so it'll broadcast actions like:
 * New mark, mark modified or mark deleted.
 *
 * @author Mauricio Pazos (www.axios.es)
 * @author Aritz Davila (www.axios.es)
 * @since 1.3.3
 *
 */
final class MarkModelImpl extends Observable implements Serializable, MarkModel {


	private static final long	serialVersionUID	= 4462169346430911665L;
	private int					xImage, yImage;
	private Double				xCoord				= Double.NaN, yCoord = Double.NaN;
	private String				ID;

	/**
	 * Constructor. The given string will be the marks ID.
	 *
	 * @param newMarkID
	 *            The ID of the mark.
	 */
	public MarkModelImpl(String newMarkID) {

		this.ID = newMarkID;
	}

	public int getXImage() {
		return xImage;
	}

	public void setXImage(int xImage) {
		this.xImage = xImage;
	}

	public int getYImage() {
		return yImage;
	}

	public void setYImage(int yImage) {
		this.yImage = yImage;
	}

	public Double getXCoord() {
		return xCoord;
	}

	public void setXCoord(Double xCoord) {
		this.xCoord = xCoord;
	}

	public Double getYCoord() {
		return yCoord;
	}

	public void setYCoord(Double yCoord) {
		this.yCoord = yCoord;
	}

	public String getID() {
		return this.ID;
	}

	public void initializeImagePosition(Point point) {

		this.xImage = point.x;
		this.yImage = point.y;

		setChanged();
		notifyObservers(MarkModelChange.NEW);
	}

	public void updateImagePosition(Point point) {

		this.xImage = point.x;
		this.yImage = point.y;

		setChanged();
		notifyObservers(MarkModelChange.MODIFY);
	}

	public void updateCoordinatePosition(Coordinate coord) {

		this.xCoord = coord.x;
		this.yCoord = coord.y;

		setChanged();
		notifyObservers(MarkModelChange.MODIFY);
	}

	public Point getImagePosition() {
		return new Point(xImage, yImage);
	}

	public void delete() {
		setChanged();
		notifyObservers(MarkModelChange.DELETE);
	}

	@Override
	public int hashCode() {
		return this.getID().hashCode();
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder(getID());
		builder.append(";"); //$NON-NLS-1$
		builder.append(getXImage());
		builder.append(";"); //$NON-NLS-1$
		builder.append(getYImage());
		builder.append(";"); //$NON-NLS-1$
		builder.append(getXCoord());
		builder.append(";"); //$NON-NLS-1$
		builder.append(getYCoord());

		return builder.toString();
	}

	public void addObserver(Observer observer) {
		super.addObserver( observer);
	}

	public void deleteObserver(Observer observer) {
		super.deleteObserver( observer);
	}

}
