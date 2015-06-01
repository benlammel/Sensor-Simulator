package it.unicam.sensorsimulator.plugin.heedv2.reporting.report;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Heedv2RunReport {

	private ArrayList<HeedAgentStatistic> agentStatistics;
	private int runNo;
	private HeedAgentStatistic coordinatorStatistic;

	public Heedv2RunReport() {
		agentStatistics = new ArrayList<HeedAgentStatistic>();
	}
	
	public Heedv2RunReport(int simulationRun) {
		this();
		setRunNo(simulationRun);
	}

	private void setRunNo(int simulationRun) {
		this.runNo = simulationRun;
	}
	
	@XmlAttribute
	private int getRunNo() {
		return runNo;
	}
	
	@XmlElementWrapper(name = "agentStatistics")
	public ArrayList<HeedAgentStatistic> getAgentStatistics() {
		return agentStatistics;
	}

	public void setAgentStatistics(ArrayList<HeedAgentStatistic> agentStatistics) {
		this.agentStatistics = agentStatistics;
	}

	public void addAgentStatistic(HeedAgentStatistic statistic) {
		agentStatistics.add(statistic);
	}

	public void setCoordinatorStatistic(HeedAgentStatistic heedAgentStatistic) {
		coordinatorStatistic = heedAgentStatistic;
	}
	
	@XmlElement
	public HeedAgentStatistic getCoordinatorStatistic() {
		return coordinatorStatistic;
	}
}