package it.unicam.sensorsimulator.plugin.heed.reporting;

import java.util.HashMap;
import java.util.Set;

import it.unicam.sensorsimulator.plugin.basestation.BaseStationPlugin;
import it.unicam.sensorsimulator.plugin.basestation.reporting.graphic.NetworkGraphic;
import it.unicam.sensorsimulator.plugin.heed.HeedPlugin;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ReportingModule extends BorderPane {
	
	private HeedPlugin heedPlugin;
	private ScrollPane spLeft;
	private ScrollPane spCenter;
	
	private ReportMenu reportMenu;
	private ReportMainContent mainContent;
	
	public ReportingModule(HeedPlugin heedPlugin){
		this.heedPlugin = heedPlugin;
		this.mainContent = new ReportMainContent();
		spLeft = new ScrollPane();
		spCenter = new ScrollPane();
		
		this.setCenter(spCenter);
		reportMenu = new ReportMenu(this);
		
//		spLeft.setContent(reportMenu);
		spCenter.setContent(mainContent);
		
	}

	public void publish(HashMap<Integer, Set<Integer>> measurement1) {
//		mainContent.getChildren().add(new NetworkGraphic(measurement1))
		mainContent.getChildren().add(new NetworkGraphic(measurement1));
	}

}
