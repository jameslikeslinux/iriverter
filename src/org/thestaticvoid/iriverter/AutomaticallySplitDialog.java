/*
 * AutomaticallySplitDialog.java
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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class AutomaticallySplitDialog extends Dialog implements SelectionListener {
	private Shell shell;
	private Label splitVideoEveryLabel, minutesLabel;
	private Spinner splitTimeInput;
	private Button automaticallySplit, cancel, ok;
	
	public AutomaticallySplitDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	public void open() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Automatically Split");
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		gridLayout.numColumns = 3;
		shell.setLayout(gridLayout);
		
		Label audioSyncLabel = new Label(shell, SWT.NONE);
		audioSyncLabel.setText("Automatically Split");
		FontData[] fontData = audioSyncLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		audioSyncLabel.setFont(new Font(getParent().getDisplay(), fontData));
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		audioSyncLabel.setLayoutData(gridData);
		
		automaticallySplit = new Button(shell, SWT.CHECK);
		automaticallySplit.setText("Automatically Split");
		automaticallySplit.setSelection(ConverterOptions.getAutoSplit());
		gridData = new GridData();
		gridData.horizontalSpan = 3;
		automaticallySplit.setLayoutData(gridData);
		automaticallySplit.addSelectionListener(this);
		
		splitVideoEveryLabel = new Label(shell, SWT.NONE);
		splitVideoEveryLabel.setText("Split video every");
		
		splitTimeInput = new Spinner(shell, SWT.BORDER);
		splitTimeInput.setSelection(Math.abs(ConverterOptions.getSplitTime()));
		splitTimeInput.setMinimum(0);
		
		minutesLabel = new Label(shell, SWT.NONE);
		minutesLabel.setText("minutes");
		
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
		
		if (!automaticallySplit.getSelection())
			toggleSelection();
		
		shell.pack();		
		shell.open();
		while (!shell.isDisposed())
			if (!getParent().getDisplay().readAndDispatch())
				getParent().getDisplay().sleep();
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == automaticallySplit)
			toggleSelection();
		
		if (e.getSource() == cancel)
			shell.dispose();
		
		if (e.getSource() == ok) {			
			ConverterOptions.setAutoSplit(automaticallySplit.getSelection());
			ConverterOptions.setSplitTime(splitTimeInput.getSelection());
			shell.dispose();
		}
	}
	
	public void toggleSelection() {
		splitVideoEveryLabel.setEnabled(!splitVideoEveryLabel.getEnabled());
		splitTimeInput.setEnabled(!splitTimeInput.getEnabled());
		minutesLabel.setEnabled(!minutesLabel.getEnabled());
	}
}
