package it.unicam.sensorsimulator.plugin.heed.reporting;

import java.util.HashMap;

import it.unicam.sensorsimulator.plugin.heed.reporting.graphic.NetworkGraphic;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class SubReport extends TitledPane {
	
	private VBox subReportGraphic;
	private RunResults runResults;
	
	private final int pictureWidth = 600;
	private final int pictureHeight = 400;
	private HashMap<Integer, NetworkGraphic> evolutionPictures;
	
	private Spinner<Integer> spinner;

	public SubReport(RunResults runResults) {
		this.runResults = runResults;
		this.setText("Run No: " +runResults.getRunID());
		subReportGraphic = new VBox();
		calculateAndAddNetworkEvolution();
		this.setContent(subReportGraphic);
		
		
//		runResults.getAgentStatisticList();
//		runResults.getClusterHeadList();
//		runResults.getCoordinatorStatistics();
//		runResults.getNetworkEvolutionList();
	}

	private void calculateAndAddNetworkEvolution() {
		BorderPane networkEvolutionLayout = new BorderPane();
		
		evolutionPictures = new HashMap<Integer, NetworkGraphic>();
		
		for(int i = 0; i<runResults.getNetworkEvolutionList().size(); i++){
			evolutionPictures.put(i, new NetworkGraphic(pictureWidth, pictureHeight, runResults.getAgentStatisticList().keySet(), runResults.getNetworkEvolutionList(), i));
		}
		
		spinner = new Spinner<Integer>();
		spinner.setEditable(false);
		spinner.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				displayPicture(newValue.intValue());
			}

			private void displayPicture(int intValue) {
				if(evolutionPictures.containsKey(intValue)){
					networkEvolutionLayout.setCenter(evolutionPictures.get(intValue));
				}
			}
          });
		spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, evolutionPictures.size()-1));
		
		networkEvolutionLayout.setCenter(evolutionPictures.get(0));
		networkEvolutionLayout.setBottom(spinner);
		subReportGraphic.getChildren().add(networkEvolutionLayout);
	}

}
