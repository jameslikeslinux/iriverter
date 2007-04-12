/*
 * MencoderCommand.java
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

import java.util.*;
import java.io.*;

public class MencoderCommand {
	private String description;
	private String[] command;
	private Process proc;
	
	public MencoderCommand(String description, String[] command) {
		this.description = description;
		this.command = command;
	}
	
	public int run(ProgressDialogInfo progressDialogInfo) {
		int exitCode = 1;
		
		MencoderStreamParser inputStream = null;
		MencoderStreamParser errorStream = null;
		
		String commandStr = "";
		for (int i = 0; i < command.length; i++)
			commandStr += command[i] + " ";
		Logger.logMessage(description + " " + commandStr, Logger.INFO);
		
		try {
			proc = Runtime.getRuntime().exec(command);
			
			inputStream = new MencoderStreamParser(progressDialogInfo);
			inputStream.parse(new BufferedReader(new InputStreamReader(proc.getInputStream())));
			errorStream = new MencoderStreamParser(progressDialogInfo);
			errorStream.parse(new BufferedReader(new InputStreamReader(proc.getErrorStream())));
			
			exitCode = proc.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return exitCode;
	}
	
	public void cancel() {
		proc.destroy();
	}
	
	public static List prepareBaseCommandList(String inputVideo, String outputVideo, String mplayerPath, MPlayerInfo info, int pass) {
		List commandList = new ArrayList();
		
		commandList.add(mplayerPath + MPlayerInfo.MENCODER_BIN);
		
		commandList.add(inputVideo);
		commandList.add("-o");
		commandList.add(outputVideo);
		
		if (ConverterOptions.getCurrentProfile().getWrapperFormat().equals("mp4")) {
			commandList.add("-of");
			commandList.add("lavf");
			commandList.add("-lavfopts");
			commandList.add("format=mp4:i_certify_that_my_video_stream_does_not_use_b_frames");
		}
		
		commandList.add("-ovc");
		if (ConverterOptions.getCurrentProfile().getVideoFormat().equals("h264")) {
			commandList.add("x264");
			commandList.add("-x264encopts");
			commandList.add("bitrate=" + ConverterOptions.getVideoBitrate() + ":bframes=0:level_idc=13:nocabac");
		} else {
			commandList.add("xvid");
			commandList.add("-xvidencopts");
			commandList.add("bitrate=" + ConverterOptions.getVideoBitrate() + ":max_bframes=0");
		}
		
		commandList.add("-oac");
		if (ConverterOptions.getCurrentProfile().getAudioFormat().equals("aac")) {
			commandList.add("faac");
			commandList.add("-faacopts");
			commandList.add("br=" + ConverterOptions.getAudioBitrate() + ":object=1");
		} else {
			commandList.add("mp3lame");
			commandList.add("-lameopts");
			commandList.add("mode=0:cbr:br=" + ConverterOptions.getAudioBitrate());
		}
		
		double ofps = (info.getFrameRate() > ConverterOptions.getCurrentProfile().getMaxFrameRate() ? ConverterOptions.getCurrentProfile().getMaxFrameRate() : info.getFrameRate());
		if (info.getFrameRate() != ofps && info.getFrameRate() < 1000) {	// HACK: wmv always shows 1000 fps
			commandList.add("-vf-add");
			commandList.add("filmdint=io=" + ((int) Math.round(info.getFrameRate() * 1000)) + ":" + ((int) Math.round(ofps * 1000)));
		}
		
		int scaledWidth = ConverterOptions.getDimensions().getWidth();
		int scaledHeight = (info.getDimensions().getHeight() * ConverterOptions.getDimensions().getWidth()) / info.getDimensions().getWidth();
		
		if (scaledHeight > ConverterOptions.getDimensions().getHeight()) {
			scaledWidth = (scaledWidth * ConverterOptions.getDimensions().getHeight()) / scaledHeight;
			scaledHeight = ConverterOptions.getDimensions().getHeight();
		}
		
		commandList.add("-vf-add");
		if (ConverterOptions.getPanAndScan())
			commandList.add("scale=" + ((int) ((info.getDimensions().getWidth()) * (((double) ConverterOptions.getDimensions().getHeight()) / (double) info.getDimensions().getHeight()))) + ":" + ConverterOptions.getDimensions().getHeight() + ",crop=" + ConverterOptions.getDimensions().getWidth() + ":" + ConverterOptions.getDimensions().getHeight());
		else
			commandList.add("scale=" + scaledWidth + ":" + scaledHeight + ",expand=" + ConverterOptions.getDimensions().getWidth() + ":" + ConverterOptions.getDimensions().getHeight());
		
		commandList.add("-vf-add");
		commandList.add("harddup");
		
		if (ConverterOptions.getVolumeFilter().equals(VolumeFilter.VOLNORM)) {
			commandList.add("-af");
			commandList.add("volnorm");
		} else if (ConverterOptions.getVolumeFilter().equals(VolumeFilter.VOLUME)) {
			commandList.add("-af");
			commandList.add("volume=" + ConverterOptions.getGain());
		}
		
		commandList.add("-ofps");
		commandList.add("" + ofps);
		commandList.add("-srate");
		commandList.add("44100");
		
		if (!ConverterOptions.getAutoSync()) {
			commandList.add("-delay");
			commandList.add("" + (ConverterOptions.getAudioDelay() / 1000.0));
		}
		
		return commandList;
	}
}
