package it.unicam.sensorsimulator.plugin.heed.agents.coordinator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface;
import it.unicam.sensorsimulator.interfaces.SimulationCoordinatorAgentInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface.LogLevels;
import it.unicam.sensorsimulator.plugin.heed.agents.GeneralAgent;
import it.unicam.sensorsimulator.plugin.heed.agents.coordinator.behaviours.ReceiveMeasurementResults;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes.MessageHandling;
import it.unicam.sensorsimulator.plugin.heed.reporting.AgentStatistic;
import it.unicam.sensorsimulator.plugin.heed.reporting.ReportingModule;
import it.unicam.sensorsimulator.plugin.heed.reporting.RunResults;
import it.unicam.sensorsimulator.plugin.heed.simulation.SimulationRunFile;

public class SimulationCoordinatorAgent extends Agent implements SimulationCoordinatorAgentInterface {
	
	private SimulationRunFile simRunFile;
	private LogFileWriterInterface log;
	private HashMap<Integer, AgentController> agentList;
	private HashMap<Integer, GeneralAgentInterface> agentNetworkList;
	private HashMap<String, Integer> sentMessageCounter;
	private HashMap<String, Integer> receivedMessageCounter;
	private ReportingModule reportingHandler;
	
	private int run = 0;
	private RunResults runResults;
	private HashMap<Integer, RunResults> runResultList;
	
	protected void setup(){
		runResultList = new HashMap<Integer, RunResults>();
		runResults = new RunResults(run);

		initAndSetArguments();
		try {
			generateNetworkList();
			loadAgents();
			startAgents();
			addBehaviour(new ReceiveMeasurementResults(this));
			sendInizializationTrigger();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
	private void sendInizializationTrigger() {
		for(Entry<Integer, GeneralAgentInterface> agent : getAgentNetworkList().entrySet()){
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			message.setConversationId(MessageTypes.SIMULATION_CONTROLS_START_INIZIALIZATION);
			message.addReceiver(convertAgentIDToAID(agent.getKey()));
			sendMessage(message);
		}
	}

	private void generateNetworkList() {
		agentNetworkList = new HashMap<Integer, GeneralAgentInterface>();
		for(GeneralAgentInterface agent : simRunFile.getAgentList()){
			agentNetworkList.put(agent.getAgentID(), agent);
		}
	}

	private void startAgents() throws StaleProxyException {
		for(Entry<Integer, AgentController> agent : agentList.entrySet()){
			agent.getValue().start();
			log.logCoordinatorAction(LogLevels.INFO, "Agent " +agent.getKey() + " was started");
		}
	}
	
	private void loadAgents() throws StaleProxyException {
		agentList = new HashMap<Integer, AgentController>();
		AgentContainer container = getContainerController();
		for(GeneralAgentInterface agent : simRunFile.getAgentList()){
			
			String name = Integer.toString(agent.getAgentID());
			Object[] args = new Object[4];
			args[0] = log;
			args[1] = agent;
			args[2] = agentNetworkList;
			args[3] = simRunFile.getGenerateRandomCosts();
			
			agentList.put(agent.getAgentID(), container.createNewAgent(name, GeneralAgent.class.getCanonicalName(), args));
			log.logCoordinatorAction(LogLevels.INFO, "Agent " +agent.getAgentID() + " has been created");
		}
	}
	
	private HashMap<Integer, GeneralAgentInterface> calculateNeighborsList(int agentID) {
		double wifiRange = 30;
		HashMap<Integer, GeneralAgentInterface> nList = new HashMap<Integer, GeneralAgentInterface>();
		
		for(Entry<Integer, GeneralAgentInterface> agent2 : getAgentNetworkList().entrySet()){
			if(doCirclesIntersect(getAgentNetworkList().get(agentID).getLocationX(), getAgentNetworkList().get(agentID).getLocationY(), wifiRange, agent2.getValue().getLocationX(), agent2.getValue().getLocationY(), wifiRange)){
				nList.put(agent2.getKey(), agent2.getValue());
			}
		}
		return nList;
	}

	public static boolean doCirclesIntersect(double x1, double y1, double r1,
			double x2, double y2, double r2) {
		return (Math.abs(r1 - r2) <= Math.sqrt(Math.pow((x1 - x2), 2)
				+ Math.pow((y1 - y2), 2)))
				&& (Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)) <= (r1 + r2));
	}
	
	public HashMap<Integer, GeneralAgentInterface> getAgentNetworkList(){
		return agentNetworkList;
	}

	private void initAndSetArguments() {
		Object[] args = getArguments();
		log = (LogFileWriterInterface) args[0];
        simRunFile = (SimulationRunFile) args[1];
        reportingHandler = (ReportingModule) args[2];
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

	public ReportingModule getReportingHandler() {
		return reportingHandler;
	}

	public int convertAIDToInteger(AID aid) {
		return Integer.parseInt(aid.getLocalName());
	}

	public HashMap<Integer, AgentController> getAgentControllerList() {
		return agentList;
	}

	
	
	public void addStatistics(int agentID, AgentStatistic statisticFile) {
		runResults.addAgentStatistics(agentID, statisticFile);
	}

	public void addClusterHead(int clusterHeadID) {
		if(!runResults.getClusterHeadList().contains(clusterHeadID)){
			runResults.addClusterHead(clusterHeadID);
		}
	}
	
	public void addProtocolMeasurement(int clusterHead,
			ArrayList<Integer> clusterMembers) {
		runResults.addProtocolMeasurement(clusterHead, clusterMembers);		
	}

	public RunResults getRunResults() {
		return runResults;
	}

	public SimulationRunFile getSimulationRunFile() {
		return simRunFile;
	}

	public AID convertAgentIDToAID(int agentID) {
		return new AID(Integer.toString(agentID), AID.ISLOCALNAME);
	}

	public void initiateNewRunOrEnd() {
		runResults.addCoordinatorStatistics(new AgentStatistic(sentMessageCounter, receivedMessageCounter));
		if(run==simRunFile.getNumberOfRuns()){
			getReportingHandler().addRunResults(runResultList);
			this.doDelete();
		}else{
			run++;
			runResultList.put(runResults.getRunID(), runResults);
			runResults = new RunResults(run);
			resetStatistics();
			try {
				loadAgents();
				startAgents();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
			sendInizializationTrigger();
		}
	}

	private void resetStatistics() {
		sentMessageCounter.clear();
		receivedMessageCounter.clear();
	}
}
