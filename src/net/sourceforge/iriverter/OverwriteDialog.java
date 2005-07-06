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
