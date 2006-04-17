package org.thestaticvoid.iriverter;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.dnd.*;

import java.io.*;
import java.util.*;

public class ConverterUI implements SelectionListener, CTabFolder2Listener, DropTargetListener {
	private ConverterOptions converterOptions;
	private Display display;
	private Shell shell;
	private ToolItem convertTool, newSingleVideoTool, newDirectoryTool, newDVDTool;
	private CTabFolder tabFolder;
	private Map profileMenuItems, dimensionsMenuItems;
	private MenuItem convert, playFile, newSingleVideo, newDirectory, newDVD, advancedJobs, manualSplit, joinVideos, moveUp, moveDown, closeJob, closeAllJobs, quit, bitrate, videoSize, panAndScan, advancedOptions, audioSync, automaticallySplit, volume, contents, logViewer, about;
	private Menu videoSizeMenu;
	private DropTarget target;
	private String fileName;
	private Process proc;
	private ProgressDialog progressDialog;
	
	public ConverterUI() {
		converterOptions = new ConverterOptions(new File(System.getProperty("user.home") + File.separator + ".iriverter.conf"));
		
		display = new Display();
		
		shell = new Shell(display);
		shell.setText("iriverter");
		InputStream is = getClass().getResourceAsStream("icons/iriverter.png");
		shell.setImage(new Image(display, is));
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		shell.setLayout(gridLayout);
		
		setupMenus();
		setupToolBar();
		
		tabFolder = new CTabFolder(shell, SWT.CLOSE);
		tabFolder.setSimple(false);
		tabFolder.setBorderVisible(true);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.addSelectionListener(this);
		tabFolder.addCTabFolder2Listener(this);
		
		target = new DropTarget(shell, DND.DROP_MOVE | DND.DROP_COPY);
		target.setTransfer(new Transfer[]{FileTransfer.getInstance()});
		target.addDropListener(this);
		
		shell.setMinimumSize(400, 267);
		shell.setSize(500, 334);
		shell.open();
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		
		display.dispose();
	}
	
