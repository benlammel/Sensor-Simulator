package it.unicam.sensorsimulator.plugin.example;

import java.util.ArrayList;

import javafx.scene.Parent;
import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.PluginInterface;
import it.unicam.sensorsimulator.interfaces.SimulationEnvironmentServices;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.plugin.example.agent.config.ExampleAgentConfiguration;
import it.unicam.sensorsimulator.plugin.example.coordinator.ExampleSimulationCoordinatorAgent;
import it.unicam.sensorsimulator.plugin.example.reporting.report.ExampleReport;
import it.unicam.sensorsimulator.plugin.example.setting.ExampleSettingsDialog;
import it.unicam.sensorsimulator.plugin.report.ExamplePluginReportingModule;

public class ExamplePlugin implements PluginInterface {
	
	private ExampleSettingsDialog settingsDialog;
	private SimulationEnvironmentServices environmentServices;
	
	public ExamplePlugin() {
		settingsDialog = new ExampleSettingsDialog(this);
	}

	@Override
	public String getPluginName() {
		return "Example";
	}

	@Override
	public String getPluginVersion() {
		return "v1";
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
		return ExamplePluginReportingModule.class;
	}
	
	@Override
	public Class<?> getReportClass() {
		return ExampleReport.class;
	}

	public ArrayList<ExampleAgentConfiguration> getAgentList() {
		ArrayList<ExampleAgentConfiguration> array = new ArrayList<ExampleAgentConfiguration>();
		
		for(GeneralAgentInterface agent : environmentServices.getAgentList()){
			array.add(new ExampleAgentConfiguration(agent.getAgentID(), agent.getAgentRadius(), agent.getLocationX(), agent.getLocationY()));
		}
		
		return array;
	}
}
