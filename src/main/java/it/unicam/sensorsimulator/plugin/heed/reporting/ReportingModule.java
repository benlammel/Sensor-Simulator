package it.unicam.sensorsimulator.plugin.heed.reporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import it.unicam.sensorsimulator.plugin.heed.HeedPlugin;
import javafx.scene.control.Accordion;

public class ReportingModule extends Accordion {
	
	private HeedPlugin heedPlugin;
	
	private ProtocolReportHandler protocol;

	public ReportingModule(HeedPlugin heedPlugin){
		this.heedPlugin = heedPlugin;
		protocol = new ProtocolReportHandler();
		
		this.getPanes().add(protocol);
        this.setExpandedPane(protocol);
	}

	public void addProtocolMeasurement(int clusterHead,
			ArrayList<Integer> clusterMembers) {
//		protocol.updateNetworkView(clusterHead, clusterMembers);
	}

	public void addNodes(Set<Integer> keySet) {
//		protocol.setNodes(keySet);
	}

	public void addRunResults(HashMap<Integer, RunResults> runResultList) {
		System.out.println("Number of Runs recorded: " +runResultList.size());
	}
}
