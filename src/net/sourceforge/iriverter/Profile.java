package net.sourceforge.iriverter;

import java.io.*;

public class Profile {
	private File optionsFile;
	private String profileName;

	private Profile(File optionsFile, String profileName) {
		this.optionsFile = optionsFile;
		this.profileName = profileName;
	}

	public static Profile getProfile(String profileName) {
		return new Profile(new File("../share/iriverter/profiles/" + profileName + ".profile"), profileName);
	}

	private String readOption(String option) {
		String returnSetting = "";

		try {
			BufferedReader input = new BufferedReader(new FileReader(
					optionsFile));
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
		return profileName;
	}
	
	public String getDevice() {
		return readOption("device");
	}
	
	public int getMaxVideoBitrate() {
		return Integer.parseInt(readOption("videoBitrate"));
	}
	
	public int getMaxAudioBitrate() {
		return Integer.parseInt(readOption("audioBitrate"));
	}
	
	public Dimensions[] getDimensions() {
		String[] dimensionsStrings = readOption("dimensions").split(" ");
		Dimensions[] dimensions = new Dimensions[dimensionsStrings.length];

		for (int i = 0; i < dimensions.length; i++)
			dimensions[i] = new Dimensions(dimensionsStrings[i]);

		return dimensions;
	}
	
	public int getMaxFrameRate() {
		return Integer.parseInt(readOption("frameRate"));
	}
	
	public int getSplitTime() {
		return Integer.parseInt(readOption("splitTime"));
	}
}
