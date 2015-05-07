package it.unicam.sensorsimulator.plugin.heed.messages;

import it.unicam.sensorsimulator.plugin.heed.messages.HeedMessage.ClusterHeadType;

import java.io.Serializable;

public class HeedMessage implements Serializable {
	
	public enum ClusterHeadType{
		FINALCH, TENTATIVECH;
	}

	private double cost;
	private ClusterHeadType clusterHeadType;

	public HeedMessage(double cost) {
		this.cost = cost;
	}

	public HeedMessage(double cost, ClusterHeadType clusterHeadType) {
		this(cost);
		this.clusterHeadType = clusterHeadType;
	}

	public double getCost() {
		return cost;
	}

	public ClusterHeadType getClusterHeadType() {
		return clusterHeadType;
	}

}
