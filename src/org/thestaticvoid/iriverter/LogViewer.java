package org.thestaticvoid.iriverter;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

public class LogViewer {
	private static LogViewer singleton;
	private Shell shell;
	private Text text;
	
	public LogViewer() {
		if (singleton != null)
			return;
		
		Display display = Display.getDefault();
		
		shell = new Shell(display);
		shell.setText("Log Viewer");
		shell.setLayout(new GridLayout());
		
		text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		text.setEditable(false);
		text.setText(Logger.getLogText());
		text.setFont(new Font(display, new FontData("monospace", 10, SWT.NORMAL)));
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		
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
			}
		});
	}
	
	public void clear() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				text.setText("");
			}
		});
	}
	
	public static LogViewer getSingleton() {
		return singleton;
	}
}
