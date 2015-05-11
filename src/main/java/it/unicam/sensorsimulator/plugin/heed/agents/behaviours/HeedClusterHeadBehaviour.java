package it.unicam.sensorsimulator.plugin.heed.agents.behaviours;

import java.io.IOException;

import it.unicam.sensorsimulator.plugin.heed.agents.GeneralAgent;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes.MessageHandling;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class HeedClusterHeadBehaviour extends Behaviour {

	private GeneralAgent agent;
	
	public HeedClusterHeadBehaviour(GeneralAgent agent) {
		this.agent = agent;
		sendClusterHeadInfoToCoordinator();
	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		if (msg != null) {
			switch (msg.getConversationId()) {
			case MessageTypes.JOIN_CLUSTER:
				agent.receiveMessageCounter(msg, MessageHandling.INCREASE);
				handleJoinHeedMessage(msg);
				break;
			default:
				agent.putBack(msg);
				break;
			}
		}
	}

	@Override
	public boolean done() {
		return false;
	}
	
	private void sendClusterHeadInfoToCoordinator() {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(MessageTypes.MEASUREMENT_PROTOCOL_BECAME_CH);
		message.addReceiver(agent.getSimulationCoordinatorAID());
		agent.sendMessage(message);
	}
	
	private void measurementJoinCluster() {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(MessageTypes.MEASUREMENT_CLUSTERFORMING_PROTOCOL);
		try {
			message.setContentObject(agent.getMyClusterView());
		} catch (IOException e) {
			e.printStackTrace();
		}
		message.addReceiver(agent.getSimulationCoordinatorAID());
		agent.sendMessage(message);
	}
	
	private void handleJoinHeedMessage(ACLMessage msg) {
		agent.addToMyCluster(agent.convertAgentAIDToInteger(msg.getSender()));
		measurementJoinCluster();
	}

}
