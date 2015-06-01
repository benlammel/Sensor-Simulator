package it.unicam.sensorsimulator.plugin.heedv2.reporting.gui;

import java.util.HashMap;
import java.util.Map.Entry;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
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

	public Heedv2RunReportItem() {
		
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
	
	public Heedv2RunReportItem(Heedv2RunReport run) {
		this();
		
		sumAgentOverallReceived(run);
		sumAgentOverallSent(run);
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

	private void sumAgentOverallSent(Heedv2RunReport run) {
		for(HeedAgentStatistic agentStatistics : run.getAgentStatistics()){
			for(MessageCounter sentStat : agentStatistics.getSentCounter()){
				if(sentStatsisticsOverall.containsKey(sentStat.getMessageIDString())){
					int value = sentStatsisticsOverall.get(sentStat.getMessageIDString());
					sentStatsisticsOverall.put(sentStat.getMessageIDString(), value + sentStat.getMessageCounter());
				}else{
					sentStatsisticsOverall.put(sentStat.getMessageIDString(), sentStat.getMessageCounter());
				}
			}
		}
	}

	private void sumAgentOverallReceived(Heedv2RunReport run) {
		for(HeedAgentStatistic agentStatistics : run.getAgentStatistics()){
			for(MessageCounter receivedStat : agentStatistics.getReceivedCounter()){
				if(receivedStatsisticsOverall.containsKey(receivedStat.getMessageIDString())){
					int value = receivedStatsisticsOverall.get(receivedStat.getMessageIDString());
					receivedStatsisticsOverall.put(receivedStat.getMessageIDString(), value +receivedStat.getMessageCounter());
				}else{
					receivedStatsisticsOverall.put(receivedStat.getMessageIDString(), receivedStat.getMessageCounter());
				}
			}
		}
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
