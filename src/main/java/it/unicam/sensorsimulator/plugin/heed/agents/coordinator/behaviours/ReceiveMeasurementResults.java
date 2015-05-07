package it.unicam.sensorsimulator.plugin.heed.agents.coordinator.behaviours;

import java.util.ArrayList;
import java.util.HashMap;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.plugin.heed.agents.coordinator.SimulationCoordinatorAgent;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes.MessageHandling;
import jade.core.AID;
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
			simulationCoordinatorAgent.receiveMessageCounter(msg,
					MessageHandling.INCREASE);
			switch (msg.getConversationId()) {
			case MessageTypes.M1:
				handleM1(msg);
				break;
			default:
				simulationCoordinatorAgent.putBack(msg);
				simulationCoordinatorAgent.receiveMessageCounter(msg,
						MessageHandling.DECREASE);
				break;
			}
		}
	}
	
	
	private void handleM1(ACLMessage msg) {
		ArrayList<Integer> results = null;
		try {
			 results = (ArrayList<Integer>) msg.getContentObject();
			 simulationCoordinatorAgent.getReportingHandler().publish(convertAIDToInteger(msg.getSender()), results);
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
	}

	private Integer convertAIDToInteger(AID aid) {
		return Integer.parseInt(aid.getLocalName());
	}
}