	public void setupMenus() {
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem jobs = new MenuItem(menu, SWT.CASCADE);
		jobs.setText("&Jobs");
		
		Menu jobsMenu = new Menu(shell, SWT.DROP_DOWN);
		jobs.setMenu(jobsMenu);
		
		convert = new MenuItem(jobsMenu, SWT.PUSH);
		convert.setText("&Convert...\tShift+Ctrl+C");
		convert.setAccelerator(SWT.SHIFT + SWT.CTRL + 'C');
		convert.addSelectionListener(this);
		
		playFile = new MenuItem(jobsMenu, SWT.PUSH);
		playFile.setText("&Play File...\tShift+Ctrl+F");
		playFile.setAccelerator(SWT.SHIFT + SWT.CTRL + 'F');
		playFile.addSelectionListener(this);
		
		new MenuItem(jobsMenu, SWT.SEPARATOR);
		
		MenuItem newJob = new MenuItem(jobsMenu, SWT.CASCADE);
		newJob.setText("&New");
		
		Menu newJobMenu = new Menu(shell, SWT.DROP_DOWN);
		newJob.setMenu(newJobMenu);
		
		newSingleVideo = new MenuItem(newJobMenu, SWT.PUSH);
		newSingleVideo.setText("&Single Video\tShift+Ctrl+S");
		newSingleVideo.setAccelerator(SWT.SHIFT + SWT.CTRL + 'S');
		newSingleVideo.addSelectionListener(this);
		
		newDirectory = new MenuItem(newJobMenu, SWT.PUSH);
		newDirectory.setText("&Directory\tShift+Ctrl+D");
		newDirectory.setAccelerator(SWT.SHIFT + SWT.CTRL + 'D');
		newDirectory.addSelectionListener(this);
		
		newDVD = new MenuItem(newJobMenu, SWT.PUSH);
		newDVD.setText("D&VD\tShift+Ctrl+V");
		newDVD.setAccelerator(SWT.SHIFT + SWT.CTRL + 'V');
		newDVD.addSelectionListener(this);
		
		advancedJobs = new MenuItem(newJobMenu, SWT.CASCADE);
		advancedJobs.setText("&Advanced");

		advancedJobs.setEnabled(converterOptions.getCurrentProfile().getWrapperFormat().equals("avi"));
		
		Menu advancedJobsMenu = new Menu(shell, SWT.DROP_DOWN);
		advancedJobs.setMenu(advancedJobsMenu);
		
		manualSplit = new MenuItem(advancedJobsMenu, SWT.PUSH);
		manualSplit.setText("Manual &Split");
		manualSplit.addSelectionListener(this);
		
		joinVideos = new MenuItem(advancedJobsMenu, SWT.PUSH);
		joinVideos.setText("&Join Videos");
		joinVideos.addSelectionListener(this);
		
		new MenuItem(jobsMenu, SWT.SEPARATOR);
		
		moveUp = new MenuItem(jobsMenu, SWT.PUSH);
		moveUp.setText("Move &Up\tPage Up");
		moveUp.setAccelerator(SWT.PAGE_UP);
		moveUp.setEnabled(false);
		moveUp.addSelectionListener(this);
		
		moveDown = new MenuItem(jobsMenu, SWT.PUSH);
		moveDown.setText("Move &Down\tPage Down");
		moveDown.setAccelerator(SWT.PAGE_DOWN);
		moveDown.setEnabled(false);
		moveDown.addSelectionListener(this);
		
		new MenuItem(jobsMenu, SWT.SEPARATOR);
		
		closeJob = new MenuItem(jobsMenu, SWT.PUSH);
		closeJob.setText("&Close\tCtrl+W");
		closeJob.setAccelerator(SWT.CTRL + 'W');
		closeJob.setEnabled(false);
		closeJob.addSelectionListener(this);
		
		closeAllJobs = new MenuItem(jobsMenu, SWT.PUSH);
		closeAllJobs.setText("&Close All\tShift+Ctrl+W");
		closeAllJobs.setAccelerator(SWT.SHIFT + SWT.CTRL + 'W');
		closeAllJobs.setEnabled(false);
		closeAllJobs.addSelectionListener(this);
		
		quit = new MenuItem(jobsMenu, SWT.PUSH);
		quit.setText("&Quit\tCtrl+Q");
		quit.setAccelerator(SWT.CTRL + 'Q');
		quit.addSelectionListener(this);
		
		MenuItem options = new MenuItem(menu, SWT.CASCADE);
		options.setText("&Options");
		
		Menu optionsMenu = new Menu(shell, SWT.DROP_DOWN);
		options.setMenu(optionsMenu);
		
		MenuItem device = new MenuItem(optionsMenu, SWT.CASCADE);
		device.setText("&Device");

		Menu deviceMenu = new Menu(shell, SWT.DROP_DOWN);
		device.setMenu(deviceMenu);
	
		profileMenuItems = new HashMap();
		Profile[] profiles = Profile.getAllProfiles();
		Profile currentProfile = converterOptions.getCurrentProfile();
		
		Map deviceToProfile = new HashMap();
		Map brandToDevices = new TreeMap();
		for (int i = 0; i < profiles.length; i++) {
			Set devices = (Set) brandToDevices.get(profiles[i].getBrand());
			if (devices == null) {
				devices = new TreeSet();
				brandToDevices.put(profiles[i].getBrand(), devices);
			}
			
			devices.add(profiles[i].getDevice());
			deviceToProfile.put(profiles[i].getDevice(), profiles[i]);
		}
		
		Iterator brandItr = brandToDevices.keySet().iterator();
		for (int i = 0; brandItr.hasNext(); i++) {
			String brand = (String) brandItr.next();
			
			MenuItem brandMenuItem = new MenuItem(deviceMenu, SWT.CASCADE);
			brandMenuItem.setText("&" + (i + 1) + " " + brand);
			
			Menu brandMenu = new Menu(shell, SWT.DROP_DOWN);
			brandMenuItem.setMenu(brandMenu);
			
			Iterator deviceItr = ((Set) brandToDevices.get(brand)).iterator();
			for (int j = 0; deviceItr.hasNext(); j++) {
				String deviceStr = (String) deviceItr.next();
				Profile profile = (Profile) deviceToProfile.get(deviceStr);
				
				MenuItem profileMenuItem = new MenuItem(brandMenu, SWT.RADIO);
				profileMenuItem.setText("&" + (j + 1) + " " + deviceStr);
				profileMenuItem.setSelection(profile.getProfileName().equals(currentProfile.getProfileName()));
				profileMenuItem.addSelectionListener(this);
				
				profileMenuItems.put(profileMenuItem, profile.getProfileName());
			}
		}
		
		new MenuItem(optionsMenu, SWT.SEPARATOR);
		
		bitrate = new MenuItem(optionsMenu, SWT.PUSH);
		bitrate.setText("&Bitrate...\tShift+Ctrl+B");
		bitrate.setAccelerator(SWT.SHIFT + SWT.CTRL + 'B');
		bitrate.addSelectionListener(this);
		
		new MenuItem(optionsMenu, SWT.SEPARATOR);
		
		videoSize = new MenuItem(optionsMenu, SWT.CASCADE);
		videoSize.setText("Video &Size");
		
		videoSizeMenu = new Menu(shell, SWT.DROP_DOWN);
		videoSize.setMenu(videoSizeMenu);

		new MenuItem(optionsMenu, SWT.SEPARATOR);
		
		panAndScan = new MenuItem(optionsMenu, SWT.CHECK);
		panAndScan.setText("&Pan and Scan\tShift+Ctrl+P");
		panAndScan.setAccelerator(SWT.SHIFT + SWT.CTRL + 'P');
		panAndScan.setSelection(converterOptions.getPanAndScan());
		panAndScan.addSelectionListener(this);
		
		advancedOptions = new MenuItem(optionsMenu, SWT.CASCADE);
		advancedOptions.setText("&Advanced");
		
		Menu advancedOptionsMenu = new Menu(shell, SWT.DROP_DOWN);
		advancedOptions.setMenu(advancedOptionsMenu);
		
		audioSync = new MenuItem(advancedOptionsMenu, SWT.PUSH);
		audioSync.setText("Audio &Sync...");
		audioSync.addSelectionListener(this);
		
		automaticallySplit = new MenuItem(advancedOptionsMenu, SWT.PUSH);
		automaticallySplit.setText("&Automatically Split...");
		automaticallySplit.addSelectionListener(this);
		
		automaticallySplit.setEnabled(converterOptions.getCurrentProfile().getWrapperFormat().equals("avi"));
		
		volume = new MenuItem(advancedOptionsMenu, SWT.PUSH);
		volume.setText("&Volume...");
		volume.addSelectionListener(this);

		MenuItem help = new MenuItem(menu, SWT.CASCADE);
		help.setText("&Help");
		
		Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
		help.setMenu(helpMenu);
		
		contents = new MenuItem(helpMenu, SWT.PUSH);
		contents.setText("&Contents\tF1");
		contents.setAccelerator(SWT.F1);
		contents.addSelectionListener(this);
		
		logViewer = new MenuItem(helpMenu, SWT.PUSH);
		logViewer.setText("&Log Viewer");
		logViewer.addSelectionListener(this);
		
		new MenuItem(helpMenu, SWT.SEPARATOR);
		
		about = new MenuItem(helpMenu, SWT.PUSH);
		about.setText("&About");
		about.addSelectionListener(this);
		
		dimensionsMenuItems = new HashMap();
		profileChanged();
	}
	
