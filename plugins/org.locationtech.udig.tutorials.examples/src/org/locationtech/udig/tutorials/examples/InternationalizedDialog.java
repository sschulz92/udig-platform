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
package org.locationtech.udig.tutorials.examples;

import org.locationtech.udig.tutorials.examples.internal.Messages;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

/**
 * This is a quick example to show how to make
 * use of the Eclipse Externalize Strings wizard.
 * <p>
 * This example is used as part of the refractions
 * training course.
 * @author Jody Garnett
 */
public class InternationalizedDialog {
	/**
	 * When this method is called a MessageDialog asking the
	 * users name will be displayed.
	 * @param parent 
	 * 
	 * @return Users name, or null if caneled
	 */
	public static String getUserName(Shell parent){
		String title = Messages.InternationalizedDialog_Title;
		String message = Messages.InternationalizedDialog_Prompt;
		String name = System.getenv("user.name"); //$NON-NLS-1$
		InputDialog prompt = new InputDialog(
			parent,
			title,
			message,
			name,
			new IInputValidator(){
				public String isValid(String name) {
					return name.length() > 2 ? name : null;
				}			
		});		
		if( prompt.open() == Window.OK ){
			return prompt.getValue();
		}
		return null; // user pressed cancel		
	}
}
