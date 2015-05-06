package it.unicam.sensorsimulator.persistence;

public enum Folder {
	TEMPFOLDER("temp/"),
	PLUGINFOLDER("plugins/"),
	LOGS("logs/");
	
	String folderName;
	
	Folder(String folderName) {
		this.folderName = folderName;
	}
	
	public String toString(){
		return folderName;
	}
}
