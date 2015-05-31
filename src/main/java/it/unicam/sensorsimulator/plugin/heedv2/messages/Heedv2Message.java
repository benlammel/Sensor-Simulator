package it.unicam.sensorsimulator.plugin.heedv2.messages;

import java.io.Serializable;

public class Heedv2Message implements Serializable {

	private double costs;

	public Heedv2Message(double costs) {
		this.costs = costs;
	}

	public double getCosts() {
		return costs;
	}

}
