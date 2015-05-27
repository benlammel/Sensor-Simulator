package it.unicam.sensorsimulator.plugin.heedv2.agent.behaviour;

import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes.MessageHandling;
import it.unicam.sensorsimulator.plugin.heedv2.agent.Heedv2Agent;
import it.unicam.sensorsimulator.plugin.heedv2.messages.MessageTypes;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class Heedv2Behaviour extends Behaviour {
	
	private Heedv2Agent agent;

	public Heedv2Behaviour(Heedv2Agent heedv2Agent) {
		this.agent = heedv2Agent;
		System.out.println("Agent started");
	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		
		if (msg != null) {
			switch (msg.getConversationId()) {
			case MessageTypes.SIMULATION_CONTROLS_START_INIZIALIZATION:
				agent.receiveMessageCounter(msg, MessageHandling.INCREASE);
				System.out.println(agent.getName() +" received SIMULATION_CONTROLS_START_INIZIALIZATION");
				break;
			}
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
