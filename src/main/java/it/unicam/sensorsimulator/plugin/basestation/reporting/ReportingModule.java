package it.unicam.sensorsimulator.plugin.basestation.reporting;

import java.util.HashMap;
import java.util.Set;

import it.unicam.sensorsimulator.interfaces.AbstractReportPane;
import it.unicam.sensorsimulator.interfaces.ReportInterface;
import it.unicam.sensorsimulator.plugin.basestation.BaseStationPlugin;
import it.unicam.sensorsimulator.plugin.basestation.reporting.graphic.NetworkGraphic;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ReportingModule extends AbstractReportPane {
	
	private ScrollPane spLeft;
	private ScrollPane spCenter;
	
	private ReportMenu reportMenu;
	private ReportMainContent mainContent;
	
	public ReportingModule(){
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

	@Override
	public void addReport(ReportInterface report) {
		// TODO Auto-generated method stub
		
	}

}
