package net.sourceforge.iriverter;

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
