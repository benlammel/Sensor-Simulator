package it.unicam.sensorsimulator.plugin.heedv2.reporting.report;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;

public class MessageCounter implements Serializable {

	private int counter;
	private String idString;

	public MessageCounter(){
	}
	
	public MessageCounter(String key, int value) {
		setMessageIDString(key);
		setMessageCounter(value);
	}

	public void setMessageCounter(int value) {
		this.counter = value;
	}
	
	@XmlAttribute
	public int getMessageCounter() {
		return counter;
	}

	public void setMessageIDString(String key) {
		this.idString = key;
	}
	
	@XmlAttribute
	public String getMessageIDString() {
		return idString;
	}

}
