package org.thestaticvoid.iriverter;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.*;

public class AudioSyncDialog extends Dialog implements SelectionListener {
	private Shell shell;
	private int delay;
	private Label bringAudioLabel, millisecondsLabel, videoLabel;
	private Spinner delayInput;
	private Button autoSync, before, after, cancel, ok;
	
	public AudioSyncDialog(Shell parent, int style) {
		super(parent, style);
		delay = ConverterOptions.getAudioDelay();
	}
	
	public void open() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Audio Sync");
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		gridLayout.numColumns = 5;
		shell.setLayout(gridLayout);
		
		Label audioSyncLabel = new Label(shell, SWT.NONE);
		audioSyncLabel.setText("Audio Sync");
		FontData[] fontData = audioSyncLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		audioSyncLabel.setFont(new Font(getParent().getDisplay(), fontData));
		GridData gridData = new GridData();
		gridData.horizontalSpan = 5;
		audioSyncLabel.setLayoutData(gridData);
		
		autoSync = new Button(shell, SWT.CHECK);
		autoSync.setText("Automatically Sync");
		autoSync.setSelection(ConverterOptions.getAutoSync());
		gridData = new GridData();
		gridData.horizontalSpan = 5;
		autoSync.setLayoutData(gridData);
		autoSync.addSelectionListener(this);
		
		bringAudioLabel = new Label(shell, SWT.NONE);
		bringAudioLabel.setText("Bring audio");
		
		delayInput = new Spinner(shell, SWT.BORDER);
		delayInput.setSelection(Math.abs(delay));
		delayInput.setMinimum(0);
		delayInput.setMaximum(1000);
		
		millisecondsLabel = new Label(shell, SWT.NONE);
		millisecondsLabel.setText("milliseconds");
		
		Composite beforeAfterGroup = new Composite(shell, SWT.NONE);		
		beforeAfterGroup.setLayout(new GridLayout());
		
		before = new Button(beforeAfterGroup, SWT.RADIO);
		before.setText("before");
		before.setSelection(delay <= 0);
		
		after = new Button(beforeAfterGroup, SWT.RADIO);
		after.setText("after");
		after.setSelection(delay > 0);
		
		videoLabel = new Label(shell, SWT.NONE);
		videoLabel.setText("video");
		
		Composite dismissComposite = new Composite(shell, SWT.NONE);
		dismissComposite.setLayout(new RowLayout());
		gridData = new GridData();
		gridData.horizontalSpan = 5;
		gridData.horizontalAlignment = SWT.RIGHT;
		dismissComposite.setLayoutData(gridData);
		
		cancel = new Button(dismissComposite, SWT.PUSH);
		cancel.setText("Cancel");
		RowData rowData = new RowData();
		rowData.width = 75;
		cancel.setLayoutData(rowData);
		cancel.addSelectionListener(this);
		
		ok = new Button(dismissComposite, SWT.PUSH);
		ok.setText("OK");
		rowData = new RowData();
		rowData.width = 75;
		ok.setLayoutData(rowData);
		ok.addSelectionListener(this);
		
		if (autoSync.getSelection()) {
			toggleSelection();
			delayInput.setSelection(0);
			before.setSelection(true);
			after.setSelection(false);
		}
		
		shell.pack();		
		shell.open();
		while (!shell.isDisposed())
			if (!getParent().getDisplay().readAndDispatch())
				getParent().getDisplay().sleep();
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == autoSync)
			toggleSelection();
		
		if (e.getSource() == cancel)
			shell.dispose();
		
		if (e.getSource() == ok) {
			if (autoSync.getSelection()) {
				ConverterOptions.setAutoSync(true);
				ConverterOptions.setAudioDelay(0);
			} else {
				delay = delayInput.getSelection();
				if (before.getSelection())
					delay = -delay;
				
				ConverterOptions.setAutoSync(false);
				ConverterOptions.setAudioDelay(delay);
			}
			
			shell.dispose();
		}
	}
	
	public void toggleSelection() {
		bringAudioLabel.setEnabled(!bringAudioLabel.getEnabled());
		delayInput.setEnabled(!delayInput.getEnabled());
		millisecondsLabel.setEnabled(!millisecondsLabel.getEnabled());
		before.setEnabled(!before.getEnabled());
		after.setEnabled(!after.getEnabled());
		videoLabel.setEnabled(!videoLabel.getEnabled());
	}
}