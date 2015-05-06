package it.unicam.sensorsimulator.persistence;

import it.unicam.sensorsimulator.logging.LogFileHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileTools {

	private static LogFileHandler log;

	public static void verifyFolderStructure(Folder[] values) {
		log = LogFileHandler.getInstance();
		try {
			for (Folder folder : values) {
				Files.createDirectories(Paths.get(folder.toString()));
			}
			log.trace(FileTools.class, "folders created successfully");
		} catch (IOException e) {
			log.catching(e);
		}
	}
}
