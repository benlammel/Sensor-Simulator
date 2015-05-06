package it.unicam.sensorsimulator.simulationcontroller;

import javafx.scene.Parent;
import jade.wrapper.StaleProxyException;
import it.unicam.sensorsimulator.StartEnvironment;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.logging.LogFileHandler;
import it.unicam.sensorsimulator.masengine.MultiAgentEngineControllerInterface;
import it.unicam.sensorsimulator.masengine.jade.JadeEngineController;
import it.unicam.sensorsimulator.persistence.Folder;
import it.unicam.sensorsimulator.simulationcontroller.xml.SerializationTools;
import it.unicam.sensorsimulator.ui.SimulationEnvironmentMode;

public class SimulationController implements SimulationControllerInterface {
	
	private StartEnvironment startEnvironment;
	private MultiAgentEngineControllerInterface masController;
	private final String CURRENTRUNFILENAME = "currentrun.xml";
	private LogFileWriterInterface log;

	public SimulationController(StartEnvironment startEnvironment) {
		this.startEnvironment = startEnvironment;
		masController = new JadeEngineController(this);
		log = LogFileHandler.getInstance();
	}

	public void stop() {
		masController.shutdown();
	}
	
	public void performRun(SimulationRunInterface simulationRunFile) throws Exception {
		SerializationTools.saveToXML(Folder.TEMPFOLDER.toString()+CURRENTRUNFILENAME, simulationRunFile);
		masController.performSimulation(simulationRunFile);
	}

	public void setSimulationEnvironmentMode(SimulationEnvironmentMode value, StaleProxyException e) {
		startEnvironment.getApplicationFrame().setSimulationEnvironmentMode(value, e);
	}

	public void setSimulationEnvironmentMode(
			SimulationEnvironmentMode value) {
		startEnvironment.getApplicationFrame().setSimulationEnvironmentMode(value);
	}

	public Class<?> getCoordinatorAgentClass() {
		return startEnvironment.getApplicationFrame().getPluginHandler().getSimulationCoordinatorAgent();
	}
	
	public Parent getReportingPane() {
		return startEnvironment.getApplicationFrame().getPluginHandler().getReportingPane();
	}
}
