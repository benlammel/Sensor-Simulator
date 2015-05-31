package it.unicam.sensorsimulator.plugin.example.reporting;

import javafx.concurrent.Task;
import it.unicam.sensorsimulator.plugin.example.reporting.gui.ExampleGUIReport;
import it.unicam.sensorsimulator.plugin.example.reporting.report.ExampleReport;
import it.unicam.sensorsimulator.plugin.example.reporting.report.ExampleRunReport;

public class ExampleReportGenerationTask extends Task<ExampleGUIReport> {

	private ExampleReport report;
	private ExampleGUIReport guiReport;

	public ExampleReportGenerationTask(ExampleReport report) {
		this.report = report;
		this.guiReport = new ExampleGUIReport();
	}

	@Override
	protected ExampleGUIReport call() throws Exception {
		int progress = 1;
		for(ExampleRunReport run : report.getRuns()){
			updateProgress(progress + 1, report.getRuns().size());
			guiReport.addRun(run);
			progress++;
		}
		return guiReport;
	}

}
