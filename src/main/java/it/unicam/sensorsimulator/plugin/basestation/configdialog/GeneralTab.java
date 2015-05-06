package it.unicam.sensorsimulator.plugin.basestation.configdialog;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class GeneralTab extends Tab {

	private BaseStationConfigDialog baseStationConfigDialog;
	
	private CheckBox ckMASObservation;
	private CheckBox ckSniffer;
	private CheckBox ckInspector;
	
	/* Run */
	private Slider sliderNoOfRuns;
	private Text txtNoOfRuns;
	private IntegerProperty sliderNoOfRunsValue = new SimpleIntegerProperty(1);
	
	/* Cluster */
	private Slider sliderNoOfClusters;
	private Text txtNoOfClusters;
	private IntegerProperty sliderNoOfClustersValue = new SimpleIntegerProperty(1);

	public GeneralTab(BaseStationConfigDialog baseStationConfigDialog) {
		this.baseStationConfigDialog = baseStationConfigDialog;
		this.setText("General");
		this.setClosable(false);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		grid.add(new Label("Number of runs: "), 0, 0);
		grid.add(setupNoOfRuns(), 1, 0);
		grid.add(txtNoOfRuns, 2, 0);

		ckMASObservation = new CheckBox("Start MultiAgent Observation UI");
		ckMASObservation.setSelected(false);
		grid.add(ckMASObservation, 0, 1);

		ckSniffer = new CheckBox("Start Sniffer");
		ckSniffer.setSelected(false);
		grid.add(ckSniffer, 0, 2);

		ckInspector = new CheckBox("Start Inspector");
		ckInspector.setSelected(false);
		grid.add(ckInspector, 0, 3);

		grid.add(new Label("Number of clusters: "), 0, 4);
		grid.add(setupNoOfClusters(), 1, 4);
		grid.add(txtNoOfClusters, 2, 4);
		
		this.setContent(grid);
	}
	
	private Node setupNoOfClusters() {
		sliderNoOfClusters = new Slider(1, 1, baseStationConfigDialog.getNumberOfClusters());
		sliderNoOfClusters.setShowTickLabels(true);
		sliderNoOfClusters.setShowTickMarks(true);
		sliderNoOfClusters.setMajorTickUnit(1);

		txtNoOfClusters = new Text("1");
		sliderNoOfClusters.valueProperty().bindBidirectional(
				sliderNoOfClustersValue);
		txtNoOfClusters.textProperty().bind(sliderNoOfClustersValue.asString());

		return sliderNoOfClusters;
	}

	private Slider setupNoOfRuns() {
		sliderNoOfRuns = new Slider(1, 10, baseStationConfigDialog.getNumberOfRuns());
		sliderNoOfRuns.setShowTickLabels(true);
		sliderNoOfRuns.setShowTickMarks(true);
		sliderNoOfRuns.setMajorTickUnit(2);

		txtNoOfRuns = new Text(Integer.toString(baseStationConfigDialog.getNumberOfRuns()));
		sliderNoOfRuns.valueProperty().bindBidirectional(sliderNoOfRunsValue);
		txtNoOfRuns.textProperty().bind(sliderNoOfRunsValue.asString());

		return sliderNoOfRuns;
	}

	public int getNumberOfRuns() {
		return (int)sliderNoOfRuns.getValue();
	}

	public void refreshContent() {
		System.out.println("************ " +baseStationConfigDialog.getNumberOfAgents());
		sliderNoOfClusters.setMax(baseStationConfigDialog.getNumberOfAgents());
	}

}
