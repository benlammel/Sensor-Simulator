package it.unicam.sensorsimulator.plugin.heedv2.reporting.gui;

import jade.util.leap.Collection;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import javafx.embed.swing.SwingNode;

public class OverlayNWGraphic extends SwingNode {
	
	private Graph<Integer, String> g;
	private Layout<Integer, String> layout;
	private HashMap<Integer, ArrayList<Integer>> networkPicture;
	
	public OverlayNWGraphic(){
		g = new DirectedSparseMultigraph<Integer, String>();
		networkPicture = new HashMap<Integer, ArrayList<Integer>>();
	}

	public OverlayNWGraphic(HashMap<Integer, ArrayList<Integer>> networkPicture, int width, int height) {
		this();
		this.networkPicture = networkPicture;
		
		generateVertexts(generateListOfNodes());
		generateEdges();
		
		Dimension preferredSize = new Dimension(width-30, 400);
		
		layout = new FRLayout<Integer, String>(g);
		layout.setSize(preferredSize);
		final VisualizationModel<Integer, String> visualizationModel = 	new DefaultVisualizationModel<Integer, String>(layout, preferredSize);
		BasicVisualizationServer<Integer, String> vv = new VisualizationViewer<Integer, String>(visualizationModel, preferredSize);
		
		float dash[] = { 10.0f };
		final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		
		Transformer<Integer, Paint> vertexPaint = new Transformer<Integer, Paint>() {
			public Paint transform(Integer i) {
				if(networkPicture.keySet().contains(i)){
					return Color.LIGHT_GRAY;
				}else{
					return Color.GREEN;
				}
			}
		};
		Transformer<String, Stroke> edgeStrokeTransformer = new Transformer<String, Stroke>() {
			public Stroke transform(String s) {
				return edgeStroke;
			}
		};
		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		this.setContent(vv);
	}

	private void generateEdges() {
		for(Entry<Integer, ArrayList<Integer>> cluster : networkPicture.entrySet()){
			for(int successor : cluster.getValue()){
				g.addEdge(Integer.toString(cluster.getKey()) + "-"+ Integer.toString(successor), successor, cluster.getKey(), EdgeType.DIRECTED);
				System.out.println("\t:" +cluster.getKey() +"-" +successor);
			}
		}
	}

	private void generateVertexts(ArrayList<Integer> nodeList) {
		for(Integer node : nodeList){
			g.addVertex(node);
			System.out.println("\t:" +node);
		}
	}

	private ArrayList<Integer> generateListOfNodes() {
		ArrayList<Integer> nodeList = new ArrayList<Integer>();
		nodeList.addAll(networkPicture.keySet());
		for(Entry<Integer, ArrayList<Integer>> cluster : networkPicture.entrySet()){
			for(int successor : cluster.getValue()){
				if(!nodeList.contains(successor)){
					nodeList.add(successor);
				}
			}
		}
		Collections.sort(nodeList);
		System.out.println(nodeList);
		return nodeList;
	}
}
