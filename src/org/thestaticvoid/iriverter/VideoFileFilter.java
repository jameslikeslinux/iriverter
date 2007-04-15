/*
 * VideoFileFilter.java
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

import java.io.*;
import java.util.*;

public class VideoFileFilter implements FilenameFilter {
	private static Set fileTypes;
	
	public boolean accept(File dir, String name) {
		return accept(new File(dir + File.separator + name));
	}
	
	public boolean accept(File file) {
        String name = file.toString().toLowerCase();
        
        boolean match = false;
        Iterator i = fileTypes.iterator();
        while (i.hasNext() && !match)
        	if (name.endsWith(((String) i.next()).toLowerCase()))
        		match = true;
        
        return match;
	}
	
	public static void addFileType(String fileType) {
		fileTypes.add(fileType);
	}
	
	public static void removeFileType(String fileType) {
		fileTypes.remove(fileType);
	}
	
	public static String[] getFileTypes() {
		return (String[]) fileTypes.toArray(new String[]{});
	}
	
	static {
		fileTypes = new TreeSet();
		fileTypes.add(".asf");
		fileTypes.add(".avi");
		fileTypes.add(".mkv");
		fileTypes.add(".mov");
		fileTypes.add(".mp4");
		fileTypes.add(".mpeg");
		fileTypes.add(".mpg");
		fileTypes.add(".ogm");
		fileTypes.add(".rm");
		fileTypes.add(".vob");
		fileTypes.add(".wmv");
	}
}
