package net.sourceforge.iriverter;

import net.sourceforge.iriverter.regex.*;

import java.io.*;

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
			while ((inputLine = input.readLine()) != null)
				if (inputLine.indexOf("Video stream:") > -1)
					lengthLine = inputLine;
			
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