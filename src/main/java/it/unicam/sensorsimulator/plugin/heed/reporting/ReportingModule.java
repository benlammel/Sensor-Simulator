package it.unicam.sensorsimulator.plugin.heed.reporting;

import it.unicam.sensorsimulator.interfaces.AbstractReportPane;
import it.unicam.sensorsimulator.interfaces.ReportInterface;
import it.unicam.sensorsimulator.plugin.heed.reporting.report.HeedReport;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;

public class ReportingModule extends AbstractReportPane {
	
	private ReportGenerator reportGeneratorTask;
	
	private Button btnGenerateReport;
	private ProgressBar progressBar;

	private HeedReport report;

	public ReportingModule(){
		progressBar = new ProgressBar(0);
		setCenter(progressBar);
	}

	@Override
	public void setReport(ReportInterface report) {
		this.report = (HeedReport) report;
		reportGeneratorTask = new ReportGenerator(this.report);
		generateReport();
	}

	private void generateReport() {
		progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(reportGeneratorTask.progressProperty());
		new Thread(reportGeneratorTask).start();
		reportGeneratorTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
		        new EventHandler<WorkerStateEvent>() {
		    @Override
		    public void handle(WorkerStateEvent t) {
		        setCenter(reportGeneratorTask.getValue());
		    }
		});
	}

	@Override
	public int getWindowWidth() {
		return 800;
	}

	@Override
	public int getWindowHeight() {
		return 500;
	}

	@Override
	public ReportInterface getReport() {
		return null;
	}
}
