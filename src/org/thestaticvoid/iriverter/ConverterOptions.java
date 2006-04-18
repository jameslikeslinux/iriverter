package org.thestaticvoid.iriverter;

import java.io.*;

public class ConverterOptions {
	private static File optionsFile = new File(System.getProperty("user.home") + File.separator + ".iriverter.conf");
	
	public static String getOptionsText() {
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(optionsFile));
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
				e.printStackTrace();
			}
		}
		
		return text;
	}

	public static void writeOption(String option, String setting) {
		try {
			Logger.logMessage("Setting: " + option + "=" + setting, Logger.INFO);
			
			if (!optionsFile.exists())
				optionsFile.createNewFile();

			BufferedReader input = new BufferedReader(new FileReader(optionsFile));

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

			PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(optionsFile)));			
			output.print(options.toString());
			output.close();
		} catch (IOException e) {
			// empty
		}
	}

	public static String readOption(String option) {
		String returnSetting = "";

		try {
			BufferedReader input = new BufferedReader(new FileReader(optionsFile));
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
	
	public static int getVideoBitrate() {
		String videoBitrate = readOption("videoBitrate");
		if (videoBitrate.equals(""))
			return getCurrentProfile().getMaxVideoBitrate();	
		
		return Integer.parseInt(videoBitrate);
	}
	
	public static int getAudioBitrate() {
		String audioBitrate = readOption("audioBitrate");
		if (audioBitrate.equals(""))
			return getCurrentProfile().getMaxAudioBitrate();
		
		return Integer.parseInt(audioBitrate);
	}

	public static Dimensions getDimensions() {
		String dimensions = readOption("dimensions");
		if (dimensions.equals(""))
			return getCurrentProfile().getDimensions()[0];

		return new Dimensions(dimensions);
	}
	
	public static boolean getAutoSync() {
		String autoSync = readOption("autoSync");
		if (autoSync.equals(""))
			return true;
		
		return autoSync.equals("true");
	}
	
	public static int getAudioDelay() {
		String audioDelay = readOption("audioDelay");
		if (audioDelay.equals(""))
			return 0;
	
		return Integer.parseInt(audioDelay);
	}
	
	public static boolean getAutoSplit() {
		String autoSplit = readOption("autoSplit");
		if (autoSplit.equals(""))
			return getCurrentProfile().getMaxLength() > 0;
		
		return autoSplit.equals("true");
	}
	
	public static int getSplitTime() {
		String splitTime = readOption("splitTime");
		if (splitTime.equals(""))
			return getCurrentProfile().getMaxLength();
		
		return Integer.parseInt(splitTime);
	}
	
	public static int getVolumeFilter() {
		String volumeFilter = readOption("volumeFilter");
		if (volumeFilter.equals("") || volumeFilter.equals("none"))
			return VolumeFilter.NONE;
		else if (volumeFilter.equals("volnorm"))
			return VolumeFilter.VOLNORM;
		
		return VolumeFilter.VOLUME;			
	}
	
	public static double getGain() {
		String gain = readOption("gain");
		if (gain.equals(""))
			return 0.0;
		
		return Double.parseDouble(gain);
	}
}
