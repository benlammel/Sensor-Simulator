package it.unicam.sensorsimulator.ui.reporting;

import javafx.scene.control.ToolBar;

public class ReportToolbar extends ToolBar {

	private ReportButton btnSaveFile;
	
	public ReportToolbar(ReportViewer reportViewer) {
		btnSaveFile = new ReportButton(ReportButtons.SAVEFILE, reportViewer);
		getItems().addAll(btnSaveFile);
	}

}
