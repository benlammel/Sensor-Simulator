package it.unicam.sensorsimulator.plugin.heed.reporting;

import it.unicam.sensorsimulator.plugin.heed.reporting.graphic.NetworkGraphic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;

public class ProtocolReportHandler extends TitledPane implements EventHandler<ActionEvent> {
	
	private HashMap<Integer, ClusterPicture> networkView;
	private Set<Integer> nodesList;
	private HashMap<Integer, NetworkGraphic> networkEvolutionSteps;
	
	private GeneratePicturesTask task;
	private ProgressBar progressBar;
	
	private final int pictureWidth = 600;
	private final int pictureHeight = 400;
	
	private ScrollPane scoller;
	
	private int taskID = 0;
	
	private BorderPane layout;
	private Spinner<Integer> spinner;
	private Button btnGenerateReport;
		
	public ProtocolReportHandler(){
		this.setText("Protocol");
		scoller = new ScrollPane();
		networkView =  new HashMap<Integer, ClusterPicture>();
//		networkEvolutionSteps = new HashMap<Integer, NetworkGraphic>();
		
		layout = new BorderPane();
		btnGenerateReport = new Button("generate graphical report");
		btnGenerateReport.setOnAction(this);
		layout.setCenter(btnGenerateReport);
		
		progressBar = new ProgressBar(0);
		layout.setBottom(progressBar);
		
//		spinner = new Spinner<Integer>();
//        spinner.setEditable(false);
//        spinner.valueProperty().addListener(new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> observable,
//					Number oldValue, Number newValue) {
//				displayPicture(newValue.intValue());
//			}
//            });
//        updateSpinner();
//		layout.setBottom(spinner);

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
		if(networkEvolutionSteps.containsKey(newValue)){
			layout.setCenter(networkEvolutionSteps.get(newValue));
		}
	}

//	private void updateSpinner() {
//		if(networkEvolutionSteps.isEmpty()){
//			spinner.setDisable(true);
//		}else{
//			spinner.setDisable(false);
//			spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, networkEvolutionSteps.size()-1));
//		}
//	}

	public void updateNetworkView(int clusterHead, ArrayList<Integer> clusterMembers) {
//		networkView.put(clusterHead, clusterMembers);
		networkView.put(taskID++, new ClusterPicture(clusterHead, clusterMembers));
//		generateAndUpdateEvolutionPicture();
	}

	private void generateAndUpdateEvolutionPicture() {
		
//		GraphicGenerationTask task = new GraphicGenerationTask(taskID++, pictureWidth, pictureHeight, nodesList, networkView);
//		task.run();
//		try {
//			networkEvolutionSteps.put(task.get().getTaskID(), task.get());
//			updateSpinner();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
	}

	public void setNodes(Set<Integer> keySet) {
		nodesList = keySet;
		System.out.println("reporting: nodes set:" +nodesList);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource()==btnGenerateReport){
			progressBar.setProgress(0);
			task = new GeneratePicturesTask(pictureWidth, pictureHeight, nodesList, networkView);
			progressBar.progressProperty().unbind();
	        progressBar.progressProperty().bind(task.progressProperty());
			new Thread(task).start();
			task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
			        new EventHandler<WorkerStateEvent>() {
			    @Override
			    public void handle(WorkerStateEvent t) {
			        displayEvolution(task.getValue());
			    }
			});
			
		}
	}

	private void displayEvolution(HashMap<Integer, NetworkGraphic> hashMap) {
		System.out.println("DONE " +hashMap.size());
		networkEvolutionSteps = hashMap;
		
		spinner = new Spinner<Integer>();
		spinner.setEditable(false);
		spinner.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				displayPicture(newValue.intValue());
			}
          });
		spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, networkEvolutionSteps.size()-1));
		layout.setBottom(spinner);
	}
}
