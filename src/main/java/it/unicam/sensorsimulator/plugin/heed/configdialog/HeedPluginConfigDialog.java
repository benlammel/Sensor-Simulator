package it.unicam.sensorsimulator.plugin.heed.configdialog;

import java.util.ArrayList;

import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.plugin.heed.HeedPlugin;
import it.unicam.sensorsimulator.plugin.heed.agents.AgentConfiguration;
import it.unicam.sensorsimulator.plugin.heed.simulation.SimulationRunFile;

public class HeedPluginConfigDialog extends BorderPane {

	private HeedPlugin heedPlugin;
	private TabPane tabs;
	private GeneralTab generalTab;
	private NodeTab nodeTab;
	private SimulationRunFile simulationRunFile;

	public HeedPluginConfigDialog(HeedPlugin heedPlugin) {
		this.heedPlugin = heedPlugin;

		initTabs();
		this.setTop(tabs);
	}

	private void initTabs() {
		tabs = new TabPane();
		generalTab = new GeneralTab(this);
		tabs.getTabs().add(generalTab);

		nodeTab = new NodeTab(this);
		tabs.getTabs().add(nodeTab);
	}

	public void refreshContent() {
		nodeTab.refreshContent();
	}

	public SimulationRunInterface generateAndReturnSimulationRunFile() {
		simulationRunFile.setNumberOfRuns(generalTab.getNumberOfRuns());
		simulationRunFile.setStartInspectorAgent(generalTab.getStartInspectorAgent());
		simulationRunFile.setStartMASObservationUI(generalTab.getStartMASObservationUI());
		simulationRunFile.setStartSnifferAgent(generalTab.getStartSnifferAgent());
		simulationRunFile.setGenerateRandomCosts(generalTab.getGenerateRandomCosts());

		loadAgents();

		return simulationRunFile;
	}

	private void loadAgents() {
		simulationRunFile.getAgentList().clear();

		for (AgentConfiguration a : nodeTab.getAgentData()) {
			simulationRunFile.addAgent(a);
		}
	}

	public SimulationRunFile getCurrentRunFile() {
		if (simulationRunFile == null) {
			simulationRunFile = new SimulationRunFile();
		}
		return simulationRunFile;
	}

	public void updateSimulationRunFile(SimulationRunInterface simulationRunFile) {
		this.simulationRunFile = (SimulationRunFile) simulationRunFile;
		this.nodeTab.updateRunFile(simulationRunFile);
	}

	public int getNumberOfRuns() {
		if (getCurrentRunFile().getNumberOfRuns() == 0) {
			return 1;
		} else {
			return getCurrentRunFile().getNumberOfRuns();
		}
	}
	
	public ArrayList<GeneralAgentInterface> getAgentListFromPanel(){
		return heedPlugin.getEnvironmentServices().getAgentList();
	}
}
