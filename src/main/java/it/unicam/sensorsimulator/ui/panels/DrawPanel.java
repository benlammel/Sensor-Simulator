package it.unicam.sensorsimulator.ui.panels;

import java.util.ArrayList;
import java.util.Random;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.ui.ApplicationFrame;
import it.unicam.sensorsimulator.ui.agent.GUIAgent;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class DrawPanel extends Pane {

	private ApplicationFrame applicationFrame;
	private int agentID = 0;

	public DrawPanel(ApplicationFrame applicationFrame) {
		this.applicationFrame = applicationFrame;
		reloadColorFromConfig();
	}

	private void reloadColorFromConfig() {
		this.setStyle("-fx-background-color: "
				+ applicationFrame.getRessourcesAndProperties()
						.getDrawPanelBackgroundColorInHex());
	}

	public void setBackgroundColor(Color color) {
		applicationFrame.getRessourcesAndProperties()
				.setDrawPanelBackgroundColor(color);
		reloadColorFromConfig();
	}

	public void addAgent(GeneralAgentInterface agent) {
		if (agent.getAgentID() > agentID) {
			agentID = agent.getAgentID();
		}
		this.addAgent(new GUIAgent(applicationFrame, agent.getAgentID(), agent.getAgentRadius(), agent.getLocationX(), agent.getLocationY()));
	}

	private void addAgent(GUIAgent uiElement) {
		this.getChildren().add(uiElement);
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
	}

	public void addAgent() {
		this.addAgent(new GUIAgent(applicationFrame, ++agentID));
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
			addAgent(new GUIAgent(applicationFrame, ++agentID,radius, generateRandomNumber(radius, applicationFrame.getWidth()-radius),
					generateRandomNumber(radius, applicationFrame.getHeight()-radius-70)));
		}
	}

	private int generateRandomNumber(double lowVal, double highVal) {
		Random r = new Random();
		int low = (int) lowVal;
		int high = (int) highVal;
		return r.nextInt(high-low) + low;
	}
}
