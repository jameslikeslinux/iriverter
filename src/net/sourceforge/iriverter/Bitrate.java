package net.sourceforge.iriverter;

public class Bitrate {
	private int video, audio;
	
	public Bitrate(int video, int audio) {
		this.video = video;
		this.audio = audio;
	}
	
	public int getVideo() {
		return video;
	}
	
	public int getAudio() {
		return audio;
	}
}
