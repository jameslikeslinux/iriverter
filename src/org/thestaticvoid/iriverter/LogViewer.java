package org.thestaticvoid.iriverter;

import java.io.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.custom.*;

public class LogViewer implements SelectionListener {
	private static LogViewer singleton;
	private Shell shell;
	private ToolItem save;
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
		
		ToolBar toolBar = new ToolBar(shell, SWT.HORIZONTAL | SWT.FLAT);
		
		save = new ToolItem(toolBar, SWT.PUSH);
		InputStream is = getClass().getResourceAsStream("icons/save-24.png");
		save.setImage(new Image(display, is));
		save.addSelectionListener(this);
		
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
	
	public void widgetDefaultSelected(SelectionEvent e) {
		// empty
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == save) {
			FileDialog fileDialog = new FileDialog(getShell(), SWT.SAVE);
			fileDialog.setText("Output Video");
			fileDialog.setFilterExtensions(new String[]{"*.txt"});
			fileDialog.setFilterNames(new String[]{"Text Files (*.txt)"});
			String file = fileDialog.open();
			if (file != null)
				try {
					PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(new File(file))));
					
					String[] lines = Logger.getLogText().split("\n");
					for (int i = 0; i < lines.length; i++)
						out.println(lines[i]);
					
					out.close();
				} catch (IOException io) {
					Logger.logMessage("Could not write file " + file, Logger.ERROR);
				}
		}
	}
	
	public Shell getShell() {
		return shell;
	}
	
	public void close() {
		shell.dispose();
		singleton = null;
	}
	
	public void logMessage(final String message) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				text.append(message + "\n");
				
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
				
				text.setSelection(text.getCharCount());
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
