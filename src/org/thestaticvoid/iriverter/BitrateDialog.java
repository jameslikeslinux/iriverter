/*
 * BitrateDialog.java
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

public class BitrateDialog extends Dialog implements SelectionListener {
	private Shell shell;
	private int currentVideoBitrate, currentAudioBitrate;
	private Scale videoBitrateScale, audioBitrateScale;
	private Label currentVideoBitrateLabel, currentAudioBitrateLabel;
	private Button cancel, ok;
	
	public BitrateDialog(Shell parent, int style) {
		super(parent, style);
		currentVideoBitrate = ConverterOptions.getVideoBitrate();
		currentAudioBitrate = ConverterOptions.getAudioBitrate();
	}
	
	public void open() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Bitrate");
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		gridLayout.numColumns = 3;
		shell.setLayout(gridLayout);
		
		Label bitrateLabel = new Label(shell, SWT.NONE);
		bitrateLabel.setText("Bitrate");
		FontData[] fontData = bitrateLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		bitrateLabel.setFont(new Font(getParent().getDisplay(), fontData));
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		bitrateLabel.setLayoutData(gridData);
		
		Label videoBitrateLabel = new Label(shell, SWT.NONE);
		videoBitrateLabel.setText("Video:");
				
		videoBitrateScale = new Scale(shell, SWT.HORIZONTAL);
		videoBitrateScale.setMinimum(50);
		videoBitrateScale.setMaximum(ConverterOptions.getCurrentProfile().getMaxVideoBitrate() / 2);
		videoBitrateScale.setSelection(currentVideoBitrate / 2);
		videoBitrateScale.setPageIncrement(25);
		videoBitrateScale.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		videoBitrateScale.addSelectionListener(this);
		
		currentVideoBitrateLabel = new Label(shell, SWT.NONE);
		currentVideoBitrateLabel.setText(currentVideoBitrate + " Kbps");
		
		Label audioBitrateLabel = new Label(shell, SWT.NONE);
		audioBitrateLabel.setText("Audio:");
		
		audioBitrateScale = new Scale(shell, SWT.HORIZONTAL);
		audioBitrateScale.setMinimum(2);
		audioBitrateScale.setMaximum(ConverterOptions.getCurrentProfile().getMaxAudioBitrate() / 16);
		audioBitrateScale.setSelection(currentAudioBitrate / 16);
		audioBitrateScale.setPageIncrement(1);
		audioBitrateScale.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		audioBitrateScale.addSelectionListener(this);
		
		currentAudioBitrateLabel = new Label(shell, SWT.NONE);
		currentAudioBitrateLabel.setText(currentAudioBitrate + " Kbps");
		
		Composite dismissComposite = new Composite(shell, SWT.NONE);
		dismissComposite.setLayout(new RowLayout());
		gridData = new GridData();
		gridData.horizontalSpan = 3;
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
		
		shell.pack();
		shell.setSize(400, shell.getSize().y);
		
		shell.open();
		while (!shell.isDisposed())
			if (!getParent().getDisplay().readAndDispatch())
				getParent().getDisplay().sleep();
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		// empty
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == videoBitrateScale) {
			currentVideoBitrate = videoBitrateScale.getSelection() * 2;
			currentVideoBitrateLabel.setText(currentVideoBitrate + " Kbps");
			currentVideoBitrateLabel.pack();
		}
		
		if (e.getSource() == audioBitrateScale) {
			currentAudioBitrate = audioBitrateScale.getSelection() * 16;
			currentAudioBitrateLabel.setText(currentAudioBitrate + " Kbps");
			currentAudioBitrateLabel.pack();
		}
		
		if (e.getSource() == cancel)
			shell.dispose();
		
		if (e.getSource() == ok) {
			ConverterOptions.setVideoBitrate(currentVideoBitrate);
			ConverterOptions.setAudioBitrate(currentAudioBitrate);
			shell.dispose();
		}
	}
}
