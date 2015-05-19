package it.unicam.sensorsimulator.plugin.example.reporting;

import javafx.scene.layout.BorderPane;
import it.unicam.sensorsimulator.plugin.example.PluginExample;

public class ExampleReportingModule extends BorderPane {
	
	private PluginExample pluginExample;

	public ExampleReportingModule(PluginExample pluginExample) {
		this.pluginExample = pluginExample;
	}

}
