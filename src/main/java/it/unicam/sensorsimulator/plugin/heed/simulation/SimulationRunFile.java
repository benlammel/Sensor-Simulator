package it.unicam.sensorsimulator.plugin.heed.simulation;

import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.plugin.heed.agents.AgentConfiguration;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class SimulationRunFile implements SimulationRunInterface {

	private boolean startMASObservationUI;
	private boolean startSnifferAgent;
	private boolean startInspectorAgent;
	private int numberOfRuns;
	
	@XmlElementWrapper(name = "agentlist")
	@XmlElement(name = "agent", type=AgentConfiguration.class)
	private ArrayList<AgentConfiguration> agentList;
	private boolean generateRandomCosts;
	
	public SimulationRunFile() {
		this.agentList = new ArrayList<AgentConfiguration>();
		this.setNumberOfRuns(1);
	}

	@Override
	@XmlElement
	public boolean getStartMASObservationUI() {
		return startMASObservationUI;
	}

	@Override
	public void setStartMASObservationUI(boolean value) {
		this.startMASObservationUI = value;
	}

	@Override
	@XmlElement
	public boolean getStartSnifferAgent() {
		return startSnifferAgent;
	}

	@Override
	public void setStartSnifferAgent(boolean value) {
		this.startSnifferAgent = value;
	}

	@Override
	@XmlElement
	public boolean getStartInspectorAgent() {
		return startInspectorAgent;
	}

	@Override
	public void setStartInspectorAgent(boolean value) {
		this.startInspectorAgent = value;
	}

	@Override
	@XmlElement
	public int getNumberOfRuns() {
		return numberOfRuns;
	}

	@Override
	public void setNumberOfRuns(int value) {
		this.numberOfRuns = value;
	}

	@Override
	public ArrayList<AgentConfiguration> getAgentList() {
		return agentList;
	}

	public void addAgent(AgentConfiguration a) {
		agentList.add(a);
	}

	@XmlElement
	public boolean getGenerateRandomCosts() {
		return generateRandomCosts;
	}
	
	public void setGenerateRandomCosts(boolean generateRandomCosts) {
		this.generateRandomCosts = generateRandomCosts;
	}
}
