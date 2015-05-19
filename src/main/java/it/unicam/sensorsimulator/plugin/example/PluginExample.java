package it.unicam.sensorsimulator.plugin.example;

import javafx.scene.Parent;
import it.unicam.sensorsimulator.interfaces.PluginInterface;
import it.unicam.sensorsimulator.interfaces.SimulationEnvironmentServices;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.plugin.example.coordinatoragent.ExampleSimulationCoordinatorAgent;
import it.unicam.sensorsimulator.plugin.example.dialog.ExampleSettingsDialog;
import it.unicam.sensorsimulator.plugin.example.reporting.ExampleReportingModule;

public class PluginExample implements PluginInterface {
	
	private ExampleSettingsDialog settingsDialog;
	private SimulationEnvironmentServices environmentServices;
	private ExampleReportingModule reportingModule;
	
	public PluginExample() {
		settingsDialog = new ExampleSettingsDialog(this);
		reportingModule = new ExampleReportingModule(this);
	}

	@Override
	public String getPluginName() {
		return "Example Plugin";
	}

	@Override
	public String getPluginVersion() {
		return "v. 0.1";
	}

	@Override
	public Parent getSettingsDialogContent() {
		return settingsDialog;
	}

	@Override
	public void refreshSettingsDialogContent() {
		settingsDialog.refreshSettingsDialogContent();
	}

	@Override
	public SimulationRunInterface generateAndReturnSimulationRunFile() {
		return settingsDialog.generateAndReturnSimulationRunFileInDialog();
	}

	@Override
	public Class<?> getSimulationRunFileClass() {
		return ExampleSimulationRunFile.class;
	}

	@Override
	public void updateSimulationRunFile(SimulationRunInterface simulationRunFile) {
		settingsDialog.updateSimulationRunFile(simulationRunFile);
	}

	@Override
	public void setSimulationEnvironmentServiceClass(
			SimulationEnvironmentServices environmentServices) {
		this.environmentServices = environmentServices;
	}

	@Override
	public Class<?> getSimulationCoordinatorAgent() {
		return ExampleSimulationCoordinatorAgent.class;
	}

	@Override
	public Parent getReportingPane() {
		return reportingModule;
	}

	@Override
	public Object getReportingHandler() {
		return reportingModule;
	}

}