	public void setupToolBar() {		
		ToolBar toolBar = new ToolBar(shell, SWT.HORIZONTAL | SWT.FLAT);
		toolBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		convertTool = new ToolItem(toolBar, SWT.PUSH);
		InputStream is = getClass().getResourceAsStream("icons/convert-24.png");
		convertTool.setImage(new Image(display, is));
		convertTool.setToolTipText("Convert");
		convertTool.addSelectionListener(this);
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		newSingleVideoTool = new ToolItem(toolBar, SWT.PUSH);
		is = getClass().getResourceAsStream("icons/singlevideo-24.png");
		newSingleVideoTool.setImage(new Image(display, is));
		newSingleVideoTool.setToolTipText("Single Video");
		newSingleVideoTool.addSelectionListener(this);
		
		newDirectoryTool = new ToolItem(toolBar, SWT.PUSH);
		is = getClass().getResourceAsStream("icons/directory-24.png");
		newDirectoryTool.setImage(new Image(display, is));
		newDirectoryTool.setToolTipText("Directory");
		newDirectoryTool.addSelectionListener(this);
		
		newDVDTool = new ToolItem(toolBar, SWT.PUSH);
		is = getClass().getResourceAsStream("icons/dvd-24.png");
		newDVDTool.setImage(new Image(display, is));
		newDVDTool.setToolTipText("DVD");
		newDVDTool.addSelectionListener(this);
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		// empty
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == tabFolder)
			tabChanged(false);
		
