/*
 * ManualSplit.java
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

public class ManualSplit extends Composite implements SelectionListener, Job {
	private InputVideo inputVideo;
	private Text hr, min, sec;
	private Button add, remove;
	private Label length;
	private List marksList;
	private String mplayerPath;
	private Mark[] syncMarks;
	
	public ManualSplit(Composite parent, int style, CTabItem tabItem, InputVideo inputVideo, String mplayerPath) throws Exception {
		super(parent, style);
		this.inputVideo = inputVideo;
		this.mplayerPath = mplayerPath;
		
		MPlayerInfo inputVideoInfo = new MPlayerInfo(inputVideo, mplayerPath);
		if (!inputVideoInfo.videoSupported()) {
			MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_ERROR);
			messageBox.setText("Unsupported Video");
			messageBox.setMessage("MPlayer does not recognize this type of video:\n" + new File(inputVideo.getName()).getName());
			messageBox.open();
			throw new Exception("Unsupported video");
		}
		
		tabItem.setText(new File(inputVideo.getName()).getName());
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		gridLayout.numColumns = 3;
		setLayout(gridLayout);
		
		Label manualSplitLabel = new Label(this, SWT.NONE);
		manualSplitLabel.setText("Manual Split");
		FontData[] fontData = manualSplitLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		manualSplitLabel.setFont(new Font(getParent().getDisplay(), fontData));
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		manualSplitLabel.setLayoutData(gridData);
		
		Composite addMarkComp = new Composite(this, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.numColumns = 6;
		addMarkComp.setLayout(gridLayout);
		addMarkComp.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
		
		Label lengthLabel = new Label(addMarkComp, SWT.NONE);
		lengthLabel.setText("Length:");
		
		length = new Label(addMarkComp, SWT.NONE);
		length.setText("0:00:00");
		gridData = new GridData();
		gridData.horizontalSpan = 5;
		length.setLayoutData(gridData);
		
		Label markLabel = new Label(addMarkComp, SWT.NONE);
		markLabel.setText("Mark:");
		
		hr = new Text(addMarkComp, SWT.BORDER);
		hr.setText("0");
		hr.addSelectionListener(this);
		
		Label colon = new Label(addMarkComp, SWT.NONE);
		colon.setText(":");
		
		min = new Text(addMarkComp, SWT.BORDER);
		min.setText("00");
		min.addSelectionListener(this);
		
		colon = new Label(addMarkComp, SWT.NONE);
		colon.setText(":");
		
		sec = new Text(addMarkComp, SWT.BORDER);
		sec.setText("00");
		sec.addSelectionListener(this);
		
		Composite addRemoveButtonComp = new Composite(this, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		addRemoveButtonComp.setLayout(gridLayout);
		addRemoveButtonComp.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
		
		add = new Button(addRemoveButtonComp, SWT.PUSH);
		add.setText("Add Mark");
		add.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		add.addSelectionListener(this);
		
		remove = new Button(addRemoveButtonComp, SWT.PUSH);
		remove.setText("Remove Mark");
		remove.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		remove.addSelectionListener(this);
		
		Composite marksListComp = new Composite(this, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		marksListComp.setLayout(gridLayout);
		marksListComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		marksList = new List(marksListComp, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER);
		marksList.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		int length = inputVideoInfo.getLength();
		int seconds = length % 60;
		int minutes = length / 60;
		int hours = minutes / 60;
		minutes = minutes - (hours * 60);

		this.length.setText(hours + ":" + ((minutes < 10) ? "0" + minutes : "" + minutes) + ":" + ((seconds < 10) ? "0" + seconds : "" + seconds));
		hr.setText("0");
		min.setText("00");
		sec.setText("00");
		marksList.removeAll();
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}
	
	public void widgetSelected(SelectionEvent e) {		
		if (e.getSource() == add || e.getSource() == hr || e.getSource() == min || e.getSource() == sec) {
			try {
				Integer.parseInt(hr.getText());
				
				if (Integer.parseInt(min.getText()) < 10 && min.getText().length() == 1)
					min.setText("0" + min.getText());
				
				if (Integer.parseInt(sec.getText()) < 10 && sec.getText().length() == 1)
					sec.setText("0" + sec.getText());
				
				if (Integer.parseInt(min.getText()) > 59 || Integer.parseInt(sec.getText()) > 59)
					return;
			} catch (Exception exception) {
				return;
			}
			
			String mark = hr.getText() + ":" + min.getText() + ":" + sec.getText();
			
			if (mark.compareTo(length.getText()) >= 0 || mark.equals("0:00:00"))
				return;

			int index;
			for (index = 0; index < marksList.getItemCount() && mark.compareTo(marksList.getItem(index)) >= 0; index++);
			marksList.add(mark, index);
		}
		
		if (e.getSource() == remove) {
			int index = marksList.getSelectionIndex();
			
			if (index > -1)
				marksList.remove(index);
		}	
	}
	
	public Mark[] getMarks() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				syncMarks = new Mark[marksList.getItemCount() + 2];
				syncMarks[0] = new Mark(Mark.START_MARK);
				for (int i = 0; i < marksList.getItemCount(); i++)
					syncMarks[i + 1] = new Mark(marksList.getItems()[i]);
				syncMarks[syncMarks.length - 1] = new Mark(Mark.END_MARK);
			}
		});
		
		return syncMarks;
	}
	
	public int getPart() {
		return -1;
	}
	
	public String getDescription() {
		return "Splitting " + new File(inputVideo.getName()).getName();
	}
	
	public ShitToDo[] getShitToDo() {
		java.util.List shitToDo = new java.util.ArrayList();
		
		for (int i = 0; (i + 1) < getMarks().length; i++) {
			String inputVideo = this.inputVideo.getName();
			String outputVideo = inputVideo.substring(0, inputVideo.lastIndexOf('.')) + ".part" + (i + 1) + ".avi";
			
			if (getMarks()[i].getTime() == Mark.START_MARK)
				shitToDo.add(new MencoderShit("Working on part " + (i + 1) + " of " + (getMarks().length - 1) + "...", new MencoderCommand(new String[]{mplayerPath + MPlayerInfo.MENCODER_BIN, "-ovc", "copy", "-oac", "copy", "-endpos", "" + getMarks()[i + 1].getTime()}, this.inputVideo, outputVideo)));
			else if (getMarks()[i + 1].getTime() == Mark.END_MARK)
				shitToDo.add(new MencoderShit("Working on part " + (i + 1) + " of " + (getMarks().length - 1) + "...", new MencoderCommand(new String[]{mplayerPath + MPlayerInfo.MENCODER_BIN, "-ovc", "copy", "-oac", "copy", "-ss", "" + getMarks()[i].getTime()}, this.inputVideo, outputVideo)));
			else
				shitToDo.add(new MencoderShit("Working on part " + (i + 1) + " of " + (getMarks().length - 1) + "...", new MencoderCommand(new String[]{mplayerPath + MPlayerInfo.MENCODER_BIN, "-ovc", "copy", "-oac", "copy", "-ss", "" + getMarks()[i].getTime(), "-endpos", "" + getMarks()[i + 1].getTime()}, this.inputVideo, outputVideo)));
		}
		
		return (ShitToDo[]) shitToDo.toArray(new ShitToDo[]{});
	}
}
