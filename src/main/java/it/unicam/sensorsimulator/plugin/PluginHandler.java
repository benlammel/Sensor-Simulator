package it.unicam.sensorsimulator.plugin;

import it.unicam.sensorsimulator.interfaces.GeneralAgentInterface;
import it.unicam.sensorsimulator.interfaces.LogFileWriterInterface;
import it.unicam.sensorsimulator.interfaces.PluginInterface;
import it.unicam.sensorsimulator.interfaces.SimulationEnvironmentServices;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.logging.LogFileHandler;
import it.unicam.sensorsimulator.plugin.example.ExamplePlugin;
import it.unicam.sensorsimulator.plugin.heed.HeedPlugin;
import it.unicam.sensorsimulator.plugin.heedv2.Heedv2Plugin;
import it.unicam.sensorsimulator.ui.ApplicationFrame;
import it.unicam.sensorsimulator.ui.dialogs.DialogMessages;
import it.unicam.sensorsimulator.ui.dialogs.GeneralDialogHandler;
import it.unicam.sensorsimulator.ui.startdialog.SimulationStartDiolg;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class PluginHandler implements SimulationEnvironmentServices {
	
	private ApplicationFrame applicationFrame;
	private GeneralDialogHandler generalDialogHandler;
	private LogFileWriterInterface log;
	private List<PluginInterface> pluginList;
	private PluginInterface currentPlugin;

	public PluginHandler(ApplicationFrame applicationFrame, GeneralDialogHandler generalDialogHandler){
		pluginList = new ArrayList<PluginInterface>();
		this.applicationFrame = applicationFrame;
		this.generalDialogHandler = generalDialogHandler;
		log = LogFileHandler.getInstance();

		//		initPlugins();
		
		registerPlugin(new HeedPlugin());
		registerPlugin(new Heedv2Plugin());
		registerPlugin(new ExamplePlugin());
	}
	
	private void registerPlugin(PluginInterface plugin) {
		this.pluginList.add(plugin);
	}

	/**
	 * This method loads all jar files in the folder /plugins/
	 * Since ServiceLoader can just invoke one class out of a plugin 
	 * jade fails to start the coordinator agent.
	 * As soon as this problem is solved, this method will be called in the
	 * constructor. 
	 */
	private void initPlugins() {
		log.debug(PluginHandler.class, "initiate plugins");
		try {
			pluginList = PluginTools.loadPlugins();

		} catch (MalformedURLException e) {
			log.catching(e);
			generalDialogHandler.showDialog(DialogMessages.PLUGINLOADPROBLEM);
		}
		log.debug(SimulationStartDiolg.class, "Number of plugins loaded: " +pluginList.size());
	}

	public List<PluginInterface> getPluginList() {
		return pluginList;
	}

	public void setCurrentPlugin(PluginInterface plugin) {
		this.currentPlugin = plugin;
		currentPlugin.setSimulationEnvironmentServiceClass(this);
	}

	public PluginInterface getCurrentPlugin() {
		return currentPlugin;
	}

	@Override
	public ArrayList<GeneralAgentInterface> getAgentList() {
		return applicationFrame.getModeller().getListOfAgents();
	}

	@Override
	public LogFileWriterInterface getLogger() {
		return log;
	}

	public void refreshPluginUI() {
		currentPlugin.refreshSettingsDialogContent();
	}

	public SimulationRunInterface generateAndReturnSimulationRunFile() {
		return currentPlugin.generateAndReturnSimulationRunFile();
	}

	public Class<?> getSimulationRunFileClass() {
		return currentPlugin.getSimulationRunFileClass();
	}

	public void updateRunFile(SimulationRunInterface simulationRun) {
		currentPlugin.updateSimulationRunFile(simulationRun);
	}

	public Class<?> getSimulationCoordinatorAgent() {
		return currentPlugin.getSimulationCoordinatorAgent();
	}

	public Class<?> getReportingPane() {
		return currentPlugin.getReportingPane();
	}
}
