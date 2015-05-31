package it.unicam.sensorsimulator.ui.reporting;

import javafx.scene.Node;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

public enum ReportButtons {
	SAVEFILE("", "save report", new Glyph("FontAwesome", FontAwesome.Glyph.SAVE));
	
	private String buttonText;
	private String tooltip;
	private Node graphic;
	
	ReportButtons(String buttonText, String tooltip, Node graphic) {
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