		if (e.getSource() == convert || e.getSource() == convertTool) {
			java.util.List jobs = new ArrayList();
			for (int i = 0; i < tabFolder.getItemCount(); i++)
				jobs.add(tabFolder.getItem(i).getControl());
			
			progressDialog = new ProgressDialog(shell, SWT.NONE);
			Converter converter = new Converter(jobs, progressDialog, converterOptions);
			converter.start();
			progressDialog.open();
			converter.cancel();
		}
		
		if (e.getSource() == playFile) {
			FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
			fileDialog.setText("Input Video");
			fileDialog.setFilterExtensions(new String[]{"*.avi;*.vob;*.mkv;*.mpg;*.mpeg;*.ogm;*.mov;*.rm;*.ram;*.wmv;*.asf", "*.avi", "*.vob", "*.mkv", "*.mpg;*.mpeg", "*.ogm", "*.mov", "*.rm;*.ram", "*.wmv;*.asf"});
			fileDialog.setFilterNames(new String[]{"All Video Files", "AVI Video (*.avi)", "DVD Video Object (*.vob)", "Matroska Video (*.mkv)", "MPEG Video (*.mpg, *.mpeg)", "Ogg Video (*.ogm)", "Quicktime Movie (*.mov)", "Real Video (*.rm, *.ram)", "Windows Media Video (*.wmv, *.asf)"});
			String file = fileDialog.open();
	
			if (file != null) {
				try {
					proc = Runtime.getRuntime().exec(new String[]{MPlayerInfo.getMPlayerPath() + "mplayer", file});
				} catch (IOException io) {
					io.printStackTrace();
				}
			}
		}
		
		if (e.getSource() == newSingleVideo || e.getSource() == newSingleVideoTool)
			newSingleVideo();
		
		if (e.getSource() == newDirectory || e.getSource() == newDirectoryTool)
			newDirectory();
		
		if (e.getSource() == newDVD || e.getSource() == newDVDTool)
			newDVD();
		
		if (e.getSource() == manualSplit)
			newManualSplit();
		
		if (e.getSource() == joinVideos)
			newJoinVideos();
		
		if (e.getSource() == moveUp) {
			Image image = tabFolder.getSelection().getImage();
			String title = tabFolder.getSelection().getText();
			Control control = tabFolder.getSelection().getControl();
			int index = tabFolder.getSelectionIndex();
			
			tabFolder.getSelection().dispose();
			CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE, --index);
			tabItem.setImage(image);
			tabItem.setText(title);
			tabItem.setControl(control);
			((TabItemControl) control).setTabItem(tabItem);
			tabFolder.setSelection(index);
			
