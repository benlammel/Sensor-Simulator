package it.unicam.sensorsimulator.plugin;

import org.apache.commons.collections4.IteratorUtils;

import it.unicam.sensorsimulator.interfaces.PluginInterface;
import it.unicam.sensorsimulator.logging.LogFileHandler;
import it.unicam.sensorsimulator.persistence.Folder;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.ServiceLoader;

public class PluginTools {
	
	private static URLClassLoader urlClassLoader;
	private static ServiceLoader<PluginInterface> serviceLoader;
	private static LogFileHandler log;

	public static List<PluginInterface> loadPlugins() throws MalformedURLException {
		log = LogFileHandler.getInstance();
		
		File loc = new File(Folder.PLUGINFOLDER.toString());
		
		File[] flist = loc.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.getPath().toLowerCase().endsWith(".jar");
			}
		});
		
		log.debug(PluginTools.class, "jar files found: " +flist.length);
		
		URL[] urls = new URL[flist.length];
		
		for (int i = 0; i < flist.length; i++){
			urls[i] = flist[i].toURI().toURL();
			}
		
		urlClassLoader = new URLClassLoader(urls);
		serviceLoader = ServiceLoader.load(PluginInterface.class, urlClassLoader);
		
		return IteratorUtils.toList(serviceLoader.iterator());
	}
}
