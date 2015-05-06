package it.unicam.sensorsimulator.ui.agent;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class InnerCircle extends Circle {

	public InnerCircle(double radius, Color color) {
		setColor(color);
		setRadius(radius);
		setStrokeWidth(3);
		setStrokeType(StrokeType.INSIDE);
		setVisible(true);
	}

	public void setColor(Color color) {
		setFill(color.deriveColor(1, 1, 1, 0.5));
		setStroke(color);
	}

}
