package org.thestaticvoid.iriverter;

public interface ProgressDialogInfo {
	public void complete(boolean success);
	public void setCurrentJob(int currentJob);
	public void setTotalJobs(int totalJobs);
	public void setInputVideo(String inputVideo);
	public void setOutputVideo(String outputVideo);
	public void setPercentComplete(int percentComplete);
	public void setStatus(String status);
	public String getStatus();
}
