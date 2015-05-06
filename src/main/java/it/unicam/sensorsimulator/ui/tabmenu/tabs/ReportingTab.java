package it.unicam.sensorsimulator.ui.tabmenu.tabs;

import it.unicam.sensorsimulator.ui.ApplicationFrame;
import javafx.scene.control.Tab;

public class ReportingTab extends Tab {

	private final String TABTEXT = "Reporting";
	
	public ReportingTab(ApplicationFrame applicationFrame) {
		this.setText(TABTEXT);
		this.setClosable(false);
	}

}
