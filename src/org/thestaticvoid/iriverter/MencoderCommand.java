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

public class MencoderCommand extends ArrayList {
	private static final long serialVersionUID = 1L;
	private InputVideo inputVideo;
	private String outputVideo;
	
	public MencoderCommand(String[] command, InputVideo inputVideo, String outputVideo) {
		this.inputVideo = inputVideo;
		this.outputVideo = outputVideo;
		
		for (int i = 0; i < command.length; i++)
			add(command[i]);
		
		inputVideo.appendToCommandList(this);
		add("-o");
		add(outputVideo);
	}
	
	public MencoderCommand(InputVideo inputVideo, String outputVideo, String mplayerPath, MPlayerInfo info, int pass) {
		this.inputVideo = inputVideo;
		this.outputVideo = outputVideo;
		
		add(mplayerPath + MPlayerInfo.MENCODER_BIN);
		
		inputVideo.appendToCommandList(this);
		add("-o");
		add(outputVideo);
		
		if (ConverterOptions.getCurrentProfile().getWrapperFormat().equals("mp4")) {
			add("-of");
			add("lavf");
			add("-lavfopts");
			add("format=mp4:i_certify_that_my_video_stream_does_not_use_b_frames");
		}
		
		add("-ovc");
		if (ConverterOptions.getCurrentProfile().getVideoFormat().equals("h264")) {
			add("x264");
			add("-x264encopts");
			add("bitrate=" + ConverterOptions.getVideoBitrate() + ":bframes=0:level_idc=13:nocabac");
		} else {
			add("xvid");
			add("-xvidencopts");
			add("bitrate=" + ConverterOptions.getVideoBitrate() + ":max_bframes=0");
		}
		
		add("-oac");
		if (ConverterOptions.getCurrentProfile().getAudioFormat().equals("aac")) {
			add("faac");
			add("-faacopts");
			add("br=" + ConverterOptions.getAudioBitrate() + ":object=1");
		} else {
			add("mp3lame");
			add("-lameopts");
			add("mode=0:cbr:br=" + ConverterOptions.getAudioBitrate());
		}
		
		double ofps = (info.getFrameRate() > ConverterOptions.getCurrentProfile().getMaxFrameRate() ? ConverterOptions.getCurrentProfile().getMaxFrameRate() : info.getFrameRate());
		if (info.getFrameRate() != ofps && info.getFrameRate() < 1000) {	// HACK: wmv always shows 1000 fps
			add("-vf-add");
			add("filmdint=io=" + ((int) Math.round(info.getFrameRate() * 1000)) + ":" + ((int) Math.round(ofps * 1000)));
		}
		
		int scaledWidth = ConverterOptions.getDimensions().getWidth();
		int scaledHeight = (info.getDimensions().getHeight() * ConverterOptions.getDimensions().getWidth()) / info.getDimensions().getWidth();
		
		if (scaledHeight > ConverterOptions.getDimensions().getHeight()) {
			scaledWidth = (scaledWidth * ConverterOptions.getDimensions().getHeight()) / scaledHeight;
			scaledHeight = ConverterOptions.getDimensions().getHeight();
		}
		
		add("-vf-add");
		if (ConverterOptions.getPanAndScan())
			add("scale=" + ((int) ((info.getDimensions().getWidth()) * (((double) ConverterOptions.getDimensions().getHeight()) / (double) info.getDimensions().getHeight()))) + ":" + ConverterOptions.getDimensions().getHeight() + ",crop=" + ConverterOptions.getDimensions().getWidth() + ":" + ConverterOptions.getDimensions().getHeight());
		else
			add("scale=" + scaledWidth + ":" + scaledHeight + ",expand=" + ConverterOptions.getDimensions().getWidth() + ":" + ConverterOptions.getDimensions().getHeight());
		
		add("-vf-add");
		add("harddup");
		
		if (ConverterOptions.getVolumeFilter().equals(VolumeFilter.VOLNORM)) {
			add("-af");
			add("volnorm");
		} else if (ConverterOptions.getVolumeFilter().equals(VolumeFilter.VOLUME)) {
			add("-af");
			add("volume=" + ConverterOptions.getGain());
		}
		
		add("-ofps");
		add("" + ofps);
		add("-srate");
		add("44100");
		
		if (!ConverterOptions.getAutoSync()) {
			add("-delay");
			add("" + (ConverterOptions.getAudioDelay() / 1000.0));
		}
	}
	
	public String toString() {
		String string = "";
		
		for (int i = 0; i < size(); i++)
			string += (String) get(i);
		
		return string;
	}
	
	public String[] toStringArray() {
		return (String[]) toArray(new String[]{});
	}
	
	public InputVideo getInputVideo() {
		return inputVideo;
	}
	
	public String getOutputVideo() {
		return outputVideo;
	}
}
