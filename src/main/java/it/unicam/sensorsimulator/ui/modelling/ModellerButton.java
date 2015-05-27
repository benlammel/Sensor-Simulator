package it.unicam.sensorsimulator.ui.modelling;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

public class ModellerButton extends Button implements EventHandler<ActionEvent> {
	
	private Modeller modeller;
	private ModellerButtons action;
	
	public ModellerButton(ModellerButtons btn, Modeller modeller) {
		super(btn.getButtonText(), btn.getGraphic());
		this.action = btn;
		this.modeller = modeller;
		this.setTooltip(new Tooltip(btn.getTooltip()));
		this.setOnAction(this);		
	}

	public void handle(ActionEvent event) {
		modeller.buttonCommand(action);
	}

	public ModellerButtons getButtonAction() {
		return action;
	}

}
