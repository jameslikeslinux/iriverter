package net.sourceforge.iriverter;

import java.io.*;

public class ConverterOptions {
	private File optionsFile;

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
	
	public String getDevice() {
		String device = readOption("device");
		if (device.equals(""))
			return "H300 Series";
		
		return device;
	}
	
	public String getShortDevice() {
		String shortDevice = "";
		if (getDevice().equals("H300 Series"))
			shortDevice = "h300";
		if (getDevice().equals("PMP Series"))
			shortDevice = "pmp";
		if (getDevice().equals("iAudio X5"))
			shortDevice = "x5";
		
		return shortDevice;
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
		if (videoBitrate.equals("")) {
			String device = getDevice();
			
			if (device.equals("H300 Series"))
				return 500;
			if (device.equals("PMP Series"))
				return 1500;
			if (device.equals("iAudio X5"))
				return 256;
		}
		
		return Integer.parseInt(videoBitrate);
	}
	
	public int getAudioBitrate() {
		String audioBitrate = readOption("audioBitrate");
		if (audioBitrate.equals("")) {
			String device = getDevice();
			
			if (device.equals("H300 Series") || device.equals("iAudio X5"))
				return 128;
			if (device.equals("PMP Series"))
				return 192;
		}
		
		return Integer.parseInt(audioBitrate);
	}
	
	public int getWidth() {
		if (getDevice().equals("H300 Series"))
			return 220;
		if (getDevice().equals("iAudio X5"))
			return 160;
		
		String dimensions = readOption("dimensions");
		if (dimensions.equals(""))
			return 320;
		
		return Integer.parseInt(dimensions.substring(0, dimensions.indexOf('x')));
	}
	
	public int getHeight() {
		if (getDevice().equals("H300 Series"))
			return 176;
		if (getDevice().equals("iAudio X5"))
			return 128;
		
		String dimensions = readOption("dimensions");
		if (dimensions.equals(""))
			return 240;
		
		return Integer.parseInt(dimensions.substring(dimensions.indexOf('x') + 1));
	}
	
	public int getFrameRate() {
		if (getDevice().equals("H300 Series"))
			return 10;
		
		String frameRate = readOption("frameRate");
		if (frameRate.equals(""))
			return 10;
		
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
			return 60;
		
		return Integer.parseInt(splitTime);
	}
	
	public String getLastDir() {
		String lastDir = readOption("lastDir");
		if (lastDir.equals(""))
			return System.getProperty("user.home");
		
		return lastDir;
	}
}