package it.unicam.sensorsimulator.plugin.example;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.plugin.example.agent.config.ExampleAgentConfiguration;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ExampleSimulationRunFile implements SimulationRunInterface {

	private boolean startMASObservationUI;
	private boolean startSnifferAgent;
	private boolean startInspectorAgent;
	private int numberOfRuns;
	
	@XmlElementWrapper(name = "agentlist")
	@XmlElement(name = "agent", type=ExampleAgentConfiguration.class)
	private ArrayList<ExampleAgentConfiguration> agentList;
	
	
	public ExampleSimulationRunFile(){
		this.agentList = new ArrayList<ExampleAgentConfiguration>();
		this.setNumberOfRuns(1);
	}

	@XmlElement
	@Override
	public boolean getStartMASObservationUI() {
		return startMASObservationUI;
	}

	@Override
	public void setStartMASObservationUI(boolean value) {
		this.startMASObservationUI = value;
	}
	
	@XmlElement
	@Override
	public boolean getStartSnifferAgent() {
		return startSnifferAgent;
	}

	@Override
	public void setStartSnifferAgent(boolean value) {
		this.startSnifferAgent = value;
	}
	
	@XmlElement
	@Override
	public boolean getStartInspectorAgent() {
		return startInspectorAgent;
	}

	@Override
	public void setStartInspectorAgent(boolean value) {
		this.startInspectorAgent = value;
	}

	@XmlElement
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

	public void addAgent(ExampleAgentConfiguration agent) {
		agentList.add(agent);
	}

}
