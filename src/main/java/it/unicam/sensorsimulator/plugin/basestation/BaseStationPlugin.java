package it.unicam.sensorsimulator.plugin.basestation;

import javafx.scene.Parent;
import it.unicam.sensorsimulator.interfaces.AbstractReportPane;
import it.unicam.sensorsimulator.interfaces.PluginInterface;
import it.unicam.sensorsimulator.interfaces.ReportInterface;
import it.unicam.sensorsimulator.interfaces.SimulationEnvironmentServices;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.plugin.basestation.agents.coordinator.SimulationCoordinatorAgent;
import it.unicam.sensorsimulator.plugin.basestation.configdialog.BaseStationConfigDialog;
import it.unicam.sensorsimulator.plugin.basestation.reporting.ReportingModule;
import it.unicam.sensorsimulator.plugin.basestation.simulation.SimulationRunFile;

public class BaseStationPlugin implements PluginInterface {
	
	private BaseStationConfigDialog configDialog;
	private SimulationEnvironmentServices environmentServices;
	
	public BaseStationPlugin() {
		configDialog = new BaseStationConfigDialog(this);
	}

	@Override
	public String getPluginName() {
		return "Base Station Plugin";
	}

	@Override
	public String getPluginVersion() {
		return "0.1";
	}

	@Override
	public Parent getSettingsDialogContent() {
		return configDialog;
	}

	@Override
	public void refreshSettingsDialogContent() {
		configDialog.refreshContent();
	}

	@Override
	public SimulationRunInterface generateAndReturnSimulationRunFile() {
		return configDialog.generateAndReturnSimulationRunFile();
	}

	@Override
	public Class<?> getSimulationRunFileClass() {
		return SimulationRunFile.class;
	}

	@Override
	public void updateSimulationRunFile(SimulationRunInterface simulationRunFile) {
		configDialog.updateSimulationRunFile(simulationRunFile);
	}

	@Override
	public void setSimulationEnvironmentServiceClass(
			SimulationEnvironmentServices environmentServices) {
		this.environmentServices = environmentServices;
	}

	@Override
	public Class<?> getSimulationCoordinatorAgent() {
		return SimulationCoordinatorAgent.class;
	}

	public SimulationEnvironmentServices getEnvironmentServices() {
		return environmentServices;
	}

	@Override
	public Class<?> getReportingPane() {
		return ReportingModule.class;
	}

	@Override
	public Class<?> getReportClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
