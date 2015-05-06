package it.unicam.sensorsimulator.ui.tabmenu;

import it.unicam.sensorsimulator.ui.ApplicationFrame;
import it.unicam.sensorsimulator.ui.SimulationEnvironmentMode;
import it.unicam.sensorsimulator.ui.tabmenu.tabs.DrawTab;
import it.unicam.sensorsimulator.ui.tabmenu.tabs.ReportingTab;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabMenu extends TabPane implements ChangeListener<Tab> {
	
	private ApplicationFrame applicationFrame;
	private DrawTab drawTab;
	private ReportingTab reportingTab;
	
	public TabMenu(ApplicationFrame applicationFrame) {
		this.applicationFrame = applicationFrame;
		
		drawTab = new DrawTab(applicationFrame);
		this.getTabs().add(drawTab);
		
		reportingTab = new ReportingTab(applicationFrame);
		this.getTabs().add(reportingTab);
		
		this.getSelectionModel().selectedItemProperty().addListener(this);
	}

	@Override
	public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
		if(t1.equals(drawTab)){
			applicationFrame.setSimulationEnvironmentMode(SimulationEnvironmentMode.DRAWTAB);
		}else if(t1.equals(reportingTab)){
			applicationFrame.setSimulationEnvironmentMode(SimulationEnvironmentMode.REPORTINGTAB);
		}
	}
}