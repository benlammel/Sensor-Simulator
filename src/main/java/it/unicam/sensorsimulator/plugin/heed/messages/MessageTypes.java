package it.unicam.sensorsimulator.plugin.heed.messages;

public class MessageTypes {
	
	public enum MessageHandling{
		INCREASE, DECREASE;
	}
	
	public static final String SIMULATION_CONTROLS_START_INIZIALIZATION = "SIMULATION_CONTROLS_START_INIZIALIZATION";
	public static final String COST_BROADCAST = "COST_BROADCAST";
	public static final String CLUSTER_HEAD_MESSAGE = "CLUSTER_HEAD_MESSAGE";
	public static final String JOIN_CLUSTER = "JOIN_CLUSTER";
	public static final String MEASUREMENT_CLUSTERFORMING_PROTOCOL = "MEASUREMENT_CLUSTERFORMING_PROTOCOL";
	public static final String MEASUREMENT_PROTOCOL_BECAME_CH = "MEASUREMENT_PROTOCOL_BECAME_CH";
	public static final String MEASUREMENT_CLUSTER_FORMING_END = "MEASUREMENT_CLUSTER_FORMING_END";
	public static final String SIMULATION_CONTROLS_END = "SIMULATION_CONTROLS_END";
	public static final String MEASUREMENT_AGENT_STATISTICS = "MEASUREMENT_AGENT_STATISTICS";
	
}
