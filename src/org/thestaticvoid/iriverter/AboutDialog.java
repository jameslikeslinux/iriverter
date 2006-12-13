/*
 * AboutDialog.java
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
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.*;

import java.io.*;

public class AboutDialog extends Dialog implements SelectionListener {
	private Shell shell;
	private Button dismiss;
	
	public AboutDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	public void open() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("About iriverter");
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		gridLayout.numColumns = 2;
		shell.setLayout(gridLayout);

		InputStream is = getClass().getResourceAsStream("icons/iriverter.png");
		Image image = new Image(Display.getDefault(), is);

		Label imageLabel = new Label(shell, SWT.NONE);
		imageLabel.setImage(image);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		gridData.horizontalSpan = 2;
		imageLabel.setLayoutData(gridData);
		
		Label appName = new Label(shell, SWT.NONE);
		appName.setText("iriverter " + Config.VERSION);
		FontData[] fontData = appName.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		fontData[0].setHeight(fontData[0].getHeight() + 7);
		appName.setFont(new Font(getParent().getDisplay(), fontData));
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		gridData.horizontalSpan = 2;
		appName.setLayoutData(gridData);
		
		Label appDesc = new Label(shell, SWT.NONE);
		appDesc.setText("A simple video converter based on MPlayer");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		gridData.horizontalSpan = 2;
		appDesc.setLayoutData(gridData);
		
		Label appCopyright = new Label(shell, SWT.NONE);
		appCopyright.setText("Copyright © 2005-2006 James Lee\n");
		fontData = appCopyright.getFont().getFontData();
		fontData[0].setHeight(fontData[0].getHeight() - 2);
		appCopyright.setFont(new Font(getParent().getDisplay(), fontData));
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		gridData.horizontalSpan = 2;
		appCopyright.setLayoutData(gridData);
	
		// Just to fill the space of the old button
		new Label(shell, SWT.NONE);		

		/* There are no credits for now
		credits = new Button(shell, SWT.PUSH);
		credits.setText("Credits");
		gridData = new GridData();
		gridData.widthHint = 75;
		credits.setLayoutData(gridData);
		credits.addSelectionListener(this); */
		
		dismiss = new Button(shell, SWT.PUSH);
		dismiss.setText("Close");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gridData.widthHint = 75;
		dismiss.setLayoutData(gridData);
		dismiss.addSelectionListener(this);
		
		shell.pack();
		shell.open();
		while (!shell.isDisposed())
			if (!getParent().getDisplay().readAndDispatch())
				getParent().getDisplay().sleep();
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		// empty
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == dismiss)
			shell.dispose();
	}
}
