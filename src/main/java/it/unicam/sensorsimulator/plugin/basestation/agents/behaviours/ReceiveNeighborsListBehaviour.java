package it.unicam.sensorsimulator.plugin.basestation.agents.behaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.plugin.basestation.agents.GeneralAgent;
import it.unicam.sensorsimulator.plugin.basestation.messages.MessageTypes;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class ReceiveNeighborsListBehaviour extends CyclicBehaviour {

	private GeneralAgent agent;
	
	private ArrayList<Integer> alreadyReceivedNeighborCheckList;

	public ReceiveNeighborsListBehaviour(GeneralAgent agent) {
		this.agent = agent;
		this.alreadyReceivedNeighborCheckList = new ArrayList<Integer>();
	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		if (msg != null) {
			agent.receiveMessageCounter(msg);
			switch (msg.getConversationId()) {
			case MessageTypes.START_FORMING:
				agent.setSimulationCoordinatorAID(msg.getSender());
				neighborListWalkThrough();
				break;
			case MessageTypes.REQUEST_FOR_NEIGBORLIST_EXCHANGE:
				alreadyReceivedNeighborCheckList.add(convertAIDToInteger(msg.getSender()));
				parseAndCompareNeighborLists(msg);
				break;
			default:
				agent.putBack(msg);
				break;
			}
		}
	}

	private void parseAndCompareNeighborLists(ACLMessage msg) {
		HashMap<Integer, GeneralAgentInterface> tempNList = null;
		try {
			tempNList = (HashMap<Integer, GeneralAgentInterface>) msg
					.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		calculateStrongNeighbors(tempNList,
				Integer.parseInt(msg.getSender().getLocalName()));
	}
	
	private Integer convertAIDToInteger(AID aid) {
		return Integer.parseInt(aid.getLocalName());
	}

	private void calculateStrongNeighbors(
			HashMap<Integer, GeneralAgentInterface> tempNList, int senderID) {
		if (tempNList != null) {
			for (Entry<Integer, GeneralAgentInterface> myNeighbor : agent
					.getNeighborList().entrySet()) {
				if (tempNList.containsKey(myNeighbor.getKey())
						&& myNeighbor.getKey() != agent.getAgentConfiguration()
								.getAgentID()
						&& senderID != myNeighbor.getKey()) {
//					System.out.println(convertAIDToInteger(agent.getAID()) +" :: sender: " +senderID +" :: " +myNeighbor.getKey());
					updateConnectionView(myNeighbor.getKey(),
							tempNList.get(myNeighbor.getKey()), senderID);
				}
			}
		}
		
		if(agent.getNeighborList().size()==(alreadyReceivedNeighborCheckList.size()+1)){
//			System.out.println("*****" +convertAIDToInteger(agent.getAID()) +" :: " +agent.getNeighborList().size() +" :. " +alreadyReceivedNeighborCheckList.size());
			sentMeasurementPoint1Message();
		}
	}

	private void sentMeasurementPoint1Message() {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(MessageTypes.MEASUREMENT_POINT_1);
		try {
			message.setContentObject(agent.getConnectionView());
		} catch (IOException e) {
			e.printStackTrace();
		}
		message.addReceiver(agent.getSimulationCoordinatorAID());
		agent.sendMessage(message);
		
	}

	private void updateConnectionView(Integer key,
			GeneralAgentInterface othersNeighbor, int senderID) {
		
		GeneralAgentInterface me = agent.getNeighborList().get(
				agent.getAgentConfiguration().getAgentID());
		GeneralAgentInterface theOther = agent.getNeighborList().get(senderID);

		double myValue = Math
				.sqrt(Math.pow(
						me.getLocationX() - othersNeighbor.getLocationX(), 2)
						+ Math.pow(
								me.getLocationY()
										- othersNeighbor.getLocationY(), 2));
		double othersValue = Math
				.sqrt(Math.pow(
						theOther.getLocationX() - othersNeighbor.getLocationX(),
						2)
						+ Math.pow(
								theOther.getLocationY()
										- othersNeighbor.getLocationY(), 2));

		
		
		if (myValue < othersValue) {
			agent.getConnectionView().remove(othersNeighbor.getAgentID());
			System.out.println(convertAIDToInteger(agent.getAID()) +" :: removes : " +othersNeighbor.getAgentID());
		}
	}

	private void neighborListWalkThrough() {
		for (Entry<Integer, GeneralAgentInterface> neighbor : agent
				.getNeighborList().entrySet()) {
			if(neighbor.getValue().getAgentID()!=agent.getAgentConfiguration().getAgentID()){
				ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
				message.setConversationId(MessageTypes.REQUEST_FOR_NEIGBORLIST_EXCHANGE);
				try {
					message.setContentObject(agent.getNeighborList());
				} catch (IOException e) {
					e.printStackTrace();
				}
				message.addReceiver(new AID(Integer.toString(neighbor.getValue()
						.getAgentID()), AID.ISLOCALNAME));
				agent.sendMessage(message);
			}
		}
	}
}
