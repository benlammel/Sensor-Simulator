package it.unicam.sensorsimulator.plugin.heed.reporting;

import it.unicam.sensorsimulator.plugin.heed.reporting.graphic.NetworkGraphic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javafx.concurrent.Task;

public class GeneratePicturesTask extends Task<HashMap<Integer, NetworkGraphic>> {

	private Set<Integer> nodesList;
	private HashMap<Integer, ClusterPicture> networkView;
	private int pictureWidth;
	private int pictureHeight;
	private ArrayList<Integer> clusterHeads;
	
	public GeneratePicturesTask(int pictureWidth, int pictureHeight, Set<Integer> nodesList,
			HashMap<Integer, ClusterPicture> networkView) {
		this.pictureWidth = pictureWidth;
		this.pictureHeight = pictureHeight;
		this.nodesList = nodesList;
		this.networkView = networkView;
		this.clusterHeads = new ArrayList<Integer>();
	}

	@Override
	protected HashMap<Integer, NetworkGraphic> call() throws Exception {
		
		HashMap<Integer, NetworkGraphic> pictures = new HashMap<Integer, NetworkGraphic>();
		for(int i = 0; i<networkView.size(); i++){
//			Thread.sleep(300);
			clusterHeads.add(networkView.get(i).getClusterHead());
			updateProgress(i + 1, networkView.size());
			pictures.put(i, new NetworkGraphic(pictureWidth, pictureHeight, nodesList, clusterHeads, i, networkView));
//			System.out.println(i + 1 +" :: " +size);
		}
		return pictures;
	}

}
