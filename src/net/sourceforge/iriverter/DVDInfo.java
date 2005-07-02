package net.sourceforge.iriverter;

public interface DVDInfo extends OutputVideoInfo {
	public String getDrive();
	public int getTitle();
	public Chapters[] getChapters();
	public int getAudioStream();
	public int getSubtitles();
}
