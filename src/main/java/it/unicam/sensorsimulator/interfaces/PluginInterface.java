package it.unicam.sensorsimulator.interfaces;

import javafx.scene.Parent;

public interface PluginInterface {
	
    public String getPluginName();
    
    public String getPluginVersion();
    
    public Parent getSettingsDialogContent();

	public void refreshSettingsDialogContent();

	public SimulationRunInterface generateAndReturnSimulationRunFile();

	public Class<?> getSimulationRunFileClass();

	public void updateSimulationRunFile(SimulationRunInterface simulationRunFile);

	public void setSimulationEnvironmentServiceClass(SimulationEnvironmentServices environmentServices);
	
	public Class<?> getSimulationCoordinatorAgent();

//	public AbstractReportPane getReportingPane();
	public Class<?> getReportingPane();

	
	public Class<?> getReportClass();
 
}
