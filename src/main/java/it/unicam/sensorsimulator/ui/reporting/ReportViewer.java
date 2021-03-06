package it.unicam.sensorsimulator.ui.reporting;

import it.unicam.sensorsimulator.interfaces.AbstractReportPane;
import it.unicam.sensorsimulator.interfaces.PluginInterface;
import it.unicam.sensorsimulator.simulationcontroller.xml.SerializationTools;
import it.unicam.sensorsimulator.ui.ApplicationFrame;
import javafx.event.EventHandler;
import javafx.scene.Scene;
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
	private AbstractReportPane reportPane;

	public ReportViewer(ApplicationFrame applicationFrame, AbstractReportPane reportPane) {
		this.applicationFrame = applicationFrame;
		this.reportPane = reportPane;
		this.reportViewer = this;
		
		initFileChooser();
		
		layout = new BorderPane();
		layout.setTop(new ReportToolbar(this));
		this.setOnCloseRequest(onCloseRequest);
		this.setScene(new Scene(layout));
		layout.setCenter(reportPane);
	}

	EventHandler<WindowEvent> onCloseRequest = new EventHandler<WindowEvent>(){

		@Override
		public void handle(WindowEvent event) {
			applicationFrame.closeSubStage(reportViewer);
		}};
	

	public void buttonCommand(ReportButtons action) {
		switch(action){
		case SAVEFILE:
			saveToFile();
			break;
		default:
			break;
		}
	}
	
	private void initFileChooser() {
		fileChooser = new FileChooser();
	}

	private void saveToFile() {
		fileChooser.setTitle("Save Report to File");
		SerializationTools.saveToXML(fileChooser.showSaveDialog(this), reportPane.getReport());
	}

//	private void selectPluginDialog() {
//		Dialog dialog = applicationFrame.getGeneralDialogHandler().createPluginSelectionDialog();		
//		Optional<PluginInterface> result = dialog.showAndWait();
//		plugin = result.get();
//		loadFromFile();
//	}
	
//	private void loadFromFile() {
//		fileChooser.setTitle("Open Resource File");
//		openXMLFileHandling(fileChooser.showOpenDialog(applicationFrame
//				.getMainScene()), plugin.getReportClass());
//	}
	
//	private void openXMLFileHandling(File file, Class<?> class1) {
//		if (file != null) {
//			try {
//				addReport(SerializationTools.loadXMLReportFile(file, class1));
//			} catch (JAXBException e) {
//				applicationFrame.getGeneralDialogHandler().showDialog(DialogMessages.FILEDOESNOTFITPLUGIN);
//			}
//		}
//	}

//	public void addReport(ReportInterface reportFile) {
////		layout.setCenter(plugin.getReportingPane());
//		
//	}

	public void setPlugin(PluginInterface plugin) {
		this.plugin = plugin;
		
	}
}
