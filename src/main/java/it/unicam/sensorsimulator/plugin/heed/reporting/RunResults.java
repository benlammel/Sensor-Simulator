package it.unicam.sensorsimulator.plugin.heed.reporting;

import java.util.ArrayList;
import java.util.HashMap;

public class RunResults {
	
	private HashMap<Integer, AgentStatistic> agentStatistics;
	private ArrayList<Integer> clusterHeadList;
	private AgentStatistic coordinatorStatistics;
	private int runID;
	
	public RunResults(int runID){
		this.runID = runID;
		agentStatistics = new HashMap<Integer, AgentStatistic>();
		clusterHeadList = new ArrayList<Integer>();
	}

	public void addAgentStatistics(int agentID, AgentStatistic statisticFile) {
		agentStatistics.put(agentID, statisticFile);
	}

	public void addClusterHead(int clusterHeadID) {
		clusterHeadList.add(clusterHeadID);
	}

	public HashMap<Integer, AgentStatistic> getAgentStatisticList() {
		return agentStatistics;
	}

	public ArrayList<Integer> getClusterHeadList() {
		return clusterHeadList;
	}

	public void addCoordinatorStatistics(AgentStatistic agentStatistic) {
		this.coordinatorStatistics = agentStatistic;
	}

	public int getRunID() {
		return runID;
	}
}
