package it.unicam.sensorsimulator.plugin.heedv2.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface.LogLevels;
import it.unicam.sensorsimulator.plugin.heedv2.agent.behaviour.SimulationControlBehaviour;
import it.unicam.sensorsimulator.plugin.heedv2.agent.config.HeedAgentConfiguration;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class Heedv2Agent extends Agent {

	private LogFileWriterInterface log;
	private HeedAgentConfiguration config;
	private HashMap<String, Integer> receivedMessageCounter;
	private HashMap<String, Integer> sentMessageCounter;
	private boolean isClusterHead = false;
	private int clusterHeadID = -1;
	private HashMap<Integer, HeedAgentConfiguration> neighborList;
	private ArrayList<Integer> mySuccessorsList;
	private AID coordinatorAID;

	protected void setup() {
		initVarsAndDS();
		initAndSetArguments();
		log.logAgentAction(LogLevels.INFO, getAID().getLocalName()
				+ " is up and waits");
		addBehaviour(new SimulationControlBehaviour(this));
	}

	private void initVarsAndDS() {
		mySuccessorsList = new ArrayList<Integer>();
	}

	private void initAndSetArguments() {
		Object[] args = getArguments();
		log = (LogFileWriterInterface) args[0];
		config = (HeedAgentConfiguration) args[1];
		neighborList = (HashMap<Integer, HeedAgentConfiguration>) args[2];
		track("initAndSetArguments");
	}

	public void track(String string) {
		StringBuilder builder = new StringBuilder();
		builder.append(";agentID;");
		builder.append(getAgentConfiguration().getAgentID());
		
		builder.append(";getQueueSize;");
		builder.append(getQueueSize());
		
		builder.append(";getCurQueueSize;");
		builder.append(getCurQueueSize());
		
		builder.append(";neighborList;");
		for(int n :neighborList.keySet()){
			builder.append(n);
			builder.append("-");
		}
		
		builder.append(";isClusterHead;");
		builder.append(isClusterHead);
		builder.append(";clusterHeadID;");
		builder.append(clusterHeadID);
		builder.append(";mySuccessorsList;");
		builder.append(mySuccessorsList.toString());
		builder.append(";agentID;");
		builder.append(getAgentConfiguration().getAgentID());
		builder.append(";agentID;");
		builder.append(getAgentConfiguration().getAgentID());
		
		builder.append(string);
		log.logAgentAction(LogLevels.INFO, builder.toString());
	}

	public void receiveMessageCounter(ACLMessage message) {
		if(receivedMessageCounter==null){
			receivedMessageCounter = new HashMap<String, Integer>();
		}
		
		if(!receivedMessageCounter.containsKey(message.getConversationId())){
			receivedMessageCounter.put(message.getConversationId(), 1);
		}else if(receivedMessageCounter.containsKey(message.getConversationId())){
			int counter = receivedMessageCounter.get(message.getConversationId());
			receivedMessageCounter.put(message.getConversationId(), ++counter );
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
			sentMessageCounter.put(message.getConversationId(), ++counter );
		}
		super.send(message);
		String receivers = "";
		for(Iterator<?> iterator = message.getAllReceiver(); iterator.hasNext();){
			AID r = (AID) iterator.next();
			receivers = receivers + r.getLocalName();
			}
		log.logAgentMessageSent(this.getAID().getLocalName(), message.getConversationId(), receivers);
	}

	public boolean isConnected() {
		if(isClusterHead || clusterHeadID !=-1){
			return true;
		}else{
			return false;
		}
	}

	public HeedAgentConfiguration getAgentConfiguration() {
		return config;
	}

	public HashMap<Integer, HeedAgentConfiguration> getNeighborList() {
		return neighborList;
	}

	public AID convertIntegerToAID(int agentID) {
		return new AID(Integer.toString(agentID), AID.ISLOCALNAME);
	}
	
	public int convertAIDToInteger(AID aid) {
		return Integer.parseInt(aid.getLocalName());
	}

	public int getCosts(int agentID) {
		return 1;
	}

	public void addToMyCluster(int agentID) {
		mySuccessorsList.add(agentID);
	}

	public void setClusterHead(boolean b) {
		isClusterHead = b;
	}

	public void setMyClusterHead(int myClusterHead) {
		clusterHeadID = myClusterHead;
	}
	
	public int getMyClusterHead(){
		return clusterHeadID;
	}
	
	public void setCoordinatorAgentAID(AID coordinator){
		this.coordinatorAID = coordinator;
	}

	public void notifyCoordinator(String conversationID) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(conversationID);
		message.addReceiver(coordinatorAID);
		sendMessage(message);
	}

	public AID getSimulationCoordinatorAID() {
		return coordinatorAID;
	}

	public HashMap<String, Integer> getSentMessageCounter() {
		return sentMessageCounter;
	}

	public HashMap<String, Integer> getReceivedMessageCounter() {
		return receivedMessageCounter;
	}
}
