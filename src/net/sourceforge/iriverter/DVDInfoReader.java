package net.sourceforge.iriverter;

import java.util.*;

public class DVDInfoReader extends Thread {
	private DVDProgressDialog progressDialog;
	private String drive;
	private Map titleInfo;
	
	public DVDInfoReader(DVDProgressDialog progressDialog, String drive) {
		this.progressDialog = progressDialog;
		this.drive = drive;
		titleInfo = new LinkedHashMap();
		
		start();
	}
	
	public void run() {
		int numberOfTitles = new MPlayerInfo("dvd://", drive).getNumberOfTitles();
		progressDialog.setNumberOfTitles(numberOfTitles);
		
		for (int i = 1; i <= numberOfTitles; i++) {
			progressDialog.setCurrentTitle(i);
			MPlayerInfo rawTitleInfo = new MPlayerInfo("dvd://" + i, drive);
			
			int length = rawTitleInfo.getLength();
			int seconds = length % 60;
			int minutes = length / 60;
			int hours = minutes / 60;
			minutes = minutes - (hours * 60);
			
			String title = i + ". " + hours + ":" + ((minutes < 10) ? "0" + minutes : "" + minutes) + ":" + ((seconds < 10) ? "0" + seconds : "" + seconds);
			titleInfo.put(title, new DVDTitleInfo(rawTitleInfo.getNumberOfChapters(), rawTitleInfo.getAudioStreams(), rawTitleInfo.getSubtitleLanguages()));
		}
		
		progressDialog.close();
	}
	
	public Map getTitleInfo() {
		return titleInfo;
	}
}
