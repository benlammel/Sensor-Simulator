package it.unicam.sensorsimulator.plugin.basestation.agents.coordinator.behaviours;

import java.util.Map.Entry;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.plugin.basestation.agents.coordinator.SimulationCoordinatorAgent;
import it.unicam.sensorsimulator.plugin.basestation.messages.MessageTypes;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class TriggerAssociationBehaviour extends Behaviour {

	private SimulationCoordinatorAgent simulationCoordinatorAgent;

	public TriggerAssociationBehaviour(
			SimulationCoordinatorAgent simulationCoordinatorAgent) {
		this.simulationCoordinatorAgent = simulationCoordinatorAgent;
	}

	@Override
	public void action() {
		
		for(Entry<Integer, GeneralAgentInterface> agent : simulationCoordinatorAgent.getAgentList().entrySet()){
			sendStartTrigger(new AID(Integer.toString(agent.getKey()), AID.ISLOCALNAME));
		}
		
//		sendNeighborsList(new AID(Integer.toString(agent.getKey()), AID.ISLOCALNAME), nList);
	}

	@Override
	public boolean done() {
		return true;
	}
	
	private void sendStartTrigger(AID aid){
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(MessageTypes.START_FORMING);
		message.addReceiver(aid);
		simulationCoordinatorAgent.sendMessage(message);
	}
}
