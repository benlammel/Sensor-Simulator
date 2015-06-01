package it.unicam.sensorsimulator.plugin.heed.agents.coordinator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javafx.application.Platform;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import it.unicam.sensorsimulator.interfaces.AbstractSimulationCoordinatorAgent;
import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface;
import it.unicam.sensorsimulator.interfaces.SimulationControlInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface.LogLevels;
import it.unicam.sensorsimulator.plugin.heed.agents.GeneralAgent;
import it.unicam.sensorsimulator.plugin.heed.agents.coordinator.behaviours.ReceiveMeasurementResults;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes.MessageHandling;
import it.unicam.sensorsimulator.plugin.heed.reporting.report.AgentStatistic;
import it.unicam.sensorsimulator.plugin.heed.reporting.report.HeedReport;
import it.unicam.sensorsimulator.plugin.heed.reporting.report.HeedRunResults;
import it.unicam.sensorsimulator.plugin.heed.simulation.SimulationRunFile;

public class SimulationCoordinatorAgent extends AbstractSimulationCoordinatorAgent {
	
	private SimulationRunFile simRunFile;
	private LogFileWriterInterface log;
	private HashMap<Integer, AgentController> agentList;
	private HashMap<Integer, GeneralAgentInterface> agentNetworkList;
	private HashMap<String, Integer> sentMessageCounter;
	private HashMap<String, Integer> receivedMessageCounter;
	private SimulationControlInterface simulationController;
	
	private int run = 0;
	private HeedRunResults runResults;
	private HeedReport heedReport;
	
	protected void setup(){
		run++;
		heedReport = new HeedReport();
		runResults = new HeedRunResults(run);

		initAndSetArguments();
		Platform.runLater(new Runnable() { 
            public void run() {
            	simulationController.startSimulation();
            }
        });
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
        simulationController = (SimulationControlInterface) args[2];
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

	public HeedRunResults getRunResults() {
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
//			reportingHandler.addAndCreateReport(runResultList);
			Platform.runLater(new Runnable() { 
	            public void run() {
	            	simulationController.addAndCreateReport(heedReport);
	            }
	        });
			this.doDelete();
		}else{
			run++;
			heedReport.addRun(runResults);
			runResults = new HeedRunResults(run);
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
