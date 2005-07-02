package net.sourceforge.iriverter;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.*;

public class FrameRateDialog extends Dialog implements SelectionListener {
	private Shell shell;
	private int maxFrameRate, currentFrameRate;
	private Scale frameRateScale;
	private Label currentFrameRateLabel;
	private Button dismiss;
	
	public FrameRateDialog(Shell parent, int style, int maxFrameRate, int currentFrameRate) {
		super(parent, style);
		this.maxFrameRate = maxFrameRate;
		this.currentFrameRate = currentFrameRate;
	}
	
	public int open() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("FrameRate");
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		gridLayout.numColumns = 2;
		shell.setLayout(gridLayout);
		
		Label FrameRateLabel = new Label(shell, SWT.NONE);
		FrameRateLabel.setText("Frame Rate");
		FontData[] fontData = FrameRateLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		FrameRateLabel.setFont(new Font(getParent().getDisplay(), fontData));
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		FrameRateLabel.setLayoutData(gridData);
				
		frameRateScale = new Scale(shell, SWT.HORIZONTAL);
		frameRateScale.setMinimum(1);
		frameRateScale.setMaximum(maxFrameRate);
		frameRateScale.setSelection(currentFrameRate);
		frameRateScale.setPageIncrement(25);
		frameRateScale.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		frameRateScale.addSelectionListener(this);
		
		currentFrameRateLabel = new Label(shell, SWT.NONE);
		currentFrameRateLabel.setText(currentFrameRate + " Kbps");
		
		dismiss = new Button(shell, SWT.PUSH);
		dismiss.setText("Close");
		gridData = new GridData();
		gridData.widthHint = 75;
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = SWT.RIGHT;
		dismiss.setLayoutData(gridData);
		dismiss.addSelectionListener(this);
		
		shell.pack();
		shell.setSize(400, shell.getSize().y);
		
		shell.open();
		while (!shell.isDisposed())
			if (!getParent().getDisplay().readAndDispatch())
				getParent().getDisplay().sleep();	
		
		return currentFrameRate;
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		// empty
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == frameRateScale) {
			currentFrameRate = frameRateScale.getSelection();
			currentFrameRateLabel.setText(currentFrameRate + " Kbps");
			currentFrameRateLabel.pack();
		}
		
		if (e.getSource() == dismiss)
			shell.dispose();
	}
}