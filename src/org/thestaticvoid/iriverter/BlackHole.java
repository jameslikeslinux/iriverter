/*
 * BlackHole.java
 * Copyright (C) 2007 David Grundberg
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

public class BlackHole extends Thread {
	private InputStream stream;
	
	public static void suck(InputStream stream) {
		new BlackHole(stream).start();
	}
	
	private BlackHole(InputStream stream) {
		this.stream = stream;
	}

	public void run() {
		try {
			while (stream.read() != -1)
				stream.skip(1024);
		} catch (IOException io) {
			Logger.logException(io);
		}
	}
}
