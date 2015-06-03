package it.unicam.sensorsimulator.ui.ressources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javafx.scene.paint.Color;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFontRegistry;

public class SimulationResourcesAndProperties {
	
	private final String APPLICATIONHEADER = "Sensor Simulation Environment";
	private static final String REPORTVIEWERHEADER = "Report Viewer";
	private Properties properties;
	private final String FONTAWESOMETTFFILEPATH = "fonts/fontawesome-webfont.ttf";
	private String PROPERTIESFILENAME = "config.properties";
	
	public SimulationResourcesAndProperties(){
		loadFontAwesomeResource();
		properties = new Properties();
		loadPropertiesFile();
	}
	
	private void loadPropertiesFile() {
		InputStream is = null;
        try {
			is = new FileInputStream(new File(PROPERTIESFILENAME));
			properties.load(is);
		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			log.error(this.getClass(), "file " +PROPERTIESFILENAME +" could not be found.");
		} catch (IOException e) {
//			e.printStackTrace();
//			log.error(this.getClass(), "file " +PROPERTIESFILENAME +" could not be read.");
		}
	}

	private void loadFontAwesomeResource() {
		GlyphFontRegistry.register(new FontAwesome(getClass().getClassLoader().getResourceAsStream(FONTAWESOMETTFFILEPATH)));
	}

	public Color getDrawPanelBackgroundColor() {
		return Color.web(getStringProperty(PropertyValues.DRAWPANELBACKGROUNDCOLOR));
	}

	private String getStringProperty(PropertyValues key) {
		if(properties.getProperty(key.getTag())==null){
			setStandardPropertyValue(key);
		}
		return properties.getProperty(key.getTag());
	}
	
	private void setStandardPropertyValue(PropertyValues key) {
		properties.setProperty(key.getTag(), key.getStandardValue());
	}

	public String getDrawPanelBackgroundColorInHex() {
		return transformColorToHex(getDrawPanelBackgroundColor());
	}

	private String transformColorToHex(Color color) {
		return String.format( "#%02X%02X%02X",
	            (int)( color.getRed() * 255 ),
	            (int)( color.getGreen() * 255 ),
	            (int)( color.getBlue() * 255 ) );
	}

	public void setDrawPanelBackgroundColor(Color color) {
		setProperty(PropertyValues.DRAWPANELBACKGROUNDCOLOR, transformColorToHex(color));
	}
	
	private void setProperty(PropertyValues key, String value) {
		properties.setProperty(key.getTag(), value);
	}
	
	public void setApplicationWidth(Double value) {
		setProperty(PropertyValues.APPLICATIONWIDTH, value);
	}

	private void setProperty(PropertyValues key, Double value) {
		properties.setProperty(key.getTag(), Double.toString(value));
	}
	
	public void setApplicationHeight(Double value) {
		setProperty(PropertyValues.APPLICATIONHEIGHT, value);
	}
	
	public double getApplicationWidth() {
		return Double.parseDouble(getStringProperty(PropertyValues.APPLICATIONWIDTH));
	}
	
	public double getApplicationHeight() {
		return Double.parseDouble(getStringProperty(PropertyValues.APPLICATIONHEIGHT));
	}

	public String getApplicationHeader() {
		return APPLICATIONHEADER;
	}
	
	public String getReportViewerHeader() {
		return REPORTVIEWERHEADER;
	}

	public void saveToPropertiesFile() {
        OutputStream out;
        try {
        	out = new FileOutputStream(new File(PROPERTIESFILENAME));
			properties.store(out, "Properties File for "+APPLICATIONHEADER);
		}catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
