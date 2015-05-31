package it.unicam.sensorsimulator.plugin.example.reporting.gui;

import javafx.scene.control.TabPane;
import it.unicam.sensorsimulator.plugin.example.reporting.report.ExampleRunReport;

public class ExampleGUIReport extends TabPane {
	
	public ExampleGUIReport(){
	}
	
	public void addRun(ExampleRunReport run) {
		getTabs().add(new ExampleRunTab(run));
	}
}
