package org.thestaticvoid.iriverter;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.*;

public class DVDProgressDialog extends Dialog {
	private Shell shell;
	private int style, syncNumberOfTitles, syncCurrentTitle;
	private Label header, status;
	private ProgressBar progressBar;
	
	public DVDProgressDialog(Shell shell, int style) {
		super(shell, style);
	}
	
	public void open() {		
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Gathering Information About DVD");
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		shell.setLayout(gridLayout);
		
		header = new Label(shell, SWT.NONE);
		header.setText("Gathering Information About DVD");
		FontData[] fontData = header.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		fontData[0].setHeight(fontData[0].getHeight() + 4);
		header.setFont(new Font(Display.getDefault(), fontData));
		
		progressBar = new ProgressBar(shell, SWT.HORIZONTAL | SWT.SMOOTH);
		progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		status = new Label(shell, SWT.NONE);
		status.setText("Getting how many titles are on the DVD");
		fontData = status.getFont().getFontData();
		fontData[0].setStyle(SWT.ITALIC);
		status.setFont(new Font(getParent().getDisplay(), fontData));
		
		shell.pack();
		shell.setSize(400, shell.getSize().y);
		
		shell.open();
		while (!shell.isDisposed())
			if (!getParent().getDisplay().readAndDispatch())
				getParent().getDisplay().sleep();
	}
	
	public synchronized void close() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				shell.dispose();
			}
		});
	}
	
	public synchronized void setNumberOfTitles(int numberOfTitles) {
		syncNumberOfTitles = numberOfTitles;
	
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				progressBar.setMaximum(syncNumberOfTitles);
			}
		});
	}
	
	public synchronized void setCurrentTitle(int currentTitle) {
		syncCurrentTitle = currentTitle;
		
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				progressBar.setSelection(syncCurrentTitle);
				status.setText("Reading title " + syncCurrentTitle + " of " + syncNumberOfTitles);
				status.pack();
			}
		});
	}
}
