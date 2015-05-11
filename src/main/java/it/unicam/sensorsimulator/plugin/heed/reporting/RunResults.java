package it.unicam.sensorsimulator.plugin.heed.reporting;

import java.util.ArrayList;
import java.util.HashMap;

public class RunResults {
	
	private HashMap<Integer, AgentStatistic> agentStatistics;
	private ArrayList<Integer> clusterHeadList;
	private AgentStatistic coordinatorStatistics;
	private int runID;
	private int protocolCounter = 0;
	private HashMap<Integer, ProtocolSnapshot> networkEvolution;
	
	public RunResults(){
		agentStatistics = new HashMap<Integer, AgentStatistic>();
		clusterHeadList = new ArrayList<Integer>();
		networkEvolution = new HashMap<Integer, ProtocolSnapshot>();
	}
	
	public RunResults(int runID){
		this();
		setRunID(runID);
	}
	

	public void addAgentStatistics(int agentID, AgentStatistic statisticFile) {
		agentStatistics.put(agentID, statisticFile);
	}
	
	public HashMap<Integer, AgentStatistic> getAgentStatisticList() {
		return agentStatistics;
	}
	
	

	public void addClusterHead(int clusterHeadID) {
		clusterHeadList.add(clusterHeadID);
	}

	public ArrayList<Integer> getClusterHeadList() {
		return clusterHeadList;
	}
	
	public void setClusterHeadList(ArrayList<Integer> clusterHeadList) {
		this.clusterHeadList = clusterHeadList;
	}
	
	

	public void addCoordinatorStatistics(AgentStatistic agentStatistic) {
		this.coordinatorStatistics = agentStatistic;
	}
	
	public AgentStatistic getCoordinatorStatistics() {
		return this.coordinatorStatistics;
	}

	public int getRunID() {
		return runID;
	}
	
	public void setRunID(int runID) {
		this.runID = runID;
	}
	
	public HashMap<Integer, ProtocolSnapshot> getNetworkEvolutionList(){
		return networkEvolution;
	}
	
	public void setNetworkEvolutionList(HashMap<Integer, ProtocolSnapshot> networkEvolutionList){
		this.networkEvolution = networkEvolutionList;
	}

	public void addProtocolMeasurement(int clusterHead,
			ArrayList<Integer> clusterMembers) {
		networkEvolution.put(protocolCounter++, new ProtocolSnapshot(clusterHead, clusterMembers));
	}
}
