package it.unicam.sensorsimulator.plugin.heedv2.reporting;

import it.unicam.sensorsimulator.interfaces.AbstractReportPane;
import it.unicam.sensorsimulator.interfaces.ReportInterface;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.report.Heedv2Report;

public class Heedv2PluginReportingModule extends AbstractReportPane {

	private Heedv2Report report;

	@Override
	public void setReport(ReportInterface report) {
		this.report = (Heedv2Report) report;
	}
	
	@Override
	public ReportInterface getReport() {
		return report;
	}

	@Override
	public int getWindowWidth() {
		return 800;
	}

	@Override
	public int getWindowHeight() {
		return 600;
	}

	

}
