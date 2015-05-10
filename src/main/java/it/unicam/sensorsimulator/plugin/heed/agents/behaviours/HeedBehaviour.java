package it.unicam.sensorsimulator.plugin.heed.agents.behaviours;
/*
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
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class HeedBehaviour extends TickerBehaviour {

	private GeneralAgent agent;
	
	private HashMap<Integer, HeedMessage> listOfClusterHeads;
	
	private double cProb = 0;
	private double chProb = 0;
	private boolean isFinalCH = false;
	
	private boolean initializationTriggerReceived = false;
	private double chPrevious;

	private boolean tentativeCLHEADMessageSent = false;
	
	public HeedBehaviour(GeneralAgent agent, long period) {
		super(agent, period);
		this.agent = agent;
		listOfClusterHeads = new HashMap<Integer, HeedMessage>();
		calculateCHProb();
	}
	
	@Override
	protected void onTick() {
		ACLMessage msg = agent.receive();
		if(!agent.isConnectedToClusterHeadOrIsClusterHead()){
			track("action");
		}
		
		if (msg != null) {
			switch (msg.getConversationId()) {
			case MessageTypes.CLUSTER_HEAD_MESSAGE:
				if(!agent.isConnectedToClusterHeadOrIsClusterHead()){
					agent.receiveMessageCounter(msg, MessageHandling.INCREASE);
					progressCHMessage(msg);
				}
				break;
			case MessageTypes.SIMULATION_CONTROLS_START_INIZIALIZATION:
				agent.receiveMessageCounter(msg, MessageHandling.INCREASE);
				agent.setSimulationCoordinatorAID(msg.getSender());
				initializationTriggerReceived = true;
//				computeAndBroadcastCostToNeighborhood();
				break;
			case MessageTypes.COST_BROADCAST:
				agent.receiveMessageCounter(msg, MessageHandling.INCREASE);
				progressCostsMessage(msg);
				break;
			default:
				agent.putBack(msg);
				break;
			}
		}
		
		if(initializationTriggerReceived && chPrevious<1.0 && !agent.isConnectedToClusterHeadOrIsClusterHead()){
			runRepeatHeedPart();
		}else if(initializationTriggerReceived && chPrevious==1.0 && !agent.isConnectedToClusterHeadOrIsClusterHead()){
			if (!isThisAgentFinalCH()) {
				checkForFinalClusterHeadsAndJoin();
			}else{
				calculateIndividualCostsAndBroadcast(
						MessageTypes.CLUSTER_HEAD_MESSAGE,
						ClusterHeadType.FINALCH);
				setThisAgentToFinalCH(true);
			}
		}
	}
	
	private boolean isThisAgentFinalCH() {
		return isFinalCH;
	}

	private void setThisAgentToFinalCH(boolean b) {
		isFinalCH = b;
		track("setThisAgentToFinalCH Remove behaviour:");
		if(isThisAgentFinalCH()){
			agent.addBehaviour(new ClusterHeadBehaviour(agent));
		}
		agent.setToClusterHead(true);
	}

	private void progressCHMessage(ACLMessage msg) {
		HeedMessage heedMessage = unmarshalHeedMessage(msg);
		listOfClusterHeads.put(agent.convertAgentAIDToInteger(msg.getSender()),
				heedMessage);
	}

	private void checkForFinalClusterHeadsAndJoin() {
		int clusterHeadID = 0;
		double clusterHeadCosts = Double.POSITIVE_INFINITY;
		
		track("checkForFinalClusterHeadsAndJoin");
		
		if(!listOfClusterHeads.isEmpty()){
			for(Entry<Integer, HeedMessage> potentialClusterHead : listOfClusterHeads.entrySet()){
				if(potentialClusterHead.getValue().getClusterHeadType().equals(ClusterHeadType.FINALCH)){
					if (potentialClusterHead.getValue().getCost() < clusterHeadCosts) {
						clusterHeadID = potentialClusterHead.getKey();
						clusterHeadCosts = potentialClusterHead.getValue()
								.getCost();
					}
				}
			}
		}
		
		if(clusterHeadID!=0){
			agent.setMyClusterHead(clusterHeadID);
			sendMessage(clusterHeadID, MessageTypes.JOIN_CLUSTER);
		}else{
			track("checkForFinalClusterHeadsAndJoin: clusterhead!0");
			calculateIndividualCostsAndBroadcast(
					MessageTypes.CLUSTER_HEAD_MESSAGE,
					ClusterHeadType.FINALCH);
			setThisAgentToFinalCH(true);
		}
	}
	
	private void sendMessage(int receiver, String messageType) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(messageType);
		message.addReceiver(agent.convertAgentIDToAID(receiver));
		agent.sendMessage(message);
	}

	private void runRepeatHeedPart() {
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
						calculateIndividualCostsAndBroadcast(
								MessageTypes.CLUSTER_HEAD_MESSAGE,
								ClusterHeadType.FINALCH);
						setThisAgentToFinalCH(true);
						track("runRepeatHeedPart: chProb == 1.0");
					} else {
						sendTentativeMessage();
						track("runRepeatHeedPart: chProb == 1.0 :: else");
					}
				}
			}
		}else if(chProb == 1.0){
			calculateIndividualCostsAndBroadcast(
					MessageTypes.CLUSTER_HEAD_MESSAGE,
					ClusterHeadType.FINALCH);
			setThisAgentToFinalCH(true);
			track("runRepeatHeedPart: else if chProb == 1.0");
		}else if(Math.random()<=chProb){
			track("runRepeatHeedPart: random");
			sendTentativeMessage();
		}
		
		chPrevious = chProb;
		chProb = Math.min(chProb*2, 1);
	}

	private void sendTentativeMessage() {
		if(!tentativeCLHEADMessageSent){
			track("sendTentativeMessage");
			calculateIndividualCostsAndBroadcast(
				MessageTypes.CLUSTER_HEAD_MESSAGE,
				ClusterHeadType.TENTATIVECH);
		listOfClusterHeads.put(agent.getAgentConfiguration().getAgentID(),
				new HeedMessage(0, ClusterHeadType.TENTATIVECH));
		tentativeCLHEADMessageSent = true;
		}
	}

	private void calculateCHProb() {
		cProb = 5.0 / 100.0;
		chProb = Math.max(cProb * agent.geteResidual() / agent.geteMax(), agent.getpMin());

		track("calculateCHProb");
	}
	
	private void calculateIndividualCostsAndBroadcast(String messageType, ClusterHeadType clusterHeadType) {
//		for (Entry<Integer, GeneralAgentInterface> myNeighbor : agent
//				.getNeighborList().entrySet()) {
//			if (myNeighbor.getKey() != agent.getAgentConfiguration()
//					.getAgentID()) {
//				double cost = Math.sqrt(Math.pow(agent.getAgentConfiguration()
//						.getLocationX() - myNeighbor.getValue().getLocationX(),
//						2)
//						+ Math.pow(agent.getAgentConfiguration().getLocationY()
//								- myNeighbor.getValue().getLocationY(), 2));
//				
//			}
//		}
		for (Entry<Integer, GeneralAgentInterface> myNeighbor : agent.getNeighborList().entrySet()) {
			if (myNeighbor.getKey() != agent.getAgentConfiguration().getAgentID()) {
			sendMessage(myNeighbor.getKey(), messageType, new HeedMessage(agent.getCosts(), clusterHeadType));
			}
		}
	}
	
	private void progressCostsMessage(ACLMessage msg) {
		HeedMessage heedMessage = unmarshalHeedMessage(msg);
		//not necessary in this simulation, because each agent knows the costs (distance) between them
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
				sendMessage(myNeighbor.getKey(), MessageTypes.COST_BROADCAST, new HeedMessage(agent.getCosts()));
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
		agent.getLog().logAgentAction(LogLevels.INFO, "agentid;" +agent.getAgentConfiguration().getAgentID() +";" +heedMessage.toString());
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
		builder.append(agent.getpMin());
		builder.append(";listOfClusterHeads;");
		builder.append(listOfClusterHeads.keySet().toString());
		builder.append(";chPrevious;");
		builder.append(chPrevious);

		agent.getLog().logAgentAction(LogLevels.INFO, builder.toString());
	}
}
*/