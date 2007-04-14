/*
 * DVDInfoReader.java
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

import java.util.*;

public class DVDInfoReader extends Thread {
	private DVDProgressDialog progressDialog;
	private InputVideo inputVideo;
	private String mplayerPath;
	private Map titleInfo;
	
	public DVDInfoReader(DVDProgressDialog progressDialog, InputVideo inputVideo, String mplayerPath) {
		this.progressDialog = progressDialog;
		this.inputVideo = inputVideo;
		this.mplayerPath = mplayerPath;
		titleInfo = new LinkedHashMap();
		
		start();
	}
	
	public void run() {
		int numberOfTitles = new MPlayerInfo(inputVideo, mplayerPath).getNumberOfTitles();
		progressDialog.setMaximum(numberOfTitles);
		
		for (int i = 1; i <= numberOfTitles; i++) {
			progressDialog.setCurrent(i);
			MPlayerInfo rawTitleInfo = new MPlayerInfo(new InputVideo("dvd://" + i, inputVideo.getName()), mplayerPath);
			
			int length = rawTitleInfo.getLength();
			int seconds = length % 60;
			int minutes = length / 60;
			int hours = minutes / 60;
			minutes = minutes - (hours * 60);
			
			String title = i + ". " + hours + ":" + ((minutes < 10) ? "0" + minutes : "" + minutes) + ":" + ((seconds < 10) ? "0" + seconds : "" + seconds);
			titleInfo.put(title, new DVDTitleInfo(rawTitleInfo.getNumberOfChapters(), rawTitleInfo.getAudioStreams(), rawTitleInfo.getSubtitleLanguages()));
		}
		
		progressDialog.close();
	}
	
	public Map getTitleInfo() {
		return titleInfo;
	}
}
