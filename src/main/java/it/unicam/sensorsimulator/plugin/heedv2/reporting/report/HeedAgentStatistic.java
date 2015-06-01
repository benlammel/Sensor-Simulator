package it.unicam.sensorsimulator.plugin.heedv2.reporting.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;

public class HeedAgentStatistic implements Serializable {

	private int agentID;
	private ArrayList<MessageCounter> sentCounter;
	private ArrayList<MessageCounter> receivedCounter;

	public HeedAgentStatistic() {
		sentCounter = new ArrayList<MessageCounter>();
		receivedCounter = new ArrayList<MessageCounter>();
	}

	public HeedAgentStatistic(int agentID,
			HashMap<String, Integer> sentMessageCounter,
			HashMap<String, Integer> receivedMessageCounter) {
		this();
		setAgentID(agentID);
		
		for (Entry<String, Integer> sent : sentMessageCounter.entrySet()) {
			sentCounter.add(new MessageCounter(sent.getKey(), sent.getValue()));
		}

		for (Entry<String, Integer> received : receivedMessageCounter
				.entrySet()) {
			receivedCounter.add(new MessageCounter(received.getKey(), received
					.getValue()));
		}
	}

	private void setAgentID(int agentID) {
		this.agentID = agentID;
	}

	@XmlAttribute
	private int getAgentID() {
		return agentID;
	}

	@XmlElementWrapper(name = "sentCounterList")
	public ArrayList<MessageCounter> getSentCounter() {
		return sentCounter;
	}

	public void setSentCounter(ArrayList<MessageCounter> sentCounter) {
		this.sentCounter = sentCounter;
	}

	@XmlElementWrapper(name = "receivedCounterList")
	public ArrayList<MessageCounter> getReceivedCounter() {
		return receivedCounter;
	}

	public void setReceivedCounter(ArrayList<MessageCounter> receivedCounter) {
		this.receivedCounter = receivedCounter;
	}
}
