package it.unicam.sensorsimulator.ui.dialogs;

import javafx.scene.control.Alert.AlertType;

public enum DialogMessages {
	PLUGINLOADPROBLEM(AlertType.ERROR, "Plugin Error", "There was a problem loading the plugins!"),
	CHOOSEPLUGIN(AlertType.ERROR, "Plugin Error", "You need to choose a plugin first before opening a file"),
	FILEDOESNOTFITPLUGIN(AlertType.ERROR, "Plugin Error", "The choosen file does not fit to the selected plugin"),
	ERRORSTARTINGSIMULATION(AlertType.ERROR, "Simulation Error", "The simulation could not be started"),
	RANDOMAGENTMESSAGE(AlertType.INFORMATION, "Random Agent Generation", "Please enter the number of agent taht should be places randomly");
	
	AlertType type;
	String message;
	String header;
	
	DialogMessages(AlertType type, String header, String message) {
		this.type = type;
		this.header = header;
		this.message = message;
	}

	public AlertType getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	public String getHeader() {
		return header;
	}
}
