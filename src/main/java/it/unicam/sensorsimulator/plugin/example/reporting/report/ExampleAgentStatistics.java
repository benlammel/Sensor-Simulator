package it.unicam.sensorsimulator.plugin.example.reporting.report;

import it.unicam.sensorsimulator.plugin.heedv2.reporting.report.MessageCounter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;

public class ExampleAgentStatistics implements Serializable {

	private int agentID;
	private ArrayList<MessageCounter> sentCounter;
	private ArrayList<MessageCounter> receivedCounter;
	
	public ExampleAgentStatistics() {
		sentCounter = new ArrayList<MessageCounter>();
		receivedCounter = new ArrayList<MessageCounter>();
	}

	public ExampleAgentStatistics(int agentID,
			HashMap<String, Integer> sentMsgCounter,
			HashMap<String, Integer> receivedMsgCounter) {
		this();
		setAgentID(agentID);
		createSentCounter(sentMsgCounter);
		createReceivedCounter(receivedMsgCounter);
	}
	
	public ExampleAgentStatistics(HashMap<String, Integer> sentMessageCounter,
			HashMap<String, Integer> receivedMessageCounter) {
		this();
		setAgentID(0);
		createSentCounter(sentMessageCounter);
		createReceivedCounter(receivedMessageCounter);
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

	private void createReceivedCounter(
			HashMap<String, Integer> receivedMsgCounter) {
		for(Entry<String, Integer> sent : receivedMsgCounter.entrySet()){
			receivedCounter.add(new MessageCounter(sent.getKey(), sent.getValue()));
		}
	}

	private void createSentCounter(HashMap<String, Integer> sentMsgCounter) {
		for(Entry<String, Integer> sent : sentMsgCounter.entrySet()){
			sentCounter.add(new MessageCounter(sent.getKey(), sent.getValue()));
		}
	}

	@XmlAttribute
	public int getAgentID() {
		return agentID;
	}
	
	public void setAgentID(int agentID) {
		this.agentID = agentID;
	}

}
