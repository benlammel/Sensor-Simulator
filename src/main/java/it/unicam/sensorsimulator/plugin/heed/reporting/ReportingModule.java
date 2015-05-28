package it.unicam.sensorsimulator.plugin.heed.reporting;

import java.util.HashMap;

import it.unicam.sensorsimulator.interfaces.AbstractReportPane;
import it.unicam.sensorsimulator.interfaces.ReportInterface;
import it.unicam.sensorsimulator.plugin.heed.reporting.report.HeedReport;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;

public class ReportingModule extends AbstractReportPane implements EventHandler<ActionEvent> {
	
	private ReportGenerator reportGeneratorTask;
	
	private Button btnGenerateReport;
	private ProgressBar progressBar;

	public ReportingModule(){
		btnGenerateReport = new Button("generate graphical report");
		btnGenerateReport.setOnAction(this);
		this.setCenter(btnGenerateReport);
		
		progressBar = new ProgressBar(0);
		setBottom(progressBar);
		
	}

//	public void addRunResults(HashMap<Integer, RunResults> runResultList) {
//		reportGeneratorTask = new ReportGenerator(runResultList);
//	}
	
	@Override
	public void handle(ActionEvent event) {
		if(event.getSource()==btnGenerateReport){
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
	}

	@Override
	public void addReport(ReportInterface report) {
//		RunResults runResults = (RunResults) report;
		
		reportGeneratorTask = new ReportGenerator((HeedReport) report);
		
	}
}
