package it.unicam.sensorsimulator.plugin.heed.reporting.report;

import java.io.Serializable;
import java.util.HashMap;

public class AgentStatistic implements Serializable {
	private HashMap<String, Integer> sentMessageCounter;
	private HashMap<String, Integer> receivedMessageCounter;

	public AgentStatistic(HashMap<String, Integer> sentMessageCounter,
			HashMap<String, Integer> receivedMessageCounter) {
		this.sentMessageCounter = sentMessageCounter;
		this.receivedMessageCounter = receivedMessageCounter;
	}
}
