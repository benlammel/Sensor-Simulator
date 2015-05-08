package it.unicam.sensorsimulator.plugin.heed.agents.behaviours;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface.LogLevels;
import it.unicam.sensorsimulator.plugin.heed.agents.GeneralAgent;
import it.unicam.sensorsimulator.plugin.heed.messages.HeedMessage;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heed.messages.HeedMessage.ClusterHeadType;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes.MessageHandling;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class HeedBehaviour extends TickerBehaviour {

	private GeneralAgent agent;
	
	private HashMap<Integer, HeedMessage> listOfClusterHeads;
	
	private double cProb = 0;
	private double chProb = 0;
	private double pMin = 0;
	private boolean isFinalCH = false;
	
	private boolean initializationTriggerReceived = false;

	private double chPrevious;

	
	public HeedBehaviour(GeneralAgent agent, long period) {
		super(agent, period);
		this.agent = agent;
		listOfClusterHeads = new HashMap<Integer, HeedMessage>();
		calculateCHProb();
	}
	
	@Override
	protected void onTick() {
		ACLMessage msg = agent.receive();
		track("action");
		if (msg != null) {
			agent.receiveMessageCounter(msg, MessageHandling.INCREASE);
			switch (msg.getConversationId()) {
			case MessageTypes.START_INIZIALIZATION:
				agent.setSimulationCoordinatorAID(msg.getSender());
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
				break;
			default:
				agent.putBack(msg);
				agent.receiveMessageCounter(msg, MessageHandling.DECREASE);
				break;
			}
		}
		
		if(initializationTriggerReceived && !agent.isConnected()){
			runRepeatHeedPart();
		}
	}
	
	private void processJoinMessage(ACLMessage msg) {
		System.out.println(agent.getAgentConfiguration().getAgentID() +" : process join message from :" +msg.getSender());
		agent.addToMyCluster(agent.convertAgentAIDToInteger(msg.getSender()));
		measurementCluster();
	}
	
	private void progressCHMessage(ACLMessage msg) {
		HeedMessage heedMessage = unmarshalHeedMessage(msg);
		listOfClusterHeads.put(agent.convertAgentAIDToInteger(msg.getSender()),
				heedMessage);
		if (!isFinalCH) {
		checkForFinalClusterHeadsAndJoin();
	}else{
		calculateIndividualCostsAndBroadcast(
				MessageTypes.CLUSTER_HEAD_MESSAGE,
				ClusterHeadType.FINALCH);
		sendToCoordinator(MessageTypes.CLUSTER_HEAD_MESSAGE);
	}
	}
	
	private void sendToCoordinator(String messageType) {
		switch(messageType){
		case MessageTypes.CLUSTER_HEAD_MESSAGE:
			measurementCluster();
			break;
		}
	}

	private void measurementCluster() {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(MessageTypes.MEASUREMENT_PROTOCOL);
		try {
			message.setContentObject(agent.getMyClusterView());
		} catch (IOException e) {
			e.printStackTrace();
		}
		message.addReceiver(agent.getSimulationCoordinatorAID());
		agent.sendMessage(message);
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
			measurementCluster();
		}
	}
	
	private void sendMessage(int receiver, String messageType) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(messageType);
		message.addReceiver(agent.convertAgentIDToAID(receiver));
		agent.sendMessage(message);
	}

	private void runRepeatHeedPart() {
//		track("**********runRepeatHeedPart");
		if (!listOfClusterHeads.isEmpty()) {
			int clusterHeadID = -1;
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
						measurementCluster();
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
		}
		
		chPrevious = chProb;
		chProb = Math.min(chProb*2, 1);
		
	}
	
	private void setFinalClusterHead(int agentID) {
		track("setFinalClusterHead " +agentID);
		isFinalCH = true;
		agent.setClusterHead(agentID);
	}

	private void calculateCHProb() {
		cProb = 5.0 / 100.0;
		pMin = 1.0 / Math.pow(10.0, 4.0);
		chProb = Math.max(cProb * agent.getEResidual() / agent.getEMax(), pMin);
		track("calculateCHProb");
	}
	
	private void calculateIndividualCostsAndBroadcast(String messageType, ClusterHeadType clusterHeadType) {
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
						cost, clusterHeadType));
			}
		}
	}
	
	private void progressCostsMessage(ACLMessage msg) {
		HeedMessage heedMessage = unmarshalHeedMessage(msg);
		//TODO
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
	
	
	private void track(String where) {
		StringBuilder builder = new StringBuilder();
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
		builder.append(";chPrevious;");
		builder.append(chPrevious);

		agent.getLog().logAgentAction(LogLevels.INFO, builder.toString());
	}
}
