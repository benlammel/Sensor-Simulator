package it.unicam.sensorsimulator.plugin.heed;

import javafx.scene.Parent;
import it.unicam.sensorsimulator.interfaces.PluginInterface;
import it.unicam.sensorsimulator.interfaces.SimulationEnvironmentServices;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.plugin.heed.agents.coordinator.SimulationCoordinatorAgent;
import it.unicam.sensorsimulator.plugin.heed.configdialog.HeedPluginConfigDialog;
import it.unicam.sensorsimulator.plugin.heed.reporting.ReportingModule;
import it.unicam.sensorsimulator.plugin.heed.reporting.report.HeedReport;
import it.unicam.sensorsimulator.plugin.heed.simulation.SimulationRunFile;

public class HeedPlugin implements PluginInterface {
	
	private HeedPluginConfigDialog configDialog;
	private SimulationEnvironmentServices environmentServices;
	
	public HeedPlugin() {
		configDialog = new HeedPluginConfigDialog(this);
	}

	@Override
	public String getPluginName() {
		return "Heed Plugin";
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
		return HeedReport.class;
	}

}
