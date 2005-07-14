package org.thestaticvoid.iriverter;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.*;

public class VolumeDialog extends Dialog implements SelectionListener {
	private int volumeFilter;
	private double gain, volume;
	private Shell shell;
	private Button filterNone, filterVolnorm, filterVolume, dismiss;
	private Text gainText;
	
	public static final double NONE = Double.MIN_VALUE;
	public static final double VOLNORM = Double.MAX_VALUE;
	
	public VolumeDialog(Shell parent, int style, int volumeFilter, double gain) {
		super(parent, style);
		this.volumeFilter = volumeFilter;
		this.gain = gain;
	}
	
	public double open() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Volume");
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		shell.setLayout(gridLayout);
		
		Label audioSyncLabel = new Label(shell, SWT.NONE);
		audioSyncLabel.setText("Volume");
		FontData[] fontData = audioSyncLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		audioSyncLabel.setFont(new Font(getParent().getDisplay(), fontData));
		
		Composite filterGroup = new Composite(shell, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.numColumns = 3;
		filterGroup.setLayout(gridLayout);
		
		filterNone = new Button(filterGroup, SWT.RADIO);
		filterNone.setText("Apply no volume filter");
		filterNone.setSelection(volumeFilter == VolumeFilter.NONE);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		filterNone.setLayoutData(gridData);
		
		filterVolnorm = new Button(filterGroup, SWT.RADIO);
		filterVolnorm.setText("Normalize the volume");
		filterVolnorm.setSelection(volumeFilter == VolumeFilter.VOLNORM);
		filterVolnorm.setLayoutData(gridData);
		
		filterVolume = new Button(filterGroup, SWT.RADIO);
		filterVolume.setText("Manually specify gain:");
		filterVolume.setSelection(volumeFilter == VolumeFilter.VOLUME);
		
		gainText = new Text(filterGroup, SWT.BORDER);
		// forces width large width
		gainText.setText("-200.0");
		
		new Label(filterGroup, SWT.NONE).setText("dB");
		
		dismiss = new Button(shell, SWT.PUSH);
		dismiss.setText("Close");
		gridData = new GridData();
		gridData.widthHint = 75;
		gridData.horizontalAlignment = SWT.RIGHT;
		dismiss.setLayoutData(gridData);
		dismiss.addSelectionListener(this);
		
		shell.pack();
		
		// set actual gain after packing
		gainText.setText("" + gain);
		
		shell.open();
		while (!shell.isDisposed())
			if (!getParent().getDisplay().readAndDispatch())
				getParent().getDisplay().sleep();
		
		return volume;
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		
	}
	
	public void widgetSelected(SelectionEvent e) {		
		if (e.getSource() == dismiss) {
			if (filterNone.getSelection())
				volume = NONE;
			else if (filterVolnorm.getSelection())
				volume = VOLNORM;
			else {
				try {
					volume = Double.parseDouble(gainText.getText());
					if (volume < -200.0 || volume > 60.0)
						throw new Exception();
				} catch (Exception exception) {
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
					messageBox.setText("Invalid Gain");
					messageBox.setMessage("The gain must be between -200.0 dB and 60.0 dB.");
					messageBox.open();
					
					return;
				}
			}
			
			shell.dispose();
		}
	}
}
