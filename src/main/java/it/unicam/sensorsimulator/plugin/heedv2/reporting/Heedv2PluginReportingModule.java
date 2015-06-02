package it.unicam.sensorsimulator.plugin.heedv2.reporting;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressBar;
import it.unicam.sensorsimulator.interfaces.AbstractReportPane;
import it.unicam.sensorsimulator.interfaces.ReportInterface;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.report.Heedv2Report;

public class Heedv2PluginReportingModule extends AbstractReportPane {

	private ProgressBar progressBar;
	private Heedv2Report report;
	private Heedv2ReportReasoningTask reportGenerationTask;

	public Heedv2PluginReportingModule(){
		progressBar = new ProgressBar();
	}
	
	@Override
	public void setReport(ReportInterface report) {
		this.report = (Heedv2Report) report;
		setCenter(progressBar);
		reportGenerationTask = new Heedv2ReportReasoningTask(this.report, getWindowWidth(), getWindowHeight());
		generateReport();
	}
	
	private void generateReport() {
		progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(reportGenerationTask.progressProperty());
		new Thread(reportGenerationTask).start();
		reportGenerationTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
		        new EventHandler<WorkerStateEvent>() {
		    @Override
		    public void handle(WorkerStateEvent t) {
		        setCenter(reportGenerationTask.getValue());
		    }
		});
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
