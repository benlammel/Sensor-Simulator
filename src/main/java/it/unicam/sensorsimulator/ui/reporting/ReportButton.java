package it.unicam.sensorsimulator.ui.reporting;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

public class ReportButton extends Button implements EventHandler<ActionEvent> {
	
	private ReportButtons action;
	private ReportViewer reportViewer;

	public ReportButton(ReportButtons btn, ReportViewer reportViewer) {
		super(btn.getButtonText(), btn.getGraphic());
		this.action = btn;
		this.reportViewer = reportViewer;
		this.setTooltip(new Tooltip(btn.getTooltip()));
		this.setOnAction(this);		
	}

	public void handle(ActionEvent event) {
		reportViewer.buttonCommand(action);
	}

	public ReportButtons getButtonAction() {
		return action;
	}

}
