/*
 * Chapters.java
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
