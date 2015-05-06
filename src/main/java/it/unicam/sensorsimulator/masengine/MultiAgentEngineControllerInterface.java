package it.unicam.sensorsimulator.masengine;

import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;

public interface MultiAgentEngineControllerInterface {

	public void shutdown();

	public void performSimulation(SimulationRunInterface simulationRun) throws Exception;

}
