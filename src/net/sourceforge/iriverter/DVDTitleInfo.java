package net.sourceforge.iriverter;

import java.util.*;

public class DVDTitleInfo {
	private int numberOfChapters;
	private Map audioStreams, subtitles;
	
	public DVDTitleInfo(int numberOfChapters, Map audioStreams, Map subtitles) {
		this.numberOfChapters = numberOfChapters;
		this.audioStreams = audioStreams;
		this.subtitles = subtitles;
	}
	
	public int getNumberOfChapters() {
		return numberOfChapters;
	}
	
	public Map getAudioStreams() {
		return audioStreams;
	}
	
	public Map getSubtitles() {
		return subtitles;
	}
}
