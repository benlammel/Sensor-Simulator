package it.unicam.sensorsimulator.plugin.heedv2;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.plugin.heed.agents.AgentConfiguration;
import it.unicam.sensorsimulator.plugin.heedv2.agent.config.HeedAgentConfiguration;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Heedv2SimulationRunFile implements SimulationRunInterface {

	private boolean startMASObservationUI;
	private boolean startSnifferAgent;
	private boolean startInspectorAgent;
	private int numberOfRuns;
	
	@XmlElementWrapper(name = "agentlist")
	@XmlElement(name = "agent", type=HeedAgentConfiguration.class)
	private ArrayList<HeedAgentConfiguration> agentList;
	
	public Heedv2SimulationRunFile(){
		this.agentList = new ArrayList<HeedAgentConfiguration>();
		this.setNumberOfRuns(1);
	}
	
	@Override
	public boolean getStartMASObservationUI() {
		return startMASObservationUI;
	}

	@Override
	public void setStartMASObservationUI(boolean value) {
		this.startMASObservationUI = value;
	}

	@Override
	public boolean getStartSnifferAgent() {
		return startSnifferAgent;
	}

	@Override
	public void setStartSnifferAgent(boolean value) {
		this.startSnifferAgent = value;
	}

	@Override
	public boolean getStartInspectorAgent() {
		return startInspectorAgent;
	}

	@Override
	public void setStartInspectorAgent(boolean value) {
		this.startInspectorAgent = value;
	}

	@Override
	public int getNumberOfRuns() {
		return numberOfRuns;
	}

	@Override
	public void setNumberOfRuns(int value) {
		this.numberOfRuns = value;
	}

	@Override
	public ArrayList<? extends GeneralAgentInterface> getAgentList() {
		return agentList;
	}

	public void addAgent(HeedAgentConfiguration agent) {
		agentList.add(agent);
	}
}
