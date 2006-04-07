package org.thestaticvoid.iriverter;

import java.io.*;

public class Logger {
	private static PrintWriter output;
	
	private static void openLogFile() {
		try {
			output = new PrintWriter(new BufferedWriter(new FileWriter(new File(System.getProperty("user.home") + File.separator + ".iriverter.log"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void logMessage(String message) {
		if (output == null)
			openLogFile();
		
		output.println(message);
		output.flush();
	}
}
