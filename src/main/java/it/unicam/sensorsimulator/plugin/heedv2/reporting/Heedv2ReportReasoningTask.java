package it.unicam.sensorsimulator.plugin.heedv2.reporting;

import javafx.concurrent.Task;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.gui.Heedv2GUIReport;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.report.Heedv2Report;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.report.Heedv2RunReport;

public class Heedv2ReportReasoningTask extends Task<Heedv2GUIReport> {

	private Heedv2Report report;
	private Heedv2GUIReport guiReport;

	public Heedv2ReportReasoningTask(Heedv2Report report) {
		this.report = report;
		this.guiReport = new Heedv2GUIReport();
	}

	@Override
	protected Heedv2GUIReport call() throws Exception {
		int progress = 1;
		guiReport.addObjectives(report.getSimulationRunFile());
		
		for(Heedv2RunReport run : report.getRuns()){
			updateProgress(progress + 1, report.getRuns().size());
			guiReport.addRun(run);
			progress++;
		}
		return guiReport;
	}

}
