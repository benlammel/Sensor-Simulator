package it.unicam.sensorsimulator.ui;

import it.unicam.sensorsimulator.StartEnvironment;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.logging.LogFileHandler;
import it.unicam.sensorsimulator.plugin.PluginHandler;
import it.unicam.sensorsimulator.simulationcontroller.SimulationController;
import it.unicam.sensorsimulator.ui.dialogs.GeneralDialogHandler;
import it.unicam.sensorsimulator.ui.modelling.Modeller;
import it.unicam.sensorsimulator.ui.ressources.SimulationResourcesAndProperties;
import it.unicam.sensorsimulator.ui.startdialog.SimulationStartDiolg;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;

public class ApplicationFrame extends BorderPane {
	
	private StartEnvironment startEnvironment;

	private Modeller modeller;
	
	private SimulationEnvironmentMode mode;
	private SimulationStartDiolg startDialogHandler;
	private GeneralDialogHandler generalDialogHandler;
	private PluginHandler pluginHandler;
	
	private SimulationRunInterface simulationRunFile;
	private LogFileHandler log;
	
	public ApplicationFrame(StartEnvironment startEnvironment){
		this.startEnvironment = startEnvironment;
		generalDialogHandler = new GeneralDialogHandler();
		pluginHandler = new PluginHandler(this, generalDialogHandler);
		log = LogFileHandler.getInstance();
		
		modeller = new Modeller(this);
		this.setCenter(modeller);
		
		startDialogHandler = new SimulationStartDiolg(this, pluginHandler);
		setSimulationEnvironmentMode(SimulationEnvironmentMode.MODELLING);
	}

	public void setSimulationEnvironmentMode(SimulationEnvironmentMode mode) {
		this.mode = mode;
		switch(mode){
		case MODELLING:
			break;
		case SIMULATION_IN_PROGRESS:
			break;
		}
	}
	
	public SimulationResourcesAndProperties getRessourcesAndProperties() {
		return startEnvironment.getRessourcesAndProperties();
	}

	public SimulationEnvironmentMode getSimulationEnvironmentMode() {
		return mode;
	}

	public PluginHandler getPluginHandler() {
		return pluginHandler;
	}

	public Modeller getModeller() {
		return modeller;
	}

	public GeneralDialogHandler getGeneralDialogHandler() {
		return generalDialogHandler;
	}

	public Window getMainScene() {
		return startEnvironment.getScene();
	}

	public void setCurrentSimulationFile(SimulationRunInterface simulationRunFile) {
		this.simulationRunFile = simulationRunFile;
	}

	public SimulationRunInterface getCurrentSimulationFile() {
		return simulationRunFile;
	}

	public SimulationController getSimulationController() {
		return startEnvironment.getSimulationController();
	}

	public SimulationStartDiolg getStartDialogHandler() {
		return startDialogHandler;
	}
}
