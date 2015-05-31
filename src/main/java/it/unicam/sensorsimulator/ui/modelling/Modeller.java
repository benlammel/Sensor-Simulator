package it.unicam.sensorsimulator.ui.modelling;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.PluginInterface;
import it.unicam.sensorsimulator.logging.LogFileHandler;
import it.unicam.sensorsimulator.plugin.PluginHandler;
import it.unicam.sensorsimulator.simulationcontroller.xml.SerializationTools;
import it.unicam.sensorsimulator.ui.ApplicationFrame;
import it.unicam.sensorsimulator.ui.SimulationEnvironmentMode;
import it.unicam.sensorsimulator.ui.dialogs.DialogMessages;
import it.unicam.sensorsimulator.ui.ressources.SimulationResourcesAndProperties;

public class Modeller extends BorderPane {

	private ApplicationFrame applicationFrame;
	private DrawPanel drawPanel;
	private ModellerToolbar toolbar;

	private FileChooser fileChooser;
	private LogFileHandler log;

	public Modeller(ApplicationFrame applicationFrame) {
		this.applicationFrame = applicationFrame;
		log = LogFileHandler.getInstance();

		initFileChooser();

		this.toolbar = new ModellerToolbar(this);
		this.setTop(toolbar);

		this.drawPanel = new DrawPanel(this);
		this.setCenter(drawPanel);
		setModellerMode(ModellerMode.NOCONTENT);
	}

	public void setModellerMode(ModellerMode mode) {
		toolbar.setModellerMode(mode);
	}

	public SimulationResourcesAndProperties getRessourcesAndProperties() {
		return applicationFrame.getRessourcesAndProperties();
	}

	private void initFileChooser() {
		fileChooser = new FileChooser();
	}

	public ArrayList<GeneralAgentInterface> getListOfAgents() {
		return drawPanel.getListOfAgents();
	}

	public SimulationEnvironmentMode getSimulationEnvironmentMode() {
		return applicationFrame.getSimulationEnvironmentMode();
	}

	public void buttonCommand(ModellerButtons action) {
		switch (action) {
		case ADDAGENT:
			drawPanel.addAgent();
			break;
		case CLEARPANEL:
			drawPanel.clearPanel();
			break;
		case LOADFILE:
			loadFromFile();
			break;
		case RANDOMAGENTGENERATION:
			int a = applicationFrame.getGeneralDialogHandler()
					.showIntegerInputDialog(DialogMessages.RANDOMAGENTMESSAGE);
			if (a != drawPanel.getListOfAgents().size()) {
				drawPanel.generateRandomAgents(a);
			}
			break;
		case SAVEFILE:
			fileChooser.setTitle("Save Resource File");
			applicationFrame.getPluginHandler().getCurrentPlugin()
					.refreshSettingsDialogContent();
			SerializationTools
					.saveToXML(fileChooser.showSaveDialog(applicationFrame
							.getMainScene()), applicationFrame
							.getPluginHandler()
							.generateAndReturnSimulationRunFile());
			break;
		case STARTSIMULATION:
			showStartDialog();
			break;
		case STOPSIMULATION:
			break;
		case REPORT:
			loadReport();
			break;
		}
	}

	private void loadReport() {
		Dialog dialog = applicationFrame.getGeneralDialogHandler().createPluginSelectionDialog();		
		Optional<PluginInterface> result = dialog.showAndWait();

		PluginInterface plugin = result.get();
		fileChooser.setTitle("Load Report File");
		applicationFrame.showReportFromFile(fileChooser.showOpenDialog(applicationFrame.getMainScene()), plugin);
	}

	private void showStartDialog() {
		Optional<ButtonType> result = applicationFrame.getStartDialogHandler()
				.updateAndDisplay();
		if (result.get() == applicationFrame.getStartDialogHandler()
				.getStartButton()) {
			try {
				applicationFrame.getSimulationController().performRun(
						applicationFrame.getPluginHandler()
								.generateAndReturnSimulationRunFile());
			} catch (Exception e) {
				e.printStackTrace();
				log.catching(e);
				applicationFrame.getGeneralDialogHandler().showDialog(
						DialogMessages.ERRORSTARTINGSIMULATION, e);
			}
		}
	}

	private void loadFromFile() {
		if (applicationFrame.getPluginHandler().getCurrentPlugin() != null) {
			fileChooser.setTitle("Open Resource File");
			openXMLFileHandling(fileChooser.showOpenDialog(applicationFrame
					.getMainScene()), applicationFrame.getPluginHandler()
					.getSimulationRunFileClass());
		} else {
			applicationFrame.getGeneralDialogHandler().showDialog(
					DialogMessages.CHOOSEPLUGIN);
		}
	}

	private void openXMLFileHandling(File file, Class<?> parseClass) {
		if (file != null) {
			try {
				applicationFrame.setCurrentSimulationFile(SerializationTools
						.loadXMLRunFile(file, parseClass));
				if (applicationFrame.getCurrentSimulationFile() != null) {
					drawPanel.addAgentBatch(applicationFrame
							.getCurrentSimulationFile());
					applicationFrame.getPluginHandler().updateRunFile(
							applicationFrame.getCurrentSimulationFile());
				}
			} catch (JAXBException e) {
				log.catching(e);
				applicationFrame.getGeneralDialogHandler().showDialog(
						DialogMessages.FILEDOESNOTFITPLUGIN);
			}
		}
	}

	public DrawPanel getDrawPanel() {
		return drawPanel;
	}

	public PluginHandler getPluginHandler() {
		return applicationFrame.getPluginHandler();
	}

}
