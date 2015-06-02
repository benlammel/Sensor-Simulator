package it.unicam.sensorsimulator.plugin.heedv2.agent;

import java.io.IOException;

import it.unicam.sensorsimulator.plugin.heedv2.agent.behaviour.Heedv2Behaviour;
import it.unicam.sensorsimulator.plugin.heedv2.messages.Heedv2Message;
import it.unicam.sensorsimulator.plugin.heedv2.messages.MessageTypes;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class MessagingBehaviour extends Behaviour {

	private Heedv2Agent agent;
	
	public MessagingBehaviour(Heedv2Agent agent) {
		this.agent = agent;
	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		if (msg != null) {
			agent.receiveMessageCounter(msg);
			switch (msg.getConversationId()){
			case MessageTypes.SIMULATION_CONTROLS_START_INIZIALIZATION:
				agent.setCoordinatorAgentAID(msg.getSender());
				agent.addBehaviour(new Heedv2Behaviour(agent, agent.getHeedRunPeriod()));
				break;
			case MessageTypes.SIMULATION_CONTROLS_TERMINATION_REQUEST:
				terminationHandler(true);
				break;
			case MessageTypes.HEED_COST_BROADCAST:
				checkCostBroadcast(msg);
				break;
			case MessageTypes.HEED_FINAL_CLUSTERHEAD:
				finalClusterHeadMsgHandling(msg);
				break;
			case MessageTypes.HEED_TENTATIVE_CLUSTERHEAD:
				tentativeClusterHeadMsgHandling(msg);
				break;
			case MessageTypes.HEED_JOIN_CLUSTER:
				addToMyCluster(agent.convertAIDToInteger(msg.getSender()));
				break;
			}
		}
	}

	private void checkCostBroadcast(ACLMessage msg) {
		if(agent.isClusterHead()){
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			message.setConversationId(MessageTypes.HEED_FINAL_CLUSTERHEAD);
			try {
				message.setContentObject(new Heedv2Message(agent.getCosts()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			message.addReceiver(msg.getSender());
			agent.sendMessage(message);
		}
	}

	private void addToMyCluster(int agentID) {
		agent.addToMyCluster(agentID);
	}

	private void tentativeClusterHeadMsgHandling(ACLMessage msg) {
		agent.addToTentativeCHList(agent.convertAIDToInteger(msg.getSender()), parseHeedMessageObject(msg));
	}

	private void finalClusterHeadMsgHandling(ACLMessage msg) {
		agent.addToFinalCHList(agent.convertAIDToInteger(msg.getSender()), parseHeedMessageObject(msg));
	}
	
	private Heedv2Message parseHeedMessageObject(ACLMessage msg) {
		Heedv2Message heedMessage = null;
		try {
			heedMessage = (Heedv2Message) msg.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		return heedMessage;
	}

	private void terminationHandler(boolean b) {
		agent.setTerminationRequest(b);
	}

	@Override
	public boolean done() {
		return false;
	}

}
