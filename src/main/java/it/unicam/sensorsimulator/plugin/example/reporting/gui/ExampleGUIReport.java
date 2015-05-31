package it.unicam.sensorsimulator.plugin.example.reporting.gui;

import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import it.unicam.sensorsimulator.plugin.example.reporting.report.ExampleRunReport;

public class ExampleGUIReport extends TabPane {
	
	public void addRun(ExampleRunReport run) {
		getTabs().add(new ExampleRunTab(run));
	}
}
