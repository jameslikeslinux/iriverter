/*
 * ProgressDialogInfo.java
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

public interface ProgressDialogInfo {
	public void complete(boolean success);
	public void setCurrentJob(int currentJob);
	public void setTotalJobs(int totalJobs);
	public void setInputVideo(String inputVideo);
	public void setOutputVideo(String outputVideo);
	public void setPercentComplete(int percentComplete);
	public void setStatus(String status);
	public String getStatus();
}
