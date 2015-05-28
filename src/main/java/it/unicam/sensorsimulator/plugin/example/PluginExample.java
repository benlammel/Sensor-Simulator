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
	
	public PluginExample() {
		settingsDialog = new ExampleSettingsDialog(this);
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
	public Class<?> getReportingPane() {
		return ExampleReportingModule.class;
	}
	
//	public AbstractReportPane getReportingPane() {
//		return new ExampleReportingModule();
//	}

	@Override
	public Class<?> getReportClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
