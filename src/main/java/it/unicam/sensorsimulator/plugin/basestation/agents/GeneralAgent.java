package it.unicam.sensorsimulator.plugin.basestation.agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface.LogLevels;
import it.unicam.sensorsimulator.plugin.basestation.agents.behaviours.ReceiveNeighborsListBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class GeneralAgent extends Agent {
	
	private LogFileWriterInterface log;
	private AgentConfiguration config;
	private HashMap<Integer, GeneralAgentInterface> nList;
	private AID simulationCoordinatorAID;
	private HashMap<String, Integer> sentMessageCounter;
	private HashMap<String, Integer> receivedMessageCounter;
	private HashMap<Integer, GeneralAgentInterface> connectionView;

	protected void setup(){
		initAndSetArguments();
		log.logAgentAction(LogLevels.INFO, getAID().getLocalName() +" is up and waits");
		
		addBehaviour(new ReceiveNeighborsListBehaviour(this));
		
		
	}
	
	private void initAndSetArguments() {
		Object [] args = getArguments();
		log = (LogFileWriterInterface) args[0];
		config = (AgentConfiguration) args[1];
		nList = (HashMap<Integer, GeneralAgentInterface>) args[2];
		this.connectionView = new HashMap<Integer, GeneralAgentInterface>(nList);
		System.out.println(getAID().getLocalName() +" :: " +nList.keySet());
	}

	public HashMap<Integer, GeneralAgentInterface> getNeighborList() {
		if(nList == null){
			nList = new HashMap<Integer, GeneralAgentInterface>();
		}
		return nList;
	}

//	public void setNeighborList(HashMap<Integer, GeneralAgentInterface> nList) {
//		this.nList = nList;
//		this.connectionView = new HashMap<Integer, GeneralAgentInterface>(nList);
//	}

	public void setSimulationCoordinatorAID(AID sender) {
		this.simulationCoordinatorAID = sender;
	}

	public void receiveMessageCounter(ACLMessage message) {
		if(receivedMessageCounter==null){
			receivedMessageCounter = new HashMap<String, Integer>();
		}
		
		if(!receivedMessageCounter.containsKey(message.getConversationId())){
			receivedMessageCounter.put(message.getConversationId(), 1);
		}else{
			int counter = receivedMessageCounter.get(message.getConversationId());
			receivedMessageCounter.put(message.getConversationId(), counter++ );
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

	public GeneralAgentInterface getAgentConfiguration() {
		return config;
	}

	public HashMap<Integer, GeneralAgentInterface> getConnectionView() {
		return connectionView;
	}

	public AID getSimulationCoordinatorAID() {
		return simulationCoordinatorAID;
	}
}
