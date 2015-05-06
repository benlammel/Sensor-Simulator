package it.unicam.sensorsimulator.ui.tabmenu.customizedobjects.buttons;

import it.unicam.sensorsimulator.ui.ApplicationFrame;
import it.unicam.sensorsimulator.ui.tabmenu.tabs.TabButtons;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

public class ToolbarButton extends Button implements EventHandler<ActionEvent> {
	
	private ApplicationFrame applicationFrame;
	private TabButtons action;
	
	public ToolbarButton(TabButtons btn, ApplicationFrame applicationFrame) {
		super(btn.getButtonText(), btn.getGraphic());
		this.action = btn;
		this.applicationFrame = applicationFrame;
		this.setTooltip(new Tooltip(btn.getTooltip()));
		this.setOnAction(this);		
	}

	public void handle(ActionEvent event) {
		applicationFrame.buttonCommand(action);
	}

	public TabButtons getButtonAction() {
		return action;
	}

}
