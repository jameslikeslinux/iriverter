/*
 * MPlayerPathDialog.java
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

public class MPlayerPathDialog extends Dialog implements SelectionListener {
	private Shell shell;
	private Button download, extraCodecs, keepUpdated, local, localDirSelect, cancel, ok;
	private Text localDir;
	private boolean canceled;
	
	public MPlayerPathDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	public boolean open() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("MPlayer Path");
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		shell.setLayout(gridLayout);
		
		Label audioSyncLabel = new Label(shell, SWT.NONE);
		audioSyncLabel.setText("MPlayer Path");
		FontData[] fontData = audioSyncLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		audioSyncLabel.setFont(new Font(getParent().getDisplay(), fontData));
		
		Composite downloadComposite = new Composite(shell, SWT.RADIO);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 3;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		downloadComposite.setLayout(gridLayout);
		
		download = new Button(downloadComposite, SWT.RADIO);
		download.setText("Attempt to download MPlayer from the Internet");
		download.addSelectionListener(this);
		
		extraCodecs = new Button(downloadComposite, SWT.CHECK);
		extraCodecs.setText("Also download non-free codecs");
		extraCodecs.setSelection(ConverterOptions.getDownloadExtraCodecs());
		GridData gridData = new GridData();
		gridData.horizontalIndent = 24;
		extraCodecs.setLayoutData(gridData);
		
		keepUpdated = new Button(downloadComposite, SWT.CHECK);
		keepUpdated.setText("Keep updated");
		keepUpdated.setSelection(ConverterOptions.getKeepUpdated());
		gridData = new GridData();
		gridData.horizontalIndent = 24;
		keepUpdated.setLayoutData(gridData);
		
		Composite localComposite = new Composite(shell, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 3;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.numColumns = 2;
		localComposite.setLayout(gridLayout);
		localComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		local = new Button(localComposite, SWT.RADIO);
		local.setText("Use MPlayer installed locally in:");
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		local.setLayoutData(gridData);
		local.addSelectionListener(this);
		
		localDir = new Text(localComposite, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalIndent = 24;
		localDir.setLayoutData(gridData);
		
		localDirSelect = new Button(localComposite, SWT.PUSH);
		localDirSelect.setText("Select");
		gridData = new GridData();
		gridData.widthHint = 75;
		localDirSelect.setLayoutData(gridData);
		
		Composite dismissComposite = new Composite(shell, SWT.NONE);
		dismissComposite.setLayout(new RowLayout());
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		dismissComposite.setLayoutData(gridData);
		
		cancel = new Button(dismissComposite, SWT.PUSH);
		cancel.setText("Cancel");
		RowData rowData = new RowData();
		rowData.width = 75;
		cancel.setLayoutData(rowData);
		cancel.addSelectionListener(this);
		
		ok = new Button(dismissComposite, SWT.PUSH);
		ok.setText("OK");
		rowData = new RowData();
		rowData.width = 75;
		ok.setLayoutData(rowData);
		ok.addSelectionListener(this);
		
		if (System.getProperty("os.name").indexOf("Windows") == -1) {
			download.setEnabled(false);
			extraCodecs.setEnabled(false);
			keepUpdated.setEnabled(false);
		}
		
		if (ConverterOptions.getMPlayerSource().equals("download")) {
			download.setSelection(true);
			localDir.setEnabled(false);
			localDirSelect.setEnabled(false);
		} else {
			local.setSelection(true);
			extraCodecs.setEnabled(false);
			localDir.setText(ConverterOptions.getMPlayerPath());
		}
		
		shell.pack();
		shell.setSize(450, shell.getSize().y);
		shell.open();
		while (!shell.isDisposed())
			if (!getParent().getDisplay().readAndDispatch())
				getParent().getDisplay().sleep();
		
		return canceled;
	}
	
	public void widgetDefaultSelected(SelectionEvent event) {
		widgetSelected(event);
	}
	
	public void widgetSelected(SelectionEvent event) {
		if (event.getSource() == download) {
			extraCodecs.setEnabled(true);
			keepUpdated.setEnabled(true);
			local.setSelection(false);
			localDir.setEnabled(false);
			localDirSelect.setEnabled(false);
		}
		
		if (event.getSource() == local) {
			download.setSelection(false);
			extraCodecs.setEnabled(false);
			keepUpdated.setEnabled(false);
			localDir.setEnabled(true);
			localDirSelect.setEnabled(true);
		}
		
		if (event.getSource() == cancel) {
			canceled = true;
			shell.dispose();
		}
	}
}
