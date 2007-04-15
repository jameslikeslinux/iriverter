/*
 * ConcatenateShit.java
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

public class ConcatenateShit implements ShitToDo {
	private String description, outputFile;
	private String[] inputFiles;
	private boolean canceled;
	
	public ConcatenateShit(String description, String[] inputFiles, String outputFile) {
		this.description = description;
		this.inputFiles = inputFiles;
		this.outputFile = outputFile;
	}
	
	public void run(final ProgressDialogInfo progressDialogInfo) throws FailedToDoSomeShit {
		try {
			progressDialogInfo.setSubdescription(description);
			progressDialogInfo.setMiscellaneous1("");
			progressDialogInfo.setMiscellaneous2("");
			
			FileOutputStream out = new FileOutputStream(outputFile);
			SequenceInputStream in = new SequenceInputStream(new Enumeration() {
				private int current = 0;
				
				public boolean hasMoreElements() {
					return current < inputFiles.length;
				}
				
				public Object nextElement() {
					InputStream in = null;
					
					if (!hasMoreElements())
						throw new NoSuchElementException();
					else {
						String nextElement = inputFiles[current];
						progressDialogInfo.setMiscellaneous1("Reading " + new File(nextElement).getName());
						progressDialogInfo.setMiscellaneous2("");
						progressDialogInfo.setPercentComplete((int) (((double) current / (double) inputFiles.length) * 100));
						current++;
						
						try {
							in = new FileInputStream(nextElement);
						} catch (IOException io) {
							Logger.logException(io);
						}
					}
					
					return in;
				}
			});
			
			int length;
			byte[] bytes = new byte[4096];
			while ((length = in.read(bytes)) != -1 && !canceled)
				out.write(bytes, 0, length);
			
			progressDialogInfo.setPercentComplete(100);
		} catch (IOException io) {
			Logger.logException(io);
			throw new FailedToDoSomeShit(description);
		}
	}
	
	public void cancel() {
		canceled = true;
	}
}
