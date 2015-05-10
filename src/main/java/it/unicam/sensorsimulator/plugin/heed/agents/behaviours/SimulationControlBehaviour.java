package it.unicam.sensorsimulator.plugin.heed.agents.behaviours;

import java.io.IOException;
import it.unicam.sensorsimulator.plugin.heed.agents.GeneralAgent;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes.MessageHandling;
import it.unicam.sensorsimulator.plugin.heed.reporting.AgentStatistic;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class SimulationControlBehaviour extends Behaviour {

	private GeneralAgent agent;
	
	public SimulationControlBehaviour(GeneralAgent agent) {
		this.agent = agent;
	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		
		if (msg != null) {
			switch (msg.getConversationId()) {
			case MessageTypes.SIMULATION_CONTROLS_START_INIZIALIZATION:
				agent.receiveMessageCounter(msg, MessageHandling.INCREASE);
				agent.setSimulationCoordinatorAID(msg.getSender());
				agent.startHeedProtocol();
				break;
			case MessageTypes.SIMULATION_CONTROLS_END:
				sendStatistics();
				agent.doDelete();
				break;
			default:
				agent.putBack(msg);
				break;
			}
		}
	}

	private void sendStatistics() {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(MessageTypes.MEASUREMENT_AGENT_STATISTICS);
		try {
			message.setContentObject(new AgentStatistic(agent.getSentMessageCounter(), agent.getReceivedMessageCounter()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		message.addReceiver(agent.getSimulationCoordinatorAID());
		agent.sendMessage(message);
	}

	@Override
	public boolean done() {
		return false;
	}

}
