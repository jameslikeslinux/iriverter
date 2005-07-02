package net.sourceforge.iriverter;

import java.io.*;
import java.util.*;

import net.sourceforge.iriverter.regex.*;

public class MPlayerInfo {
	private Process proc;
	private StringBuffer mplayerOutput;
	private boolean commandFound = true;
	
	public MPlayerInfo(String video) {
		this(video, null);
	}

	public MPlayerInfo(String video, String dvdDrive) {
		String[] command = null;
		if (dvdDrive != null)
			command = new String[]{MPlayerInfo.getMPlayerPath() + "mplayer", "-vo", "null", "-ao", "null", "-frames", "1", "-dvd-device", dvdDrive, video.toString(), "-v", "-identify"};
		else
			command = new String[]{MPlayerInfo.getMPlayerPath() + "mplayer", "-vo", "null", "-ao", "null", "-frames", "1", video.toString(), "-identify"};
		
		try {
			proc = Runtime.getRuntime().exec(command);
		} catch (IOException io) {
			io.printStackTrace();
		}
		
		mplayerOutput = new StringBuffer();
		
		new Thread() {
			public void run() {
				try {
					BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
					String line;
					while ((line = input.readLine()) != null)
						mplayerOutput.append(line + "\n");
					
					input.close();
				} catch (IOException io) {
					io.printStackTrace();
				}
			}
		}.start();
		
		new Thread() {
			public void run() {
				try {
					BufferedReader input = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
					String line;
					while ((line = input.readLine()) != null);
					
					input.close();
				} catch (IOException io) {
					io.printStackTrace();
				}
			}
		}.start();
		
		try {
			proc.waitFor();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
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
		Matcher matcher = Pattern.compile("There are [0-9]* titles").matcher(mplayerOutput);
		matcher.find();
		String output = matcher.group();
	
		// originally used String.split() not available in GCJ
		return Integer.parseInt(output.substring(10, output.indexOf(" titles")));
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

	public int getWidth() {
		int width = 0;
		Matcher matcher = Pattern.compile("=> [0-9]*x[0-9]*").matcher(mplayerOutput);

		if (!matcher.find())
			return -1;
		
		width = Integer.parseInt(matcher.group().substring(matcher.group().indexOf(' ') + 1, matcher.group().indexOf('x')));

		return width;
	}

	public int getHeight() {
		int height = 0;
		Matcher matcher = Pattern.compile("=> [0-9]*x[0-9]*").matcher(mplayerOutput);

		if (!matcher.find())
			return -1;
		
		height = Integer.parseInt(matcher.group().substring(matcher.group().indexOf('x') + 1, matcher.group().length()));

		return height;
	}
	
	public static String getMPlayerPath() {
		File currentDirectory = new File(".");
		String[] files = currentDirectory.list();
		
		boolean foundMplayer = false, foundMencoder = false;
		for (int i = 0; i < files.length; i++) {
			if (files[i].equals("mplayer"))
				foundMplayer = true;
			if (files[i].equals("mencoder"))
				foundMencoder = true;
		}
		
		return (foundMplayer && foundMencoder) ? currentDirectory.getAbsolutePath() + File.separator : "";
	}
	
	public boolean commandFound() {
		return commandFound;
	}
	
	public boolean videoSupported() {
		if (getFrameRate() == -1 || getWidth() == -1 || getHeight() == -1)
			return false;
		
		return true;
	}
}