package it.unicam.sensorsimulator.plugin.basestation.configdialog;

import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.plugin.basestation.BaseStationPlugin;
import it.unicam.sensorsimulator.plugin.basestation.agents.AgentConfiguration;
import it.unicam.sensorsimulator.plugin.basestation.simulation.SimulationRunFile;

public class BaseStationConfigDialog extends BorderPane {
	
	private BaseStationPlugin baseStationPlugin;
	private TabPane tabs;
	private GeneralTab generalTab;
	private SimulationRunFile simulationRunFile;

	public BaseStationConfigDialog(BaseStationPlugin baseStationPlugin) {
		this.baseStationPlugin = baseStationPlugin;
		
		initTabs();
		this.setTop(tabs);
	}
	
	private void initTabs() {
		tabs = new TabPane();
		generalTab = new GeneralTab(this);
		tabs.getTabs().add(generalTab);
	}

	public void refreshContent() {
		generalTab.refreshContent();
	}

	public SimulationRunInterface generateAndReturnSimulationRunFile() {
		simulationRunFile.setNumberOfRuns(generalTab.getNumberOfRuns());
		simulationRunFile.setStartInspectorAgent(false);
		simulationRunFile.setStartMASObservationUI(true);
		simulationRunFile.setStartSnifferAgent(false);
		
		loadAgents();
		
		return simulationRunFile ;
	}
	
	private void loadAgents() {
		simulationRunFile.getAgentList().clear();
		
		for(GeneralAgentInterface agent : baseStationPlugin.getEnvironmentServices().getAgentList()){
				createAgentAndAddtoRunFile(agent);
			}
		}

	private void createAgentAndAddtoRunFile(GeneralAgentInterface agent) {
		AgentConfiguration a = new AgentConfiguration();
		a.setAgentID(agent.getAgentID());
		a.setAgentRadius(agent.getAgentRadius());
		a.setLocationX(agent.getLocationX());
		a.setLocationY(agent.getLocationY());
		simulationRunFile.addAgent(a);
	}

	public SimulationRunFile getCurrentRunFile() {
		if(simulationRunFile==null){
			simulationRunFile = new SimulationRunFile();
		}
		return simulationRunFile;
	}

	public void updateSimulationRunFile(SimulationRunInterface simulationRunFile) {
		this.simulationRunFile = (SimulationRunFile) simulationRunFile;
	}

	public int getNumberOfRuns() {
		if(getCurrentRunFile().getNumberOfRuns()==0){
			return 1;
		}else{
			return getCurrentRunFile().getNumberOfRuns();
		}
	}

	public double getNumberOfClusters() {
		if(getCurrentRunFile().getNumberOfClusters()==0){
			return 1;
		}else{
			return getCurrentRunFile().getNumberOfClusters();
		}
	}

	public int getNumberOfAgents() {
		return baseStationPlugin.getEnvironmentServices().getAgentList().size();
	}
}
