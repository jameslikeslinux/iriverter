/*
 * Converter.java
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

public class Converter extends Thread {
	private Job[] jobs;
	private ProgressDialogInfo progressDialogInfo;
	private DoSomeShit currentCommand;
	private List failures;
	
	public Converter(Job[] jobs, ProgressDialogInfo progressDialogInfo) {
		this.jobs = jobs;
		this.progressDialogInfo = progressDialogInfo;
		failures = new ArrayList();
	}
	
	public void run() {
		for (int i = 0; i < jobs.length; i++) {
			progressDialogInfo.setCurrentJob(i + 1);
			progressDialogInfo.setJobDescription(jobs[i].getDescription());
			
			try {
				DoSomeShit[] shitToDo = jobs[i].getShitToDo();
				for (int j = 0; j < shitToDo.length; j++) {
					currentCommand = shitToDo[j];
					currentCommand.run(progressDialogInfo);
				}
			} catch (FailedToDoSomeShit failed) {
				failures.add(failed.getMessage());
			}
		}
	}
	
	public void cancel() {
		currentCommand.cancel();
	}
	
	public String[] getFailures() {
		return (String[]) failures.toArray(new String[]{});
	}
}
