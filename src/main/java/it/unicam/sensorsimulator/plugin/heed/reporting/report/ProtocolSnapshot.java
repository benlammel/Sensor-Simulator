package it.unicam.sensorsimulator.plugin.heed.reporting.report;

import java.util.ArrayList;

public class ProtocolSnapshot {

	private int clusterHead;
	private ArrayList<Integer> clusterMembers;

	public ProtocolSnapshot(int clusterHead, ArrayList<Integer> clusterMembers) {
		this.clusterHead = clusterHead;
		this.clusterMembers = clusterMembers;
	}

	public int getClusterHead() {
		return clusterHead;
	}

	public ArrayList<Integer> getClusterMemberList() {
		return clusterMembers;
	}

}
