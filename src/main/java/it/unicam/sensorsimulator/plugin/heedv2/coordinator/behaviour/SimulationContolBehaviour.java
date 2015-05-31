package it.unicam.sensorsimulator.plugin.heedv2.coordinator.behaviour;

import java.io.Serializable;
import java.util.ArrayList;

import it.unicam.sensorsimulator.plugin.heedv2.coordinator.Heedv2SimulationCoordinatorAgent;
import it.unicam.sensorsimulator.plugin.heedv2.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heedv2.messages.MessageTypes.MessageHandling;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.report.HeedAgentStatistic;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class SimulationContolBehaviour extends Behaviour {
	
	private Heedv2SimulationCoordinatorAgent coordinator;
	private ArrayList<Integer> clusterHeadList;
	private ArrayList<Integer> successorList;
	private int receivedStatisticCounter = 0;

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
			switch (msg.getConversationId()) {
			case MessageTypes.SIMULATION_CONTROLS_BECAME_CLUSTER_HEAD:
				coordinator.receiveMessageCounter(msg, MessageHandling.INCREASE);
				clusterHeadHandler(msg);
				checkForSendingTerminationMessage();
				break;
			case MessageTypes.SIMULATION_CONTROLS_JOINED_CLUSTER:
				coordinator.receiveMessageCounter(msg, MessageHandling.INCREASE);
				joinedClusterHandler(msg);
				checkForSendingTerminationMessage();
				break;
			case MessageTypes.MEASUREMENT_AGENT_STATISTICS:
				coordinator.receiveMessageCounter(msg, MessageHandling.INCREASE);
				processAgentStatistics(msg);
				checkForSimulationEnd();
				break;
			default:
				coordinator.putBack(msg);
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
		receivedStatisticCounter ++;
		track("processAgentStatistics");
	}

	private void track(String string) {
		StringBuilder builder = new StringBuilder();
		builder.append(string);
		builder.append(";coordinator.getAgentList().size();");
		builder.append(coordinator.getAgentList().size());
		builder.append(";receivedStatisticCounter;");
		builder.append(receivedStatisticCounter);
		builder.append(";successorList;");
		builder.append(successorList);
		builder.append(";clusterHeadList;");
		builder.append(clusterHeadList);
		
		coordinator.track(builder.toString());
	}

	private HeedAgentStatistic parseAgentStatisticsMessage(Serializable contentObject) {
		return (HeedAgentStatistic) contentObject;
	}

	private void checkForSimulationEnd() {
		track("checkForSimulationEnd");
		if(receivedStatisticCounter==coordinator.getAgentList().size()){
			//All agents sent statistics
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
		if(!successorList.contains(coordinator.convertAIDToInteger(msg.getSender()))){
			successorList.add(coordinator.convertAIDToInteger(msg.getSender()));
		}
		
	}

	private void clusterHeadHandler(ACLMessage msg) {
		if(!clusterHeadList.contains(coordinator.convertAIDToInteger(msg.getSender()))){
			clusterHeadList.add(coordinator.convertAIDToInteger(msg.getSender()));
		}
		
		
	}

	private void checkForSendingTerminationMessage() {
		if(successorList.size()+clusterHeadList.size()==coordinator.getAgentList().size()){
			createAndSendBroadcastMessage(MessageTypes.SIMULATION_CONTROLS_TERMINATION_REQUEST);
		}
	}

	private void createAndSendBroadcastMessage(String conversationID) {
		for(int agent : coordinator.getAgentList().keySet()){
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			message.setConversationId(conversationID);
			message.addReceiver(coordinator.convertIntegerToAID(agent));
			coordinator.sendMessage(message);
		}
	}

	@Override
	public boolean done() {
		return false;
	}
}
