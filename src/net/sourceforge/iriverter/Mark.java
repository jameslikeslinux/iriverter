package net.sourceforge.iriverter;

public class Mark {
	private int time;
	
	public static final int START_MARK = 0;
	public static final int END_MARK = Integer.MAX_VALUE;
	
	public Mark(String time) {
		int hour = Integer.parseInt(time.substring(0, time.indexOf(':')));
		time = time.substring(time.indexOf(':') + 1);
		int minute = Integer.parseInt(time.substring(0, time.indexOf(':')));
		time = time.substring(time.indexOf(':') + 1);
		int second = Integer.parseInt(time);
		
		this.time = hour * 3600 + minute * 60 + second;
	}
	
	public Mark(int time) {
		this.time = time;
	}
	
	public int getTime() {
		return time;
	}
}
