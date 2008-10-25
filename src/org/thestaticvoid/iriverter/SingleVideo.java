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
	private InputVideo inputVideo;
	private MPlayerInfo inputVideoInfo;
	private Text outputVideoInput;
	private Button outputVideoSelect, chapterSelection, previewButton;
	private Combo titleCombo, audioStreamCombo, subtitlesCombo;
	private String mplayerPath, outputVideoText;
	private boolean isDvd, hasMultipleAudio, hasMultipleSubtitles;
	
	public SingleVideo(Composite parent, int style, CTabItem tabItem, InputVideo inputVideo, String mplayerPath) throws Exception {
		super(parent, style);
		this.inputVideo = inputVideo;
		this.mplayerPath = mplayerPath;
		
		inputVideoInfo = new MPlayerInfo(inputVideo, mplayerPath);
		if (!inputVideoInfo.videoSupported()) {
			MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_ERROR);
			messageBox.setText("Unsupported Video");
			messageBox.setMessage("MPlayer does not recognize this type of video:\n" + new File(inputVideo.getName()).getName());
			messageBox.open();
			throw new Exception("Unsupported video");
		}
		
		if (inputVideoInfo.getNumberOfTitles() > 0) {
			isDvd = true;
			hasMultipleAudio = true;
			hasMultipleSubtitles = true;
		} else {
			isDvd = false;
			hasMultipleAudio = false;
			hasMultipleSubtitles = false;
			
			if (inputVideoInfo.getAudioStreams().size() > 1)
				hasMultipleAudio = true;
			if (inputVideoInfo.getSubtitleLanguages().size() > 1)
				hasMultipleSubtitles = true;
		}
		
		InputStream is = getClass().getResourceAsStream(isDvd ? "icons/dvd-16.png" : "icons/singlevideo-16.png");
		tabItem.setImage(new Image(getDisplay(), is));
		tabItem.setText(new File(inputVideo.getName()).getName());
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		// gridLayout.numColumns = 4;
		gridLayout.numColumns = 3;
		setLayout(gridLayout);
		
		Label singleVideoLabel = new Label(this, SWT.NONE);
		singleVideoLabel.setText(isDvd ? "DVD" : "Video");
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
		outputVideoInput.setText(inputVideo.getName().substring(0, inputVideo.getName().lastIndexOf('.')) + "." + ConverterOptions.getCurrentProfile().getProfileName() + "." + ConverterOptions.getCurrentProfile().getWrapperFormat());
		outputVideoInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		outputVideoSelect = new Button(this, SWT.PUSH);
		outputVideoSelect.setText("Select");
		gridData = new GridData();
		gridData.widthHint = 75;
		outputVideoSelect.setLayoutData(gridData);
		outputVideoSelect.addSelectionListener(this);
		
		Composite dvdComp = new Composite(this, SWT.NONE);
		dvdComp.setVisible(isDvd || hasMultipleAudio || hasMultipleSubtitles);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		dvdComp.setLayout(gridLayout);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		dvdComp.setLayoutData(gridData);
		
		Composite groupsComp = new Composite(dvdComp, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = true;
		groupsComp.setLayout(gridLayout);
		groupsComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Group locations = new Group(groupsComp, SWT.NONE);
		locations.setEnabled(isDvd);
		locations.setText("Parts");
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		locations.setLayout(gridLayout);
		locations.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite titleComposite = new Composite(locations, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.numColumns = 2;
		titleComposite.setLayout(gridLayout);
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.verticalAlignment = SWT.CENTER;
		titleComposite.setLayoutData(gridData);
		
		Label titleLabel = new Label(titleComposite, SWT.NONE);
		titleLabel.setEnabled(isDvd);
		titleLabel.setText("Title:");
		
		titleCombo = new Combo(titleComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
		titleCombo.setEnabled(isDvd);
		titleCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		titleCombo.addSelectionListener(this);
		
		chapterSelection = new Button(locations, SWT.PUSH);
		chapterSelection.setEnabled(isDvd);
		chapterSelection.setText("Chapters");
		chapterSelection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		chapterSelection.addSelectionListener(this);
		
		Group languages = new Group(groupsComp, SWT.NONE);
		languages.setEnabled(hasMultipleAudio || hasMultipleSubtitles);
		languages.setText("Languages");
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.numColumns = 2;
		languages.setLayout(gridLayout);
		languages.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label audioStreamLabel = new Label(languages, SWT.NONE);
		audioStreamLabel.setEnabled(hasMultipleAudio);
		audioStreamLabel.setText("Audio:");
		
		audioStreamCombo = new Combo(languages, SWT.DROP_DOWN | SWT.READ_ONLY);
		audioStreamCombo.setEnabled(hasMultipleAudio);
		audioStreamCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label subtitlesLabel = new Label(languages, SWT.NONE);
		subtitlesLabel.setEnabled(hasMultipleSubtitles);
		subtitlesLabel.setText("Subtitles:");
		
		subtitlesCombo = new Combo(languages, SWT.DROP_DOWN | SWT.READ_ONLY);
		subtitlesCombo.setEnabled(hasMultipleSubtitles);
		subtitlesCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		previewButton = new Button(languages, SWT.PUSH);
		previewButton.setText("Preview");
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		previewButton.setLayoutData(gridData);
		previewButton.addSelectionListener(this);
		
		Composite output = new Composite(dvdComp, SWT.NONE);
		output.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.numColumns = 3;
		output.setLayout(gridLayout);
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
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
		// empty
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
		return "Converting " + new File(inputVideo.getName()).getName();
	}
	
	public ShitToDo[] getShitToDo() {
		java.util.List shitToDo = new ArrayList();
		
		MencoderCommand command = new MencoderCommand(inputVideo, getOutputVideo(), mplayerPath, inputVideoInfo, 0);
		shitToDo.add(new MencoderShit("Encoding...", command));
		
		return (ShitToDo[]) shitToDo.toArray(new ShitToDo[]{});
	}
}
