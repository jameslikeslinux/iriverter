/*
 * OverwriteDialog.java
 * Copyright (C) 2005-2007 James Lee
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 * 
 * $Id$
 */
package org.thestaticvoid.iriverter;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

import java.io.*;

public class OverwriteDialog {
	public static boolean overwriteFile(String file) {
		MessageBox dialog = new MessageBox(Display.getCurrent().getShells()[0], SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		dialog.setText("Overwrite File?");
		dialog.setMessage(new File(file).getName() + " exists.  Would you like to overwrite it?");
		
		return dialog.open() == SWT.YES;
	}
}
