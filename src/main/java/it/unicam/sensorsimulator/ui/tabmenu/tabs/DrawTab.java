package it.unicam.sensorsimulator.ui.tabmenu.tabs;

import it.unicam.sensorsimulator.interfaces.PluginInterface;
import it.unicam.sensorsimulator.logging.LogFileHandler;
import it.unicam.sensorsimulator.ui.ApplicationFrame;
import it.unicam.sensorsimulator.ui.startdialog.PluginInterfaceCell;
import it.unicam.sensorsimulator.ui.tabmenu.customizedobjects.buttons.ToolbarButton;
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
import javafx.scene.control.Tab;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class DrawTab extends Tab {
	
	private ApplicationFrame applicationFrame;
	private final String TABTEXT = "Draw";
	private ToolBar toolbar;
	private ColorPicker colorPicker;
	private LogFileHandler log;
	
	private ComboBox<PluginInterface> cboPlugins;
	
	public DrawTab(ApplicationFrame applicationFrame){
		log = LogFileHandler.getInstance();

		this.applicationFrame = applicationFrame;
		this.setText(TABTEXT);
		this.setClosable(false);
		
		fillComboBoxWithPlugins();
		
		this.setContent(createToolbar());
	}

	private ToolBar createToolbar() {
		toolbar = new ToolBar();

		toolbar.getItems().addAll(
				new ToolbarButton(TabButtons.LOADFILE, applicationFrame),
				new ToolbarButton(TabButtons.SAVEFILE, applicationFrame),
				new Separator(),
				new ToolbarButton(TabButtons.CLEARPANEL, applicationFrame),
				new Separator(),
				createColorPicker(),
				new Separator(),
				new ToolbarButton(TabButtons.STARTSIMULATION, applicationFrame),
				new ToolbarButton(TabButtons.STOPSIMULATION, applicationFrame),
				new Separator(),
				cboPlugins,
				new Separator(),
				new ToolbarButton(TabButtons.ADDAGENT, applicationFrame),
				new ToolbarButton(TabButtons.RANDOMAGENTGENERATION, applicationFrame));
		return toolbar;
	}

	private Node createColorPicker() {
		colorPicker = new ColorPicker(applicationFrame.getRessourcesAndProperties().getDrawPanelBackgroundColor());
		colorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
            	applicationFrame.getDrawPanel().setBackgroundColor(colorPicker.getValue());
            }
        });
		return new HBox(colorPicker);
	}
	
	private void fillComboBoxWithPlugins() {
		cboPlugins = new ComboBox(FXCollections.observableArrayList(applicationFrame.getPluginHandler().getPluginList()));
		cboPlugins.setButtonCell(new PluginInterfaceCell());

		cboPlugins.getSelectionModel().select(0);
		applicationFrame.getPluginHandler().setCurrentPlugin(cboPlugins.getSelectionModel().getSelectedItem());
		
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
						log.trace(DrawTab.class, "selected plugin: " +newValue.getPluginName() +" version: " +newValue.getPluginVersion());
						applicationFrame.getPluginHandler().setCurrentPlugin(newValue);
					}
				});
}
}
