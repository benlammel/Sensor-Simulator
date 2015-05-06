package it.unicam.sensorsimulator.interfaces;

public interface LogFileWriterInterface {
	
	public enum LogLevels{
		ERROR,
		WARN,
		INFO,
		DEBUG;
	}
	
	public void logAgentAction(LogLevels level, String message);
	public void logAgentMessageReceived(String agentID, String messageType, String receiver);
	public void logAgentMessageSent(String agentID, String messageType, String sender);

	
	public void logCoordinatorAction(LogLevels level, String message);
	public void logCoordinatorMessage(int AgentID);
	
	

	public void trace(Class<?> className, String message);

	public void debug(Class<?> className, String message);

	public void catching(Exception e);
	
	

}
