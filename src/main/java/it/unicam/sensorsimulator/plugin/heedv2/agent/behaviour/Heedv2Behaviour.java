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
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class Heedv2Behaviour extends Behaviour {
	
	private Heedv2Agent agent;
	private HashMap<Integer, Heedv2Message> tentativeClusterHeadList;
	private HashMap<Integer, Heedv2Message> finalClusterHeadList;
	
	private boolean IS_FINAL_CH = false;
	private boolean IS_TENTATIVE_CH = false;
	private static final float C_PROB = 0.10f;
	private static final float P_MIN = 1e-4f;
	private static final int E_MAX = 100;
	private float CH_PROB;
	private float CH_PREVIOUS;
	private boolean heedDone = false;

	public Heedv2Behaviour(Heedv2Agent agent) {
		this.agent = agent;
		System.out.println("Agent started");
		tentativeClusterHeadList = new HashMap<Integer, Heedv2Message>();
		finalClusterHeadList = new HashMap<Integer, Heedv2Message>();
		initialize();
	}

	private void initialize() {
		CH_PROB = Math.max((C_PROB * E_MAX) / E_MAX, P_MIN);
		CH_PREVIOUS = 0.0f;
		IS_FINAL_CH = false;
		track("initialize");
	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		
		if (msg != null) {
			switch (msg.getConversationId()) {
			case MessageTypes.HEED_FINAL_CLUSTERHEAD:
				agent.receiveMessageCounter(msg);
				finalClusterHeadMsgHandling(msg);
				break;
			case MessageTypes.HEED_TENTATIVE_CLUSTERHEAD:
				agent.receiveMessageCounter(msg);
				tentativeClusterHeadMsgHandling(msg);
				break;
			default:
				agent.putBack(msg);
				break;
			}
		}
		
		if(CH_PREVIOUS!=1){
			heedRepeatPart();
		}
		
		if(CH_PREVIOUS==1){
			heedFinalize();
		}
		
	}

	private void tentativeClusterHeadMsgHandling(ACLMessage msg) {
		Heedv2Message contentObject = parseMessageObject(msg);
		tentativeClusterHeadList.put(agent.convertAIDToInteger(msg.getSender()), contentObject);
	}

	private void finalClusterHeadMsgHandling(ACLMessage msg) {
		Heedv2Message contentObject = parseMessageObject(msg);
		tentativeClusterHeadList.put(agent.convertAIDToInteger(msg.getSender()), contentObject);
		finalClusterHeadList.put(agent.convertAIDToInteger(msg.getSender()), contentObject);
	}

	private void heedFinalize() {
		track("heedFinalize");
		if(!IS_FINAL_CH){
			if(!finalClusterHeadList.isEmpty()){
				int myClusterHead = getCheapestFinalClusterHead();
				sendJoinMessage(myClusterHead);
			}else{
				if(!IS_FINAL_CH){
					broadcastClusterHeadMsg(MessageTypes.HEED_FINAL_CLUSTERHEAD);
					setFinalCH(true);
				}
			}
		}else{
			if(!IS_FINAL_CH){
				broadcastClusterHeadMsg(MessageTypes.HEED_FINAL_CLUSTERHEAD);
				setFinalCH(true);
			}
		}
		heedDone = true;
	}

	private void heedRepeatPart() {
		track("heedRepeatPart");
		Random randomNumber = new Random();
		
		if(!tentativeClusterHeadList.isEmpty()){
			int myClusterHead = getCheapestClusterHead();
			if(myClusterHead==agent.getAgentConfiguration().getAgentID()){
				if(CH_PROB==1){
					if(!IS_FINAL_CH){
						broadcastClusterHeadMsg(MessageTypes.HEED_FINAL_CLUSTERHEAD);
						setFinalCH(true);
					}
				}else{
					if(!IS_TENTATIVE_CH){
						broadcastClusterHeadMsg(MessageTypes.HEED_TENTATIVE_CLUSTERHEAD);
						IS_TENTATIVE_CH  = true;
					}
				}
			}
		}else if(CH_PROB==1){
			if(!IS_FINAL_CH){
				broadcastClusterHeadMsg(MessageTypes.HEED_FINAL_CLUSTERHEAD);
				setFinalCH(true);
			}
			
		}else if (randomNumber.nextFloat() <= CH_PROB) {
			track("random");
			if(!IS_TENTATIVE_CH){
				broadcastClusterHeadMsg(MessageTypes.HEED_TENTATIVE_CLUSTERHEAD);
				IS_TENTATIVE_CH  = true;
			}
		}
		
		CH_PREVIOUS = CH_PROB;
		CH_PROB = Math.min(CH_PROB*2, 1);
		track("heedRepeatPart");
	}
	
	private void setFinalCH(boolean b) {
		IS_FINAL_CH = b;
		if(b){
			agent.setClusterHead(b);
			agent.addBehaviour(new HeedClusterHeadBehaviour(agent));
			agent.notifyCoordinator(MessageTypes.SIMULATION_CONTROLS_BECAME_CLUSTER_HEAD);
		}
	}

	private int getCheapestFinalClusterHead() {
		int tempCHID= -1;
		double tempCHCost = Double.POSITIVE_INFINITY;
		for(Entry<Integer, Heedv2Message> clusterHead : finalClusterHeadList.entrySet()){
			if(clusterHead.getValue().getCosts()<tempCHCost){
				tempCHID = clusterHead.getKey();
				tempCHCost = clusterHead.getValue().getCosts();
			}
		}
		return tempCHID;
	}

	private int getCheapestClusterHead() {
		int tempCHID= -1;
		double tempCHCost = Double.POSITIVE_INFINITY;
		for(Entry<Integer, Heedv2Message> clusterHead : tentativeClusterHeadList.entrySet()){
			if(clusterHead.getValue().getCosts()<tempCHCost){
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
		agent.setMyClusterHead(myClusterHead);
		agent.notifyCoordinator(MessageTypes.SIMULATION_CONTROLS_JOINED_CLUSTER);
	}

	private void broadcastClusterHeadMsg(String msgType) {
		for(Entry<Integer, HeedAgentConfiguration> myNeighbor : agent.getNeighborList().entrySet()){
			if (myNeighbor.getKey() != agent.getAgentConfiguration().getAgentID()) {
				sendHeedMessage(agent.convertIntegerToAID(myNeighbor.getKey()), msgType, new Heedv2Message(agent.getCosts(myNeighbor.getKey())));
			}
		}
	}

	private void sendHeedMessage(AID receiver, String conversationID, Heedv2Message heedv2Message) {
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
	
	private Heedv2Message parseMessageObject(ACLMessage msg) {
		Heedv2Message heedMessage = null;
		try {
			heedMessage = (Heedv2Message) msg.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		return heedMessage;
	}
	
	private void track(String string) {
		StringBuilder builder = new StringBuilder();
		builder.append(string);
		builder.append(";IS_FINAL_CH;");
		builder.append(IS_FINAL_CH);
		builder.append(";IS_TENTATIVE_CH;");
		builder.append(IS_TENTATIVE_CH);
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
		builder.append(";heedDone;");
		builder.append(heedDone);
		builder.append(";tentativeClusterHeadList;");
		builder.append(tentativeClusterHeadList.keySet());
		builder.append(";finalClusterHeadList;");
		builder.append(finalClusterHeadList.keySet());
		agent.track(builder.toString());
	}

	@Override
	public boolean done() {
		if(heedDone){
			track("FINISH HEED BEHAVIOUR");
			return true;
		}else{
			return false;
		}
	}
}
