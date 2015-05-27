package it.unicam.sensorsimulator.ui.startdialog;

import java.util.Optional;

import it.unicam.sensorsimulator.plugin.PluginHandler;
import it.unicam.sensorsimulator.ui.ApplicationFrame;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.BorderPane;

public class SimulationStartDiolg extends Dialog<ButtonType> {

	private ApplicationFrame applicationFrame;
	private PluginHandler pluginHandler;
	private ButtonType btnOK;
	private ButtonType btnAPPLY;
	private BorderPane pane;
	
	public SimulationStartDiolg(ApplicationFrame applicationFrame, PluginHandler pluginHandler) {
		this.applicationFrame = applicationFrame;
		this.pluginHandler = pluginHandler;
		
		pane = new BorderPane();
		initDialog();
		
		getDialogPane().setContent(pane);
		this.setResizable(true);

		getDialogPane().getButtonTypes().addAll(btnAPPLY, btnOK, ButtonType.CANCEL);
	}

	private void initDialog() {
		setTitle("Simulation Settings");
		setHeaderText("Simulation Settings");
		btnOK = new ButtonType("Start Simulation", ButtonData.OK_DONE);
		btnAPPLY = new ButtonType("Apply", ButtonData.APPLY);
	}

	public ButtonType getStartButton() {
		return btnOK;
	}

	public Optional<ButtonType> updateAndDisplay() {
		pluginHandler.getCurrentPlugin().refreshSettingsDialogContent();
		pane.setCenter(pluginHandler.getCurrentPlugin().getSettingsDialogContent());
		return super.showAndWait();
	}

}
