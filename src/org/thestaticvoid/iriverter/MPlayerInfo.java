/*
 * MPlayerInfo.java
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
import java.util.*;
import java.util.regex.*;

public class MPlayerInfo {
	public static final String MPLAYER_BIN = System.getProperty("os.name").indexOf("Windows") >= 0 ? "mplayer.exe" : "mplayer";
	public static final String MENCODER_BIN = System.getProperty("os.name").indexOf("Windows") >= 0 ? "mencoder.exe" : "mencoder";
	
	private Process proc;
	private StringBuffer mplayerOutput;
	private boolean commandFound = true;
	
	public MPlayerInfo(String video) throws MPlayerNotFoundException {
		this(video, MPlayerInfo.getMPlayerPath());
	}
	
	public MPlayerInfo(String video, String mplayerPath) {
		this(video, null, mplayerPath);
	}

	public MPlayerInfo(String video, String dvdDrive, String mplayerPath) {
		String[] command = null;
		if (dvdDrive != null)
			command = new String[]{mplayerPath + File.separator + MPLAYER_BIN, "-vo", "null", "-ao", "null", "-frames", "1", "-dvd-device", dvdDrive, video.toString(), "-v", "-identify"};
		else
			command = new String[]{mplayerPath + File.separator + MPLAYER_BIN, "-vo", "null", "-ao", "null", "-frames", "1", video.toString(), "-identify"};
		
		String commandStr = "";
		for (int i = 0; i < command.length; i++)
			commandStr += command[i] + " ";
		Logger.logMessage(commandStr, Logger.INFO);
		
		try {
			proc = Runtime.getRuntime().exec(command);
		} catch (IOException io) {
			Logger.logException(io);
		}
		
		mplayerOutput = new StringBuffer();
		
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				mplayerOutput.append(line + "\n");
				Logger.logMessage(line, Logger.MPLAYER);
			}
			
			input.close();
		} catch (IOException io) {
			Logger.logException(io);
		}
	}
	
	public String getVideoFormat() {
		Matcher matcher = Pattern.compile("ID_VIDEO_FORMAT=.*").matcher(mplayerOutput);
		matcher.find();
		String output = "";
		
		try {
			output = matcher.group();
		} catch (Exception e) {
			// empty
		}

		return output.substring(output.indexOf('=') + 1);
	}
	
	public int getLength() {
		Matcher matcher = Pattern.compile("ID_LENGTH=[0-9]*").matcher(mplayerOutput);
		matcher.find();
		String output = "";
		
		try {	
			output = matcher.group();
		} catch (Exception e) {
			// empty
		}
		
		try {
			return Integer.parseInt(output.substring(output.indexOf('=') + 1));
		} catch (Exception e) {
			return 0;
		}
	}
	
	public int getNumberOfTitles() {
		Matcher matcher = Pattern.compile("ID_DVD_TITLES=[0-9]*").matcher(mplayerOutput);
		matcher.find();
		String output = matcher.group();
	
		return Integer.parseInt(output.substring(14));
	}
	
	public int getNumberOfChapters() {
		Matcher matcher = Pattern.compile("There are [0-9]* chapters").matcher(mplayerOutput);
		matcher.find();
		String output = matcher.group();

		// originally used String.split() not available in GCJ
		return Integer.parseInt(output.substring(10, output.indexOf(" chapters")));
	}
	
	public Map getAudioStreams() {
		Map languages = new LinkedHashMap();
		
		languages.put("Default", "-1");
		
		Matcher matcher = Pattern.compile("audio stream: [0-9]* audio format: ac3.*").matcher(mplayerOutput);
		while (matcher.find()) {
			String stream = matcher.group();
			String lang = (Integer.parseInt(stream.substring(stream.indexOf("stream: ") + 8, stream.indexOf(" audio format:"))) + 1) + ". " + stream.substring(stream.indexOf("language: ") + 10, stream.indexOf(" aid:"));
			languages.put(lang, stream.substring(stream.indexOf(" aid: ") + 6));
		}
		
		return languages;
	}
	
	public Map getSubtitleLanguages() {
		Map languages = new LinkedHashMap();
		
		languages.put("None", "-1");
		
		Matcher matcher = Pattern.compile("[0-9]{1,} language: [a-z]{2}").matcher(mplayerOutput);
		while (matcher.find()) {
			String sub = matcher.group();
			String lang = (Integer.parseInt(sub.substring(0, sub.indexOf(' '))) + 1) + ". " + sub.substring(sub.indexOf(": ") + 2);
			languages.put(lang, sub.substring(0, sub.indexOf(' ')));
		}
		
		return languages;
	}

	public double getFrameRate() {
		double frameRate = 0;
		Matcher matcher = Pattern.compile("[0-9.]* fps").matcher(mplayerOutput);

		if (!matcher.find())
			return -1;
		
		frameRate = Double.parseDouble(matcher.group().substring(0,
				matcher.group().indexOf(' ')));

		return frameRate;
	}

	public Dimensions getDimensions() {
		Matcher matcher = Pattern.compile("=> [0-9]*x[0-9]*").matcher(mplayerOutput);

		if (!matcher.find())
			return new Dimensions(-1, -1);

		return new Dimensions(matcher.group().substring(matcher.group().indexOf(' ') + 1));
	}
	
	public static String getMPlayerPath() throws MPlayerNotFoundException {
		File currentDirectory = new File(ConverterOptions.getMPlayerPath());
		String[] files = currentDirectory.list();
		
		boolean foundMplayer = false, foundMencoder = false;
		for (int i = 0; i < files.length; i++) {
			if (files[i].equals("mplayer") || files[i].equals("mplayer.exe"))
				foundMplayer = true;
			if (files[i].equals("mencoder") || files[i].equals("mencoder.exe"))
				foundMencoder = true;
		}
		
		if (!foundMplayer || !foundMencoder)
			throw new MPlayerNotFoundException();
		
		return currentDirectory.getAbsolutePath() + File.separator;
	}
	
	public boolean videoSupported() {
		if (getFrameRate() == -1 || getDimensions().getWidth() == -1 || getDimensions().getHeight() == -1)
			return false;
		
		return true;
	}
}
