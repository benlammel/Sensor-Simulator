package it.unicam.sensorsimulator.plugin.heed.agents.behaviours;

import java.io.IOException;
import java.util.Map.Entry;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.plugin.heed.agents.GeneralAgent;
import it.unicam.sensorsimulator.plugin.heed.messages.HeedMessage;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes.MessageHandling;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class HeedClusteringBehaviour extends CyclicBehaviour {

	private GeneralAgent agent;
	private double cProb = 5/100;
	private double chProb = 0;
	private double pMin = 1/10^4;
	
	public HeedClusteringBehaviour(GeneralAgent agent) {
		this.agent = agent;
		calculateCHProb();
	}

	private void calculateCHProb() {
		chProb = cProb*agent.getEResidual()/agent.getEMax();
		
	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		if (msg != null) {
			agent.receiveMessageCounter(msg, MessageHandling.INCREASE);
			switch(msg.getConversationId()){
			case MessageTypes.START_INIZIALIZATION:
				computeAndBroadcastCostToNeighborhood();
				break;
			case MessageTypes.COST_BROADCAST:
				progressCosts(msg);
				break;
			default:
				agent.putBack(msg);
				agent.receiveMessageCounter(msg, MessageHandling.DECREASE);
				break;
			}
		}

	}

	private void progressCosts(ACLMessage msg) {
		HeedMessage heedMessage = unmarshalHeedMessage(msg);
		System.out.println(agent.getAgentConfiguration().getAgentID() +" :: received: " +msg.getSender().getLocalName() +" :: costs: " +heedMessage.getCost());
	}

	private HeedMessage unmarshalHeedMessage(ACLMessage msg) {
		HeedMessage heedMessage = null;
		try {
			heedMessage = (HeedMessage) msg.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		return heedMessage;
	}

	private void computeAndBroadcastCostToNeighborhood() {
		for (Entry<Integer, GeneralAgentInterface> myNeighbor : agent.getNeighborList().entrySet()) {
			if(myNeighbor.getKey() != agent.getAgentConfiguration().getAgentID()){
				double cost = Math.sqrt(Math.pow(agent.getAgentConfiguration().getLocationX() - myNeighbor.getValue().getLocationX(),2)+ Math.pow(agent.getAgentConfiguration().getLocationY()- myNeighbor.getValue().getLocationY(), 2));
				sendMessage(myNeighbor.getKey(), MessageTypes.COST_BROADCAST, cost);
			}
		}
	}

	private void sendMessage(int agentID, String messageType, double value) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(messageType);
		try {
			message.setContentObject(new HeedMessage(messageType, value));
		} catch (IOException e) {
			e.printStackTrace();
		}
		message.addReceiver(agent.convertAgentIDToAID(agentID));
		agent.sendMessage(message);
	}
}
