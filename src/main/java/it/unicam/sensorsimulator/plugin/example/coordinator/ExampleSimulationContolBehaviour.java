package it.unicam.sensorsimulator.plugin.example.coordinator;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.plugin.example.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.example.reporting.report.ExampleAgentStatistics;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class ExampleSimulationContolBehaviour extends Behaviour {

	private ExampleSimulationCoordinatorAgent coordinator;
	private int responses = 0;
	private int measurementResponses = 0;

	public ExampleSimulationContolBehaviour(
			ExampleSimulationCoordinatorAgent coordinator) {
		this.coordinator = coordinator;
	}

	@Override
	public void action() {
		ACLMessage msg = coordinator.receive();
		if (msg != null) {
			switch (msg.getConversationId()) {
			case MessageTypes.FIRST_MESSAGE_REPLY_OPTION_1:
				coordinator.receiveMessageCounter(msg);
				actOnFirstMessageReply(msg);
				checkStopCriterion();
				break;
			case MessageTypes.FIRST_MESSAGE_REPLY_OPTION_2:
				coordinator.receiveMessageCounter(msg);
				actOnSecondMessageReply(msg);
				checkStopCriterion();
				break;
			case MessageTypes.MEASUREMENTS_REPLY:
				parseMeasurementMesssage(msg);
				checkForTemination();
				break;
			}
		}
	}
	
	private void checkForTemination() {
		if(measurementResponses==coordinator.getListOfAgents().size()){
			coordinator.finalizeRun();
		}
	}

	private void parseMeasurementMesssage(ACLMessage msg) {
		measurementResponses ++;
		ExampleAgentStatistics statistics = null;
		try {
			statistics = (ExampleAgentStatistics) msg.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		coordinator.addToRunReport(statistics);
	}

	private void checkStopCriterion() {
		if(responses==coordinator.getListOfAgents().size()){
			broadcastTerminationMessage();
		}
	}

	private void broadcastTerminationMessage() {
		for(GeneralAgentInterface agent : coordinator.getListOfAgents()){
			sendResultAndTerminationRequest(coordinator.convertIntegerToAID(agent.getAgentID()), MessageTypes.TERMINATION_REQUEST);
		}
	}

	private void sendResultAndTerminationRequest(AID receiver, String conversationID) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(conversationID);
		message.addReceiver(receiver);
		coordinator.sendMessage(message);
	}

	private void actOnSecondMessageReply(ACLMessage msg) {
		responses++;
	}

	private void actOnFirstMessageReply(ACLMessage msg) {
		responses++;
	}

	@Override
	public boolean done() {
		return false;
	}

}
