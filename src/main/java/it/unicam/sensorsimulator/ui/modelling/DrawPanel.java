package it.unicam.sensorsimulator.ui.modelling;

import java.util.ArrayList;
import java.util.Random;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.ui.agent.GUIAgent;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class DrawPanel extends Pane {

	private int agentID = 0;
	private Modeller modeller;

	public DrawPanel(Modeller modeller) {
		this.modeller = modeller;
		reloadColorFromConfig();
	}

	private void reloadColorFromConfig() {
		this.setStyle("-fx-background-color: "
				+ modeller.getRessourcesAndProperties()
						.getDrawPanelBackgroundColorInHex());
	}

	public void setBackgroundColor(Color color) {
		modeller.getRessourcesAndProperties()
				.setDrawPanelBackgroundColor(color);
		reloadColorFromConfig();
	}

	public void addAgent(GeneralAgentInterface agent) {
		if (agent.getAgentID() > agentID) {
			agentID = agent.getAgentID();
		}
		this.addAgent(new GUIAgent(agent.getAgentID(), modeller, agent.getAgentRadius(), agent.getLocationX(), agent.getLocationY()));
	}

	private void addAgent(GUIAgent uiElement) {
		this.getChildren().add(uiElement);
		modeller.setModellerMode(ModellerMode.CONTENT);
	}

	public void addAgentBatch(SimulationRunInterface simulationRun) {
		clearPanel();
		for (GeneralAgentInterface agent : simulationRun.getAgentList()) {
			addAgent(agent);
		}
	}

	public void clearPanel() {
		this.getChildren().clear();
		agentID = 0;
		modeller.setModellerMode(ModellerMode.NOCONTENT);
	}

	public void addAgent() {
		this.addAgent(new GUIAgent(++agentID, modeller));
		modeller.setModellerMode(ModellerMode.CONTENT);
	}

	public ArrayList<GeneralAgentInterface> getListOfAgents() {
		ArrayList<GeneralAgentInterface> agentList = new ArrayList<GeneralAgentInterface>();
		for(Node agent : this.getChildren()){
			if(agent instanceof GeneralAgentInterface){
				agentList.add((GeneralAgentInterface) agent);
			}
		}
		return agentList;
	}

	public void generateRandomAgents(int numberOfAgents) {
		clearPanel();
		int radius = 20;
		for(int i = 0; i<numberOfAgents; i++){
			addAgent(new GUIAgent(++agentID, modeller, radius, generateRandomNumber(radius, modeller.getWidth()-radius),
					generateRandomNumber(radius, modeller.getHeight()-radius-40)));
		}
	}

	private int generateRandomNumber(double lowVal, double highVal) {
		Random r = new Random();
		int low = (int) lowVal;
		int high = (int) highVal;
		return r.nextInt(high-low) + low;
	}
}
