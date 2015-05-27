package it.unicam.sensorsimulator.plugin.heed.reporting;

import java.util.HashMap;

import it.unicam.sensorsimulator.plugin.heed.HeedPlugin;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;

public class ReportingModule extends BorderPane implements EventHandler<ActionEvent> {
	
	private HeedPlugin heedPlugin;
	private ReportGenerator reportGeneratorTask;
	
	private Button btnGenerateReport;
	private ProgressBar progressBar;

	public ReportingModule(HeedPlugin heedPlugin){
		this.heedPlugin = heedPlugin;
		
		
		btnGenerateReport = new Button("generate graphical report");
		btnGenerateReport.setOnAction(this);
		this.setCenter(btnGenerateReport);
		
		progressBar = new ProgressBar(0);
		setBottom(progressBar);
		
	}

	public void addRunResults(HashMap<Integer, RunResults> runResultList) {
		reportGeneratorTask = new ReportGenerator(runResultList);
		
		
//		reportGeneratorTask = new ReportGenerator(new TestResults());
		
		
		
		
//		System.out.println("Number of Runs recorded: " +runResultList.size());
	}
	
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
}
