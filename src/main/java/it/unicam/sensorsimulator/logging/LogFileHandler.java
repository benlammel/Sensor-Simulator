package it.unicam.sensorsimulator.logging;

import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class LogFileHandler implements LogFileWriterInterface {
	
	private static Logger guiLogger;
	private static Logger communicationLogger;
 
	private static LogFileHandler INSTANCE = null;
	
	public static LogFileHandler getInstance() {
		if (INSTANCE == null) {
			synchronized (LogFileHandler.class) {
				if (INSTANCE == null) {
					INSTANCE = new LogFileHandler();
				}
			}
		}
		return INSTANCE;
	}
	
	public LogFileHandler() {
		guiLogger = LogManager.getLogger("it.unicam.sensorsimulator.logging.GUI");
		communicationLogger = LogManager.getLogger("it.unicam.sensorsimulator.logging.Communication");
	}

	public void trace(Class cls, String message) {
		guiLogger.trace(cls.getName() +";" +message);
	}
	
	public void debug(Class cls, String message) {
		guiLogger.debug(cls.getName() +";" +message);
	}

	public void error(Class cls, String text) {
		guiLogger.error(cls.getName() +";" +text);
	}

	public void catching(Exception printStackTrace) {
		guiLogger.catching(printStackTrace);
	}

	private void logCommunication(LogLevels level, String message) {
		switch(level){
		case DEBUG:
			communicationLogger.debug(message);
			break;
		case ERROR:
			communicationLogger.error(message);
			break;
		case INFO:
			communicationLogger.info(message);
			break;
		case WARN:
			communicationLogger.warn(message);
			break;
		}
	}

	@Override
	public void logCoordinatorAction(LogLevels level, String message) {
		logCommunication(level, message);
	}

	@Override
	public void logCoordinatorMessage(int AgentID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logAgentAction(LogLevels level, String message) {
		logCommunication(level, message);
	}

	@Override
	public void logAgentMessageReceived(String agentID, String messageType, String receivers) {
		communicationLogger.info("agent ({}) received a {} message from agent ID: {} ", agentID, messageType, receivers);
	}
	
	@Override
	public void logAgentMessageSent(String agentID, String messageType, String sender) {
		communicationLogger.info("agent ({}) sent a {} message to agent IDs: {} ", agentID, messageType, sender);
	}

}


