package net.sourceforge.iriverter;

public class DVDAdapter implements DVDInfo {
	private String drive, outputVideo;
	private int title, audioStream, subtitles;
	private Chapters[] chapters;
	
	public DVDAdapter(String drive, int title, Chapters[] chapters, int audioStream, int subtitles, String outputVideo) {
		this.drive = drive;
		this.title = title;
		this.chapters = chapters;
		this.audioStream = audioStream;
		this.subtitles = subtitles;
		this.outputVideo = outputVideo;
	}

	public String getDrive() {
		return drive;
	}

	public int getTitle() {
		return title;
	}

	public Chapters[] getChapters() {
		return chapters;
	}

	public int getAudioStream() {
		return audioStream;
	}	
	
	public int getSubtitles() {
		return subtitles;
	}

	public String getOutputVideo() {
		return outputVideo;
	}
	
	public synchronized void setOutputVideo(String outputVideo) {
		this.outputVideo = outputVideo;
	}
}
