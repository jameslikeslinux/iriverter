/*
 * Logger.java
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

public class Logger {
	public static final int INFO = 0, ERROR = 1, MPLAYER = 2;
	public static final String[] PREFIX = {"--- ", "!!! ", ">>> "};
	                      
	private static PrintWriter output;
	
	private static void openLogFile() {
		try {
			output = new PrintWriter(new BufferedWriter(new FileWriter(new File(System.getProperty("user.home") + File.separator + ".iriverter.log"))));
			
			if (LogViewer.getSingleton() != null)
				LogViewer.getSingleton().clear();
			
			logMessage("iriverter " + Config.VERSION + "\n", Logger.INFO);
			logMessage("Settings:\n" + ConverterOptions.getOptionsText().trim() + "\n", Logger.INFO);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void logMessage(String message, int type) {
		String[] messageLines = message.split("\n", -1);
		if (messageLines.length > 1) {
			for (int i = 0; i < messageLines.length; i++)
				logMessage(messageLines[i], type);
			return;
		}
	
		message = PREFIX[type] + messageLines[0];
		
		System.out.println(message);
		System.out.flush();
		
		if (output == null)
			openLogFile();
		
		output.println(message);
		output.flush();
		
		if (LogViewer.getSingleton() != null)
			LogViewer.getSingleton().logMessage(message);
	}
	
	public static String getLogText() {
		InputStream input = null;
		try {
			input = new FileInputStream(new File(System.getProperty("user.home") + File.separator + ".iriverter.log"));
		} catch (IOException e) {
			// empty
		}
		
		String text = "";
		if (input != null) {
			try {
				int read;
				byte[] buffer = new byte[4096];
				while ((read = input.read(buffer)) > 0)
					text += new String(buffer, 0, read);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return text;
	}
}
