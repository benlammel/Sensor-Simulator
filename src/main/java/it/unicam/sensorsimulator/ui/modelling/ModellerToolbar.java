package it.unicam.sensorsimulator.ui.modelling;

import it.unicam.sensorsimulator.interfaces.PluginInterface;
import it.unicam.sensorsimulator.logging.LogFileHandler;
import it.unicam.sensorsimulator.ui.startdialog.PluginInterfaceCell;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class ModellerToolbar extends ToolBar {

	private Modeller modeller;
	private ComboBox<PluginInterface> cboPlugins;

	private ColorPicker colorPicker;
	private LogFileHandler log;
	

	private ModellerButton btnLoadFile, btnSaveFile, btnClearPanel, btnStartSimulation, btnStopSimulation, btnAddAgent, btnRandomgeneration;
	
	public ModellerToolbar(Modeller modeller) {
		log = LogFileHandler.getInstance();
		this.modeller = modeller;

		fillComboBoxWithPlugins();
		
		btnLoadFile = new ModellerButton(ModellerButtons.LOADFILE, modeller);
		btnSaveFile = new ModellerButton(ModellerButtons.SAVEFILE, modeller);
		btnClearPanel = new ModellerButton(ModellerButtons.CLEARPANEL, modeller);
		btnStartSimulation =  new ModellerButton(ModellerButtons.STARTSIMULATION, modeller);
		btnStopSimulation =  new ModellerButton(ModellerButtons.STOPSIMULATION, modeller);
		btnAddAgent = new ModellerButton(ModellerButtons.ADDAGENT, modeller);
		btnRandomgeneration = new ModellerButton(ModellerButtons.RANDOMAGENTGENERATION, modeller);

		getItems().addAll(btnLoadFile,
				btnSaveFile,
				new Separator(),
				btnClearPanel,
				new Separator(), createColorPicker(), new Separator(),
				btnStartSimulation,
				btnStopSimulation,
				new Separator(), cboPlugins, new Separator(),
				btnAddAgent,
				btnRandomgeneration);
	}

	private Node createColorPicker() {
		colorPicker = new ColorPicker(modeller.getRessourcesAndProperties()
				.getDrawPanelBackgroundColor());
		colorPicker.setOnAction(new EventHandler() {
			public void handle(Event t) {
				modeller.getDrawPanel().setBackgroundColor(
						colorPicker.getValue());
			}
		});
		return new HBox(colorPicker);
	}

	private void fillComboBoxWithPlugins() {
		cboPlugins = new ComboBox(FXCollections.observableArrayList(modeller
				.getPluginHandler().getPluginList()));
		cboPlugins.setButtonCell(new PluginInterfaceCell());

		cboPlugins.getSelectionModel().select(0);
		modeller.getPluginHandler().setCurrentPlugin(
				cboPlugins.getSelectionModel().getSelectedItem());

		cboPlugins
				.setCellFactory(new Callback<ListView<PluginInterface>, ListCell<PluginInterface>>() {
					@Override
					public ListCell<PluginInterface> call(
							ListView<PluginInterface> p) {
						return new PluginInterfaceCell();
					}
				});
		cboPlugins.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<PluginInterface>() {
					@Override
					public void changed(
							ObservableValue<? extends PluginInterface> observable,
							PluginInterface oldValue, PluginInterface newValue) {
						modeller.getPluginHandler().setCurrentPlugin(newValue);
					}
				});
	}

	public void setModellerMode(ModellerMode mode) {
		switch(mode){
		case NOCONTENT:
			btnLoadFile.setDisable(false);
			btnSaveFile.setDisable(true);
			btnClearPanel.setDisable(true);
			btnStartSimulation.setDisable(true);
			btnStopSimulation.setDisable(true);
			btnAddAgent.setDisable(false);
			btnRandomgeneration.setDisable(false);
			break;
		case CONTENT:
			btnLoadFile.setDisable(false);
			btnSaveFile.setDisable(false);
			btnClearPanel.setDisable(false);
			btnStartSimulation.setDisable(false);
			btnStopSimulation.setDisable(true);
			btnAddAgent.setDisable(false);
			btnRandomgeneration.setDisable(false);
			break;
		default:
			break;
		}
	}

}
