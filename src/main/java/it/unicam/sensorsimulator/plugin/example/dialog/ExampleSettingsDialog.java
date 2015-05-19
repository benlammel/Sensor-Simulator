package it.unicam.sensorsimulator.plugin.example.dialog;

import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.plugin.example.PluginExample;
import javafx.scene.layout.BorderPane;

public class ExampleSettingsDialog extends BorderPane {
	
	private PluginExample pluginExample;

	public ExampleSettingsDialog(PluginExample pluginExample) {
		this.pluginExample = pluginExample;
	}

	public void refreshSettingsDialogContent() {
		// TODO Auto-generated method stub
		
	}

	public SimulationRunInterface generateAndReturnSimulationRunFileInDialog() {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateSimulationRunFile(SimulationRunInterface simulationRunFile) {
		// TODO Auto-generated method stub
		
	}

}
