package it.unicam.sensorsimulator.plugin.heed.agents;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface.LogLevels;
import it.unicam.sensorsimulator.plugin.heed.agents.behaviours.HeedBehaviour;
import it.unicam.sensorsimulator.plugin.heed.agents.behaviours.SimulationControlBehaviour;
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
	private int clusterHead = 0;
	private ArrayList<Integer> myClusterAgents;
	private boolean isClusterHead = false;

	protected void setup(){
		initAndSetArguments();
		log.logAgentAction(LogLevels.INFO, getAID().getLocalName() +" is up and waits");
		myClusterAgents = new ArrayList<Integer>();

////		addBehaviour(new ReceiveNeighborsListBehaviour(this));
////		addBehaviour(new HeedClusteringBehaviour(this, generateTickNumber()));
		
		addBehaviour(new HeedBehaviour(this, generateTickNumber()));
		addBehaviour(new SimulationControlBehaviour(this));
		
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

	public void addToMyCluster(int agentID) {
		myClusterAgents.add(agentID);
		this.log.logAgentAction(LogLevels.INFO, getAgentConfiguration().getAgentID() +" added " +agentID +" :: " +myClusterAgents.toString());
	}
	
	public ArrayList<Integer> getMyClusterView() {
		return myClusterAgents;
	}

	public LogFileWriterInterface getLog() {
		return log;
	}

	public boolean isConnectedToAClusterHead() {
		if(clusterHead!=0){
			return true;
		}else{
			return false;
		}
	}

	public void setMyClusterHead(int clusterHeadID) {
		clusterHead  = clusterHeadID;
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(MessageTypes.MEASUREMENT_CLUSTER_FORMING_END);
		message.addReceiver(getSimulationCoordinatorAID());
		sendMessage(message);
	}

	public void isClusterHead(boolean b) {
		isClusterHead  = b;
	}

	public boolean isConnectedToClusterHeadOrIsClusterHead() {
		if(isClusterHead || clusterHead!=0){
			return true;
		}else{
			return false;
		}
	}
}
