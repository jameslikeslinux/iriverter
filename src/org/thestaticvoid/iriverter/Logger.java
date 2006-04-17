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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void logMessage(String message, int type) {
		String[] messageLines = message.split("\n");
		message = "";
		for (int i = 0; i < messageLines.length; i++)
			message += PREFIX[type] + messageLines[i] + (i == messageLines.length - 1 ? "" : "\n");
		
		System.out.println(message);
		
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
			int read;
			byte[] buffer = new byte[4096];
			try {
				while ((read = input.read(buffer)) > -1)
					text += new String(buffer, 0, read) + "\n";
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return text;
	}
}
