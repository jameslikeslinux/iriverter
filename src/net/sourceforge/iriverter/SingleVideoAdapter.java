package net.sourceforge.iriverter;

public class SingleVideoAdapter implements SingleVideoInfo {
	private String inputVideo, outputVideo;
	
	public SingleVideoAdapter(String inputVideo, String outputVideo) {
		this.inputVideo = inputVideo;
		this.outputVideo = outputVideo;
	}
	
	public synchronized String getInputVideo() {
		return inputVideo;
	}
	
	public synchronized String getOutputVideo() {
		return outputVideo;		
	}
	
	public synchronized void setOutputVideo(String outputVideo) {
		this.outputVideo = outputVideo;
	}
}
