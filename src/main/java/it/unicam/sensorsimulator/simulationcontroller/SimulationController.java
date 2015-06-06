package it.unicam.sensorsimulator.simulationcontroller;

import it.unicam.sensorsimulator.StartEnvironment;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface;
import it.unicam.sensorsimulator.interfaces.ReportInterface;
import it.unicam.sensorsimulator.interfaces.SimulationControlInterface;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.logging.LogFileHandler;
import it.unicam.sensorsimulator.masengine.MultiAgentEngineControllerInterface;
import it.unicam.sensorsimulator.masengine.jade.JadeEngineController;
import it.unicam.sensorsimulator.persistence.Folder;
import it.unicam.sensorsimulator.simulationcontroller.xml.SerializationTools;
import it.unicam.sensorsimulator.ui.SimulationEnvironmentMode;

public class SimulationController implements SimulationControlInterface {
	
	private StartEnvironment startEnvironment;
	private MultiAgentEngineControllerInterface masController;
	private final String CURRENTRUNFILENAME = "currentrun.xml";
	private final String CURRENTREPORTFILENAME = "currentreport.xml";
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

	public void setSimulationEnvironmentMode(
			SimulationEnvironmentMode value) {
		startEnvironment.getApplicationFrame().setSimulationEnvironmentMode(value);
	}

	public Class<?> getCoordinatorAgentClass() {
		return startEnvironment.getApplicationFrame().getPluginHandler().getSimulationCoordinatorAgent();
	}

	public void abortOngoingSimulation() {
		masController.abortOngoingSimulation();
		startEnvironment.getApplicationFrame().setSimulationEnvironmentMode(SimulationEnvironmentMode.SIMULATION_CANCELED);
	}
	
	public StartEnvironment getStartEnvironment(){
		return startEnvironment;
	}

	@Override
	public void startSimulation() {
		startEnvironment.getApplicationFrame().setSimulationEnvironmentMode(SimulationEnvironmentMode.SIMULATION_IN_PROGRESS);
	}

	@Override
	public void addAndCreateReport(ReportInterface report) {
		SerializationTools.saveToXML(Folder.TEMPFOLDER.toString()+CURRENTREPORTFILENAME, report);
		startEnvironment.getApplicationFrame().setSimulationEnvironmentMode(SimulationEnvironmentMode.SIMULATION_COMPLETED);
		startEnvironment.getApplicationFrame().addAndCreateReport(report);
	}

}
