package it.unicam.sensorsimulator.ui.ressources;

public enum PropertyValues {
	APPLICATIONWIDTH("applicationWidth", "800"),
	APPLICATIONHEIGHT("applicationHeight", "600"),
	DRAWPANELBACKGROUNDCOLOR("drawPanelBG", "#B0C4DE");
	
	private String tag;
	private String standardValue;
	
	private PropertyValues(String tag, String standardValue) {
		this.tag = tag;
		this.standardValue = standardValue;
	}

	public String getTag() {
		return tag;
	}

	public String getStandardValue() {
		return standardValue;
	}

}