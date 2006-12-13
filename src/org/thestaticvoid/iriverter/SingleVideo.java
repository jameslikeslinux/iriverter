/*
 * SingleVideo.java
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
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;

import java.io.*;

public class SingleVideo extends Composite implements SelectionListener, TabItemControl, SingleVideoInfo {
	private CTabItem tabItem;
	private Text inputVideoInput, outputVideoInput;
	private Button inputVideoSelect, outputVideoSelect;
	private String syncInputVideo, syncOutputVideo;
	
	public SingleVideo(Composite parent, int style, CTabItem tabItem) {
		super(parent, style);
		this.tabItem = tabItem;
		
		InputStream is = getClass().getResourceAsStream("icons/singlevideo-16.png");
		tabItem.setImage(new Image(getDisplay(), is));
		tabItem.setText("New Single Video");
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		// gridLayout.numColumns = 4;
		gridLayout.numColumns = 3;
		setLayout(gridLayout);
		
		Label singleVideoLabel = new Label(this, SWT.NONE);
		singleVideoLabel.setText("Single Video");
		FontData[] fontData = singleVideoLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		singleVideoLabel.setFont(new Font(getParent().getDisplay(), fontData));
		GridData gridData = new GridData();
		// gridData.horizontalSpan = 4;
		gridData.horizontalSpan = 3;
		singleVideoLabel.setLayoutData(gridData);
		
		/* Label tab = new Label(this, SWT.NONE);
		tab.setText("\t"); */
		
		Label inputVideo = new Label(this, SWT.NONE);
		inputVideo.setText("Input:");
		
		inputVideoInput = new Text(this, SWT.BORDER);
		inputVideoInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		inputVideoSelect = new Button(this, SWT.PUSH);
		inputVideoSelect.setText("Select");
		gridData = new GridData();
		gridData.widthHint = 75;
		inputVideoSelect.setLayoutData(gridData);
		inputVideoSelect.addSelectionListener(this);
		
		/* tab = new Label(this, SWT.NONE);
		tab.setText("\t"); */
		
		Label outputVideo = new Label(this, SWT.NONE);
		outputVideo.setText("Output:");
		
		outputVideoInput = new Text(this, SWT.BORDER);
		outputVideoInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		outputVideoSelect = new Button(this, SWT.PUSH);
		outputVideoSelect.setText("Select");
		gridData = new GridData();
		gridData.widthHint = 75;
		outputVideoSelect.setLayoutData(gridData);
		outputVideoSelect.addSelectionListener(this);
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		// empty
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == inputVideoSelect) {
			FileDialog fileDialog = new FileDialog(getShell(), SWT.OPEN);
			fileDialog.setText("Input Video");
			fileDialog.setFilterExtensions(new String[]{"*.avi;*.vob;*.mkv;*.mpg;*.mpeg;*.ogm;*.mov;*.rm;*.ram;*.wmv;*.asf", "*.avi", "*.vob", "*.mkv", "*.mpg;*.mpeg;*.mp4", "*.ogm", "*.mov", "*.rm;*.ram", "*.wmv;*.asf", "*"});
			fileDialog.setFilterNames(new String[]{"All Video Files", "AVI Video (*.avi)", "DVD Video Object (*.vob)", "Matroska Video (*.mkv)", "MPEG Video (*.mpg, *.mpeg, *.mp4)", "Ogg Video (*.ogm)", "Quicktime Movie (*.mov)", "Real Video (*.rm, *.ram)", "Windows Media Video (*.wmv, *.asf)", "All Files"});
			String file = fileDialog.open();
			if (file != null) {
				inputVideoInput.setText(file);
				outputVideoInput.setText(file.substring(0, file.lastIndexOf('.')) + "." + ConverterOptions.getCurrentProfile().getProfileName() + "." + ConverterOptions.getCurrentProfile().getWrapperFormat());
				tabItem.setText(new File(file).getName());
			}
		}
		
		if (e.getSource() == outputVideoSelect) {
			FileDialog fileDialog = new FileDialog(getShell(), SWT.SAVE);
			fileDialog.setText("Output Video");
			if (ConverterOptions.getCurrentProfile().getWrapperFormat().equals("mp4")) {
				fileDialog.setFilterExtensions(new String[]{"*.mp4"});
				fileDialog.setFilterNames(new String[]{"MP4 Video (*.mp4)"});
			} else {
				fileDialog.setFilterExtensions(new String[]{"*.avi"});
				fileDialog.setFilterNames(new String[]{"AVI Video (*.avi)"});
			}
			String file = fileDialog.open();
			if (file != null)
				outputVideoInput.setText(file);
		}
	}
	
	public void setTabItem(CTabItem tabItem) {
		this.tabItem = tabItem;
	}
	
	public void setInputVideo(String inputVideo) {
		tabItem.setText(new File(inputVideo).getName());
		
		inputVideoInput.setText(inputVideo);
		outputVideoInput.setText(inputVideo.substring(0, inputVideo.lastIndexOf('.')) + "." + ConverterOptions.getCurrentProfile().getProfileName() + ".avi");
	}
	
	public synchronized String getInputVideo() {		
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				syncInputVideo = inputVideoInput.getText();
			}
		});
		
		return syncInputVideo;
	}
	
	public synchronized String getOutputVideo() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				syncOutputVideo = outputVideoInput.getText();
			}
		});
		
		return syncOutputVideo;
	}
	
	public synchronized void setOutputVideo(String outputVideo) {
		syncOutputVideo = outputVideo;

		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				outputVideoInput.setText(syncOutputVideo);
			}
		});
	}
}
