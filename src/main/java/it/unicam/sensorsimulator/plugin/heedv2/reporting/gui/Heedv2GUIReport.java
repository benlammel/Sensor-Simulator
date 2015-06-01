package it.unicam.sensorsimulator.plugin.heedv2.reporting.gui;

import javafx.scene.control.TabPane;
import it.unicam.sensorsimulator.plugin.heedv2.Heedv2SimulationRunFile;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.report.Heedv2RunReport;

public class Heedv2GUIReport extends TabPane {

	public void addRun(Heedv2RunReport run) {
		getTabs().add(new Heedv2RunTab(run));
	}

	public void addObjectives(Heedv2SimulationRunFile simulationRunFile) {
		getTabs().add(new Heedv2ObjectivesTab(simulationRunFile));
		
	}
}
