package net.sourceforge.iriverter;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class AutomaticallySplitDialog extends Dialog implements SelectionListener {
	private Shell shell;
	private boolean autoSplit;
	private int splitTime;
	private Label splitVideoEveryLabel, minutesLabel;
	private Spinner splitTimeInput;
	private Button automaticallySplit, dismiss;
	
	public static final int NO_SPLIT = Integer.MAX_VALUE;
	
	public AutomaticallySplitDialog(Shell parent, int style, boolean autoSplit, int delay) {
		super(parent, style);
		this.autoSplit = autoSplit;
		this.splitTime = delay;
	}
	
	public int open() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Automatically Split");
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		gridLayout.numColumns = 3;
		shell.setLayout(gridLayout);
		
		Label audioSyncLabel = new Label(shell, SWT.NONE);
		audioSyncLabel.setText("Automatically Split");
		FontData[] fontData = audioSyncLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		audioSyncLabel.setFont(new Font(getParent().getDisplay(), fontData));
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		audioSyncLabel.setLayoutData(gridData);
		
		automaticallySplit = new Button(shell, SWT.CHECK);
		automaticallySplit.setText("Automatically Split");
		automaticallySplit.setSelection(autoSplit);
		gridData = new GridData();
		gridData.horizontalSpan = 3;
		automaticallySplit.setLayoutData(gridData);
		automaticallySplit.addSelectionListener(this);
		
		splitVideoEveryLabel = new Label(shell, SWT.NONE);
		splitVideoEveryLabel.setText("Split video every");
		
		splitTimeInput = new Spinner(shell, SWT.BORDER);
		splitTimeInput.setSelection(Math.abs(splitTime));
		splitTimeInput.setMinimum(0);
		
		minutesLabel = new Label(shell, SWT.NONE);
		minutesLabel.setText("minutes");
		
		dismiss = new Button(shell, SWT.PUSH);
		dismiss.setText("Close");
		gridData = new GridData();
		gridData.widthHint = 75;
		gridData.horizontalSpan = 3;
		gridData.horizontalAlignment = SWT.RIGHT;
		dismiss.setLayoutData(gridData);
		dismiss.addSelectionListener(this);
		
		if (!automaticallySplit.getSelection())
			toggleSelection();
		
		shell.pack();		
		shell.open();
		while (!shell.isDisposed())
			if (!getParent().getDisplay().readAndDispatch())
				getParent().getDisplay().sleep();	
		
		return splitTime;
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		
	}
	
	public void widgetSelected(SelectionEvent e) {		
		if (e.getSource() == dismiss) {
			if (!automaticallySplit.getSelection())
				splitTime = NO_SPLIT;
			else
				splitTime = splitTimeInput.getSelection();
			
			shell.dispose();
		}
		
		if (e.getSource() == automaticallySplit)
			toggleSelection();
	}
	
	public void toggleSelection() {
		splitVideoEveryLabel.setEnabled(!splitVideoEveryLabel.getEnabled());
		splitTimeInput.setEnabled(!splitTimeInput.getEnabled());
		minutesLabel.setEnabled(!minutesLabel.getEnabled());
	}
}
