/*
 * ProgressDialog.java
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

public class ProgressDialog extends Dialog implements SelectionListener, ProgressDialogInfo {
	private Shell shell;
	private Label header, jobDescription, subdescription, miscellaneous1, miscellaneous2;
	private ProgressBar progressBar;
	private Button dismiss;
	private int currentJob, totalJobs;
	
	public ProgressDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	public void open() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Converting");
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		shell.setLayout(gridLayout);
		
		header = new Label(shell, SWT.NONE);
		header.setText("Job");
		FontData[] fontData = header.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		fontData[0].setHeight(fontData[0].getHeight() + 4);
		header.setFont(new Font(getParent().getDisplay(), fontData));
		GridData gridData = new GridData();
		header.setLayoutData(gridData);
		
		jobDescription = new Label(shell, SWT.NONE);
		fontData = jobDescription.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		jobDescription.setFont(new Font(getParent().getDisplay(), fontData));
		
		progressBar = new ProgressBar(shell, SWT.HORIZONTAL | SWT.SMOOTH);
		progressBar.setMaximum(100);
		progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		subdescription = new Label(shell, SWT.NONE);
		fontData = subdescription.getFont().getFontData();
		fontData[0].setStyle(SWT.ITALIC);
		subdescription.setFont(new Font(getParent().getDisplay(), fontData));
		
		Composite details = new Composite(shell, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		details.setLayout(gridLayout);
		details.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		miscellaneous1 = new Label(details, SWT.NONE);
		miscellaneous2 = new Label(details, SWT.NONE);
		
		dismiss = new Button(shell, SWT.PUSH);
		dismiss.setText("Cancel");
		gridData = new GridData();
		gridData.widthHint = 75;
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = SWT.RIGHT;
		dismiss.setLayoutData(gridData);
		dismiss.addSelectionListener(this);
		
		shell.pack();
		shell.setSize(400, shell.getSize().y);
		
		shell.open();
		while (!shell.isDisposed())
			if (!getParent().getDisplay().readAndDispatch())
				getParent().getDisplay().sleep();
	}
	
	public void close() {
		shell.dispose();
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		// empty
	}
	
	public void widgetSelected(SelectionEvent e) {
		close();
	}
	
	public void setCurrentJob(int currentJob) {
		this.currentJob = currentJob;
		
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (!shell.isDisposed() && !header.isDisposed()) {
					shell.setText("Job " + ProgressDialog.this.currentJob + " of " + totalJobs);
					header.setText("Job " + ProgressDialog.this.currentJob + " of " + totalJobs);
					header.pack();
				}
			}
		});		
	}
	
	public void setTotalJobs(int totalJobs) {
		this.totalJobs = totalJobs;
	}
	
	public synchronized void setJobDescription(final String jobDescription) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (!ProgressDialog.this.jobDescription.isDisposed()) {
					ProgressDialog.this.jobDescription.setText(jobDescription);
					ProgressDialog.this.jobDescription.pack();
				}
			}
		});
	}
	
	public void setPercentComplete(final int percentComplete) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (!progressBar.isDisposed())
					progressBar.setSelection(percentComplete);
			}
		});
	}
	
	public void setSubdescription(final String subdescription) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (!ProgressDialog.this.subdescription.isDisposed()) {
					ProgressDialog.this.subdescription.setText(subdescription);
					ProgressDialog.this.subdescription.pack();
				}
			}
		});
	}
	
	public void setMiscellaneous1(final String miscellaneous1) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (!ProgressDialog.this.miscellaneous1.isDisposed()) {
					ProgressDialog.this.miscellaneous1.setText(miscellaneous1);
					ProgressDialog.this.miscellaneous1.pack();
				}
			}
		});
	}
	
	public void setMiscellaneous2(final String miscellaneous2) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (!ProgressDialog.this.miscellaneous2.isDisposed()) {
					ProgressDialog.this.miscellaneous2.setText(miscellaneous2);
					ProgressDialog.this.miscellaneous2.pack();
				}
			}
		});
	}
}
