/*
 * VolumeDialog.java
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

public class VolumeDialog extends Dialog implements SelectionListener {
	private Shell shell;
	private Button filterNone, filterVolnorm, filterVolume, cancel, ok;
	private Text gainText;
	
	public VolumeDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	public void open() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Volume");
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		shell.setLayout(gridLayout);
		
		Label audioSyncLabel = new Label(shell, SWT.NONE);
		audioSyncLabel.setText("Volume");
		FontData[] fontData = audioSyncLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		audioSyncLabel.setFont(new Font(getParent().getDisplay(), fontData));
		
		Composite filterGroup = new Composite(shell, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 0;
		gridLayout.numColumns = 3;
		filterGroup.setLayout(gridLayout);
		
		filterNone = new Button(filterGroup, SWT.RADIO);
		filterNone.setText("Apply no volume filter");
		filterNone.setSelection(ConverterOptions.getVolumeFilter().equals(VolumeFilter.NONE));
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		filterNone.setLayoutData(gridData);
		
		filterVolnorm = new Button(filterGroup, SWT.RADIO);
		filterVolnorm.setText("Normalize the volume");
		filterVolnorm.setSelection(ConverterOptions.getVolumeFilter().equals(VolumeFilter.VOLNORM));
		filterVolnorm.setLayoutData(gridData);
		
		filterVolume = new Button(filterGroup, SWT.RADIO);
		filterVolume.setText("Manually specify gain:");
		filterVolume.setSelection(ConverterOptions.getVolumeFilter().equals(VolumeFilter.VOLUME));
		
		gainText = new Text(filterGroup, SWT.BORDER);
		// forces width large width
		gainText.setText("-200.0");
		
		new Label(filterGroup, SWT.NONE).setText("dB");
		
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
		
		shell.pack();
		
		// set actual gain after packing
		gainText.setText("" + ConverterOptions.getGain());
		
		shell.open();
		while (!shell.isDisposed())
			if (!getParent().getDisplay().readAndDispatch())
				getParent().getDisplay().sleep();
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == cancel)
			shell.dispose();
		
		if (e.getSource() == ok) {
			if (filterNone.getSelection())
				ConverterOptions.setVolumeFilter(VolumeFilter.NONE);
			else if (filterVolnorm.getSelection())
				ConverterOptions.setVolumeFilter(VolumeFilter.VOLNORM);
			else {
				try {
					double volume = Double.parseDouble(gainText.getText());
					if (volume < -200.0 || volume > 60.0)
						throw new Exception();
					
					ConverterOptions.setVolumeFilter(VolumeFilter.VOLUME);
					ConverterOptions.setGain(volume);
				} catch (Exception exception) {
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
					messageBox.setText("Invalid Gain");
					messageBox.setMessage("The gain must be between -200.0 dB and 60.0 dB.");
					messageBox.open();
					
					return;
				}
			}
			
			shell.dispose();
		}
	}
}
