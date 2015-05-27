package it.unicam.sensorsimulator.ui.agent;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import it.unicam.sensorsimulator.logging.LogFileHandler;
import it.unicam.sensorsimulator.ui.SimulationEnvironmentMode;
import it.unicam.sensorsimulator.ui.modelling.Modeller;

public class GUIAgent extends StackPane implements GUIAgentInterface {

	private int agentID = -1;
	private double radius;
	private double locationX;
	private double locationY;
	private Text agentText;
	private InnerCircle innerCircle;

	private double sceneMousePositionX, sceneMousePositionY;
	private double objectX;
	private double objectY;
	private LogFileHandler log;
	private Modeller modeller;

	public GUIAgent(int agentID, Modeller modeller, double agentRadius, double locationX,
			double locationY) {
		this(agentID, modeller);
		setAgentRadius(agentRadius);
		relocateByCenterCoordinations(locationX, locationY);
		log.trace(GUIAgent.class, "agent properties changed;" + getLogMessage());
	}

	public GUIAgent(int agentID, Modeller modeller) {
		this.modeller = modeller;
		log = LogFileHandler.getInstance();
		initElements();
		setAgentID(agentID);
		setAgentRadius(20);
		relocateByCenterCoordinations(100, 100);

		//this.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5);");
		getChildren().addAll(innerCircle, agentText);
		log.trace(GUIAgent.class, "agent created;" + getLogMessage());
	}

	private void initElements() {
		agentText = new Text("not set");
		agentText.setTextAlignment(TextAlignment.CENTER);
		innerCircle = new InnerCircle(10, Color.BLACK);
		relocate(100, 100);
		this.setOnMouseDragged(onMouseDraggedEventHandler);
		this.setOnMousePressed(onMousePressedEventHandler);
		this.setOnMouseReleased(onMouseReleasedEventHandler);
	}

	@Override
	public void setAgentID(int id) {
		this.agentID = id;
		agentText.setText(Integer.toString(agentID));
	}

	@Override
	public int getAgentID() {
		return agentID;
	}

	@Override
	public void setAgentRadius(double radius) {
		this.radius = radius;
		innerCircle.setRadius(radius);
	}

	@Override
	public double getAgentRadius() {
		return radius;
	}

	@Override
	public void setLocationX(double locationX) {
		this.locationX = locationX;
	}

	@Override
	public double getLocationX() {
		return locationX;
	}

	@Override
	public void setLocationY(double locationY) {
		this.locationY = locationY;
	}

	@Override
	public double getLocationY() {
		return locationY;
	}

	private void relocateByLeftUpperCornerVals(double x, double y) {
		relocate(x, y);
		setLocationX(x + getAgentRadius());
		setLocationY(y + getAgentRadius());
	}

	private void relocateByCenterCoordinations(double x, double y) {
		setLocationX(x);
		setLocationY(y);
		relocate(x - getAgentRadius(), y - getAgentRadius());
	}

	private void setMousePositions(double x, double y) {
		sceneMousePositionX = x;
		sceneMousePositionY = y;
	}

	public StringBuilder getLogMessage() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getName());
		builder.append(";ID;");
		builder.append(getAgentID());
		builder.append(";RADIUS;");
		builder.append(radius);
		builder.append(";XCENTER;");
		builder.append(getLocationX());
		builder.append(";YCENTER;");
		builder.append(getLocationY());
		builder.append(";HEIGHT;");
		builder.append(getHeight());
		builder.append(";WIDTH;");
		builder.append(getWidth());
		builder.append(";LAYOUTX;");
		builder.append(getLayoutX());
		builder.append(";LAYOUTY;");
		builder.append(getLayoutY());
		return builder;
	}

	EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent t) {
			toFront();

			setMousePositions(t.getSceneX(), t.getSceneY());

			objectX = getLayoutX();
			objectY = getLayoutY();
		}
	};

	EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent t) {
			if (!modeller.getSimulationEnvironmentMode().equals(
					SimulationEnvironmentMode.SIMULATION_IN_PROGRESS)) {
				double offsetX = t.getSceneX() - sceneMousePositionX;
				double offsetY = t.getSceneY() - sceneMousePositionY;

				objectX += offsetX;
				objectY += offsetY;

				relocateByLeftUpperCornerVals(objectX, objectY);

				setMousePositions(t.getSceneX(), t.getSceneY());

				t.consume();
			}
		}
	};

	EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent t) {
			if(t.getButton().equals(MouseButton.PRIMARY))
				log.trace(GUIAgent.class, "agent change;" + getLogMessage());
		}
	};
}
