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
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(new File(System.getProperty("user.home") + File.separator + ".iriverter.log")));
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
}
