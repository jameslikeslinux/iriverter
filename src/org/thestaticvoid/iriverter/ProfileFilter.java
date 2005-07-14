package org.thestaticvoid.iriverter;

import java.io.*;

public class ProfileFilter implements FilenameFilter {
	public boolean accept(File file, String name) {
		return name.endsWith(".profile");
	}
}
