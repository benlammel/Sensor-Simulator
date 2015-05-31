package it.unicam.sensorsimulator.plugin.example.reporting.report;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ExampleRunReport {

	private int runNumber;
	private ArrayList<ExampleAgentStatistics> agentStatistics;
	private ExampleAgentStatistics coordinatorStatistics;

	public ExampleRunReport() {
		agentStatistics = new ArrayList<ExampleAgentStatistics>();
		runNumber = 0;
	}
	
	public ExampleRunReport(int runCounter) {
		this();
		setRunNumber(runCounter);
	}

	public void setRunNumber(int runCounter) {
		this.runNumber = runCounter;
	}
	
	@XmlAttribute
	public int getRunNumber() {
		return runNumber;
	}
	
	public void setAgentStatistics(ArrayList<ExampleAgentStatistics> agentStatistics){
		this.agentStatistics = agentStatistics;
	}
	
	@XmlElement
	public ArrayList<ExampleAgentStatistics> getAgentStatistics(){
		return agentStatistics;
	}

	public void addAgentStatistics(ExampleAgentStatistics statistics) {
		agentStatistics.add(statistics);
	}
	
	@XmlElement
	public ExampleAgentStatistics getCoordinatorStatsitics() {
		return coordinatorStatistics;
	}

	public void setCoordinatorStatsitics(
			ExampleAgentStatistics exampleAgentStatistics) {
		this.coordinatorStatistics = exampleAgentStatistics;
	}

}
