/*
 * MencoderShit.java
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

public class MencoderShit implements ShitToDo {
	private String description;
	private MencoderCommand mencoderCommand;
	private Process proc;
	private boolean canceled;
	
	public MencoderShit(String description, MencoderCommand mencoderCommand) {
		this.description = description;
		this.mencoderCommand = mencoderCommand;
	}
	
	public void run(ProgressDialogInfo progressDialogInfo) throws FailedToDoSomeShit {
		int exitCode = 1;
		
		MencoderStreamParser inputStream = null;
		MencoderStreamParser errorStream = null;
		
		String[] command = mencoderCommand.toStringArray();
		Logger.logMessage(description + " " + mencoderCommand, Logger.INFO);
		
		progressDialogInfo.setSubdescription(description);
		
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
		
		if (exitCode > 0)
			throw new FailedToDoSomeShit(description);
		
		if (!canceled)
			splitVideo(progressDialogInfo, mencoderCommand.getOutputVideo(), ((errorStream.getLength() > -1) ? errorStream.getLength() : inputStream.getLength()));
	}
	
	public void cancel() {
		canceled = true;
		proc.destroy();
	}
	
	public void splitVideo(ProgressDialogInfo progressDialogInfo, String inputVideo, int length) throws FailedToDoSomeShit {
		if (!ConverterOptions.getAutoSplit() || length < ConverterOptions.getSplitTime() * 60)
			return;
		
		int pieces = (length / (ConverterOptions.getSplitTime() * 60)) + 1;
		for (int i = 0; i < pieces; i++) {
			try {
				String outputVideo = inputVideo.substring(0, inputVideo.lastIndexOf('.')) + ".part" + (i + 1) + ".avi";
				MencoderCommand command;
				
				if ((i + 1) == 1)
					command = new MencoderCommand(new String[]{MPlayerInfo.getMPlayerPath() + MPlayerInfo.MENCODER_BIN, "-ovc", "copy", "-oac", "copy", "-endpos", "" + (length / pieces)}, new InputVideo(inputVideo), outputVideo);
				else if ((i + 1) == pieces)
					command = new MencoderCommand(new String[]{MPlayerInfo.getMPlayerPath() + MPlayerInfo.MENCODER_BIN, "-ovc", "copy", "-oac", "copy", "-ss", "" + (length / pieces) * i}, new InputVideo(inputVideo), outputVideo);
				else
					command = new MencoderCommand(new String[]{MPlayerInfo.getMPlayerPath() + MPlayerInfo.MENCODER_BIN, "-ovc", "copy", "-oac", "copy", "-ss", "" + (length / pieces) * i, "-endpos", "" + (length / pieces)}, new InputVideo(inputVideo), outputVideo);
				
				new MencoderShit("Splitting Part " + (i + 1) + " of " + pieces, command).run(progressDialogInfo);
			} catch (MPlayerNotFoundException e) {
				// This should never happen
				e.printStackTrace();
			}			
		}
	}
	

}
