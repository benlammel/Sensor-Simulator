package it.unicam.sensorsimulator.plugin.example.coordinator;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javafx.application.Platform;
import it.unicam.sensorsimulator.interfaces.AbstractSimulationCoordinatorAgent;
import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface;
import it.unicam.sensorsimulator.interfaces.SimulationControlInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface.LogLevels;
import it.unicam.sensorsimulator.plugin.example.ExampleSimulationRunFile;
import it.unicam.sensorsimulator.plugin.example.agent.ExampleAgent;
import it.unicam.sensorsimulator.plugin.example.agent.config.ExampleAgentConfiguration;
import it.unicam.sensorsimulator.plugin.example.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.example.reporting.report.ExampleAgentStatistics;
import it.unicam.sensorsimulator.plugin.example.reporting.report.ExampleReport;
import it.unicam.sensorsimulator.plugin.example.reporting.report.ExampleRunReport;

public class ExampleSimulationCoordinatorAgent extends AbstractSimulationCoordinatorAgent {

	private ExampleReport report;
	private ExampleRunReport runReport;
	private int runCounter = 0;
	private LogFileWriterInterface log;
	private ExampleSimulationRunFile simulationRunFile;
	private SimulationControlInterface simulationController;
	private HashMap<String, Integer> sentMessageCounter;
	private HashMap<String, Integer> receivedMessageCounter;

	@Override
	protected void setup() {
		runCounter++;
		report = new ExampleReport();
		runReport = new ExampleRunReport(runCounter);
		initAndSetArguments();
		
		try {
			startSimulation();
			loadAndStartAgents();
			addBehaviour(new ExampleSimulationContolBehaviour(this));
			broadcastStartTrigger();
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

	private void broadcastStartTrigger() {
		for(GeneralAgentInterface agent : simulationRunFile.getAgentList()){
			sendTrigger(convertIntegerToAID(agent.getAgentID()), MessageTypes.FIRST_MESSAGE);
		}
	}

	private void sendTrigger(AID receiver, String conversationID) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(conversationID);
		message.addReceiver(receiver);
		sendMessage(message);
	}
	
	public AID convertIntegerToAID(int agentID) {
		return new AID(Integer.toString(agentID), AID.ISLOCALNAME);
	}

	private void loadAndStartAgents() throws StaleProxyException {
		AgentContainer container = getContainerController();
		
		for(GeneralAgentInterface agent : simulationRunFile.getAgentList()){
			//casting to ExampleAgentConfiguration for making the additional properties available
			ExampleAgentConfiguration agentConf = (ExampleAgentConfiguration) agent;
			
			String name = Integer.toString(agent.getAgentID());
			Object[] args = new Object[2];
			args[0] = log;
			args[1] = agentConf;

			container.createNewAgent(name, ExampleAgent.class.getCanonicalName(), args).start();
			log.logCoordinatorAction(LogLevels.INFO, "Agent " +name + " was created and started");
		}
	}

	private void initAndSetArguments() {
		Object[] args = getArguments();
		log = (LogFileWriterInterface) args[0];
		simulationRunFile = (ExampleSimulationRunFile) args[1];
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

	public ArrayList<? extends GeneralAgentInterface> getListOfAgents() {
		return simulationRunFile.getAgentList();
	}

	public void addToRunReport(ExampleAgentStatistics statistics) {
		runReport.addAgentStatistics(statistics);
	}

	public void finalizeRun() {
		runReport.setCoordinatorStatsitics(new ExampleAgentStatistics(sentMessageCounter, receivedMessageCounter));
		report.addRun(runReport);
		if(runCounter==simulationRunFile.getNumberOfRuns()){
			transferReport();
		}else{
			//clear statistics
			receivedMessageCounter.clear();
			sentMessageCounter.clear();
			
			//increase counter and create run report
			runReport = new ExampleRunReport(++runCounter);
			
			//start
			try {
				loadAndStartAgents();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
	}

	private void transferReport() {
		Platform.runLater(new Runnable() { 
            public void run() {
            	simulationController.addAndCreateReport(report);
            }
        });
	}
}
