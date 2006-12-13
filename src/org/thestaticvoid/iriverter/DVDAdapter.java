/*
 * DVDAdapter.java
 * Copyright (C) 2005-2007 James Lee
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 * 
 * $Id$
 */
package org.thestaticvoid.iriverter;

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
