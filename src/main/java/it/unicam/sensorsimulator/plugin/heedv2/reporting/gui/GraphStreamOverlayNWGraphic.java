package it.unicam.sensorsimulator.plugin.heedv2.reporting.gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import javafx.embed.swing.SwingNode;

public class GraphStreamOverlayNWGraphic extends SwingNode {

	private Graph graph;
	private GraphStreamOverlayNWGraphic swingNode;
	
	public GraphStreamOverlayNWGraphic(ArrayList<Integer> clusterHeadList,
			ArrayList<Integer> successorList,
			HashMap<Integer, ArrayList<Integer>> networkPicture,
			int windowWidth, int windowHeight) {
		swingNode = this;
		
		String v = UUID.randomUUID().toString();
		System.out.println("************************* " +v);
		System.out.println("CH " +clusterHeadList);
		System.out.println("SU " +successorList);
		System.out.println("NP " +networkPicture.entrySet().toString());
		System.out.println("************************* ");
		
		graph = new SingleGraph(v);

		createNode(0);
		
		for(int ch : clusterHeadList){
			createNode(ch);
			connectNodes(ch, 0);
		}
		
		for(int member : successorList){
			createNode(member);
		}
		
		for(Entry<Integer, ArrayList<Integer>> cluster : networkPicture.entrySet()){
			for(int member : cluster.getValue()){
				connectNodes(cluster.getKey(), member);
			}
		}

        // create viewer
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_SWING_THREAD);
        viewer.enableAutoLayout();

        View view = viewer.addDefaultView(false);

		
        view.setPreferredSize(new Dimension(windowWidth, 500));
        swingNode.setContent(view);
	}

	private void connectNodes(Integer from, Integer to) {
		System.out.println("node connected: " +from +" - " +to);
		graph.addEdge(Integer.toString(from)+"->" + Integer.toString(to), Integer.toString(from), Integer.toString(to), false);
	}

	private void createNode(int id) {
		System.out.println("node created: " +id);
		Node node = graph.addNode(Integer.toString(id));
		node.addAttribute("label", Integer.toString(id));
	}
}
