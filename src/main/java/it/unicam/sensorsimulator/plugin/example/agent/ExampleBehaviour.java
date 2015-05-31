package it.unicam.sensorsimulator.plugin.example.agent;

import java.io.IOException;
import java.util.Random;

import it.unicam.sensorsimulator.plugin.example.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.example.reporting.report.ExampleAgentStatistics;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class ExampleBehaviour extends Behaviour {

	private ExampleAgent agent;

	public ExampleBehaviour(ExampleAgent agent) {
		this.agent = agent;
	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		if (msg != null) {
			switch (msg.getConversationId()) {
			case MessageTypes.FIRST_MESSAGE:
				agent.receiveMessageCounter(msg);
				doSomeOperations(msg);
				break;
			case MessageTypes.TERMINATION_REQUEST:
				agent.receiveMessageCounter(msg);
				sendStatisticsAndTerminate(msg);
				break;
			}
		}
	}

	private void sendStatisticsAndTerminate(ACLMessage msg) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(MessageTypes.MEASUREMENTS_REPLY);
		try {
			message.setContentObject(new ExampleAgentStatistics(Integer.parseInt(agent.getAID().getLocalName()), agent.getSentMsgCounter(), agent.getReceivedMsgCounter()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		message.addReceiver(msg.getSender());
		agent.sendMessage(message);
		terminate();
	}

	private void terminate() {
		agent.doDelete();		
	}

	private void doSomeOperations(ACLMessage msg) {
		Random r = new Random();
		if(r.nextBoolean()){
			sendReply(msg.getSender(), MessageTypes.FIRST_MESSAGE_REPLY_OPTION_1);
		}else{
			sendReply(msg.getSender(), MessageTypes.FIRST_MESSAGE_REPLY_OPTION_2);
		}
	}

	private void sendReply(AID receiver, String conversationID) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(conversationID);
		message.addReceiver(receiver);
		agent.sendMessage(message);
	}

	@Override
	public boolean done() {
		return false;
	}

}
