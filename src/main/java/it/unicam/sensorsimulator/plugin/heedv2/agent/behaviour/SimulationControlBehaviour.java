package it.unicam.sensorsimulator.plugin.heedv2.agent.behaviour;

import java.io.IOException;
import it.unicam.sensorsimulator.plugin.heedv2.agent.Heedv2Agent;
import it.unicam.sensorsimulator.plugin.heedv2.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.report.HeedAgentStatistic;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class SimulationControlBehaviour extends Behaviour {

	private Heedv2Agent heedv2Agent;

	public SimulationControlBehaviour(Heedv2Agent heedv2Agent) {
		this.heedv2Agent = heedv2Agent;
	}

	@Override
	public void action() {
		track("SimulationControlBehaviour");
		
		if (heedv2Agent.getTerminationRequest() && heedv2Agent.getCurQueueSize() == 0) {
			prepareAndSendStatisticsAndTerminate();
		}
	}

	private void track(String string) {
		heedv2Agent.track(string);
	}

	private void terminate() {
		heedv2Agent.track("termination");
		heedv2Agent.doDelete();
	}

	private void prepareAndSendStatisticsAndTerminate() {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(MessageTypes.MEASUREMENT_AGENT_STATISTICS);
		try {
			message.setContentObject(new HeedAgentStatistic(heedv2Agent
					.getAgentConfiguration().getAgentID(), heedv2Agent
					.getSentMessageCounter(), heedv2Agent
					.getReceivedMessageCounter()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		message.addReceiver(heedv2Agent.getSimulationCoordinatorAID());
		heedv2Agent.sendMessage(message);
		terminate();
	}

	@Override
	public boolean done() {
		return false;
	}
}
