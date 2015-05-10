package it.unicam.sensorsimulator.plugin.heed.reporting;

import java.util.ArrayList;

public class ClusterPicture {

	private int clusterHead;
	private ArrayList<Integer> clusterList;

	public ClusterPicture(int clusterHead, ArrayList<Integer> clusterMembers) {
		this.clusterHead = clusterHead;
		this.clusterList = new ArrayList<Integer>(clusterMembers);
	}

	public ArrayList<Integer> getList() {
		return clusterList;
	}

	public int getClusterHead() {
		return clusterHead;
	}

}
