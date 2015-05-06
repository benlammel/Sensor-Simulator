package it.unicam.sensorsimulator.interfaces;

import java.util.ArrayList;

public interface SimulationEnvironmentServices {
	
	public ArrayList<GeneralAgentInterface> getAgentList();

	public LogFileWriterInterface getLogger();
	

}
