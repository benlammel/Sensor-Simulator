package it.unicam.sensorsimulator.plugin.heedv2.reporting.gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.report.HeedAgentStatistic;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.report.Heedv2RunReport;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.report.MessageCounter;

public class Heedv2RunReportItem extends ScrollPane {
	
	private VBox layout;
	private String name;
	private HashMap<String, Integer> receivedStatsisticsOverall;
	private HashMap<String, Integer> sentStatsisticsOverall;
	private HashMap<String, Integer> receivedStatsisticsCoordinator;
	private HashMap<String, Integer> sentStatsisticsCoordinator;
	
	private BarChart<Number,String> agentBarChart;
	private BarChart<Number,String> coordinatorBarChart;
	private CategoryAxis agentBarChartYAxis;
	private NumberAxis agentBarChartXAxis;
	private CategoryAxis coordinatorBarChartYAxis;
	private NumberAxis coordinatorBarChartXAxis;
	private Heedv2GUIReport heedv2guiReport;
	private HashMap<Integer, ArrayList<Integer>> networkPicture;

	public Heedv2RunReportItem() {
		
		networkPicture = new HashMap<Integer, ArrayList<Integer>>();
		
		receivedStatsisticsOverall = new HashMap<String, Integer>();
		sentStatsisticsOverall = new HashMap<String, Integer>();
		
		receivedStatsisticsCoordinator = new HashMap<String, Integer>();
		sentStatsisticsCoordinator = new HashMap<String, Integer>();
		
		layout = new VBox();
		
		agentBarChartYAxis = new CategoryAxis();
		agentBarChartYAxis.setLabel("Message Type");
		agentBarChartXAxis = new NumberAxis();
		agentBarChartXAxis.setLabel("Number of Messages");
		agentBarChart = new BarChart<Number,String>(agentBarChartXAxis,agentBarChartYAxis);
		agentBarChart.setTitle("Agent Message Overview");
		
		coordinatorBarChartYAxis = new CategoryAxis();
		coordinatorBarChartYAxis.setLabel("Message Type");
		coordinatorBarChartXAxis = new NumberAxis();
		coordinatorBarChartXAxis.setLabel("Number of Messages");
		coordinatorBarChart = new BarChart<Number,String>(coordinatorBarChartXAxis,coordinatorBarChartYAxis);
		coordinatorBarChart.setTitle("Coordinator Message Overview");
	}
	
	public Heedv2RunReportItem(Heedv2GUIReport heedv2guiReport, Heedv2RunReport run) {
		this();
		this.heedv2guiReport = heedv2guiReport;
		
		layout.getChildren().add(createTimeMeasurement(run.getStartTime(), run.getStopTime()));
		
		retrieveAgentData(run);
		
		createOverlayNetworkGraphic(run.getClusterHeadList(), run.getSuccessorList());
		layout.getChildren().add(createTextListing(run.getClusterHeadList()));
		
		createAgentReceivedChart();
		createAgentSentChart();
		
		sumCoordinatorOverallReceived(run);
		sumCoordinatorOverallSent(run);
		createCoordinatorReceivedChart();
		createCoordinatorSentChart();
		
		layout.getChildren().add(agentBarChart);
		layout.getChildren().add(coordinatorBarChart);
		this.setContent(layout);
	}

	private GridPane createTextListing(ArrayList<Integer> clusterHeads) {
		GridPane details = new GridPane();
		
		details.setHgap(10);
		details.setVgap(10);
		details.setPadding(new Insets(20, 150, 10, 10));

		details.add(new Label("Clusters:"), 0, 0);
		
		Collections.sort(clusterHeads);
		
		int row = 1;
		int col = 0;
		for(Integer cluster : clusterHeads){
			
			if(networkPicture.containsKey(cluster)){
				details.add(new Label("ID " +cluster +" :: " +networkPicture.get(cluster)), col++, row);
			}else{
				details.add(new Label("ID " +cluster +" :: no successors"), col++, row);
			}
			
			if(col==3){
				row++;
				col = 0;
			}
		}
		
		return details;
	}

	private GridPane createTimeMeasurement(long start, long stop) {
		GridPane details = new GridPane();
		
		details.setHgap(10);
		details.setVgap(10);
		details.setPadding(new Insets(20, 150, 10, 10));

		details.add(new Label("start (HH:mm:ss:SSS):"), 0, 0);
		details.add(new Label(new SimpleDateFormat("HH:mm:ss:SSS").format(new Date(start))), 1, 0);
		
		details.add(new Label("end (HH:mm:ss:SSS):"), 0, 1);
		details.add(new Label(new SimpleDateFormat("HH:mm:ss:SSS").format(new Date(stop))), 1, 1);
		
		String duration = String.format("%d min, %d sec, %d ms", 
			    TimeUnit.MILLISECONDS.toMinutes(stop-start),
			    TimeUnit.MILLISECONDS.toSeconds(stop-start) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(stop-start)),
			    (stop-start)%1000);
		
		details.add(new Label("duration:"), 0, 2);
		details.add(new Label(duration), 1, 2);

