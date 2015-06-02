package it.unicam.sensorsimulator.plugin.heedv2.reporting.gui;

import it.unicam.sensorsimulator.plugin.heedv2.reporting.report.Heedv2RunReport;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;

public class Heedv2RunTab extends Tab {

	private Heedv2RunReport run;
	private BorderPane layout;
	private ScrollPane sp;
	private Heedv2GUIReport heedv2guiReport;

	public Heedv2RunTab(Heedv2GUIReport heedv2guiReport, Heedv2RunReport run) {
		this.heedv2guiReport = heedv2guiReport;
		this.run = run;
		this.setClosable(false);
		this.setText("Run #" +run.getRunNumber());
		
		layout = new BorderPane();
		layout.setCenter(new Heedv2RunReportItem(heedv2guiReport, run));
		sp = new ScrollPane();
		sp.setPrefWidth(800);
		sp.setContent(layout);
		sp.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		setContent(sp);
	}
}
