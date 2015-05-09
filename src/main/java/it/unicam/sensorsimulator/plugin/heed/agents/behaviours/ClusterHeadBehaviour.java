package it.unicam.sensorsimulator.plugin.heed.agents.behaviours;

import java.io.IOException;

import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface.LogLevels;
import it.unicam.sensorsimulator.plugin.heed.agents.GeneralAgent;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes.MessageHandling;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class ClusterHeadBehaviour extends Behaviour {

	private GeneralAgent agent;

	public ClusterHeadBehaviour(GeneralAgent agent) {
		this.agent = agent;
		measurementCluster();
	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		if (msg != null) {
			switch (msg.getConversationId()) {
			case MessageTypes.JOIN_CLUSTER:
				agent.receiveMessageCounter(msg, MessageHandling.INCREASE);
				processJoinMessage(msg);
				break;
			default:
				agent.putBack(msg);
				break;
			}
		}
	}

	@Override
	public boolean done() {
		return false;
	}
	
	private void processJoinMessage(ACLMessage msg) {
		agent.addToMyCluster(agent.convertAgentAIDToInteger(msg.getSender()));
		measurementCluster();
	}
	
	private void measurementCluster() {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(MessageTypes.MEASUREMENT_CLUSTERFORMING_PROTOCOL);
		try {
			message.setContentObject(agent.getMyClusterView());
		} catch (IOException e) {
			e.printStackTrace();
		}
		message.addReceiver(agent.getSimulationCoordinatorAID());
		agent.sendMessage(message);
	}
	
	private void track() {
		StringBuilder builder = new StringBuilder();
		builder.append("ClusterHeadBehaviour ;ID;");
		builder.append(agent.getAgentConfiguration().getAgentID());
//		builder.append(";isFinal;");
//		builder.append(isFinalCH);
//		builder.append(";cprob;");
//		builder.append(cProb);
//		builder.append(";chprob;");
//		builder.append(chProb);
//		builder.append(";pmin;");
//		builder.append(pMin);
//		builder.append(";listOfClusterHeads;");
//		builder.append(listOfClusterHeads.keySet().toString());
//		builder.append(";chPrevious;");
//		builder.append(chPrevious);

		agent.getLog().logAgentAction(LogLevels.INFO, builder.toString());
	}
}
