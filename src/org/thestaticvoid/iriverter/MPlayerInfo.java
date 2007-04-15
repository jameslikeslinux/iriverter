/*
 * MPlayerInfo.java
 * Copyright (C) 2005-2007 James Lee
 * Copyright (C) 2007 David Grundberg
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
	
	public MPlayerInfo(InputVideo inputVideo) throws MPlayerNotFoundException {
		this(inputVideo, MPlayerInfo.getMPlayerPath());
	}

	public MPlayerInfo(InputVideo inputVideo, String mplayerPath) {
		String[] command = inputVideo.appendToCommand(new String[]{mplayerPath + File.separator + MPLAYER_BIN, "-v", "-identify", "-vo", "null", "-ao", "null", "-frames", "1"});
		
		String commandStr = "";
		for (int i = 0; i < command.length; i++)
			commandStr += command[i] + " ";
		Logger.logMessage(commandStr, Logger.INFO);

		mplayerOutput = new StringBuffer();
		
		try {
			proc = Runtime.getRuntime().exec(command);
		} catch (IOException io) {
			Logger.logException(io);
		}
		
		// We'll need to read the error stream too, otherwise mplayer may stall.
		BlackHole.suck(proc.getErrorStream());
		
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
		Matcher matcher = Pattern.compile("ID_VIDEO_FORMAT=(.*)").matcher(mplayerOutput);

		if (matcher.find())
			return matcher.group(1);
		
		return "";
	}
	
	public int getLength() {
		Matcher matcher = Pattern.compile("ID_LENGTH=([0-9]*)").matcher(mplayerOutput);
	
		if (matcher.find())
			return Integer.parseInt(matcher.group(1));
		
		return 0;
	}
	
	public int getNumberOfTitles() {
		Matcher matcher = Pattern.compile("ID_DVD_TITLES=([0-9]*)").matcher(mplayerOutput);

		if (matcher.find())
			return Integer.parseInt(matcher.group(1));
		
		return 0;
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

		/*
		 * = Pattern.compile("audio stream: [0-9]* audio format:
		 * ac3.*").matcher(mplayerOutput); while (matcher.find()) { String
		 * stream = matcher.group(); String lang =
		 * (Integer.parseInt(stream.substring(stream.indexOf("stream: ") + 8,
		 * stream.indexOf(" audio format:"))) + 1) + ". " +
		 * stream.substring(stream.indexOf("language: ") + 10, stream.indexOf("
		 * aid:")); languages.put(lang, stream.substring(stream.indexOf(" aid: ") +
		 * 6)); }
		 */

		// Parse embedded audio streams (matroska)
		/*
		 * if (languages.size() == 1) { matcher = Pattern.compile(" -aid
		 * ([0-9]{1,})[ ,-]* -alang ([a-z]{2,})").matcher(mplayerOutput); while
		 * (matcher.find()) { String aid = matcher.group(1); String lang =
		 * matcher.group(2); languages.put(lang, aid); } }
		 */

		// Parse embedded audio streams (tested with matroska, ogg vorbis)
		if (languages.size() == 1) {
			Matcher matcher = Pattern.compile("^ID_AUDIO_ID=([0-9]+)$",	Pattern.MULTILINE).matcher(mplayerOutput);
			
			List audioIds = new ArrayList();
			while (matcher.find())
				audioIds.add(matcher.group(1));
			
			if (audioIds.size() > 1)
				for (int i = 0; i < audioIds.size(); i++) {
					String aid = (String) audioIds.get(i);
					
					// Find out audio stream language if available
					// Default string in case there is no corresponding
					// ID_AID_x_LANG (can happen)
					String description = "Unknown";
					Matcher descMatcher = Pattern.compile("^ID_AID_" + aid + "_LANG=(.*)$", Pattern.MULTILINE).matcher(mplayerOutput);
					if (descMatcher.find())
						description = descMatcher.group(1);

					// Add format info if DVD (hack)
					descMatcher = Pattern.compile("^audio stream: .* format: (.*) language: .* aid: " + aid + ".$", Pattern.MULTILINE).matcher(mplayerOutput);
					if (descMatcher.find())
						description = description + " " + descMatcher.group(1);
					description = aid + ": " + description;
					// System.out.println(aid+":---"+lang);

					languages.put(description, aid);
				}
		}

		return languages;
	}
	
	public Map getSubtitleLanguages() {
		Map languages = new LinkedHashMap();
		languages.put("None", "-1");
		
		/*
		 * Matcher matcher = Pattern.compile("[0-9]{1,} language:
		 * [a-z]{2}").matcher(mplayerOutput); while (matcher.find()) { String
		 * sub = matcher.group(); String lang =
		 * (Integer.parseInt(sub.substring(0, sub.indexOf(' '))) + 1) + ". " +
		 * sub.substring(sub.indexOf(": ") + 2); languages.put(lang,
		 * sub.substring(0, sub.indexOf(' '))); }
		 */

		/*
		 * // Parse embedded subtitles (matroska) if (languages.size() == 1) {
		 * matcher = Pattern.compile(" -sid ([0-9]{1,})[ ,-]* -slang
		 * ([a-z]{2,})").matcher(mplayerOutput); while (matcher.find()) { String
		 * sid = matcher.group(1); String lang = matcher.group(2);
		 * languages.put(lang, sid); } }
		 */
		
		// Parse embedded subtitles (tested with matroska, ogg vorbis)
		if (languages.size() == 1) {
			Matcher matcher = Pattern.compile("^ID_SUBTITLE_ID=([0-9]+)$", Pattern.MULTILINE).matcher(mplayerOutput);
			while (matcher.find()) {
				String sid = matcher.group(1);
				String description = "Unknown";

				Matcher descMatcher = Pattern.compile("^ID_SID_" + sid + "_LANG=(.*)$", Pattern.MULTILINE).matcher(mplayerOutput);
				if (descMatcher.find())
					description = descMatcher.group(1);

				// Add name if found (I've seen Matroska use these) Here's an
				// example:
				// ID_SUBTITLE_ID=0
				// ID_SID_0_NAME=Normal Subtitles
				// ID_SID_0_LANG=eng
				// [mkv] Track ID 4: subtitles (S_TEXT/SSA) "Normal Subtitles",
				// -sid 0, -slang eng
				// ID_SUBTITLE_ID=1
				// ID_SID_1_NAME=Subtitles with Karaoke
				// ID_SID_1_LANG=eng
				// [mkv] Track ID 5: subtitles (S_TEXT/SSA) "Subtitles with
				// Karaoke", -sid 1, -slang eng

				descMatcher = Pattern.compile("^ID_SID_" + sid + "_NAME=(.*)$",	Pattern.MULTILINE).matcher(mplayerOutput);
				if (descMatcher.find())
					description = description + ": " + descMatcher.group(1);

				// hack: Add matroska subtitle format info.
				String t = "^\\[mkv\\] Track ID .*: subtitles \\((.*)\\).* -sid " + sid + ",.*$";
				System.out.println("muh: " + t);
				descMatcher = Pattern.compile(t, Pattern.MULTILINE).matcher(mplayerOutput);
				if (descMatcher.find())
					description = description + " (" + descMatcher.group(1)	+ ")";

				description = sid + ": " + description;
				// System.out.println(sid+":---"+descr);

				languages.put(description, sid);
			}
		}

		return languages;
	}

	public double getFrameRate() {
		Matcher matcher = Pattern.compile("([0-9.]+) fps").matcher(mplayerOutput);

		if (!matcher.find())
			return -1;
		
		return Double.parseDouble(matcher.group(1));
	}

	public Dimensions getDimensions() {
		Matcher matcher = Pattern.compile("ID_VIDEO_WIDTH=(\\d+)").matcher(mplayerOutput);

		if (matcher.find()) {
			int width = Integer.parseInt(matcher.group(1));
			matcher = Pattern.compile("ID_VIDEO_HEIGHT=(\\d+)").matcher(mplayerOutput);
			if (matcher.find()) {
				int height = Integer.parseInt(matcher.group(1));
				matcher = Pattern.compile("Movie-Aspect is (\\d+(?:\\.\\d+)?):1 - prescaling to correct movie aspect.").matcher(mplayerOutput);
				if (matcher.find()) {
					double scale = Double.parseDouble(matcher.group(1));
					width = (int) Math.round(height * scale);
				}
				return new Dimensions(width, height);
			}
		}

		matcher = Pattern.compile("=> [0-9]*x[0-9]*").matcher(mplayerOutput);

		if (!matcher.find())
			return new Dimensions(-1, -1);
		
		matcher = Pattern.compile("=> ([0-9]*x[0-9]*)").matcher(mplayerOutput);

		if (!matcher.find())
			return new Dimensions(-1, -1);

		return new Dimensions(matcher.group(1));
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
		return getFrameRate() != -1 && getDimensions().getWidth() != -1 && getDimensions().getHeight() != -1;
	}
}
