package it.unicam.sensorsimulator.plugin.heed.reporting;

import it.unicam.sensorsimulator.plugin.heed.reporting.graphic.NetworkGraphic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class ProtocolReportHandler extends TitledPane {
	
	private HashMap<Integer, ArrayList<Integer>> networkView;
	private ArrayList<NetworkGraphic> networkEvolutionSteps;
	
	private final int pictureWidth = 600;
	private final int pictureHeight = 400;
	
	private int taskID = 0;
	
	private BorderPane layout;
	private Spinner<Integer> spinner;
		
	public ProtocolReportHandler(){
		this.setText("Protocol");
		networkView =  new HashMap<Integer, ArrayList<Integer>>();
		networkEvolutionSteps = new ArrayList<NetworkGraphic>();
		
		layout = new BorderPane();
		layout.setCenter(new Text("noting to show yet"));
		
		spinner = new Spinner<Integer>();
        spinner.setEditable(false);
        spinner.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				displayPicture(newValue.intValue());
			}
            });
        updateSpinner();
		layout.setBottom(spinner);

//		ArrayList<Integer> a  = new ArrayList();
//		a.add(2);
//		a.add(3);
//		a.add(4);
//		a.add(5);
////		networkView.put(1, a);
//		
//		
//		ArrayList<Integer> b  = new ArrayList();
//		b.add(20);
//		b.add(30);
//		b.add(40);
//		b.add(50);
////		networkView.put(10, b);
//		
//		updateNetworkView(1, a);
//		updateNetworkView(10, b);
		
		this.setContent(layout);
	}
	
	private void displayPicture(int newValue) {
		layout.setCenter(networkEvolutionSteps.get(newValue));
	}

	private void updateSpinner() {
		if(networkEvolutionSteps.isEmpty()){
			spinner.setDisable(true);
		}else{
			spinner.setDisable(false);
			spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, networkEvolutionSteps.size()-1));
		}
	}

	public void updateNetworkView(int clusterHead,
			ArrayList<Integer> clusterMembers) {
		networkView.put(clusterHead, clusterMembers);
		System.out.println(networkView);
		generateAndUpdateEvolutionPicture();
	}

	private void generateAndUpdateEvolutionPicture() {
		
		GraphicGenerationTask task = new GraphicGenerationTask(taskID++, pictureWidth, pictureHeight, networkView);
		task.run();
		try {
			networkEvolutionSteps.add(task.get());
			updateSpinner();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
