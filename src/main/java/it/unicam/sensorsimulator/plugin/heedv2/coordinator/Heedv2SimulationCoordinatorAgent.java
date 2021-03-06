package it.unicam.sensorsimulator.plugin.heedv2.coordinator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javafx.application.Platform;
import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface.LogLevels;
import it.unicam.sensorsimulator.interfaces.SimulationControlInterface;
import it.unicam.sensorsimulator.plugin.heedv2.Heedv2SimulationRunFile;
import it.unicam.sensorsimulator.plugin.heedv2.agent.Heedv2Agent;
import it.unicam.sensorsimulator.plugin.heedv2.agent.config.HeedAgentConfiguration;
import it.unicam.sensorsimulator.plugin.heedv2.coordinator.behaviour.SimulationContolBehaviour;
import it.unicam.sensorsimulator.plugin.heedv2.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.report.HeedAgentStatistic;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.report.Heedv2Report;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.report.Heedv2RunReport;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

public class Heedv2SimulationCoordinatorAgent extends Agent {

	private Heedv2Report report;
	private Heedv2RunReport runReport;
	private int simulationRun = 0;
	private LogFileWriterInterface log;
	private Heedv2SimulationRunFile simulationRunFile;
	private SimulationControlInterface simulationController;
	private HashMap<String, Integer> sentMessageCounter;
	private HashMap<String, Integer> receivedMessageCounter;
	private HashMap<Integer, HeedAgentConfiguration> agentNetworkList;
	
	protected void setup() {
		report = new Heedv2Report();
		simulationRun = 1;
		runReport = new Heedv2RunReport(simulationRun);

		initAndSetArguments();
		startSimulation();
		try {
			loadAndStartAgents();
			addBehaviour(new SimulationContolBehaviour(this));
			sendInizializationTrigger();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
	private void startSimulation(){
		Platform.runLater(new Runnable() { 
            public void run() {
            	simulationController.startSimulation();
            }
        });
	}

	private void sendInizializationTrigger() {
		runReport.setStartTime(System.currentTimeMillis());
		for(GeneralAgentInterface agent : simulationRunFile.getAgentList()){
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			message.setConversationId(MessageTypes.SIMULATION_CONTROLS_START_INIZIALIZATION);
			message.addReceiver(convertAgentIDToAID(agent.getAgentID()));
			sendMessage(message);
		}
	}

	private AID convertAgentIDToAID(Integer agentID) {
		return new AID(Integer.toString(agentID), AID.ISLOCALNAME);
	}

	private void loadAndStartAgents() throws StaleProxyException {
		AgentContainer container = getContainerController();

		for (GeneralAgentInterface agent : simulationRunFile.getAgentList()) {

			String name = Integer.toString(agent.getAgentID());
			Object[] args = new Object[3];
			args[0] = log;
			args[1] = agent;
			args[2] = generateNeighborList(agent.getAgentID());

			container.createNewAgent(name,
							Heedv2Agent.class.getCanonicalName(), args).start();
			log.logCoordinatorAction(LogLevels.INFO,
					"Agent " + agent.getAgentID() + " has been created");
		}
	}

	private HashMap<Integer, HeedAgentConfiguration> generateNeighborList(int agentID) {
		HashMap<Integer, HeedAgentConfiguration> nList = new HashMap<Integer, HeedAgentConfiguration>();
		
		for(Entry<Integer, HeedAgentConfiguration> agent2 : agentNetworkList.entrySet()){
			if(doCirclesIntersect(agentNetworkList.get(agentID).getLocationX(), agentNetworkList.get(agentID).getLocationY(), agentNetworkList.get(agentID).getAgentRadius(), agent2.getValue().getLocationX(), agent2.getValue().getLocationY(), agent2.getValue().getAgentRadius())){
				nList.put(agent2.getKey(), agent2.getValue());
			}
		}
		return nList;
	}

	private boolean doCirclesIntersect(double x1, double y1, double r1,
			double x2, double y2, double r2) {
		return (Math.abs(r1 - r2) <= Math.sqrt(Math.pow((x1 - x2), 2)
				+ Math.pow((y1 - y2), 2)))
				&& (Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)) <= (r1 + r2));
	}

	private void initAndSetArguments() {
		Object[] args = getArguments();
		log = (LogFileWriterInterface) args[0];
		simulationRunFile = (Heedv2SimulationRunFile) args[1];
		report.setSimulationRunFile(simulationRunFile);
		simulationController = (SimulationControlInterface) args[2];
		createAgentNetworkList();
	}
	
	private void createAgentNetworkList() {
		agentNetworkList = new HashMap<Integer, HeedAgentConfiguration>();
		for(GeneralAgentInterface agent : simulationRunFile.getAgentList()){
			//convert to Heed Agent
			HeedAgentConfiguration heedAgent = (HeedAgentConfiguration) agent;
			agentNetworkList.put(heedAgent.getAgentID(), heedAgent);
		}
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

	public HashMap<Integer, HeedAgentConfiguration> getAgentList() {
		return agentNetworkList;
	}
	
	public AID convertIntegerToAID(int agentID) {
		return new AID(Integer.toString(agentID), AID.ISLOCALNAME);
	}
	
	public int convertAIDToInteger(AID aid) {
		return Integer.parseInt(aid.getLocalName());
	}

	public void addToReport(HeedAgentStatistic statistic) {
		runReport.addAgentStatistic(statistic);
	}

	public int getSimulationRunCounter() {
		return simulationRun;
	}

	public int getNoOfSimulationRuns() {
		return simulationRunFile.getNumberOfRuns();
	}

	public void restartSimulation() {
		simulationRun++;
		receivedMessageCounter.clear();
		sentMessageCounter.clear();
		runReport = new Heedv2RunReport(simulationRun);
		try {
			loadAndStartAgents();
			sendInizializationTrigger();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}

	public void addOwnStatisticsAndAddToReport() {
		runReport.setCoordinatorStatistic(new HeedAgentStatistic(0, sentMessageCounter, receivedMessageCounter));
		report.addRun(runReport);
	}

	public void transferReport() {
		Platform.runLater(new Runnable() { 
            public void run() {
            	simulationController.addAndCreateReport(report);
            }
        });
	}

	public void track(String string) {
		StringBuilder builder = new StringBuilder();
		builder.append(";coordinator;");
		builder.append(";getNoOfSimulationRuns;");
		builder.append(getNoOfSimulationRuns());
		builder.append(";getSimulationRunCounter;");
		builder.append(getSimulationRunCounter());
		
		builder.append(string);
		
		log.logCoordinatorAction(LogLevels.INFO, builder.toString());
	}

	public void addNodesLists(ArrayList<Integer> clusterHeadList,
			ArrayList<Integer> successorList) {
		runReport.setClusterHeadList(clusterHeadList);
		runReport.setSuccessorList(successorList);
	}

	public void setStopTime(long timeMillis) {
		runReport.setStopTime(timeMillis);
	}
}
