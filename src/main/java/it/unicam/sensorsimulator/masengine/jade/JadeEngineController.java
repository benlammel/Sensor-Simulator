package it.unicam.sensorsimulator.masengine.jade;

import java.util.ArrayList;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.tools.rma.rma;
import jade.tools.introspector.Introspector;
import jade.tools.sniffer.Sniffer;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.logging.LogFileHandler;
import it.unicam.sensorsimulator.masengine.MultiAgentEngineControllerInterface;
import it.unicam.sensorsimulator.masengine.jade.tools.JadePlatformAgents;
import it.unicam.sensorsimulator.simulationcontroller.SimulationController;

public class JadeEngineController implements
		MultiAgentEngineControllerInterface {

	private static final int CONTAINER_PORT = 1200;

	private AgentContainer mainContainer;
	private AgentContainer agentContainer;
	private ArrayList<AgentController> agentRunController;
	private SimulationController simulationController;
	
	private AgentController simulationCoordinatorAgent = null;
	private AgentController snifferController;
	private AgentController rmaController;
	private AgentController inspectorController;
	
	private SimulationRunInterface currentRunFile;
	
	public JadeEngineController(SimulationController simulationController) {
		this.simulationController = simulationController;
		mainContainer = getJadeRunTime().createMainContainer(
				new ProfileImpl(null, CONTAINER_PORT, null));
		agentContainer = getJadeRunTime().createAgentContainer(
				new ProfileImpl(null, CONTAINER_PORT, null));
		
		agentRunController = new ArrayList<AgentController>();
	}

	private static jade.core.Runtime getJadeRunTime() {
		return Runtime.instance();
	}

	@Override
	public void shutdown() {
		System.exit(0);
	}

	@Override
	public void performSimulation(SimulationRunInterface simulationRun) throws Exception {
		
		startRMA(simulationRun.getStartMASObservationUI());
		startSniffer(simulationRun.getStartSnifferAgent());
		startInspectorAgent(simulationRun.getStartSnifferAgent());
		setupCoordinatorAgentAndRun(simulationRun);
	}

	private void setupCoordinatorAgentAndRun(SimulationRunInterface simulationRun) throws StaleProxyException {
		currentRunFile = simulationRun;
		Object[] args = new Object[3];
		args[0] = LogFileHandler.getInstance();
		args[1] = simulationRun;
		args[2] = simulationController.getReportingPane();
			
		simulationCoordinatorAgent = agentContainer.createNewAgent("SimulationCoordinator", loadCoordinatorClass().getCanonicalName(), args);
		agentRunController.add(simulationCoordinatorAgent);
		simulationCoordinatorAgent.start();
	}

	private Class<?> loadCoordinatorClass() {
		return simulationController.getCoordinatorAgentClass();
	}

	private void startInspectorAgent(boolean value) throws StaleProxyException {
		if (value) {
			inspectorController = agentContainer.createNewAgent(JadePlatformAgents.INSPECTOR.getName(), Introspector.class.getCanonicalName(), null);
			inspectorController.start();
		}
	}

	private void startSniffer(boolean value) throws StaleProxyException {
		if (value) {
			Object[] args = new Object[1];
			args[0] = "ID*";

			snifferController = agentContainer.createNewAgent(JadePlatformAgents.SNIFFER.getName(),
					Sniffer.class.getCanonicalName(), args);
			snifferController.start();
		}
	}

	private void startRMA(boolean value) throws StaleProxyException {
		if (value) {
			rmaController = agentContainer.createNewAgent(JadePlatformAgents.RMA.getName(),
					rma.class.getCanonicalName(), null);
			rmaController.start();
			
		}
	}

	@Override
	public void abortOngoingSimulation() {	
		try {
			agentContainer.kill();
			agentContainer = getJadeRunTime().createAgentContainer(
					new ProfileImpl(null, CONTAINER_PORT, null));
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
}
