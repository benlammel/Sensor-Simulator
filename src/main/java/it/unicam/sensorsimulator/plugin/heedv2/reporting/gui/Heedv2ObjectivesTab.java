package it.unicam.sensorsimulator.plugin.heedv2.reporting.gui;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.plugin.heedv2.Heedv2SimulationRunFile;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class Heedv2ObjectivesTab extends Tab {

	private Heedv2SimulationRunFile simulationRunFile;
	private VBox layout;
	private ScrollPane sp;

	public Heedv2ObjectivesTab(Heedv2SimulationRunFile simulationRunFile) {
		this.simulationRunFile = simulationRunFile;
		this.layout = new VBox();
		this.setClosable(false);
		this.setText("Objectives");
		
		layout.getChildren().add(createDetails());
		layout.getChildren().add(createAgentLocationPicture());
		sp = new ScrollPane();
		sp.setContent(layout);
		sp.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		setContent(sp);
	}

	private GridPane createDetails() {
		GridPane details = new GridPane();
		
		details.setHgap(10);
		details.setVgap(10);
		details.setPadding(new Insets(20, 150, 10, 10));

		details.add(new Label("Number of runs: "), 0, 0);
		details.add(new Label(Integer.toString(simulationRunFile.getNumberOfRuns())), 1, 0);
		
		details.add(new Label("Number of agents: "), 0, 1);
		details.add(new Label(Integer.toString(simulationRunFile.getAgentList().size())), 1, 1);

		return details;
	}

	private Pane createAgentLocationPicture(){
		Pane pic = new Pane();
		pic.setStyle("-fx-border-color: red;");
		
		for(GeneralAgentInterface agent : simulationRunFile.getAgentList()){
			Circle circle = new Circle(agent.getAgentRadius(), Color.TOMATO);
			Text text = new Text(Integer.toString(agent.getAgentID()));
			text.setBoundsType(TextBoundsType.VISUAL); 
			StackPane stack = new StackPane();
			stack.setLayoutX(agent.getLocationX()-agent.getAgentRadius()/2);
			stack.setLayoutY(agent.getLocationY()-agent.getAgentRadius()/2);
			stack.getChildren().addAll(circle, text);
			
			pic.getChildren().add(stack);
		}
		return pic;
	}
}
