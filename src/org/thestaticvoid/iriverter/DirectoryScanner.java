/*
 * DirectoryScanner.java
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

public class DirectoryScanner {
	private File directory;
	private String[] fileTypes;
	private boolean doSubdirectories;
	
	public DirectoryScanner(File directory, String[] fileTypes, boolean doSubdirectories) {
		this.directory = directory;
		this.fileTypes = fileTypes;
		this.doSubdirectories = doSubdirectories;
	}
	
	private List getVideos(File directory) {
		List files = new ArrayList();
		
		String[] directoryListing = directory.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return accept(new File(dir + File.separator + name));
			}
			
			public boolean accept(File file) {
                String name = file.toString().toLowerCase();
                
                boolean match = false;
                for (int i = 0; i < fileTypes.length && !match; i++)
                	if (name.endsWith(fileTypes[i].toLowerCase()))
                		match = true;
                
                return match;
			}
		});
		
		for (int i = 0; i < directoryListing.length; i++)
			if (new File(directory + File.separator + directoryListing[i]).isDirectory() && doSubdirectories)
				files.addAll(getVideos(new File(directoryListing[i])));
			else if (new File(directory + File.separator + directoryListing[i]).isFile())
				files.add(directory + File.separator + directoryListing[i]);
		
		return files;
	}
	
	public String[] getVideos() {
		return (String[]) getVideos(directory).toArray(new String[]{});
	}
}
