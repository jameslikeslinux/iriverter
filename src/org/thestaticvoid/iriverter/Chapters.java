package org.thestaticvoid.iriverter;

public class Chapters {
	private int firstChapter, lastChapter;
	
	public Chapters(int firstChapter, int lastChapter) {
		this.firstChapter = firstChapter;
		this.lastChapter = lastChapter;
	}
	
	public int getFirstChapter() {
		return firstChapter;
	}
	
	public String getFirstChapterPadded() {
		return (firstChapter < 10) ? "0" + firstChapter : "" + firstChapter;
	}
	
	public int getLastChapter() {
		return lastChapter;
	}
	
	public String getLastChapterPadded() {
		return (lastChapter < 10) ? "0" + lastChapter : "" + lastChapter;
	}
}
