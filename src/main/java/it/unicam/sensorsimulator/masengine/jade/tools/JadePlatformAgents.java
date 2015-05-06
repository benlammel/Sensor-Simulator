package it.unicam.sensorsimulator.masengine.jade.tools;

import java.util.UUID;

public enum JadePlatformAgents {
	SNIFFER("sniffer-"),
	RMA("rma-"),
	INSPECTOR("inp-");
	
	private String name;
	
	private JadePlatformAgents(String name) {
		this.name = name + UUID.randomUUID();
	}

	public String getName() {
		return name;
	}

}
