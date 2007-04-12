package org.thestaticvoid.iriverter;

public class Converter extends Thread {
	private Job[] jobs;
	private ProgressDialogInfo progressDialogInfo;
	private MencoderCommand currentCommand;
	
	public Converter(Job[] jobs, ProgressDialogInfo progressDialogInfo) {
		this.jobs = jobs;
		this.progressDialogInfo = progressDialogInfo;
	}
	
	public void run() {
		for (int i = 0; i < jobs.length; i++) {
			MencoderCommand[] mencoderCommands = jobs[i].getMencoderCommands();
			for (int j = 0; j < mencoderCommands.length; j++) {
				currentCommand = mencoderCommands[j];
				currentCommand.run(progressDialogInfo);
			}
		}
	}
	
	public void cancel() {
		currentCommand.cancel();
	}
}
