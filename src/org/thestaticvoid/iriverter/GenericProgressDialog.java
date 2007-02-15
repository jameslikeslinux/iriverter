/*
 * GenericProgressDialog.java
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
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.*;

public abstract class GenericProgressDialog extends Dialog {
	private Shell shell;
	private String title;
	private int maximum, current;
	private Label header, status;
	private ProgressBar progressBar;
	
	public GenericProgressDialog(Shell shell, int style, String title) {
		super(shell, style);
		this.title = title;
	}
	
	public void open() {		
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(title);
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		shell.setLayout(gridLayout);
		
		header = new Label(shell, SWT.NONE);
		header.setText(title);
		FontData[] fontData = header.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		fontData[0].setHeight(fontData[0].getHeight() + 4);
		header.setFont(new Font(Display.getDefault(), fontData));
		
		progressBar = new ProgressBar(shell, SWT.HORIZONTAL | SWT.SMOOTH);
		progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		status = new Label(shell, SWT.NONE);
		status.setText(getStatusText());
		fontData = status.getFont().getFontData();
		fontData[0].setStyle(SWT.ITALIC);
		status.setFont(new Font(getParent().getDisplay(), fontData));
		
		shell.pack();
		shell.setSize(400, shell.getSize().y);
		
		shell.open();
		while (!shell.isDisposed())
			if (!getParent().getDisplay().readAndDispatch())
				getParent().getDisplay().sleep();
	}
	
	public synchronized void close() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				shell.dispose();
			}
		});
	}
	
	public int getMaximum() {
		return maximum;
	}
	
	public synchronized void setMaximum(final int maximum) {
		this.maximum = maximum;
	
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				progressBar.setMaximum(maximum);
			}
		});
	}
	
	public int getCurrent() {
		return current;
	}
	
	public synchronized void setCurrent(final int current) {
		this.current = current;
		
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				progressBar.setSelection(current);
				status.setText(getStatusText());
				status.pack();
			}
		});
	}
	
	public abstract String getStatusText();
}
