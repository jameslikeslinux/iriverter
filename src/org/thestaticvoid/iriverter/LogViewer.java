package org.thestaticvoid.iriverter;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.custom.*;

public class LogViewer {
	private static LogViewer singleton;
	private Shell shell;
	private StyledText text;
	private java.util.List lineColors;
	
	public LogViewer() {
		if (singleton != null)
			return;
		
		lineColors = new ArrayList();
		
		Display display = Display.getDefault();
		
		shell = new Shell(display);
		shell.setText("Log Viewer");
		shell.setLayout(new GridLayout());
		
		text = new StyledText(shell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
		text.setFont(new Font(display, new FontData("monospace", 10, SWT.NORMAL)));
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		String[] lines = Logger.getLogText().split("\n");
		for (int i = 0; i < lines.length; i++)
			logMessage(lines[i]);

		shell.open();
		
		singleton = this;
		
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		shell.dispose();
		
		singleton = null;
	}
	
	public Shell getShell() {
		return shell;
	}
	
	public void logMessage(final String message) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {				
				text.append(message + "\n");
				text.setCaretOffset(text.getCharCount());
				
				if (message.charAt(0) == Logger.PREFIX[Logger.INFO].charAt(0))
					lineColors.add(new Color(Display.getDefault(), 240, 255, 126));	// light yellow
				else if (message.charAt(0) == Logger.PREFIX[Logger.ERROR].charAt(0))
					lineColors.add(new Color(Display.getDefault(), 255, 137, 126));	// light red
				else if (message.charAt(0) == Logger.PREFIX[Logger.MPLAYER].charAt(0))
					lineColors.add(new Color(Display.getDefault(), 126, 160, 255));	// light blue
				else
					lineColors.add(new Color(Display.getDefault(), 255, 255, 255));	// white
				
				for (int i = 0; i < lineColors.size(); i++)
					text.setLineBackground(i, 1, (Color) lineColors.get(i));
			}
		});
	}
	
	public void clear() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				text.setText("");
				lineColors.clear();
			}
		});
	}
	
	public static LogViewer getSingleton() {
		return singleton;
	}
}
