package it.unicam.sensorsimulator.plugin.heedv2.agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface.LogLevels;
import it.unicam.sensorsimulator.plugin.heedv2.agent.behaviour.SimulationControlBehaviour;
import it.unicam.sensorsimulator.plugin.heedv2.agent.config.HeedAgentConfiguration;
import it.unicam.sensorsimulator.plugin.heedv2.messages.Heedv2ClusterMeasureMessage;
import it.unicam.sensorsimulator.plugin.heedv2.messages.Heedv2Message;
import it.unicam.sensorsimulator.plugin.heedv2.messages.MessageTypes;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class Heedv2Agent extends Agent {

	private LogFileWriterInterface log;
	private HeedAgentConfiguration config;
	
	private HashMap<String, Integer> receivedMessageCounter;
	private HashMap<String, Integer> sentMessageCounter;
	private HashMap<Integer, Heedv2Message> tentativeClusterHeadList;
	private HashMap<Integer, Heedv2Message> finalClusterHeadList;
	private HashMap<Integer, HeedAgentConfiguration> neighborList;
	
	private ArrayList<Integer> mySuccessorsList;
	private AID coordinatorAID;
	
	private boolean isClusterHead = false;
	private int clusterHeadID = -1;
	private boolean terminationRequest = false;

	protected void setup() {
		initVarsAndDS();
		initAndSetArguments();
		log.logAgentAction(LogLevels.INFO, getAID().getLocalName()
				+ " is up and waits");
		addBehaviour(new MessagingBehaviour(this));
		addBehaviour(new SimulationControlBehaviour(this));
	}

	private void initVarsAndDS() {
		sentMessageCounter = new HashMap<String, Integer>();
		receivedMessageCounter = new HashMap<String, Integer>();
		mySuccessorsList = new ArrayList<Integer>();
		tentativeClusterHeadList = new HashMap<Integer, Heedv2Message>();
		finalClusterHeadList = new HashMap<Integer, Heedv2Message>();
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

		builder.append(";tentativeClusterHeadList;");
		builder.append(tentativeClusterHeadList.keySet());
		builder.append(";finalClusterHeadList;");
		builder.append(finalClusterHeadList.keySet());
		
		builder.append(";" +string +";");
		
		log.logAgentAction(LogLevels.INFO, builder.toString());
	}

	public void receiveMessageCounter(ACLMessage message) {
		if(!receivedMessageCounter.containsKey(message.getConversationId())){
			receivedMessageCounter.put(message.getConversationId(), 1);
		}else if(receivedMessageCounter.containsKey(message.getConversationId())){
			int counter = receivedMessageCounter.get(message.getConversationId());
			receivedMessageCounter.put(message.getConversationId(), ++counter );
		}

		log.logAgentMessageReceived(this.getAID().getLocalName(), message.getConversationId(), message.getSender().getLocalName());
	}
	
	public void sendMessage(ACLMessage message) {
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
		notifyCoordinator(MessageTypes.SIMULATION_CONTROLS_JOINED_CLUSTER);
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
		
		switch(conversationID){
		case MessageTypes.HEED_JOIN_CLUSTER:
			try {
				message.setContentObject(new Heedv2ClusterMeasureMessage(getAgentConfiguration().getAgentID(), mySuccessorsList));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
			default: break;
		}
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

	public void addToTentativeCHList(int agentID, Heedv2Message message) {
		tentativeClusterHeadList.put(agentID, message);
	}

	public void addToFinalCHList(int agentID, Heedv2Message message) {
		finalClusterHeadList.put(agentID, message);
	}

	public HashMap<Integer, Heedv2Message> getFinalClusterHeadList() {
		return finalClusterHeadList;
	}
	
	public HashMap<Integer, Heedv2Message> getTentativeClusterHeadList() {
		return tentativeClusterHeadList;
	}

	public void isClusterHead(boolean b) {
		if(!isClusterHead && b){
			notifyCoordinator(MessageTypes.SIMULATION_CONTROLS_BECAME_CLUSTER_HEAD);
			isClusterHead  = b;
		}
	}

	public boolean isClusterHead() {
		return isClusterHead;
	}

	public void setTerminationRequest(boolean b) {
		terminationRequest  = b;
	}

	public boolean getTerminationRequest() {
		return terminationRequest;
	}

	public ArrayList<Integer> getMySuccessorList() {
		return mySuccessorsList;
	}
}
