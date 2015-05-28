package it.unicam.sensorsimulator.plugin.heed.agents.coordinator.behaviours;

import java.util.ArrayList;

import it.unicam.sensorsimulator.plugin.heed.agents.coordinator.SimulationCoordinatorAgent;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes.MessageHandling;
import it.unicam.sensorsimulator.plugin.heed.reporting.report.AgentStatistic;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class ReceiveMeasurementResults extends CyclicBehaviour {

	private SimulationCoordinatorAgent simulationCoordinatorAgent;

	public ReceiveMeasurementResults(
			SimulationCoordinatorAgent simulationCoordinatorAgent) {
		this.simulationCoordinatorAgent = simulationCoordinatorAgent;
	}

	@Override
	public void action() {
		ACLMessage msg = simulationCoordinatorAgent.receive();
		if (msg != null) {
			switch (msg.getConversationId()) {
			case MessageTypes.MEASUREMENT_CLUSTERFORMING_PROTOCOL:
				simulationCoordinatorAgent.receiveMessageCounter(msg, MessageHandling.INCREASE);
				handleClusterFormingProtocol(msg);
				break;
			case MessageTypes.MEASUREMENT_CLUSTER_FORMING_END:
				simulationCoordinatorAgent.receiveMessageCounter(msg, MessageHandling.INCREASE);
				triggerSimulationEnd(msg);
				break;
			case MessageTypes.MEASUREMENT_AGENT_STATISTICS:
				simulationCoordinatorAgent.receiveMessageCounter(msg, MessageHandling.INCREASE);
				handleAgentStatistics(msg);
				checkForSimulationEnd();
				break;
			case MessageTypes.MEASUREMENT_PROTOCOL_BECAME_CH:
				simulationCoordinatorAgent.receiveMessageCounter(msg, MessageHandling.INCREASE);
				handleBecameClusterHead(msg);
				break;
			default:
				simulationCoordinatorAgent.putBack(msg);
				break;
			}
		}
	}
	
	private void checkForSimulationEnd() {
		if(simulationCoordinatorAgent.getRunResults().getAgentStatisticList().size()+simulationCoordinatorAgent.getRunResults().getClusterHeadList().size() == simulationCoordinatorAgent.getSimulationRunFile().getAgentList().size()){
			triggerSimulationEnd(simulationCoordinatorAgent.getRunResults().getClusterHeadList());
		}
		
		if(simulationCoordinatorAgent.getRunResults().getAgentStatisticList().size() == simulationCoordinatorAgent.getSimulationRunFile().getAgentList().size()){
			simulationCoordinatorAgent.initiateNewRunOrEnd();
		}

	}

	private void handleBecameClusterHead(ACLMessage msg) {
		simulationCoordinatorAgent.addClusterHead(simulationCoordinatorAgent.convertAIDToInteger(msg.getSender()));
	}

	private void handleAgentStatistics(ACLMessage msg) {
		try {
			simulationCoordinatorAgent.addStatistics(simulationCoordinatorAgent.convertAIDToInteger(msg.getSender()), (AgentStatistic) msg.getContentObject());
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
	}
	
	private void triggerSimulationEnd(ArrayList<Integer> clusterHeadList) {
		for(int clusterHead : clusterHeadList){
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			message.setConversationId(MessageTypes.SIMULATION_CONTROLS_END);
			message.addReceiver(simulationCoordinatorAgent.convertAgentIDToAID(clusterHead));
			simulationCoordinatorAgent.sendMessage(message);
		}
	}

	private void triggerSimulationEnd(ACLMessage msg) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(MessageTypes.SIMULATION_CONTROLS_END);
		message.addReceiver(msg.getSender());
		simulationCoordinatorAgent.sendMessage(message);
	}

	private void handleClusterFormingProtocol(ACLMessage msg) {
		ArrayList<Integer> clusterMembers = null;
		try {
			 clusterMembers = (ArrayList<Integer>) msg.getContentObject();
//			 simulationCoordinatorAgent.getReportingHandler().addProtocolMeasurement(simulationCoordinatorAgent.convertAIDToInteger(msg.getSender()), clusterMembers);
			 simulationCoordinatorAgent.addProtocolMeasurement(simulationCoordinatorAgent.convertAIDToInteger(msg.getSender()), clusterMembers);
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
	}
}
