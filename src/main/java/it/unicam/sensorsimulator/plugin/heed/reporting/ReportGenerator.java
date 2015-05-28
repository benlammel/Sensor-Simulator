package it.unicam.sensorsimulator.plugin.heed.reporting;

import it.unicam.sensorsimulator.plugin.heed.reporting.report.HeedReport;
import it.unicam.sensorsimulator.plugin.heed.reporting.report.HeedRunResults;
import javafx.concurrent.Task;

public class ReportGenerator extends Task<Report> {
	
	private Report report;
	private HeedReport runResultList;

	public ReportGenerator(HeedReport runResultList) {
		report = new Report();
		this.runResultList = runResultList;
	}

	@Override
	protected Report call() throws Exception {
		int progress = 1;
		for(HeedRunResults run : runResultList.getRunList()){
			updateProgress(progress + 1, runResultList.getRunList().size());
			report.addSubReport(run);
			progress++;
		}
		return report;
	}

}
