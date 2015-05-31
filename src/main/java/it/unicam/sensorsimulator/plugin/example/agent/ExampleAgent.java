package it.unicam.sensorsimulator.plugin.example.agent;

import java.util.HashMap;
import java.util.Iterator;

import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface;
import it.unicam.sensorsimulator.plugin.example.agent.config.ExampleAgentConfiguration;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class ExampleAgent extends Agent {
	
	private ExampleAgentConfiguration config;
	private LogFileWriterInterface log;
	private HashMap<String, Integer> receivedMessageCounter;
	private HashMap<String, Integer> sentMessageCounter;

	protected void setup() {
		initAndSetArguments();
		addBehaviour(new ExampleBehaviour(this));
	}

	private void initAndSetArguments() {
		Object[] args = getArguments();
		log = (LogFileWriterInterface) args[0];
		config = (ExampleAgentConfiguration) args[1];
	}
	
	
	public void receiveMessageCounter(ACLMessage message) {
		if(receivedMessageCounter==null){
			receivedMessageCounter = new HashMap<String, Integer>();
		}
		
		if(!receivedMessageCounter.containsKey(message.getConversationId())){
			receivedMessageCounter.put(message.getConversationId(), 1);
		}else if(receivedMessageCounter.containsKey(message.getConversationId())){
			int counter = receivedMessageCounter.get(message.getConversationId());
			receivedMessageCounter.put(message.getConversationId(), ++counter);
		}
		log.logAgentMessageReceived(this.getAID().getLocalName(), message.getConversationId(), message.getSender().getLocalName());
	}
	
	public void sendMessage(ACLMessage message) {
		if(sentMessageCounter==null){
			sentMessageCounter = new HashMap<String, Integer>();
		}
		
		if(!sentMessageCounter.containsKey(message.getConversationId())){
			sentMessageCounter.put(message.getConversationId(), 1);
		}else{
			int counter = sentMessageCounter.get(message.getConversationId());
			sentMessageCounter.put(message.getConversationId(), ++counter);
		}
		super.send(message);
		String receivers = "";
		for(Iterator<?> iterator = message.getAllReceiver(); iterator.hasNext();){
			AID r = (AID) iterator.next();
			receivers = receivers + r.getLocalName();
			}
		log.logAgentMessageSent(this.getAID().getLocalName(), message.getConversationId(), receivers);
	}

	public HashMap<String, Integer> getReceivedMsgCounter() {
		return receivedMessageCounter;
	}

	public HashMap<String, Integer> getSentMsgCounter() {
		return sentMessageCounter;
	}
}
