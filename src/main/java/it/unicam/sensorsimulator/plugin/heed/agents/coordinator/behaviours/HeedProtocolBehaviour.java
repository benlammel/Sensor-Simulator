package it.unicam.sensorsimulator.plugin.heed.agents.coordinator.behaviours;

import java.util.Map.Entry;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.plugin.heed.agents.coordinator.SimulationCoordinatorAgent;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes;
import it.unicam.sensorsimulator.plugin.heed.messages.MessageTypes.MessageHandling;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class HeedProtocolBehaviour extends CyclicBehaviour {

	SimulationCoordinatorAgent simulationCoordinatorAgent;
	private boolean inizalizationTriggerSent = false;
	
	public HeedProtocolBehaviour(
			SimulationCoordinatorAgent simulationCoordinatorAgent) {
		this.simulationCoordinatorAgent = simulationCoordinatorAgent;
	}

	@Override
	public void action() {
		if(!inizalizationTriggerSent ){
			sendInizializationTrigger();
		}
		
		ACLMessage msg = simulationCoordinatorAgent.receive();
		if (msg != null) {
			simulationCoordinatorAgent.receiveMessageCounter(msg, MessageHandling.INCREASE);
			switch(msg.getConversationId()){
			case MessageTypes.START_INIZIALIZATION:
				break;
			default:
				simulationCoordinatorAgent.putBack(msg);
				simulationCoordinatorAgent.receiveMessageCounter(msg, MessageHandling.DECREASE);
				break;
			}
		}
	}

	private void sendInizializationTrigger() {
		for(Entry<Integer, GeneralAgentInterface> agent : simulationCoordinatorAgent.getAgentList().entrySet()){
			sendMessageInizalizationTriggerToAgent(new AID(Integer.toString(agent.getKey()), AID.ISLOCALNAME));
		}
		inizalizationTriggerSent = true;
	}

	private void sendMessageInizalizationTriggerToAgent(AID aid) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(MessageTypes.START_INIZIALIZATION);
		message.addReceiver(aid);
		simulationCoordinatorAgent.sendMessage(message);
	}
}
