/*
 * ConverterOptions.java
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

public class ConverterOptions {
	public static final File CONF_DIR;
	static {
		if (System.getProperty("os.name").indexOf("Windows") >= 0)
			CONF_DIR = new File(System.getProperty("user.home") + File.separator + "Application Data" + File.separator + "iriverter");
		else
			CONF_DIR = new File(System.getProperty("user.home") + File.separator + ".iriverter");

		try {
			if (!CONF_DIR.exists())
				CONF_DIR.mkdirs();
		} catch (Exception e) {
			System.err.println("Could not create " + CONF_DIR);
			System.exit(1);
		}
	}
	public static final File CONF_FILE = new File(CONF_DIR + File.separator + "conf");
	
	public static String getOptionsText() {
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(CONF_FILE));
		} catch (IOException e) {
			// empty
		}
		
		String text = "";
		if (input != null) {
			try {
				String line;
				while ((line = input.readLine()) != null)
					text += line + "\n";
			} catch (IOException e) {
				Logger.logException(e);
			}
		}
		
		return text;
	}

	private static void writeOption(String option, String setting) {
		try {
			Logger.logMessage("Setting: " + option + "=" + setting, Logger.INFO);
			
			if (!CONF_FILE.exists()) {
				CONF_FILE.mkdirs();
				CONF_FILE.delete();
				CONF_FILE.createNewFile();
			}

			BufferedReader input = new BufferedReader(new FileReader(CONF_FILE));

			StringBuffer options = new StringBuffer();
			String line;

			boolean setOption = false;

			while ((line = input.readLine()) != null)
				if (line.indexOf(option + "=") > -1) {
					options.append(option + "=" + setting + "\n");
					setOption = true;
				} else
					options.append(line + "\n");

			if (!setOption)
				options.append(option + "=" + setting + "\n");

			input.close();

			PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(CONF_FILE)));			
			output.print(options.toString());
			output.close();
		} catch (IOException e) {
			// empty
		}
	}

	private static String readOption(String option) {
		String returnSetting = "";

		try {
			BufferedReader input = new BufferedReader(new FileReader(CONF_FILE));
			String line;

			while ((line = input.readLine()) != null)
				if (line.indexOf(option + "=") > -1)
					returnSetting = line.substring(line.indexOf("=") + 1);

			input.close();
		} catch (IOException e) {
			// empty
		}

		return returnSetting;
	}

	public static void setCurrentProfile(Profile profile) {
		writeOption("currentProfile", profile.getProfileName());
		writeOption("videoBitrate", "");
		writeOption("audioBitrate", "");
		writeOption("frameRate", "");
		writeOption("dimensions", "");
		writeOption("autoSplit", "");
		writeOption("splitTime", "");
	}

	public static Profile getCurrentProfile() {
		String currentProfile = readOption("currentProfile");
		if (currentProfile.equals(""))
			return Profile.getProfile("h300");

		return Profile.getProfile(currentProfile);
	}
	
	public static boolean getPanAndScan() {
		String panAndScan = readOption("panAndScan");
		if (panAndScan.equals(""))
			return false;
			
		return panAndScan.equals("true");
	}
	
	public static void setPanAndScan(boolean panAndScan) {
		writeOption("panAndScan", "" + panAndScan);
	}
	
	public static int getVideoBitrate() {
		String videoBitrate = readOption("videoBitrate");
		if (videoBitrate.equals(""))
			return getCurrentProfile().getMaxVideoBitrate();	
		
		return Integer.parseInt(videoBitrate);
	}
	
	public static void setVideoBitrate(int videoBitrate) {
		writeOption("videoBitrate", "" + videoBitrate);
	}
	
	public static int getAudioBitrate() {
		String audioBitrate = readOption("audioBitrate");
		if (audioBitrate.equals(""))
			return getCurrentProfile().getMaxAudioBitrate();
		
		return Integer.parseInt(audioBitrate);
	}
	
	public static void setAudioBitrate(int audioBitrate) {
		writeOption("audioBitrate", "" + audioBitrate);
	}

	public static Dimensions getDimensions() {
		String dimensions = readOption("dimensions");
		if (dimensions.equals(""))
			return getCurrentProfile().getDimensions()[0];

		return new Dimensions(dimensions);
	}
	
	public static void setDimensions(Dimensions dimensions) {
		writeOption("dimensions", "" + dimensions);
	}
	
	public static boolean getAutoSync() {
		String autoSync = readOption("autoSync");
		if (autoSync.equals(""))
			return true;
		
		return autoSync.equals("true");
	}
	
	public static void setAutoSync(boolean autoSync) {
		writeOption("autoSync", "" + autoSync);
	}
	
	public static int getAudioDelay() {
		String audioDelay = readOption("audioDelay");
		if (audioDelay.equals(""))
			return 0;
	
		return Integer.parseInt(audioDelay);
	}
	
	public static void setAudioDelay(int audioDelay) {
		writeOption("audioDelay", "" + audioDelay);
	}
	
	public static boolean getAutoSplit() {
		String autoSplit = readOption("autoSplit");
		if (autoSplit.equals(""))
			return getCurrentProfile().getMaxLength() > 0;
		
		return autoSplit.equals("true");
	}
	
	public static void setAutoSplit(boolean autoSplit) {
		writeOption("autoSplit", "" + autoSplit);
	}
	
	public static int getSplitTime() {
		String splitTime = readOption("splitTime");
		if (splitTime.equals(""))
			return getCurrentProfile().getMaxLength();
		
		return Integer.parseInt(splitTime);
	}
	
	public static void setSplitTime(int splitTime) {
		writeOption("splitTime", "" + splitTime);
	}
	
	public static String getVolumeFilter() {
		String volumeFilter = readOption("volumeFilter");
		if (volumeFilter.equals(""))
			return "none";
		
		return volumeFilter;			
	}
	
	public static void setVolumeFilter(String volumeFilter) {
		writeOption("volumeFilter", volumeFilter);
	}
	
	public static double getGain() {
		String gain = readOption("gain");
		if (gain.equals(""))
			return 0.0;
		
		return Double.parseDouble(gain);
	}
	
	public static void setGain(double gain) {
		writeOption("gain", "" + gain);
	}
	
	public static String getMPlayerPath() {
		String mplayerPath = readOption("mplayerPath");
		if (mplayerPath.equals("") || !new File(mplayerPath).isDirectory())
			if (System.getProperty("os.name").indexOf("Windows") >= 0)
				return ConverterOptions.CONF_DIR + File.separator + "mplayer";
			else
				return "/usr/bin";
		
		return mplayerPath;
	}
	
	public static void setMPlayerPath(String mplayerPath) {
		writeOption("mplayerPath", mplayerPath);
	}
}
