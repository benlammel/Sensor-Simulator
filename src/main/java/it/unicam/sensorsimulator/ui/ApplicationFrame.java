package it.unicam.sensorsimulator.ui;

import jade.wrapper.StaleProxyException;

import java.io.File;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import it.unicam.sensorsimulator.StartEnvironment;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.logging.LogFileHandler;
import it.unicam.sensorsimulator.plugin.PluginHandler;
import it.unicam.sensorsimulator.simulationcontroller.xml.SerializationTools;
import it.unicam.sensorsimulator.ui.dialogs.DialogMessages;
import it.unicam.sensorsimulator.ui.dialogs.GeneralDialogHandler;
import it.unicam.sensorsimulator.ui.panels.DrawPanel;
import it.unicam.sensorsimulator.ui.ressources.SimulationRessourcesAndProperties;
import it.unicam.sensorsimulator.ui.startdialog.SimulationStartDiolg;
import it.unicam.sensorsimulator.ui.tabmenu.TabMenu;
import it.unicam.sensorsimulator.ui.tabmenu.tabs.TabButtons;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

public class ApplicationFrame extends BorderPane {
	
	private StartEnvironment startEnvironment;
//	private SimulationController simulationController;

	private TabMenu tabMenu;
	private DrawPanel drawPanel;
	private FileChooser fileChooser;
	private SimulationEnvironmentMode mode;
	private SimulationStartDiolg startDialogHandler;
	private GeneralDialogHandler generalDialogHandler;
	private PluginHandler pluginHandler;
	
	private SimulationRunInterface simulationRun;
	private LogFileHandler log;
	
	public ApplicationFrame(StartEnvironment startEnvironment){
		this.startEnvironment = startEnvironment;
//		simulationController = startEnvironment.getSimulationController();
		generalDialogHandler = new GeneralDialogHandler();
		pluginHandler = new PluginHandler(this, generalDialogHandler);
		log = LogFileHandler.getInstance();
		initFileChooser();
		
		tabMenu = new TabMenu(this);
		this.setTop(tabMenu);
		
		drawPanel = new DrawPanel(this);
		this.setCenter(drawPanel);
		
		startDialogHandler = new SimulationStartDiolg(this, drawPanel, pluginHandler);
		setSimulationEnvironmentMode(SimulationEnvironmentMode.DRAWTAB);
	}

	private void initFileChooser() {
		fileChooser = new FileChooser();
	}

	public void setSimulationEnvironmentMode(SimulationEnvironmentMode mode) {
		this.mode = mode;
		switch(mode){
		case DRAWTAB:
			this.setCenter(drawPanel);
			break;
		case SIMULATION:
			break;
		case ABORT:
			break;
		case REPORTINGTAB:
			this.setCenter(pluginHandler.getCurrentPlugin().getReportingPane());
			break;
		case RUNNINGSIMULATION:
			break;
		default:
			break;
		}
	}
	
	public void setSimulationEnvironmentMode(SimulationEnvironmentMode mode, StaleProxyException e) {
		this.mode = mode;
		switch(mode){
		case ABORT:
			generalDialogHandler.showDialog(DialogMessages.ERRORSTARTINGSIMULATION, e);
			break;
		default:
			break;
		}
	}

	public DrawPanel getDrawPanel() {
		return drawPanel;
	}

	public void buttonCommand(TabButtons action) {
		switch(action){
		case CLEARPANEL:
			break;
		case LOADFILE:
			loadFromFile();
			break;
		case PAUSESIMULATION:
			break;
		case SAVEFILE:
			fileChooser.setTitle("Save Resource File");
			SerializationTools.saveToXML(fileChooser.showSaveDialog(startEnvironment.getScene()), pluginHandler.generateAndReturnSimulationRunFile());
			break;
		case STARTSIMULATION:
			showStartDialog();
			break;
		case STOPSIMULATION:
			startEnvironment.getSimulationController().abortOngoingSimulation();
			break;
		case ADDAGENT:
			drawPanel.addAgent();
			break;
		default:
			break;
		}
	}

	private void loadFromFile() {
		if(pluginHandler.getCurrentPlugin()!=null){
			fileChooser.setTitle("Open Resource File");
			openXMLFileHandling(fileChooser.showOpenDialog(startEnvironment.getScene()), pluginHandler.getSimulationRunFileClass());
		}else{
			generalDialogHandler.showDialog(DialogMessages.CHOOSEPLUGIN);
		}
	}

	private void showStartDialog() {
		Optional<ButtonType> result = startDialogHandler.updateAndDisplay();
		if(result.get() == startDialogHandler.getStartButton()){
			 try {
				 startEnvironment.getSimulationController().performRun(pluginHandler.generateAndReturnSimulationRunFile());
			} catch (Exception e) {
				e.printStackTrace();
				log.catching(e);
				generalDialogHandler.showDialog(DialogMessages.ERRORSTARTINGSIMULATION, e);
			}
			 }		
	}

	private void openXMLFileHandling(File file, Class<?> parseClass) {
		if(file!=null){
			try {
				simulationRun = SerializationTools.loadXMLRunFile(file, parseClass);
				if(simulationRun!=null){
					drawPanel.addAgentBatch(simulationRun);
					pluginHandler.updateRunFile(simulationRun);
				}
			} catch (JAXBException e) {
				log.catching(e);
				generalDialogHandler.showDialog(DialogMessages.FILEDOESNOTFITPLUGIN);
			}
		}
	}

	public SimulationRessourcesAndProperties getRessourcesAndProperties() {
		return startEnvironment.getRessourcesAndProperties();
	}

	public SimulationEnvironmentMode getSimulationEnvironmentMode() {
		return mode;
	}

	public PluginHandler getPluginHandler() {
		return pluginHandler;
	}

	public SimulationRunInterface getSimulationRunFile() {
		return simulationRun;
	}
}
