/*
 * ListOfFiles.java
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
import java.io.*;

public class ListOfFiles implements Enumeration {
    private String[] listOfFiles;
    private ProgressDialogInfo progressDialogInfo;
    private int current = 0;

    public ListOfFiles(String[] listOfFiles, ProgressDialogInfo progressDialogInfo) {
        this.listOfFiles = listOfFiles;
        this.progressDialogInfo = progressDialogInfo;
        progressDialogInfo.setPercentComplete(0);
    }

    public boolean hasMoreElements() {
        return (current < listOfFiles.length);
    }

    public Object nextElement() {
        InputStream in = null;

        if (!hasMoreElements())
            throw new NoSuchElementException();
        else {
            String nextElement = listOfFiles[current];
            progressDialogInfo.setInputVideo(new File(nextElement).getName());
            progressDialogInfo.setPercentComplete((int) (((double) current / (double) listOfFiles.length) * 100));
            current++;
            
            try {
                in = new FileInputStream(nextElement);
            } catch (FileNotFoundException e) {
                // empty
            }
        }
        
        return in;
    }
}
