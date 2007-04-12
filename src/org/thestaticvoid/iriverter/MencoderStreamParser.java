/*
 * MencoderStreamParser.java
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
import java.util.regex.*;

public class MencoderStreamParser extends Thread {
	private ProgressDialogInfo progressDialogInfo;
	private String status;
	private BufferedReader input;
	private String inputLine = "", lengthLine = "";
	private boolean stopReading = false;
	
	public MencoderStreamParser(ProgressDialogInfo progressDialogInfo) {
		this.progressDialogInfo = progressDialogInfo;
		status = progressDialogInfo.getStatus();
	}
	
	public void parse(BufferedReader input) {
		this.input = input;
		start();
	}
	
	public void run() {
		ProgressUpdater progressUpdater = new ProgressUpdater();
		progressUpdater.start();
		
		try {			
			while ((inputLine = input.readLine()) != null) {
				if (inputLine.indexOf("Pos:") == -1)
					Logger.logMessage(inputLine, Logger.MPLAYER);
				
				if (inputLine.indexOf("Video stream:") > -1)
					lengthLine = inputLine;
			}
			
			input.close();
		} catch (Exception e) {
			// empty
		}
		
		progressUpdater.stopUpdating();
	}
	
	public int getLength() {
		try {
			Matcher matcher = Pattern.compile("[0-9.]* secs").matcher(lengthLine);
			matcher.find();
			return (int) Double.parseDouble(matcher.group().substring(0, matcher.group().indexOf(' ')));
		} catch (Exception e) {
			return -1;
		}
	}
	
	public class ProgressUpdater extends Thread {
		private boolean stopUpdating = false;
		
		public void run() {
			while (!stopUpdating) {
				if (inputLine.indexOf("Pos:") > -1) {
					progressDialogInfo.setPercentComplete(Integer.parseInt(inputLine.substring(inputLine.indexOf("(") + 1, inputLine.indexOf("%")).trim()));
					
					String timeRemaining = inputLine.substring(inputLine.indexOf("Trem:") + 6, inputLine.indexOf("min")).trim();
					if (timeRemaining.equals("0"))
						timeRemaining = "less than a minute remaining";
					else if (timeRemaining.equals("1"))
						timeRemaining = "about " + timeRemaining + " minute remaining";
					else
						timeRemaining = "about " + timeRemaining + " minutes remaining";
					
					progressDialogInfo.setStatus(status + " at " + inputLine.substring(inputLine.indexOf(")") + 1, inputLine.indexOf("fps")).trim() + " FPS with " + timeRemaining);
				}
				
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		public void stopUpdating() {
			stopUpdating = true;
		}
	}
}