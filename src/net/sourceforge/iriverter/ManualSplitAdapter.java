package net.sourceforge.iriverter;

public class ManualSplitAdapter implements ManualSplitInfo {
	private String video;
	private Mark[] marks;
	private int part;
	
	public ManualSplitAdapter(String video, Mark[] marks, int part) {
		this.video = video;
		this.marks = marks;
		this.part = part;
	}
	
	public String getVideo() {
		return video;
	}
	
	public Mark[] getMarks() {
		return marks;
	}
	
	public int getPart() {
		return part;
	}
}
