package it.unicam.sensorsimulator.plugin.example;

import java.util.ArrayList;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;

public class ExampleSimulationRunFile implements SimulationRunInterface {

	public ExampleSimulationRunFile() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean getStartMASObservationUI() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setStartMASObservationUI(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getStartSnifferAgent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setStartSnifferAgent(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getStartInspectorAgent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setStartInspectorAgent(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNumberOfRuns() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setNumberOfRuns(int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<? extends GeneralAgentInterface> getAgentList() {
		// TODO Auto-generated method stub
		return null;
	}

}
