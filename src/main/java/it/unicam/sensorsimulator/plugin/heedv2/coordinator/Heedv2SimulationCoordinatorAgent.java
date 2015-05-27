package it.unicam.sensorsimulator.plugin.heedv2.coordinator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface.LogLevels;
import it.unicam.sensorsimulator.plugin.heedv2.Heedv2SimulationRunFile;
import it.unicam.sensorsimulator.plugin.heedv2.agent.Heedv2Agent;
import it.unicam.sensorsimulator.plugin.heedv2.coordinator.behaviour.SimulationContolBehaviour;
import it.unicam.sensorsimulator.plugin.heedv2.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heedv2.messages.MessageTypes.MessageHandling;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.Heedv2PluginReportingModule;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.RunReport;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Heedv2SimulationCoordinatorAgent extends Agent {

	private RunReport report;
	private LogFileWriterInterface log;
	private Heedv2SimulationRunFile simulationRunFile;
	private Heedv2PluginReportingModule reportingHandler;
	private HashMap<Integer, AgentController> agentList;
	private HashMap<String, Integer> sentMessageCounter;
	private HashMap<String, Integer> receivedMessageCounter;


	protected void setup() {
		report = new RunReport();

		initAndSetArguments();
		try {
			loadAgents();
			startAgents();
			addBehaviour(new SimulationContolBehaviour(this));
			sendInizializationTrigger();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}

	private void sendInizializationTrigger() {
		for(Entry<Integer, AgentController> agent : agentList.entrySet()){
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			message.setConversationId(MessageTypes.SIMULATION_CONTROLS_START_INIZIALIZATION);
			message.addReceiver(convertAgentIDToAID(agent.getKey()));
			sendMessage(message);
		}
	}

	private AID convertAgentIDToAID(Integer agentID) {
		return new AID(Integer.toString(agentID), AID.ISLOCALNAME);
	}

	private void loadAgents() throws StaleProxyException {
		agentList = new HashMap<Integer, AgentController>();
		AgentContainer container = getContainerController();

		for (GeneralAgentInterface agent : simulationRunFile.getAgentList()) {

			String name = Integer.toString(agent.getAgentID());
			Object[] args = new Object[2];
			args[0] = log;
			args[1] = agent;

			agentList.put(
					agent.getAgentID(),
					container.createNewAgent(name,
							Heedv2Agent.class.getCanonicalName(), args));
			log.logCoordinatorAction(LogLevels.INFO,
					"Agent " + agent.getAgentID() + " has been created");
		}

	}

	private void startAgents() throws StaleProxyException {
		for(Entry<Integer, AgentController> agent : agentList.entrySet()){
			agent.getValue().start();
			log.logCoordinatorAction(LogLevels.INFO, "Agent " +agent.getKey() + " was started");
		}
	}

	private void initAndSetArguments() {
		Object[] args = getArguments();
		log = (LogFileWriterInterface) args[0];
		simulationRunFile = (Heedv2SimulationRunFile) args[1];
		reportingHandler = (Heedv2PluginReportingModule) args[2];
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
}
