package it.unicam.sensorsimulator.plugin.report;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressBar;
import it.unicam.sensorsimulator.interfaces.AbstractReportPane;
import it.unicam.sensorsimulator.interfaces.ReportInterface;
import it.unicam.sensorsimulator.plugin.example.reporting.ExampleReportGenerationTask;
import it.unicam.sensorsimulator.plugin.example.reporting.report.ExampleReport;

public class ExamplePluginReportingModule extends AbstractReportPane {
	
	private ProgressBar progressBar;
	private ExampleReport report;
	private ExampleReportGenerationTask reportGenerationTask;
	
	public ExamplePluginReportingModule(){
		progressBar = new ProgressBar();
	}

	@Override
	public void setReport(ReportInterface report) {
		this.report = (ExampleReport) report;
		setCenter(progressBar);
		reportGenerationTask = new ExampleReportGenerationTask(this.report);
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
