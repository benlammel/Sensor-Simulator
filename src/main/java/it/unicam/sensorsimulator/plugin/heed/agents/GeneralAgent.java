package it.unicam.sensorsimulator.plugin.heed.agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface.LogLevels;
import it.unicam.sensorsimulator.plugin.heed.agents.behaviours.HeedBehaviour;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes.MessageHandling;
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
	private boolean isConnectedToClusterHead = false;
	private int clusterHead;
	private ArrayList<Integer> myClusterAgents;

	protected void setup(){
		initAndSetArguments();
		log.logAgentAction(LogLevels.INFO, getAID().getLocalName() +" is up and waits");
		myClusterAgents = new ArrayList<Integer>();
		
//		addBehaviour(new ReceiveNeighborsListBehaviour(this));
//		addBehaviour(new HeedClusteringBehaviour(this, generateTickNumber()));
		addBehaviour(new HeedBehaviour(this, generateTickNumber()));
		
	}
	
	private int generateTickNumber() {
		Random r = new Random();
		int Low = 300;
		int High = 1000;
		return r.nextInt(High-Low) + Low;
	}

	private void initAndSetArguments() {
		Object [] args = getArguments();
		log = (LogFileWriterInterface) args[0];
		config = (AgentConfiguration) args[1];
		nList = (HashMap<Integer, GeneralAgentInterface>) args[2];
	}

	public HashMap<Integer, GeneralAgentInterface> getNeighborList() {
		if(nList == null){
			nList = new HashMap<Integer, GeneralAgentInterface>();
		}
		return nList;
	}

	public void setSimulationCoordinatorAID(AID sender) {
		this.simulationCoordinatorAID = sender;
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

	public GeneralAgentInterface getAgentConfiguration() {
		return config;
	}

	public AID getSimulationCoordinatorAID() {
		return simulationCoordinatorAID;
	}

	public AID convertAgentIDToAID(int agentID) {
		return new AID(Integer.toString(agentID), AID.ISLOCALNAME);
	}

	public double getEMax() {
		return 5000;
	}

	public double getEResidual() {
		return 3000;
	}

	public int convertAgentAIDToInteger(AID aid) {
		return Integer.parseInt(aid.getLocalName());
	}

	public void setClusterHead(int agentID) {
		clusterHead = agentID;
	}

	public void addToMyCluster(int agentID) {
		myClusterAgents.add(agentID);
		sendReportMessage(MessageTypes.M1);
	}

	private void sendReportMessage(String messageType) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(messageType);
		try {
			message.setContentObject(myClusterAgents);
		} catch (IOException e) {
			e.printStackTrace();
		}
		message.addReceiver(getSimulationCoordinatorAID());
		sendMessage(message);		
	}

	public LogFileWriterInterface getLog() {
		return log;
	}

	public void setConnected(boolean b) {
		isConnectedToClusterHead = b;
	}

	public boolean isConnected() {
		return isConnectedToClusterHead;
	}
}
