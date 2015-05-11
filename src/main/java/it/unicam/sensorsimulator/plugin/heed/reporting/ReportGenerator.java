package it.unicam.sensorsimulator.plugin.heed.reporting;

import java.util.HashMap;
import java.util.Map.Entry;

import javafx.concurrent.Task;

public class ReportGenerator extends Task<Report> {
	
	private Report report;
	private HashMap<Integer, RunResults> runResultList;

	public ReportGenerator(HashMap<Integer, RunResults> runResultList) {
		report = new Report();
		this.runResultList = runResultList;
	}

	@Override
	protected Report call() throws Exception {
		int progress = 1;
		for(Entry<Integer, RunResults> run : runResultList.entrySet()){
			updateProgress(progress + 1, runResultList.size());
			
			report.addSubReport(run.getValue());
			
			progress++;
		}
		
		return report;
	}

}
