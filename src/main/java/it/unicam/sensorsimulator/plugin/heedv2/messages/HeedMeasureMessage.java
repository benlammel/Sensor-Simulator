package it.unicam.sensorsimulator.plugin.heedv2.messages;

import java.io.Serializable;
import java.util.ArrayList;

public class HeedMeasureMessage implements Serializable {

	private int agentID;
	private ArrayList<Integer> successorsList;

	public HeedMeasureMessage(int agentID, ArrayList<Integer> mySuccessorsList) {
		this.agentID = agentID;
		this.successorsList = mySuccessorsList;
	}

	public ArrayList<Integer> getSuccessorList() {
		return successorsList;
	}
	
	public void setSuccessorList(ArrayList<Integer> successorsList) {
		this.successorsList = successorsList;
	}
}
