/*
 * Profile.java
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

public class Profile {
	private File profileFile;

	private Profile(File profileFile) {
		this.profileFile = profileFile;
	}

	public static Profile getProfile(String profileName) {
		return new Profile(new File(ConverterOptions.CONF_DIR + File.separator + "profiles" + File.separator + profileName + ".profile"));
	}

	public static Profile[] getAllProfiles() {
		String[] profilesStrings = new File(ConverterOptions.CONF_DIR + File.separator + "profiles" + File.separator).list(new ProfileFilter());
		Profile[] profiles = new Profile[profilesStrings.length];

		for (int i = 0; i < profiles.length; i++)
			profiles[i] = new Profile(new File(ConverterOptions.CONF_DIR + File.separator + "profiles" + File.separator + profilesStrings[i]));

		return profiles;
	}

	private String readOption(String option) {
		String returnSetting = "";

		try {
			BufferedReader input = new BufferedReader(new FileReader(profileFile));
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
	
	public String getProfileName() {
		String profileFileName = profileFile.getName();
		return profileFileName.substring(0, profileFileName.indexOf('.'));
	}
	
	public String getBrand() {
		return readOption("brand");
	}
	
	public String getDevice() {
		return readOption("device");
	}
	
	public int getMaxVideoBitrate() {
		return Integer.parseInt(readOption("maxVideoBitrate"));
	}
	
	public int getMaxAudioBitrate() {
		return Integer.parseInt(readOption("maxAudioBitrate"));
	}
	
	public Dimensions[] getDimensions() {
		String[] dimensionsTokens = readOption("dimensions").split(" ");
		Dimensions[] dimensions = new Dimensions[dimensionsTokens.length];

		for (int i = 0; i < dimensionsTokens.length; i++)
			dimensions[i] = new Dimensions(dimensionsTokens[i]);

		return dimensions;
	}
	
	public double getMaxFrameRate() {
		return Double.parseDouble(readOption("maxFrameRate"));
	}
	
	public int getMaxLength() {
		try {
			return Integer.parseInt(readOption("maxLength"));
		} catch (Exception e) {
			return 0;
		}
	}
	
	public String getWrapperFormat() {
		String wrapperFormat = readOption("wrapperFormat");
		if (wrapperFormat.equals(""))
			return "avi";
		
		return wrapperFormat;
	}
	
	public String getVideoFormat() {
		String videoFormat = readOption("videoFormat");
		if (videoFormat.equals(""))
			return "mpeg4";
		
		return videoFormat;
	}
	
	public String getAudioFormat() {
		String audioFormat = readOption("audioFormat");
		if (audioFormat.equals(""))
			return "mp3";
		
		return audioFormat;
	}
}
