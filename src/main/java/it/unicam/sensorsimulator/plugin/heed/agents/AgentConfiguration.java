package it.unicam.sensorsimulator.plugin.heed.agents;

import java.io.Serializable;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;

import javax.xml.bind.annotation.XmlAttribute;

public class AgentConfiguration implements GeneralAgentInterface, Serializable {

	private int agentID;
	private double agentRadius;
	private double locationX;
	private double locationY;
	
	private double eMax;
	private double eResidential;
	private double pMin;
	
	public AgentConfiguration() {
	}

	public AgentConfiguration(int agentID, double agentRadius,
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

	@XmlAttribute
	public double geteMax() {
		return eMax;
	}

	public void seteMax(double eMax) {
		this.eMax = eMax;
	}

	@XmlAttribute
	public double geteResidential() {
		return eResidential;
	}

	public void seteResidential(double eResidential) {
		this.eResidential = eResidential;
	}

	@XmlAttribute
	public double getpMin() {
		return pMin;
	}

	public void setpMin(double pMin) {
		this.pMin = pMin;
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("agent config info; agent id;");
		builder.append(getAgentID());
		builder.append(";agentRadius;");
		builder.append(getAgentRadius());
		builder.append(";locationX;");
		builder.append(getLocationX());
		builder.append(";locationY;");
		builder.append(getLocationY());
		builder.append(";e max;");
		builder.append(geteMax());
		builder.append(";e residential;");
		builder.append(geteResidential());
		builder.append(";p min;");
		builder.append(getpMin());
		return builder.toString();
	}
}