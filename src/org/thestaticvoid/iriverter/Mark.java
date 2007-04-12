/*
 * Mark.java
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
