/*
 * InputVideo.java
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

import java.util.*;

public class InputVideo {
	private String name;
	private String[] inputVideo;
	
	public InputVideo(String file) {
		name = file;
		inputVideo = new String[]{file};
	}
	
	public InputVideo(String dvd, String dvdDrive) {
		name = dvdDrive;
		inputVideo = new String[]{dvd, "-dvd-device", dvdDrive};
	}
	
	public String[] appendToCommand(String[] command) {
		String[] newCommand = new String[command.length + inputVideo.length];
		
		for (int i = 0; i < command.length; i++)
			newCommand[i] = command[i];
		
		for (int i = command.length, j = 0; i < newCommand.length; i++, j++)
			newCommand[i] = inputVideo[j];
		
		return newCommand;
	}
	
	public void appendToCommandList(List commandList) {		
		for (int i = 0; i < inputVideo.length; i++)
			commandList.add(inputVideo[i]);
	}
	
	public String getName() {
		return name;
	}
}
