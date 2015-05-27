package it.unicam.sensorsimulator.plugin.heedv2.agent.config;

import javax.xml.bind.annotation.XmlAttribute;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;

public class HeedAgentConfiguration implements GeneralAgentInterface {

	private int agentID;
	private double agentRadius;
	private double locationX;
	private double locationY;
	
	
	public HeedAgentConfiguration() {
		
	}
	
	public HeedAgentConfiguration(int agentID, double agentRadius,
			double locationX, double locationY) {
		setAgentID(agentID);
		setAgentRadius(agentRadius);
		setLocationX(locationX);
		setLocationY(locationY);
	}

	@Override
	public void setAgentID(int id) {
		this.agentID = id;
	}

	@XmlAttribute
	@Override
	public int getAgentID() {
		return agentID;
	}

	@Override
	public void setAgentRadius(double radius) {
		this.agentRadius = radius;
	}

	@XmlAttribute
	@Override
	public double getAgentRadius() {
		return agentRadius;
	}

	@Override
	public void setLocationX(double locationX) {
		this.locationX = locationX;
	}

	@XmlAttribute
	@Override
	public double getLocationX() {
		return locationX;
	}

	@Override
	public void setLocationY(double locationY) {
		this.locationY = locationY;
	}

	@XmlAttribute
	@Override
	public double getLocationY() {
		return locationY;
	}
}
