package it.unicam.sensorsimulator.plugin.heedv2.coordinator.behaviour;

import it.unicam.sensorsimulator.plugin.heedv2.coordinator.Heedv2SimulationCoordinatorAgent;
import jade.core.behaviours.Behaviour;

public class SimulationContolBehaviour extends Behaviour {
	
	private Heedv2SimulationCoordinatorAgent heedv2SimulationCoordinatorAgent;

	public SimulationContolBehaviour(
			Heedv2SimulationCoordinatorAgent heedv2SimulationCoordinatorAgent) {
		this.heedv2SimulationCoordinatorAgent = heedv2SimulationCoordinatorAgent;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
