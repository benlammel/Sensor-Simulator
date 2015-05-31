package it.unicam.sensorsimulator.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import it.unicam.sensorsimulator.StartEnvironment;
import it.unicam.sensorsimulator.interfaces.AbstractReportPane;
import it.unicam.sensorsimulator.interfaces.PluginInterface;
import it.unicam.sensorsimulator.interfaces.ReportInterface;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.logging.LogFileHandler;
import it.unicam.sensorsimulator.plugin.PluginHandler;
import it.unicam.sensorsimulator.simulationcontroller.SimulationController;
import it.unicam.sensorsimulator.simulationcontroller.xml.SerializationTools;
import it.unicam.sensorsimulator.ui.dialogs.GeneralDialogHandler;
import it.unicam.sensorsimulator.ui.modelling.Modeller;
import it.unicam.sensorsimulator.ui.reporting.ReportViewer;
import it.unicam.sensorsimulator.ui.ressources.SimulationResourcesAndProperties;
import it.unicam.sensorsimulator.ui.startdialog.SimulationStartDiolg;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ApplicationFrame extends BorderPane {
	
	private StartEnvironment startEnvironment;
	private Modeller modeller;
	private SimulationEnvironmentMode mode;
	private SimulationStartDiolg startDialogHandler;
	private GeneralDialogHandler generalDialogHandler;
	private PluginHandler pluginHandler;
	
	private SimulationRunInterface simulationRunFile;
	private LogFileHandler log;
	private ArrayList<ReportViewer> reportViewers;
	private Dialog<SimulationEnvironmentMode> simulationProgressDialog;
	
	public ApplicationFrame(StartEnvironment startEnvironment){
		this.startEnvironment = startEnvironment;
		generalDialogHandler = new GeneralDialogHandler(this);
		pluginHandler = new PluginHandler(this, generalDialogHandler);
		log = LogFileHandler.getInstance();
		
		modeller = new Modeller(this);
		this.setCenter(modeller);
		
		startDialogHandler = new SimulationStartDiolg(this, pluginHandler);
		setSimulationEnvironmentMode(SimulationEnvironmentMode.MODELLING);
	}

	public void setSimulationEnvironmentMode(SimulationEnvironmentMode mode) {
		this.mode = mode;
		switch(mode){
		case MODELLING:
			break;
		case SIMULATION_IN_PROGRESS:
			startProgressDialog(true);
			break;
		case SIMULATION_CANCELED:
			startProgressDialog(false);
			break;
		case SIMULATION_COMPLETED:
			startProgressDialog(false);
			break;
		}
	}
	
	private void startProgressDialog(boolean b) {
		if(b){
			simulationProgressDialog = generalDialogHandler.createProgressDialog();
			Optional<SimulationEnvironmentMode> result = simulationProgressDialog.showAndWait();
			try{
				//abort
				startEnvironment.getSimulationController().abortOngoingSimulation();
			}catch(NoSuchElementException e){
			}
		}else{
			simulationProgressDialog.close();
		}
	}

	public SimulationResourcesAndProperties getRessourcesAndProperties() {
		return startEnvironment.getRessourcesAndProperties();
	}

	public SimulationEnvironmentMode getSimulationEnvironmentMode() {
		return mode;
	}

	public PluginHandler getPluginHandler() {
		return pluginHandler;
	}

	public Modeller getModeller() {
		return modeller;
	}

	public GeneralDialogHandler getGeneralDialogHandler() {
		return generalDialogHandler;
	}

	public Window getMainScene() {
		return startEnvironment.getMainStage();
	}

	public void setCurrentSimulationFile(SimulationRunInterface simulationRunFile) {
		this.simulationRunFile = simulationRunFile;
	}

	public SimulationRunInterface getCurrentSimulationFile() {
		return simulationRunFile;
	}

	public SimulationController getSimulationController() {
		return startEnvironment.getSimulationController();
	}

	public SimulationStartDiolg getStartDialogHandler() {
		return startDialogHandler;
	}

	public void closeSubStages() {
		if(reportViewers != null && !reportViewers.isEmpty()){
			for(Stage stage : reportViewers){
				System.out.println("close " +stage);
				stage.hide();
			}	
		}
	}

	public void closeSubStage(ReportViewer reportViewer) {
		reportViewers.remove(reportViewer);
	}
	
	private void addAndCreateReport(Class<?> reportingPane,
			ReportInterface report) {
		AbstractReportPane pane = null;
		try {
			pane = (AbstractReportPane) reportingPane.newInstance();
			pane.setReport(report);
			createReportViewer(pane);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void addAndCreateReport(ReportInterface report) {
		AbstractReportPane pane = null;
		try {
			pane = (AbstractReportPane) pluginHandler.getCurrentPlugin().getReportingPane().newInstance();
			pane.setReport(report);
			createReportViewer(pane);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void createReportViewer(AbstractReportPane reportPane) {
		if(reportViewers==null){
			this.reportViewers = new ArrayList<ReportViewer>();
			System.out.println("reportViewers created " +reportViewers.size());
		}
		ReportViewer stage = new ReportViewer(this, reportPane);
		stage.setTitle(getRessourcesAndProperties().getReportingViewerHeader());
		stage.centerOnScreen();
		stage.setWidth(reportPane.getWindowWidth());
		stage.setHeight(reportPane.getWindowHeight());
		reportViewers.add(stage);
        stage.show();
	}
	
	public void showReportFromFile(File file, PluginInterface plugin) {
		if (file != null) {
			try {
				addAndCreateReport(plugin.getReportingPane(), SerializationTools.loadXMLReportFile(file, plugin.getReportClass()));
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
	}
}
