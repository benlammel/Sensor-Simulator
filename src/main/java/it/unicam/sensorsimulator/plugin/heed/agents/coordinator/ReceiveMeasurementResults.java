package it.unicam.sensorsimulator.plugin.heed.agents.coordinator;
/*
import java.util.HashMap;
import java.util.Set;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.plugin.basestation.measurements.Measurement1;
import it.unicam.sensorsimulator.plugin.basestation.messages.MessageTypes;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class ReceiveMeasurementResults extends CyclicBehaviour {

	private SimulationCoordinatorAgent simulationCoordinatorAgent;

	private HashMap<Integer, Set<Integer>> measurement1 = new Measurement1();
	
	public ReceiveMeasurementResults(
			SimulationCoordinatorAgent simulationCoordinatorAgent) {
		this.simulationCoordinatorAgent = simulationCoordinatorAgent;
	}

	@Override
	public void action() {
		ACLMessage msg = simulationCoordinatorAgent.receive();
		if (msg != null) {
			simulationCoordinatorAgent.receiveMessageCounter(msg);
			switch (msg.getConversationId()) {
			case MessageTypes.MEASUREMENT_POINT_1:
				handleMeasurementPoint1(msg);
				break;
			default:
				simulationCoordinatorAgent.putBack(msg);
				break;
			}
		}
	}

	private void handleMeasurementPoint1(ACLMessage msg) {
		HashMap<Integer, GeneralAgentInterface> results = null;
		try {
			 results = (HashMap<Integer, GeneralAgentInterface>) msg.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		measurement1.put(convertAIDToInteger(msg.getSender()), results.keySet());
		if(measurement1.size()==simulationCoordinatorAgent.getAgentList().size()){
			simulationCoordinatorAgent.getReportingHandler().publish(measurement1);
		}
	}
	
	private Integer convertAIDToInteger(AID aid) {
		return Integer.parseInt(aid.getLocalName());
	}
}*/
