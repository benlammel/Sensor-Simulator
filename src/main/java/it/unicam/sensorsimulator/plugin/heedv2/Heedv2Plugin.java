package it.unicam.sensorsimulator.plugin.heedv2;

import java.util.ArrayList;

import javafx.scene.Parent;
import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.PluginInterface;
import it.unicam.sensorsimulator.interfaces.SimulationEnvironmentServices;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.plugin.heedv2.agent.config.HeedAgentConfiguration;
import it.unicam.sensorsimulator.plugin.heedv2.coordinator.Heedv2SimulationCoordinatorAgent;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.Heedv2PluginReportingModule;
import it.unicam.sensorsimulator.plugin.heedv2.settingsdialog.Heedv2SettingsDialog;

public class Heedv2Plugin implements PluginInterface {
	
	private Heedv2SettingsDialog settingsDialog;
	private SimulationEnvironmentServices environmentServices;
	private Heedv2PluginReportingModule reportingModule;
	
	public Heedv2Plugin() {
		settingsDialog = new Heedv2SettingsDialog(this);
		reportingModule = new Heedv2PluginReportingModule(this);
	}

	@Override
	public String getPluginName() {
		return "Heed";
	}

	@Override
	public String getPluginVersion() {
		return "v2";
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
		return Heedv2SimulationRunFile.class;
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
		return Heedv2SimulationCoordinatorAgent.class;
	}

	@Override
	public Parent getReportingPane() {
		return reportingModule;
	}

	@Override
	public Object getReportingHandler() {
		return reportingModule;
	}

	public ArrayList<HeedAgentConfiguration> getAgentList() {
		ArrayList<HeedAgentConfiguration> array = new ArrayList<HeedAgentConfiguration>();
		
		for(GeneralAgentInterface agent : environmentServices.getAgentList()){
			array.add(new HeedAgentConfiguration(agent.getAgentID(), agent.getAgentRadius(), agent.getLocationX(), agent.getLocationY()));
		}
		
		return array;
	}
}
