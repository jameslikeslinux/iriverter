package org.thestaticvoid.iriverter;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.*;

import java.util.*;

public class ChapterDialog extends Dialog implements SelectionListener {
	private int totalChapters;
	private Chapters[] chapters;
	private Shell shell;
	private Table chaptersTable;
	private Button all, none, oneFile, dismiss;
	private boolean[] chaptersTableAfterDispose;
	private boolean oneFileAfterDispose;
	
	public ChapterDialog(Shell shell, int style, int totalChapters, boolean selectAll) {
		super(shell, style);
		this.totalChapters = totalChapters;
		chapters = new Chapters[]{new Chapters(1, totalChapters)};
	}
	
	public ChapterDialog(Shell shell, int style, int totalChapters, Chapters[] chapters) {
		super(shell, style);
		this.totalChapters = totalChapters;
		this.chapters = chapters;
	}
	
	public Chapters[] open() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Chapters");
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.verticalSpacing = 6;
		gridLayout.marginHeight = 12;
		gridLayout.marginWidth = 12;
		gridLayout.numColumns = 2;
		shell.setLayout(gridLayout);
		
		Label chaptersLabel = new Label(shell, SWT.NONE);
		chaptersLabel.setText("Chapters");
		FontData[] fontData = chaptersLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		chaptersLabel.setFont(new Font(getParent().getDisplay(), fontData));
		
		Composite buttonComp = new Composite(shell, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.pack = false;
		rowLayout.spacing = 6;
		buttonComp.setLayout(rowLayout);
		buttonComp.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		
		all = new Button(buttonComp, SWT.PUSH);
		all.setText("All");
		all.addSelectionListener(this);
		
		none = new Button(buttonComp, SWT.PUSH);
		none.setText("None");
		none.addSelectionListener(this);
		
		chaptersTable = new Table(shell, SWT.SINGLE | SWT.CHECK | SWT.V_SCROLL | SWT.BORDER);
		for (int i = 1; i <= totalChapters; i++) {
			TableItem chapter = new TableItem(chaptersTable, SWT.NONE);
			chapter.setText("Chapter " + i);
		}
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		chaptersTable.setLayoutData(gridData);
		chaptersTable.addSelectionListener(this);
		
		oneFile = new Button(shell, SWT.CHECK);
		oneFile.setText("One file per continuous selection");
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		oneFile.setLayoutData(gridData);
		
		dismiss = new Button(shell, SWT.PUSH);
		dismiss.setText("Close");
		gridData = new GridData();
		gridData.widthHint = 75;
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = SWT.RIGHT;
		dismiss.setLayoutData(gridData);
		dismiss.addSelectionListener(this);
		
		setSelection();
		
		shell.setSize(300, 300);
		shell.open();
		while (!shell.isDisposed())
			if (!getParent().getDisplay().readAndDispatch())
				getParent().getDisplay().sleep();
		
		if (chaptersTableAfterDispose != null)
			return getChapters();
		
		return chapters;
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		// empty
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == all)
			for (int i = 0; i < chaptersTable.getItemCount(); i++)
				chaptersTable.getItem(i).setChecked(true);
		
		if (e.getSource() == none)
			for (int i = 0; i < chaptersTable.getItemCount(); i++)
				chaptersTable.getItem(i).setChecked(false);
		
		//if (e.getSource() == chaptersTable)
			//chaptersTable.getSelection()[0].setChecked(!chaptersTable.getSelection()[0].getChecked());
		
		if (e.getSource() == dismiss) {
			boolean noneSelected = true;
			for (int i = 0; i < chaptersTable.getItemCount() && noneSelected; i++)
				noneSelected = !chaptersTable.getItem(i).getChecked();
			
			if (noneSelected) {
				MessageBox message = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
				message.setText("Empty Selection");
				message.setMessage("You must select at least one chapter.");
				message.open();
				
				return;
			}
			
			chaptersTableAfterDispose = new boolean[chaptersTable.getItemCount()];
			for (int i = 0; i < chaptersTableAfterDispose.length; i++)
				chaptersTableAfterDispose[i] = chaptersTable.getItem(i).getChecked();
			
			oneFileAfterDispose = oneFile.getSelection();
			
			shell.dispose();
		}
	}
	
	private void setSelection() {
		for (int i = 0; i < chapters.length; i++)
			for (int j = chapters[i].getFirstChapter() - 1; j < chapters[i].getLastChapter(); j++)
				chaptersTable.getItem(j).setChecked(true);
		
		boolean oneFile = false;
		for (int i = 0; i < chapters.length && !oneFile; i++)
			oneFile = chapters[i].getFirstChapter() != chapters[i].getLastChapter();
		
		this.oneFile.setSelection(oneFile);
	}
	
	private Chapters[] getChapters() {
		java.util.List chaptersList = new ArrayList();
		
		if (oneFileAfterDispose)
			for (int i = 0; i < chaptersTableAfterDispose.length; i++) {
				int selectionSize = 0;
				for (int j = i; j < chaptersTableAfterDispose.length && chaptersTableAfterDispose[j] ; j++, selectionSize++);
				if (selectionSize > 0)
					chaptersList.add(new Chapters(i + 1, i + selectionSize));
				
				i += selectionSize;
			}
		else
			for (int i = 0; i < chaptersTableAfterDispose.length; i++)
				if (chaptersTableAfterDispose[i])
					chaptersList.add(new Chapters(i + 1, i + 1));
		
		Chapters[] chapters = new Chapters[chaptersList.size()];
		for (int i = 0; i < chapters.length; i++)
			chapters[i] = (Chapters) chaptersList.get(i);
		
		if (chapters.length == 1 && chapters[0].getFirstChapter() == 1 && chapters[0].getLastChapter() == totalChapters)
			chapters = null;
		
 		return chapters;
	}
}
