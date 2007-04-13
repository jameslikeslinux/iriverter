/*
 * AddDirectoryDialog.java
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

public class AddDirectoryDialog extends Dialog implements SelectionListener {
	private Shell shell;
	private Text directory, fileType;
	private Button select, doSubdirectories, add, remove, cancel, ok;
	private List fileTypesList;
	private DirectoryScanner directoryScanner;
	
	public AddDirectoryDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	public DirectoryScanner open() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Add Directory");
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		gridLayout.numColumns = 1;
		shell.setLayout(gridLayout);
		
		Label addDirectoryLabel = new Label(shell, SWT.NONE);
		addDirectoryLabel.setText("Add Directory");
		FontData[] fontData = addDirectoryLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		addDirectoryLabel.setFont(new Font(getParent().getDisplay(), fontData));
		
		Composite inputComposite = new Composite(shell, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.numColumns = 2;
		inputComposite.setLayout(gridLayout);
		inputComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		directory = new Text(inputComposite, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.widthHint = 200;
		directory.setLayoutData(gridData);
		directory.addSelectionListener(this);
		
		select = new Button(inputComposite, SWT.PUSH);
		select.setText("Select");
		gridData = new GridData();
		gridData.widthHint = 75;
		select.setLayoutData(gridData);
		select.addSelectionListener(this);
		
		Group fileTypesGroup = new Group(shell, SWT.NONE);
		fileTypesGroup.setText("File Types");
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 6;
		gridLayout.marginWidth = 6;
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = true;
		fileTypesGroup.setLayout(gridLayout);
		fileTypesGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite modifyTypesGroup = new Composite(fileTypesGroup, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.numColumns = 2;
		modifyTypesGroup.setLayout(gridLayout);
		modifyTypesGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		fileType = new Text(modifyTypesGroup, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		fileType.setLayoutData(gridData);
		fileType.addSelectionListener(this);
		
		add = new Button(modifyTypesGroup, SWT.PUSH);
		add.setText("Add");
		add.addSelectionListener(this);
		
		remove = new Button(modifyTypesGroup, SWT.PUSH);
		remove.setText("Remove");
		remove.addSelectionListener(this);
		
		fileTypesList = new List(fileTypesGroup, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER);
		fileTypesList.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		doSubdirectories = new Button(shell, SWT.CHECK);
		doSubdirectories.setText("Scan Subdirectories");
		
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
		shell.open();
		
		fileTypesList.add(".asf");
		fileTypesList.add(".avi");
		fileTypesList.add(".mkv");
		fileTypesList.add(".mov");
		fileTypesList.add(".mp4");
		fileTypesList.add(".mpeg");
		fileTypesList.add(".mpg");
		fileTypesList.add(".ogm");
		fileTypesList.add(".rm");
		fileTypesList.add(".vob");
		fileTypesList.add(".wmv");
		
		while (!shell.isDisposed())
			if (!getParent().getDisplay().readAndDispatch())
				getParent().getDisplay().sleep();
		
		return directoryScanner;
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == select) {
			String directory = new DirectoryDialog(shell, SWT.OPEN).open();
			if (directory != null)
				this.directory.setText(directory);
		}
		
		if (e.getSource() == add || e.getSource() == fileType) {
			String fileType = this.fileType.getText();
			if (!fileType.equals("")) {				
				int index;
				for (index = 0; index < fileTypesList.getItemCount() && fileType.compareTo(fileTypesList.getItem(index)) > 0; index++);
				if (fileType.compareTo(fileTypesList.getItem(index)) != 0)
						fileTypesList.add(fileType, index);
			}
		}
		
		if (e.getSource() == remove) {
			int index = fileTypesList.getSelectionIndex();
			
			if (index > -1)
				fileTypesList.remove(index);
		}
		
		if (e.getSource() == cancel)
			shell.dispose();
		
		if (e.getSource() == ok) {
			if (this.directory.getText().equals("")) {
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
				messageBox.setText("Empty Directory");
				messageBox.setMessage("Select a directory");
				messageBox.open();
				return;
			}
			
			if (fileTypesList.getItemCount() == 0) {
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
				messageBox.setText("Empty File Types List");
				messageBox.setMessage("Choose at least one file type for which to scan");
				messageBox.open();
				return;
			}
			
			File directory = new File(this.directory.getText());
			if (!directory.isDirectory()) {
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
				messageBox.setText("Invalid Directory");
				messageBox.setMessage(directory + "\nis not a directory.");
				messageBox.open();
				return;
			}
			
			directoryScanner = new DirectoryScanner(directory, fileTypesList.getItems(), doSubdirectories.getSelection());
			shell.dispose();
		}
	}
}
