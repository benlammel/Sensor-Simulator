package it.unicam.sensorsimulator.plugin.example.agent.config;

import javax.xml.bind.annotation.XmlAttribute;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;

public class ExampleAgentConfiguration implements GeneralAgentInterface {

	private int agentID;
	private double radius;
	private double locationX;
	private double locationY;

	public ExampleAgentConfiguration(int agentID, double agentRadius,
			double locationX, double locationY) {
		setAgentID(agentID);
		setAgentRadius(agentRadius);
		setLocationX(locationX);
		setLocationY(locationY);
	}

	@XmlAttribute
	@Override
	public int getAgentID() {
		return agentID;
	}

	@Override
	public void setAgentID(int id) {
		this.agentID = id;
	}

	@XmlAttribute
	@Override
	public double getAgentRadius() {
		return radius;
	}

	@Override
	public void setAgentRadius(double radius) {
		this.radius = radius;
	}

	@XmlAttribute
	@Override
	public double getLocationX() {
		return locationX;
	}

	@Override
	public void setLocationX(double locationX) {
		this.locationX = locationX;
	}

	@XmlAttribute
	@Override
	public double getLocationY() {
		return locationY;
	}

	@Override
	public void setLocationY(double locationY) {
		this.locationY = locationY;
	}
}
