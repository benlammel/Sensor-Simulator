package it.unicam.sensorsimulator.plugin.heed.agents;

import java.io.Serializable;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;

import javax.xml.bind.annotation.XmlAttribute;

public class AgentConfiguration implements GeneralAgentInterface, Serializable {

	private int agentID;
	private double agentRadius;
	private double locationX;
	private double locationY;
	
	public AgentConfiguration() {
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