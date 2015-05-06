package it.unicam.sensorsimulator.plugin.heed.messages;

import java.io.Serializable;

public class HeedMessage implements Serializable {

	private double cost;

	public HeedMessage(String messageType, double value) {
		switch(messageType){
		case MessageTypes.COST_BROADCAST:
			this.cost = value;
			break;
		default:
			break;
		}
	}

	public double getCost() {
		return cost;
	}

}
