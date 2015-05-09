package it.unicam.sensorsimulator.plugin.heed.agents.behaviours;

import it.unicam.sensorsimulator.plugin.heed.agents.GeneralAgent;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes.MessageHandling;
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
			case MessageTypes.SIMULATION_CONTROLS_END:
				agent.doDelete();
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

}
