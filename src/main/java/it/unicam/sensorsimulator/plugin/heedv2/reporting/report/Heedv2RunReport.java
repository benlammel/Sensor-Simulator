package it.unicam.sensorsimulator.plugin.heedv2.reporting.report;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Heedv2RunReport {

	private ArrayList<HeedAgentStatistic> agentStatistics;
	private int runNo;
	private HeedAgentStatistic coordinatorStatistic;
	private ArrayList<Integer> successorList;
	private ArrayList<Integer> clusterHeadList;
	private long startTime;
	private long stopTime;

	public Heedv2RunReport() {
		agentStatistics = new ArrayList<HeedAgentStatistic>();
	}
	
	public Heedv2RunReport(int simulationRun) {
		this();
		setRunNumber(simulationRun);
	}

	public void setRunNumber(int simulationRun) {
		this.runNo = simulationRun;
	}
	
	@XmlAttribute
	public int getRunNumber() {
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

	public void setSuccessorList(ArrayList<Integer> successorList) {
		this.successorList = successorList;
	}
	
//	@XmlElementWrapper(name = "successorList")
	public ArrayList<Integer> getSuccessorList() {
		return successorList;
	}

	public void setClusterHeadList(ArrayList<Integer> clusterHeadList) {
		this.clusterHeadList = clusterHeadList;
	}
	
//	@XmlElementWrapper(name = "clusterHeadList")
	public ArrayList<Integer> getClusterHeadList() {
		return clusterHeadList;
	}

	public void setStartTime(long timeMillis) {
		startTime = timeMillis;
	}
	
	@XmlAttribute
	public long getStartTime() {
		return startTime;
	}
	
	public void setStopTime(long timeMillis) {
		stopTime = timeMillis;
	}
	
	@XmlAttribute
	public long getStopTime() {
		return stopTime;
	}
}
