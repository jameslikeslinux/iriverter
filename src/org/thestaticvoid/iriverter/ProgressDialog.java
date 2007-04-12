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
	private Label header, inputVideoLabel, inputVideo, outputVideoLabel, outputVideo, status;
	private ProgressBar progressBar;
	private String syncInputVideo, syncOutputVideo, syncStatus;
	private Button dismiss;
	private int currentJob, totalJobs, syncPercentComplete;
	
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
		header.setText("Converting");
		FontData[] fontData = header.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		fontData[0].setHeight(fontData[0].getHeight() + 4);
		header.setFont(new Font(getParent().getDisplay(), fontData));
		GridData gridData = new GridData();
		header.setLayoutData(gridData);
		
		Composite infoComp = new Composite(shell, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.numColumns = 2;
		infoComp.setLayout(gridLayout);
		infoComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		inputVideoLabel = new Label(infoComp, SWT.NONE);
		inputVideoLabel.setText("Input:");
		fontData = inputVideoLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		inputVideoLabel.setFont(new Font(getParent().getDisplay(), fontData));
		
		inputVideo = new Label(infoComp, SWT.NONE);
		
		outputVideoLabel = new Label(infoComp, SWT.NONE);
		outputVideoLabel.setText("Output:");
		outputVideoLabel.setFont(new Font(getParent().getDisplay(), fontData));
		
		outputVideo = new Label(infoComp, SWT.NONE);
		
		progressBar = new ProgressBar(shell, SWT.HORIZONTAL | SWT.SMOOTH);
		progressBar.setMaximum(100);
		progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		status = new Label(shell, SWT.NONE);
		fontData = status.getFont().getFontData();
		fontData[0].setStyle(SWT.ITALIC);
		status.setFont(new Font(getParent().getDisplay(), fontData));
		
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
	
	public synchronized void complete(final boolean success) {		
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (!success) {
					MessageBox dialog = new MessageBox(getParent().getShell(), SWT.ICON_ERROR);
					dialog.setText("There Was an Error While Converting");
					dialog.setMessage("An error occurred while converting " + inputVideo.getText());
					dialog.open();
				}

				shell.setText("Complete");
				header.setText("Complete");
				inputVideoLabel.setText("");
				inputVideo.setText("");
				outputVideoLabel.setText("");
				outputVideo.setText("");
				progressBar.setSelection(100);
				status.setText("");
				dismiss.setText("Close");
			}
		});
	}
	
	public synchronized void setCurrentJob(int currentJob) {
		this.currentJob = currentJob;
		
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (!shell.isDisposed() && !header.isDisposed()) {
					shell.setText("Converting " + ProgressDialog.this.currentJob + " of " + totalJobs);
					header.setText("Converting " + ProgressDialog.this.currentJob + " of " + totalJobs);
					header.pack();
				}
			}
		});		
	}
	
	public synchronized void setTotalJobs(int totalJobs) {
		this.totalJobs = totalJobs;
	}
	
	public synchronized void setInputVideo(String inputVideo) {
		syncInputVideo = inputVideo;

		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (!ProgressDialog.this.inputVideo.isDisposed()) {
					ProgressDialog.this.inputVideo.setText(syncInputVideo);
					ProgressDialog.this.inputVideo.pack();
				}
			}
		});
	}
	
	public synchronized void setOutputVideo(String outputVideo) {
		syncOutputVideo = outputVideo;
		
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (!ProgressDialog.this.outputVideo.isDisposed()) {
					ProgressDialog.this.outputVideo.setText(syncOutputVideo);
					ProgressDialog.this.outputVideo.pack();
				}
			}
		});
	}
	
	public synchronized void setPercentComplete(int percentComplete) {
		syncPercentComplete = percentComplete;
		
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (!progressBar.isDisposed())
					progressBar.setSelection(syncPercentComplete);
			}
		});
	}
	
	public synchronized void setStatus(String status) {
		syncStatus = status;
		
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (!ProgressDialog.this.status.isDisposed()) {
					ProgressDialog.this.status.setText(syncStatus);
					ProgressDialog.this.status.pack();
				}
			}
		});
	}
	
	public synchronized String getStatus() {		
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (!status.isDisposed())
					syncStatus = status.getText();
			}
		});
		
		return syncStatus;
	}
}
