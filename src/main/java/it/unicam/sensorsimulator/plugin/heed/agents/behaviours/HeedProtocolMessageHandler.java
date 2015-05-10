package it.unicam.sensorsimulator.plugin.heed.agents.behaviours;

import it.unicam.sensorsimulator.plugin.heed.agents.GeneralAgent;
import it.unicam.sensorsimulator.plugin.heed.messages.HeedMessage;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes.MessageHandling;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class HeedProtocolMessageHandler extends Behaviour {

	private GeneralAgent agent;

	public HeedProtocolMessageHandler(GeneralAgent agent) {
		this.agent = agent;
	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		if (msg != null) {
			switch (msg.getConversationId()) {
			case MessageTypes.COST_BROADCAST:
				agent.receiveMessageCounter(msg, MessageHandling.INCREASE);
				handleCostBroadcastMsg(msg);
				break;
			case MessageTypes.CLUSTER_HEAD_MESSAGE:
				agent.receiveMessageCounter(msg, MessageHandling.INCREASE);
				handleClusterHeadMessage(msg);
				break;
			default:
				agent.putBack(msg);
				break;
			}
		}
	}

	private void handleClusterHeadMessage(ACLMessage msg) {
		HeedMessage contentObject = parseMessageObject(msg);
		agent.addToListOfClusterHeads(agent.convertAgentAIDToInteger(msg.getSender()), contentObject);
	}

	private HeedMessage parseMessageObject(ACLMessage msg) {
		HeedMessage heedMessage = null;
		try {
			heedMessage = (HeedMessage) msg.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		return heedMessage;
	}

	private void handleCostBroadcastMsg(ACLMessage msg) {
		//not relevant here
	}

	@Override
	public boolean done() {
		return false;
	}
}
