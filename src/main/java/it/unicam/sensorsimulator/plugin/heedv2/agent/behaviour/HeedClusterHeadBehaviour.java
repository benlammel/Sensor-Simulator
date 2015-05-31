package it.unicam.sensorsimulator.plugin.heedv2.agent.behaviour;

import it.unicam.sensorsimulator.plugin.heedv2.agent.Heedv2Agent;
import it.unicam.sensorsimulator.plugin.heedv2.messages.MessageTypes;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class HeedClusterHeadBehaviour extends Behaviour {
	
	private Heedv2Agent agent;

	public HeedClusterHeadBehaviour(Heedv2Agent agent) {
		this.agent = agent;
	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		
		if (msg != null) {
			switch (msg.getConversationId()) {
			case MessageTypes.HEED_JOIN_CLUSTER:
				agent.receiveMessageCounter(msg);
				joinClusterMsgHandler(agent.convertAIDToInteger(msg.getSender()));
				break;
			default:
				agent.putBack(msg);
				break;
			}
		}
	}

	private void joinClusterMsgHandler(int agentID) {
		agent.addToMyCluster(agentID);
	}


	@Override
	public boolean done() {
		return false;
	}

}
