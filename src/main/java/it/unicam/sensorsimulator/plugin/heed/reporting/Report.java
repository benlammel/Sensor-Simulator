package it.unicam.sensorsimulator.plugin.heed.reporting;

import it.unicam.sensorsimulator.plugin.heed.reporting.report.HeedRunResults;
import javafx.scene.control.Accordion;

public class Report extends Accordion {
	
	public Report(){
		// this.getPanes().add(protocol);
//		this.setExpandedPane(protocol);
	}

	public void addSubReport(HeedRunResults values) {
		this.getPanes().add(new SubReport(values));
	}

}
