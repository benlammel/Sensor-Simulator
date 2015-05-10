package it.unicam.sensorsimulator.interfaces;

import java.util.ArrayList;

public interface SimulationRunInterface {
	
	public boolean getStartMASObservationUI();
	public void setStartMASObservationUI(boolean value);

	public boolean getStartSnifferAgent();
	public void setStartSnifferAgent(boolean value);
	
	public boolean getStartInspectorAgent();
	public void setStartInspectorAgent(boolean value);

	public int getNumberOfRuns();
	public void setNumberOfRuns(int value);

	public ArrayList<? extends GeneralAgentInterface> getAgentList();

}
