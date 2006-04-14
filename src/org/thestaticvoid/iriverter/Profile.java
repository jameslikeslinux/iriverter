package org.thestaticvoid.iriverter;

import java.io.*;
import java.util.*;

public class Profile {
	private File profileFile;

	private Profile(File profileFile) {
		this.profileFile = profileFile;
	}

	public static Profile getProfile(String profileName) {
		return new Profile(new File(Config.getPackageDataDir() + File.separator + "profiles" + File.separator + profileName + ".profile"));
	}

	public static Profile[] getAllProfiles() {
		String[] profilesStrings = new File(Config.getPackageDataDir() + File.separator + "profiles" + File.separator).list(new ProfileFilter());
		Profile[] profiles = new Profile[profilesStrings.length];

		for (int i = 0; i < profiles.length; i++)
			profiles[i] = new Profile(new File(Config.getPackageDataDir() + File.separator + "profiles" + File.separator + profilesStrings[i]));

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
	
	public String getAudioFormat() {
		String audioFormat = readOption("audioFormat");
		if (audioFormat.equals(""))
			return "mp3";
		
		return audioFormat;
	}
}
