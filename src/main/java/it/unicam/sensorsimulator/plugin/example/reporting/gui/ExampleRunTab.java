package it.unicam.sensorsimulator.plugin.example.reporting.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import it.unicam.sensorsimulator.plugin.example.reporting.report.ExampleAgentStatistics;
import it.unicam.sensorsimulator.plugin.example.reporting.report.ExampleRunReport;
import it.unicam.sensorsimulator.plugin.heedv2.reporting.report.MessageCounter;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tab;

public class ExampleRunTab extends Tab {
	
	private BarChart<String,Number> bc;
	private CategoryAxis xAxis;
	private NumberAxis yAxis;
	
	private HashMap<String, Integer> receivedDataSets;
	private HashMap<String, Integer> sentDataSets;
	private ExampleRunReport run;
	private HashMap<String, Integer> coordinatorReceived;
	private HashMap<String, Integer> coordinatorSent;

	public ExampleRunTab(ExampleRunReport run) {
		this.run = run;
		this.setClosable(false);
		this.setText("Run #" +run.getRunNumber());
		this.setContent(createBarChart());
	}

	private BarChart<String, Number> createBarChart() {
		xAxis = new CategoryAxis();
		yAxis = new NumberAxis();
		bc = new BarChart<String,Number>(xAxis,yAxis);
		
        bc.setTitle("Message Overview");
        xAxis.setLabel("Message");       
        yAxis.setLabel("#");
        
        calculateReceivedDataSets();
        calculateSentDataSets();
        
        setDataReceivedSets();
        setDataSentSets();
        
        coordinatorReceivedAndSent();
        setDataCoordinatorSets();

        return bc;
	}
	
	private void setDataCoordinatorSets() {
		
		XYChart.Series series1 = new XYChart.Series();
		series1.setName("Coordinator Received");
        
		for(Entry<String, Integer> data : coordinatorReceived.entrySet()){
			series1.getData().add(new XYChart.Data(data.getKey(), data.getValue()));
		}
		bc.getData().add(series1);
		
		
		XYChart.Series series2 = new XYChart.Series();
		series2.setName("Coordinator Sent");
        
		for(Entry<String, Integer> data : coordinatorSent.entrySet()){
			series2.getData().add(new XYChart.Data(data.getKey(), data.getValue()));
		}
		bc.getData().add(series2);
		
	}

	private void coordinatorReceivedAndSent(){
		coordinatorReceived = new HashMap<String, Integer>();
		for(MessageCounter data : run.getCoordinatorStatsitics().getReceivedCounter()){
			if(coordinatorReceived.containsKey(data.getMessageIDString())){
				int value = coordinatorReceived.get(data.getMessageIDString());
				coordinatorReceived.put(data.getMessageIDString(), value+data.getMessageCounter());
			}else{
				coordinatorReceived.put(data.getMessageIDString(), data.getMessageCounter());
			}
		}
		
		coordinatorSent = new HashMap<String, Integer>();
		for(MessageCounter data : run.getCoordinatorStatsitics().getReceivedCounter()){
			if(coordinatorSent.containsKey(data.getMessageIDString())){
				int value = coordinatorSent.get(data.getMessageIDString());
				coordinatorSent.put(data.getMessageIDString(), value+data.getMessageCounter());
			}else{
				coordinatorSent.put(data.getMessageIDString(), data.getMessageCounter());
			}
		}
		
	}

	private void setDataSentSets() {
		XYChart.Series series = new XYChart.Series();
		series.setName("Agents Sent");
        
		for(Entry<String, Integer> data : sentDataSets.entrySet()){
			series.getData().add(new XYChart.Data(data.getKey(), data.getValue()));
		}
		bc.getData().add(series);
		
	}

	private void calculateSentDataSets() {
		sentDataSets = new HashMap<String, Integer>();		
		for(ExampleAgentStatistics data : run.getAgentStatistics()){
			for(MessageCounter sent : data.getSentCounter()){
				if(sentDataSets.containsKey(sent.getMessageIDString())){
					int value = sentDataSets.get(sent.getMessageIDString());
					sentDataSets.put(sent.getMessageIDString(), value+sent.getMessageCounter());
				}else{
					sentDataSets.put(sent.getMessageIDString(), sent.getMessageCounter());
				}
			}
		}
		
	}

	private void setDataReceivedSets() {
		XYChart.Series series = new XYChart.Series();
		series.setName("Agents Received");
        
		for(Entry<String, Integer> data : receivedDataSets.entrySet()){
			series.getData().add(new XYChart.Data(data.getKey(), data.getValue()));
		}
		bc.getData().add(series);
	}

	private void calculateReceivedDataSets() {
		receivedDataSets = new HashMap<String, Integer>();		
		for(ExampleAgentStatistics data : run.getAgentStatistics()){
			for(MessageCounter received : data.getReceivedCounter()){
				if(receivedDataSets.containsKey(received.getMessageIDString())){
					int value = receivedDataSets.get(received.getMessageIDString());
					receivedDataSets.put(received.getMessageIDString(), value+received.getMessageCounter());
				}else{
					receivedDataSets.put(received.getMessageIDString(), received.getMessageCounter());
				}
			}
		}
	}

}
