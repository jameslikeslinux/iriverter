package org.thestaticvoid.iriverter;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

import java.io.*;

public class HelpBrowser implements SelectionListener, TitleListener, LocationListener {
	private String homeURL;
	private Shell shell;
	private ToolItem back, forward, home;
	private Browser browser;
	
	public HelpBrowser(String url) {
		homeURL = url;
		
		Display display = Display.getDefault();
		
		shell = new Shell(display);
		shell.setLayout(new GridLayout());
		
		ToolBar toolBar = new ToolBar(shell, SWT.HORIZONTAL | SWT.FLAT);
		
		back = new ToolItem(toolBar, SWT.PUSH);
		InputStream is = getClass().getResourceAsStream("icons/back-24.png");
		back.setImage(new Image(display, is));
		back.setEnabled(false);
		back.addSelectionListener(this);
		
		forward = new ToolItem(toolBar, SWT.PUSH);
		is = getClass().getResourceAsStream("icons/forward-24.png");
		forward.setImage(new Image(display, is));
		forward.setEnabled(false);
		forward.addSelectionListener(this);
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		home = new ToolItem(toolBar, SWT.PUSH);
		is = getClass().getResourceAsStream("icons/home-24.png");
		home.setImage(new Image(display, is));
		home.addSelectionListener(this);
		
		browser = new Browser(shell, SWT.BORDER);
		browser.setLayoutData(new GridData(GridData.FILL_BOTH));
		browser.addTitleListener(this);
		browser.addLocationListener(this);
		
		shell.open();
		browser.setUrl(homeURL);
		
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		shell.dispose();
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		// empty
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == back) {
			browser.back();
			back.setEnabled(browser.isBackEnabled());
			forward.setEnabled(browser.isForwardEnabled());
		}
		
		if (e.getSource() == forward) {
			browser.forward();
			back.setEnabled(browser.isBackEnabled());
			forward.setEnabled(browser.isForwardEnabled());
		}
		
		if (e.getSource() == home)
			browser.setUrl(homeURL);
	}
	
	public void changed(LocationEvent e) {
		if (e.getSource() == browser) {
			back.setEnabled(browser.isBackEnabled());
			forward.setEnabled(browser.isForwardEnabled());
		}
	}
	
	public void changing(LocationEvent e) {
		// empty
	}
	
	public void changed(TitleEvent e) {
		if (e.getSource() == browser)
			shell.setText(e.title);
	}
}
