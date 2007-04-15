/*
 * JoinVideos.java
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

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;

public class JoinVideos extends Composite implements SelectionListener, TabItemControl, Job {	
	private CTabItem tabItem;
	private java.util.List inputVideos;
	private List videosList;
	private Button up, add, remove, down, outputVideoSelect;
	private Text outputVideoInput;
	private String syncOutputVideo, mplayerPath;
	
	public JoinVideos(Composite parent, int style, CTabItem tabItem, String mplayerPath) {
		super(parent, style);
		this.tabItem = tabItem;
		this.mplayerPath = mplayerPath;
		inputVideos = new java.util.ArrayList();
		
		tabItem.setText("New Join Videos");
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		gridLayout.numColumns = 3;
		setLayout(gridLayout);
		
		Label joinVideosLabel = new Label(this, SWT.NONE);
		joinVideosLabel.setText("Join Videos");
		FontData[] fontData = joinVideosLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		joinVideosLabel.setFont(new Font(getParent().getDisplay(), fontData));
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		joinVideosLabel.setLayoutData(gridData);
		
		Label inputLabel = new Label(this, SWT.NONE);
		inputLabel.setText("Input:");
		inputLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		
		videosList = new List(this, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER);
		videosList.setLayoutData(new GridData(GridData.FILL_BOTH));
		videosList.addSelectionListener(this);
		
		Composite addRemoveMoveButtonComp = new Composite(this, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		addRemoveMoveButtonComp.setLayout(gridLayout);
		addRemoveMoveButtonComp.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
		
		up = new Button(addRemoveMoveButtonComp, SWT.PUSH);
		up.setText("Up");
		up.setEnabled(false);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 75;
		up.setLayoutData(gridData);
		up.addSelectionListener(this);
		
		add = new Button(addRemoveMoveButtonComp, SWT.PUSH);
		add.setText("Add");
		add.setLayoutData(gridData);
		add.addSelectionListener(this);
		
		remove = new Button(addRemoveMoveButtonComp, SWT.PUSH);
		remove.setText("Remove");
		remove.setEnabled(false);
		remove.setLayoutData(gridData);
		remove.addSelectionListener(this);
		
		down = new Button(addRemoveMoveButtonComp, SWT.PUSH);
		down.setText("Down");
		down.setEnabled(false);
		down.setLayoutData(gridData);
		down.addSelectionListener(this);
		
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
		widgetSelected(e);
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == videosList) {
			listChanged();
		}
		
		if (e.getSource() == up) {
			int index = videosList.getSelectionIndex();
			
			if (index > 0) {
				String item = videosList.getItem(index);
				videosList.remove(index);
				videosList.add(item, index - 1);
				
				Object object = inputVideos.get(index);
				inputVideos.remove(index);
				inputVideos.add(index - 1, object);
				
				videosList.select(index - 1);
			}
			
			listChanged();
		}
		
		if (e.getSource() == add) {
			FileDialog fileDialog = new FileDialog(getShell(), SWT.OPEN);
			fileDialog.setText("Input Video");
			fileDialog.setFilterExtensions(new String[]{"*.avi"});
			fileDialog.setFilterNames(new String[]{"AVI Video (*.avi)"});
			String file = fileDialog.open();
			if (file != null) {
					videosList.add(new File(file).getName());
					inputVideos.add(file);
					
					videosList.select(videosList.getItemCount() - 1);
			}
			
			listChanged();
		}
		
		if (e.getSource() == remove) {
			int index = videosList.getSelectionIndex();
			
			if (index > -1) {
				videosList.remove(index);
				inputVideos.remove(index);
				
				videosList.select((index == videosList.getItemCount()) ? index - 1 : index);
			}
			
			listChanged();
		}
		
		if (e.getSource() == down) {
			int index = videosList.getSelectionIndex();
			
			if (index != -1 && index != videosList.getItemCount() - 1) {
				String item = videosList.getItem(index);
				videosList.remove(index);
				videosList.add(item, index + 1);
				
				Object object = inputVideos.get(index);
				inputVideos.remove(index);
				inputVideos.add(index + 1, object);
				
				videosList.select(index + 1);
			}
			
			listChanged();
		}
		
		if (e.getSource() == outputVideoSelect) {
			FileDialog fileDialog = new FileDialog(getShell(), SWT.SAVE);
			fileDialog.setText("Input Video");
			fileDialog.setFilterExtensions(new String[]{"*.avi"});
			fileDialog.setFilterNames(new String[]{"AVI Video (*.avi)"});
			String file = fileDialog.open();
			if (file != null) {
				outputVideoInput.setText(file);
				tabItem.setText(new File(file).getName());
			}
		}
	}
	
	public void listChanged() {
		int index = videosList.getSelectionIndex();
		
		if (videosList.getItemCount() <= 1) {
			up.setEnabled(false);
			down.setEnabled(false);
			
			if (videosList.getItemCount() == 0)
				remove.setEnabled(false);
			else
				remove.setEnabled(true);
		} else if (index == 0) {
			up.setEnabled(false);
			down.setEnabled(true);
		} else if (index == videosList.getItemCount() - 1) {
			up.setEnabled(true);
			down.setEnabled(false);
		} else {
			up.setEnabled(true);
			down.setEnabled(true);
		}
	}
	
	public void setTabItem(CTabItem tabItem) {
		this.tabItem = tabItem;
	}
	
	public String[] getInputVideos() {
		String[] inputVideos = new String[this.inputVideos.size()];
		for (int i = 0; i < this.inputVideos.size(); i++)
			inputVideos[i] = (String) this.inputVideos.get(i);
		
		return inputVideos;
	}
	
	public String getOutputVideo() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				syncOutputVideo = outputVideoInput.getText();
			}
		});
		
		return syncOutputVideo;
	}
	
	public void setOutputVideo(String outputVideo) {
		syncOutputVideo = outputVideo;
		
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				outputVideoInput.setText(syncOutputVideo);
			}
		});
	}
	
	public String getDescription() {
		return "Joining videos into " + new File(getOutputVideo()).getName();
	}
	
	public ShitToDo[] getShitToDo() {
		java.util.List shitToDo = new java.util.ArrayList();
		
		File tempFile;		
		try {
			tempFile = File.createTempFile("iriverter-", ".avi");
			tempFile.deleteOnExit();
		} catch (IOException io) {
			Logger.logException(io);
			return new ShitToDo[]{};
		}
		
		shitToDo.add(new ConcatenateShit("Concatenating videos to a temporary file...", getInputVideos(), tempFile.toString()));
		shitToDo.add(new MencoderShit("Writing header...", new MencoderCommand(new String[]{mplayerPath + MPlayerInfo.MENCODER_BIN, "-forceidx", "-ovc", "copy", "-oac", "copy"}, new InputVideo(tempFile.toString()), getOutputVideo())));
		
		return (ShitToDo[]) shitToDo.toArray(new ShitToDo[]{});
	}
}
