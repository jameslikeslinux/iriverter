package net.sourceforge.iriverter;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.*;

public class AboutDialog extends Dialog implements SelectionListener {
	private Shell shell;
	private Button credits, dismiss;
	
	public AboutDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	public void open() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("About iriverter");
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		gridLayout.numColumns = 2;
		shell.setLayout(gridLayout);
		
		Label appName = new Label(shell, SWT.NONE);
		appName.setText("iriverter 0.13");
		FontData[] fontData = appName.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		fontData[0].setHeight(fontData[0].getHeight() + 7);
		appName.setFont(new Font(getParent().getDisplay(), fontData));
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		gridData.horizontalSpan = 2;
		appName.setLayoutData(gridData);
		
		Label appDesc = new Label(shell, SWT.NONE);
		appDesc.setText("A simple video converter based on MPlayer");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		gridData.horizontalSpan = 2;
		appDesc.setLayoutData(gridData);
		
		Label appCopyright = new Label(shell, SWT.NONE);
		appCopyright.setText("Copyright \u00a9 2005 James Lee\n");
		fontData = appCopyright.getFont().getFontData();
		fontData[0].setHeight(fontData[0].getHeight() - 2);
		appCopyright.setFont(new Font(getParent().getDisplay(), fontData));
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		gridData.horizontalSpan = 2;
		appCopyright.setLayoutData(gridData);
		
		credits = new Button(shell, SWT.PUSH);
		credits.setText("Credits");
		gridData = new GridData();
		gridData.widthHint = 75;
		credits.setLayoutData(gridData);
		credits.addSelectionListener(this);
		
		dismiss = new Button(shell, SWT.PUSH);
		dismiss.setText("Close");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gridData.widthHint = 75;
		dismiss.setLayoutData(gridData);
		dismiss.addSelectionListener(this);
		
		shell.pack();
		shell.open();
		while (!shell.isDisposed())
			if (!getParent().getDisplay().readAndDispatch())
				getParent().getDisplay().sleep();
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		// empty
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == dismiss)
			shell.dispose();
	}
}
