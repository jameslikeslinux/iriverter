package net.sourceforge.iriverter;

import java.io.*;

public class VideoFileFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
		return accept(new File(dir.toString() + File.separator + name));
	}

	public boolean accept(File file) {
		String name = file.toString();
		return file.isDirectory() || name.endsWith(".avi") || name.endsWith(".AVI") || name.endsWith(".vob") || name.endsWith(".VOB") || name.endsWith(".mkv") || name.endsWith(".MKV") || name.endsWith(".mpg") || name.endsWith(".MPG") || name.endsWith(".mpeg") || name.endsWith(".MPEG") || name.endsWith(".ogm") || name.endsWith(".OGM") || name.endsWith(".mov") || name.endsWith(".MOV") || name.endsWith(".rm") || name.endsWith(".RM") || name.endsWith(".ram") || name.endsWith(".RAM") || name.endsWith(".wmv") || name.endsWith(".WMV") || name.endsWith(".asf") || name.endsWith(".ASF");
	}
}