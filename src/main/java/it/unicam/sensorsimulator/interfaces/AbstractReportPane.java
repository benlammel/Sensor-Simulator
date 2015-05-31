package it.unicam.sensorsimulator.interfaces;

import javafx.scene.layout.BorderPane;

public abstract class AbstractReportPane extends BorderPane {

	public abstract void setReport(ReportInterface report);
	
	public abstract ReportInterface getReport();

	public abstract int getWindowWidth();

	public abstract int getWindowHeight();

}
