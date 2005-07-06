package org.thestaticvoid.iriverter;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.*;

public class BitrateDialog extends Dialog implements SelectionListener {
	private Shell shell;
	private Bitrate maxBitrate, currentBitrate;
	private int currentVideoBitrate, currentAudioBitrate;
	private Scale videoBitrateScale, audioBitrateScale;
	private Label currentVideoBitrateLabel, currentAudioBitrateLabel;
	private Button dismiss;
	
	public BitrateDialog(Shell parent, int style, Bitrate maxBitrate, Bitrate currentBitrate) {
		super(parent, style);
		this.maxBitrate = maxBitrate;
		this.currentBitrate = currentBitrate;
		currentVideoBitrate = currentBitrate.getVideo();
		currentAudioBitrate = currentBitrate.getAudio();
	}
	
	public Bitrate open() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Bitrate");
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		gridLayout.numColumns = 3;
		shell.setLayout(gridLayout);
		
		Label bitrateLabel = new Label(shell, SWT.NONE);
		bitrateLabel.setText("Bitrate");
		FontData[] fontData = bitrateLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		bitrateLabel.setFont(new Font(getParent().getDisplay(), fontData));
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		bitrateLabel.setLayoutData(gridData);
		
		Label videoBitrateLabel = new Label(shell, SWT.NONE);
		videoBitrateLabel.setText("Video:");
				
		videoBitrateScale = new Scale(shell, SWT.HORIZONTAL);
		videoBitrateScale.setMinimum(0);
		videoBitrateScale.setMaximum(maxBitrate.getVideo() / 2);
		videoBitrateScale.setSelection(currentVideoBitrate / 2);
		videoBitrateScale.setPageIncrement(25);
		videoBitrateScale.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		videoBitrateScale.addSelectionListener(this);
		
		currentVideoBitrateLabel = new Label(shell, SWT.NONE);
		currentVideoBitrateLabel.setText(currentVideoBitrate + " Kbps");
		
		Label audioBitrateLabel = new Label(shell, SWT.NONE);
		audioBitrateLabel.setText("Audio:");
		
		audioBitrateScale = new Scale(shell, SWT.HORIZONTAL);
		audioBitrateScale.setMinimum(0);
		audioBitrateScale.setMaximum(maxBitrate.getAudio() / 16);
		audioBitrateScale.setSelection(currentAudioBitrate / 16);
		audioBitrateScale.setPageIncrement(1);
		audioBitrateScale.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		audioBitrateScale.addSelectionListener(this);
		
		currentAudioBitrateLabel = new Label(shell, SWT.NONE);
		currentAudioBitrateLabel.setText(currentAudioBitrate + " Kbps");
		
		dismiss = new Button(shell, SWT.PUSH);
		dismiss.setText("Close");
		gridData = new GridData();
		gridData.widthHint = 75;
		gridData.horizontalSpan = 3;
		gridData.horizontalAlignment = SWT.RIGHT;
		dismiss.setLayoutData(gridData);
		dismiss.addSelectionListener(this);
		
		shell.pack();
		shell.setSize(400, shell.getSize().y);
		
		shell.open();
		while (!shell.isDisposed())
			if (!getParent().getDisplay().readAndDispatch())
				getParent().getDisplay().sleep();	
		
		return new Bitrate(currentVideoBitrate, currentAudioBitrate);
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		// empty
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == videoBitrateScale) {
			currentVideoBitrate = videoBitrateScale.getSelection() * 2;
			currentVideoBitrateLabel.setText(currentVideoBitrate + " Kbps");
			currentVideoBitrateLabel.pack();
		}
		
		if (e.getSource() == audioBitrateScale) {
			currentAudioBitrate = audioBitrateScale.getSelection() * 16;
			currentAudioBitrateLabel.setText(currentAudioBitrate + " Kbps");
			currentAudioBitrateLabel.pack();
		}
		
		if (e.getSource() == dismiss)
			shell.dispose();
	}
}
