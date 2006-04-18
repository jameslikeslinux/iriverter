package org.thestaticvoid.iriverter;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;

import java.io.*;

public class Directory extends Composite implements SelectionListener, TabItemControl, DirectoryInfo {
	private CTabItem tabItem;
	private Text inputDirectoryInput, outputDirectoryInput;
	private Button inputDirectorySelect, outputDirectorySelect, convertSubdirectories;
	private String syncInputDirectory, syncOutputDirectory;
	private boolean syncConvertSubdirectories;
	
	public Directory(Composite parent, int style, CTabItem tabItem) {
		super(parent, style);
		this.tabItem = tabItem;
		
		InputStream is = getClass().getResourceAsStream("icons/directory-16.png");
		tabItem.setImage(new Image(getDisplay(), is));
		tabItem.setText("New Directory");
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		// gridLayout.numColumns = 4;
		gridLayout.numColumns = 3;
		setLayout(gridLayout);
		
		Label directoryLabel = new Label(this, SWT.NONE);
		directoryLabel.setText("Directory");
		FontData[] fontData = directoryLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		directoryLabel.setFont(new Font(getParent().getDisplay(), fontData));
		GridData gridData = new GridData();
		// gridData.horizontalSpan = 4;
		gridData.horizontalSpan = 3;
		directoryLabel.setLayoutData(gridData);
		
		/* Label tab = new Label(this, SWT.NONE);
		tab.setText("\t"); */
		
		Label inputVideo = new Label(this, SWT.NONE);
		inputVideo.setText("Input:");
		
		inputDirectoryInput = new Text(this, SWT.BORDER);
		inputDirectoryInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		inputDirectorySelect = new Button(this, SWT.PUSH);
		inputDirectorySelect.setText("Select");
		gridData = new GridData();
		gridData.widthHint = 75;
		inputDirectorySelect.setLayoutData(gridData);
		inputDirectorySelect.addSelectionListener(this);
		
		/* tab = new Label(this, SWT.NONE);
		tab.setText("\t"); */
		
		Label outputVideo = new Label(this, SWT.NONE);
		outputVideo.setText("Output:");
		
		outputDirectoryInput = new Text(this, SWT.BORDER);
		outputDirectoryInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		outputDirectorySelect = new Button(this, SWT.PUSH);
		outputDirectorySelect.setText("Select");
		gridData = new GridData();
		gridData.widthHint = 75;
		outputDirectorySelect.setLayoutData(gridData);
		outputDirectorySelect.addSelectionListener(this);
		
		/* tab = new Label(this, SWT.NONE);
		tab.setText("\t"); */
		
		new Label(this, SWT.NONE);
		
		convertSubdirectories = new Button(this, SWT.CHECK);
		convertSubdirectories.setText("Convert Subdirectories");
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		convertSubdirectories.setLayoutData(gridData);
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		// empty
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == inputDirectorySelect) {
			DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
			directoryDialog.setText("Input Directory");
			String directory = directoryDialog.open();
			if (directory != null) {
				inputDirectoryInput.setText(directory);
				outputDirectoryInput.setText(directory + "-" + ConverterOptions.getCurrentProfile().getProfileName());
				tabItem.setText(new File(directory).getName());
			}
		}
		
		if (e.getSource() == outputDirectorySelect) {
			DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
			directoryDialog.setText("Output Directory");
			String directory = directoryDialog.open();
			if (directory != null)
				outputDirectoryInput.setText(directory);
		}
	}
	
	public void setTabItem(CTabItem tabItem) {
		this.tabItem = tabItem;
	}
	
	public void setInputDirectory(String directory) {
		tabItem.setText(new File(directory).getName());
		
		inputDirectoryInput.setText(directory);
		outputDirectoryInput.setText(directory + "-" + ConverterOptions.getCurrentProfile().getProfileName());
	}
	
	public synchronized String getInputDirectory() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				syncInputDirectory = inputDirectoryInput.getText();
			}
		});
		
		return syncInputDirectory;
	}
	
	public synchronized String getOutputDirectory() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				syncOutputDirectory = outputDirectoryInput.getText();
			}
		});
		
		return syncOutputDirectory;
	}
	
	public synchronized boolean getConvertSubdirectories() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				syncConvertSubdirectories = convertSubdirectories.getSelection();
			}
		});
		
		return syncConvertSubdirectories;
	}
}
