package it.unicam.sensorsimulator.plugin.heed.agents.behaviours;

import java.io.IOException;
import java.util.Map.Entry;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.plugin.heed.agents.GeneralAgent;
import it.unicam.sensorsimulator.plugin.heed.messages.HeedMessage;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heed.messages.HeedMessage.ClusterHeadType;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class HeedProtocolBehaviour extends TickerBehaviour {

	private GeneralAgent agent;

	private double chPrevious = 0;
	private double chProb = 0;
	private double cProb = 0;

	private boolean finalizedPartPerformed = false;
	private boolean tentativeMessageSent = false;

	public HeedProtocolBehaviour(GeneralAgent agent, long period) {
		super(agent, period);
		this.agent = agent;
		calculateCHProb();
	}

	@Override
	public void onTick() {
//		ACLMessage msg = agent.receive();
//		if (msg != null) {
//			switch (msg.getConversationId()) {
//			case MessageTypes.COST_BROADCAST:
//				agent.receiveMessageCounter(msg, MessageHandling.INCREASE);
////				handleCostBroadcastMsg(msg);
//				break;
//			case MessageTypes.CLUSTER_HEAD_MESSAGE:
//				agent.receiveMessageCounter(msg, MessageHandling.INCREASE);
//				handleClusterHeadMessage(msg);
//				break;
//			default:
//				agent.putBack(msg);
//				break;
//			}
//		}
		
			if (chPrevious < 1.0
					&& !agent.isConnectedToClusterHeadOrIsClusterHead()) {
				heedRepeatPart();
			}else if(chPrevious==1.0 && !agent.isConnectedToClusterHeadOrIsClusterHead() && agent.getQueueSize()==0){
				heedFinalizePart();
		}
			
			checkIfDone();
	}
	
//	private void handleClusterHeadMessage(ACLMessage msg) {
//		HeedMessage contentObject = parseMessageObject(msg);
//		agent.addToListOfClusterHeads(agent.convertAgentAIDToInteger(msg.getSender()), contentObject);
//	}

//	private HeedMessage parseMessageObject(ACLMessage msg) {
//		HeedMessage heedMessage = null;
//		try {
//			heedMessage = (HeedMessage) msg.getContentObject();
//		} catch (UnreadableException e) {
//			e.printStackTrace();
//		}
//		return heedMessage;
//	}

	private void heedFinalizePart() {
		track("heedFinalizePart ");
		if(!agent.isClusterHead()){
			int clusterHead = returnCheapestFinalClusterHeadID();
			if(clusterHead!=-1){
				sendMessage(clusterHead, MessageTypes.JOIN_CLUSTER);
				agent.setMyClusterHead(clusterHead);
			}else{
				sendHeedMessageBroadcast(ClusterHeadType.FINALCH, MessageTypes.CLUSTER_HEAD_MESSAGE);
				agent.setToClusterHead(true);
			}
		}else{
			sendHeedMessageBroadcast(ClusterHeadType.FINALCH, MessageTypes.CLUSTER_HEAD_MESSAGE);
			agent.setToClusterHead(true);
		}
		finalizedPartPerformed =true;
	}

	private void heedRepeatPart() {
		track("Beginn-heedRepeatPart");
		if (!agent.getListOfClusterHeads().isEmpty()) {
			int clusterHeadID = returnCheapestClusterHeadID();
			if (clusterHeadID == agent.getAgentConfiguration().getAgentID()) {
				if (chProb == 1.0) {
					sendHeedMessageBroadcast(ClusterHeadType.FINALCH, MessageTypes.CLUSTER_HEAD_MESSAGE);
					agent.setToClusterHead(true);
				}else{
					if(!tentativeMessageSent ){
						sendHeedMessageBroadcast(ClusterHeadType.TENTATIVECH, MessageTypes.CLUSTER_HEAD_MESSAGE);
						tentativeMessageSent= true;
					}
				}
			}
		}else if(chProb == 1.0){
			sendHeedMessageBroadcast(ClusterHeadType.FINALCH, MessageTypes.CLUSTER_HEAD_MESSAGE);
			agent.setToClusterHead(true);
		}else if(Math.random()<=chProb){
			track("random");
			if(!tentativeMessageSent ){
				sendHeedMessageBroadcast(ClusterHeadType.TENTATIVECH, MessageTypes.CLUSTER_HEAD_MESSAGE);
				tentativeMessageSent= true;
			}
		}
		
		chPrevious = chProb;
		chProb = Math.min(chProb*2, 1);
		track("End-heedRepeatPart");
	}

	private void track(String string) {
		StringBuilder builder = new StringBuilder();
		builder.append(string);
		builder.append(";chPrevious;");
		builder.append(chPrevious);
		builder.append(";chProb;");
		builder.append(chProb);
		builder.append(";cProb;");
		builder.append(cProb);
		agent.track(builder.toString());
	}

	private void sendHeedMessageBroadcast(ClusterHeadType clusterHeadType, String messageType) {
		for (Entry<Integer, GeneralAgentInterface> myNeighbor : agent.getNeighborList().entrySet()) {
			if (myNeighbor.getKey() != agent.getAgentConfiguration().getAgentID()) {
				sendMessage(myNeighbor.getKey(), messageType, new HeedMessage(agent.getCosts(myNeighbor.getKey()), clusterHeadType));
			}
		}
	}

	private void sendMessage(Integer receiver, String conversationID,
			HeedMessage heedMessage) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(conversationID);
		try {
			message.setContentObject(heedMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		message.addReceiver(agent.convertAgentIDToAID(receiver));
		track("sendMessage "+heedMessage.toString());
		agent.sendMessage(message);
	}
	
	private void sendMessage(int receiver, String conversationID) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(conversationID);
		message.addReceiver(agent.convertAgentIDToAID(receiver));
		agent.sendMessage(message);
	}
	
	private int returnCheapestFinalClusterHeadID() {
		int clusterHeadID = -1;
		double clusterHeadCosts = Double.POSITIVE_INFINITY;
		for (Entry<Integer, HeedMessage> potentialClusterHead : agent
				.getListOfClusterHeads().entrySet()) {
			if (potentialClusterHead.getValue().getCost() <= clusterHeadCosts && potentialClusterHead.getValue().getClusterHeadType().equals(ClusterHeadType.FINALCH)) {
				clusterHeadID = potentialClusterHead.getKey();
				clusterHeadCosts = potentialClusterHead.getValue().getCost();
			}
		}
		return clusterHeadID;
	}

	private int returnCheapestClusterHeadID() {
		int clusterHeadID = -1;
		double clusterHeadCosts = Double.POSITIVE_INFINITY;
		for (Entry<Integer, HeedMessage> potentialClusterHead : agent
				.getListOfClusterHeads().entrySet()) {
			if (potentialClusterHead.getValue().getCost() <= clusterHeadCosts) {
				clusterHeadID = potentialClusterHead.getKey();
				clusterHeadCosts = potentialClusterHead.getValue().getCost();
			}
		}
		return clusterHeadID;
	}
	
	private void calculateCHProb() {
		cProb = 5.0 / 100.0;
		chProb = Math.max(cProb * agent.geteResidual() / agent.geteMax(), agent.getpMin());
	}

	public void checkIfDone() {
		if(agent.isConnectedToClusterHeadOrIsClusterHead() && finalizedPartPerformed){
			track("End behaviour, agent is connected");
			agent.removeBehaviour(this);
		}
	}
}
