package it.unicam.sensorsimulator.plugin.example.reporting.report;

import javax.xml.bind.annotation.XmlAttribute;

public class MessageCounter {
	
	private int counter;
	private String idString;

	public MessageCounter(String key, int value) {
		setMessageIDString(key);
		setMessageCounter(value);
	}

	private void setMessageCounter(int value) {
		this.counter = value;
	}
	
	@XmlAttribute
	private int getMessageCounter() {
		return counter;
	}

	private void setMessageIDString(String key) {
		this.idString = key;
	}
	
	@XmlAttribute
	private String getMessageIDString() {
		return idString;
	}
}
