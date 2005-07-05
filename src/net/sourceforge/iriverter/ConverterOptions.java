package net.sourceforge.iriverter;

import java.io.*;

public class ConverterOptions {
	private File optionsFile;
	private Profile profile;

	public ConverterOptions(File optionsFile) {
		this.optionsFile = optionsFile;
	}

	public void writeOption(String option, String setting) {
		try {
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

	public String readOption(String option) {
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

	public Profile getCurrentProfile() {
		String currentProfile = readOption("currentProfile");
		if (currentProfile.equals(""))
			return Profile.getProfile("h300");

		return Profile.getProfile(currentProfile);
	}
	
	public boolean getPanAndScan() {
		String panAndScan = readOption("panAndScan");
		if (panAndScan.equals(""))
			return false;
			
		return panAndScan.equals("true");
	}
	
	public boolean getNormalizeVolume() {
		String normalizeVolume = readOption("normalizeVolume");
		if (normalizeVolume.equals(""))
			return false;
			
		return normalizeVolume.equals("true");
	}
	
	public boolean getAutomaticallySplit() {
		String automaticallySplit = readOption("automaticallySplit");
		if (automaticallySplit.equals(""))
				return true;
		
		return automaticallySplit.equals("true");
	}
	
	public int getVideoBitrate() {
		String videoBitrate = readOption("videoBitrate");
		if (videoBitrate.equals(""))
			return getCurrentProfile().getMaxVideoBitrate();	
		
		return Integer.parseInt(videoBitrate);
	}
	
	public int getAudioBitrate() {
		String audioBitrate = readOption("audioBitrate");
		if (audioBitrate.equals(""))
			return getCurrentProfile().getMaxAudioBitrate();
		
		return Integer.parseInt(audioBitrate);
	}
	
	public int getFrameRate() {
		String frameRate = readOption("frameRate");
		if (frameRate.equals(""))
			return getCurrentProfile().getMaxFramerate();
		
		return Integer.parseInt(frameRate);
	}
	
	public boolean getAutoSync() {
		String autoSync = readOption("autoSync");
		if (autoSync.equals(""))
			return true;
		
		return autoSync.equals("true");
	}
	
	public int getAudioDelay() {
		String audioDelay = readOption("audioDelay");
		if (audioDelay.equals(""))
			return 0;
		
		return Integer.parseInt(audioDelay);
	}
	
	public boolean getAutoSplit() {
		String autoSplit = readOption("autoSplit");
		if (autoSplit.equals(""))
			return true;
		
		return autoSplit.equals("true");
	}
	
	public int getSplitTime() {
		String splitTime = readOption("splitTime");
		if (splitTime.equals(""))
			getCurrentProfile().getSplitTime();
		
		return Integer.parseInt(splitTime);
	}
	
	public String getLastDir() {
		String lastDir = readOption("lastDir");
		if (lastDir.equals(""))
			return System.getProperty("user.home");
		
		return lastDir;
	}
}
