package it.unicam.sensorsimulator.ui.reporting;

import java.io.File;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import it.unicam.sensorsimulator.interfaces.AbstractReportPane;
import it.unicam.sensorsimulator.interfaces.PluginInterface;
import it.unicam.sensorsimulator.interfaces.ReportInterface;
import it.unicam.sensorsimulator.simulationcontroller.xml.SerializationTools;
import it.unicam.sensorsimulator.ui.ApplicationFrame;
import it.unicam.sensorsimulator.ui.dialogs.DialogMessages;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ReportViewer extends Stage {
	
	private ApplicationFrame applicationFrame;
	private ReportViewer reportViewer;
	private BorderPane layout;
	
	private FileChooser fileChooser;
	private PluginInterface plugin;

	public ReportViewer(ApplicationFrame applicationFrame, AbstractReportPane reportPane) {
		this.applicationFrame = applicationFrame;
		this.reportViewer = this;
		this.setOnCloseRequest(onCloseRequest);
		
		layout = new BorderPane();
		layout.setTop(new ReportToolbar(this));
		layout.setCenter(reportPane);		
		this.setScene(new Scene(layout));
	}

	EventHandler<WindowEvent> onCloseRequest = new EventHandler<WindowEvent>(){

		@Override
		public void handle(WindowEvent event) {
			applicationFrame.closeSubStage(reportViewer);
		}};
	

	public void buttonCommand(ReportButtons action) {
		switch(action){
		case LOADFILE:
			selectPluginDialog();
			break;
		case SAVEFILE:
			break;
		default:
			break;
		}
	}

	private void selectPluginDialog() {
		Dialog dialog = applicationFrame.getGeneralDialogHandler().createPluginSelectionDialog();		
		Optional<PluginInterface> result = dialog.showAndWait();
		plugin = result.get();
		loadFromFile();
	}
	
	private void loadFromFile() {
		fileChooser.setTitle("Open Resource File");
		openXMLFileHandling(fileChooser.showOpenDialog(applicationFrame
				.getMainScene()), plugin.getReportClass());
	}
	
	private void openXMLFileHandling(File file, Class<?> class1) {
		if (file != null) {
			try {
				addReport(SerializationTools.loadXMLReportFile(file, class1));
			} catch (JAXBException e) {
				applicationFrame.getGeneralDialogHandler().showDialog(DialogMessages.FILEDOESNOTFITPLUGIN);
			}
		}
	}

	public void addReport(ReportInterface reportFile) {
//		layout.setCenter(plugin.getReportingPane());
		
	}

	public void setPlugin(PluginInterface plugin) {
		this.plugin = plugin;
		
	}
}
