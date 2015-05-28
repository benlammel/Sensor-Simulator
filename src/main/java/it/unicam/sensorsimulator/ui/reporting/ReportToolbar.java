package it.unicam.sensorsimulator.ui.reporting;

import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;

public class ReportToolbar extends ToolBar {

	private ReportViewer reportViewer;
	private ReportButton btnLoadFile, btnSaveFile;
	
	public ReportToolbar(ReportViewer reportViewer) {
		this.reportViewer = reportViewer;

		btnLoadFile = new ReportButton(ReportButtons.LOADFILE, reportViewer);
		btnSaveFile = new ReportButton(ReportButtons.SAVEFILE, reportViewer);

		getItems().addAll(btnLoadFile,
				new Separator(),
				btnSaveFile);
	}

}