			tabChanged(false);
		}
		
		if (e.getSource() == moveDown) {
			Image image = tabFolder.getSelection().getImage();
			String title = tabFolder.getSelection().getText();
			Control control = tabFolder.getSelection().getControl();
			int index = tabFolder.getSelectionIndex();
			
			tabFolder.getSelection().dispose();
			CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE, ++index);
			tabItem.setImage(image);
			tabItem.setText(title);
			tabItem.setControl(control);
			((TabItemControl) control).setTabItem(tabItem);
			tabFolder.setSelection(index);
			
			tabChanged(false);
		}
		
		if (e.getSource() == closeJob) {
			tabFolder.getSelection().dispose();
			tabChanged(false);
		}
		
		if (e.getSource() == closeAllJobs) {
			CTabItem[] tabItems = tabFolder.getItems();
			for (int i = 0; i < tabItems.length; i++)
				tabItems[i].dispose();
			tabChanged(false);
		}
		
		if (e.getSource() == quit)
			shell.dispose();
	
		// Consider putting the actual profile in the map
		if (profileMenuItems.containsKey(e.getSource())) {
			MenuItem selectedMenuItem = (MenuItem) e.getSource();
			String selectedProfileName = (String) profileMenuItems.get(selectedMenuItem);
			
			if (selectedProfileName.equals(converterOptions.getCurrentProfile().getProfileName()))
				return;
			
			for (Iterator i = profileMenuItems.keySet().iterator(); i.hasNext();)
				((MenuItem) i.next()).setSelection(false);
			selectedMenuItem.setSelection(true);
			
			converterOptions.setCurrentProfile(Profile.getProfile(selectedProfileName));
			profileChanged();
		}
		
		if (e.getSource() == bitrate) {
			BitrateDialog bitrateDialog = new BitrateDialog(shell, SWT.NONE, new Bitrate(converterOptions.getCurrentProfile().getMaxVideoBitrate(), converterOptions.getCurrentProfile().getMaxAudioBitrate()), new Bitrate(converterOptions.getVideoBitrate(), converterOptions.getAudioBitrate()));
			Bitrate newBitrate = bitrateDialog.open();
			converterOptions.writeOption("videoBitrate", "" + newBitrate.getVideo());
			converterOptions.writeOption("audioBitrate", "" + newBitrate.getAudio());
		}

		if (dimensionsMenuItems.containsKey(e.getSource())) {

		}
		
		if (e.getSource() == panAndScan)
			converterOptions.writeOption("panAndScan", "" + panAndScan.getSelection());
		
		if (e.getSource() == audioSync) {
			int audioDelay = new AudioSyncDialog(shell, SWT.NONE, (converterOptions.getAutoSync()) ? AudioSyncDialog.AUTO_SYNC : converterOptions.getAudioDelay()).open();
			
			if (audioDelay == AudioSyncDialog.AUTO_SYNC) {
				converterOptions.writeOption("autoSync", "true");
				converterOptions.writeOption("audioDelay", "0");
			} else {
				converterOptions.writeOption("autoSync", "false");
				converterOptions.writeOption("audioDelay", "" + audioDelay);
			}
		}
		
		if (e.getSource() == automaticallySplit) {
			int splitTime = new AutomaticallySplitDialog(shell, SWT.NONE, converterOptions.getAutoSplit(), converterOptions.getSplitTime()).open();
			
			if (splitTime == AutomaticallySplitDialog.NO_SPLIT)
				converterOptions.writeOption("autoSplit", "false");
			else {
				converterOptions.writeOption("autoSplit", "true");
				converterOptions.writeOption("splitTime", "" + splitTime);
			}
		}
		
		if (e.getSource() == volume) {
			double volume = new VolumeDialog(shell, SWT.NONE, converterOptions.getVolumeFilter(), converterOptions.getGain()).open();
			
			if (volume == VolumeDialog.NONE)
				converterOptions.writeOption("volumeFilter", "none");
			else if (volume == VolumeDialog.VOLNORM)
				converterOptions.writeOption("volumeFilter", "volnorm");
			else {
				converterOptions.writeOption("volumeFilter", "volume");
				converterOptions.writeOption("gain", "" + volume);
			}
		}
		
		if (e.getSource() == contents) {
			String index;
			if (Config.getPackageDataDir().equals("."))
				index = "file://" + System.getProperty("user.dir") + "/doc/html/index.html";
			else
				index = "file://" + Config.getPackageDataDir() + "/doc/html/index.html";
			
			new HelpBrowser(index);
		}
		
		if (e.getSource() == logViewer) {
			if (LogViewer.getSingleton() == null)
				new LogViewer();
			else
				LogViewer.getSingleton().getShell().setActive();
		}
		
		if (e.getSource() == about)
			new AboutDialog(shell, SWT.NONE).open();
	}

	public void profileChanged() {
		for (Iterator i = dimensionsMenuItems.keySet().iterator(); i.hasNext();)
			((MenuItem) i.next()).dispose();

		dimensionsMenuItems.clear();
		
		Dimensions[] dimensions = converterOptions.getCurrentProfile().getDimensions();
		Dimensions currentDimensions = converterOptions.getDimensions();

		for (int i = 0; i < dimensions.length; i++) {
			MenuItem dimensionsMenuItem = new MenuItem(videoSizeMenu, SWT.RADIO);
			dimensionsMenuItem.setText("&" + (i + 1) + " " + dimensions[i].toString());
			dimensionsMenuItem.setSelection(dimensions[i].toString().equals(currentDimensions.toString()));
			dimensionsMenuItem.addSelectionListener(this);

			dimensionsMenuItems.put(dimensionsMenuItem, dimensions[i]);
		}
		
		advancedJobs.setEnabled(converterOptions.getCurrentProfile().getWrapperFormat().equals("avi"));
		automaticallySplit.setEnabled(converterOptions.getCurrentProfile().getWrapperFormat().equals("avi"));
	}
	
	public void close(CTabFolderEvent event) {
		tabChanged(true);
	}
	
	public void maximize(CTabFolderEvent event) {
		// empty
	}
	
	public void minimize(CTabFolderEvent event) {
		// empty
	}
	
	public void restore(CTabFolderEvent event) {
		// empty
	}
	
	public void showList(CTabFolderEvent event) {
		// empty
	}
	
	public void tabChanged(boolean closed) {
		int index = tabFolder.getSelectionIndex();
		if (closed)
			index--;
		
		if (index == -1) {
			moveUp.setEnabled(false);
			moveDown.setEnabled(false);
			closeJob.setEnabled(false);
			closeAllJobs.setEnabled(false);
		} else {
			moveUp.setEnabled(true);
			moveDown.setEnabled(true);
			closeJob.setEnabled(true);
			closeAllJobs.setEnabled(true);
			
			if (index == 0)
				moveUp.setEnabled(false);
			if (index == (tabFolder.getItemCount() - (closed ? 2 : 1)))
				moveDown.setEnabled(false);
		}
	}
	
	public void dragEnter(DropTargetEvent e) {
		
	}
	
	public void dragLeave(DropTargetEvent e) {
		
	}
	
	public void dragOperationChanged(DropTargetEvent e) {
		
	}
	
	public void dragOver(DropTargetEvent e) {
		
	}
	
	public void drop(DropTargetEvent e) {
		if (e.getSource() == target) {
			if (e.data == null) {
				e.detail = DND.DROP_NONE;
				return;
			}
			
			String[] files = (String[]) e.data;
			for (int i = 0; i < files.length; i++) {
				File file = new File(files[i]);
				
				if (file.isFile() && new VideoFileFilter().accept(file))
					newSingleVideo().setInputVideo(files[i]);
				else if (file.isDirectory())
					if (new File(files[i] + File.separator + "VIDEO_TS").exists())
						newDVD().setDrive(files[i]);
					else
						newDirectory().setInputDirectory(files[i]);
			}
		}		
	}
	
	public void dropAccept(DropTargetEvent e) {

	}
	
	private SingleVideo newSingleVideo() {
		CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
		SingleVideo singleVideo = new SingleVideo(tabFolder, SWT.NONE, tabItem, converterOptions);
		tabItem.setControl(singleVideo);
		tabFolder.setSelection(tabItem);
		tabChanged(false);
		
		return singleVideo;
	}
	
	private Directory newDirectory() {
		CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
		Directory directory = new Directory(tabFolder, SWT.NONE, tabItem, converterOptions);
		tabItem.setControl(directory);
		tabFolder.setSelection(tabItem);
		tabChanged(false);
		
		return directory;
	}
	
	private DVD newDVD() {
		DVD lastDVD = null;
		for (int i = tabFolder.getItemCount() - 1; i >= 0 && lastDVD == null; i--)
			if (tabFolder.getItem(i).getControl() instanceof DVD)
				lastDVD = (DVD) tabFolder.getItem(i).getControl();
		
		CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
		DVD dvd = new DVD(tabFolder, SWT.NONE, tabItem, converterOptions);
		tabItem.setControl(dvd);
		tabFolder.setSelection(tabItem);
		tabChanged(false);
		
		if (lastDVD != null && !lastDVD.getDrive().equals("")) {
			dvd.setDrive(lastDVD.getDrive());
			dvd.setTitleInfo(lastDVD.getTitleInfo());
		} else
			dvd.setTitleCombo();
		
		return dvd;
	}
	
	private ManualSplit newManualSplit() {
		CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
		ManualSplit manualSplit = new ManualSplit(tabFolder, SWT.NONE, tabItem, converterOptions);
		tabItem.setControl(manualSplit);
		tabFolder.setSelection(tabItem);
		tabChanged(false);
		
		return manualSplit;
	}
	
	private JoinVideos newJoinVideos() {
		CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
		JoinVideos joinVideos = new JoinVideos(tabFolder, SWT.NONE, tabItem, converterOptions);
		tabItem.setControl(joinVideos);
		tabFolder.setSelection(tabItem);
		tabChanged(false);
		
		return joinVideos;
	}
	
	public static void main(String[] args) {
		try {
			ConverterUI ui = new ConverterUI();
		} catch (Throwable e) {
			String message = "An unhandled exception occured: " + e.getMessage() + "\n\n";
			StackTraceElement[] st = e.getStackTrace();
			for (int i = 0; i < st.length; i++)
				message += st[i] + "\n";
			
			Logger.logMessage(message, Logger.ERROR);
			
			MessageBox messageBox = new MessageBox(new Shell(Display.getDefault()), SWT.ICON_ERROR | SWT.OK);
			messageBox.setText("Error");
			messageBox.setMessage(message);
			messageBox.open();
		}
	}
}
