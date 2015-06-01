package it.unicam.sensorsimulator.plugin.heedv2.coordinator.behaviour;

import java.io.Serializable;
import java.util.ArrayList;
import it.unicam.sensorsimulator.plugin.heedv2.coordinator.Heedv2SimulationCoordinatorAgent;
import it.unicam.sensorsimulator.plugin.heedv2.messages.HeedMeasureMessage;
import it.unicam.sensorsimulator.plugin.heedv2.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.report.HeedAgentStatistic;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class SimulationContolBehaviour extends Behaviour {
	
	private Heedv2SimulationCoordinatorAgent coordinator;
	private ArrayList<Integer> clusterHeadList;
	private int receivedStatisticCounter = 0;
	private ArrayList<Integer> successorList;

	public SimulationContolBehaviour(
			Heedv2SimulationCoordinatorAgent heedv2SimulationCoordinatorAgent) {
		this.coordinator = heedv2SimulationCoordinatorAgent;
		clusterHeadList = new ArrayList<Integer>();
		successorList = new ArrayList<Integer>();
	}

	@Override
	public void action() {
		ACLMessage msg = coordinator.receive();
		if (msg != null) {
			coordinator.receiveMessageCounter(msg);
			switch (msg.getConversationId()) {
			case MessageTypes.SIMULATION_CONTROLS_BECAME_CLUSTER_HEAD:
				clusterHeadHandler(msg);
				checkForSendingTerminationMessage();
				break;
			case MessageTypes.SIMULATION_CONTROLS_JOINED_CLUSTER:
				joinedClusterHandler(msg);
				checkForSendingTerminationMessage();
				break;
			case MessageTypes.MEASUREMENT_AGENT_STATISTICS:
				receivedStatisticCounter++;
				processAgentStatistics(msg);
				checkForSimulationEnd();
				break;
			}
		}
	}

	private void processAgentStatistics(ACLMessage msg) {
		try {
			coordinator.addToReport(parseAgentStatisticsMessage(msg.getContentObject()));
		} catch (UnreadableException e) {
			e.printStackTrace();
			coordinator.addToReport(new HeedAgentStatistic(coordinator.convertAIDToInteger(msg.getSender())));
		}
		track("processAgentStatistics");
	}

	private void track(String string) {
		StringBuilder builder = new StringBuilder();
		builder.append(string);
		builder.append(";coordinator.getAgentList().size();");
		builder.append(coordinator.getAgentList().size());
		builder.append(";receivedStatisticCounter;");
		builder.append(receivedStatisticCounter);
		builder.append(";clusterHeadList;");
		builder.append(clusterHeadList);
		builder.append(";clusterHeadList.size;");
		builder.append(clusterHeadList.size());
		builder.append(";successorList;");
		builder.append(successorList);
		builder.append(";successorList.size;");
		builder.append(successorList.size());
		
		coordinator.track(builder.toString());
	}

	private HeedAgentStatistic parseAgentStatisticsMessage(Serializable contentObject) {
		return (HeedAgentStatistic) contentObject;
	}
	
	private void checkForSendingTerminationMessage() {
		track("checkForSendingTerminationMessage");
		if(clusterHeadList.size()==coordinator.getAgentList().size() || coordinator.getAgentList().size()==successorList.size()+clusterHeadList.size()){
			//all agents have become cluster heads
			
			createAndSendBroadcastMessage(MessageTypes.SIMULATION_CONTROLS_TERMINATION_REQUEST);
			
//			for(int agent : clusterHeadList){
//				sendMessage(coordinator.convertIntegerToAID(agent), MessageTypes.SIMULATION_CONTROLS_TERMINATION_REQUEST);
//			}
		}
//		else if(){
//			//check if successor have sent statistics and terminated AND if all cluster if all agents have contacted the coordinator yet
//			for(int agent : clusterHeadList){
//				sendMessage(coordinator.convertIntegerToAID(agent), MessageTypes.SIMULATION_CONTROLS_TERMINATION_REQUEST);
//			}
//		}
	}

	private void checkForSimulationEnd() {
		track("checkForSimulationEnd");
		if(receivedStatisticCounter==coordinator.getAgentList().size()){
			//received all agents statistics
			
			if(coordinator.getNoOfSimulationRuns()==coordinator.getSimulationRunCounter()){
				//all runs have been performed
				coordinator.addOwnStatisticsAndTransferReport();
				track("coordinator terminates");
				coordinator.doDelete();
			}else{
				coordinator.addOwnStatisticsAndFinalizeRun();
				coordinator.restartSimulation();
			}
		}
	}

	private void joinedClusterHandler(ACLMessage msg) {
		HeedMeasureMessage message = null;
		try {
			message = (HeedMeasureMessage) msg.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		
		for(int node : message.getSuccessorList()){
			if(!successorList.contains(node)){
				successorList.add(node);
			}
		}
	}

	private void clusterHeadHandler(ACLMessage msg) {
		if(!clusterHeadList.contains(coordinator.convertAIDToInteger(msg.getSender()))){
			clusterHeadList.add(coordinator.convertAIDToInteger(msg.getSender()));
		}
	}

	private void createAndSendBroadcastMessage(String conversationID) {
		for(int agent : coordinator.getAgentList().keySet()){
			sendMessage(coordinator.convertIntegerToAID(agent), conversationID);
		}
	}
	
	private void sendMessage(AID receiver, String conversationID) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(conversationID);
		message.addReceiver(receiver);
		coordinator.sendMessage(message);
	}

	@Override
	public boolean done() {
		return false;
	}
}
