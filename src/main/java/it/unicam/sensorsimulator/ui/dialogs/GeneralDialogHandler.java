package it.unicam.sensorsimulator.ui.dialogs;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class GeneralDialogHandler {
	
	private Alert alert;

	public GeneralDialogHandler() {
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

}
