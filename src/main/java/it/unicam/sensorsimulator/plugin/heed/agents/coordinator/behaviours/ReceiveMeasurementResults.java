package it.unicam.sensorsimulator.plugin.heed.agents.coordinator.behaviours;

import java.util.ArrayList;
import java.util.HashMap;

import it.unicam.sensorsimulator.plugin.heed.agents.coordinator.SimulationCoordinatorAgent;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes.MessageHandling;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class ReceiveMeasurementResults extends CyclicBehaviour {

	private SimulationCoordinatorAgent simulationCoordinatorAgent;
	private HashMap<Integer, ArrayList<Integer>> m1;

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
				handleClusterFormingEnd(msg);
				break;
			default:
				simulationCoordinatorAgent.putBack(msg);
//				simulationCoordinatorAgent.receiveMessageCounter(msg,MessageHandling.DECREASE);
				break;
			}
		}
	}
	
	private void handleClusterFormingEnd(ACLMessage msg) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(MessageTypes.SIMULATION_CONTROLS_END);
		message.addReceiver(msg.getSender());
		simulationCoordinatorAgent.sendMessage(message);
	}

	private void handleClusterFormingProtocol(ACLMessage msg) {
		ArrayList<Integer> clusterMembers = null;
		try {
			 clusterMembers = (ArrayList<Integer>) msg.getContentObject();
			 simulationCoordinatorAgent.getReportingHandler().addProtocolMeasurement(simulationCoordinatorAgent.convertAIDToInteger(msg.getSender()), clusterMembers);
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
	}
}
