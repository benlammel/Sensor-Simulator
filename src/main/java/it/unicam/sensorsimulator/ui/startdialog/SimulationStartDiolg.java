package it.unicam.sensorsimulator.ui.startdialog;

import java.util.ArrayList;
import java.util.Optional;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface;
import it.unicam.sensorsimulator.interfaces.PluginInterface;
import it.unicam.sensorsimulator.interfaces.SimulationEnvironmentServices;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.logging.LogFileHandler;
import it.unicam.sensorsimulator.plugin.PluginHandler;
import it.unicam.sensorsimulator.ui.ApplicationFrame;
import it.unicam.sensorsimulator.ui.panels.DrawPanel;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.BorderPane;

public class SimulationStartDiolg extends Dialog<ButtonType> {

	private ApplicationFrame applicationFrame;
	private DrawPanel drawPanel;
	private PluginHandler pluginHandler;
	private ButtonType btnOK;
	private BorderPane pane;
	
	public SimulationStartDiolg(ApplicationFrame applicationFrame, DrawPanel drawPanel, PluginHandler pluginHandler) {
		this.applicationFrame = applicationFrame;
		this.drawPanel = drawPanel;
		this.pluginHandler = pluginHandler;
		
		pane = new BorderPane();
		initDialog();
		
		getDialogPane().setContent(pane);
		this.setResizable(true);

		getDialogPane().getButtonTypes().addAll(btnOK, ButtonType.CANCEL);
	}

	private void initDialog() {
		setTitle("Simulation Settings");
		setHeaderText("Simulation Settings");
		btnOK = new ButtonType("Start Simulation", ButtonData.OK_DONE);
	}

	public ButtonType getStartButton() {
		return btnOK;
	}

	public Optional<ButtonType> updateAndDisplay() {
		pluginHandler.getCurrentPlugin().refreshSettingsDialogContent();
		pane.setCenter(pluginHandler.getCurrentPlugin().getSettingsDialogContent());
		return super.showAndWait();
	}

	
//	public ArrayList<GeneralAgentInterface> getAgentList(){
//		return drawPanel.getListOfAgents();
//	}
//
//	public SimulationRunInterface generateAndReturnSimulationRunFile() {
//		return pluginHandler.getCurrentPlugin().generateAndReturnSimulationRunFile();
//	}
//
//	public void updateRunFile(SimulationRunInterface simulationRunFile) {
//		System.out.println(".... " +simulationRunFile);
//		pluginHandler.getCurrentPlugin().updateSimulationRunFile(simulationRunFile);
//	}

}
