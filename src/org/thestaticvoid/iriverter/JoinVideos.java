package org.thestaticvoid.iriverter;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;

public class JoinVideos extends Composite implements SelectionListener, TabItemControl, JoinVideosInfo {	
	private CTabItem tabItem;
	private java.util.List inputVideos;
	private List videosList;
	private Button up, add, remove, down, outputVideoSelect;
	private Text outputVideoInput;
	private String syncOutputVideo;
	
	public JoinVideos(Composite parent, int style, CTabItem tabItem) {
		super(parent, style);
		this.tabItem = tabItem;
		inputVideos = new java.util.ArrayList();
		
		tabItem.setText("New Join Videos");
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		gridLayout.numColumns = 3;
		setLayout(gridLayout);
		
		Label joinVideosLabel = new Label(this, SWT.NONE);
		joinVideosLabel.setText("Join Videos");
		FontData[] fontData = joinVideosLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		joinVideosLabel.setFont(new Font(getParent().getDisplay(), fontData));
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		joinVideosLabel.setLayoutData(gridData);
		
		Label inputLabel = new Label(this, SWT.NONE);
		inputLabel.setText("Input:");
		inputLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		
		videosList = new List(this, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER);
		videosList.setLayoutData(new GridData(GridData.FILL_BOTH));
		videosList.addSelectionListener(this);
		
		Composite addRemoveMoveButtonComp = new Composite(this, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		addRemoveMoveButtonComp.setLayout(gridLayout);
		addRemoveMoveButtonComp.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
		
		up = new Button(addRemoveMoveButtonComp, SWT.PUSH);
		up.setText("Up");
		up.setEnabled(false);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 75;
		up.setLayoutData(gridData);
		up.addSelectionListener(this);
		
		add = new Button(addRemoveMoveButtonComp, SWT.PUSH);
		add.setText("Add");
		add.setLayoutData(gridData);
		add.addSelectionListener(this);
		
		remove = new Button(addRemoveMoveButtonComp, SWT.PUSH);
		remove.setText("Remove");
		remove.setEnabled(false);
		remove.setLayoutData(gridData);
		remove.addSelectionListener(this);
		
		down = new Button(addRemoveMoveButtonComp, SWT.PUSH);
		down.setText("Down");
		down.setEnabled(false);
		down.setLayoutData(gridData);
		down.addSelectionListener(this);
		
		Label outputVideo = new Label(this, SWT.NONE);
		outputVideo.setText("Output:");
		
		outputVideoInput = new Text(this, SWT.BORDER);
		outputVideoInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		outputVideoSelect = new Button(this, SWT.PUSH);
		outputVideoSelect.setText("Select");
		gridData = new GridData();
		gridData.widthHint = 75;
		outputVideoSelect.setLayoutData(gridData);
		outputVideoSelect.addSelectionListener(this);
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == videosList) {
			listChanged();
		}
		
		if (e.getSource() == up) {
			int index = videosList.getSelectionIndex();
			
			if (index > 0) {
				String item = videosList.getItem(index);
				videosList.remove(index);
				videosList.add(item, index - 1);
				
				Object object = inputVideos.get(index);
				inputVideos.remove(index);
				inputVideos.add(index - 1, object);
				
				videosList.select(index - 1);
			}
			
			listChanged();
		}
		
		if (e.getSource() == add) {
			FileDialog fileDialog = new FileDialog(getShell(), SWT.OPEN);
			fileDialog.setText("Input Video");
			fileDialog.setFilterExtensions(new String[]{"*.avi"});
			fileDialog.setFilterNames(new String[]{"AVI Video (*.avi)"});
			String file = fileDialog.open();
			if (file != null) {
					videosList.add(new File(file).getName());
					inputVideos.add(file);
					
					videosList.select(videosList.getItemCount() - 1);
			}
			
			listChanged();
		}
		
		if (e.getSource() == remove) {
			int index = videosList.getSelectionIndex();
			
			if (index > -1) {
				videosList.remove(index);
				inputVideos.remove(index);
				
				videosList.select((index == videosList.getItemCount()) ? index - 1 : index);
			}
			
			listChanged();
		}
		
		if (e.getSource() == down) {
			int index = videosList.getSelectionIndex();
			
			if (index != -1 && index != videosList.getItemCount() - 1) {
				String item = videosList.getItem(index);
				videosList.remove(index);
				videosList.add(item, index + 1);
				
				Object object = inputVideos.get(index);
				inputVideos.remove(index);
				inputVideos.add(index + 1, object);
				
				videosList.select(index + 1);
			}
			
			listChanged();
		}
		
		if (e.getSource() == outputVideoSelect) {
			FileDialog fileDialog = new FileDialog(getShell(), SWT.SAVE);
			fileDialog.setText("Input Video");
			fileDialog.setFilterExtensions(new String[]{"*.avi"});
			fileDialog.setFilterNames(new String[]{"AVI Video (*.avi)"});
			String file = fileDialog.open();
			if (file != null) {
				outputVideoInput.setText(file);
				tabItem.setText(new File(file).getName());
			}
		}
	}
	
	public void listChanged() {
		int index = videosList.getSelectionIndex();
		
		if (videosList.getItemCount() <= 1) {
			up.setEnabled(false);
			down.setEnabled(false);
			
			if (videosList.getItemCount() == 0)
				remove.setEnabled(false);
			else
				remove.setEnabled(true);
		} else if (index == 0) {
			up.setEnabled(false);
			down.setEnabled(true);
		} else if (index == videosList.getItemCount() - 1) {
			up.setEnabled(true);
			down.setEnabled(false);
		} else {
			up.setEnabled(true);
			down.setEnabled(true);
		}
	}
	
	public void setTabItem(CTabItem tabItem) {
		this.tabItem = tabItem;
	}
	
	public String[] getInputVideos() {
		String[] inputVideos = new String[this.inputVideos.size()];
		for (int i = 0; i < this.inputVideos.size(); i++)
			inputVideos[i] = (String) this.inputVideos.get(i);
		
		return inputVideos;
	}
	
	public synchronized String getOutputVideo() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				syncOutputVideo = outputVideoInput.getText();
			}
		});
		
		return syncOutputVideo;
	}
	
	public synchronized void setOutputVideo(String outputVideo) {
		syncOutputVideo = outputVideo;
		
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				outputVideoInput.setText(syncOutputVideo);
			}
		});
	}
}