		return details;
	}

	private void retrieveAgentData(Heedv2RunReport run) {
		for(HeedAgentStatistic agentStatistics : run.getAgentStatistics()){
			
			sumAgentStatisticsSent(agentStatistics.getSentCounter());
			sumAgentStatisticsReceived(agentStatistics.getReceivedCounter());
			
			processSuccessorList(agentStatistics.getAgentID(), agentStatistics.getMySuccessorList());
		}
	}

	private void processSuccessorList(int agentID,
			ArrayList<Integer> mySuccessorList) {
		if(!mySuccessorList.isEmpty()){
			networkPicture.put(agentID, mySuccessorList);
		}
	}

	private void sumAgentStatisticsReceived(
			ArrayList<MessageCounter> receivedCounter) {
		for(MessageCounter receivedStat : receivedCounter){
			if(receivedStatsisticsOverall.containsKey(receivedStat.getMessageIDString())){
				int value = receivedStatsisticsOverall.get(receivedStat.getMessageIDString());
				receivedStatsisticsOverall.put(receivedStat.getMessageIDString(), value +receivedStat.getMessageCounter());
			}else{
				receivedStatsisticsOverall.put(receivedStat.getMessageIDString(), receivedStat.getMessageCounter());
			}
		}
	}

	private void sumAgentStatisticsSent(ArrayList<MessageCounter> sentCounter) {
		for(MessageCounter sentStat : sentCounter){
			if(sentStatsisticsOverall.containsKey(sentStat.getMessageIDString())){
				int value = sentStatsisticsOverall.get(sentStat.getMessageIDString());
				sentStatsisticsOverall.put(sentStat.getMessageIDString(), value + sentStat.getMessageCounter());
			}else{
				sentStatsisticsOverall.put(sentStat.getMessageIDString(), sentStat.getMessageCounter());
			}
		}
	}

	private void createOverlayNetworkGraphic(ArrayList<Integer> clusterHeadList, ArrayList<Integer> successorList) {
//		layout.getChildren().add(new JungOverlayNWGraphic(clusterHeadList, successorList, networkPicture, heedv2guiReport.getWindowWidth(), heedv2guiReport.getWindowHeight()));
		
//		Platform.runLater(new Runnable() { 
//            public void run() {
//            	layout.getChildren().add(new GraphStreamOverlayNWGraphic(clusterHeadList, successorList, networkPicture, heedv2guiReport.getWindowWidth(), heedv2guiReport.getWindowHeight()));
//            }
//        });
		
    	layout.getChildren().add(new GraphStreamOverlayNWGraphic(clusterHeadList, successorList, networkPicture, heedv2guiReport.getWindowWidth(), heedv2guiReport.getWindowHeight()));

	}

	private void createAgentSentChart() {
		XYChart.Series series = new XYChart.Series();
		series.setName("sent");
        
		for(Entry<String, Integer> data : sentStatsisticsOverall.entrySet()){
			series.getData().add(new XYChart.Data(data.getValue(), data.getKey()));
		}
		agentBarChart.getData().add(series);
	}

	private void createAgentReceivedChart() {
		XYChart.Series series = new XYChart.Series();
		series.setName("received");
        
		for(Entry<String, Integer> data : receivedStatsisticsOverall.entrySet()){
			series.getData().add(new XYChart.Data(data.getValue(), data.getKey()));
		}
		agentBarChart.getData().add(series);
	}
	
	private void createCoordinatorSentChart() {
		XYChart.Series series = new XYChart.Series();
		series.setName("sent");
        
		for(Entry<String, Integer> data : sentStatsisticsCoordinator.entrySet()){
			series.getData().add(new XYChart.Data(data.getValue(), data.getKey()));
		}
		coordinatorBarChart.getData().add(series);
	}

	private void createCoordinatorReceivedChart() {
		XYChart.Series series = new XYChart.Series();
		series.setName("received");
        
		for(Entry<String, Integer> data : receivedStatsisticsCoordinator.entrySet()){
			series.getData().add(new XYChart.Data(data.getValue(), data.getKey()));
		}
		coordinatorBarChart.getData().add(series);
	}

	private void sumCoordinatorOverallSent(Heedv2RunReport run) {
		for(MessageCounter coordinatorStatistics : run.getCoordinatorStatistic().getSentCounter()){
			if(sentStatsisticsCoordinator.containsKey(coordinatorStatistics.getMessageIDString())){
				int value = sentStatsisticsCoordinator.get(coordinatorStatistics.getMessageIDString());
				sentStatsisticsCoordinator.put(coordinatorStatistics.getMessageIDString(), value + coordinatorStatistics.getMessageCounter());
			}else{
				sentStatsisticsCoordinator.put(coordinatorStatistics.getMessageIDString(), coordinatorStatistics.getMessageCounter());
			}
		}
	}

	private void sumCoordinatorOverallReceived(Heedv2RunReport run) {
		for(MessageCounter coordinatorStatistics : run.getCoordinatorStatistic().getReceivedCounter()){
			if(receivedStatsisticsCoordinator.containsKey(coordinatorStatistics.getMessageIDString())){
				int value = receivedStatsisticsCoordinator.get(coordinatorStatistics.getMessageIDString());
				receivedStatsisticsCoordinator.put(coordinatorStatistics.getMessageIDString(), value +coordinatorStatistics.getMessageCounter());
			}else{
				receivedStatsisticsCoordinator.put(coordinatorStatistics.getMessageIDString(), coordinatorStatistics.getMessageCounter());
			}
		}
	}
}
