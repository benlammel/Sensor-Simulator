package it.unicam.sensorsimulator.plugin.heed.agents.behaviours;
/*
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface.LogLevels;
import it.unicam.sensorsimulator.plugin.heed.agents.GeneralAgent;
import it.unicam.sensorsimulator.plugin.heed.messages.HeedMessage;
import it.unicam.sensorsimulator.plugin.heed.messages.HeedMessage.ClusterHeadType;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes.MessageHandling;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class HeedClusteringBehaviour extends TickerBehaviour {

	private GeneralAgent agent;
	private double cProb = 0;
	private double chProb = 0;
	private double pMin = 0;
	private boolean isFinalCH;
	private HashMap<Integer, HeedMessage> listOfClusterHeads;
	private double chPrevious;
	private boolean initializationTriggerReceived;

	public HeedClusteringBehaviour(GeneralAgent agent) {
		this.agent = agent;
		listOfClusterHeads = new HashMap<Integer, HeedMessage>();
		calculateCHProb();
		isFinalCH = false;
	}

	private void calculateCHProb() {
		cProb = 5.0 / 100.0;
		pMin = 1.0 / Math.pow(10.0, 4.0);
		chProb = Math.max(cProb * agent.getEResidual() / agent.getEMax(), pMin);
		track("calculateCHProb");
		
	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		track("action");
		if (msg != null) {
			agent.receiveMessageCounter(msg, MessageHandling.INCREASE);
			switch (msg.getConversationId()) {
			case MessageTypes.START_INIZIALIZATION:
				computeAndBroadcastCostToNeighborhood();
				break;
			case MessageTypes.COST_BROADCAST:
				progressCostsMessage(msg);
				break;
			case MessageTypes.CLUSTER_HEAD_MESSAGE:
				progressCHMessage(msg);
				break;
			case MessageTypes.JOIN_CLUSTER:
				processJoinMessage(msg);
			default:
				agent.putBack(msg);
				agent.receiveMessageCounter(msg, MessageHandling.DECREASE);
				break;
			}
		}
		
		if(initializationTriggerReceived){
			runRepeatHeedPart();
		}
		
//		if (!isFinalCH) {
//			checkForFinalClusterHeadsAndJoin();
//		}else{
//			calculateIndividualCostsAndBroadcast(
//					MessageTypes.CLUSTER_HEAD_MESSAGE,
//					ClusterHeadType.FINALCH);
//		}
	}

	private void track(String where) {
		StringBuilder builder = new StringBuilder();
//		builder.append(this.getClass().getName());
		builder.append(where +" ;ID;");
		builder.append(agent.getAgentConfiguration().getAgentID());
		builder.append(";isFinal;");
		builder.append(isFinalCH);
		builder.append(";cprob;");
		builder.append(cProb);
		builder.append(";chprob;");
		builder.append(chProb);
		builder.append(";pmin;");
		builder.append(pMin);
		builder.append(";listOfClusterHeads;");
		builder.append(listOfClusterHeads.keySet().toString());
//		builder.append(";clusterHeadID;");
//		builder.append(clusterHeadID);
//		builder.append(";LAYOUTY;");
//		builder.append(getLayoutY());
		agent.getLog().logAgentAction(LogLevels.INFO, builder.toString());
		
	}

	private void processJoinMessage(ACLMessage msg) {
		agent.addToMyCluster(agent.convertAgentAIDToInteger(msg.getSender()));
	}

	private void checkForFinalClusterHeadsAndJoin() {
		int clusterHeadID = 0;
		double clusterHeadCosts = Double.POSITIVE_INFINITY;
		
		
		track("checkForFinalClusterHeadsAndJoin");
		
		if(listOfClusterHeads.size()>0){
			for(Entry<Integer, HeedMessage> potentialClusterHead : listOfClusterHeads.entrySet()){
				if(potentialClusterHead.getValue().getClusterHeadType().equals(ClusterHeadType.FINALCH)){
					if (potentialClusterHead.getValue().getCost() <= clusterHeadCosts) {
						clusterHeadID = potentialClusterHead.getKey();
						clusterHeadCosts = potentialClusterHead.getValue()
								.getCost();
					}
				}
				
			}
		}
		
		if(clusterHeadID!=0){
			setFinalClusterHead(clusterHeadID);
			sendMessage(clusterHeadID, MessageTypes.JOIN_CLUSTER);
		}else{
			track("checkForFinalClusterHeadsAndJoin: clusterhead!0");
			calculateIndividualCostsAndBroadcast(
					MessageTypes.CLUSTER_HEAD_MESSAGE,
					ClusterHeadType.FINALCH);
		}
	}

	private void runRepeatHeedPart() {
		if (!listOfClusterHeads.isEmpty()) {
			int clusterHeadID = 0;
			double clusterHeadCosts = Double.POSITIVE_INFINITY;
			for (Entry<Integer, HeedMessage> potentialClusterHead : listOfClusterHeads
					.entrySet()) {
				if (potentialClusterHead.getValue().getCost() <= clusterHeadCosts) {
					clusterHeadID = potentialClusterHead.getKey();
					clusterHeadCosts = potentialClusterHead.getValue()
							.getCost();
				}

				if (clusterHeadID == agent.getAgentConfiguration().getAgentID()) {
					if (chProb == 1.0) {
						track("runRepeatHeedPart: chProb == 1.0");
						calculateIndividualCostsAndBroadcast(
								MessageTypes.CLUSTER_HEAD_MESSAGE,
								ClusterHeadType.FINALCH);
						setFinalClusterHead(agent.getAgentConfiguration().getAgentID());
					} else {
						track("runRepeatHeedPart: else");
						calculateIndividualCostsAndBroadcast(
								MessageTypes.CLUSTER_HEAD_MESSAGE,
								ClusterHeadType.TENTATIVECH);
					}
				}
			}

		}else if(chProb == 1.0){
			track("runRepeatHeedPart: else if chProb == 1.0");
			calculateIndividualCostsAndBroadcast(
					MessageTypes.CLUSTER_HEAD_MESSAGE,
					ClusterHeadType.FINALCH);
			setFinalClusterHead(agent.getAgentConfiguration().getAgentID());
		}else if(Math.random()<=chProb){
			track("runRepeatHeedPart: random");
			calculateIndividualCostsAndBroadcast(
					MessageTypes.CLUSTER_HEAD_MESSAGE,
					ClusterHeadType.TENTATIVECH);
		}
		chPrevious = chProb;
		chProb = Math.min(chProb*2, 1);
	}

	private void setFinalClusterHead(int agentID) {
		isFinalCH = true;
		agent.setClusterHead(agentID);
	}

	private void progressCHMessage(ACLMessage msg) {
		HeedMessage heedMessage = unmarshalHeedMessage(msg);
		listOfClusterHeads.put(agent.convertAgentAIDToInteger(msg.getSender()),
				heedMessage);
	}

	private void progressCostsMessage(ACLMessage msg) {
		HeedMessage heedMessage = unmarshalHeedMessage(msg);
		System.out.println(agent.getAgentConfiguration().getAgentID()
				+ " :: received: " + msg.getSender().getLocalName()
				+ " :: costs: " + heedMessage.getCost());
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

	private void calculateIndividualCostsAndBroadcast(String messageType,
			ClusterHeadType finalch) {
		for (Entry<Integer, GeneralAgentInterface> myNeighbor : agent
				.getNeighborList().entrySet()) {
			if (myNeighbor.getKey() != agent.getAgentConfiguration()
					.getAgentID()) {
				double cost = Math.sqrt(Math.pow(agent.getAgentConfiguration()
						.getLocationX() - myNeighbor.getValue().getLocationX(),
						2)
						+ Math.pow(agent.getAgentConfiguration().getLocationY()
								- myNeighbor.getValue().getLocationY(), 2));
				sendMessage(myNeighbor.getKey(), messageType, new HeedMessage(
						cost));
			}
		}
	}

	private void sendMessage(Integer receiver, String messageType,
			HeedMessage heedMessage) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(messageType);
		try {
			message.setContentObject(heedMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		message.addReceiver(agent.convertAgentIDToAID(receiver));
		agent.sendMessage(message);
	}
	
	private void sendMessage(int receiver, String messageType) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(messageType);
		message.addReceiver(agent.convertAgentIDToAID(receiver));
		agent.sendMessage(message);
	}

	private void computeAndBroadcastCostToNeighborhood() {
		for (Entry<Integer, GeneralAgentInterface> myNeighbor : agent
				.getNeighborList().entrySet()) {
			if (myNeighbor.getKey() != agent.getAgentConfiguration()
					.getAgentID()) {
				double cost = Math.sqrt(Math.pow(agent.getAgentConfiguration()
						.getLocationX() - myNeighbor.getValue().getLocationX(),
						2)
						+ Math.pow(agent.getAgentConfiguration().getLocationY()
								- myNeighbor.getValue().getLocationY(), 2));
				// sendMessage(myNeighbor.getKey(), MessageTypes.COST_BROADCAST,
				// cost);
				sendMessage(myNeighbor.getKey(), MessageTypes.COST_BROADCAST,
						new HeedMessage(cost));
			}
		}
		initializationTriggerReceived = true;
	}

	@Override
	protected void onTick() {
		// TODO Auto-generated method stub
		
	}

	// private void sendMessage(int agentID, String messageType, double value) {
	// ACLMessage message = new ACLMessage(ACLMessage.INFORM);
	// message.setConversationId(messageType);
	// try {
	// message.setContentObject(new HeedMessage(messageType, value));
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// message.addReceiver(agent.convertAgentIDToAID(agentID));
	// agent.sendMessage(message);
	// }
}
*/