package it.unicam.sensorsimulator.ui.startdialog;

import it.unicam.sensorsimulator.interfaces.PluginInterface;
import javafx.scene.control.ListCell;

public class PluginInterfaceCell extends ListCell<PluginInterface> {
	@Override
	protected void updateItem(PluginInterface item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null) {
			setText(item.getPluginName() + "; " + item.getPluginVersion());
		}
	}
}
