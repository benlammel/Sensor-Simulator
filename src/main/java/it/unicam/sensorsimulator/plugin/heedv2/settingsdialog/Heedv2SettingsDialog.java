package it.unicam.sensorsimulator.plugin.heedv2.settingsdialog;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import it.unicam.sensorsimulator.interfaces.SimulationRunInterface;
import it.unicam.sensorsimulator.plugin.heedv2.Heedv2Plugin;
import it.unicam.sensorsimulator.plugin.heedv2.Heedv2SimulationRunFile;
import it.unicam.sensorsimulator.plugin.heedv2.agent.config.HeedAgentConfiguration;

public class Heedv2SettingsDialog extends GridPane {
	
	private Heedv2Plugin heedv2Plugin;
	
	private Heedv2SimulationRunFile simulationRunFile;
	
	private CheckBox ckMASObservation;
	private CheckBox ckSniffer;
	private CheckBox ckInspector;
	
	private Slider sliderNoOfRuns;
	private Text txtNoOfRuns;
	private IntegerProperty sliderNoOfRunsValue = new SimpleIntegerProperty(1);
	
	private CheckBox ckRandomCost;

	public Heedv2SettingsDialog(Heedv2Plugin heedv2Plugin) {
		this.heedv2Plugin = heedv2Plugin;
		
		setHgap(10);
		setVgap(10);
		setPadding(new Insets(20, 150, 10, 10));

		add(new Label("Number of runs: "), 0, 0);
		add(setupNoOfRuns(), 1, 0);
		add(txtNoOfRuns, 2, 0);

		ckMASObservation = new CheckBox("Start MultiAgent Observation UI");
		ckMASObservation.setSelected(false);
		add(ckMASObservation, 0, 1);

		ckSniffer = new CheckBox("Start Sniffer");
		ckSniffer.setSelected(false);
		add(ckSniffer, 0, 2);

		ckInspector = new CheckBox("Start Inspector");
		ckInspector.setSelected(false);
		add(ckInspector, 0, 3);

		ckRandomCost = new CheckBox("Random Costs");
		ckRandomCost.setSelected(true);
		ckRandomCost.setDisable(true);
		add(ckRandomCost, 0, 4);
	}

	public void refreshSettingsDialogContent() {
		// TODO Auto-generated method stub
		
	}

	public SimulationRunInterface generateAndReturnSimulationRunFileInDialog() {
		getCurrentRunFile().setNumberOfRuns((int) sliderNoOfRuns.getValue());
		getCurrentRunFile().setStartInspectorAgent(ckInspector.isSelected());
		getCurrentRunFile().setStartMASObservationUI(ckMASObservation.isSelected());
		getCurrentRunFile().setStartSnifferAgent(ckSniffer.isSelected());

		loadAgents();
		
		return simulationRunFile;
	}
	
	private void loadAgents() {
		getCurrentRunFile().getAgentList().clear();
		
		for (HeedAgentConfiguration a : heedv2Plugin.getAgentList()) {
			simulationRunFile.addAgent(a);
		}
	}

	public void updateSimulationRunFile(SimulationRunInterface simulationRunFile) {
		this.simulationRunFile = (Heedv2SimulationRunFile) simulationRunFile;
	}
	
	private Slider setupNoOfRuns() {
		sliderNoOfRuns = new Slider(1, 10, getNumberOfRuns());
		sliderNoOfRuns.setShowTickLabels(true);
		sliderNoOfRuns.setShowTickMarks(true);
		sliderNoOfRuns.setMajorTickUnit(2);

		txtNoOfRuns = new Text(Integer.toString(getNumberOfRuns()));
		sliderNoOfRuns.valueProperty().bindBidirectional(sliderNoOfRunsValue);
		txtNoOfRuns.textProperty().bind(sliderNoOfRunsValue.asString());

		return sliderNoOfRuns;
	}
	
	public int getNumberOfRuns() {
		if (getCurrentRunFile().getNumberOfRuns() == 0) {
			return 1;
		} else {
			return getCurrentRunFile().getNumberOfRuns();
		}
	}
	
	private Heedv2SimulationRunFile getCurrentRunFile() {
		if (simulationRunFile == null) {
			simulationRunFile = new Heedv2SimulationRunFile();
		}
		return simulationRunFile;
	}
}
