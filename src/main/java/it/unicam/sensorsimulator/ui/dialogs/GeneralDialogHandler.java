package it.unicam.sensorsimulator.ui.dialogs;

import it.unicam.sensorsimulator.interfaces.PluginInterface;
import it.unicam.sensorsimulator.ui.ApplicationFrame;
import it.unicam.sensorsimulator.ui.SimulationEnvironmentMode;
import it.unicam.sensorsimulator.ui.startdialog.PluginInterfaceCell;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import org.controlsfx.dialog.ProgressDialog;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import javafx.util.Pair;

public class GeneralDialogHandler {
	
	private ApplicationFrame applicationFrame;
	private Alert alert;

	public GeneralDialogHandler(ApplicationFrame applicationFrame) {
		this.applicationFrame = applicationFrame;
	}

	public void showDialog(DialogMessages dialog) {
		alert = new Alert(dialog.getType());
		alert.setTitle("Error Occured");
		alert.setHeaderText(dialog.getHeader());
		alert.setContentText(dialog.getMessage());
		alert.showAndWait();
	}

	public void showDialog(DialogMessages dialog, Exception e) {
		alert = new Alert(dialog.getType());
		alert.setTitle("Error Occured");
		alert.setHeaderText(dialog.getHeader());
		alert.setContentText(dialog.getMessage());

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
		
	}

	public int showIntegerInputDialog(DialogMessages dialog) {
		int value = 120;
		TextInputDialog tInputDialog = new TextInputDialog(Integer.toString(value));
		tInputDialog.setTitle("Input");
		tInputDialog.setHeaderText(dialog.getHeader());
		tInputDialog.setContentText(dialog.getMessage());
		
		Optional<String> result = tInputDialog.showAndWait();
		if (result.isPresent()){
			try	{
			   value = Integer.parseInt(result.get());
			   if(value<=1){
				   value = 120;
			   }
			}catch (NumberFormatException e){
			   value = 120;
			}
		}
		return value;
	}

//	public Optional<PluginInterface> showPluginSelectionDialog() {
////		ChoiceDialog<PluginInterface> dialog = new ChoiceDialog<>(applicationFrame.getPluginHandler().getCurrentPlugin(), applicationFrame.getPluginHandler().getPluginList());
////		dialog.setTitle("Choice Dialog");
////		dialog.setHeaderText("Look, a Choice Dialog");
////		dialog.setContentText("Choose your letter:");
//		return new PluginSelectionDialog(applicationFrame).showAndWait();
//	}
	
//	public Optional<PluginInterface> showPluginSelectionDialog() {
//		ChoiceDialog<PluginInterface> dialog = new ChoiceDialog<>(applicationFrame.getPluginHandler().getCurrentPlugin(), applicationFrame.getPluginHandler().getPluginList());
//		dialog.setTitle("Choice Dialog");
//		dialog.setHeaderText("Look, a Choice Dialog");
//		dialog.setContentText("Choose your letter:");
//
//		return dialog.showAndWait();
//	}
	
	
	
	public Dialog<PluginInterface> createPluginSelectionDialog() {
		
		Dialog<PluginInterface> dialogWdw = new Dialog<PluginInterface>();
		dialogWdw.setTitle("Plugin Selection");
		dialogWdw.setHeaderText("Select the plugin for the to be opened report");
		
		ButtonType btnOK = new ButtonType("Ok", ButtonData.OK_DONE);
		dialogWdw.getDialogPane().getButtonTypes().addAll(btnOK, ButtonType.CANCEL);

		ComboBox<PluginInterface> cboPlugins = new ComboBox(FXCollections.observableArrayList(applicationFrame.getPluginHandler().getPluginList()));
		cboPlugins.setButtonCell(new PluginInterfaceCell());
		cboPlugins.setCellFactory(new Callback<ListView<PluginInterface>, ListCell<PluginInterface>>() {
			@Override
			public ListCell<PluginInterface> call(
					ListView<PluginInterface> p) {
				return new PluginInterfaceCell();
			}
		});
		
		cboPlugins.getSelectionModel().select(applicationFrame.getPluginHandler().getCurrentPlugin());

		dialogWdw.getDialogPane().setContent(cboPlugins);
		
		dialogWdw.setResultConverter(dialogButton -> {
		    if (dialogButton == btnOK) {
		        return cboPlugins.getSelectionModel().getSelectedItem();
		    }else{
		    	return null;
		    }
		});
		
		return dialogWdw;
	}

	public Dialog<SimulationEnvironmentMode> createProgressDialog() {
		Dialog<SimulationEnvironmentMode> dialogWdw = new Dialog<SimulationEnvironmentMode>();
		dialogWdw.setTitle("Simulation in Progress");
		dialogWdw.setHeaderText("Please wait until the simulation has finished.");
		
		ButtonType btnCancel = new ButtonType("Abort", ButtonData.CANCEL_CLOSE);
		dialogWdw.getDialogPane().getButtonTypes().addAll(btnCancel);

		ProgressBar pb = new ProgressBar();

		dialogWdw.getDialogPane().setContent(pb);
		
		dialogWdw.setResultConverter(dialogButton -> {
		    if (dialogButton == btnCancel) {
		        return SimulationEnvironmentMode.SIMULATION_CANCELED;
		    }else{
		    	return null;
		    }
		});
		return dialogWdw;
	}
}
