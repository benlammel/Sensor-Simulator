package it.unicam.sensorsimulator.plugin.heedv2.agent.behaviour;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;
import it.unicam.sensorsimulator.plugin.heedv2.agent.Heedv2Agent;
import it.unicam.sensorsimulator.plugin.heedv2.agent.config.HeedAgentConfiguration;
import it.unicam.sensorsimulator.plugin.heedv2.messages.Heedv2Message;
import it.unicam.sensorsimulator.plugin.heedv2.messages.MessageTypes;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class Heedv2Behaviour extends TickerBehaviour {

	private Heedv2Agent agent;
	private static final float C_PROB = 0.10f;
	private static final float P_MIN = 1e-4f;
	private static final int E_MAX = 100;
	private float CH_PROB;
	private float CH_PREVIOUS;
	private boolean tentative = false;

	public Heedv2Behaviour(Heedv2Agent agent, int period) {
		super(agent, period);
		this.agent = agent;
		System.out.println("Agent started");
		initialize();
	}

	private void initialize() {
		broadcastHeedMsg(MessageTypes.HEED_COST_BROADCAST);
		CH_PROB = Math.max((C_PROB * E_MAX) / E_MAX, P_MIN);
		CH_PREVIOUS = 0.0f;
//		track("initialize");
	}

	@Override
	public void onTick() {
		if (CH_PREVIOUS <= 1) {
			heedRepeatPart();
		}

		if (CH_PREVIOUS == 1 && agent.getCurQueueSize()==0) {
			heedFinalize();
		}
	}

	private void heedFinalize() {
		track("heedFinalize");

		if (!agent.isClusterHead()) {
			if (!agent.getFinalClusterHeadList().isEmpty()) {
				int myClusterHead = getCheapestClusterHead(agent
						.getFinalClusterHeadList());
				agent.setMyClusterHead(myClusterHead);
				sendJoinMessage(myClusterHead);
			} else {
				broadcastHeedMsg(MessageTypes.HEED_FINAL_CLUSTERHEAD);
				agent.isClusterHead(true);
			}
		} else {
			broadcastHeedMsg(MessageTypes.HEED_FINAL_CLUSTERHEAD);
			agent.isClusterHead(true);
		}
		agent.removeBehaviour(this);
	}

	private void heedRepeatPart() {
//		track("heedRepeatPart");
		Random randomNumber = new Random();

		
		if (!agent.getTentativeClusterHeadList().isEmpty()) {
			int myClusterHead = getCheapestClusterHead(agent
					.getTentativeClusterHeadList());
			if (myClusterHead == agent.getAgentConfiguration().getAgentID()) {
				if (CH_PROB == 1) {
					broadcastHeedMsg(MessageTypes.HEED_FINAL_CLUSTERHEAD);
					agent.isClusterHead(true);
				} else {
					broadcastHeedMsg(MessageTypes.HEED_TENTATIVE_CLUSTERHEAD);
					agent.addToTentativeCHList(agent.getAgentConfiguration()
							.getAgentID(), new Heedv2Message(0));
				}
			}
		} else if (CH_PROB == 1) {
			broadcastHeedMsg(MessageTypes.HEED_FINAL_CLUSTERHEAD);
			agent.isClusterHead(true);

		} else if (randomNumber.nextFloat() <= CH_PROB) {
//			track("random");
			broadcastHeedMsg(MessageTypes.HEED_TENTATIVE_CLUSTERHEAD);
			agent.addToTentativeCHList(agent.getAgentConfiguration()
					.getAgentID(), new Heedv2Message(0));
		}

		CH_PREVIOUS = CH_PROB;
		CH_PROB = Math.min(CH_PROB * 2, 1);
//		track("heedRepeatPart-End");
	}

	private int getCheapestClusterHead(HashMap<Integer, Heedv2Message> hashMap) {
		int tempCHID = -1;
		double tempCHCost = Double.POSITIVE_INFINITY;
		for (Entry<Integer, Heedv2Message> clusterHead : hashMap.entrySet()) {
			if (clusterHead.getValue().getCosts() < tempCHCost) {
				tempCHID = clusterHead.getKey();
				tempCHCost = clusterHead.getValue().getCosts();
			}
		}
		return tempCHID;
	}

	private void sendJoinMessage(int myClusterHead) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(MessageTypes.HEED_JOIN_CLUSTER);
		message.addReceiver(agent.convertIntegerToAID(myClusterHead));
		agent.sendMessage(message);
		agent.notifyCoordinator(MessageTypes.SIMULATION_CONTROLS_JOINED_CLUSTER);
	}

	private void broadcastHeedMsg(String msgType) {
		boolean var = false;

		if (msgType.equals(MessageTypes.HEED_FINAL_CLUSTERHEAD)) {
			if (!agent.isClusterHead()) {
				var = true;
			}
		} else if (msgType.equals(MessageTypes.HEED_TENTATIVE_CLUSTERHEAD)) {
			if (!tentative) {
				var = true;
			}
		} else if(msgType.equals(MessageTypes.HEED_COST_BROADCAST)){
			var = true;
		}

		if (var) {
			for (Entry<Integer, HeedAgentConfiguration> myNeighbor : agent
					.getNeighborList().entrySet()) {
				if (myNeighbor.getKey() != agent.getAgentConfiguration()
						.getAgentID()) {
					sendHeedMessage(
							agent.convertIntegerToAID(myNeighbor.getKey()),
							msgType,
							new Heedv2Message(agent.getCosts()));
				}
			}
		}
	}
	
	private void sendHeedMessage(AID receiver, String conversationID,
			Heedv2Message heedv2Message) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(conversationID);
		try {
			message.setContentObject(heedv2Message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		message.addReceiver(receiver);
		agent.sendMessage(message);
	}

	private void track(String string) {
		StringBuilder builder = new StringBuilder();
		builder.append(string);
		builder.append(";C_PROB;");
		builder.append(C_PROB);
		builder.append(";CH_PROB;");
		builder.append(CH_PROB);
		builder.append(";CH_PREVIOUS;");
		builder.append(CH_PREVIOUS);
		builder.append(";P_MIN;");
		builder.append(P_MIN);
		builder.append(";E_MAX;");
		builder.append(E_MAX);

		agent.track(builder.toString());
	}

//	@Override
//	public boolean done() {
//		return false;
//	}
}
