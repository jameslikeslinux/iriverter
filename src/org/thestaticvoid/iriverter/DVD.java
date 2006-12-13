/*
 * DVD.java
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

public class DVD extends Composite implements SelectionListener, TabItemControl, DVDInfo {
	private CTabItem tabItem;
	private Combo dvdCombo, titleCombo, audioStreamCombo, subtitlesCombo;
	private Map titleInfo, audioStreams, subtitles;
	private Button chapterSelection, previewButton, outputVideoSelect;
	private Text outputVideoInput;
	private Chapters[] chapters;
	private String syncDrive, syncOutputVideo;
	private DVDProgressDialog progressDialog;
	private int syncTitle, syncAudioStream, syncSubtitles;
	private Process proc;
	
	public DVD(Composite parent, int style, CTabItem tabItem) {
		super(parent, style);
		this.tabItem = tabItem;
		
		titleInfo = new LinkedHashMap();
		
		InputStream is = getClass().getResourceAsStream("icons/dvd-16.png");
		tabItem.setImage(new Image(getDisplay(), is));
		tabItem.setText("New DVD");
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 12;
		gridLayout.marginTop = 12;
		gridLayout.marginBottom = 0;
		setLayout(gridLayout);
		
		Label dvdLabel = new Label(this, SWT.NONE);
		dvdLabel.setText("DVD");
		FontData[] fontData = dvdLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		dvdLabel.setFont(new Font(getParent().getDisplay(), fontData));
		GridData gridData = new GridData();
		dvdLabel.setLayoutData(gridData);
		
		Composite dvdComp = new Composite(this, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		dvdComp.setLayout(gridLayout);
		dvdComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		
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
		locations.setText("Locations");
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.numColumns = 2;
		locations.setLayout(gridLayout);
		locations.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label driveLabel = new Label(locations, SWT.NONE);
		driveLabel.setText("DVD Drive:");
		
		dvdCombo = new Combo(locations, SWT.DROP_DOWN | SWT.READ_ONLY);
		dvdCombo.setItems(getDVDDrives());
		dvdCombo.select(0);
		dvdCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		dvdCombo.addSelectionListener(this);
		
		Label titleLabel = new Label(locations, SWT.NONE);
		titleLabel.setText("Title:");
		
		titleCombo = new Combo(locations, SWT.DROP_DOWN | SWT.READ_ONLY);
		titleCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		titleCombo.addSelectionListener(this);
		
		chapterSelection = new Button(locations, SWT.PUSH);
		chapterSelection.setText("Chapters");
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		chapterSelection.setLayoutData(gridData);
		chapterSelection.addSelectionListener(this);
		
		Group languages = new Group(groupsComp, SWT.NONE);
		languages.setText("Languages");
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.numColumns = 2;
		languages.setLayout(gridLayout);
		languages.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label audioStreamLabel = new Label(languages, SWT.NONE);
		audioStreamLabel.setText("Audio Stream:");
		
		audioStreamCombo = new Combo(languages, SWT.DROP_DOWN | SWT.READ_ONLY);
		audioStreamCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label subtitlesLabel = new Label(languages, SWT.NONE);
		subtitlesLabel.setText("Subtitles:");
		
		subtitlesCombo = new Combo(languages, SWT.DROP_DOWN | SWT.READ_ONLY);
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
		
		Label outputVideo = new Label(output, SWT.NONE);
		outputVideo.setText("Output:");
		
		outputVideoInput = new Text(output, SWT.BORDER);
		outputVideoInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		outputVideoSelect = new Button(output, SWT.PUSH);
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
		if (e.getSource() == dvdCombo) {
			if (dvdCombo.getText().equals("Other...")) {
				DirectoryDialog directoryDialog = new DirectoryDialog(getShell(), SWT.NONE);
				directoryDialog.setText("DVD Device");
				String dvdDevice = directoryDialog.open();
				if (dvdDevice != null && new File(dvdDevice + File.separator + "VIDEO_TS").exists()) {				
					if (dvdCombo.getItem(0).equals("None Found"))
						dvdCombo.remove(0);
					dvdCombo.add(dvdDevice, 0);
					dvdCombo.select(0);
				}
			} 
			
			setTitleCombo();
		}
		
		if (e.getSource() == titleCombo) {
			chapters = null;
			setLanguageCombos();
		}
		
		if (e.getSource() == chapterSelection) {
			if (dvdCombo.getText().equals("None Found") || dvdCombo.getText().equals("Other..."))
				return;
			
			ChapterDialog chapterDialog = null;
			if (chapters == null) 
				chapterDialog = new ChapterDialog(getParent().getShell(), SWT.NONE, ((DVDTitleInfo) titleInfo.get(titleCombo.getText())).getNumberOfChapters(), true);
			else
				chapterDialog = new ChapterDialog(getParent().getShell(), SWT.NONE, ((DVDTitleInfo) titleInfo.get(titleCombo.getText())).getNumberOfChapters(), chapters);
			
			chapters = chapterDialog.open();
		}

		if (e.getSource() == previewButton) {
			if (!getDrive().equals("")) {
				boolean canceled = false;
				while (!canceled)
					try {
						java.util.List commandList = new ArrayList();
						commandList.add(MPlayerInfo.getMPlayerPath() + "mplayer");
						commandList.add("-dvd-device");
						commandList.add(getDrive());
						commandList.add("dvd://" + getTitle());
						
						if (getAudioStream() > -1) {
							commandList.add("-aid");
							commandList.add("" + getAudioStream());
						}
						
						if (getSubtitles() > -1) {
							commandList.add("-sid");
							commandList.add("" + getSubtitles());
						}
						
						String commandStr = "";
						String[] command = new String[commandList.size()];
						for (int i = 0; i < command.length; i++) {
							command[i] = (String) commandList.get(i);
							commandStr += command[i] + " ";
						}
						Logger.logMessage(commandStr, Logger.INFO);
						
						proc = Runtime.getRuntime().exec(command);
					} catch (IOException io) {
						io.printStackTrace();
						canceled = true;
					} catch (MPlayerNotFoundException mpe) {
						canceled = new MPlayerPathDialog(getParent().getShell(), SWT.NONE).open();
					}
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
	
	private String[] getDVDDrives() {
		java.util.List drives = new ArrayList();
		if (System.getProperty("os.name").indexOf("Windows") > -1) {
			for (char driveLetter = 'D'; driveLetter <= 'Z'; driveLetter++) {
				try {
					File drive = new File(driveLetter + ":\\");
					if (drive.canRead() && new File(drive.getAbsolutePath() + "VIDEO_TS").exists())
						drives.add(drive.getAbsolutePath());
				} catch (Exception e) {
					// empty
				}
			}
		} else {
			try {
				Process proc = Runtime.getRuntime().exec("mount");

				BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String line;

				while ((line = input.readLine()) != null)
					if (line.substring(0, 4).equals("/dev")) {
						String mountPoint = line.split(" ")[2];
						
						if (new File(mountPoint + File.separator + "VIDEO_TS").exists())
							drives.add(mountPoint);
					}
			} catch (Exception e) {
				// empty
			}
		}
		

		if (drives.size() == 0)
			drives.add("None Found");

		drives.add("Other...");

		String[] driveStrings = new String[drives.size()];
		for (int i = 0; i < driveStrings.length; i++)
			driveStrings[i] = (String) drives.get(i);
		
		return driveStrings;
	}
	
	public void setTitleCombo() {
		if (dvdCombo.getText().equals("None Found") || dvdCombo.getText().equals("Other...")) {
			titleCombo.removeAll();
			audioStreamCombo.removeAll();
			subtitlesCombo.removeAll();
			
			return;
		}

		boolean canceled = false;
		while (!canceled)
			try {
				tabItem.setText(dvdCombo.getText());
				progressDialog = new DVDProgressDialog(getShell(), SWT.NONE);
				DVDInfoReader infoReader = new DVDInfoReader(progressDialog, dvdCombo.getText(), MPlayerInfo.getMPlayerPath());
				progressDialog.open();

				titleInfo = infoReader.getTitleInfo();

				titleCombo.removeAll();
				for (int i = 0; i < titleInfo.keySet().toArray().length; i++)
					titleCombo.add((String) titleInfo.keySet().toArray()[i]);
				titleCombo.select(0);

				setLanguageCombos();
			} catch (MPlayerNotFoundException mpe) {
				canceled = new MPlayerPathDialog(getParent().getShell(), SWT.NONE).open();
				if (canceled)
					progressDialog.close();
			}
	}

	private void setLanguageCombos() {
		audioStreams = ((DVDTitleInfo) titleInfo.get(titleCombo.getText())).getAudioStreams();
		subtitles = ((DVDTitleInfo) titleInfo.get(titleCombo.getText())).getSubtitles();
		
		audioStreamCombo.removeAll();
		for (int i = 0; i < audioStreams.keySet().size(); i++)
			audioStreamCombo.add((String) audioStreams.keySet().toArray()[i]);
		audioStreamCombo.select(0);
		
		subtitlesCombo.removeAll();
		for (int i = 0; i < subtitles.keySet().size(); i++)
			subtitlesCombo.add((String) subtitles.keySet().toArray()[i]);
		subtitlesCombo.select(0);
	}
	
	public Map getTitleInfo() {
		return titleInfo;
	}
	
	public void setTitleInfo(Map titleInfo) {
		this.titleInfo = titleInfo;
		for (int i = 0; i < titleInfo.size(); i++)
			titleCombo.add((String) titleInfo.keySet().toArray()[i]);
		
		if (titleInfo.size() > 0) {
			titleCombo.select(0);
			setLanguageCombos();
		}
	}
	
	public void setTabItem(CTabItem tabItem) {
		this.tabItem = tabItem;
	}
	
	public void setDrive(String drive) {
		for (int i = 0; i < dvdCombo.getItemCount(); i++)
			if (dvdCombo.getItem(i).equals(drive)) {
				tabItem.setText(drive);
				return;
			}
		
		tabItem.setText(drive);
		
		if (dvdCombo.getItem(0).equals("None Found"))
			dvdCombo.remove(0);
		dvdCombo.add(drive, 0);
		dvdCombo.select(0);
	}
	
	public synchronized String getDrive() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				syncDrive = (dvdCombo.getText().equals("None Found") || dvdCombo.getText().equals("Other...") ? "" : dvdCombo.getText());
			}
		});
		
		return syncDrive;
	}
	
	public synchronized int getTitle() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				syncTitle = titleCombo.getText().equals("") ? -1 : Integer.parseInt(titleCombo.getText().substring(0, titleCombo.getText().indexOf('.')));
			}
		});
		
		return syncTitle;
	}
	
	public synchronized Chapters[] getChapters() {
		return chapters;
	}
	
	public synchronized int getAudioStream() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				syncAudioStream = audioStreams.get(audioStreamCombo.getText()).equals("") ? -1 : Integer.parseInt((String) audioStreams.get(audioStreamCombo.getText()));
			}
		});
		
		return syncAudioStream;
	}
	
	public synchronized int getSubtitles() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				syncSubtitles = subtitles.get(subtitlesCombo.getText()).equals("") ? -1 : Integer.parseInt((String) subtitles.get(subtitlesCombo.getText()));
			}
		});
		
		return syncSubtitles;
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
