package it.unicam.sensorsimulator.plugin.heed.reporting;

import it.unicam.sensorsimulator.plugin.heed.reporting.graphic.NetworkGraphic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javafx.concurrent.Task;

public class GraphicGenerationTask extends Task<NetworkGraphic> {
	
	private int taskID;
	private int pictureWidth;
	private int pictureHeight;
	private HashMap<Integer, ArrayList<Integer>> networkView;
	private Set<Integer> nodesList;

	public GraphicGenerationTask(int taskID, int pictureWidth, int pictureHeight, Set<Integer> nodesList, HashMap<Integer, ArrayList<Integer>> networkView) {
		this.taskID = taskID;
		this.pictureWidth = pictureWidth;
		this.pictureHeight = pictureHeight;
		this.nodesList = nodesList;
		this.networkView = networkView;
	}

	@Override
	protected NetworkGraphic call() throws Exception {
		return new NetworkGraphic(taskID, pictureWidth, pictureHeight, nodesList, networkView);
	}

}
