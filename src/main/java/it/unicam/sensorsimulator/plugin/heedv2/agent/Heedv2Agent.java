package it.unicam.sensorsimulator.plugin.heedv2.agent;

import java.util.HashMap;
import java.util.Iterator;

import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface.LogLevels;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes.MessageHandling;
import it.unicam.sensorsimulator.plugin.heedv2.agent.behaviour.Heedv2Behaviour;
import it.unicam.sensorsimulator.plugin.heedv2.agent.config.HeedAgentConfiguration;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class Heedv2Agent extends Agent {

	private LogFileWriterInterface log;
	private HeedAgentConfiguration config;
	private HashMap<String, Integer> receivedMessageCounter;
	private HashMap<String, Integer> sentMessageCounter;

	protected void setup() {
		initAndSetArguments();
		log.logAgentAction(LogLevels.INFO, getAID().getLocalName()
				+ " is up and waits");
		addBehaviour(new Heedv2Behaviour(this));
	}

	private void initAndSetArguments() {
		Object[] args = getArguments();
		log = (LogFileWriterInterface) args[0];
		config = (HeedAgentConfiguration) args[1];
	}

	public void receiveMessageCounter(ACLMessage message, MessageHandling handling) {
		if(receivedMessageCounter==null){
			receivedMessageCounter = new HashMap<String, Integer>();
		}
		
		if(!receivedMessageCounter.containsKey(message.getConversationId()) && handling.equals(MessageHandling.INCREASE)){
			receivedMessageCounter.put(message.getConversationId(), 1);
		}else if(receivedMessageCounter.containsKey(message.getConversationId()) && handling.equals(MessageHandling.INCREASE)){
			int counter = receivedMessageCounter.get(message.getConversationId());
			receivedMessageCounter.put(message.getConversationId(), counter++ );
		}else if(receivedMessageCounter.containsKey(message.getConversationId()) && handling.equals(MessageHandling.DECREASE)){
			int counter = receivedMessageCounter.get(message.getConversationId());
			receivedMessageCounter.put(message.getConversationId(), counter-- );
		}else if(!receivedMessageCounter.containsKey(message.getConversationId()) && handling.equals(MessageHandling.DECREASE)){
			receivedMessageCounter.put(message.getConversationId(), 0);
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
			sentMessageCounter.put(message.getConversationId(), counter++ );
		}
		super.send(message);
		String receivers = "";
		for(Iterator<?> iterator = message.getAllReceiver(); iterator.hasNext();){
			AID r = (AID) iterator.next();
			receivers = receivers + r.getLocalName();
			}
		log.logAgentMessageSent(this.getAID().getLocalName(), message.getConversationId(), receivers);
	}
}
