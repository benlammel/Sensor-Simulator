package it.unicam.sensorsimulator.plugin.heed.messages;

public class MessageTypes {
	
	public enum MessageHandling{
		INCREASE, DECREASE;
	}
	
	public static final String START_INIZIALIZATION = "START_INIZIALIZATION";
	public static final String COST_BROADCAST = "COST_BROADCAST";
	public static final String CLUSTER_HEAD_MESSAGE = "CLUSTER_HEAD_MESSAGE";
	public static final String JOIN_CLUSTER = "JOIN_CLUSTER";
	public static final String M1 = "M1";
	
}
