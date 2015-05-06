package it.unicam.sensorsimulator.ui.tabmenu.tabs;

import javafx.scene.Node;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

public enum TabButtons {
	LOADFILE("", "load scenario from file", new Glyph("FontAwesome", FontAwesome.Glyph.FOLDER_OPEN_ALT)),
	STARTSIMULATION("", "start simulation", new Glyph("FontAwesome", FontAwesome.Glyph.PLAY)),
	PAUSESIMULATION("", "pause simulation", new Glyph("FontAwesome", FontAwesome.Glyph.PAUSE)),
	STOPSIMULATION("", "stop simulation", new Glyph("FontAwesome", FontAwesome.Glyph.STOP)),
	SAVEFILE("", "save scenario", new Glyph("FontAwesome", FontAwesome.Glyph.SAVE)),
	CLEARPANEL("", "clear panel", new Glyph("FontAwesome", FontAwesome.Glyph.TRASH_ALT)),
	ADDAGENT("", "add agent", new Glyph("FontAwesome", FontAwesome.Glyph.CIRCLE_ALT));
	
	private String buttonText;
	private String tooltip;
	private Node graphic;
	
	TabButtons(String buttonText, String tooltip, Node graphic) {
		this.buttonText = buttonText;
		this.tooltip = tooltip;
		this.graphic = graphic;
	}

	public Node getGraphic() {
		return graphic;
	}

	public String getButtonText() {
		return buttonText;
	}

	public String getTooltip() {
		return tooltip;
	}

}
