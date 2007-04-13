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
import java.util.*;

public class SingleVideo extends Composite implements SelectionListener, TabItemControl, Job {
	private CTabItem tabItem;
	private String inputVideo;
	private MPlayerInfo inputVideoInfo;
	private Text outputVideoInput;
	private Button outputVideoSelect;
	private String mplayerPath, outputVideoText;
	
	public SingleVideo(Composite parent, int style, CTabItem tabItem, String inputVideo, String mplayerPath) throws Exception {
		super(parent, style);
		this.tabItem = tabItem;
		this.inputVideo = inputVideo;
		this.mplayerPath = mplayerPath;
		
		inputVideoInfo = new MPlayerInfo(inputVideo.toString(), mplayerPath);
		if (!inputVideoInfo.videoSupported()) {
			MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_ERROR);
			messageBox.setText("Unsupported Video");
			messageBox.setMessage("MPlayer does not recognize this type of video:\n" + new File(inputVideo).getName());
			messageBox.open();
			throw new Exception("Unsupported video");
		}
		
		InputStream is = getClass().getResourceAsStream("icons/singlevideo-16.png");
		tabItem.setImage(new Image(getDisplay(), is));
		tabItem.setText(new File(inputVideo).getName());
		
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
		
		Label outputVideo = new Label(this, SWT.NONE);
		outputVideo.setText("Output:");
		
		outputVideoInput = new Text(this, SWT.BORDER);
		outputVideoInput.setText(inputVideo.substring(0, inputVideo.lastIndexOf('.')) + "." + ConverterOptions.getCurrentProfile().getProfileName() + "." + ConverterOptions.getCurrentProfile().getWrapperFormat());
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
	
	private String getOutputVideo() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				outputVideoText = outputVideoInput.getText();
			}
		});
		
		return outputVideoText;
	}
	
	public String getDescription() {		
		return "Converting " + new File(inputVideo).getName();
	}
	
	public ShitToDo[] getShitToDo() {
		java.util.List shitToDo = new ArrayList();
		
		java.util.List commandList = MencoderCommand.prepareBaseCommandList(inputVideo, getOutputVideo(), mplayerPath, inputVideoInfo, 0);
		String[] command = (String[]) commandList.toArray(new String[]{});
		shitToDo.add(new MencoderCommand("Encoding...", command));
		
		int length = inputVideoInfo.getLength();
		String inputVideo = getOutputVideo();
		if (length > ConverterOptions.getSplitTime() * 60 && ConverterOptions.getAutoSplit()) {
			int pieces = (length / (ConverterOptions.getSplitTime() * 60)) + 1;
			for (int i = 0; i < pieces; i++) {
				String outputVideo = inputVideo.substring(0, inputVideo.lastIndexOf('.')) + ".part" + (i + 1) + ".avi";
				
				if ((i + 1) == 1)
					command = new String[]{mplayerPath + MPlayerInfo.MENCODER_BIN, inputVideo, "-o", outputVideo, "-ovc", "copy", "-oac", "copy", "-endpos", "" + (length / pieces)};
				else if ((i + 1) == pieces)
					command = new String[]{mplayerPath + MPlayerInfo.MENCODER_BIN, inputVideo, "-o", outputVideo, "-ovc", "copy", "-oac", "copy", "-ss", "" + (length / pieces) * i};
				else
					command = new String[]{mplayerPath + MPlayerInfo.MENCODER_BIN, inputVideo, "-o", outputVideo, "-ovc", "copy", "-oac", "copy", "-ss", "" + (length / pieces) * i, "-endpos", "" + (length / pieces)};
				
				shitToDo.add(new MencoderCommand("Splitting Part " + (i + 1) + " of " + pieces, command));
			}
		}
		
		return (ShitToDo[]) shitToDo.toArray(new ShitToDo[]{});
	}
}
