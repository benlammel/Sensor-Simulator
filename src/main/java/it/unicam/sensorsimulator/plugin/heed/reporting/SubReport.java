package it.unicam.sensorsimulator.plugin.heed.reporting;

import java.util.HashMap;

import it.unicam.sensorsimulator.plugin.heed.reporting.graphic.NetworkGraphic;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class SubReport extends TitledPane {
	
	private VBox subReportGraphic;
	private RunResults runResults;
	
	private final int pictureWidth = 600;
	private final int pictureHeight = 400;
	private HashMap<Integer, NetworkGraphic> evolutionPictures;
	
	private Spinner<Integer> spinner;
	
	private ScrollPane scoller;
//	private HashMap<Integer, WritableImage> evolutionPictures2;

	public SubReport(RunResults runResults) {
		this.runResults = runResults;
		this.setText("Run No: " +runResults.getRunID());
		scoller = new ScrollPane();
		subReportGraphic = new VBox();
		calculateAndAddNetworkEvolution();
		scoller.setContent(subReportGraphic);
		this.setContent(scoller);
		
		
//		runResults.getAgentStatisticList();
//		runResults.getClusterHeadList();
//		runResults.getCoordinatorStatistics();
//		runResults.getNetworkEvolutionList();
	}

	private void calculateAndAddNetworkEvolution() {
		BorderPane networkEvolutionLayout = new BorderPane();
		
		evolutionPictures = new HashMap<Integer, NetworkGraphic>();
//		evolutionPictures2 = new HashMap<Integer, WritableImage>();
		
		for(int i = 0; i<runResults.getNetworkEvolutionList().size(); i++){
			evolutionPictures.put(i, new NetworkGraphic(pictureWidth, pictureHeight, runResults.getAgentStatisticList().keySet(), runResults.getNetworkEvolutionList(), i));
			
//			NetworkGraphic test = new NetworkGraphic(pictureWidth, pictureHeight, runResults.getAgentStatisticList().keySet(), runResults.getNetworkEvolutionList(), i);
//			WritableImage a = test.snapshot(new SnapshotParameters(), null);
//			evolutionPictures2.put(i, a);

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
				
//				if(evolutionPictures2.containsKey(intValue)){
//					networkEvolutionLayout.setCenter(new ImageView(evolutionPictures2.get(intValue)));
//				}
			}
          });
		spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, evolutionPictures.size()-1));
//		spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, evolutionPictures2.size()-1));
		
		networkEvolutionLayout.setCenter(evolutionPictures.get(0));
//		networkEvolutionLayout.setCenter(new ImageView(evolutionPictures2.get(0)));
		networkEvolutionLayout.setBottom(spinner);
		subReportGraphic.getChildren().add(networkEvolutionLayout);
	}

}
