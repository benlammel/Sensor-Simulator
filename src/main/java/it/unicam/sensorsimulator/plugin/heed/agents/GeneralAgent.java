package it.unicam.sensorsimulator.plugin.heed.agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface.LogLevels;
import it.unicam.sensorsimulator.plugin.heed.agents.behaviours.HeedClusterHeadBehaviour;
import it.unicam.sensorsimulator.plugin.heed.agents.behaviours.HeedProtocolBehaviour;
import it.unicam.sensorsimulator.plugin.heed.agents.behaviours.HeedProtocolMessageHandler;
import it.unicam.sensorsimulator.plugin.heed.agents.behaviours.SimulationControlBehaviour;
import it.unicam.sensorsimulator.plugin.heed.messages.HeedMessage;
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
	private boolean isClusterHead = false;
	
	/* Protocol Stuff */
	private double eMax;
	private double eResidential;
	private double pMin;
	private ArrayList<Integer> myClusterAgents;
	private HashMap<Integer, HeedMessage> listOfClusterHeads;
	private HashMap<Integer, Double> myCostPlan;

	protected void setup(){
		initAndSetArguments();
		log.logAgentAction(LogLevels.INFO, getAID().getLocalName() +" is up and waits");
		listOfClusterHeads = new HashMap<Integer, HeedMessage>();
		myClusterAgents = new ArrayList<Integer>();
		
		addBehaviour(new SimulationControlBehaviour(this));
	}
	
	private void initAndSetArguments() {
		Object [] args = getArguments();
		log = (LogFileWriterInterface) args[0];
		config = (AgentConfiguration) args[1];
		nList = (HashMap<Integer, GeneralAgentInterface>) args[2];
		boolean costsRandom = (boolean) args[3];
		if(costsRandom){
			createCostPlan();
		}
		setAndApplyConfig();
	}

	private void createCostPlan() {
		myCostPlan = new HashMap<Integer, Double>();
		for(Entry<Integer, GeneralAgentInterface> agent : nList.entrySet()){
			myCostPlan.put(agent.getKey(), Math.random());
		}
	}

	private void setAndApplyConfig() {
		log.logAgentAction(LogLevels.INFO, config.toString());
		this.setEMax(config.geteMax());
		this.setEResidential(config.geteResidential());
		this.setpMin(config.getpMin());
	}

	private void setpMin(double pMin) {
		this.pMin = pMin;	
	}

	private void setEResidential(double eResidential) {
		this.eResidential = eResidential;		
	}

	private void setEMax(double eMax) {
		this.eMax = eMax;
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
	
	public double getpMin() {
		return pMin;
	}

	public double geteMax() {
		return eMax;
	}

	public double geteResidual() {
		return eResidential;
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

	public void setToClusterHead(boolean b) {
		if(!isClusterHead && b){
			addBehaviour(new HeedClusterHeadBehaviour(this));
			track("added cluster head behaviour");
		}
		isClusterHead  = b;
	}
	
	public boolean isClusterHead() {
		return isClusterHead;
	}

	public boolean isConnectedToClusterHeadOrIsClusterHead() {
		if(isClusterHead || clusterHead!=0){
			return true;
		}else{
			return false;
		}
	}

	public void startHeedProtocol() {
		HeedProtocolMessageHandler messageHandler = new HeedProtocolMessageHandler(this);
		addBehaviour(messageHandler);
		
		HeedProtocolBehaviour heedProtocolBehaviour = new HeedProtocolBehaviour(this, 300);
		addBehaviour(heedProtocolBehaviour);
	}

	public HashMap<Integer, HeedMessage> getListOfClusterHeads() {
		return listOfClusterHeads;
	}

	public void addToListOfClusterHeads(int chID, HeedMessage message) {
		listOfClusterHeads.put(chID, message);
	}

	public void track(String string) {
		StringBuilder builder = new StringBuilder();
		builder.append(";agentID;");
		builder.append(this.getAgentConfiguration().getAgentID());
		builder.append(string);
		builder.append(";isClusterHead;");
		builder.append(isClusterHead);
		builder.append(";listOfClusterHeads;");
		builder.append(listOfClusterHeads);
		builder.append(";myClusterAgents;");
		builder.append(myClusterAgents);
		log.logAgentAction(LogLevels.INFO, builder.toString());
	}

	public double getCosts(int key) {
		return myCostPlan.get(key);
	}

	public HashMap<String, Integer> getSentMessageCounter() {
		return sentMessageCounter;
	}

	public HashMap<String, Integer> getReceivedMessageCounter() {
		return receivedMessageCounter;
	}
}
